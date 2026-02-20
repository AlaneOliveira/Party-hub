const selectedCharId = sessionStorage.getItem('selectedCharId');

window.onload = function() {
    const token = sessionStorage.getItem('token');
     
    if (!token || !selectedCharId) {
        window.location.href = 'gamerhome.html';
        return;
    }
 
    document.getElementById('charNameDisplay').innerText = `Personagem ID: ${selectedCharId}`;

    loadFeed();
};
 
async function enviarPost() {
    const content = document.getElementById('postContent').value;
    const token = sessionStorage.getItem('token');

    if (!content.trim()) {
        alert("Escreva algo antes de postar!");
        return;
    }
 
    const payload = {
        content: content,
        description: "Postagem de Personagem", 
        category: "Geral",
        characterId: parseInt(selectedCharId), 
        betValue: 0.0,
        betAnswer: false
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
    const feedContainer = document.getElementById('feedContainer');

    try { 
        const response = await fetch(`${BASE_URL}/post`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const posts = await response.json();
            renderFeed(posts);
        } else {
            feedContainer.innerHTML = '<p class="text-danger">Erro ao carregar o feed.</p>';
        }
    } catch (error) {
        console.error("Erro no feed:", error);
    }
}
 
function renderFeed(posts) {
    const container = document.getElementById('feedContainer');
    
    if (posts.length === 0) {
        container.innerHTML = '<p class="text-muted">Nenhuma postagem encontrada.</p>';
        return;
    }

    container.innerHTML = posts.map(post => `
        <div class="card bg-secondary text-white mb-3 shadow-sm border-0">
            <div class="card-body">
                <p class="mb-1 text-light opacity-75" style="font-size: 0.85rem;">
                    <i class="bi bi-clock"></i> ${new Date(post.date).toLocaleString()}
                </p>
                <p class="card-text fs-5">${post.content}</p>
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <span class="badge bg-dark me-2">❤️ ${post.likes || 0}</span>
                        <span class="badge bg-dark">💰 Pot: ${post.pot.toFixed(2)}</span>
                    </div>
                    <button class="btn btn-sm btn-outline-light">Comentar</button>
                </div>
            </div>
        </div>
    `).join('');
}
 
async function buscarMesmoJogo() {
    const playersList = document.getElementById('playersList');
    playersList.innerHTML = '<p class="text-warning">Buscando jogadores do seu jogo...</p>';
}