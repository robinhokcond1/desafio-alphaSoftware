package view;

import dao.LivroDAO;
import model.Livro;
import util.OpenLibraryService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class CadastroLivroDialog extends JDialog {

    private JTextField txtTitulo;
    private JTextField txtAutores;
    private JTextField txtIsbn;
    private JTextField txtEditora;
    private JTextField txtDataPublicacao;
    private JTextField txtLivrosSemelhantes;

    private Livro livro;
    private LivroDAO livroDAO;
    private Runnable onSaveCallback;

    public CadastroLivroDialog(JFrame parent, Livro livro, Runnable onSaveCallback) {
        super(parent, "Cadastro de Livro", true);
        this.livro = (livro != null) ? livro : new Livro();
        this.livroDAO = new LivroDAO();
        this.onSaveCallback = onSaveCallback;

        setSize(450, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initComponents();
        carregarDados();
    }

    private void initComponents() {
        JPanel painel = new JPanel(new GridLayout(9, 2, 5, 5));

        // 🔍 Instrução de uso
        JLabel lblInstrucao = new JLabel("🔍 Você pode buscar os dados preenchendo apenas o ISBN:");
        lblInstrucao.setForeground(Color.BLUE);
        lblInstrucao.setFont(lblInstrucao.getFont().deriveFont(Font.ITALIC, 12f));
        painel.add(lblInstrucao);
        painel.add(new JLabel()); // célula vazia para alinhamento

        painel.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        painel.add(txtTitulo);

        painel.add(new JLabel("Autores:"));
        txtAutores = new JTextField();
        painel.add(txtAutores);

        painel.add(new JLabel("ISBN:"));
        txtIsbn = new JTextField();
        painel.add(txtIsbn);

        JButton btnBuscarIsbn = new JButton("Buscar pelo ISBN");
        btnBuscarIsbn.addActionListener(e -> preencherViaIsbn());
        painel.add(new JLabel()); // célula vazia para alinhar
        painel.add(btnBuscarIsbn);

        painel.add(new JLabel("Editora:"));
        txtEditora = new JTextField();
        painel.add(txtEditora);

        painel.add(new JLabel("Data Publicação (AAAA-MM-DD):"));
        txtDataPublicacao = new JTextField();
        painel.add(txtDataPublicacao);

        painel.add(new JLabel("Livros Semelhantes:"));
        txtLivrosSemelhantes = new JTextField();
        painel.add(txtLivrosSemelhantes);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel botoes = new JPanel();
        botoes.add(btnSalvar);
        botoes.add(btnCancelar);

        add(painel, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);
    }

    private void carregarDados() {
        if (livro.getId() != null) {
            txtTitulo.setText(livro.getTitulo());
            txtAutores.setText(livro.getAutores());
            txtIsbn.setText(livro.getIsbn());
            txtEditora.setText(livro.getEditora());
            txtDataPublicacao.setText(
                    livro.getDataPublicacao() != null ? livro.getDataPublicacao().toString() : ""
            );
            txtLivrosSemelhantes.setText(livro.getLivrosSemelhantes());
        }
    }

    private void preencherViaIsbn() {
        String isbn = txtIsbn.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um ISBN antes de buscar.");
            return;
        }

        Livro resultado = OpenLibraryService.buscarLivroPorIsbn(isbn);

        if (resultado.getTitulo() != null) {
            txtTitulo.setText(resultado.getTitulo());
            txtAutores.setText(resultado.getAutores());
            txtEditora.setText(resultado.getEditora());
            txtDataPublicacao.setText(
                    resultado.getDataPublicacao() != null ? resultado.getDataPublicacao().toString() : ""
            );
            JOptionPane.showMessageDialog(this, "Dados preenchidos com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Livro não encontrado na OpenLibrary.");
        }
    }

    private void salvar() {
        try {
            livro.setTitulo(txtTitulo.getText());
            livro.setAutores(txtAutores.getText());
            livro.setIsbn(txtIsbn.getText());
            livro.setEditora(txtEditora.getText());

            String dataTexto = txtDataPublicacao.getText().trim();
            if (!dataTexto.isEmpty()) {
                livro.setDataPublicacao(LocalDate.parse(dataTexto));
            }

            livro.setLivrosSemelhantes(txtLivrosSemelhantes.getText());

            livroDAO.salvar(livro);
            JOptionPane.showMessageDialog(this, "Livro salvo com sucesso!");
            if (onSaveCallback != null) onSaveCallback.run();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar livro: " + ex.getMessage());
        }
    }
}
