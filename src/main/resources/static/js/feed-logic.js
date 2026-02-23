// Variáveis de estado globais para a UI
let openToggles = new Set();
let openInputs = new Set();

// Função para contar comentários e subcomentários
function countTotalComments(comments) {
    if (!comments) return 0;
    let count = comments.length;
    comments.forEach(c => {
        if (c.replies) count += countTotalComments(c.replies);
    });
    return count;
}

// Renderização Principal do Feed
function renderFeed(posts, containerId = 'feedContainer', isProfilePage = false) {
    const container = document.getElementById(containerId);
    if (!container) return;
    
    if (!posts || posts.length === 0) {
        let emptyMsg = "";
        
        if (isProfilePage) {
            const profileId = new URLSearchParams(window.location.search).get('id');
            const myCharId = sessionStorage.getItem('selectedCharId');
            
            emptyMsg = (profileId == myCharId) 
                ? "Você não possui nenhuma postagem!" 
                : "Este personagem não possui postagens!";
        } else {
            emptyMsg = "Seu feed está vazio! Siga jogadores do seu universo para ver o que eles estão postando.";
        }

        container.innerHTML = `
            <div class="text-center mt-5 p-4 bg-secondary rounded shadow-sm border border-dark">
                <i class="bi bi-chat-dots" style="font-size: 2.5rem; color: #ffc107;"></i>
                <h5 class="mt-3 text-white">${emptyMsg}</h5>
            </div>
        `;
        return;
    }

    container.innerHTML = posts.map(post => {  
        const isVisibleClass = openToggles.has(post.id) ? '' : 'd-none';

        // Lógica para deletar Posts (Dono do post)
        const isMyPost = post.characterId == sessionStorage.getItem('selectedCharId');
        const deleteBtn = isMyPost ? `
            <button onclick="deletePost(${post.id})" class="btn btn-sm btn-outline-danger border-0 ms-auto" title="Excluir Post">
                <i class="bi bi-trash3"></i>
            </button>` : '';

        return `
        <div class="card bg-secondary text-white mb-3 shadow-sm border-0 text-start">
            <div class="card-header d-flex align-items-center bg-transparent border-0">
                <img src="${post.charPhoto || 'images/default-avatar.jpg'}" 
                     class="rounded-circle me-2" 
                     style="width: 40px; height: 40px; object-fit: cover; cursor: pointer;"
                     onclick="window.location.href='profilepage.html?id=${post.characterId}'">
                <div> 
                    <h6 class="mb-0" style="cursor: pointer" onclick="window.location.href='profilepage.html?id=${post.characterId}'">
                        ${post.charName}
                    </h6>
                    <small class="text-light opacity-75">${new Date(post.date).toLocaleString()}</small>
                </div>
                ${deleteBtn}
            </div>
            <div class="card-body py-2">
                <h5 class="card-title">${post.title}</h5>
                <p class="card-text">${post.content}</p>
                <div class="d-flex justify-content-between align-items-center mb-3"> 
                    <button id="like-btn-${post.id}" onclick="darLike(${post.id})" 
                            class="btn btn-sm ${post.alreadyLiked ? 'btn-danger' : 'btn-dark'}">
                        ❤️ ${post.likesCount || 0}
                    </button>
                    <div>
                        <button class="btn btn-sm btn-outline-light me-2" onclick="toggleCommentInput(${post.id})">Comentar</button>
                        <button class="btn btn-sm btn-link text-white text-decoration-none" onclick="toggleAllComments(${post.id})">
                             <i class="bi bi-chat-left-text"></i> Ver comentários (${countTotalComments(post.topComments)})
                        </button>
                    </div>
                </div>

                <div class="card-footer bg-dark bg-opacity-25 border-0 rounded ${isVisibleClass}" id="comments-section-${post.id}">
                    <div id="input-container-${post.id}" class="mb-3 ${openInputs.has(`post-${post.id}`) ? '' : 'd-none'}">
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
        </div>`;
    }).join('');
}

// Funções de Interação (Likes e Comentários)
async function darLike(postId) {
    const token = sessionStorage.getItem('token');
    const charId = sessionStorage.getItem('selectedCharId');
    const btn = document.getElementById(`like-btn-${postId}`);
    const currentLikes = parseInt(btn.innerText.replace(/[^\d]/g, '')) || 0;
    const isRemovingLike = btn.classList.contains('btn-danger');

    if (isRemovingLike) {
        btn.classList.replace('btn-danger', 'btn-dark');
        btn.innerHTML = `❤️ ${Math.max(0, currentLikes - 1)}`;
    } else {
        btn.classList.replace('btn-dark', 'btn-danger');
        btn.innerHTML = `❤️ ${currentLikes + 1}`;
    }
    btn.disabled = true;

    try {
        await fetch(`${BASE_URL}/like`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
            body: JSON.stringify({ postId, characterId: parseInt(charId) })
        });
    } catch (error) {
        console.error(error);
        loadFeed(); // Função global definida nos scripts específicos
    } finally {
        btn.disabled = false;
    }
}

