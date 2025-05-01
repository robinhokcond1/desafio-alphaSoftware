package view;

import dao.LivroDAO;
import model.Livro;
import util.OpenLibraryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TelaPrincipal extends JFrame {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private LivroDAO livroDAO;

    public TelaPrincipal() {
        livroDAO = new LivroDAO();
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
        int totalImportados = 0;
        int totalFalhas = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] campos = linha.split(",", -1);
                if (campos.length >= 6) {
                    try {
                        String isbn = campos[2].trim();
                        Livro livro = livroDAO.buscarPorIsbn(isbn);
                        if (livro == null) {
                            livro = new Livro();
                            livro.setIsbn(isbn);
                        }

                        livro.setTitulo(campos[0].trim());
                        livro.setAutores(campos[1].trim());
                        livro.setEditora(campos[3].trim());
                        if (!campos[4].isEmpty()) {
                            livro.setDataPublicacao(LocalDate.parse(campos[4].trim()));
                        }
                        livro.setLivrosSemelhantes(campos[5].trim());

                        livroDAO.salvar(livro);
                        totalImportados++;
                    } catch (Exception ex) {
                        totalFalhas++;
                        System.err.println("Erro ao importar linha: " + linha);
                    }
                }
            }

            carregarTabela();
            JOptionPane.showMessageDialog(this,
                    "Importação concluída!\nImportados: " + totalImportados + "\nFalhas: " + totalFalhas);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    private void exportarCsv() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File arquivo = fileChooser.getSelectedFile();
        if (!arquivo.getName().toLowerCase().endsWith(".csv")) {
            arquivo = new File(arquivo.getAbsolutePath() + ".csv");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            writer.write("titulo,autores,isbn,editora,dataPublicacao,livrosSemelhantes");
            writer.newLine();

            for (Livro livro : livroDAO.listarTodos()) {
                writer.write(String.join(",",
                        tratarCsv(livro.getTitulo()),
                        tratarCsv(livro.getAutores()),
                        tratarCsv(livro.getIsbn()),
                        tratarCsv(livro.getEditora()),
                        livro.getDataPublicacao() != null ? livro.getDataPublicacao().toString() : "",
                        tratarCsv(livro.getLivrosSemelhantes())
                ));
                writer.newLine();
            }

            JOptionPane.showMessageDialog(this, "Exportação concluída com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao exportar CSV: " + e.getMessage());
        }
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
        loadingDialog.add(new JLabel("⏳ Importando livros da OpenLibrary, aguarde...", JLabel.CENTER), BorderLayout.CENTER);

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

    private void exportarXml() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File arquivo = fileChooser.getSelectedFile();
        if (!arquivo.getName().toLowerCase().endsWith(".xml")) {
            arquivo = new File(arquivo.getAbsolutePath() + ".xml");
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("livros");
            doc.appendChild(root);

            for (Livro livro : livroDAO.listarTodos()) {
                Element el = doc.createElement("livro");
                el.appendChild(criarElemento(doc, "titulo", livro.getTitulo()));
                el.appendChild(criarElemento(doc, "autores", livro.getAutores()));
                el.appendChild(criarElemento(doc, "isbn", livro.getIsbn()));
                el.appendChild(criarElemento(doc, "editora", livro.getEditora()));
                el.appendChild(criarElemento(doc, "dataPublicacao",
                        livro.getDataPublicacao() != null ? livro.getDataPublicacao().toString() : ""));
                el.appendChild(criarElemento(doc, "livrosSemelhantes", livro.getLivrosSemelhantes()));
                root.appendChild(el);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(arquivo));

            JOptionPane.showMessageDialog(this, "Exportação concluída com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao exportar XML: " + e.getMessage());
        }
    }

    private Element criarElemento(Document doc, String nome, String valor) {
        Element el = doc.createElement(nome);
        el.appendChild(doc.createTextNode(valor != null ? valor : ""));
        return el;
    }

    private String tratarCsv(String texto) {
        if (texto == null) return "";
        return texto.replace(",", " ").replaceAll("\\R", " ").trim();
    }
}
