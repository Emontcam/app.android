package com.medac.eventosjaen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityFavoritos extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
    }

    public void irPerfil(View v){
        Intent i = new Intent(this, ActivityPerfil.class);
        startActivity(i);
    }
    public void irEventos(View v){
        Intent i = new Intent(this, ActivityInicio.class);
        startActivity(i);
    }


}
