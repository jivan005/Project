const API_BASE = 'http://localhost:8082';
let authToken = localStorage.getItem('token');
let currentUserId = localStorage.getItem('userId');

document.addEventListener('DOMContentLoaded', () => {
    if (authToken && currentUserId) {
        showDashboard();
    } else {
        document.getElementById('auth-view').classList.add('active');
    }
});

function switchAuth(type) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    document.querySelector(`.tab[onclick="switchAuth('${type}')"]`).classList.add('active');
    
    if (type === 'login') {
        document.getElementById('login-form').style.display = 'block';
        document.getElementById('signup-form').style.display = 'none';
    } else {
        document.getElementById('login-form').style.display = 'none';
        document.getElementById('signup-form').style.display = 'block';
    }
    document.getElementById('auth-error').innerText = '';
}

document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const u = document.getElementById('login-username').value;
    const p = document.getElementById('login-password').value;
    
    try {
        const res = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username: u, password: p})
        });
        const data = await res.json();
        
        if (res.ok) {
            loginSuccess(data);
        } else {
            document.getElementById('auth-error').innerText = data.error || 'Login failed - invalid credentials';
        }
    } catch (err) {
        document.getElementById('auth-error').innerText = 'Connection error';
    }
});

document.getElementById('signup-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const u = document.getElementById('signup-username').value;
    const em = document.getElementById('signup-email').value;
    const p = document.getElementById('signup-password').value;
    
    try {
        const res = await fetch(`${API_BASE}/auth/signup`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username: u, email: em, password: p})
        });
        
        if (res.ok) {
            switchAuth('login');
            document.getElementById('login-username').value = u;
            document.getElementById('login-password').value = p;
            document.getElementById('auth-error').innerText = 'Creation success! Please login.';
            document.getElementById('auth-error').style.color = '#58a6ff';
        } else {
            const data = await res.json();
            document.getElementById('auth-error').innerText = data.error || data.message || 'Signup failed';
            document.getElementById('auth-error').style.color = '#f85149';
        }
    } catch (err) {
        document.getElementById('auth-error').innerText = 'Connection error';
    }
});

function loginSuccess(data) {
    authToken = data.token;
    currentUserId = data.id;
    localStorage.setItem('token', authToken);
    localStorage.setItem('userId', currentUserId);
    showDashboard();
}

function logout() {
    authToken = null;
    currentUserId = null;
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    document.getElementById('dashboard-view').classList.remove('active');
    document.getElementById('auth-view').classList.add('active');
}

async function showDashboard() {
    document.getElementById('auth-view').classList.remove('active');
    document.getElementById('dashboard-view').classList.add('active');
    await loadUserProfile();
}

async function loadUserProfile() {
    try {
        const res = await fetch(`${API_BASE}/dashboard/${currentUserId}`, {
            headers: {'Authorization': `Bearer ${authToken}`}
        });
        
        if (res.ok) {
            const data = await res.json();
            document.getElementById('profile-name').innerText = `@${data.username}`;
            animateValue('profile-level', parseInt(document.getElementById('profile-level').innerText), data.level, 1000);
            animateValue('profile-xp', parseInt(document.getElementById('profile-xp').innerText) || 0, data.xp, 1500);
            document.getElementById('profile-streak').innerText = `${data.streak} 🔥`;
        } else if (res.status === 401) {
            logout();
        }
    } catch (err) {
        console.error('Failed to load profile');
    }
}

async function submitSolution() {
    const code = document.getElementById('code-snippet').value;
    if (!code) return;

    try {
        await ensureProblemExists();
        
        const res = await fetch(`${API_BASE}/submissions`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify({problemId: 1, codeSnippet: code})
        });
        
        if (res.ok) {
            const data = await res.json();
            const evalDiv = document.getElementById('eval-result');
            evalDiv.style.display = 'block';
            
            const color = data.status === 'ACCEPTED' ? '#56d364' : '#f85149';
            evalDiv.style.borderLeftColor = color;
            
            evalDiv.innerHTML = `
                <div class="eval-score" style="color: ${color}">
                    ${data.status} (+${data.xpEarned} XP)
                </div>
                <div class="eval-feedback">
                    AI feedback processed! Level and XP updated in your profile!
                </div>
            `;
            
            await loadUserProfile();
            document.getElementById('code-snippet').value = '';
        }
    } catch (err) {
        console.error('Submission failed', err);
    }
}

async function ensureProblemExists() {
    const res = await fetch(`${API_BASE}/problems`, {headers: {'Authorization': `Bearer ${authToken}`}});
    if (res.ok) {
        let problems = await res.json();
        if (problems.length === 0) {
            await fetch(`${API_BASE}/problems`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${authToken}`},
                body: JSON.stringify({title: "Default Training Problem", description: "Solve a random algorithm.", difficulty: "EASY"})
            });
        }
    }
}

function animateValue(id, start, end, duration) {
    if (isNaN(start)) start = 0;
    if (start === end) {
        document.getElementById(id).innerHTML = end;
        return;
    }
    let range = end - start;
    let current = start;
    let increment = end > start ? 1 : -1;
    let stepTime = Math.abs(Math.floor(duration / range));
    if(stepTime < 20) stepTime = 20; 
    
    increment = Math.ceil((range / (duration / stepTime))) * (end > start ? 1 : -1);

    let obj = document.getElementById(id);
    let timer = setInterval(function() {
        current += increment;
        if ((increment > 0 && current >= end) || (increment < 0 && current <= end)) {
            clearInterval(timer);
            current = end;
        }
        obj.innerHTML = current;
    }, stepTime);
}
