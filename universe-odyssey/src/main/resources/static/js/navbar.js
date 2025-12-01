document.addEventListener('DOMContentLoaded', function () {
    fetchUserStatus();
});

function fetchUserStatus() {
    fetch('/auth/me')
        .then(response => response.json())
        .then(data => {
            const authContainer = document.getElementById('navbar-auth');
            if (!authContainer) return;

            if (data.success && data.user) {
                renderLoggedInState(authContainer, data.user);
            } else {
                renderLoggedOutState(authContainer);
            }
        })
        .catch(error => {
            console.error('Error fetching user status:', error);
            const authContainer = document.getElementById('navbar-auth');
            if (authContainer) renderLoggedOutState(authContainer);
        });
}

function renderLoggedInState(container, user) {
    container.innerHTML = `
        <div class="relative ml-3">
            <div>
                <button type="button" class="flex max-w-xs items-center rounded-full text-sm focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800" id="user-menu-button" aria-expanded="false" aria-haspopup="true">
                    <span class="sr-only">Open user menu</span>
                    <div class="h-8 w-8 rounded-full bg-gradient-to-r from-blue-500 to-purple-600 flex items-center justify-center text-white font-bold">
                        ${getInitials(user.fullName)}
                    </div>
                    <span class="ml-3 text-gray-300 font-medium hidden md:block">${user.fullName}</span>
                    <svg class="ml-2 h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                        <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
                    </svg>
                </button>
            </div>
            <div class="hidden absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-gray-800 py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none glass-panel" role="menu" aria-orientation="vertical" aria-labelledby="user-menu-button" tabindex="-1" id="user-menu-dropdown">
                <a href="/profile" class="block px-4 py-2 text-sm text-gray-300 hover:bg-gray-700 hover:text-white" role="menuitem" tabindex="-1" id="user-menu-item-0">My Profile</a>
                <a href="#" onclick="logout(event)" class="block px-4 py-2 text-sm text-gray-300 hover:bg-gray-700 hover:text-white" role="menuitem" tabindex="-1" id="user-menu-item-2">Logout</a>
            </div>
        </div>
    `;

    // Dropdown toggle logic
    const button = document.getElementById('user-menu-button');
    const dropdown = document.getElementById('user-menu-dropdown');

    button.addEventListener('click', () => {
        const isExpanded = button.getAttribute('aria-expanded') === 'true';
        button.setAttribute('aria-expanded', !isExpanded);
        dropdown.classList.toggle('hidden');
    });

    // Close dropdown when clicking outside
    document.addEventListener('click', (event) => {
        if (!button.contains(event.target) && !dropdown.contains(event.target)) {
            button.setAttribute('aria-expanded', 'false');
            dropdown.classList.add('hidden');
        }
    });
}

function renderLoggedOutState(container) {
    container.innerHTML = `
        <div class="flex items-center space-x-4">
            <a href="/login.html" class="text-gray-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium">Login</a>
            <a href="/signup.html" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors">Sign Up</a>
        </div>
    `;
}

function getInitials(name) {
    if (!name) return 'U';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2);
}

function logout(event) {
    event.preventDefault();
    fetch('/logout', { method: 'POST' }) // Assuming Spring Security default logout
        .then(() => {
            window.location.href = '/';
        })
        .catch(err => {
            console.error('Logout failed', err);
            // Fallback if POST fails or if it's a GET logout
            window.location.href = '/logout';
        });
}
