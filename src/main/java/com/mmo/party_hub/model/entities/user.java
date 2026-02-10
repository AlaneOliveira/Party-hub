package com.mmo.party_hub.model.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity; // impor
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; // Importação necessária para o @Id funcionar


@Entity // anotação para indicar que esta classe é uma entidade JPA, ou seja, mapeada para uma tabela no banco de dados, chamada  "User" para o nosso usuario
public class user { // atributos comuns a todos os tipos de usuário

    @Id // mostra que este atributo é a chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Isso gera o AUTO_INCREMENT
    private Integer id;
    @Column(length = 100,nullable = false, unique = true) // Define que o campo não pode ser nulo e deve ser único
    private String name;
    private String email;
    private String password;


    // O Getter é como uma janela. Ele permite que você veja o valor da variável, mas não permite que você a altere diretamente.
    public Integer getId() {
        return id;
    }


    // O Setter é como uma porta. Ele recebe um valor novo e o coloca dentro da variável da classe.
    public void setId(Integer id) {
        this.id = id;
    }


    // Getters e Setters para os outros campos seguindo a mesma lógica:


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }
}
