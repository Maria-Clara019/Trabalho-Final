package com.musicapp;

import com.musicapp.view.MainView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Se não conseguir aplicar o look and feel do sistema, segue com o padrão.
        }

        File pastaDados = new File("dados");
        if (!pastaDados.exists()) {
            pastaDados.mkdirs();
        }

        SwingUtilities.invokeLater(() -> new MainView().setVisible(true));
    }
}
