// items.js
// Requires api.js and auth.js

const ItemsAPI = {
  getLostItems: () => api.get('/lost-items'),
  getFoundItems: () => api.get('/found-items'),
  reportLostItem: (data) => api.post('/lost-items', data),
  reportFoundItem: (data) => api.post('/found-items', data),
};

// Generic function to render items in a table
function renderItemsTable(items, containerId, type) {
  const container = document.getElementById(containerId);
  if (!container) return;

  if (!items || items.length === 0) {
    container.innerHTML = `<div class="empty-state">No ${type} items to display right now.</div>`;
    return;
  }

  let html = `
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>Item Name</th>
            <th>Description</th>
            <th>Category</th>
            <th>Status</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
  `;

  items.forEach(item => {
    const badgeClass = item.status === 'FOUND' ? 'badge-found' : (item.status === 'LOST' ? 'badge-lost' : 'badge-pending');
    const displayStatus = item.status || type.toUpperCase();
    
    html += `
      <tr>
        <td><strong>${item.name || item.itemName || 'Unknown Item'}</strong></td>
        <td>${item.description || '-'}</td>
        <td>${item.category || item.itemCategory || 'General'}</td>
        <td><span class="badge ${badgeClass}">${displayStatus}</span></td>
        <td>${item.dateLost || item.dateFound || item.createdAt || new Date().toISOString().split('T')[0]}</td>
      </tr>
    `;
  });

  html += `
        </tbody>
      </table>
    </div>
  `;

  container.innerHTML = html;
}

// Load items based on page
document.addEventListener('DOMContentLoaded', async () => {
  // Always check auth on these protected pages
  if (window.location.pathname.includes('dashboard.html') || 
      window.location.pathname.includes('lost-items.html') || 
      window.location.pathname.includes('found-items.html')) {
    requireAuth();
  }

  // --- Display user info in nav ---
  const userStr = localStorage.getItem('user');
  if (userStr) {
    const user = JSON.parse(userStr);
    const userInfoEl = document.getElementById('userNameDisplay');
    if (userInfoEl) userInfoEl.textContent = `Hello, ${user.name || user.email}`;
  }

  // 1. Dashboard summary
  const dashboardLost = document.getElementById('dashboard-lost-items');
  const dashboardFound = document.getElementById('dashboard-found-items');
  if (dashboardLost || dashboardFound) {
    try {
      const [lost, found] = await Promise.all([
        ItemsAPI.getLostItems().catch(() => []),
        ItemsAPI.getFoundItems().catch(() => [])
      ]);
      if (dashboardLost) renderItemsTable(lost.slice(0, 5), 'dashboard-lost-items', 'lost'); // Show max 5
      if (dashboardFound) renderItemsTable(found.slice(0, 5), 'dashboard-found-items', 'found');
    } catch (e) {
      console.error(e);
    }
  }

  // 2. Lost Items Page
  const lostItemsList = document.getElementById('lost-items-list');
  if (lostItemsList) {
    try {
      const items = await ItemsAPI.getLostItems();
      renderItemsTable(items, 'lost-items-list', 'lost');
    } catch (e) {
      console.error(e);
    }
  }

  // 3. Found Items Page
  const foundItemsList = document.getElementById('found-items-list');
  if (foundItemsList) {
    try {
      const items = await ItemsAPI.getFoundItems();
      renderItemsTable(items, 'found-items-list', 'found');
    } catch (e) {
      console.error(e);
    }
  }

  // --- Form submissions ---
  const lostItemForm = document.getElementById('lostItemForm');
  if (lostItemForm) {
    lostItemForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const name = document.getElementById('itemName').value;
      const description = document.getElementById('itemDescription').value;
      const category = document.getElementById('itemCategory').value;
      
      const submitBtn = document.getElementById('submitLostBtn');
      submitBtn.disabled = true;

      try {
        await ItemsAPI.reportLostItem({ name, description, category });
        showToast('Lost item reported successfully', 'success');
        lostItemForm.reset();
        
        // Refresh list
        const items = await ItemsAPI.getLostItems();
        renderItemsTable(items, 'lost-items-list', 'lost');
      } catch (err) {
        showToast(err.message, 'error');
      } finally {
        submitBtn.disabled = false;
      }
    });
  }

  const foundItemForm = document.getElementById('foundItemForm');
  if (foundItemForm) {
    foundItemForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const name = document.getElementById('itemName').value;
      const description = document.getElementById('itemDescription').value;
      const category = document.getElementById('itemCategory').value;
      
      const submitBtn = document.getElementById('submitFoundBtn');
      submitBtn.disabled = true;

      try {
        await ItemsAPI.reportFoundItem({ name, description, category });
        showToast('Found item reported successfully', 'success');
        foundItemForm.reset();
        
        // Refresh list
        const items = await ItemsAPI.getFoundItems();
        renderItemsTable(items, 'found-items-list', 'found');
      } catch (err) {
        showToast(err.message, 'error');
      } finally {
        submitBtn.disabled = false;
      }
    });
  }

});
