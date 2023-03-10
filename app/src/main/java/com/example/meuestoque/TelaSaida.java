package com.example.meuestoque;

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
    private final ArrayList<Produtos> lista_saida = new ArrayList<>();
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
    /////////////////////////////////////////////////////////////////////////
    //  Metodos de verificação e preenchimento dos campos digitaveis       //
    /////////////////////////////////////////////////////////////////////////
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
            CxMsg.erroHumano(this, "O campo NOME ou CODIGO deve ser preenchido!!!");
            return itens;
        }
        return itens;
    }

    public void mostraDados(Produtos produto){
        et_nome.setText(produto.getNomeProduto());
        et_valor.setText(String.valueOf(produto.getValorProduto()));
        et_codigo.setText(String.valueOf(produto.getCodProduto()));
    }

    public void limparTela(){
        et_nome.setText("");
        et_valor.setText("");
        et_codigo.setText("");
        et_saida.setText("1");
    }

    /////////////////////////////////////////////////////////////////////////
    //                 Metodos relacionado aos botoes(onClick)             //
    /////////////////////////////////////////////////////////////////////////

    public void confirmarProduto(View v){
        int saida = Integer.parseInt(et_saida.getText().toString().trim());
        String txt_saida = et_saida.getText().toString().trim();

        // Testa se o campo de saida tem um valor valido
        if(txt_saida.equals("")) {
            CxMsg.erroHumano(this, "Saida invalida.");
        }
        else if (saida < 0)
        {
            CxMsg.erroHumano(this, "Saida invalida.");
        }
        else{
            lista_busca = buscar();

            // Testa o tamanho da lista
            switch(lista_busca.size())
            {
                case 0:// Não forão encotrados produtos
                    CxMsg.erro(this, "O produto indicado não existe.");
                break;

                case 1:// produto e cadastrado na lista de saida
                    if(!testeLista(lista_busca.get(0), lista_saida)) {
                        int estoque_velho = lista_busca.get(0).getQuantidadeTotal();
                        int estoque_saida = Integer.parseInt(et_saida.getText().toString().trim());

                        if (estoque_velho < estoque_saida) {
                            CxMsg.erroHumano(
                                    this,
                                    "Quantidade em estoque é " + lista_busca.get(0).getQuantidadeTotal()
                            );
                            break;
                        } else { // modifica o estoque com base na saida passada
                            int estoque_novo = estoque_velho - estoque_saida;

                            lista_busca.get(0).setQuantidadeTotal(estoque_novo);
                            mostraDados(lista_busca.get(0));

                            lista_saida.add(lista_busca.get(0));
                            atualizarListaProdutos(lista_saida);
                            limparTela();
                        }
                    }
                    else{
                        Toast.makeText(this, "A saida desse produto ja foi cadastrada.",Toast.LENGTH_LONG).show();
                    }
                break;

                default:
                    atualizarListaBuscar(lista_busca);
                break;
            }
        }
    }

    // Remove a visibilidade da lista de busca e do botão fechar
    public void fecharListaBusca(View v){
        listaBusca.setVisibility(View.GONE);
        btn_fechar.setVisibility(View.GONE);
    }

    // Chama um metodo que busca um ou mais produtos na lista de produtos
    public void buscarProduto(View v){
        lista_busca = buscar();

        if(!lista_busca.isEmpty()) {
            if (lista_busca.size() == 1) {
                mostraDados(lista_busca.get(0));
            }
            else{
                atualizarListaBuscar(lista_busca);
            }
        }
        else{
            Toast.makeText(this, "Esse produto não existe", Toast.LENGTH_SHORT).show();
        }

    }

    // Chama um metodo de produto que relisa um update para cada produto na lista de saida
    public void finalizar(View v){
        if(lista_saida.isEmpty()){
            CxMsg.erroHumano(this, "Produtos devem ser confirmados antes. ");
        }
        else{
            for(Produtos p: lista_saida){
                p.editarTotal();
            }

            lista_saida.clear();
            atualizarListaProdutos(lista_saida);
        }
    }

    // Volta para a tela anterior matando a tela atual
    public void voltaTela(View v){this.finish();}

    //////////////////////////////////////////////////////////////////////////////////
    //        Metodos que atualizam na tela as lista(busca e lista de saida)       //
    /////////////////////////////////////////////////////////////////////////////////

    public boolean testeLista(Produtos p, ArrayList<Produtos> lista){
        for(Produtos item: lista){
            if(p.getCodProduto() == item.getCodProduto()) {
                return true;
            }
        }
        return false;
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
            // Prepara lista de produtos para ser mostrada na tela
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itens
            );

            // Mostra a lista de produtos na tela
            listaSaida.setAdapter(adapter);
            listaSaida.setVisibility(View.VISIBLE);
        }
        else{
            listaSaida.setVisibility(View.GONE);
        }
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
}