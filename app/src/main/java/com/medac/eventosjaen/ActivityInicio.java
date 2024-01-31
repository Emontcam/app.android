package com.medac.eventosjaen;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ActivityInicio extends Activity {
    Resources res;
    ScrollView s;
    LinearLayout layaoutEvento;
    LinearLayout layaoutCat;
    ImageView img;
    TextView tituloEvento;
    TextView titulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //asignamos su elemento gráfico a cada variable
        layaoutEvento = findViewById(R.id.tarjeta);
        // img = findViewById(R.id.imagen);
        tituloEvento = findViewById(R.id.tituloEvento);
        s = findViewById(R.id.scroll);
        titulo = findViewById(R.id.titulo);
        //eliminamos el contenido del scroll
        layaoutEvento.removeAllViews();
        layaoutCat = findViewById(R.id.categoriasLayout);

        res = getResources();

        //actualizacion automática
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference cRef = db.collection("Eventos");
        cRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && !snapshot.isEmpty()) {

                    leerDatos();

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });


    }
    public void leerDatos(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //hacemos una referencia a la coleccion de la que queremos los datos
        db.collection("Eventos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Limpiamos el contenido actual
                            layaoutEvento.removeAllViews();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombre = document.getString("Nombre");
                                String imagenR = document.getString("Imagen");
                                String descrip = document.getString("Descripcion");
                                mostrar(nombre, imagenR, descrip);
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }
    public void leerDatos(View v){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Button boton = (Button) v;
        //cambiamos el color del boton
        String categoria = boton.getText().toString();
        restaurarBotones(categoria);
        //hacemos una referencia a la coleccion de la que queremos los datos
        db.collection("Eventos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Limpiamos el contenido actual
                            layaoutEvento.removeAllViews();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombre = document.getString("Nombre");
                                String imagenR = document.getString("Imagen");
                                String descrip = document.getString("Descripcion");
                                mostrar(nombre, imagenR, descrip);
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }
    public void mostrar(String nombre, String img, String descripcion) {

            //creamos una tarjeta
            CardView tarjeta = new CardView(this);

            // Configuramos los márgenes para la tarjeta
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 50);

            //añadimos los parametros a la tarjeta
            tarjeta.setLayoutParams(params);
            tarjeta.setMinimumHeight(200);

            tarjeta.setRadius(30);
            tarjeta.setCardBackgroundColor(res.getColor(R.color.verdeAgua));

            //layout para la tarjeta
            LinearLayout layoutTarjeta = new LinearLayout(this);
            layoutTarjeta.setOrientation(LinearLayout.HORIZONTAL);
            layoutTarjeta.setPadding(20, 40, 20, 40);
            //centramos el contenido de la tarjeta
            layoutTarjeta.setGravity(Gravity.CENTER_VERTICAL);

            //creamos un texto
            TextView titulo = new TextView(this);
            titulo.setText(nombre);
            titulo.setTextSize(22);
            titulo.setTextColor(res.getColor(R.color.verdeClaro));
            titulo.setPadding(50, 20, 20, 20);

            //creamos un texto para la descripcion
            TextView desc = new TextView(this);
            desc.setText(descripcion);
            desc.setTextSize(15);
            desc.setTextColor(res.getColor(R.color.verdeMedio));
            desc.setPadding(50, 20, 20, 20);
            String descripcionTexto = desc.getText().toString();
            if (descripcionTexto.length()>65){
               descripcionTexto =  descripcionTexto.substring(0, 65) + "...";
            }
            desc.setText(descripcionTexto);
            //layout para el texto
            LinearLayout layoutTexto = new LinearLayout(this);
            layoutTexto.setOrientation(LinearLayout.VERTICAL);
            //layoutTexto.setPadding(20, 30, 20, 30);

            //añadimos el contenido al layout
            layoutTexto.addView(titulo);
            layoutTexto.addView(desc);
            //creamos una imagen
            ImageView imagen = new ImageView(this);
            int altura = 200;
            int anchura = 200;
            if(img.isEmpty()){
                Glide.with(this).load(R.drawable.logo).apply(new RequestOptions().override(anchura, altura)).into(imagen);
            }else{
                //cargamos la img
                Glide.with(this).load(img).apply(new RequestOptions().override(anchura, altura)).into(imagen);

            }

            //agregamos el texto y la img al layout
            layoutTarjeta.addView(imagen);
            //añadimos el layout de texto al de la trajeta
            layoutTarjeta.addView(layoutTexto);

            // Agregamos el layout a la tarjeta
            tarjeta.addView(layoutTarjeta);

            //agregamos la tarjeta al layout
            layaoutEvento.addView(tarjeta);



    }
    public void restaurarBotones(String nombre){
        Button[] boton = new Button[6];

        boton[0] = findViewById(R.id.button_todo);
        boton[1] = findViewById(R.id.button_civico);
        boton[2] = findViewById(R.id.button_cultura);
        boton[3] = findViewById(R.id.button_deporte);
        boton[4] = findViewById(R.id.button_Educa);
        boton[5] = findViewById(R.id.button_social);

        for (int i = 0; i < boton.length; i++) {
            if (boton[i].getText().equals(nombre)){
                //cambiamos el color del boton
                boton[i].setBackground(res.getDrawable(R.drawable.botones_activos));
                boton[i].setTextColor(res.getColor(R.color.verdeClaro));
            }else{
                boton[i].setBackground(res.getDrawable(R.drawable.botones));
                boton[i].setTextColor(res.getColor(R.color.verdeOscuro));
            }
        }

    }
    public void filtro(@NonNull View v){
       Button boton = (Button) v;
       //cambiamos el color del boton
       // boton.setBackgroundColor(res.getColor(R.color.verdeOscuro));
       FirebaseFirestore db = FirebaseFirestore.getInstance();
       String categoria = boton.getText().toString();

       restaurarBotones(categoria);
       //hacemos una referencia a la coleccion de la que queremos los datos
       db.collection("Eventos").whereEqualTo("Categoria",categoria)
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           // Limpiamos el contenido actual
                           layaoutEvento.removeAllViews();

                           for (QueryDocumentSnapshot document : task.getResult()) {
                               String nombre = document.getString("Nombre");
                               String imagenR = document.getString("Imagen");
                               String descrip = document.getString("Descripcion");
                               mostrar(nombre, imagenR, descrip);
                           }

                       } else {
                           Log.w(TAG, "Error getting documents.", task.getException());
                       }
                   }
               });
   }
   public void irPerfil(View v){
       Intent i = new Intent(this, ActivityPerfil.class);
       startActivity(i);
   }

    public void irFavoritos(View v){
        Intent i = new Intent(this, ActivityFavoritos.class);
        startActivity(i);
    }
}