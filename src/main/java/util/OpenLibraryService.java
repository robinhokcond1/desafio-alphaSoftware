package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Livro;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

public class OpenLibraryService {

    private static final String BASE_URL = "https://openlibrary.org/isbn/";

    public static Livro buscarLivroPorIsbn(String isbn) {
        Livro livro = new Livro();
        livro.setIsbn(isbn);

        try {
            URL url = new URL("https://openlibrary.org/isbn/" + isbn + ".json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                return livro;
            }

            InputStream resposta = conn.getInputStream();
            String json = new Scanner(resposta).useDelimiter("\\A").next();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            if (root.has("title")) livro.setTitulo(root.get("title").asText());
            if (root.has("publishers") && root.get("publishers").isArray())
                livro.setEditora(root.get("publishers").get(0).asText());
            if (root.has("publish_date")) {
                String data = root.get("publish_date").asText();
                if (data.matches("\\d{4}")) {
                    livro.setDataPublicacao(LocalDate.of(Integer.parseInt(data), 1, 1));
                }
            }
            if (root.has("authors") && root.get("authors").isArray()) {
                JsonNode autorNode = root.get("authors").get(0);
                if (autorNode.has("key")) {
                    JsonNode autorJson = mapper.readTree(new URL("https://openlibrary.org" + autorNode.get("key").asText() + ".json"));
                    if (autorJson.has("name")) {
                        livro.setAutores(autorJson.get("name").asText());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao buscar livro por ISBN: https://openlibrary.org/isbn/" + isbn + ".json");
        }

        return livro;
    }
}
