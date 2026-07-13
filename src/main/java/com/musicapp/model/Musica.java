package com.musicapp.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa uma música do sistema.
 * Segue as convenções de JavaBean (construtor sem argumentos + getters/setters)
 * para permitir a serialização via XMLEncoder/XMLDecoder.
 */
public class Musica implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String titulo;
    private int duracao; // duração em segundos

    public Musica() {
        this.id = UUID.randomUUID().toString();
    }

    public Musica(String titulo, int duracao) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.duracao = duracao;
    }

    public Musica(String id, String titulo, int duracao) {
        this.id = id;
        this.titulo = titulo;
        this.duracao = duracao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    /** Retorna a duração formatada como mm:ss, útil para exibição na tabela. */
    public String getDuracaoFormatada() {
        int min = duracao / 60;
        int seg = duracao % 60;
        return String.format("%02d:%02d", min, seg);
    }

    @Override
    public String toString() {
        return titulo + " - " + getDuracaoFormatada();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Musica)) return false;
        Musica musica = (Musica) o;
        return Objects.equals(id, musica.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
