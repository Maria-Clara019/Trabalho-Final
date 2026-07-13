package com.musicapp.view;

import com.musicapp.controller.AlbumController;
import com.musicapp.controller.ArtistaController;
import com.musicapp.model.Album;
import com.musicapp.model.Artista;
import com.musicapp.model.Musica;
import com.musicapp.view.tablemodel.AlbumTableModel;
import com.musicapp.view.tablemodel.ArtistaTableModel;

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
import java.util.List;

/**
 * Tela (View) do cadastro de Artista. Usa {@link ArtistaTableModel} para a
 * lista geral de artistas e {@link AlbumTableModel} para exibir os álbuns
 * que compõem o artista em edição/seleção.
 */
public class CadArtistaView extends JFrame {

    private final ArtistaController artistaController = new ArtistaController();
    private final AlbumController albumController = new AlbumController();

    private final ArtistaTableModel tableModelArtistas = new ArtistaTableModel();
    private final AlbumTableModel tableModelAlbunsDoArtista = new AlbumTableModel();

    private JTextField txtNome;
    private JTextField txtPesquisaArtista;
    private JTextField txtPesquisaAlbum;
    private JTextField txtPesquisaMusica;
    private JComboBox<Album> cbAlbunsDisponiveis;
    private JComboBox<String> cbFonte;
    private JTable tabelaArtistas;
    private JTable tabelaAlbunsDoArtista;

    private Artista artistaSelecionado;
    private Artista artistaEmEdicao = new Artista();

