// JS helper for user login and signup using the /auth endpoints
async function postJson(url, data) {
    const res = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
    return res.json();
}

async function handleUserLogin(form, onSuccess) {
    const username = form.querySelector('[name="email"]').value;
    const password = form.querySelector('[name="password"]').value;
    try {
        const resp = await postJson('/auth/login', { email: username, password });
        if (resp && resp.success) {
            window.location.href = onSuccess || '/';
        } else {
            showMessage(form, resp.message || 'Login failed');
        }
    } catch (e) {
        showMessage(form, 'Network error');
    }
}

async function handleSignup(form, onSuccess) {
    const fullName = form.querySelector('[name="fullName"]').value;
    const email = form.querySelector('[name="email"]').value;
    const password = form.querySelector('[name="password"]').value;
    try {
        const resp = await postJson('/auth/signup', { fullName, email, password });
        if (resp && resp.success) {
            window.location.href = onSuccess || '/';
        } else {
            showMessage(form, resp.message || 'Signup failed');
        }
    } catch (e) {
        showMessage(form, 'Network error');
    }
}

function showMessage(container, text, success=false) {
    let box = container.querySelector('.msg');
    if (!box) {
        box = document.createElement('div');
        box.className = 'msg';
        container.insertBefore(box, container.firstChild);
    }
    box.textContent = text;
    if (success) box.classList.add('success');
}

document.addEventListener('DOMContentLoaded', ()=>{
    const loginForm = document.querySelector('form[data-auth="login"]');
    if (loginForm) {
        loginForm.addEventListener('submit', (e)=>{e.preventDefault(); handleUserLogin(loginForm);});
    }
    const signupForm = document.querySelector('form[data-auth="signup"]');
    if (signupForm) {
        signupForm.addEventListener('submit', (e)=>{e.preventDefault(); handleSignup(signupForm);});
    }
    // Password visibility toggle support for any page
    document.querySelectorAll('.toggle-password').forEach(btn => {
        btn.addEventListener('click', (ev) => {
            const wrapper = btn.closest('.password-wrapper');
            if (!wrapper) return;
            const input = wrapper.querySelector('.form-control');
            if (!input) return;
            if (input.type === 'password') {
                input.type = 'text';
                btn.innerHTML = eyeOffSvg();
            } else {
                input.type = 'password';
                btn.innerHTML = eyeSvg();
            }
            input.focus();
        });
    });
});

function eyeSvg(){
    return '<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M12 5c5 0 9.27 3.11 11 7-1.73 3.89-6 7-11 7S3.73 15.89 2 12c1.73-3.89 6-7 10-7z" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/><circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/></svg>';
}

function eyeOffSvg(){
    return '<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M17.94 17.94A10.94 10.94 0 0 1 12 19c-5 0-9.27-3.11-11-7 .7-1.58 1.83-3.02 3.27-4.21" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/><path d="M1 1l22 22" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/></svg>';
}
