package model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autores;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    private String isbn;
    private String editora;

    @Column(name = "livros_semelhantes")
    private String livrosSemelhantes;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutores() { return autores; }

    public void setAutores(String autores) { this.autores = autores; }

    public LocalDate getDataPublicacao() { return dataPublicacao; }

    public void setDataPublicacao(LocalDate dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getEditora() { return editora; }

    public void setEditora(String editora) { this.editora = editora; }

    public String getLivrosSemelhantes() { return livrosSemelhantes; }

    public void setLivrosSemelhantes(String livrosSemelhantes) { this.livrosSemelhantes = livrosSemelhantes; }
}
