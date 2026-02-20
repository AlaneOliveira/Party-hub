const BASE_URL = 'http://localhost:8080'; 

// --- CADASTRO ---
const registerForm = document.getElementById('registerForm');
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        // Os campos devem bater com o RegisterDTO.java (name, email, password)
        const payload = {
            name: document.getElementById('regName').value,
            email: document.getElementById('regEmail').value,
            password: document.getElementById('regPassword').value
        };

        try {
            const response = await fetch(`${BASE_URL}/auth/register`, {  
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const message = await response.text();

            if (response.ok) {
                alert(message); // "Cadastro realizado com sucesso!..."
                window.location.href = 'login.html'; 
            } else {
                alert(message); // Exibe o erro retornado pelo Spring (ex: "Erro: Este e-mail já está cadastrado!")
            }
        } catch (error) {
            console.error("Erro ao cadastrar:", error);
            alert("Erro de conexão com o servidor.");
        }
    });
}

// --- LOGIN ---
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        // O seu LoginDTO espera "login" e "password"
        const payload = {
            login: document.getElementById('loginEmail').value,
            password: document.getElementById('loginPassword').value
        };

        try {
            const response = await fetch(`${BASE_URL}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const token = await response.text();
                sessionStorage.setItem('token', token); 
                
                // Busca os dados do perfil antes de redirecionar
                await fetchGamerData(token);
                
                window.location.href = 'gamerhome.html';
            } else {
                const errorMsg = await response.text();
                alert(errorMsg || "Email ou senha inválidos.");
            }
        } catch (error) {
            console.error("Erro no login:", error);
            alert("Erro de conexão com o servidor.");
        }
    });
}

// Função para buscar dados do perfil
async function fetchGamerData(token) {
    try {
        const response = await fetch(`${BASE_URL}/gamer`, { 
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) {
            const gamer = await response.json();
            sessionStorage.setItem('gamer', JSON.stringify(gamer));
        }
    } catch (err) {
        console.error("Erro ao buscar dados do gamer:", err);
    }
}