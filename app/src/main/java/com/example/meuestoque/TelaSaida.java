package com.example.meuestoque;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.meuestoque.models.BancoDados;
import com.example.meuestoque.models.CxMsg;
import com.example.meuestoque.models.Produtos;

import java.util.ArrayList;


public class TelaSaida extends AppCompatActivity {
    private ListView listaSaida, listaBusca;
    private EditText et_nome, et_valor, et_codigo, et_saida;
    private Button btn_fechar;
    private ArrayList<Produtos> lista_produtos = new ArrayList<>();
    private ArrayList<Produtos> lista_saida = new ArrayList<>();
    private ArrayList<Produtos> lista_busca = new ArrayList<>();

    private final BancoDados db = new BancoDados(this);
    private final Produtos produto = new Produtos(this, db);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_saida);

        // Listas
        listaSaida = findViewById(R.id.lista_saida);
        listaBusca = findViewById(R.id.lista_pesquisar);

        // Entrada de Textos
        et_nome = findViewById(R.id.et_saida_nomeProduto);
        et_valor = findViewById(R.id.et_saida_valorProduto);
        et_codigo = findViewById(R.id.et_saida_codigo);
        et_saida = findViewById(R.id.et_saida_saida);

        // Botões
        btn_fechar = findViewById(R.id.btn_fechar);

        //Busca todos os produtos
        lista_produtos = produto.buscarTodos();

        //Inicia saida em 1
        et_saida.setText("1");

        // Caso o usuario selecione algum item exibe os dados na tela
        listaSaida.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!lista_saida.isEmpty()){
                    produto.setCodProduto(lista_saida.get(position).getCodProduto());
                    produto.setNomeProduto(lista_saida.get(position).getNomeProduto());
                    produto.setValorProduto(lista_saida.get(position).getValorProduto());
                    produto.setQuantidadeMinima(lista_saida.get(position).getQuantidadeMinima());
                    produto.setQuantidadeTotal(lista_saida.get(position).getQuantidadeTotal());
                    mostraDados(produto);
                    lista_saida.remove(position);
                    atualizarListaProdutos(lista_saida);
                }
            }
        });

        // Caso o usuario selecione algum item exibe os dados na tela
        listaBusca.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                produto.setCodProduto(lista_busca.get(position).getCodProduto());
                produto.setNomeProduto(lista_busca.get(position).getNomeProduto());
                produto.setValorProduto(lista_busca.get(position).getValorProduto());
                produto.setQuantidadeMinima(lista_busca.get(position).getQuantidadeMinima());
                produto.setQuantidadeTotal(lista_busca.get(position).getQuantidadeTotal());
                mostraDados(produto);
                lista_busca.remove(position);
                listaBusca.setVisibility(View.GONE);
                btn_fechar.setVisibility(View.GONE);
            }
        });
    }

    public ArrayList<Produtos> buscar(){

        ArrayList<Produtos> itens = new ArrayList<>();
        String nome = et_nome.getText().toString().trim();
        String codigo = et_codigo.getText().toString().trim();

        if(!codigo.equals(""))
        {
            for(Produtos item: lista_produtos){
                if(String.valueOf(item.getCodProduto()).equals(codigo)) {
                    itens.add(item);
                }
            }
        }
        else if(!nome.equals(""))
        {
            for(Produtos item: lista_produtos){
                if(item.getNomeProduto().contains(nome))
                {
                    itens.add(item);
                }
            }
        }
        else{
            CxMsg.erroHumano(this, "O campo nome ou codigo deve ser preenchido");
            return itens;
        }
        return itens;
    }

    public void atualizarListaProdutos(ArrayList<Produtos> lista_produtos){
        // Lista com os nomes dos produtos
        ArrayList<String> itens = new ArrayList<>();

        if(!lista_produtos.isEmpty()){
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
        listaSaida.setAdapter(adapter);
    }

    public void atualizarListaBuscar(ArrayList<Produtos> lista){
        // Lista com os nomes dos produtos
        ArrayList<String> itens = new ArrayList<>();

        for(Produtos item : lista)
        {
            itens.add(
                    "Codigo >>> "+item.getCodProduto()+"\n"+
                    "Nome   >>> "+item.getNomeProduto()+"\n"+
                    "Total  >>> "+item.getQuantidadeTotal()+"\n"
            );
        }

        // Prepara lista de produtos para ser mostrada na tela
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                itens
        );

        // Mostra a lista de produtos na tela
        listaBusca.setAdapter(adapter);
        listaBusca.setVisibility(View.VISIBLE);
        btn_fechar.setVisibility(View.VISIBLE);
    }

    public void mostraDados(Produtos produto){
        et_nome.setText(produto.getNomeProduto());
        et_valor.setText(String.valueOf(produto.getValorProduto()));
        et_codigo.setText(String.valueOf(produto.getCodProduto()));
    }

    public void fecharListaBusca(View v){
        listaBusca.setVisibility(View.GONE);
        btn_fechar.setVisibility(View.GONE);
    }

    public void confirmarProduto(View v){
        int saida = Integer.parseInt(et_saida.getText().toString().trim());
        String txt_saida = et_saida.getText().toString().trim();

        if(txt_saida.equals("")) {
            CxMsg.erroHumano(this, "Saida invalida");
        }
        else if (saida > 0)
        {
            lista_busca = buscar();

            if (lista_busca.isEmpty()) {
                CxMsg.erro(this, "O produto indicado não existe");
                return;
            }
            else if (lista_busca.size() == 1) {
                if(!existeProduto(lista_busca.get(0))) {
                    int estoque_velho = lista_busca.get(0).getQuantidadeTotal();
                    int estoque_saida = Integer.parseInt(et_saida.getText().toString().trim());

                    if (estoque_velho >= estoque_saida) {
                        int estoque_novo = estoque_velho - estoque_saida;

                        lista_busca.get(0).setQuantidadeTotal(estoque_novo);
                        mostraDados(lista_busca.get(0));
                        lista_saida.add(lista_busca.get(0));
                        atualizarListaProdutos(lista_saida);
                    } else {
                        CxMsg.erroHumano(this, "A saida de " + lista_busca.get(0).getNomeProduto() + " tresta maior que a quantidade em estoque");
                        return;
                    }
                }
                else{
                    Toast.makeText(this, "Protudo ja foi confirmado", Toast.LENGTH_SHORT).show();
                }
            } else {
                atualizarListaBuscar(lista_busca);
            }
        }
        else{
            CxMsg.erroHumano(this, "Saida invalida");
        }
        limparTela();
    }

    public void buscarProduto(View v){
        lista_busca = buscar();

        if(!lista_busca.isEmpty()) {
            if (lista_busca.size() == 1) {
                mostraDados(lista_busca.get(0));
                lista_saida.add(lista_busca.get(0));
            }
            else{
                atualizarListaBuscar(lista_busca);
            }
        }

    }

    public boolean existeProduto(Produtos p){

        for(Produtos item_saida: lista_saida) {
            if (p.getCodProduto() == item_saida.getCodProduto()) {
                return true;
            }
        }

        return false;
    }

    public void finalizar(View v){
        if(lista_saida.isEmpty()){
            CxMsg.erroHumano(this, "Produtos devem ser comfirmados antes ");
        }
        else{
            for(Produtos p: lista_saida){
                p.editar();
                lista_saida.remove(p);
            }

            lista_saida.clear();
            atualizarListaProdutos(lista_saida);
        }
    }

    public void limparTela(){
        et_nome.setText("");
        et_valor.setText("");
        et_codigo.setText("");
        et_saida.setText("1");
    }

    public void voltaTela(View v){
        sairTela();
    }

    // Volta para a tela anterior matando a tela atual
    public void sairTela(){
        Intent it_main = new Intent(this, MainActivity.class);
        startActivity(it_main);
        this.finish();
    }
}