package org.example;

public class SessionManager {
    private static int idConta;
    private static String nomeUsuario;

    public static void setIdConta(int id) { idConta = id; }
    public static int getIdConta() { return idConta; }

    public static void setNome(String nome) { nomeUsuario = nome; }
    public static String getNome() { return nomeUsuario; }

    public static void limparSessao() {
        idConta = 0;
        nomeUsuario = null;
    }
}