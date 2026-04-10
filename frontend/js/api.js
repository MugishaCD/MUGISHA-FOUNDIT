const API_BASE_URL = 'http://localhost:8081/api';

/**
 * Common API fetch wrapper
 */
async function fetchApi(endpoint, options = {}) {
  // Setup headers
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  // Append Auth header if the user has a token. 
  // If the backend uses Basic auth or JWT, we include it here.
  // Assuming JWT token is stored as 'authToken'
  const token = localStorage.getItem('authToken');
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const config = {
    ...options,
    headers,
  };

  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
    
    // Status code handling
    if (!response.ok) {
      if (response.status === 401) {
        // Unauthorized - flush session and redirect
        localStorage.removeItem('user');
        localStorage.removeItem('authToken');
        window.location.href = 'login.html';
        return;
      }
      
      let errorData;
      try {
        errorData = await response.json();
      } catch (e) {
        errorData = { message: `HTTP Error: ${response.status}` };
      }
      throw new Error(errorData.message || `Request failed with status ${response.status}`);
    }
    
    // Some responses might be 204 No Content
    if (response.status === 204) {
      return null;
    }

    return await response.json();
  } catch (error) {
    console.error(`API Fetch Error [${endpoint}]:`, error);
    showToast(error.message, 'error');
    throw error;
  }
}

// API Methods
const api = {
  get: (endpoint) => fetchApi(endpoint, { method: 'GET' }),
  post: (endpoint, body) => fetchApi(endpoint, { method: 'POST', body: JSON.stringify(body) }),
  put: (endpoint, body) => fetchApi(endpoint, { method: 'PUT', body: JSON.stringify(body) }),
  delete: (endpoint) => fetchApi(endpoint, { method: 'DELETE' })
};

// --- Toast System ---
function initToastContainer() {
  if (!document.getElementById('toast-container')) {
    const container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }
}

function showToast(message, type = 'info') {
  initToastContainer();
  const container = document.getElementById('toast-container');
  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.innerText = message;
  
  container.appendChild(toast);
  
  setTimeout(() => {
    toast.style.animation = 'slideOut 0.3s forwards';
    setTimeout(() => {
      if (toast.parentNode) {
        toast.parentNode.removeChild(toast);
      }
    }, 300);
  }, 4000);
}

// Ensure init is called on page load
document.addEventListener('DOMContentLoaded', initToastContainer);
