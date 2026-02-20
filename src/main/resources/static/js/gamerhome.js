window.onload = function() {
    const token = sessionStorage.getItem('token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }

    const gamerData = JSON.parse(sessionStorage.getItem('gamer'));
    if (gamerData) {
        document.getElementById('welcomeText').innerText = `Olá, ${gamerData.name}!`;
    }

    loadCharacters(); 
};

function logout() {
    sessionStorage.clear();
    window.location.href = 'index.html';
}

document.getElementById('charForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const charData = {
        name: document.getElementById('name').value,
        gameTitle: document.getElementById('gameTitle').value,
        clazz: document.getElementById('clazz').value,
        level: 1 
    };

    const token = sessionStorage.getItem('token');
 
    try { 
        const response = await fetch(`${BASE_URL}/gamer/character`, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}` 
            },
            body: JSON.stringify(charData)
        });

        if (response.ok) {
            const savedChar = await response.json(); // Pega o personagem criado
            const fileInput = document.getElementById('photoFile');

            if (fileInput.files.length > 0) {
                const formData = new FormData();
                formData.append('file', fileInput.files[0]);

                await fetch(`${BASE_URL}/gamer/character/photo/${savedChar.id}`, {
                    method: 'POST',
                    headers: { 'Authorization': `Bearer ${token}` },
                    body: formData
                });
            }

            alert('Personagem e foto salvos!');
            location.reload(); 
        
        } else {
            const erro = await response.text();
            alert('Erro ao criar personagem: ' + erro);
        }
    } catch (error) {
        console.error("Erro no fluxo:", error);
    }
});

async function loadCharacters() {
    const token = sessionStorage.getItem('token');
    try { 
        const response = await fetch(`${BASE_URL}/gamer/character/me`, { 
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        if (response.ok) {
            const characters = await response.json();
            renderCharacters(characters);
        }
    } catch (error) {
        console.error("Erro ao carregar personagens:", error);
    }
}

function selectCharacter(charId) {
    sessionStorage.setItem('selectedCharId', charId); 
    window.location.href = 'charhome.html';
}

function renderCharacters(list) {
    const container = document.getElementById('charactersList');
    if (list.length > 0) {
        container.innerHTML = list.map(char => { 
            const imgSrc = char.hasPhoto 
                ? `${BASE_URL}${char.imageUrl}`
                : 'images/default-avatar.jpg';

           return `
            <div class="col-sm-6 col-md-4 col-lg-3 mb-4 d-flex justify-content-center" onclick="selectCharacter(${char.id})" style="cursor: pointer;">
                <div class="card bg-secondary text-white shadow-sm border-0" style="width: 100%; max-width: 300px;">
                    <div class="p-3"> 
                        <img src="${imgSrc}" 
                            class="card-img-top rounded-3" 
                            style="aspect-ratio: 1 / 1; object-fit: cover; width: 100%;" 
                            onerror="this.onerror=null; this.src='/images/default-avatar.jpg'"
                            alt="Avatar">
                    </div>
                    <div class="card-body pt-0">
                        <h4 class="card-title fw-bold mb-1">${char.name}</h4>
                        <h6 class="card-subtitle mb-3 text-warning fw-semibold">${char.gameTitle}</h6>
                        <p class="card-text text-light opacity-75">
                            Classe: ${char.clazz || 'N/A'} <br> 
                            Nível: ${char.level}
                        </p>
                    </div>
                </div>
            </div>
        `;
        }).join('');
    }
} 

