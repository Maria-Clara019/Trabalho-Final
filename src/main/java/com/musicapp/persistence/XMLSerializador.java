package com.musicapp.persistence;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação de {@link Serializador} que grava e lê listas de objetos em
 * formato XML usando {@link XMLEncoder}/{@link XMLDecoder} do próprio JDK.
 * <p>
 * Funciona para qualquer classe que siga as convenções de JavaBean (construtor
 * sem argumentos + getters/setters), como Musica, Album e Artista deste projeto.
 */
public class XMLSerializador<T> implements Serializador<T> {

    @Override
    public void gravar(List<T> lista, String caminhoArquivo) throws Exception {
        File arquivo = new File(caminhoArquivo);
        File pai = arquivo.getParentFile();
        if (pai != null && !pai.exists()) {
            pai.mkdirs();
        }
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(arquivo)))) {
            encoder.writeObject(lista);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> ler(String caminhoArquivo) throws Exception {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(arquivo)))) {
            Object objeto = decoder.readObject();
            if (objeto instanceof List) {
                return (List<T>) objeto;
            }
            return new ArrayList<>();
        }
    }
}
