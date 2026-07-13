package com.musicapp.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Tela de menu principal do sistema.
 */
public class MainView extends JFrame {

    public MainView() {
        super("Sistema de Gerenciamento de Músicas");
        montarTela();
        setSize(420, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void montarTela() {
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel titulo = new JLabel("Menu Principal", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));

        JButton btnMusica = new JButton("Cadastro de Músicas");
        JButton btnAlbum = new JButton("Cadastro de Álbuns");
        JButton btnArtista = new JButton("Cadastro de Artistas");

        btnMusica.addActionListener(e -> new CadMusicaView().setVisible(true));
        btnAlbum.addActionListener(e -> new CadAlbumView().setVisible(true));
        btnArtista.addActionListener(e -> new CadArtistaView().setVisible(true));

        add(titulo);
        add(btnMusica);
        add(btnAlbum);
        add(btnArtista);
    }
}
