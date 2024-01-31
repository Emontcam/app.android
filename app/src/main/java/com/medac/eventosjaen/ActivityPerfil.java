package com.medac.eventosjaen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityPerfil extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
    }
    public void irEventos(View v){
        Intent i = new Intent(this, ActivityInicio.class);
        startActivity(i);
    }
    public void irFavoritos(View v){
        Intent i = new Intent(this, ActivityFavoritos.class);
        startActivity(i);
    }
}
