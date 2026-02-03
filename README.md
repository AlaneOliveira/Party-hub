# 🎮 MMO Social Platform

## 📌 Descrição do Projeto
O **MMO Social Platform** é um sistema de gerenciamento de personagens de jogos MMO que funciona como uma rede social voltada para gamers.  
Nele, usuários podem se cadastrar, criar personagens de seus jogos favoritos e interagir com outros personagens por meio de postagens, curtidas, comentários, seguidores e transmissões ao vivo de gameplays.

O projeto foi desenvolvido com foco acadêmico e também como peça de portfólio, utilizando **Java com Spring Boot no backend**, incluindo **Spring Security**, e um frontend desenvolvido com tecnologia livre.

---

## 🧠 Objetivo
Permitir que gamers:
- Gerenciem múltiplos personagens de diferentes jogos
- Interajam socialmente como seus personagens
- Compartilhem experiências, postagens e gameplays
- Acompanhem personagens do mesmo jogo ou de interesse

---

## 👤 Fluxo do Usuário

### 🔹 Tela Inicial
- Opção de **Cadastro**
- Opção de **Login**

### 🔹 Cadastro
- Formulário de criação de usuário
- Após o cadastro, o usuário retorna para a tela de login

### 🔹 Login
- Autenticação do usuário
- Redirecionamento para a **Tela Inicial do Gamer**

---

## 🎮 Funcionalidades – Tela Inicial do Gamer
- Criar novos personagens
- Associar personagens a jogos
- Visualizar lista de personagens cadastrados
- Acessar a tela de um personagem ao clicar em sua imagem

---

## 🧙 Funcionalidades – Tela Inicial do Personagem
- Visualizar postagens de personagens seguidos
- Curtir e comentar postagens
- Criar novas postagens
- Pesquisar personagens do mesmo jogo
- Seguir outros personagens
- Visualizar lives de personagens seguidos

---

## 👥 Perfil do Personagem
- Informações básicas do personagem
- Lista de todas as postagens realizadas
- Acesso ao conteúdo público do personagem

---

## 📺 Lives de Gameplay
> Funcionalidade opcional de acordo com o avanço do projeto

- Transmissão da tela do jogador utilizando **WebSocket**
- Visualização das lives na tela inicial do personagem
- Acesso à live ao clicar no personagem transmissor

---

## 🛠️ Tecnologias Utilizadas

### 🔙 Backend
- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Banco de dados relacional (MySQL / PostgreSQL / H2)
- WebSocket (para lives)

### 🔜 Frontend
- Tecnologia livre (React, Angular, Vue, HTML/CSS/JS, etc.)

### 🔧 Outros
- Maven
- Git e GitHub

---

## 🔐 Segurança
- Autenticação e autorização com **Spring Security**
- Usuários autenticados para acesso às funcionalidades
- Separação de permissões entre Gamer e Personagem

---

## 🎥 Vídeo de Apresentação
Ao final do desenvolvimento, será gravado um vídeo demonstrando as funcionalidades do sistema.  
O vídeo será disponibilizado em uma plataforma como **YouTube** ou **Vimeo**.

📎 Link do vídeo: *(a ser adicionado)*

---

## 👨‍💻 Desenvolvedores
Projeto desenvolvido em dupla para fins acadêmicos.

- AlaneOliveira
- EduardaRFSousa

---

## 📌 Observações
Este projeto pode ser utilizado como **portfólio profissional**, demonstrando conhecimentos em:
- Backend com Spring Boot
- Segurança de aplicações
- Arquitetura REST
- Integração frontend/backend
- Conceitos de redes sociais
- WebSocket e aplicações em tempo real
