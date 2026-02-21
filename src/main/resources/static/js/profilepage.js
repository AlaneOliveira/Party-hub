const urlParams = new URLSearchParams(window.location.search);
const profileId = urlParams.get('id'); // ID do personagem visitado
const myCharId = sessionStorage.getItem('selectedCharId'); // Nosso personagem logado

console.log("Profile ID:", profileId);

window.onload = async function() {
    if (!profileId || profileId === "undefined") {
        console.error("ID do perfil não encontrado. Redirecionando para a página inicial.");
        window.location.href = 'charhome.html';
        return;
    }
    loadProfile();
};

async function loadProfile() {
    const token = sessionStorage.getItem('token');
    const response = await fetch(`${BASE_URL}/gamer/character/${profileId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });

    if (response.ok) {
        const char = await response.json();
        document.getElementById('profileName').innerText = char.name;
        document.getElementById('profileGame').innerText = char.gameTitle;
        document.getElementById('profileImage').src = char.imageUrl || 'images/default-avatar.jpg';
        
        const isMe = (profileId == myCharId);
        
        if (isMe) {
            // Lógica para o MEU perfil
            document.getElementById('editPhotoBtn').classList.remove('d-none');
            document.getElementById('manageBtn').classList.remove('d-none');
            document.getElementById('postsTitle').innerText = "Minhas Postagens";
            loadFollowingList(); // Carrega o modal
        } else {
            // Lógica para perfil de OUTROS
            document.getElementById('followBtn').classList.remove('d-none');
            checkIfFollowing(); // Verifica se o botão deve ser "Seguindo"
        }

        loadCharacterPosts(); // Carrega apenas posts do ID da URL
    }
}

async function checkIfFollowing() {
    // Aqui você pode criar um endpoint no Java que retorne um boolean 
    // ou filtrar a lista de quem você segue no front
}

async function loadCharacterPosts() {
    const token = sessionStorage.getItem('token');
    // Você vai precisar criar essa rota no PostController: /post/character/{id}
    const response = await fetch(`${BASE_URL}/post/character/${profileId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    if (response.ok) {
        const posts = await response.json();
        renderProfilePosts(posts);
    }
}

async function uploadPhoto(file) {
    if (!file) return;
    const token = sessionStorage.getItem('token');
    const formData = new FormData();
    formData.append('file', file);

    const response = await fetch(`${BASE_URL}/gamer/character/${myCharId}/photo`, {
        method: 'PATCH',
        headers: { 'Authorization': `Bearer ${token}` },
        body: formData
    });

    if (response.ok) {
        alert("Foto atualizada!");
        location.reload();
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