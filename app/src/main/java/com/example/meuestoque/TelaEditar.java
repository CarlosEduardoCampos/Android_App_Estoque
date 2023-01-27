package com.example.meuestoque;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TelaEditar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_editar);
    }

    public void editarProduto(View v){
        Toast.makeText(this, "Editar produto", Toast.LENGTH_SHORT).show();
    }

    public void deletarProduto(View v){
        Toast.makeText(this, "Editar produto", Toast.LENGTH_SHORT).show();
    }
}