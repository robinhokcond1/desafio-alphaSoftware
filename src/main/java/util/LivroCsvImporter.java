package util;

import dao.LivroDAO;
import model.Livro;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class LivroCsvImporter {

    private final LivroDAO livroDAO;

    public LivroCsvImporter(LivroDAO livroDAO) {
        this.livroDAO = livroDAO;
    }

    public void importar(File arquivo, Runnable onSuccess, JFrame parent) {
        int totalImportados = 0;
        int totalFalhas = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] campos = linha.split(",", -1);
                if (campos.length >= 6) {
                    try {
                        String isbn = campos[2].trim();
                        Livro livro = livroDAO.buscarPorIsbn(isbn);
                        if (livro == null) {
                            livro = new Livro();
                            livro.setIsbn(isbn);
                        }

                        livro.setTitulo(campos[0].trim());
                        livro.setAutores(campos[1].trim());
                        livro.setEditora(campos[3].trim());
                        if (!campos[4].isEmpty()) {
                            livro.setDataPublicacao(LocalDate.parse(campos[4].trim()));
                        }
                        livro.setLivrosSemelhantes(campos[5].trim());

                        livroDAO.salvar(livro);
                        totalImportados++;
                    } catch (Exception ex) {
                        totalFalhas++;
                        System.err.println("Erro ao importar linha: " + linha);
                    }
                }
            }

            if (onSuccess != null) onSuccess.run();

            JOptionPane.showMessageDialog(parent,
                    "Importação concluída!\nImportados: " + totalImportados + "\nFalhas: " + totalFalhas);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}
