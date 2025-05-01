package dao;

import model.Livro;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LivroDAOTest {

    private static SessionFactory sessionFactory;
    private LivroDAO livroDAO;

    @BeforeAll
    static void setupOnce() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
    }

    @BeforeEach
    void setup() {
        livroDAO = new LivroDAO();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM Livro").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @AfterAll
    static void tearDownOnce() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void deveSalvarELerLivro() {
        Livro livro = criarLivroTeste("9780132350884");
        livroDAO.salvar(livro);

        Livro recuperado = livroDAO.buscarPorIsbn("9780132350884");
        assertNotNull(recuperado);
        assertEquals("Clean Code", recuperado.getTitulo());
    }

    @Test
    void deveBuscarPorId() {
        Livro livro = criarLivroTeste("9780134685991");
        livroDAO.salvar(livro);
        Livro recuperado = livroDAO.buscarPorIsbn("9780134685991");
        assertNotNull(recuperado);

        Livro porId = livroDAO.buscarPorId(recuperado.getId());
        assertEquals("Effective Java", porId.getTitulo());
    }

    @Test
    void deveListarTodos() {
        livroDAO.salvar(criarLivroTeste("1111111111111"));
        livroDAO.salvar(criarLivroTeste("2222222222222"));

        List<Livro> lista = livroDAO.listarTodos();
        assertTrue(lista.size() >= 2);
    }

    @Test
    void deveExcluirLivro() {
        Livro livro = criarLivroTeste("3333333333333");
        livroDAO.salvar(livro);

        Livro salvo = livroDAO.buscarPorIsbn("3333333333333");
        assertNotNull(salvo);

        livroDAO.excluir(salvo);

        Livro apagado = livroDAO.buscarPorIsbn("3333333333333");
        assertNull(apagado);
    }

    private Livro criarLivroTeste(String isbn) {
        Livro livro = new Livro();
        livro.setTitulo(isbn.equals("9780134685991") ? "Effective Java" : "Clean Code");
        livro.setAutores("Autor Teste");
        livro.setIsbn(isbn);
        livro.setEditora("Editora Teste");
        livro.setDataPublicacao(LocalDate.of(2023, 1, 1));
        livro.setLivrosSemelhantes("Outro Livro");
        return livro;
    }
}