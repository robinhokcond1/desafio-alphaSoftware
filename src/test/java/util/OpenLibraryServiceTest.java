package util;

import model.Livro;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenLibraryServiceTest {

    @Test
    void deveBuscarLivroValidoPorIsbn() {
        String isbn = "9780134685991"; // Effective Java

        Livro livro = OpenLibraryService.buscarLivroPorIsbn(isbn);

        assertNotNull(livro);
        assertEquals(isbn, livro.getIsbn());
        assertNotNull(livro.getTitulo());
        System.out.println("Livro encontrado: " + livro.getTitulo());

        if (livro.getAutores() != null) {
            System.out.println("Autor: " + livro.getAutores());
        } else {
            System.out.println("Autor não disponível na resposta da OpenLibrary.");
        }
    }

    @Test
    void deveRetornarLivroVazioParaIsbnInvalido() {
        String isbn = "0000000000000";
        Livro livro = OpenLibraryService.buscarLivroPorIsbn(isbn);

        assertNotNull(livro);
        assertEquals(isbn, livro.getIsbn());
        assertNull(livro.getTitulo());
    }

    @Test
    void deveTratarErroDeConexao() {
        // Simular ISBN inválido com caracteres ou erro proposital
        String isbn = "invalid-isbn";
        Livro livro = OpenLibraryService.buscarLivroPorIsbn(isbn);

        assertNotNull(livro);
        assertEquals(isbn, livro.getIsbn());
        assertNull(livro.getTitulo());
    }
}
