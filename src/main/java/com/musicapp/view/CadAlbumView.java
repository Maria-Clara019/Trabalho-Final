package com.musicapp.view;

import com.musicapp.controller.AlbumController;
import com.musicapp.controller.MusicaController;
import com.musicapp.model.Album;
import com.musicapp.model.Musica;
import com.musicapp.view.tablemodel.AlbumTableModel;
import com.musicapp.view.tablemodel.MusicaTableModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

/**
 * Tela (View) do cadastro de Álbum. Usa {@link AlbumTableModel} para a lista
 * geral de álbuns e {@link MusicaTableModel} para exibir as músicas que
 * compõem o álbum em edição/seleção.
 */
public class CadAlbumView extends JFrame {

    private final AlbumController albumController = new AlbumController();
    private final MusicaController musicaController = new MusicaController();

    private final AlbumTableModel tableModelAlbuns = new AlbumTableModel();
    private final MusicaTableModel tableModelMusicasDoAlbum = new MusicaTableModel();

    private JTextField txtNome;
    private JTextField txtAno;
    private JTextField txtPesquisaAlbum;
    private JTextField txtPesquisaMusica;
    private JComboBox<Musica> cbMusicasDisponiveis;
    private JComboBox<String> cbFonte;
    private JTable tabelaAlbuns;
    private JTable tabelaMusicasDoAlbum;

    private Album albumSelecionado;
    private Album albumEmEdicao = new Album();

