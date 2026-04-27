// auth.js
// Requires api.js to be loaded first

/**
 * Handle User Login
 * @param {Event} e 
 */
async function handleLogin(e) {
  e.preventDefault();
  
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  
  if (!email || !password) {
    showToast('Please fill in all fields', 'error');
    return;
  }
  
  // Clear any existing auth data before logging in
  localStorage.removeItem('authToken');
  localStorage.removeItem('user');

  const loginBtn = document.getElementById('loginBtn');
  loginBtn.disabled = true;
  loginBtn.classList.add('btn-loading');

  try {
    const payload = {
      email,
      password
    };
    
    let response;
    try {
       response = await api.post('/v1/auth/login', payload);
    } catch(apiError) {
       throw apiError;
    }
    
    const token = response.token || 'mock-token-for-dev';
    const user = response.user || response;

    localStorage.setItem('authToken', token);
    localStorage.setItem('user', JSON.stringify(user));
    
    showToast('Login successful!', 'success');
    
    setTimeout(() => {
      window.location.href = 'dashboard.html';
    }, 500);

  } catch (error) {
    showToast(error.message, 'error');
  } finally {
    loginBtn.disabled = false;
    loginBtn.classList.remove('btn-loading');
  }
}

/**
 * Handle User Registration
 * @param {Event} e 
 */
async function handleRegister(e) {
  e.preventDefault();
  
  const name = document.getElementById('name').value;
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  const confirmPassword = document.getElementById('confirmPassword').value;

  if (!name || !email || !password || !confirmPassword) {
    showToast('Please fill in all fields', 'error');
    return;
  }

  if (password !== confirmPassword) {
    showToast('Passwords do not match', 'error');
    return;
  }

  const btn = document.getElementById('registerBtn');
  btn.disabled = true;
  btn.classList.add('btn-loading');

  try {
    const payload = { fullName: name, email, password, phone: '', role: 'USER' };
    
    await api.post('/v1/auth/register', payload);
    
    showToast('Registration successful! Please login.', 'success');
    
    setTimeout(() => {
      window.location.href = 'login.html';
    }, 1500);

  } catch (error) {
    showToast(error.message, 'error');
  } finally {
    btn.disabled = false;
    btn.classList.remove('btn-loading');
  }
}

/**
 * Check if the user is authenticated, redirecting to login if not
 */
function requireAuth() {
  const token = localStorage.getItem('authToken');
  const user = localStorage.getItem('user');
  
  if (!token || !user) {
    window.location.href = 'login.html';
  }
}

/**
 * Logout utility
 */
function logout() {
  localStorage.removeItem('authToken');
  localStorage.removeItem('user');
  window.location.href = 'login.html';
}

// Bind event listeners based on the page
document.addEventListener('DOMContentLoaded', () => {
  const loginForm = document.getElementById('loginForm');
  if (loginForm) {
    loginForm.addEventListener('submit', handleLogin);
  }

  const registerForm = document.getElementById('registerForm');
  if (registerForm) {
    registerForm.addEventListener('submit', handleRegister);
  }

  const logoutBtn = document.getElementById('logoutBtn');
  if (logoutBtn) {
    logoutBtn.addEventListener('click', (e) => {
      e.preventDefault();
      logout();
    });
  }
});
