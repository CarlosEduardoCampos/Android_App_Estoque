package com.example.meuestoque.models;

import android.app.Activity;
import android.app.AlertDialog;

public class CxMsg {

    // Mesagem padrão para erros(try/catch)
    public static void erro(Activity activity, String txt){
        AlertDialog.Builder alerta = new AlertDialog.Builder(activity);

        alerta.setTitle("!!! Error de Execução !!!");
        alerta.setMessage(txt);
        alerta.setNeutralButton("OK", null);
        alerta.show();
    }

    // Mesagem padrão para erros(try/catch)
    public static void erroExecucao(Activity activity, String txt, Exception erro){
        AlertDialog.Builder alerta = new AlertDialog.Builder(activity);

        alerta.setTitle("!!! Error de Execução !!!");
        alerta.setMessage(txt + "\n<<< DESCRIÇÃO >>>\n" + erro);
        alerta.setNeutralButton("OK", null);
        alerta.show();
    }

    // Mensagem para erros ocorridos quando o usuario fazer merda
    public static void erroHumano(Activity activity, String txt){
        AlertDialog.Builder campo_alerta= new AlertDialog.Builder(activity);

        campo_alerta.setTitle("!!! Error Humano !!!");
        campo_alerta.setMessage(txt);
        campo_alerta.show();
    }
}
