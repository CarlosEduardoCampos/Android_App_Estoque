package com.example.meuestoque;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.meuestoque.models.BancoDados;
import com.example.meuestoque.models.CxMsg;
import com.example.meuestoque.models.Produtos;

public class TelaCriar extends AppCompatActivity {

    private EditText et_nomeProduto, et_valorPoduto, et_totalProduto, et_minimProduto;
    private Button btn_salvar;
    private BancoDados db = new BancoDados(this);
    private Produtos produto = new Produtos(this, db);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_criar);

        // Relaciona elementos da tela as variaveis
        //EditText -> elementos de imput de texto
        et_nomeProduto = findViewById(R.id.et_saida_nomeProduto);
        et_valorPoduto = findViewById(R.id.et_saida_valorProduto);
        et_totalProduto = findViewById(R.id.et_edit_totalProduto);
        et_minimProduto = findViewById(R.id.et_edit_minimProduto);

        //Button
        btn_salvar = findViewById(R.id.btn_confirmar);

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });
    }

    // Chama uma fução que cadastra um produto no banco de dados
    public void salvar(){
        if(!campoVazio()){
            // setando valores ao produto
            produto.setNomeProduto(et_nomeProduto.getText().toString().trim());
            produto.setValorProduto(Double.parseDouble(et_valorPoduto.getText().toString().trim()));
            produto.setQuantidadeTotal(Integer.parseInt(et_totalProduto.getText().toString().trim()));
            produto.setQuantidadeMinima(Integer.parseInt(et_minimProduto.getText().toString().trim()));

            // cadastrar um novo produto
            produto.inserir();
            limparTela();
        }
        else {
            CxMsg.erroHumano(this, "Todos os campos devem ser preenchidos");
        }
    }

    private boolean campoVazio(){
        if(et_nomeProduto.getText().toString().equals("")){return true;}
        else if(et_valorPoduto.getText().toString().equals("")){return true;}
        else if(et_totalProduto.getText().toString().equals("")){return true;}
        else if(et_minimProduto.getText().toString().equals("")){return true;}
        else{return false;}
    }

    // Apaga as informações digitadas
    public void limparTela(){
        et_nomeProduto.setText("");
        et_valorPoduto.setText("");
        et_totalProduto.setText("");
        et_minimProduto.setText("");
    }

    // Volta para a tela anterior matando a tela atual
    public void voltaTela(View v){
        this.finish();
    }
}

