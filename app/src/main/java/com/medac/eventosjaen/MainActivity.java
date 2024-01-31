package com.medac.eventosjaen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    //recogemos las variables de la intefaz
    private EditText usuario, contrasena, contrConfirm;
    private TextView subTitulo;
    private Button registro, incioSesion;
    ImageView imagen;
    //para guardar el color que tiene el texto
    ColorStateList color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa Firebase Auth (login)
        mAuth = FirebaseAuth.getInstance();
        //asociamos con la interfaz
        usuario = findViewById(R.id.usuario);
        //color del texto
        color = usuario.getTextColors();

        subTitulo = findViewById(R.id.subtitulo);
        contrasena = findViewById(R.id.contrasena);
        contrConfirm = findViewById(R.id.contrasenaConfirmar);
        registro = findViewById(R.id.crearCuenta);
        incioSesion = findViewById(R.id.iniciarSesion);
        imagen = findViewById(R.id.aceituna);

        //funcion del boton inicio sesion
        incioSesion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //guardamos en variable los datos recogidos del editText
                String email = usuario.getText().toString();
                String pass = contrasena.getText().toString();
                //si el subtitulo no es igual a inicio sesion, lo ponemos
                if (!subTitulo.getText().equals(getResources().getText(R.string.inicioSesion))){
                    correcto();
                    usuario.setText("");
                    contrasena.setText("");
                    //cambiamos el texto del subtitulo por iniciar sesion
                    subTitulo.setText(getResources().getText(R.string.inicioSesion));
                    Button inicioSesion = (Button)v;
                    inicioSesion.setText(getResources().getText(R.string.inicioSesion));
                    //ocultamos confirmar contraseña y cambiamos el texto
                    contrConfirm.setVisibility(View.GONE);
                    contrasena.setHint(getResources().getText(R.string.contrasenia));

                }else{
                    if(email.isEmpty() || pass.isEmpty()){
                        incorrecto();
                        Toast.makeText(MainActivity.this, "Debe introducir el correo y contraseña",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        correcto();
                        entrar(email, pass);
                    }
                }


            }

        });
    }

    @Override
    public void onStart() {
       super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
   }

   public void registro(View v){
       //guardamos en variable los datos recogidos del editText
       String user = usuario.getText().toString();
       String contra = contrasena.getText().toString();
       String contraConfirm = contrConfirm.getText().toString();
       if (!subTitulo.getText().equals(getResources().getText(R.string.registro))){
           usuario.setText("");
           contrasena.setText("");
           contrConfirm.setText("");
           //cambiamos el texto del subtitulo por registro
           subTitulo.setText(getResources().getText(R.string.registro));
           incioSesion.setText(getResources().getText(R.string.atras));
           correcto();
           //mostramos confirmar contraseña y cambiamos el texto
           contrConfirm.setVisibility(View.VISIBLE);
           contrasena.setHint(getResources().getText(R.string.contraseniaCrear));
       }else{
           //le pasamos a la función que enviará los datos a FireBase las variables
           if(user.isEmpty() || contra.isEmpty() || contraConfirm.isEmpty()){
               incorrecto();
               Toast.makeText(MainActivity.this, "Debe introducir el correo, la contraseña y su confirmación",
                       Toast.LENGTH_SHORT).show();
           }else{
               if (contra.equals(contraConfirm)){
                   correcto();
                   //le pasamos a la función que enviará los datos a FireBase las variables
                   crearCuenta(user, contra);
               }else{
                   incorrecto();
                   Toast.makeText(MainActivity.this, "Las contraseñas no coinciden",
                           Toast.LENGTH_SHORT).show();
               }

           }
       }


   }
    public void crearCuenta(String user, String pass){
        //de firebase
        mAuth.createUserWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            correcto();
                            Toast.makeText(MainActivity.this, "Autentificación completada", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            incorrecto();
                            Toast.makeText(MainActivity.this, "Fallo de autentificación", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    public void entrar(String user, String pass){
        mAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            correcto();
                            Toast.makeText(MainActivity.this, "Entrando...",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.getUid().equals("CNWjf1vUZcVPXQbdaE4nOWYqFYA2")){
                                irEntrada();
                            }else{
                                irInicio();
                            }

                        } else {
                            incorrecto();
                            Toast.makeText(MainActivity.this, "Identificación errónea, prueba otra vez",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    public void correcto(){
        usuario.setTextColor(color);
        contrasena.setTextColor(color);
        contrConfirm.setTextColor(color);
        imagen.setImageResource(R.drawable.acierto);
    }
    public void incorrecto(){
        contrasena.setTextColor(getResources().getColor(R.color.rojo));
        contrConfirm.setTextColor(getResources().getColor(R.color.rojo));
        usuario.setTextColor(getResources().getColor(R.color.rojo));
        imagen.setImageResource(R.drawable.error);
    }
    public void irInicio(){
        Intent i = new Intent(this, ActivityInicio.class);
        startActivity(i);
    }
    public void irEntrada(){
        Intent i = new Intent(this, ActivityEntrada.class);

        startActivity(i);
    }

}