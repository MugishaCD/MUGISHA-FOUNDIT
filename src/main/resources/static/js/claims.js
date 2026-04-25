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
    const score = m.matchScore || 0;
    const scorePercent = Math.round(score * 100);
    const isBestMatch = score >= 0.8;
    const badgeHtml = isBestMatch ? `<span class="badge badge-found">Confidence: ${scorePercent}%</span>` : `<span class="badge-pending" style="padding: 0.2rem 0.5rem; border-radius: 4px; font-size: 0.75rem;">Score: ${scorePercent}%</span>`;

    const lostName = m.lostItem?.item?.name || 'Unknown';
    const foundName = m.foundItem?.item?.name || 'Unknown';

    html += `
      <tr>
        <td><strong>${lostName}</strong> <span style="opacity: 0.5; font-size: 0.8rem;">#${m.lostItem?.id}</span></td>
        <td><strong>${foundName}</strong> <span style="opacity: 0.5; font-size: 0.8rem;">#${m.foundItem?.id}</span></td>
        <td>${badgeHtml}</td>
        <td><span class="badge badge-pending">POTENTIAL</span></td>
        <td>
          <button class="btn btn-primary" style="padding: 0.3rem 0.6rem; font-size: 0.8rem;" onclick="prefillClaim(${m.lostItem?.id}, ${m.foundItem?.id})">Verify & Claim</button>
          <a href="chat.html?matchId=${m.id}" class="btn btn-secondary" style="padding: 0.3rem 0.6rem; font-size: 0.8rem; text-decoration: none; display: inline-block;">Handover Chat</a>
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

    const itemName = c.item ? c.item.name : 'Unknown Item';
    const itemId = c.item ? c.item.id : '-';
    const claimantName = c.user ? c.user.fullName : 'User';

    html += `
      <tr>
        <td>#${c.id}</td>
        <td><strong>${itemName}</strong> <span style="opacity: 0.5; font-size: 0.8rem;">(Item #${itemId})</span></td>
        <td>${claimantName}</td>
        <td><span class="badge ${statusClass}">${c.status}</span></td>
        ${isAdmin ? `
        <td>
          ${c.status === 'PENDING' ? `
            <button class="btn btn-secondary" style="background:#10B981; color:white; border:none; padding:0.2rem 0.5rem; font-size: 0.8rem;" onclick="handleApprove(${c.id})">Approve</button>
            <button class="btn btn-secondary" style="background:#F43F5E; color:white; border:none; padding:0.2rem 0.5rem; font-size: 0.8rem;" onclick="handleReject(${c.id})">Reject</button>
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
