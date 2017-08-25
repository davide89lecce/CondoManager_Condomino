package com.gambino_serra.condomanager_condomino.Model.Entity;

/**
 * Created by condomanager_condomino on 13/02/17.
 */

public class TicketIntervento {

    private String idTicketIntervento;
    private String uidAmministratore;
    private String dataTicket;
    private String dataUltimoAggiornamento;
    private String fornitore;
    private String messaggioCondomino;
    private String noteCondomini;
    private String oggetto;
    private String priorita;
    private String rapportiIntervento; //link
    private String richiesta;
    private String stabile;
    private String stato;

    public TicketIntervento(String idTicketIntervento, String uidAmministratore, String dataTicket, String dataUltimoAggiornamento, String fornitore, String messaggioCondomino, String noteCondomini, String oggetto, String priorita, String rapportiIntervento, String richiesta, String stabile, String stato) {
        this.idTicketIntervento = idTicketIntervento;
        this.uidAmministratore = uidAmministratore;
        this.dataTicket = dataTicket;
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
        this.fornitore = fornitore;
        this.messaggioCondomino = messaggioCondomino;
        this.noteCondomini = noteCondomini;
        this.oggetto = oggetto;
        this.priorita = priorita;
        this.rapportiIntervento = rapportiIntervento;
        this.richiesta = richiesta;
        this.stabile = stabile;
        this.stato = stato;
    }

    public String getIdTicketIntervento() {
        return idTicketIntervento;
    }

    public void setIdTicketIntervento(String idTicketIntervento) {
        this.idTicketIntervento = idTicketIntervento;
    }

    public String getUidAmministratore() {
        return uidAmministratore;
    }

    public void setUidAmministratore(String uidAmministratore) {
        this.uidAmministratore = uidAmministratore;
    }

    public String getDataTicket() {
        return dataTicket;
    }

    public void setDataTicket(String dataTicket) {
        this.dataTicket = dataTicket;
    }

    public String getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }

    public void setDataUltimoAggiornamento(String dataUltimoAggiornamento) {
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }

    public String getFornitore() {
        return fornitore;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public String getMessaggioCondomino() {
        return messaggioCondomino;
    }

    public void setMessaggioCondomino(String messaggioCondomino) {
        this.messaggioCondomino = messaggioCondomino;
    }

    public String getNoteCondomini() {
        return noteCondomini;
    }

    public void setNoteCondomini(String noteCondomini) {
        this.noteCondomini = noteCondomini;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getPriorita() {
        return priorita;
    }

    public void setPriorita(String priorita) {
        this.priorita = priorita;
    }

    public String getRapportiIntervento() {
        return rapportiIntervento;
    }

    public void setRapportiIntervento(String rapportiIntervento) {
        this.rapportiIntervento = rapportiIntervento;
    }

    public String getRichiesta() {
        return richiesta;
    }

    public void setRichiesta(String richiesta) {
        this.richiesta = richiesta;
    }

    public String getStabile() {
        return stabile;
    }

    public void setStabile(String stabile) {
        this.stabile = stabile;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
