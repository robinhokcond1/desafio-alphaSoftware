package view;

import dao.LivroDAO;
import model.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CadastroLivroDialogTest {

    private LivroDAO livroDAOMock;
    private CadastroLivroDialog dialog;

    @BeforeEach
    void setup() {
        livroDAOMock = mock(LivroDAO.class);

        JFrame dummyFrame = new JFrame();
        Livro livro = new Livro();
        dialog = new CadastroLivroDialog(dummyFrame, livro, () -> {});

        dialog.getTxtTitulo().setText("Java em Ação");
        dialog.getTxtAutores().setText("Robson Ramos");
        dialog.getTxtIsbn().setText("9780134685991");
        dialog.getTxtEditora().setText("Casa do Código");
        dialog.getTxtDataPublicacao().setText("2023-01-01");
        dialog.getTxtLivrosSemelhantes().setText("Effective Java");

        dialog.setLivroDAO(livroDAOMock);
    }

    @Test
    void deveSalvarLivroComCamposValidos() {
        dialog.salvar();

        verify(livroDAOMock, times(1)).salvar(any(Livro.class));
    }

    @Test
    void naoDeveSalvarLivroComDataInvalida() {
        dialog.getTxtDataPublicacao().setText("data-errada");

        dialog.salvar();

        verify(livroDAOMock, never()).salvar(any());
    }

    @Test
    void deveManterDadosNoObjetoLivro() {
        Livro livro = new Livro();
        dialog = new CadastroLivroDialog(new JFrame(), livro, null);

        dialog.getTxtTitulo().setText("Clean Code");
        dialog.getTxtAutores().setText("Robert Martin");
        dialog.getTxtIsbn().setText("9780132350884");
        dialog.getTxtEditora().setText("Prentice Hall");
        dialog.getTxtDataPublicacao().setText("2008-08-01");
        dialog.getTxtLivrosSemelhantes().setText("Effective Java");

        dialog.setLivroDAO(livroDAOMock);
        dialog.salvar();

        assertEquals("Clean Code", livro.getTitulo());
        assertEquals(LocalDate.of(2008, 8, 1), livro.getDataPublicacao());
    }
}