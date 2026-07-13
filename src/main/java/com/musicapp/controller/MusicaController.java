package com.musicapp.controller;

import com.musicapp.dao.IDAO;
import com.musicapp.dao.musica.MusicaFileDAO;
import com.musicapp.dao.musica.MusicaSQLiteDAO;
import com.musicapp.model.Musica;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller de Música (padrão MVC). Faz a ponte entre a View (CadMusicaView)
 * e a persistência, aplicando validações de negócio.
 * <p>
 * Depende apenas da abstração {@link IDAO} (Inversão de Dependência): a troca
 * entre arquivo e banco de dados é feita sem alterar nenhum outro código.
 */
public class MusicaController {

    private IDAO<Musica, String> dao;

    public MusicaController() {
        this.dao = new MusicaFileDAO();
    }

    public void usarArquivo() {
        this.dao = new MusicaFileDAO();
    }

    public void usarBancoDeDados() {
        this.dao = new MusicaSQLiteDAO();
    }

    /** Permite injetar qualquer implementação de IDAO (útil também para testes). */
    public void setDao(IDAO<Musica, String> dao) {
        this.dao = dao;
    }

    public void incluir(Musica m) {
        validar(m);
        dao.incluir(m);
    }

    public void alterar(Musica m) {
        validar(m);
        dao.alterar(m);
    }

    public void excluir(String id) {
        dao.excluir(id);
    }

    public Musica consultar(String id) {
        return dao.consultar(id);
    }

    public List<Musica> listar() {
        return dao.listar();
    }

    /** Pesquisa músicas cujo título contenha o trecho informado. */
    public List<Musica> pesquisarPorTitulo(String trecho) {
        List<Musica> resultado = new ArrayList<>();
        String alvo = trecho == null ? "" : trecho.toLowerCase();
        for (Musica m : dao.listar()) {
            if (m.getTitulo() != null && m.getTitulo().toLowerCase().contains(alvo)) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    private void validar(Musica m) {
        if (m.getTitulo() == null || m.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("O título da música é obrigatório.");
        }
        if (m.getDuracao() <= 0) {
            throw new IllegalArgumentException("A duração deve ser maior que zero.");
        }
    }
}
