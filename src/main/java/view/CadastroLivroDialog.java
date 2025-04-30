package view;

import dao.LivroDAO;
import model.Livro;

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

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initComponents();
        carregarDados();
    }

    private void initComponents() {
        JPanel painel = new JPanel(new GridLayout(7, 2, 5, 5));

        painel.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        painel.add(txtTitulo);

        painel.add(new JLabel("Autores:"));
        txtAutores = new JTextField();
        painel.add(txtAutores);

        painel.add(new JLabel("ISBN:"));
        txtIsbn = new JTextField();
        painel.add(txtIsbn);

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
