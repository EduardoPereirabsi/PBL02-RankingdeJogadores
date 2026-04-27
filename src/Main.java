import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RankingGUI tela = new RankingGUI();
            tela.setVisible(true);
        });
    }
}