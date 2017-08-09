package com.gambino_serra.condomanager_condomino.Model.Entity;


public class MessaggioCondomino {

    private Integer idMessaggio;
    private String data;
    private String tipologia;
    private String messaggio;
    private Integer idCondomino;
    private Integer idAmministratore;


    public MessaggioCondomino(Integer idMessaggio, String data, String tipologia, String messaggio, Integer idCondomino, Integer idAmministratore) {
        this.idMessaggio = idMessaggio;
        this.data = data;
        this.tipologia = tipologia;
        this.messaggio = messaggio;
        this.idCondomino = idCondomino;
        this.idAmministratore = idAmministratore;
    }

    public Integer getIdMessaggio() {
        return idMessaggio;
    }

    public void setIdMessaggio(Integer idMessaggio) {
        this.idMessaggio = idMessaggio;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public Integer getIdCondomino() {
        return idCondomino;
    }

    public void setIdCondomino(Integer idCondomino) {
        this.idCondomino = idCondomino;
    }

    public Integer getIdAmministratore() {
        return idAmministratore;
    }

    public void setIdAmministratore(Integer idAmministratore) {
        this.idAmministratore = idAmministratore;
    }

}
