/**
 * Navbar Handler - Initialize and manage navbar across all pages
 */

// Check if user is logged in
function checkUserStatus() {
    return fetch('/auth/me')
        .then(response => response.ok ? response.json() : null)
        .catch(error => {
            console.error('Error checking user status:', error);
            return null;
        });
}

// Initialize navbar based on user status
async function initializeNavbar() {
    const user = await checkUserStatus();
    const authButtons = document.getElementById('authButtons');
    
    if (!authButtons) {
        console.warn('Auth buttons container not found');
        return;
    }

    if (user && user.success) {
        // User is logged in
        const u = user.user || {};
        authButtons.innerHTML = `
            <div class="user-menu">
                <div class="user-dropdown" style="position:relative;">
                  <button id="profileToggle" class="btn btn-profile">${escapeHtml(u.fullName || 'Profile')}</button>
                  <div id="profileMenu" class="profile-menu" style="display:none; position:absolute; right:0; background:rgba(10,12,20,0.9); border:1px solid rgba(255,255,255,0.04); padding:8px; border-radius:8px; min-width:160px; z-index:1000;">
                    <a href="/profile" style="display:block; padding:8px; color:#e6eef8; text-decoration:none;">My Profile</a>
                    <button id="navLogout" style="display:block; width:100%; padding:8px; margin-top:6px; background:transparent; border:none; color:#e6eef8; text-align:left; cursor:pointer;">Logout</button>
                  </div>
                </div>
            </div>
        `;
        // wire up dropdown and logout
        const toggle = document.getElementById('profileToggle');
        const menu = document.getElementById('profileMenu');
        if (toggle && menu) {
            toggle.addEventListener('click', function() { menu.style.display = (menu.style.display === 'none') ? 'block' : 'none'; });
            document.addEventListener('click', function(e) { if (!toggle.contains(e.target) && !menu.contains(e.target)) { menu.style.display = 'none'; } });
        }
        const navLogout = document.getElementById('navLogout');
        if (navLogout) navLogout.addEventListener('click', logout);
    } else {
        // User is not logged in
        authButtons.innerHTML = `
            <a href="/login.html" class="btn btn-login">Log In</a>
            <a href="/signup.html" class="btn btn-signup">Sign Up</a>
        `;
    }
}

// Logout function
function logout() {
    fetch('/logout', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                // clear security context on client
                window.location.href = '/';
            } else {
                window.location.href = '/';
            }
        })
        .catch(error => {
            console.error('Logout error:', error);
            alert('An error occurred while logging out');
        });
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Set active navbar link based on current page
function setActiveNavLink() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.navbar-nav a');
    
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentPath || (href === '/' && currentPath === '/')) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
}

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    initializeNavbar();
    setActiveNavLink();
});

// Update navbar when user navigates back to the page
window.addEventListener('pageshow', function() {
    initializeNavbar();
    setActiveNavLink();
});
