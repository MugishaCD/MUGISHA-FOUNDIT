// claims.js
// Requires api.js and auth.js

const ClaimsAPI = {
  runMatches: () => api.post('/matches/run', {}),
  getMatches: () => api.get('/matches'),
  getClaims: () => api.get('/claims'),
  submitClaim: (data) => api.post('/claims', data),
  approveClaim: (id) => api.put(`/claims/${id}/approve`, {}),
  rejectClaim: (id) => api.put(`/claims/${id}/reject`, {})
};

function renderMatchesTable(matches) {
  const container = document.getElementById('matches-list');
  if (!container) return;

  if (!matches || matches.length === 0) {
    container.innerHTML = `<div class="empty-state">No matches found. Try running the matching algorithm.</div>`;
    return;
  }

  let html = `
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>Lost Item UID</th>
            <th>Found Item UID</th>
            <th>Match Score</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
  `;

  matches.forEach(m => {
    // A heuristic for best matches
    const isBestMatch = m.score > 0.8 || m.score > 80; 
    const badgeHtml = isBestMatch ? `<span class="badge badge-found">High Match</span>` : `<span class="badge badge-pending">Score: ${m.score}</span>`;

    html += `
      <tr>
        <td>${m.lostItemId}</td>
        <td>${m.foundItemId}</td>
        <td>${badgeHtml}</td>
        <td>${m.status || 'PENDING'}</td>
        <td>
          <button class="btn btn-secondary" style="padding: 0.2rem 0.5rem; font-size: 0.8rem;" onclick="prefillClaim(${m.lostItemId}, ${m.foundItemId})">File Claim</button>
        </td>
      </tr>
    `;
  });

  html += `</tbody></table></div>`;
  container.innerHTML = html;
}

function renderClaimsTable(claims) {
  const container = document.getElementById('claims-list');
  if (!container) return;

  if (!claims || claims.length === 0) {
    container.innerHTML = `<div class="empty-state">No active claims at this time.</div>`;
    return;
  }

  // Admin logic (Assume role is stored)
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const isAdmin = user.role === 'ADMIN';

  let html = `
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>Claim ID</th>
            <th>Lost Item</th>
            <th>Found Item</th>
            <th>Claimant</th>
            <th>Status</th>
            ${isAdmin ? '<th>Admin Actions</th>' : ''}
          </tr>
        </thead>
        <tbody>
  `;

  claims.forEach(c => {
    let statusClass = 'badge-pending';
    if (c.status === 'APPROVED') statusClass = 'badge-found';
    if (c.status === 'REJECTED') statusClass = 'badge-lost';

    html += `
      <tr>
        <td>#${c.id}</td>
        <td>${c.lostItemId}</td>
        <td>${c.foundItemId}</td>
        <td>${c.userId || c.userName || 'User'}</td>
        <td><span class="badge ${statusClass}">${c.status}</span></td>
        ${isAdmin ? `
        <td>
          ${c.status === 'PENDING' ? `
            <button class="btn" style="background:#10B981; color:white; padding:0.2rem 0.5rem; font-size: 0.8rem;" onclick="handleApprove(${c.id})">Approve</button>
            <button class="btn" style="background:#EF4444; color:white; padding:0.2rem 0.5rem; font-size: 0.8rem;" onclick="handleReject(${c.id})">Reject</button>
          ` : '-'}
        </td>
        ` : ''}
      </tr>
    `;
  });

  html += `</tbody></table></div>`;
  container.innerHTML = html;
}

// Global functions for inline HTML event handlers
window.prefillClaim = function(lostId, foundId) {
  document.getElementById('claimLostId').value = lostId;
  document.getElementById('claimFoundId').value = foundId;
  document.getElementById('claimFormContainer').scrollIntoView({ behavior: 'smooth' });
}

window.handleApprove = async function(id) {
  try {
    await ClaimsAPI.approveClaim(id);
    showToast('Claim approved', 'success');
    loadData();
  } catch(e) {
    showToast(e.message, 'error');
  }
}

window.handleReject = async function(id) {
  try {
    await ClaimsAPI.rejectClaim(id);
    showToast('Claim rejected', 'success');
    loadData();
  } catch(e) {
    showToast(e.message, 'error');
  }
}

async function loadData() {
  try {
    const matches = await ClaimsAPI.getMatches();
    renderMatchesTable(matches);
  } catch (e) {
    console.warn("Failed to fetch matches", e);
  }

  try {
    const claims = await ClaimsAPI.getClaims();
    renderClaimsTable(claims);
  } catch (e) {
    console.warn("Failed to fetch claims", e);
  }
}

document.addEventListener('DOMContentLoaded', () => {
  requireAuth();
  loadData();

  // Run match button
  const runMatchesBtn = document.getElementById('runMatchesBtn');
  if (runMatchesBtn) {
    runMatchesBtn.addEventListener('click', async () => {
      runMatchesBtn.disabled = true;
      runMatchesBtn.innerText = 'Running Algorithm...';
      try {
        await ClaimsAPI.runMatches();
        showToast('Matching algorithm completed successfully', 'success');
        await loadData();
      } catch (e) {
        showToast(e.message, 'error');
      } finally {
        runMatchesBtn.disabled = false;
        runMatchesBtn.innerText = 'Run Matching Algorithm';
      }
    });
  }

  // Claim Form submission
  const claimForm = document.getElementById('claimForm');
  if (claimForm) {
    claimForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      
      const lostItemId = document.getElementById('claimLostId').value;
      const foundItemId = document.getElementById('claimFoundId').value;
      const proof = document.getElementById('claimProof').value;
      
      const btn = document.getElementById('submitClaimBtn');
      btn.disabled = true;

      try {
        await ClaimsAPI.submitClaim({ lostItemId, foundItemId, proofOfOwnership: proof });
        showToast('Claim submitted successfully!', 'success');
        claimForm.reset();
        await loadData();
      } catch (err) {
        showToast(err.message, 'error');
      } finally {
        btn.disabled = false;
      }
    });
  }
});
