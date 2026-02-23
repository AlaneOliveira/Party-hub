const selectedCharId = sessionStorage.getItem('selectedCharId');  

window.onload = function() {
    const token = sessionStorage.getItem('token');
     
    if (!token || !selectedCharId) {
        window.location.href = 'gamerhome.html';
        return;
    }
 
    loadNavData();
    loadFeed();
};
 
async function loadNavData() {
    const token = sessionStorage.getItem('token');
    try {
        // Usamos o endpoint de detalhes que você já tem no Java
        const response = await fetch(`${BASE_URL}/gamer/character/${selectedCharId}?viewerId=${selectedCharId}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const char = await response.json();
            
            // Atualiza o nome do Universo (Jogo)
            document.getElementById('universeDisplay').innerText = char.gameTitle;
            
            // Atualiza a foto e o link do perfil
            document.getElementById('navProfileImg').src = char.imageUrl || 'images/default-avatar.jpg';
            document.getElementById('navProfileLink').href = `profilepage.html?id=${selectedCharId}`;
        }
    } catch (error) {
        console.error("Erro ao carregar dados da nav:", error);
    }
}

async function enviarPost() {
    const title = document.getElementById('postTitle').value; // Novo campo
    const content = document.getElementById('postContent').value;
    const token = sessionStorage.getItem('token');
    const selectedCharId = sessionStorage.getItem('selectedCharId');

    if (!title.trim() || !content.trim()) {
        alert("Preencha o título e o conteúdo!");
        return;
    }

    const payload = {
        title: title,        // Agora mapeado para o NewPostDTO
        content: content,    // Descrição/Corpo do post
        characterId: parseInt(selectedCharId)
    };

    try {
        const response = await fetch(`${BASE_URL}/post`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("Postado com sucesso!");
            document.getElementById('postTitle').value = '';
            document.getElementById('postContent').value = '';
            loadFeed(); 
        } else {
            const erro = await response.text();
            alert("Erro ao postar: " + erro);
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
    }
}
 
async function loadFeed() {
    const token = sessionStorage.getItem('token');
    const charId = sessionStorage.getItem('selectedCharId');
    const response = await fetch(`${BASE_URL}/post?characterId=${charId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    if (response.ok) {
        const posts = await response.json();
        renderFeed(posts, 'feedContainer'); // isProfilePage será false por padrão
    }
}
  
async function buscarMesmoJogo() {
    const playersList = document.getElementById('playersList');
    playersList.innerHTML = '<p class="text-warning">Buscando jogadores do seu jogo...</p>';
} 

async function buscarJogadores() {
    const query = document.getElementById('searchInput').value.trim();
    const playersList = document.getElementById('playersList');
    const token = sessionStorage.getItem('token');

    if (!query) {
        playersList.innerHTML = '<p class="text-muted small">Digite um nome para buscar.</p>';
        return;
    }

    playersList.innerHTML = '<div class="text-center py-2"><div class="spinner-border spinner-border-sm text-warning"></div></div>';

    try {
        // Buscamos apenas personagens que pertencem ao mesmo universo (jogo) do usuário logado
        const response = await fetch(`${BASE_URL}/gamer/character/search?name=${query}&charId=${selectedCharId}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const players = await response.json();
            renderPlayersList(players);
        }
    } catch (error) {
        console.error("Erro na busca:", error);
        playersList.innerHTML = '<p class="text-danger small">Erro ao conectar ao servidor.</p>';
    }
}

function renderPlayersList(players) {
    const playersList = document.getElementById('playersList');
    
    if (players.length === 0) {
        playersList.innerHTML = '<p class="text-center text-muted small py-3">Nenhum personagem com esse nome encontrado!</p>';
        return;
    }

    playersList.innerHTML = players.map(player => `
        <div class="d-flex align-items-center justify-content-between p-2 mb-2 bg-dark rounded shadow-sm border-start border-3 border-warning" 
             style="cursor: pointer;" onclick="window.location.href='profilepage.html?id=${player.id}'">
            <div class="d-flex align-items-center">
                <img src="${player.imageUrl || 'images/default-avatar.jpg'}" class="rounded-circle me-2" style="width: 35px; height: 35px; object-fit: cover;">
                <div>
                    <h6 class="mb-0 text-white small font-weight-bold">${player.name}</h6>
                </div>
            </div>
            <i class="bi bi-chevron-right text-muted" style="font-size: 0.8rem;"></i>
        </div>
    `).join('');
}

// Adicione loadSuggestions() no seu window.onload
window.onload = function() {
    // ... código anterior ...
    loadNavData();
    loadFeed();
    loadSuggestions(); // Nova chamada
};

async function loadSuggestions() {
    const token = sessionStorage.getItem('token');
    const suggestionsList = document.getElementById('suggestionsList');

    try {
        const response = await fetch(`${BASE_URL}/gamer/character/${selectedCharId}/suggestions`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const suggestions = await response.json();
            
            if (suggestions.length === 0) {
                document.getElementById('suggestionsSection').classList.add('d-none');
                return;
            }

            suggestionsList.innerHTML = suggestions.map(char => `
                <div class="d-flex align-items-center justify-content-between mb-3">
                    <div class="d-flex align-items-center" style="cursor: pointer" onclick="window.location.href='profilepage.html?id=${char.id}'">
                        <img src="${char.imageUrl || 'images/default-avatar.jpg'}" class="rounded-circle me-2" style="width: 32px; height: 32px; object-fit: cover;">
                        <span class="small fw-bold">${char.name}</span>
                    </div>
                    <button onclick="seguirRapido(${char.id})" class="btn btn-sm btn-primary py-0 px-2" style="font-size: 0.7rem;">Seguir</button>
                </div>
            `).join('');
        }
    } catch (error) {
        console.error("Erro ao carregar sugestões:", error);
    }
}

async function seguirRapido(targetId) {
    const token = sessionStorage.getItem('token');
    const response = await fetch(`${BASE_URL}/gamer/character/follow`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
        body: JSON.stringify({ followerId: parseInt(selectedCharId), followingId: parseInt(targetId) })
    });

    if (response.ok) {
        loadSuggestions(); // Remove quem você seguiu da lista
        loadFeed();        // Atualiza o feed para mostrar os posts do novo amigo!
    }
}