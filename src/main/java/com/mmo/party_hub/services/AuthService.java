package com.mmo.party_hub.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mmo.party_hub.dto.LoginDTO;
import com.mmo.party_hub.dto.RegisterDTO;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.repositories.GamerRepository;
import com.mmo.party_hub.security.JwtUtils;

@Component
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private GamerRepository gamerRepo;
    
    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<?> newGamer(Gamer gamer){

        gamer.setPassword(this.encoder.encode(gamer.getPassword()));

        this.gamerRepo.save(gamer);
        
        return ResponseEntity.ok("Success!");

    }

    // 1. PRIMEIRO PASSO: O usuário se cadastra
    public ResponseEntity<?> register(RegisterDTO dados) {
        // Verifica se o email já existe no banco
        if (gamerRepo.findByEmail(dados.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Erro: Este e-mail já está cadastrado!");
        }

        // Criamos o objeto Gamer (Entidade) a partir dos dados do DTO
        Gamer novoGamer = new Gamer();
        novoGamer.setName(dados.getName());
        novoGamer.setEmail(dados.getEmail());
        
        // Encriptamos a senha antes de salvar
        novoGamer.setPassword(this.encoder.encode(dados.getPassword()));

        // Salvamos no banco de dados
        this.gamerRepo.save(novoGamer);
        
        return ResponseEntity.ok("Cadastro realizado com sucesso! Agora você pode fazer login.");
    }

    // 2. SEGUNDO PASSO: O usuário faz login (precisa estar cadastrado)
    public ResponseEntity<?> login(LoginDTO login) {
        
        // Busca o usuário pelo email enviado no login
        // Se não encontrar, o Optional estará vazio
        Optional<Gamer> gamerOpt = this.gamerRepo.findByEmail(login.getLogin());

        if (gamerOpt.isPresent()) {
            Gamer gamer = gamerOpt.get();

            // Verifica se a senha enviada no login bate com a senha encriptada do banco
            if (this.encoder.matches(login.getPassword(), gamer.getPassword())) {
                // Se tudo estiver certo, gera o Token JWT
                String token = this.jwtUtils.generateToken(String.valueOf(gamer.getId()), "GAMER");
                return ResponseEntity.ok(token);
            }
        }

        // Se o usuário não existir ou a senha estiver errada
        return ResponseEntity.status(401).body("Credenciais inválidas ou usuário não cadastrado.");
    }
}

