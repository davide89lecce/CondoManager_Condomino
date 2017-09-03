package com.gambino_serra.condomanager_condomino.Model.Entity;

public class Condomino {
    private String nome;
    private String codice_fiscale;
    private String email;
    private String telefono;
    private String stabile;
    private String interno;


    public Condomino(String nome,
                     String codice_fiscale,
                     String email,
                     String telefono,
                     String stabile,
                     String interno)
    {
        this.nome = nome;
        this.codice_fiscale = codice_fiscale;
        this.email = email;
        this.telefono = telefono;
        this.stabile = stabile;
        this.interno = interno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodice_fiscale() {
        return codice_fiscale;
    }

    public void setCodice_fiscale(String codice_fiscale) {
        this.codice_fiscale = codice_fiscale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getStabile() {
        return stabile;
    }

    public void setStabile(String stabile) {
        this.stabile = stabile;
    }

    public String getInterno() {
        return interno;
    }

    public void setInterno(String interno) {
        this.interno = interno;
    }

}