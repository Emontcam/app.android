package com.medac.eventosjaen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ActivityEntrada extends Activity {
    FirebaseFirestore db;
    EditText nombreEvento;
    EditText descripcion;
    EditText img;
    RadioGroup categorias;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);
        db = FirebaseFirestore.getInstance();
    }

    public void anadirEvento(View v){
        //variables del nuevo evento
        String categoria="";
        String nombre="";
        String des="";
        String rutaImg="";
        //nombre
        nombreEvento = findViewById(R.id.nombre);
        nombre = nombreEvento.getText().toString();
        //ruta de la imagen
        img = findViewById(R.id.rutaDeImagen);
        rutaImg = img.getText().toString();
        //descripcion
        descripcion = findViewById(R.id.nombre);
        des = nombreEvento.getText().toString();
        //categoria
        categorias = findViewById(R.id.grupoCategorias);
            // Obtenemos el ID del RadioButton seleccionado
            int radioButtonId = categorias.getCheckedRadioButtonId();

            if (radioButtonId != -1) {
                // Obtenemos el RadioButton seleccionado usando el ID
                RadioButton radioButtonSeleccionado = findViewById(radioButtonId);

                categoria = radioButtonSeleccionado.getText().toString();

            } else {
                // si no hay ningún RadioButton seleccionado
                Toast.makeText(this, "Ningún RadioButton seleccionado", Toast.LENGTH_SHORT).show();
            }
          //comprobamos que hay datos en los campos obligatorios
        if (nombre.isEmpty() ){
            Toast.makeText(ActivityEntrada.this, "Debe ponerle nombre al evento", Toast.LENGTH_SHORT).show();
        }else{
            if (categoria.isEmpty()){
                Toast.makeText(ActivityEntrada.this, "Debe elegir una categoría", Toast.LENGTH_SHORT).show();
            }else {
                Map<String, Object> nuevoEvento = new HashMap<>();
                nuevoEvento.put("Nombre", nombre);
                nuevoEvento.put("Imagen", rutaImg);
                nuevoEvento.put("Descripcion", des);
                nuevoEvento.put("Categoria", categoria);

                //ponemos la variable nombre como final para poder mandarla con el toast de confirmacion
                final String nombreFinal = nombre;
                db.collection("Eventos")
                        .add(nuevoEvento)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(ActivityEntrada.this, "Se ha añadido el evento " + nombreFinal, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ActivityEntrada.this, "Ha habido un error al añadir el nuevo evento ", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

      //  db.collection("Eventos").document().set();
    }
}
