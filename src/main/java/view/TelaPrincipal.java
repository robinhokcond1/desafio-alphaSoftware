package view;

import dao.LivroDAO;
import model.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

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
                return false; // impede edição direta na tabela
            }
        };

        tabela = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabela);

        JPanel painelBotoes = new JPanel();
        JButton btnIncluir = new JButton("Incluir");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnPesquisar = new JButton("Pesquisar");

        // Adicionar ações
        btnIncluir.addActionListener(e -> incluirLivro());
        btnEditar.addActionListener(e -> editarLivro());
        btnExcluir.addActionListener(e -> excluirLivro());
        btnPesquisar.addActionListener(e -> pesquisarLivro());

        painelBotoes.add(btnIncluir);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnPesquisar);

        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0); // limpa tabela antes de recarregar
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
        JOptionPane.showMessageDialog(this, "Funcionalidade de pesquisa ainda não implementada.");
        // Depois podemos abrir uma caixa de diálogo para buscar por título, autor ou ISBN
    }
}
