package com.example.UrbanTrip.Models;

public class RecensioneApprovata {

    int codRecensione;
    double numeroStelle;
    String descrizioneTestuale;
    String codiceStruttura;
    String utente;

    public RecensioneApprovata (int codRecensione, double numeroStelle, String descrizioneTestuale, String codiceStruttura, String utente){
        this.codRecensione=codRecensione;
        this.numeroStelle=numeroStelle;
        this.descrizioneTestuale=descrizioneTestuale;
        this.codiceStruttura=codiceStruttura;
        this.utente=utente;
    }

    public int getCodRecensione() {
        return codRecensione;
    }

    public void setCodRecensione(int codRecensione) {
        this.codRecensione = codRecensione;
    }

    public double getNumeroStelle() {
        return numeroStelle;
    }

    public void setNumeroStelle(double numeroStelle) {
        this.numeroStelle = numeroStelle;
    }

    public String getDescrizioneTestuale() {
        return descrizioneTestuale;
    }

    public void setDescrizioneTestuale(String descrizioneTestuale) {
        this.descrizioneTestuale = descrizioneTestuale;
    }

    public String getCodiceStruttura() {
        return codiceStruttura;
    }

    public void setCodiceStruttura(String codiceStruttura) {
        this.codiceStruttura = codiceStruttura;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }
}
