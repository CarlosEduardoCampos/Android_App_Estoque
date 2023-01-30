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

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            CxMsg.erro(this, "Não foi passado um id do produto");
        } else {
            produto.setCodProduto(extras.getInt("codi"));
            produto.setNomeProduto(extras.getString("nome"));
            produto.setValorProduto(extras.getDouble("valor"));
            produto.setQuantidadeTotal(extras.getInt("total"));
            produto.setQuantidadeMinima(extras.getInt("minimo"));
        }

        et_nome = findViewById(R.id.et_saida_nomeProduto);
        et_valor = findViewById(R.id.et_saida_valorProduto);
        et_quantidadeTotoal = findViewById(R.id.et_edit_totalProduto);
        et_quantidademinima = findViewById(R.id.et_edit_minimProduto);

        mostraDados();
    }

    public void mostraDados(){
        et_nome.setText(produto.getNomeProduto());
        et_valor.setText(String.valueOf(produto.getValorProduto()));
        et_quantidadeTotoal.setText(String.valueOf(produto.getQuantidadeTotal()));
        et_quantidademinima.setText(String.valueOf(produto.getQuantidadeMinima()));
    }

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

    public void deletarProduto(View v){
        produto.deletar();
        sairTela();
    }

    private boolean campoVazio(){
        if(et_nome.getText().toString().equals("")){return true;}
        else if (et_valor.getText().toString().equals("")) {return true;}
        else if (et_quantidadeTotoal.getText().toString().equals("")) {return true;}
        else if (et_quantidademinima.getText().toString().equals("")) {return true;}
        else {return false;}
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