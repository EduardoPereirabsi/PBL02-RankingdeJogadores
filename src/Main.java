import javax.swing.*;

// classe principal - inicia a interface grafica
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfaceRanking tela = new InterfaceRanking();
            tela.setVisible(true);
        });
    }
}