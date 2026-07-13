package com.musicapp.view.tablemodel;

import com.musicapp.model.Artista;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * AbstractTableModel usado no cadastro de Artista.
 */
public class ArtistaTableModel extends AbstractTableModel {

    private final String[] colunas = {"Nome", "Qtd. Álbuns"};
    private List<Artista> artistas = new ArrayList<>();

    public void setArtistas(List<Artista> artistas) {
        this.artistas = artistas != null ? artistas : new ArrayList<>();
        fireTableDataChanged();
    }

    public Artista getArtistaNaLinha(int linha) {
        if (linha < 0 || linha >= artistas.size()) {
            return null;
        }
        return artistas.get(linha);
    }

    @Override
    public int getRowCount() {
        return artistas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Artista a = artistas.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return a.getNome();
            case 1:
                return a.getAlbuns().size();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
