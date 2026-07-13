package com.musicapp.persistence;

import java.util.List;

/**
 * Abstrai a forma como uma lista de objetos é convertida de/para um arquivo em disco.
 * Novas estratégias (JSON, CSV, etc.) podem ser adicionadas implementando esta
 * interface, sem qualquer alteração em {@link FilePersistence}.
 *
 * @param <T> tipo dos objetos a serializar
 */
public interface Serializador<T> {

    void gravar(List<T> lista, String caminhoArquivo) throws Exception;

    List<T> ler(String caminhoArquivo) throws Exception;
}
