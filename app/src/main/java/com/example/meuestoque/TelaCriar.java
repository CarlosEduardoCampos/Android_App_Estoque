package com.example.meuestoque;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.meuestoque.models.BancoDados;
import com.example.meuestoque.models.Produtos;

public class TelaCriar extends AppCompatActivity {

    EditText et_nomeProduto, et_valorPoduto, et_totalProduto, et_minimProduto;
    Button btn_salvar;
    BancoDados db = new BancoDados(this);
    Produtos produtos = new Produtos(this, db);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_criar);

        // Relaciona elementos da tela as variaveis
        //EditText -> elementos de imput de texto
        et_nomeProduto = findViewById(R.id.et_nomeProduto);
        et_valorPoduto = findViewById(R.id.et_valorProduto);
        et_totalProduto = findViewById(R.id.et_totalProduto);
        et_minimProduto = findViewById(R.id.et_minimProduto);

        //Button
        btn_salvar = findViewById(R.id.btn_salvar);

        // Chama uma fução que cadastra um produto no banco de dados
        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setando valores ao produto
                produtos.setNomeProduto(et_nomeProduto.getText().toString());
                produtos.setValorProduto(Double.parseDouble(et_valorPoduto.getText().toString().trim()));
                produtos.setQuantidadeTotal(Integer.parseInt(et_totalProduto.getText().toString().trim()));
                produtos.setQuantidadeMinima(Integer.parseInt(et_minimProduto.getText().toString().trim()));

                // cadatra um novo produto
                produtos.inserir();
                limparTela();
                finalizarTela();
            }
        });
    }

    // Apaga as informações digitadas
    public void limparTela(){
        et_nomeProduto.setText("");
        et_valorPoduto.setText("");
        et_totalProduto.setText("");
        et_minimProduto.setText("");
    }

    // Volta para a tela anterior matando a tela atual
    public void finalizarTela(){
        Intent it_main = new Intent(this, MainActivity.class);
        startActivity(it_main);
        this.finish();
    }
}
