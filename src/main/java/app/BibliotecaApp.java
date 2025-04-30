package app;

import view.TelaPrincipal;

import javax.swing.*;

public class BibliotecaApp {

    public static void main(String[] args) {
        // Configura o look and feel para o sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Não foi possível aplicar o tema do sistema.");
        }

        // Inicia a interface gráfica
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}
