package com.exemplo.treinadores.erro;

// Exceção de negócio. Quem lança diz o quê e em qual campo; o formato da resposta é problema do ApiExceptionHandler.
public class ErroDeDominio extends RuntimeException {

    private final int status;
    private final String campo;

    public ErroDeDominio(String mensagem) {
        this(mensagem, null);
    }

    public ErroDeDominio(String mensagem, String campo) {
        this(400, mensagem, campo);
    }

    private ErroDeDominio(int status, String mensagem, String campo) {
        super(mensagem);
        this.status = status;
        this.campo = campo;
    }

    public static ErroDeDominio naoEncontrado(String mensagem) {
        return new ErroDeDominio(404, mensagem, null);
    }

    public int getStatus() {
        return status;
    }

    public String getCampo() {
        return campo;
    }
}
