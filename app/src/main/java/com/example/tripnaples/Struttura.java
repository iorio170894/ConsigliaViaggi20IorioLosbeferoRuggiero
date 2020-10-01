package com.example.tripnaples;

public class Struttura {
    private int cod_struttura;
    private String indirizzo;
    private int range_prezzo;
    private double latitudine;
    private double longitudine;
    private String nome;
    private String città;
    private String tipo_struttura;
    private String link_immagine;


    public Struttura (int cod_struttura, String indirizzo, int range_prezzo, double latitudine,
                      double longitudine, String nome, String città, String tipo_struttura, String link_immagine){
        this.cod_struttura=cod_struttura;
        this.indirizzo=indirizzo;
        this.range_prezzo=range_prezzo;
        this.latitudine=latitudine;
        this.longitudine=longitudine;
        this.nome=nome;
        this.città=città;
        this.tipo_struttura=tipo_struttura;
        this.link_immagine=link_immagine;
    }


    @Override
    public String toString() {
        return "Struttura{" +
                "cod_struttura=" + cod_struttura +
                ", \nindirizzo='" + indirizzo + '\'' +
                ", \nrange_prezzo=" + range_prezzo +
                ", \nlatitudine='" + latitudine + '\'' +
                ", \nlongitudine='" + longitudine + '\'' +
                ", \nnome='" + nome + '\'' +
                ", \ncittà='" + città + '\'' +
                ", \ntipo_struttura='" + tipo_struttura + '\'' +
                ", \nlink_immagine='" + link_immagine + '\'' +
                "}\n\n";
    }

    public String getLink_immagine() {
        return link_immagine;
    }

    public void setLink_immagine(String link_immagine) {
        this.link_immagine = link_immagine;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCod_struttura() {
        return cod_struttura;
    }

    public void setCod_struttura(int cod_struttura) {
        this.cod_struttura = cod_struttura;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public int getRange_prezzo() {
        return range_prezzo;
    }

    public void setRange_prezzo(int range_prezzo) {
        this.range_prezzo = range_prezzo;
    }

    public String getCittà() {
        return città;
    }

    public void setCittà(String città) {
        this.città = città;
    }

    public String getTipo_struttura() {
        return tipo_struttura;
    }

    public void setTipo_struttura(String tipo_struttura) {
        this.tipo_struttura = tipo_struttura;
    }
}
