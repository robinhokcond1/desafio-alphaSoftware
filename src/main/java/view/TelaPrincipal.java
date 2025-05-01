package view;

import dao.LivroDAO;
import model.Livro;
import util.LivroCsvExporter;
import util.LivroCsvImporter;
import util.LivroXmlExporter;
import util.OpenLibraryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TelaPrincipal extends JFrame {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private LivroDAO livroDAO;

    public TelaPrincipal() {
        this.livroDAO = new LivroDAO();
        setTitle("Biblioteca - Catálogo de Livros");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Título", "Autores", "ISBN"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabela);

        JPanel painelBotoes = new JPanel();
        JButton btnIncluir = new JButton("Incluir");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnPesquisar = new JButton("Pesquisar");
        JButton btnImportar = new JButton("Importar CSV");
        JButton btnExportar = new JButton("Exportar CSV");
        JButton btnImportarIsbn = new JButton("Importar ISBNs");
        JButton btnExportarXml = new JButton("Exportar XML");

        btnImportar.addActionListener(e -> importarCsv());
        btnExportar.addActionListener(e -> exportarCsv());
        btnImportarIsbn.addActionListener(e -> importarIsbnsViaOpenLibrary());
        btnExportarXml.addActionListener(e -> exportarXml());

        btnIncluir.addActionListener(e -> incluirLivro());
        btnEditar.addActionListener(e -> editarLivro());
        btnExcluir.addActionListener(e -> excluirLivro());
        btnPesquisar.addActionListener(e -> pesquisarLivro());

        painelBotoes.add(btnImportar);
        painelBotoes.add(btnExportar);
        painelBotoes.add(btnImportarIsbn);
        painelBotoes.add(btnExportarXml);
        painelBotoes.add(btnIncluir);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnPesquisar);

        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Livro> livros = livroDAO.listarTodos();
        for (Livro livro : livros) {
            modeloTabela.addRow(new Object[]{
                    livro.getId(),
                    livro.getTitulo(),
                    livro.getAutores(),
                    livro.getIsbn()
            });
        }
    }

    private void incluirLivro() {
        CadastroLivroDialog dialog = new CadastroLivroDialog(this, null, this::carregarTabela);
        dialog.setVisible(true);
    }

    private void editarLivro() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            Long id = (Long) modeloTabela.getValueAt(linhaSelecionada, 0);
            Livro livro = livroDAO.buscarPorId(id);
            CadastroLivroDialog dialog = new CadastroLivroDialog(this, livro, this::carregarTabela);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um livro para editar.");
        }
    }

    private void excluirLivro() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            Long id = (Long) modeloTabela.getValueAt(linhaSelecionada, 0);
            Livro livro = livroDAO.buscarPorId(id);
            int opcao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o livro?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                livroDAO.excluir(livro);
                carregarTabela();
                JOptionPane.showMessageDialog(this, "Livro excluído com sucesso!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um livro para excluir.");
        }
    }

    private void pesquisarLivro() {
        PesquisaLivroDialog dialog = new PesquisaLivroDialog(this);
        dialog.setVisible(true);
    }

    private void importarCsv() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File arquivo = fileChooser.getSelectedFile();
        new LivroCsvImporter(livroDAO).importar(arquivo, this::carregarTabela, this);
    }

    private void exportarCsv() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File arquivo = fileChooser.getSelectedFile();
        new LivroCsvExporter(livroDAO).exportar(arquivo, this);
    }

    private void exportarXml() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File arquivo = fileChooser.getSelectedFile();
        new LivroXmlExporter(livroDAO).exportar(arquivo, this);
    }

    private void importarIsbnsViaOpenLibrary() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File arquivo = fileChooser.getSelectedFile();
        JDialog loadingDialog = new JDialog(this, "Importando", true);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setSize(350, 100);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setLayout(new BorderLayout());
        loadingDialog.add(new JLabel("\u23F3 Importando livros da OpenLibrary, aguarde...", JLabel.CENTER), BorderLayout.CENTER);

        new Thread(() -> {
            AtomicInteger totalImportados = new AtomicInteger();
            AtomicInteger totalFalhas = new AtomicInteger();
            List<String> mensagensErro = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                boolean primeiraLinha = true;

                while ((linha = reader.readLine()) != null) {
                    if (primeiraLinha) {
                        primeiraLinha = false;
                        continue;
                    }

                    String isbn = linha.trim();
                    if (!isbn.isEmpty()) {
                        if (!isbn.matches("\\d{13}")) {
                            mensagensErro.add("ISBN inválido: " + isbn);
                            totalFalhas.incrementAndGet();
                            continue;
                        }

                        try {
                            Livro novo = OpenLibraryService.buscarLivroPorIsbn(isbn);
                            if (novo.getTitulo() != null) {
                                Livro existente = livroDAO.buscarPorIsbn(isbn);
                                if (existente != null) {
                                    existente.setTitulo(novo.getTitulo());
                                    existente.setAutores(novo.getAutores());
                                    existente.setEditora(novo.getEditora());
                                    existente.setDataPublicacao(novo.getDataPublicacao());
                                    existente.setLivrosSemelhantes(novo.getLivrosSemelhantes());
                                    livroDAO.salvar(existente);
                                } else {
                                    livroDAO.salvar(novo);
                                }
                                totalImportados.incrementAndGet();
                            } else {
                                mensagensErro.add("Livro não encontrado: " + isbn);
                                totalFalhas.incrementAndGet();
                            }
                        } catch (Exception e) {
                            mensagensErro.add("Erro ISBN " + isbn + ": " + e.getMessage());
                            totalFalhas.incrementAndGet();
                        }
                    }
                }
            } catch (IOException e) {
                mensagensErro.add("Erro ao ler o arquivo: " + e.getMessage());
            }

            SwingUtilities.invokeLater(() -> {
                loadingDialog.dispose();
                carregarTabela();

                StringBuilder msg = new StringBuilder();
                msg.append("Importação concluída!\nImportados: ")
                        .append(totalImportados.get()).append("\nFalhas: ")
                        .append(totalFalhas.get());

                if (!mensagensErro.isEmpty()) {
                    msg.append("\n\nDetalhes das falhas:\n");
                    for (String erro : mensagensErro) {
                        msg.append("- ").append(erro).append("\n");
                    }
                }

                JTextArea area = new JTextArea(msg.toString());
                area.setEditable(false);
                area.setLineWrap(true);
                JScrollPane scroll = new JScrollPane(area);
                scroll.setPreferredSize(new Dimension(500, 300));

                JOptionPane.showMessageDialog(this, scroll, "Resumo da Importação", JOptionPane.INFORMATION_MESSAGE);
            });
        }).start();

        loadingDialog.setVisible(true);
    }
}
