// admin.js: UX helpers — modals, toasts, API helpers, animations
function createOverlay(innerHtml) {
  const overlay = document.createElement('div');
  overlay.className = 'modal fade-in';
  overlay.innerHTML = `<div class="modal-content scale-in">${innerHtml}</div>`;
  overlay.addEventListener('click', (e) => { if (e.target === overlay) overlay.remove(); });
  document.body.appendChild(overlay);
  return overlay;
}

function closeOverlay() {
  document.querySelectorAll('.modal').forEach(m => m.remove());
}

function showToast(message, type = 'info') {
  let t = document.getElementById('admin-toast');
  if (!t) {
    t = document.createElement('div');
    t.id = 'admin-toast';
    t.className = 'toast';
    document.body.appendChild(t);
  }
  t.textContent = message;
  t.className = 'toast show ' + (type === 'error' ? 'toast-error' : 'toast-success');
  setTimeout(() => { t.className = 'toast'; }, 3500);
}

function setLoading(on) {
  let s = document.getElementById('admin-loader');
  if (!s) {
    s = document.createElement('div');
    s.id = 'admin-loader';
    s.className = 'loader';
    s.innerHTML = '<div class="spinner"></div>';
    document.body.appendChild(s);
  }
  s.style.display = on ? 'flex' : 'none';
}

async function confirmDelete(url) {
  const html = `<div style="text-align:center"><h3>Confirm Delete</h3><p>Are you sure?</p><div style="display:flex;gap:8px;justify-content:center;margin-top:12px"><button class="btn" id="confirm-yes">Yes</button><button class="btn btn-ghost" id="confirm-no">No</button></div></div>`;
  const overlay = createOverlay(html);
  overlay.querySelector('#confirm-no').addEventListener('click', () => closeOverlay());
  overlay.querySelector('#confirm-yes').addEventListener('click', async () => {
    closeOverlay();
    try {
      setLoading(true);
      let opts = {};
      // If this is our API namespace, send DELETE; else just navigate GET
      if (url.startsWith('/admin/api/')) {
        opts = { method: 'DELETE' };
        const r = await fetch(url, opts);
        setLoading(false);
        if (r.ok) { showToast('Deleted', 'success'); document.querySelectorAll('.fade-out').forEach(el => el.remove()); location.reload(); }
        else showToast('Delete failed', 'error');
      } else {
        // fallback to GET delete endpoints
        const r = await fetch(url, { method: 'GET' });
        setLoading(false);
        if (r.ok) location.reload(); else showToast('Delete failed', 'error');
      }
    } catch (e) {
      setLoading(false);
      showToast('Error deleting', 'error');
    }
  });
}

function openEditUserModal(id) {
  // fetch user data from API
  fetch('/admin/api/users').then(r => r.json()).then(users => {
    const user = users.find(u => u.id == id);
    if (!user) { showToast('User not found', 'error'); return; }
    document.getElementById('edit-user-id').value = user.id;
    document.getElementById('edit-username').value = user.username || '';
    document.getElementById('edit-email').value = user.email || '';
    document.getElementById('edit-role').value = user.role || '';
    document.getElementById('edit-user-modal').style.display = 'flex';
    document.getElementById('edit-user-modal').classList.add('modal','scale-in');
  }).catch(()=>showToast('Failed to load user', 'error'));
}

function closeEditUserModal() {
  const m = document.getElementById('edit-user-modal');
  if (m) m.style.display = 'none';
}

async function submitEditUser(ev) {
  ev.preventDefault();
  const id = document.getElementById('edit-user-id').value;
  const payload = { id: id, username: document.getElementById('edit-username').value, email: document.getElementById('edit-email').value, role: document.getElementById('edit-role').value, active: true };
  try {
    setLoading(true);
    const r = await fetch('/admin/api/users/' + id, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) });
    setLoading(false);
    if (r.ok) { closeEditUserModal(); showToast('Saved', 'success'); location.reload(); }
    else { showToast('Save failed', 'error'); }
  } catch (e) { setLoading(false); showToast('Save error', 'error'); }
}

document.addEventListener('DOMContentLoaded', () => {
  // wire modal close
  document.querySelectorAll('.modal-close').forEach(el => el.addEventListener('click', closeEditUserModal));
  // wire form submit
  const f = document.getElementById('edit-user-form'); if (f) f.addEventListener('submit', submitEditUser);
});
