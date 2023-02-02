package com.example.meuestoque;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.meuestoque.models.BancoDados;
import com.example.meuestoque.models.CxMsg;
import com.example.meuestoque.models.Produtos;

public class TelaEditar extends AppCompatActivity {
    EditText et_nome, et_valor, et_quantidadeTotoal, et_quantidademinima;
    private final BancoDados db = new BancoDados(this);
    private final Produtos produto = new Produtos(this, db);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_editar);

        // Recebe asinformações do produto enviado pela MainActivity
        Bundle extras = getIntent().getExtras();
        if(extras == null)
        {
            CxMsg.erro(this, "As informações do produto não foram encontradas");
        }
        else {

            produto.setCodProduto(extras.getInt("codi"));
            produto.setNomeProduto(extras.getString("nome"));
            produto.setValorProduto(extras.getDouble("valor"));
            produto.setQuantidadeTotal(extras.getInt("total"));
            produto.setQuantidadeMinima(extras.getInt("minimo"));
        }

        // Faz uma referencia logica entre os elementos na tela e as variaveis
        et_nome = findViewById(R.id.et_saida_nomeProduto);
        et_valor = findViewById(R.id.et_saida_valorProduto);
        et_quantidadeTotoal = findViewById(R.id.et_edit_totalProduto);
        et_quantidademinima = findViewById(R.id.et_edit_minimProduto);

        // Mostrta o produto recebido
        mostraDados();
    }

    /////////////////////////////////////////////////////////////////////////
    //  Metodos de verificação e preenchimento dos campos digitaveis       //
    /////////////////////////////////////////////////////////////////////////

    // Preenche os campos de digitação com os valores em produto
    public void mostraDados(){
        et_nome.setText(produto.getNomeProduto());
        et_valor.setText(String.valueOf(produto.getValorProduto()));
        et_quantidadeTotoal.setText(String.valueOf(produto.getQuantidadeTotal()));
        et_quantidademinima.setText(String.valueOf(produto.getQuantidadeMinima()));
    }

    // Preenche todos os campos com uma string vazia ""
    public void limparDados(){
        et_nome.setText("");
        et_valor.setText("");
        et_quantidadeTotoal.setText("");
        et_quantidademinima.setText("");
    }

    // Verifica se todos os campos foram preenchido se não retorna false
    private boolean campoVazio(){
        if(et_nome.getText().toString().equals("")){return true;}
        else if (et_valor.getText().toString().equals("")) {return true;}
        else if (et_quantidadeTotoal.getText().toString().equals("")) {return true;}
        else if (et_quantidademinima.getText().toString().equals("")) {return true;}
        else {return false;}
    }

    /////////////////////////////////////////////////////////////////////////
    //                 Metodos relacionado aos botoes(onClick)             //
    /////////////////////////////////////////////////////////////////////////

    // Chama um metodo de produto que realisa um update no banco de dados
    public void editarProduto(View v){

        if(!campoVazio()){
            produto.setNomeProduto(et_nome.getText().toString().trim());
            produto.setValorProduto(Double.parseDouble(et_valor.getText().toString().trim()));
            produto.setQuantidadeTotal(Integer.parseInt(et_quantidadeTotoal.getText().toString().trim()));
            produto.setQuantidadeMinima(Integer.parseInt(et_quantidademinima.getText().toString().trim()));

            produto.editar();
        }
        else{
            CxMsg.erroHumano(this, "Todos os campos devem ser preenchidos");
        }
    }

    // Chama um metodo de produto que executa um delete no banco
    public void deletarProduto(View v){
        produto.deletar();
        limparDados();
        this.finish();
    }

    // Volta para a tela anterior matando a tela atual
    public void voltaTela(View v){ this.finish(); }
}