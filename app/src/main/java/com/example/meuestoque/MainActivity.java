package com.example.meuestoque;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.os.Environment;

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

        // Refeencia logica de elemetos da tela as variaveis (vinculo de variavel com o componente)
        listaProdutos = findViewById(R.id.lista_produtos);
        listaEstoqueBaixo = findViewById(R.id.lista_estoque_baixo);
        titulo_estoque_baixo = findViewById(R.id.tituloEstoqueBaixo);
        et_pesquisar = findViewById(R.id.et_pesquisa);
        btn_pesquisar = findViewById(R.id.btn_pesquisar);

        // Evento de click relacionado aos itens da lista de produtos
        //Quando voce clica em cima do produto na lista de produtos normal
        listaProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(!lista_produtos.isEmpty()){ abrirTelaEditar(position, lista_produtos); }
            }
        });

        // Evento de click relacionado aos itens da lista de produtos em estoque baixo
        //Quando voce clica em cima do produto na lista de produtos com baixo estoque
        listaEstoqueBaixo.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(!lista_estoque_baixo.isEmpty()){abrirTelaEditar(position, lista_estoque_baixo);}
            }
        });

        // Evento de click relacionado ao botão pesquisar
        btn_pesquisar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                buscarPodutos();
            }
        });
    }

    // Realiza uma pesquisa na lista de produtos, procurando a informação digitada
    public void buscarPodutos(){
        ArrayList<Produtos> itens = new ArrayList<>();
        String pesquisa = et_pesquisar.getText().toString().trim();

        // Ferifica se o campo de pesquisa foi preenchido
        if(!pesquisa.equals(""))
        {
            // Relaciona as informações passadas a um ou mais produtos da lista
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
            CxMsg.erroHumano(this, "O campo de pesquisa esta vazio");
            atualizarListaProdutos(lista_produtos);
        }

        // Testa se existem produtos cadastrado na lista itens
        if(!itens.isEmpty()){
            //atualiza a lista de produtos mostrada na tela
            atualizarListaProdutos(itens);
        }
        else{
            Toast.makeText(
                this,
                "Não foi encontrado nem um produto relacionado",
                Toast.LENGTH_LONG
            ).show();
        }
    }

    // Abastece a lista relacionada aos intens que a quantidade em estoque esteja abaixo que
    // a quantidade minima passada
    public void criaEstoqueBaixo(ArrayList<Produtos> lista_produtos){
        // limpa os dados antigos da lista de produtos em estoque baixa
        lista_estoque_baixo.clear();

        // Verifica se um produto da lista esta com uma quantidade abaixo da minima
        for(Produtos item: lista_produtos){
            boolean teste = item.getQuantidadeTotal() < item.getQuantidadeMinima();
            if(teste) {
                // cadastra no estoque baixo um produto
                lista_estoque_baixo.add(item);
            }
        }

        // Testa se existem produtos com estoque baixo e mostra a lista
        if(!lista_estoque_baixo.isEmpty()){
            atualizarListaEstoqueBaixo(lista_estoque_baixo);
            titulo_estoque_baixo.setVisibility(View.VISIBLE);
            listaEstoqueBaixo.setVisibility(View.VISIBLE);
        }
        else{
            listaEstoqueBaixo.setVisibility(View.GONE);
            titulo_estoque_baixo.setVisibility(View.GONE);
        }
    }

    // Cria uma intent e passa as informaçõe de um produto localizado na lista
    public void abrirTelaEditar(Integer position, ArrayList<Produtos> lista_produtos){
        Intent it_TelaEditar = new Intent(this, TelaEditar.class);

        it_TelaEditar.putExtra("codi", lista_produtos.get(position).getCodProduto());
        it_TelaEditar.putExtra("nome", lista_produtos.get(position).getNomeProduto());
        it_TelaEditar.putExtra("valor", lista_produtos.get(position).getValorProduto());
        it_TelaEditar.putExtra("total", lista_produtos.get(position).getQuantidadeTotal());
        it_TelaEditar.putExtra("minimo", lista_produtos.get(position).getQuantidadeMinima());

        startActivity(it_TelaEditar);
    }

    /////////////////////////////////////////////////////////////////////////
    //                 Metodos relacionado aos botoes(onClick)             //
    /////////////////////////////////////////////////////////////////////////

    public void abrirTelaCriar(View v)
    {
        //Toast.makeText(this, "Abrir nova tela", Toast.LENGTH_SHORT).show();
        Intent it_TelaCriar = new Intent(this, TelaCriar.class);
        startActivity(it_TelaCriar);
    }

    public void abrirTelaSaida(View v)
    {
        //Toast.makeText(this, "Abrir nova tela", Toast.LENGTH_SHORT).show();
        Intent it_TelaSaida = new Intent(this, TelaSaida.class);
        startActivity(it_TelaSaida);
    }
    public void criarPDF(View v){

        if(lista_produtos.isEmpty()){
            CxMsg.erroHumano(this, "A lista esta vazia");
        }
        else {
            // Cria um documento para gerar o pdf
            PdfDocument document = new PdfDocument();

            // Especifica detalhes da página
            PdfDocument.PageInfo detalhes = new PdfDocument.PageInfo.Builder(595, 842, 1).create();

            // Criar a primeira página
            PdfDocument.Page novaPagina = document.startPage(detalhes);

            Canvas canvas = novaPagina.getCanvas();

            Paint corTxt = new Paint();
            corTxt.setColor(Color.BLACK);

            int altura = 100;
            for (Produtos p : lista_produtos) {
                canvas.drawText(
                        "Codigo  >>> " + p.getCodProduto() + "\t" +
                                "Produto >>> " + p.getNomeProduto() + "\t" +
                                "Valor   >>> " + p.getValorProduto() + "\t" +
                                "Total   >>" + p.getQuantidadeTotal() + "\n"
                        , 50, altura, corTxt);

                altura += 100;
            }

            document.finishPage(novaPagina);
            // busca qual a data e a hr atual
            Calendar calendario = Calendar.getInstance();
            SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyy HH:mm:ss", Locale.getDefault());
            String dataFomatada = formatoData.format(calendario.getTime())
                .replaceAll("-", "")
                .replaceAll(" ", "")
                .replaceAll(":","");

            // Criar o pdf na memoria
            String pastaPDF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
            String caminhoPDF = pastaPDF + "/Estoque" + dataFomatada + ".pdf";

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    document.writeTo(Files.newOutputStream(Paths.get(caminhoPDF)));
                }
                Toast.makeText(this, "PDF Criado com Sucesso!!!", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "PDF Salvo em Documents.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                CxMsg.erroExecucao(this, "Erro", e);
            }

            // fecha o arquivo
            document.close();
        }

    }

    //////////////////////////////////////////////////////////////////////////////////
    //  Metodos que atualizam na tela as lista(estoque baixo e lista de produtos)  //
    /////////////////////////////////////////////////////////////////////////////////
    public void atualizarListaProdutos(ArrayList<Produtos> lista_produtos){
        // Lista com os dados dos produtos
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

    public void atualizarListaEstoqueBaixo(ArrayList<Produtos> lista){
        // Lista com os dados dos produtos
        ArrayList<String> itens = new ArrayList<>();

        // Caso a lista de estoque baixo não seja vazia, este metodo atualiza na tela
        if(!lista.isEmpty()){
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
            listaEstoqueBaixo.setAdapter(adapter);
        }
    }

    // Atualiza as lista sempre que a tela estiver no estado Star
    public void onStart(){
        super.onStart();

        //busca todos os produtos no banco
        lista_produtos = produto.buscarTodos();

        // Atualiza as listas da tela
        atualizarListaProdutos(lista_produtos);
    }
}