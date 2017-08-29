package com.gambino_serra.condomanager_condomino.Old_Model.JsonSerializable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by condomanager_condomino on 13/02/17.
 */

public class JsonIntervento {

    @SerializedName("idIntervento")
    public Integer idIntervento;
    @SerializedName("data")
    public String data;
    @SerializedName("intervento")
    public String intervento;
    @SerializedName("segnalazione")
    public String segnalazione;
}