    public CadArtistaView() {
        super("Cadastro de Artistas");
        montarTela();
        atualizarTabelaArtistas();
        atualizarComboAlbuns();
        setSize(840, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void montarTela() {
        setLayout(new BorderLayout(10, 10));

        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createTitledBorder("Dados do Artista"));
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
        painelForm.add(new JLabel("Fonte de dados:"), gbc);
        cbFonte = new JComboBox<>(new String[]{"Arquivo (XML)", "Banco de Dados (SQLite)"});
        cbFonte.addActionListener(e -> trocarFonte());
        gbc.gridx = 1;
        painelForm.add(cbFonte, gbc);

        JPanel painelAlbunsArtista = new JPanel(new BorderLayout(5, 5));
        painelAlbunsArtista.setBorder(BorderFactory.createTitledBorder("Álbuns do Artista"));
        JPanel painelAddAlbum = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbAlbunsDisponiveis = new JComboBox<>();
        JButton btnAddAlbum = new JButton("Adicionar");
        btnAddAlbum.addActionListener(e -> adicionarAlbumAoArtista());
        JButton btnRemAlbum = new JButton("Remover Selecionado");
        btnRemAlbum.addActionListener(e -> removerAlbumDoArtista());
        painelAddAlbum.add(new JLabel("Álbum:"));
        painelAddAlbum.add(cbAlbunsDisponiveis);
        painelAddAlbum.add(btnAddAlbum);
        painelAddAlbum.add(btnRemAlbum);
        tabelaAlbunsDoArtista = new JTable(tableModelAlbunsDoArtista);
        painelAlbunsArtista.add(painelAddAlbum, BorderLayout.NORTH);
        painelAlbunsArtista.add(new JScrollPane(tabelaAlbunsDoArtista), BorderLayout.CENTER);

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
        painelEsquerda.add(painelAlbunsArtista, BorderLayout.CENTER);
        painelEsquerda.add(painelBotoes, BorderLayout.SOUTH);

        JPanel painelPesquisa = new JPanel(new GridLayout(3, 1));
        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha1.add(new JLabel("Pesquisar artista por nome:"));
        txtPesquisaArtista = new JTextField(14);
        linha1.add(txtPesquisaArtista);
        JButton btnPesquisarArtista = new JButton("Pesquisar");
        btnPesquisarArtista.addActionListener(e -> pesquisarArtista());
        linha1.add(btnPesquisarArtista);
        JButton btnListarTodos = new JButton("Listar Todos");
        btnListarTodos.addActionListener(e -> atualizarTabelaArtistas());
        linha1.add(btnListarTodos);

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha2.add(new JLabel("Pesquisar álbum (do artista selecionado):"));
        txtPesquisaAlbum = new JTextField(14);
        linha2.add(txtPesquisaAlbum);
        JButton btnPesquisarAlbum = new JButton("Pesquisar");
        btnPesquisarAlbum.addActionListener(e -> pesquisarAlbumDoArtista());
        linha2.add(btnPesquisarAlbum);

        JPanel linha3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha3.add(new JLabel("Pesquisar música (do artista selecionado):"));
        txtPesquisaMusica = new JTextField(14);
        linha3.add(txtPesquisaMusica);
        JButton btnPesquisarMusica = new JButton("Pesquisar");
        btnPesquisarMusica.addActionListener(e -> pesquisarMusicaDoArtista());
        linha3.add(btnPesquisarMusica);

        painelPesquisa.add(linha1);
        painelPesquisa.add(linha2);
        painelPesquisa.add(linha3);

        tabelaArtistas = new JTable(tableModelArtistas);
        tabelaArtistas.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                selecionarArtista();
            }
        });
        JScrollPane scrollArtistas = new JScrollPane(tabelaArtistas);

        JPanel painelDireita = new JPanel(new BorderLayout(5, 5));
        painelDireita.add(painelPesquisa, BorderLayout.NORTH);
        painelDireita.add(scrollArtistas, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerda, painelDireita);
        split.setDividerLocation(440);
        add(split, BorderLayout.CENTER);
    }

    private void trocarFonte() {
        if (cbFonte.getSelectedIndex() == 0) {
            artistaController.usarArquivo();
            albumController.usarArquivo();
        } else {
            artistaController.usarBancoDeDados();
            albumController.usarBancoDeDados();
        }
        atualizarTabelaArtistas();
        atualizarComboAlbuns();
    }

    private void atualizarComboAlbuns() {
        cbAlbunsDisponiveis.removeAllItems();
        for (Album a : albumController.listar()) {
            cbAlbunsDisponiveis.addItem(a);
        }
    }

    private void adicionarAlbumAoArtista() {
        Album a = (Album) cbAlbunsDisponiveis.getSelectedItem();
        if (a == null) {
            JOptionPane.showMessageDialog(this, "Cadastre álbuns antes, na tela Cadastro de Álbuns.");
            return;
        }
        artistaEmEdicao.adicionarAlbum(a);
        tableModelAlbunsDoArtista.setAlbuns(artistaEmEdicao.getAlbuns());
    }

    private void removerAlbumDoArtista() {
        int linha = tabelaAlbunsDoArtista.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um álbum na lista do artista.");
            return;
        }
        Album a = tableModelAlbunsDoArtista.getAlbumNaLinha(linha);
        artistaEmEdicao.removerAlbum(a);
        tableModelAlbunsDoArtista.setAlbuns(artistaEmEdicao.getAlbuns());
    }

    private void selecionarArtista() {
        int linha = tabelaArtistas.getSelectedRow();
        if (linha < 0) {
            return;
        }
        artistaSelecionado = tableModelArtistas.getArtistaNaLinha(linha);
        if (artistaSelecionado != null) {
            artistaEmEdicao = artistaSelecionado;
            txtNome.setText(artistaSelecionado.getNome());
            tableModelAlbunsDoArtista.setAlbuns(artistaEmEdicao.getAlbuns());
        }
    }

    private void incluir() {
        try {
            artistaEmEdicao.setNome(txtNome.getText().trim());
            artistaController.incluir(artistaEmEdicao);
            JOptionPane.showMessageDialog(this, "Artista incluído com sucesso!");
            limparCampos();
            atualizarTabelaArtistas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar() {
        if (artistaSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um artista na tabela.");
            return;
        }
        try {
            artistaSelecionado.setNome(txtNome.getText().trim());
            artistaController.alterar(artistaSelecionado);
            JOptionPane.showMessageDialog(this, "Artista alterado com sucesso!");
            limparCampos();
            atualizarTabelaArtistas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (artistaSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um artista na tabela.");
            return;
        }
        int opcao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este artista?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            artistaController.excluir(artistaSelecionado.getId());
            limparCampos();
            atualizarTabelaArtistas();
        }
    }

    private void pesquisarArtista() {
        String texto = txtPesquisaArtista.getText().trim();
        if (texto.isEmpty()) {
            atualizarTabelaArtistas();
        } else {
            tableModelArtistas.setArtistas(artistaController.pesquisarPorNome(texto));
        }
    }

    private void pesquisarAlbumDoArtista() {
        if (artistaSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um artista primeiro.");
            return;
        }
        String texto = txtPesquisaAlbum.getText().trim();
        Album encontrado = artistaController.pesquisarAlbumPorNome(artistaSelecionado, texto);
        if (encontrado != null) {
            JOptionPane.showMessageDialog(this, "Álbum encontrado: " + encontrado);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum álbum com esse nome para este artista.");
        }
    }

    private void pesquisarMusicaDoArtista() {
        if (artistaSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um artista primeiro.");
            return;
        }
        String texto = txtPesquisaMusica.getText().trim();
        List<Musica> encontradas = artistaController.pesquisarMusicaPorTitulo(artistaSelecionado, texto);
        if (encontradas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma música com esse título para este artista.");
        } else {
            StringBuilder sb = new StringBuilder("Músicas encontradas:\n");
            for (Musica m : encontradas) {
                sb.append("- ").append(m).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        }
    }

    private void atualizarTabelaArtistas() {
        tableModelArtistas.setArtistas(artistaController.listar());
    }

    private void limparCampos() {
        txtNome.setText("");
        artistaSelecionado = null;
        artistaEmEdicao = new Artista();
        tableModelAlbunsDoArtista.setAlbuns(artistaEmEdicao.getAlbuns());
        tabelaArtistas.clearSelection();
        atualizarComboAlbuns();
    }
}
