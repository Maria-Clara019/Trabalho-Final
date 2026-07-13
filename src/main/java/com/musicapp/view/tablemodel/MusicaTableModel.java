package com.musicapp.view.tablemodel;

import com.musicapp.model.Musica;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * AbstractTableModel usado no cadastro de Música (e também para exibir as
 * músicas pertencentes a um álbum em CadAlbumView).
 */
public class MusicaTableModel extends AbstractTableModel {

    private final String[] colunas = {"Título", "Duração"};
    private List<Musica> musicas = new ArrayList<>();

    public void setMusicas(List<Musica> musicas) {
        this.musicas = musicas != null ? musicas : new ArrayList<>();
        fireTableDataChanged();
    }

    public Musica getMusicaNaLinha(int linha) {
        if (linha < 0 || linha >= musicas.size()) {
            return null;
        }
        return musicas.get(linha);
    }

    @Override
    public int getRowCount() {
        return musicas.size();
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
        Musica m = musicas.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return m.getTitulo();
            case 1:
                return m.getDuracaoFormatada();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
