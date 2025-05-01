package util;

import dao.LivroDAO;
import model.Livro;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class LivroXmlExporter {

    private final LivroDAO livroDAO;

    public LivroXmlExporter(LivroDAO livroDAO) {
        this.livroDAO = livroDAO;
    }

    public void exportar(File arquivo, JFrame parent) {
        if (!arquivo.getName().toLowerCase().endsWith(".xml")) {
            arquivo = new File(arquivo.getAbsolutePath() + ".xml");
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("livros");
            doc.appendChild(root);

            List<Livro> livros = livroDAO.listarTodos();
            for (Livro livro : livros) {
                Element el = doc.createElement("livro");
                el.appendChild(criarElemento(doc, "titulo", livro.getTitulo()));
                el.appendChild(criarElemento(doc, "autores", livro.getAutores()));
                el.appendChild(criarElemento(doc, "isbn", livro.getIsbn()));
                el.appendChild(criarElemento(doc, "editora", livro.getEditora()));
                el.appendChild(criarElemento(doc, "dataPublicacao",
                        livro.getDataPublicacao() != null ? livro.getDataPublicacao().toString() : ""));
                el.appendChild(criarElemento(doc, "livrosSemelhantes", livro.getLivrosSemelhantes()));
                root.appendChild(el);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(arquivo));

            JOptionPane.showMessageDialog(parent, "Exportação XML concluída com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Erro ao exportar XML: " + e.getMessage());
        }
    }

    private Element criarElemento(Document doc, String nome, String valor) {
        Element el = doc.createElement(nome);
        el.appendChild(doc.createTextNode(valor != null ? valor : ""));
        return el;
    }
}
