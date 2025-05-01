package util;

import dao.LivroDAO;
import model.Livro;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LivroCsvExporter {

    private final LivroDAO livroDAO;

    public LivroCsvExporter(LivroDAO livroDAO) {
        this.livroDAO = livroDAO;
    }

    public void exportar(File arquivo, JFrame parent) {
        if (!arquivo.getName().toLowerCase().endsWith(".csv")) {
            arquivo = new File(arquivo.getAbsolutePath() + ".csv");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            writer.write("titulo,autores,isbn,editora,dataPublicacao,livrosSemelhantes");
            writer.newLine();

            List<Livro> livros = livroDAO.listarTodos();
            for (Livro livro : livros) {
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

            JOptionPane.showMessageDialog(parent, "Exportação concluída com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Erro ao exportar CSV: " + e.getMessage());
        }
    }

    private String tratarCsv(String texto) {
        if (texto == null) return "";
        return texto.replace(",", " ").replaceAll("\\R", " ").trim();
    }
}