async function darLikeComentario(commentId) {
    const token = sessionStorage.getItem('token');
    const charId = sessionStorage.getItem('selectedCharId');
    const btn = document.getElementById(`comment-like-btn-${commentId}`);
    btn.disabled = true;

    try {
        const response = await fetch(`${BASE_URL}/comment/like`, { 
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
            body: JSON.stringify({ commentId, characterId: parseInt(charId) })
        });
        if (response.ok) loadFeed();
    } finally {
        btn.disabled = false;
    }
}

async function comentar(postId, parentId = null) {
    const token = sessionStorage.getItem('token');
    const selectedCharId = sessionStorage.getItem('selectedCharId');
    const inputId = parentId ? `reply-input-${parentId}` : `comment-input-${postId}`;
    const input = document.getElementById(inputId);
    
    if (!input || !input.value.trim()) return;

    const payload = { content: input.value, postId, characterId: parseInt(selectedCharId) };
    if (parentId) payload.id = parentId;

    try {
        const response = await fetch(`${BASE_URL}/comment`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            input.value = '';
            openToggles.add(postId); 
            openInputs.delete(parentId ? `reply-${parentId}` : `post-${postId}`); 
            loadFeed(); 
        }
    } catch (error) {
        console.error(error);
    }
}

// Funções de UI (Toggles)
function toggleAllComments(postId) {
    const section = document.getElementById(`comments-section-${postId}`);
    section.classList.toggle('d-none');
    if (section.classList.contains('d-none')) openToggles.delete(postId);
    else openToggles.add(postId);
}

function toggleCommentInput(postId) {
    const section = document.getElementById(`comments-section-${postId}`);
    if (section.classList.contains('d-none')) {
        section.classList.remove('d-none');
        openToggles.add(postId);
    }
    const inputKey = `post-${postId}`;
    if (openInputs.has(inputKey)) openInputs.delete(inputKey);
    else openInputs.add(inputKey);
    loadFeed();
}

function abrirFormResposta(postId, commentId) {
    const inputKey = `reply-${commentId}`;
    if (openInputs.has(inputKey)) openInputs.delete(inputKey);
    else openInputs.add(inputKey);
    loadFeed();
}

// Renderização de Comentários
function renderComments(comments, postId, isReply = false) {
    if (!comments || comments.length === 0) return isReply ? '' : '<small class="text-muted">Sem comentários ainda.</small>';
    
    const myCharId = sessionStorage.getItem('selectedCharId');

    return comments.map(c => {
        // Lógica para deletar Comentários (Dono do comentário)
        const isMyComment = c.charId == myCharId;
        const deleteCommentsBtn = isMyComment ? `
            <button onclick="deleteComment(${c.id})" class="btn btn-link text-danger p-0 ms-1" style="font-size: 0.75rem; text-decoration: none;" title="Excluir Comentário">
                <i class="bi bi-trash"></i>
            </button>` : '';

        return `
        <div class="${isReply ? 'ms-4 border-start ps-3 mt-2' : 'mb-3'} border-secondary">
            <div class="d-flex align-items-center mb-1">
                <img src="${c.charPhoto || 'images/default-avatar.jpg'}" 
                     class="rounded-circle me-2" 
                     style="width: ${isReply ? '20px' : '30px'}; height: ${isReply ? '20px' : '30px'}; cursor: pointer;"
                     onclick="window.location.href='profilepage.html?id=${c.charId}'">
                <strong style="font-size: 0.85rem; cursor: pointer;" 
                        onclick="window.location.href='profilepage.html?id=${c.charId}'">
                    ${c.charName}
                </strong>
                ${deleteCommentsBtn}
                <button id="comment-like-btn-${c.id}" onclick="darLikeComentario(${c.id})" class="btn btn-link text-warning p-0 ms-auto" style="font-size: 0.75rem; text-decoration: none;">
                    ❤️ ${c.likesCount || 0}
                </button>
            </div>
            <p class="small mb-1">${c.content}</p>
            <div class="d-flex gap-2">
                <button onclick="abrirFormResposta(${postId}, ${c.id})" class="btn btn-link text-info p-0 btn-very-sm" style="font-size: 0.7rem; text-decoration: none;">Responder</button>
            </div>
            <div id="reply-input-container-${c.id}" class="mt-2 ${openInputs.has(`reply-${c.id}`) ? '' : 'd-none'}">
                <div class="d-flex">
                    <input type="text" id="reply-input-${c.id}" class="form-control form-control-sm me-2" placeholder="Responder a ${c.charName}...">
                    <button onclick="comentar(${postId}, ${c.id})" class="btn btn-sm btn-info">Ok</button>
                </div>
            </div>
            <div class="sub-comments">${renderComments(c.replies, postId, true)}</div>
        </div>`;
    }).join('');
}

async function deletePost(postId) {
    if (!confirm("Deseja realmente excluir esta postagem?")) return;
    
    try {
        const response = await fetch(`${BASE_URL}/post/${postId}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}` }
        });
        
        if (response.ok) {
            loadFeed(); // Recarrega o feed dinamicamente
        }
    } catch (error) {
        console.error("Erro ao deletar post:", error);
    }
}

async function deleteComment(commentId) {
    if (!confirm("Excluir este comentário?")) return;

    try {
        const response = await fetch(`${BASE_URL}/comment/${commentId}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}` }
        });
        
        if (response.ok) {
            loadFeed();
        }
    } catch (error) {
        console.error("Erro ao deletar comentário:", error);
    }
}