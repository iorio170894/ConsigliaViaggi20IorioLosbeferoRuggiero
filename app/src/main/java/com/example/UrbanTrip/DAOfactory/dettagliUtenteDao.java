package com.example.UrbanTrip.DAOfactory;

import android.content.Context;
import android.widget.TextView;

public interface dettagliUtenteDao {

    public void getDettagliUtente(final Context context, final TextView textEmail, final TextView textNickname, final TextView textNomeCognome);
    public void getNicknameUtente(final Context context, final TextView textFirma);

}
