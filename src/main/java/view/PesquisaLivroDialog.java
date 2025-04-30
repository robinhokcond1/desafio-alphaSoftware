package view;

import dao.LivroDAO;
import model.CampoBusca;
import model.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PesquisaLivroDialog extends JDialog {

    private JComboBox<CampoBusca> campoCombo;
    private JTextField valorField;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private LivroDAO livroDAO;

    public PesquisaLivroDialog(JFrame parent) {
        super(parent, "Pesquisar Livros", true);
        this.livroDAO = new LivroDAO();

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JPanel topo = new JPanel(new FlowLayout());

        campoCombo = new JComboBox<>(CampoBusca.values());
        valorField = new JTextField(20);
        JButton buscarBtn = new JButton("Buscar");

        buscarBtn.addActionListener(e -> pesquisar());

        topo.add(new JLabel("Campo:"));
        topo.add(campoCombo);
        topo.add(new JLabel("Valor:"));
        topo.add(valorField);
        topo.add(buscarBtn);

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Título", "Autores", "ISBN"}, 0);
        tabela = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabela);

        add(topo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void pesquisar() {
        CampoBusca campo = (CampoBusca) campoCombo.getSelectedItem();
        String valor = valorField.getText().trim();

        modeloTabela.setRowCount(0); // limpa a tabela

        List<Livro> resultados = livroDAO.buscarPorCampo(campo, valor);
        for (Livro livro : resultados) {
            modeloTabela.addRow(new Object[]{
                    livro.getId(),
                    livro.getTitulo(),
                    livro.getAutores(),
                    livro.getIsbn()
            });
        }
    }
}