    public CadAlbumView() {
        super("Cadastro de Álbuns");
        montarTela();
        atualizarTabelaAlbuns();
        atualizarComboMusicas();
        setSize(820, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void montarTela() {
        setLayout(new BorderLayout(10, 10));

        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createTitledBorder("Dados do Álbum"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelForm.add(new JLabel("Nome:"), gbc);
        txtNome = new JTextField(18);
        gbc.gridx = 1;
        painelForm.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelForm.add(new JLabel("Ano de lançamento:"), gbc);
        txtAno = new JTextField(6);
        gbc.gridx = 1;
        painelForm.add(txtAno, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelForm.add(new JLabel("Fonte de dados:"), gbc);
        cbFonte = new JComboBox<>(new String[]{"Arquivo (XML)", "Banco de Dados (SQLite)"});
        cbFonte.addActionListener(e -> trocarFonte());
        gbc.gridx = 1;
        painelForm.add(cbFonte, gbc);

        JPanel painelMusicasAlbum = new JPanel(new BorderLayout(5, 5));
        painelMusicasAlbum.setBorder(BorderFactory.createTitledBorder("Músicas do Álbum"));
        JPanel painelAddMusica = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbMusicasDisponiveis = new JComboBox<>();
        JButton btnAddMusica = new JButton("Adicionar");
        btnAddMusica.addActionListener(e -> adicionarMusicaAoAlbum());
        JButton btnRemMusica = new JButton("Remover Selecionada");
        btnRemMusica.addActionListener(e -> removerMusicaDoAlbum());
        painelAddMusica.add(new JLabel("Música:"));
        painelAddMusica.add(cbMusicasDisponiveis);
        painelAddMusica.add(btnAddMusica);
        painelAddMusica.add(btnRemMusica);
        tabelaMusicasDoAlbum = new JTable(tableModelMusicasDoAlbum);
        painelMusicasAlbum.add(painelAddMusica, BorderLayout.NORTH);
        painelMusicasAlbum.add(new JScrollPane(tabelaMusicasDoAlbum), BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnIncluir = new JButton("Incluir");
        JButton btnAlterar = new JButton("Alterar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");
        btnIncluir.addActionListener(e -> incluir());
        btnAlterar.addActionListener(e -> alterar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparCampos());
        painelBotoes.add(btnIncluir);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        JPanel painelEsquerda = new JPanel(new BorderLayout(5, 5));
        painelEsquerda.add(painelForm, BorderLayout.NORTH);
        painelEsquerda.add(painelMusicasAlbum, BorderLayout.CENTER);
        painelEsquerda.add(painelBotoes, BorderLayout.SOUTH);

        JPanel painelPesquisa = new JPanel(new GridLayout(2, 1));
        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha1.add(new JLabel("Pesquisar álbum por nome:"));
        txtPesquisaAlbum = new JTextField(14);
        linha1.add(txtPesquisaAlbum);
        JButton btnPesquisarAlbum = new JButton("Pesquisar");
        btnPesquisarAlbum.addActionListener(e -> pesquisarAlbum());
        linha1.add(btnPesquisarAlbum);
        JButton btnListarTodos = new JButton("Listar Todos");
        btnListarTodos.addActionListener(e -> atualizarTabelaAlbuns());
        linha1.add(btnListarTodos);

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha2.add(new JLabel("Pesquisar música (no álbum selecionado):"));
        txtPesquisaMusica = new JTextField(14);
        linha2.add(txtPesquisaMusica);
        JButton btnPesquisarMusica = new JButton("Pesquisar");
        btnPesquisarMusica.addActionListener(e -> pesquisarMusicaNoAlbum());
        linha2.add(btnPesquisarMusica);

        painelPesquisa.add(linha1);
        painelPesquisa.add(linha2);

        tabelaAlbuns = new JTable(tableModelAlbuns);
        tabelaAlbuns.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                selecionarAlbum();
            }
        });
        JScrollPane scrollAlbuns = new JScrollPane(tabelaAlbuns);

        JPanel painelDireita = new JPanel(new BorderLayout(5, 5));
        painelDireita.add(painelPesquisa, BorderLayout.NORTH);
        painelDireita.add(scrollAlbuns, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerda, painelDireita);
        split.setDividerLocation(430);
        add(split, BorderLayout.CENTER);
    }

    private void trocarFonte() {
        if (cbFonte.getSelectedIndex() == 0) {
            albumController.usarArquivo();
            musicaController.usarArquivo();
        } else {
            albumController.usarBancoDeDados();
            musicaController.usarBancoDeDados();
        }
        atualizarTabelaAlbuns();
        atualizarComboMusicas();
    }

    private void atualizarComboMusicas() {
        cbMusicasDisponiveis.removeAllItems();
        for (Musica m : musicaController.listar()) {
            cbMusicasDisponiveis.addItem(m);
        }
    }

    private void adicionarMusicaAoAlbum() {
        Musica m = (Musica) cbMusicasDisponiveis.getSelectedItem();
        if (m == null) {
            JOptionPane.showMessageDialog(this, "Cadastre músicas antes, na tela Cadastro de Músicas.");
            return;
        }
        albumEmEdicao.adicionarMusica(m);
        tableModelMusicasDoAlbum.setMusicas(albumEmEdicao.getMusicas());
    }

    private void removerMusicaDoAlbum() {
        int linha = tabelaMusicasDoAlbum.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma música na lista do álbum.");
            return;
        }
        Musica m = tableModelMusicasDoAlbum.getMusicaNaLinha(linha);
        albumEmEdicao.removerMusica(m);
        tableModelMusicasDoAlbum.setMusicas(albumEmEdicao.getMusicas());
    }

    private void selecionarAlbum() {
        int linha = tabelaAlbuns.getSelectedRow();
        if (linha < 0) {
            return;
        }
        albumSelecionado = tableModelAlbuns.getAlbumNaLinha(linha);
        if (albumSelecionado != null) {
            albumEmEdicao = albumSelecionado;
            txtNome.setText(albumSelecionado.getNome());
            txtAno.setText(String.valueOf(albumSelecionado.getAnoLancamento()));
            tableModelMusicasDoAlbum.setMusicas(albumEmEdicao.getMusicas());
        }
    }

    private void incluir() {
        try {
            albumEmEdicao.setNome(txtNome.getText().trim());
            albumEmEdicao.setAnoLancamento(Integer.parseInt(txtAno.getText().trim()));
            albumController.incluir(albumEmEdicao);
            JOptionPane.showMessageDialog(this, "Álbum incluído com sucesso!");
            limparCampos();
            atualizarTabelaAlbuns();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe um ano válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar() {
        if (albumSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um álbum na tabela.");
            return;
        }
        try {
            albumSelecionado.setNome(txtNome.getText().trim());
            albumSelecionado.setAnoLancamento(Integer.parseInt(txtAno.getText().trim()));
            albumController.alterar(albumSelecionado);
            JOptionPane.showMessageDialog(this, "Álbum alterado com sucesso!");
            limparCampos();
            atualizarTabelaAlbuns();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (albumSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um álbum na tabela.");
            return;
        }
        int opcao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este álbum?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            albumController.excluir(albumSelecionado.getId());
            limparCampos();
            atualizarTabelaAlbuns();
        }
    }

    private void pesquisarAlbum() {
        String texto = txtPesquisaAlbum.getText().trim();
        if (texto.isEmpty()) {
            atualizarTabelaAlbuns();
        } else {
            tableModelAlbuns.setAlbuns(albumController.pesquisarPorNome(texto));
        }
    }

    private void pesquisarMusicaNoAlbum() {
        if (albumSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um álbum primeiro.");
            return;
        }
        String texto = txtPesquisaMusica.getText().trim();
        Musica encontrada = albumSelecionado.pesquisarMusicaPorTitulo(texto);
        if (encontrada != null) {
            JOptionPane.showMessageDialog(this, "Música encontrada: " + encontrada);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma música com esse título neste álbum.");
        }
    }

    private void atualizarTabelaAlbuns() {
        tableModelAlbuns.setAlbuns(albumController.listar());
    }

    private void limparCampos() {
        txtNome.setText("");
        txtAno.setText("");
        albumSelecionado = null;
        albumEmEdicao = new Album();
        tableModelMusicasDoAlbum.setMusicas(albumEmEdicao.getMusicas());
        tabelaAlbuns.clearSelection();
        atualizarComboMusicas();
    }
}
