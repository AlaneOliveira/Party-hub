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
    const response = await fetch(`${BASE_URL}/post`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    if (response.ok) {
        const posts = await response.json();
        renderFeed(posts);
    }
}
  
async function buscarMesmoJogo() {
    const playersList = document.getElementById('playersList');
    playersList.innerHTML = '<p class="text-warning">Buscando jogadores do seu jogo...</p>';
}

async function darLike(postId) {
    const token = sessionStorage.getItem('token');
    const response = await fetch(`${BASE_URL}/like`, {
        method: 'POST',
        headers: { 
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` 
        },
        body: JSON.stringify({ postId: postId })
    });
    if (response.ok) loadFeed(); 
}

async function comentar(postId, parentId = null) {
    const token = sessionStorage.getItem('token'); 
    const inputId = parentId ? `reply-input-${parentId}` : `comment-input-${postId}`;
    const input = document.getElementById(inputId);
    
    if (!input || !input.value.trim()) {
        alert("Escreva algo antes de enviar!");
        return;
    }

    const payload = {
        content: input.value,
        postId: postId,
        characterId: parseInt(selectedCharId)
    };
 
    if (parentId) {
        payload.id = parentId;
    }

    try {
        const response = await fetch(`${BASE_URL}/comment`, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json', 
                'Authorization': `Bearer ${token}` 
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            input.value = '';
            if (parentId) abrirFormResposta(postId, parentId); // Agora as variáveis existem no escopo
            loadFeed(); 
        } else {
            const erroTxt = await response.text();
            console.error("Erro no servidor:", erroTxt);
            alert("Não foi possível comentar.");
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
    }
}

function renderFeed(posts) {
    const container = document.getElementById('feedContainer');
    container.innerHTML = posts.map(post => `
        <div class="card bg-secondary text-white mb-3 shadow-sm border-0">
            <div class="card-header d-flex align-items-center bg-transparent border-0">
                <img src="${post.charPhoto || 'images/default-avatar.jpg'}" class="rounded-circle me-2" style="width: 40px; height: 40px; object-fit: cover;">
                <div>
                    <h6 class="mb-0">${post.charName}</h6>
                    <small class="text-light opacity-75">${new Date(post.date).toLocaleString()}</small>
                </div>
            </div>
            <div class="card-body py-2">
                <h5 class="card-title">${post.title}</h5>
                <p class="card-text">${post.content}</p>
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <button onclick="darLike(${post.id})" class="btn btn-sm btn-dark">
                        ❤️ ${post.likes || 0}
                    </button>
                    <button class="btn btn-sm btn-outline-light" onclick="toggleCommentInput(${post.id})">Comentar</button>
                </div>

                <div class="card-footer bg-dark bg-opacity-25 border-0 rounded">
                    <div id="input-container-${post.id}" class="d-none mb-3">
                        <div class="d-flex">
                            <input type="text" id="comment-input-${post.id}" class="form-control form-control-sm me-2" placeholder="Escreva um comentário...">
                            <button onclick="comentar(${post.id})" class="btn btn-sm btn-primary">Enviar</button>
                        </div>
                    </div>

                    <div id="comments-list-${post.id}">
                        ${renderComments(post.topComments, post.id)}
                    </div>
                </div>
            </div>
        </div>
    `).join('');
} 
 
// Função que renderiza comentários e seus subcomentários
function renderComments(comments, postId, isReply = false) {
    if (!comments || comments.length === 0) return isReply ? '' : '<small class="text-muted">Sem comentários ainda.</small>';

    return comments.map(c => `
        <div class="${isReply ? 'ms-4 border-start ps-3 mt-2' : 'mb-3'} border-secondary">
            <div class="d-flex align-items-center mb-1">
                <img src="${c.charPhoto || 'images/default-avatar.jpg'}" class="rounded-circle me-2" style="width: ${isReply ? '20px' : '30px'}; height: ${isReply ? '20px' : '30px'};">
                <strong style="font-size: 0.85rem;">${c.charName}</strong>
                <small class="ms-auto text-warning" style="font-size: 0.75rem;">❤️ ${c.likesCount}</small>
            </div>
            <p class="small mb-1">${c.content}</p>
            <div class="d-flex gap-2">
                <button onclick="abrirFormResposta(${postId}, ${c.id})" class="btn btn-link text-info p-0 btn-very-sm" style="font-size: 0.7rem; text-decoration: none;">Responder</button>
            </div>
            
            <div id="reply-input-container-${c.id}" class="mt-2 d-none">
                <div class="d-flex">
                    <input type="text" id="reply-input-${c.id}" class="form-control form-control-sm me-2" placeholder="Responder a ${c.charName}...">
                    <button onclick="comentar(${postId}, ${c.id})" class="btn btn-sm btn-info">Ok</button>
                </div>
            </div>

            <div class="sub-comments">
                ${renderComments(c.replies, postId, true)}
            </div>
        </div>
    `).join('');
}

// Funções de auxílio para a interface
function toggleCommentInput(postId) {
    const el = document.getElementById(`input-container-${postId}`);
    el.classList.toggle('d-none');
}

function abrirFormResposta(postId, commentId) {
    const el = document.getElementById(`reply-input-container-${commentId}`);
    el.classList.toggle('d-none');
}