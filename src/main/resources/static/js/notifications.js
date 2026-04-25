// notifications.js
// Requires api.js and auth.js

const NotificationsAPI = {
  getNotifications: (userId) => api.get(`/notifications/${userId}`),
};

function renderNotifications(notifications) {
  const container = document.getElementById('notifications-list');
  if (!container) return;

  if (!notifications || notifications.length === 0) {
    container.innerHTML = `
      <div class="empty-state">
        <h3>All caught up!</h3>
        <p>You have no new notifications.</p>
      </div>`;
    return;
  }

  let html = `<div class="grid" style="gap: 1rem;">`;

  notifications.forEach(n => {
    // If notification has an 'isRead' property, we can style accordingly
    const unreadClass = n.read ? '' : 'border-left-color: var(--primary); border-left-width: 4px; border-left-style: solid;';
    
    html += `
      <div class="card" style="padding: 1.5rem; ${unreadClass}">
        <div class="flex-between">
          <h3 style="font-size: 1.1rem; margin-bottom: 0;">${n.title || 'New Notification'}</h3>
          <span style="font-size: 0.8rem; color: var(--text-muted);">${n.createdAt || new Date().toISOString().split('T')[0]}</span>
        </div>
        <p style="margin-top: 0.5rem; margin-bottom: 0;">${n.message}</p>
      </div>
    `;
  });

  html += `</div>`;
  container.innerHTML = html;
}

let notificationInterval = null;

async function fetchAndRenderNotifications() {
  const userStr = localStorage.getItem('user');
  if (!userStr) return;
  const user = JSON.parse(userStr);
  const userId = user.id; // Expecting ID from local storage

  if (!userId) {
    document.getElementById('notifications-list').innerHTML = `
      <div class="empty-state text-center" style="color: var(--danger)">
        Unable to find user ID to load notifications. Please login again.
      </div>
    `;
    return;
  }

  try {
    const notifications = await NotificationsAPI.getNotifications(userId);
    renderNotifications(notifications);
  } catch (error) {
    console.error('Error fetching notifications:', error);
  }
}

document.addEventListener('DOMContentLoaded', () => {
  requireAuth();
  
  // Initial fetch
  fetchAndRenderNotifications();

  // "Real-time" polling every 10 seconds
  notificationInterval = setInterval(() => {
    fetchAndRenderNotifications();
  }, 10000);
});

// Clean up polling if leaving the page (optional since SPA/refresh kills it anyway)
window.addEventListener('beforeunload', () => {
  if(notificationInterval) clearInterval(notificationInterval);
});
