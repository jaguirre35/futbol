package com.example.futbol_personalsotf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {
    EditText jetcodigo,jetnombre,jetciudad;
    RadioButton jrbprofesional,jrbascenso,jrbaficionado;
    CheckBox jcbactivo;
    String codigo,nombre,cuidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ocultar titulo por defecto y asociar objetos java con xml
        getSupportActionBar().hide();
        jetcodigo.findViewById(R.id.etcodigo);
        jetnombre.findViewById(R.id.etnombre);
        jetciudad.findViewById(R.id.etciudad);
        jcbactivo.findViewById(R.id.cbactivo);
        jrbprofesional.findViewById(R.id.rbprofesional);
        jrbascenso.findViewById(R.id.rbascenso);
        jrbaficionado.findViewById(R.id.rbaficionado);

    }
    public void Adicionar(View view){
        codigo=jetcodigo.getText().toString();
        nombre=jetnombre.getText().toString();
        cuidad=jetciudad.getText().toString();
    }
}