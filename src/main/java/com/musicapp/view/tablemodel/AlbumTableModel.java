package com.musicapp.view.tablemodel;

import com.musicapp.model.Album;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * AbstractTableModel usado no cadastro de Álbum (e também para exibir os
 * álbuns pertencentes a um artista em CadArtistaView).
 */
public class AlbumTableModel extends AbstractTableModel {

    private final String[] colunas = {"Nome", "Ano", "Qtd. Músicas"};
    private List<Album> albuns = new ArrayList<>();

    public void setAlbuns(List<Album> albuns) {
        this.albuns = albuns != null ? albuns : new ArrayList<>();
        fireTableDataChanged();
    }

    public Album getAlbumNaLinha(int linha) {
        if (linha < 0 || linha >= albuns.size()) {
            return null;
        }
        return albuns.get(linha);
    }

    @Override
    public int getRowCount() {
        return albuns.size();
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
        Album a = albuns.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return a.getNome();
            case 1:
                return a.getAnoLancamento();
            case 2:
                return a.getMusicas().size();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
