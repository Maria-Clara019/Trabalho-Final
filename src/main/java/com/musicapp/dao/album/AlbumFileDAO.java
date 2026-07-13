package com.musicapp.dao.album;

import com.musicapp.model.Album;
import com.musicapp.persistence.FilePersistence;
import com.musicapp.persistence.XMLSerializador;

/**
 * DAO de Álbum que persiste em arquivo XML (inclui a lista de músicas do álbum).
 */
public class AlbumFileDAO extends FilePersistence<Album, String> {

    public AlbumFileDAO() {
        super("dados/albuns.xml", new XMLSerializador<>(), Album::getId);
    }
}
