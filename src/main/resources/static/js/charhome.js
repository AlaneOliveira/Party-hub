// Recupera os dados persistidos no login e na seleção do personagem
const selectedCharId = sessionStorage.getItem('selectedCharId');
const token = sessionStorage.getItem('token');

window.onload = function() {
    // Redireciona se tentar acessar a página sem selecionar um personagem
    if (!selectedCharId || !token) {
        window.location.href = 'gamerhome.html';
        return;
    }
    
    // Opcional: carregar dados básicos do personagem no topo da página
    carregarDadosPersonagem();
    carregarFeed();
};

// --- FUNÇÃO: CRIAR POST ---
async function enviarPost() {
    const textarea = document.getElementById('postContent');
    const content = textarea.value.trim();

    if (!content) {
        alert("O post não pode estar vazio!");
        return;
    }

    try {
        const response = await fetch(`/posts/create/${selectedCharId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ content: content })
        });

        if (response.ok) {
            textarea.value = ''; // Limpa o campo
            alert("Postagem realizada com sucesso!");
            carregarFeed(); // Atualiza a lista de posts automaticamente
        } else {
            const erro = await response.text();
            alert("Erro ao postar: " + erro);
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
    }
}

// --- FUNÇÃO: CARREGAR FEED ---
async function carregarFeed() {
    const feedContainer = document.getElementById('feedContainer');
    
    try {
        const response = await fetch(`/posts/feed/${selectedCharId}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const posts = await response.json();
            renderizarFeed(posts);
        }
    } catch (error) {
        console.error("Erro ao carregar feed:", error);
    }
}

function renderizarFeed(posts) {
    const feedContainer = document.getElementById('feedContainer');
    
    if (posts.length === 0) {
        feedContainer.innerHTML = '<div class="alert alert-info">Ninguém postou nada ainda. Siga novos heróis!</div>';
        return;
    }

    feedContainer.innerHTML = posts.map(post => `
        <div class="card bg-secondary text-white mb-3 shadow-sm border-0">
            <div class="card-body">
                <div class="d-flex align-items-center mb-2">
                    <img src="/photos/show/${post.character.id}" 
                         class="rounded-circle me-2" 
                         style="width: 40px; height: 40px; object-fit: cover;"
                         onerror="this.src='/images/default-avatar.jpg'">
                    <h6 class="card-title mb-0">@${post.character.name}</h6>
                </div>
                <p class="card-text">${post.content}</p>
                <div class="d-flex justify-content-between align-items-center">
                    <small class="text-light opacity-50">Postado em: ${new Date(post.createdAt).toLocaleString()}</small>
                    <div>
                        <button class="btn btn-sm btn-outline-warning me-2" onclick="abrirModalComentario(${post.id})">
                            <i class="bi bi-chat-dots"></i> Comentar
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="curtirPost(${post.id})">
                            <i class="bi bi-heart"></i> Curtir
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}

