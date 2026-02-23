window.onload = async function() {
    const token = sessionStorage.getItem('token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }

    // Tenta pegar do storage primeiro
    let gamerData = JSON.parse(sessionStorage.getItem('gamer'));
    
    if (gamerData && (gamerData.name || gamerData.username)) {
        renderWelcomeText(gamerData);
    } else {
        // Se não houver dados ou o nome estiver faltando, busca no backend
        await fetchGamerProfile();
    }

    loadCharacters(); 
};

async function fetchGamerProfile() {
    const token = sessionStorage.getItem('token');
    try {
        const response = await fetch(`${BASE_URL}/gamer/profile`, { // Certifique-se de ter essa rota no Java
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) {
            const gamer = await response.json();
            sessionStorage.setItem('gamer', JSON.stringify(gamer));
            renderWelcomeText(gamer);
        }
    } catch (error) {
        console.error("Erro ao carregar perfil do gamer:", error);
    }
}

function renderWelcomeText(gamer) {
    const nomeDisplay = gamer.name || gamer.username || gamer.email || "Jogador";
    const element = document.getElementById('welcomeText');
    if (element) {
        element.innerText = `Olá, ${nomeDisplay}!`;
    }
}

function logout() {
    sessionStorage.clear();
    window.location.href = 'index.html';
}
 
document.getElementById('charForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const token = sessionStorage.getItem('token');
    const charData = {
        name: document.getElementById('name').value,
        gameTitle: document.getElementById('gameTitle').value,
        imageUrl: document.getElementById('imageUrl').value // Captura a URL do input
    };

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
            alert('Personagem criado com sucesso!');
            location.reload(); 
        } else {
            const erro = await response.text();
            alert('Erro ao criar: ' + erro);
        }
    } catch (error) {
        console.error("Erro na criação:", error);
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
            // Se char.imageUrl for nulo ou vazio, usa a imagem padrão
            const imgSrc = char.imageUrl ? char.imageUrl : 'images/default-avatar.jpg';
            const btnDelete = `
                            <button class="btn btn-outline-danger btn-sm position-absolute top-0 end-0 m-2" 
                                    onclick="event.stopPropagation(); confirmarExclusao(${char.id})">
                                <i class="bi bi-trash"></i>
                            </button>
                        `;

            return `
            <div class="col-sm-6 col-md-4 col-lg-3 mb-4 d-flex justify-content-center" 
                 onclick="selectCharacter(${char.id})" style="cursor: pointer;">
                <div class="card bg-secondary text-white shadow-sm border-0" style="width: 100%; max-width: 300px;">
                    <div class="p-3"> 
                        <img src="${imgSrc}" 
                            class="card-img-top rounded-3" 
                            style="aspect-ratio: 1 / 1; object-fit: cover; width: 100%;" 
                            onerror="this.onerror=null; this.src='images/default-avatar.jpg'"
                            alt="Avatar">
                    </div>
                    <div class="card-body pt-0 text-center">
                        <h4 class="card-title fw-bold mb-1">${char.name}</h4>
                        ${btnDelete}
                        <h6 class="card-subtitle mb-3 text-warning fw-semibold">${char.gameTitle}</h6>
                    </div>
                </div>
            </div>
        `;
        }).join('');
    } else {
        container.innerHTML = '<div class="text-center"><p>Você ainda não possui personagens cadastrados.</p></div>';
    }
}

function previewImage(url) {
    const preview = document.getElementById('imagePreview');
    
    if (url && url.trim() !== "") {
        preview.src = url;
    } else {
        // Se o campo estiver vazio, volta para a imagem padrão
        preview.src = 'images/default-avatar.jpg';
    }

    // Tratamento de erro: se a URL for inválida ou não for uma imagem
    preview.onerror = function() {
        this.src = 'images/default-avatar.jpg';
    };
}

async function confirmarExclusao(id) {
    if (confirm("Tem certeza que deseja deletar este personagem? Todos os posts e comentários dele serão apagados!")) {
        const token = sessionStorage.getItem('token');
        const response = await fetch(`${BASE_URL}/gamer/character/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        if (response.ok) {
            location.reload();
        } else {
            alert("Erro ao deletar personagem.");
        }
    }
}