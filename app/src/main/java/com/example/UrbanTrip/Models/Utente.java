package com.example.UrbanTrip.Models;


public class Utente {
    private String nomeCognome;
    private int  id;
    private String email;
    private String nickname;

    public Utente( String nomeCognome, String email, String nickname) {
        this.nomeCognome = nomeCognome;
        this.email = email;
        this.nickname=nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = (id);
    }

    public String getNomeCognome() {
        return nomeCognome;
    }

    public void setNomeCognome(String nomeCognome) {
        this.nomeCognome = (nomeCognome);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = (email);
    }

    @Override
    public String toString(){
        return getId()+getNomeCognome();
    }

}

