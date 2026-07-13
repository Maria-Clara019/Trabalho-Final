package com.musicapp.persistence;

import com.musicapp.dao.IDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Implementação genérica e reutilizável de {@link IDAO} que mantém a lista de
 * objetos em memória e a persiste em arquivo a cada alteração, delegando o
 * formato de gravação/leitura a um {@link Serializador}.
 * <p>
 * Como não é possível, em Java, obter o identificador de um tipo genérico T em
 * tempo de compilação, o "extrator" de id é recebido via referência de método
 * (ex.: {@code Musica::getId}), mantendo a classe totalmente reutilizável para
 * qualquer entidade do sistema.
 *
 * @param <T>  tipo da entidade
 * @param <ID> tipo do identificador da entidade
 */
public class FilePersistence<T, ID> implements IDAO<T, ID> {

    private final String caminhoArquivo;
    private final Serializador<T> serializador;
    private final Function<T, ID> extratorId;
    private List<T> lista;

    public FilePersistence(String caminhoArquivo, Serializador<T> serializador, Function<T, ID> extratorId) {
        this.caminhoArquivo = caminhoArquivo;
        this.serializador = serializador;
        this.extratorId = extratorId;
        carregar();
    }

    private void carregar() {
        try {
            lista = serializador.ler(caminhoArquivo);
        } catch (Exception e) {
            lista = new ArrayList<>();
        }
    }

    private void salvar() {
        try {
            serializador.gravar(lista, caminhoArquivo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gravar arquivo '" + caminhoArquivo + "': " + e.getMessage(), e);
        }
    }

    @Override
    public void incluir(T obj) {
        lista.add(obj);
        salvar();
    }

    @Override
    public void alterar(T obj) {
        ID id = extratorId.apply(obj);
        for (int i = 0; i < lista.size(); i++) {
            if (extratorId.apply(lista.get(i)).equals(id)) {
                lista.set(i, obj);
                salvar();
                return;
            }
        }
        // Se não encontrou (por segurança), inclui.
        lista.add(obj);
        salvar();
    }

    @Override
    public void excluir(ID id) {
        lista.removeIf(item -> extratorId.apply(item).equals(id));
        salvar();
    }

    @Override
    public T consultar(ID id) {
        for (T item : lista) {
            if (extratorId.apply(item).equals(id)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public List<T> listar() {
        return new ArrayList<>(lista);
    }
}
