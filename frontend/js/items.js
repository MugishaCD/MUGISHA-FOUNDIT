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
    const itemName = item.item ? item.item.name : (item.name || item.itemName || 'Unknown Item');
    const itemDesc = item.item ? item.item.description : (item.description || '-');
    const itemCat = item.item ? item.item.category : (item.category || item.itemCategory || 'General');
    const badgeClass = item.status === 'FOUND' ? 'badge-found' : (item.status === 'LOST' ? 'badge-lost' : 'badge-pending');
    const displayStatus = item.status || type.toUpperCase();
    
    html += `
      <tr>
        <td><strong>${itemName}</strong></td>
        <td>${itemDesc}</td>
        <td>${itemCat}</td>
        <td><span class="badge ${badgeClass}">${displayStatus}</span></td>
        <td>${item.dateLost || item.dateFound || item.dateReported || '-'}</td>
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

  // 0. Dashboard Stats & Notifications
  const statsContainer = document.getElementById('stats-grid');
  if (statsContainer) {
    try {
      const stats = await api.get('/stats/dashboard');
      document.getElementById('stat-lost').textContent = stats.totalLost;
      document.getElementById('stat-found').textContent = stats.totalFound;
      document.getElementById('stat-matches').textContent = stats.totalMatches;
      document.getElementById('stat-claims').textContent = stats.pendingClaims;
    } catch (e) {
      console.error('Failed to fetch stats:', e);
    }
  }

  // Handle Notification Badge
  try {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      const user = JSON.parse(userStr);
      const notifs = await api.get(`/notifications/${user.id}`);
      const unread = notifs.filter(n => !n.read).length;
      const badge = document.getElementById('notif-badge');
      if (badge && unread > 0) {
        badge.textContent = unread;
        badge.style.display = 'flex';
      } else if (badge) {
        badge.style.display = 'none';
      }
    }
  } catch (e) {
    console.error('Failed to fetch notifications:', e);
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
        await ItemsAPI.reportLostItem({ 
          item: { name, description, category },
          dateLost: new Date().toISOString(),
          locationLost: "Community Area"
        });
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
        await ItemsAPI.reportFoundItem({ 
          item: { name, description, category },
          dateFound: new Date().toISOString(),
          locationFound: "Community Area"
        });
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
