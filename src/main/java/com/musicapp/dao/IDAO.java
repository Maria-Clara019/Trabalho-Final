package com.musicapp.dao;

import java.util.List;

/**
 * Interface genérica que define o contrato de persistência para qualquer entidade.
 * <p>
 * É a base da Inversão de Dependência (DIP) do sistema: os Controllers dependem
 * apenas desta abstração, nunca de uma implementação concreta (arquivo ou banco
 * de dados). Isso permite trocar a forma de persistência em tempo de execução
 * sem alterar uma linha sequer da camada de controle ou de visão.
 *
 * @param <T>  tipo da entidade (Musica, Album, Artista...)
 * @param <ID> tipo do identificador da entidade
 */
public interface IDAO<T, ID> {

    void incluir(T obj);

    void alterar(T obj);

    void excluir(ID id);

    T consultar(ID id);

    List<T> listar();
}
