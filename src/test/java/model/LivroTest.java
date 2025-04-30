package model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LivroTest {

    @Test
    public void deveCriarLivroComTodosOsCampos() {
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Clean Code");
        livro.setAutores("Robert C. Martin");
        livro.setDataPublicacao(LocalDate.of(2008, 8, 1));
        livro.setIsbn("9780132350884");
        livro.setEditora("Prentice Hall");
        livro.setLivrosSemelhantes("Código Limpo;Java Efetivo");

        assertEquals(1L, livro.getId());
        assertEquals("Clean Code", livro.getTitulo());
        assertEquals("Robert C. Martin", livro.getAutores());
        assertEquals(LocalDate.of(2008, 8, 1), livro.getDataPublicacao());
        assertEquals("9780132350884", livro.getIsbn());
        assertEquals("Prentice Hall", livro.getEditora());
        assertEquals("Código Limpo;Java Efetivo", livro.getLivrosSemelhantes());
    }

    @Test
    public void devePermitirAlterarCampos() {
        Livro livro = new Livro();
        livro.setTitulo("Título Antigo");
        livro.setTitulo("Título Novo");

        assertEquals("Título Novo", livro.getTitulo());
    }

    @Test
    public void dataPublicacaoDeveSerLocalDate() {
        Livro livro = new Livro();
        LocalDate data = LocalDate.now();
        livro.setDataPublicacao(data);

        assertTrue(livro.getDataPublicacao() instanceof LocalDate);
    }
}
