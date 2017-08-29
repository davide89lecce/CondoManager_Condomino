package com.gambino_serra.condomanager_condomino.Old_Model.JsonSerializable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by condomanager_condomino on 13/02/17.
 */

public class JsonCondomino {

    @SerializedName("usernameC")
    public String username;
    @SerializedName("nome")
    public String nome;
    @SerializedName("cognome")
    public String cognome;
    @SerializedName("condominio")
    public String condominio;
}
