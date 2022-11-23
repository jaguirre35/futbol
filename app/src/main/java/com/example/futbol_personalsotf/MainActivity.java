package com.example.futbol_personalsotf;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText jetcodigo,jetnombre,jetciudad;
    RadioButton jrbprofesional,jrbascenso,jrbaficionado;
    CheckBox jcbactivo;
    boolean respuesta;
    String codigo,nombre,ciudad,categoria,activo,ident_doc;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ocultar barra de titulo y asociar objetos Java con Xml
        getSupportActionBar().hide();
        jetciudad=findViewById(R.id.etciudad);
        jetnombre=findViewById(R.id.etnombre);
        jetcodigo=findViewById(R.id.etcodigo);
        jcbactivo=findViewById(R.id.cbactivo);
        jrbaficionado=findViewById(R.id.rbaficionado);
        jrbascenso=findViewById(R.id.rbascenso);
        jrbprofesional=findViewById(R.id.rbprofesional);
    }

    public void Adicionar(View view){
        codigo=jetcodigo.getText().toString();
        nombre=jetnombre.getText().toString();
        ciudad=jetciudad.getText().toString();
        if (codigo.isEmpty() || nombre.isEmpty() || ciudad.isEmpty()){
            Toast.makeText(this, "Los campos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
            if (jrbprofesional.isChecked())
                categoria="Profesional";
            else
            if (jrbascenso.isChecked())
                categoria="Ascenso";
            else
                categoria="Aficionado";
            // Create a new user with a first and last name
            Map<String, Object> equipo = new HashMap<>();
            equipo.put("Codigo", codigo);
            equipo.put("Nombre", nombre);
            equipo.put("Ciudad", ciudad);
            equipo.put("Categoria",categoria);
            equipo.put("Activo","si");

            // Add a new document with a generated ID
            db.collection("Campeonato")
                    .add(equipo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(MainActivity.this, "Documento adicionado", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log.w(TAG, "Error adding document", e);
                            Toast.makeText(MainActivity.this, "Error adicionando documento", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void Consultar(View view){
        Buscar_equipo();
    }

    private void Buscar_equipo(){
        respuesta=false;
        codigo=jetcodigo.getText().toString();
        if (codigo.isEmpty()){
            Toast.makeText(this, "Codigo es requerido", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }else{
            db.collection("Campeonato")
                    .whereEqualTo("Codigo",codigo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    respuesta=true;
                                    ident_doc=document.getId();
                                    jetnombre.setText(document.getString("Nombre"));
                                    jetciudad.setText(document.getString("Ciudad"));
                                    if (document.getString("Categoria").equals("Profesional"))
                                        jrbprofesional.setChecked(true);
                                    else
                                    if (document.getString("Categoria").equals("Ascenso"))
                                        jrbascenso.setChecked(true);
                                    else
                                        jrbaficionado.setChecked(true);
                                    if (document.getString("Activo").equals("si"))
                                        jcbactivo.setChecked(true);
                                    else
                                        jcbactivo.setChecked(false);
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            } else {
                                // Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }

    public void Modificar(View view){
        if (respuesta == true){
            if (jrbprofesional.isChecked())
                categoria="Profesional";
            else
            if (jrbascenso.isChecked())
                categoria="Ascenso";
            else
                categoria="Aficionado";
            // Create a new user with a first and last name
            Map<String, Object> equipo = new HashMap<>();
            equipo.put("Codigo", codigo);
            equipo.put("Nombre", nombre);
            equipo.put("Ciudad", ciudad);
            equipo.put("Categoria",categoria);
            equipo.put("Activo","si");

            // Modify a new document with a generated ID
            db.collection("Campeonato").document(ident_doc)
                    .set(equipo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this,"Documento actualizado ...",Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"Error actualizando documento...",Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this, "Debe primero consultar", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }

    }

    private void Limpiar_campos(){
        jetcodigo.setText("");
        jetnombre.setText("");
        jetciudad.setText("");
        jrbprofesional.setChecked(true);
        jcbactivo.setChecked(false);
        respuesta=false;
        jetcodigo.requestFocus();
    }
}


