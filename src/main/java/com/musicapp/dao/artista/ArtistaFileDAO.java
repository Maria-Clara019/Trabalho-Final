package com.musicapp.dao.artista;

import com.musicapp.model.Artista;
import com.musicapp.persistence.FilePersistence;
import com.musicapp.persistence.XMLSerializador;

/**
 * DAO de Artista que persiste em arquivo XML (inclui a lista de álbuns do artista).
 */
public class ArtistaFileDAO extends FilePersistence<Artista, String> {

    public ArtistaFileDAO() {
        super("dados/artistas.xml", new XMLSerializador<>(), Artista::getId);
    }
}
