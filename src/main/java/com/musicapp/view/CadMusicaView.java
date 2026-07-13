package com.musicapp.view;

import com.musicapp.controller.MusicaController;
import com.musicapp.model.Musica;
import com.musicapp.view.tablemodel.MusicaTableModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

/**
 * Tela (View) do cadastro de Música. Usa {@link MusicaTableModel}
 * (AbstractTableModel) para alimentar a JTable e delega toda a regra de
 * negócio/persistência para {@link MusicaController}.
 */
public class CadMusicaView extends JFrame {

    private final MusicaController controller = new MusicaController();
    private final MusicaTableModel tableModel = new MusicaTableModel();

    private JTextField txtTitulo;
    private JTextField txtMinutos;
    private JTextField txtSegundos;
    private JTextField txtPesquisa;
    private JTable tabela;
    private JComboBox<String> cbFonte;

    private Musica musicaSelecionada;

    public CadMusicaView() {
        super("Cadastro de Músicas");
        montarTela();
        atualizarTabela();
        setSize(650, 470);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void montarTela() {
        setLayout(new BorderLayout(10, 10));

        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createTitledBorder("Dados da Música"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelForm.add(new JLabel("Título:"), gbc);
        txtTitulo = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        painelForm.add(txtTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelForm.add(new JLabel("Duração (min:seg):"), gbc);
        txtMinutos = new JTextField(4);
        gbc.gridx = 1;
        painelForm.add(txtMinutos, gbc);
        gbc.gridx = 2;
        painelForm.add(new JLabel(":"), gbc);
        txtSegundos = new JTextField(4);
        gbc.gridx = 3;
        painelForm.add(txtSegundos, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelForm.add(new JLabel("Fonte de dados:"), gbc);
        cbFonte = new JComboBox<>(new String[]{"Arquivo (XML)", "Banco de Dados (SQLite)"});
        cbFonte.addActionListener(e -> trocarFonte());
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        painelForm.add(cbFonte, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnIncluir = new JButton("Incluir");
        JButton btnAlterar = new JButton("Alterar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");
        btnIncluir.addActionListener(this::incluir);
        btnAlterar.addActionListener(this::alterar);
        btnExcluir.addActionListener(this::excluir);
        btnLimpar.addActionListener(e -> limparCampos());
        painelBotoes.add(btnIncluir);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.add(painelForm, BorderLayout.CENTER);
        painelTopo.add(painelBotoes, BorderLayout.SOUTH);

        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelPesquisa.add(new JLabel("Pesquisar por título:"));
        txtPesquisa = new JTextField(20);
        painelPesquisa.add(txtPesquisa);
        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> pesquisar());
        painelPesquisa.add(btnPesquisar);
        JButton btnListarTodas = new JButton("Listar Todas");
        btnListarTodas.addActionListener(e -> atualizarTabela());
        painelPesquisa.add(btnListarTodas);

        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarLinha();
            }
        });
        JScrollPane scroll = new JScrollPane(tabela);

        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.add(painelPesquisa, BorderLayout.NORTH);
        painelCentro.add(scroll, BorderLayout.CENTER);

        add(painelTopo, BorderLayout.NORTH);
        add(painelCentro, BorderLayout.CENTER);
    }

    private void trocarFonte() {
        if (cbFonte.getSelectedIndex() == 0) {
            controller.usarArquivo();
        } else {
            controller.usarBancoDeDados();
        }
        atualizarTabela();
    }

    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        musicaSelecionada = tableModel.getMusicaNaLinha(linha);
        if (musicaSelecionada != null) {
            txtTitulo.setText(musicaSelecionada.getTitulo());
            txtMinutos.setText(String.valueOf(musicaSelecionada.getDuracao() / 60));
            txtSegundos.setText(String.valueOf(musicaSelecionada.getDuracao() % 60));
        }
    }

    private int obterDuracaoEmSegundos() {
        String txtMin = txtMinutos.getText().trim();
        String txtSeg = txtSegundos.getText().trim();
        int min = txtMin.isEmpty() ? 0 : Integer.parseInt(txtMin);
        int seg = txtSeg.isEmpty() ? 0 : Integer.parseInt(txtSeg);
        return min * 60 + seg;
    }

    private void incluir(ActionEvent e) {
        try {
            Musica m = new Musica(txtTitulo.getText().trim(), obterDuracaoEmSegundos());
            controller.incluir(m);
            JOptionPane.showMessageDialog(this, "Música incluída com sucesso!");
            limparCampos();
            atualizarTabela();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe minutos/segundos válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar(ActionEvent e) {
        if (musicaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma música na tabela.");
            return;
        }
        try {
            musicaSelecionada.setTitulo(txtTitulo.getText().trim());
            musicaSelecionada.setDuracao(obterDuracaoEmSegundos());
            controller.alterar(musicaSelecionada);
            JOptionPane.showMessageDialog(this, "Música alterada com sucesso!");
            limparCampos();
            atualizarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir(ActionEvent e) {
        if (musicaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma música na tabela.");
            return;
        }
        int opcao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir esta música?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            controller.excluir(musicaSelecionada.getId());
            limparCampos();
            atualizarTabela();
        }
    }

    private void pesquisar() {
        String texto = txtPesquisa.getText().trim();
        if (texto.isEmpty()) {
            atualizarTabela();
        } else {
            tableModel.setMusicas(controller.pesquisarPorTitulo(texto));
        }
    }

    private void atualizarTabela() {
        tableModel.setMusicas(controller.listar());
    }

    private void limparCampos() {
        txtTitulo.setText("");
        txtMinutos.setText("");
        txtSegundos.setText("");
        musicaSelecionada = null;
        tabela.clearSelection();
    }
}
