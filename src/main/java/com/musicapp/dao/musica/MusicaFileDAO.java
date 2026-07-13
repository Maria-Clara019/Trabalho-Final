package com.musicapp.dao.musica;

import com.musicapp.model.Musica;
import com.musicapp.persistence.FilePersistence;
import com.musicapp.persistence.XMLSerializador;

/**
 * DAO de Música que persiste em arquivo XML.
 * Toda a lógica de leitura/escrita é reaproveitada de {@link FilePersistence}.
 */
public class MusicaFileDAO extends FilePersistence<Musica, String> {

    public MusicaFileDAO() {
        super("dados/musicas.xml", new XMLSerializador<>(), Musica::getId);
    }
}
