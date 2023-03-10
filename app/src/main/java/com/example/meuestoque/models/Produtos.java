package com.example.meuestoque.models;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;


public class Produtos {

    // Objetos externos
    private final Activity activity;// Tela que a função e chamada
    private final BancoDados bancoDados;// Banco de dados

    // Titulo da tabela e nomes das colunas
    private final String TITULO_TABELA = "produtos";
    private final String COLUNA_ID = "codProduto";
    private final String COLUNA_NOME = "nomeProduto";
    private final String COLUNA_VALOR = "valorProduto";
    private final String COLUNA_QUANTIDADE_TOTAL = "quantidadeTotal";
    private final String COLUNA_QUANTIDADE_MINIMA = "quantidadeMinima";

    // Variaveis
    protected Integer codProduto; // Indetificador(unico)
    protected String nomeProduto = "";
    protected Double valorProduto = 0.0;
    protected Integer quantidadeTotal = 0;
    protected Integer quantidadeMinima = 0;

    // Construtor da Classe produtos
    public Produtos(Activity act, BancoDados db){
        this.activity = act;
        this.bancoDados = db;
    }

    // GETs e SETs
    public Integer getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(Integer codProduto) {
        this.codProduto = codProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Double getValorProduto() {
        return valorProduto;
    }

    public void setValorProduto(Double valorProduto) {
        this.valorProduto = valorProduto;
    }

    public Integer getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(Integer quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public Integer getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(Integer quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    // Apaga a tabela de produtoss completamente
    public void deletarTabela(){
        bancoDados.deletarTabela(TITULO_TABELA);
    }

    // Cria a tabela de Podutos
    public void criarTabela(){
        try {
            bancoDados.abrirTabela(
                TITULO_TABELA + "(" +
                        COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUNA_NOME + " TEXT, " +
                        COLUNA_VALOR + " DOUBLE, " +
                        COLUNA_QUANTIDADE_TOTAL + " INTEGER, " +
                        COLUNA_QUANTIDADE_MINIMA + " INTEGER " +
                ");"
            );
        }
        catch (Exception ex){
            CxMsg.erroExecucao(activity, "Erro ao tentar criar tabela.", ex);
        }
    }

    // Cadastra um nono produto na agenda
    public void inserir()
    {
        // Verifica se todos os campos estão preenchidos
        if (nomeProduto.equals("")) {
            CxMsg.erroHumano(activity, "Não e possivel realizar cadastro com campos vazios!");
            return;
        }

        // Cadastra um produto no banco de dados
        try {
            bancoDados.inserir(
                    TITULO_TABELA + "(" +
                            COLUNA_NOME + ", " +
                            COLUNA_VALOR + ", " +
                            COLUNA_QUANTIDADE_TOTAL + ", " +
                            COLUNA_QUANTIDADE_MINIMA +
                            ") VALUES('" +
                            nomeProduto + "', " +
                            valorProduto + ", " +
                            quantidadeTotal + ", " +
                            quantidadeMinima +
                            ");"
            );
        }
        catch (Exception ex) {
            // Mesagem de Erro
            CxMsg.erroExecucao(activity, "Erro ao tentar cadastrar um novo produto.", ex);
        }
        finally {
            // Mesagem de sucesso
            Toast.makeText(activity, "Produto gravado com sucesso!!!", Toast.LENGTH_SHORT).show();
        }
    }

    // Apaga a um contado no banco e dados
    public void deletar()
    {
        try{
            bancoDados.deletar(
                    TITULO_TABELA,
                    COLUNA_ID,
                    codProduto
            );
        }
        catch (Exception ex){
            // Mensagem de Erro
            CxMsg.erroExecucao(activity,"Erro ao tentar deletar um produto." , ex);
        }
        finally {
            // Mesagem de sucesso
            Toast.makeText(activity,"Produto deletado com sucesso.",Toast.LENGTH_SHORT).show();
        }
    }

    // Edita o produto no banco de dados
    public void editar()
    {
        try{
            ContentValues valores = new ContentValues();

            valores.put(COLUNA_QUANTIDADE_MINIMA, quantidadeMinima);
            valores.put(COLUNA_QUANTIDADE_TOTAL, quantidadeTotal);
            valores.put(COLUNA_VALOR, valorProduto);
            valores.put(COLUNA_NOME, nomeProduto);

            bancoDados.editar(
                    codProduto,
                    TITULO_TABELA,
                    COLUNA_ID,
                    valores
            );
        }
        catch (Exception ex){
            // Mensagem de Erro
            CxMsg.erroExecucao(activity,"Erro ao tentar editar um produto." , ex);
        }
        finally {
            // Mensagem de sucesso
            Toast.makeText(activity,"Produto editado com sucesso.",Toast.LENGTH_SHORT).show();
        }
    }

    // Edita o campo de quantidade total do produto no banco de dados
    public void editarTotal()
    {
        try{
            ContentValues valores = new ContentValues();


            valores.put(COLUNA_QUANTIDADE_TOTAL, quantidadeTotal);

            bancoDados.editar(
                    codProduto,
                    TITULO_TABELA,
                    COLUNA_ID,
                    valores
            );
        }
        catch (Exception ex){
            // Mensagem de Erro
            CxMsg.erroExecucao(activity,"Erro ao tentar editar um produto." , ex);
        }
        finally {
            // Mensagem de sucesso
            Toast.makeText(activity,"Saida de produto com sucesso!!!",Toast.LENGTH_SHORT).show();
        }
    }

    // Realiza uma busca de todos os produtos no banco de dados
    public ArrayList<Produtos> buscarTodos(){
        bancoDados.abrirDB();
            ArrayList<Produtos> lista_produtos = new ArrayList<>();

            // Busca tosdos os produtos
            Cursor ponteiro = bancoDados.buscarDados(
                    TITULO_TABELA,
                    new String[]{
                            COLUNA_ID,
                            COLUNA_NOME,
                            COLUNA_VALOR,
                            COLUNA_QUANTIDADE_TOTAL,
                            COLUNA_QUANTIDADE_MINIMA
                    });

            // Testa se foi encontrado um produto
            if(ponteiro != null){
                if(ponteiro.getCount() != 0) {
                    // Transfere de Cursor para ArrayList<>
                    int cont = 0;
                    do {
                        Produtos temp_produto = new Produtos(activity, bancoDados);
                        if (cont == 0) {
                            ponteiro.moveToFirst();
                            cont++;
                        }

                        temp_produto.setCodProduto(ponteiro.getInt(0));
                        temp_produto.setNomeProduto(ponteiro.getString(1));
                        temp_produto.setValorProduto(ponteiro.getDouble(2));
                        temp_produto.setQuantidadeTotal(ponteiro.getInt(3));
                        temp_produto.setQuantidadeMinima(ponteiro.getInt(4));

                        lista_produtos.add(temp_produto);

                    } while (ponteiro.moveToNext());
                    ponteiro.close();
                }
            }
            else{
                CxMsg.erro(activity, "Não foi encontrado nenhum produto.");
            }
        bancoDados.fecharDB();
        return lista_produtos;
    }
}
