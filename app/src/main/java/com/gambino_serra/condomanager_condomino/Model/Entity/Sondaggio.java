package com.gambino_serra.condomanager_condomino.Model.Entity;


public class Sondaggio {
    private String idSondaggio;
    private String stabile;
    private String tipologia;
    private String oggetto;
    private String descrizione;
    private String opzione1;
    private String opzione2;
    private String opzione3;
    private String data;
    private String stato;

    public Sondaggio(String idSondaggio, String tipologia, String oggetto, String descrizione, String data, String stato) {
        this.idSondaggio = idSondaggio;
        this.tipologia = tipologia;
        this.oggetto = oggetto;
        this.descrizione = descrizione;
        this.data = data;
        this.stato = stato;
    }

    public Sondaggio(String idSondaggio, String stabile, String tipologia, String oggetto, String descrizione, String opzione1, String opzione2, String opzione3, String data, String stato) {
        this.idSondaggio = idSondaggio;
        this.stabile = stabile;
        this.tipologia = tipologia;
        this.oggetto = oggetto;
        this.descrizione = descrizione;
        this.opzione1 = opzione1;
        this.opzione2 = opzione2;
        this.opzione3 = opzione3;
        this.data = data;
        this.stato = stato;
    }


    public String getIdSondaggio() {
        return idSondaggio;
    }

    public void setIdSondaggio(String idSondaggio) {
        this.idSondaggio = idSondaggio;
    }

    public String getStabile() {
        return stabile;
    }

    public void setStabile(String stabile) {
        this.stabile = stabile;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getOpzione1() {
        return opzione1;
    }

    public void setOpzione1(String opzione1) {
        this.opzione1 = opzione1;
    }

    public String getOpzione2() {
        return opzione2;
    }

    public void setOpzione2(String opzione2) {
        this.opzione2 = opzione2;
    }

    public String getOpzione3() {
        return opzione3;
    }

    public void setOpzione3(String opzione3) {
        this.opzione3 = opzione3;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
