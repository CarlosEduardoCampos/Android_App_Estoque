package com.example.meuestoque;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.meuestoque.models.BancoDados;
import com.example.meuestoque.models.CxMsg;
import com.example.meuestoque.models.Produtos;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listaProdutos;
    private final CxMsg msg = new CxMsg(this);
    public static ArrayList<Produtos> lista_produtos = new ArrayList<>();
    private final BancoDados db = new BancoDados(this);
    private final Produtos produto = new Produtos(this, db);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicia o Banco de dados
        //produto.deletarTabela();
        produto.criarTabela();

        //busca produtos no banco
        lista_produtos = produto.buscarTodos();

        // Lista com os nomes dos produtos
        ArrayList<String> itens = new ArrayList<>();

        for(Produtos item : lista_produtos)
        {
            itens.add(item.getCodProduto()+" >>> "+item.getNomeProduto());
        }

        // Refeencia logiaca ao elemento listView
        listaProdutos = findViewById(R.id.lista_produtos);

        // Prepara lista de produtos para ser mostrada na tela
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itens
        );

        // Mostra a lista de produtos na tela
        listaProdutos.setAdapter(adapter);

        // Caso o usuario selecione algum item exibe uma mensagem OBS: substituir por tela editar
        listaProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                abrirTelaEditar();
            }
        });
    }

    public void abrirTelaCriar(View v)
    {
        Toast.makeText(this, "Abrir nova tela", Toast.LENGTH_SHORT).show();
        Intent it_TelaCriar = new Intent(this, TelaCriar.class);
        startActivity(it_TelaCriar);
        this.finish();
    }
    public void abrirTelaEditar(){
        Intent it_TelaEditar = new Intent(this, TelaEditar.class);
        startActivity(it_TelaEditar);
        this.finish();
    }
}