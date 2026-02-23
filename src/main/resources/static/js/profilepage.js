const urlParams = new URLSearchParams(window.location.search);
const profileId = urlParams.get('id'); 
const myCharId = sessionStorage.getItem('selectedCharId'); 

window.onload = async function() {
    if (!profileId || profileId === "undefined") {
        window.location.href = 'charhome.html';
        return;
    }
    loadProfile();
};

// Esta função é chamada pelo feed-logic.js após likes/comentários
async function loadFeed() {
    const token = sessionStorage.getItem('token');
    const response = await fetch(`${BASE_URL}/post/character/${profileId}?viewerId=${myCharId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    if (response.ok) {
        const posts = await response.json();
        // O terceiro parâmetro 'true' indica que é uma página de perfil
        renderFeed(posts, 'characterPosts', true); 
    }
}

async function loadProfile() {
    const token = sessionStorage.getItem('token');
    const response = await fetch(`${BASE_URL}/gamer/character/${profileId}?viewerId=${myCharId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });

    if (response.ok) {
        const char = await response.json();
        
        // Dados básicos
        document.getElementById('profileName').innerText = char.name;
        document.getElementById('profileGame').innerText = char.gameTitle;
        document.getElementById('profileImage').src = char.imageUrl || 'images/default-avatar.jpg';

        const btnFollow = document.getElementById('followBtn');
        const btnEditPhoto = document.getElementById('editPhotoBtn');
        const isMe = (profileId == myCharId);

        if (isMe) {
            btnFollow.classList.add('d-none');
            btnEditPhoto.classList.remove('d-none');
            document.getElementById('postsTitle').innerText = "Minhas Postagens";
            document.getElementById('navtitle').innerText = "Meu Perfil";
        } else {
            btnFollow.classList.remove('d-none');
            btnFollow.innerText = char.following ? "Seguindo" : "Seguir";
            btnFollow.classList.replace(char.following ? 'btn-primary' : 'btn-outline-light', char.following ? 'btn-outline-light' : 'btn-primary');
            btnEditPhoto.classList.add('d-none');
        }

        // Configura os cliques dos contadores para abrir e carregar os modais
        document.getElementById('showFollowingBtn').onclick = () => loadList('following', profileId);
        document.getElementById('showFollowersBtn').onclick = () => loadList('followers', profileId);

        // Carrega o feed e as contagens
        loadFeed();
        updateFollowCounts();
    }
}

async function updateFollowCounts() {
    const token = sessionStorage.getItem('token');
    // Certifique-se de ter esse endpoint no Java
    const response = await fetch(`${BASE_URL}/gamer/character/${profileId}/counts`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });

    if (response.ok) {
        const counts = await response.json();
        document.getElementById('followersCount').innerText = counts.followers;
        document.getElementById('followingCount').innerText = counts.following;
    }
}

async function loadList(type, id) {
    const token = sessionStorage.getItem('token');
    const response = await fetch(`${BASE_URL}/gamer/character/${id}/${type}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });

    if (response.ok) {
        const list = await response.json();
        // Mapeia para o ID correto da div no seu HTML (followingList ou followersList)
        const containerId = type === 'following' ? 'followingList' : 'followersList';
        const container = document.getElementById(containerId);
        
        // Verifica se é o dono do perfil para mostrar o botão 'Remover'
        const isMeOnMyProfile = (profileId == myCharId); 

        // Caso a lista esteja vazia
        if (!list || list.length === 0) {
            const emptyMsg = type === 'following' 
                ? "Este personagem não segue ninguém." 
                : "Este personagem não possui seguidores.";
            container.innerHTML = `<p class="text-center text-muted my-3">${emptyMsg}</p>`;
            return;
        }

        // Renderiza a lista se houver dados
        container.innerHTML = list.map(char => `
            <div class="d-flex align-items-center justify-content-between mb-3 p-2 bg-dark rounded shadow-sm">
                <div class="d-flex align-items-center" style="cursor: pointer" onclick="window.location.href='profilepage.html?id=${char.id}'">
                    <img src="${char.imageUrl || 'images/default-avatar.jpg'}" class="rounded-circle me-3" style="width: 45px; height: 45px; object-fit: cover;">
                    <div>
                        <h6 class="mb-0 text-white">${char.name}</h6>
                        <small class="text-warning">${char.gameTitle}</small>
                    </div>
                </div>
                ${isMeOnMyProfile ? `
                    <button onclick="unfollowFromModal(${char.id}, '${type}')" class="btn btn-sm btn-outline-danger">
                        ${type === 'following' ? 'Remover' : 'Remover Seguidor'}
                    </button>
                ` : ''}
            </div>
        `).join('');
    } else {
        console.error(`Erro ao carregar lista de ${type}`);
    }
}

async function unfollowFromModal(targetId, type) {
    const token = sessionStorage.getItem('token');
    
    // Define quem é o seguidor e quem é o seguido com base no modal aberto
    // Se o modal for 'following', EU (myCharId) deixo de seguir o alvo (targetId)
    // Se o modal for 'followers', o alvo (targetId) deixa de me seguir (myCharId)
    const payload = type === 'following' 
        ? { followerId: parseInt(myCharId), followingId: parseInt(targetId) }
        : { followerId: parseInt(targetId), followingId: parseInt(myCharId) };

    const response = await fetch(`${BASE_URL}/gamer/character/follow`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
        body: JSON.stringify(payload)
    });

    if (response.ok) {
        loadList(type, profileId); // Recarrega a lista específica (seguindo ou seguidores)
        updateFollowCounts();      // Atualiza os números (0 seguindo / 0 seguidores)
    }
}
 
async function trocarFotoUrl() {
    const novaUrl = prompt("Cole a URL da nova imagem de perfil:");
    
    if (!novaUrl || novaUrl.trim() === "") return;

    const token = sessionStorage.getItem('token');
    // 'myCharId' deve ser capturado da URL da página ou do sessionStorage
    const charId = new URLSearchParams(window.location.search).get('id');

    try {
        const response = await fetch(`${BASE_URL}/gamer/character/${charId}/photo-url`, {
            method: 'PATCH',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}` 
            },
            body: JSON.stringify({ imageUrl: novaUrl })
        });

        if (response.ok) {
            // ATUALIZAÇÃO VISUAL: Busca o elemento da imagem e troca o src na hora
            const imgElement = document.getElementById('profileImage');
            if (imgElement) {
                imgElement.src = novaUrl;
            }
            alert("Foto atualizada com sucesso!");
        } else {
            alert("Erro ao atualizar a foto no servidor.");
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
    }
}

async function toggleFollow() {
    const token = sessionStorage.getItem('token');
    const btn = document.getElementById('followBtn');
    
    const response = await fetch(`${BASE_URL}/gamer/character/follow`, {
        method: 'POST',
        headers: { 
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` 
        },
        body: JSON.stringify({ 
            followerId: parseInt(myCharId), 
            followingId: parseInt(profileId) 
        })
    });

    if (response.ok) {
        const msg = await response.text();
        btn.innerText = msg === "Seguindo" ? "Seguindo" : "Seguir";
        btn.classList.toggle('btn-primary');
        btn.classList.toggle('btn-outline-light');
    }
} 