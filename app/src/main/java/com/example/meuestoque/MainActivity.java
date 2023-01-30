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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meuestoque.models.BancoDados;
import com.example.meuestoque.models.CxMsg;
import com.example.meuestoque.models.Produtos;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText et_pesquisar;
    private TextView titulo_estoque_baixo;
    private Button btn_pesquisar;
    private ListView listaProdutos, listaEstoqueBaixo;
    private ArrayList<Produtos> lista_produtos = new ArrayList<>();
    private ArrayList<Produtos> lista_estoque_baixo = new ArrayList<>();
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

        // Refeencia logiaca ao elemento listView
        listaProdutos = findViewById(R.id.lista_produtos);
        listaEstoqueBaixo = findViewById(R.id.lista_estoque_baixo);
        titulo_estoque_baixo = findViewById(R.id.tituloEstoqueBaixo);

        et_pesquisar = findViewById(R.id.et_pesquisa);
        btn_pesquisar = findViewById(R.id.btn_pesquisar);

        atualizarListaProdutos(lista_produtos);

        // Caso o usuario selecione algum item exibe uma mensagem OBS: substituir por tela editar
        listaProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!lista_produtos.isEmpty()){ abrirTelaEditar(position, lista_produtos); }
            }
        });

        listaEstoqueBaixo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!lista_estoque_baixo.isEmpty()){ abrirTelaEditar(position, lista_estoque_baixo); }
            }
        });

        btn_pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarPodutos();
            }
        });
    }

    public void buscarPodutos(){
        ArrayList<Produtos> itens = new ArrayList<>();
        String pesquisa = et_pesquisar.getText().toString().trim();

        if(!pesquisa.equals(""))
        {
            for(Produtos item: lista_produtos){
                if(item.getNomeProduto().contains(pesquisa)){
                    itens.add(item);
                }
                if(String.valueOf(item.getCodProduto()).equals(pesquisa)) {
                    itens.add(item);
                }
            }
        }
        else{
            CxMsg.erroHumano(this, "O campo deve estar preenchido");
            atualizarListaProdutos(lista_produtos);
        }
        if(!itens.isEmpty()){
            atualizarListaProdutos(itens);
        }
        else{
            Toast.makeText(this, "Esses produtos não forão emcontrados", Toast.LENGTH_SHORT).show();
        }
    }

    public void criaEstoqueBaixo(ArrayList<Produtos> lista_produtos){

        for(Produtos item: lista_produtos){
            boolean teste = item.getQuantidadeTotal() < item.getQuantidadeMinima();

            if(teste) {
                lista_estoque_baixo.add(item);
            }
        }

        if(!lista_estoque_baixo.isEmpty()){
            atualizarListaEstoqueBaixo(lista_estoque_baixo);
            titulo_estoque_baixo.setVisibility(View.VISIBLE);
            listaEstoqueBaixo.setVisibility(View.VISIBLE);
        }
    }

    public void abrirTelaCriar(View v)
    {
        Toast.makeText(this, "Abrir nova tela", Toast.LENGTH_SHORT).show();
        Intent it_TelaCriar = new Intent(this, TelaCriar.class);
        startActivity(it_TelaCriar);
        this.finish();
    }

    public void abrirTelaSaida(View v)
    {
        Toast.makeText(this, "Abrir nova tela", Toast.LENGTH_SHORT).show();
        Intent it_TelaSaida = new Intent(this, TelaSaida.class);
        startActivity(it_TelaSaida);
        this.finish();
    }
    public void abrirTelaEditar(Integer position, ArrayList<Produtos> lista_produtos){
        Intent it_TelaEditar = new Intent(this, TelaEditar.class);

        it_TelaEditar.putExtra("codi", lista_produtos.get(position).getCodProduto());
        it_TelaEditar.putExtra("nome", lista_produtos.get(position).getNomeProduto());
        it_TelaEditar.putExtra("valor", lista_produtos.get(position).getValorProduto());
        it_TelaEditar.putExtra("total", lista_produtos.get(position).getQuantidadeTotal());
        it_TelaEditar.putExtra("minimo", lista_produtos.get(position).getQuantidadeMinima());

        startActivity(it_TelaEditar);
        this.finish();
    }

    public void atualizarListaProdutos(ArrayList<Produtos> lista_produtos){
        // Lista com os nomes dos produtos
        ArrayList<String> itens = new ArrayList<>();

        if(lista_produtos.isEmpty()){
            itens.add(" !!!!! Lista Vazia  !!!!!");
        }
        else{
            criaEstoqueBaixo(lista_produtos);
            for(Produtos item : lista_produtos)
            {
                itens.add(
                        "Codigo >>> "+item.getCodProduto()+"\n"+
                        "Nome   >>> "+item.getNomeProduto()+"\n"+
                        "Total  >>> "+item.getQuantidadeTotal()+"\n"
                );
            }
        }

        // Prepara lista de produtos para ser mostrada na tela
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itens
        );

        // Mostra a lista de produtos na tela
        listaProdutos.setAdapter(adapter);
    }

    public void atualizarListaEstoqueBaixo(ArrayList<Produtos> lista_produtos){
        // Lista com os nomes dos produtos
        ArrayList<String> itens = new ArrayList<>();

        if(lista_produtos.isEmpty()){
            itens.add(" !!!!! Lista Vazia  !!!!!");
        }
        else{
            for(Produtos item : lista_produtos)
            {
                itens.add(
                        "Codigo >>> "+item.getCodProduto()+"\n"+
                                "Nome   >>> "+item.getNomeProduto()+"\n"+
                                "Total  >>> "+item.getQuantidadeTotal()+"\n"
                );
            }
        }

        // Prepara lista de produtos para ser mostrada na tela
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                itens
        );

        // Mostra a lista de produtos na tela
        listaEstoqueBaixo.setAdapter(adapter);
    }
}