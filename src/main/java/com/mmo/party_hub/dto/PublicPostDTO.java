package com.mmo.party_hub.dto;

import com.mmo.party_hub.model.entities.Post;


public class PublicPostDTO {

    private String content;
    private String description;
    private String category;
    private long date;
    private double pot;

    public PublicPostDTO() {}

    public PublicPostDTO(Post c, double pot) {
        this.content = c.getContent();
        this.date = c.getDate(); // getDate() que é o da Entity
        this.pot = pot;

        // Se o post estiver respondendo a outro (que tem os dados)
        if (c.getPost() != null) {
            // Aqui é onde a mágica acontece: ele "herda" o contexto do pai
            // Se a sua lógica for diferente, esses campos podem vir de parâmetros
            this.category = "Categoria Exemplo"; 
            this.description = "Descrição Exemplo";
        }
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public double getPot() {
        return pot;
    }
    public void setPot(double pot) {
        this.pot = pot;
    }

}
