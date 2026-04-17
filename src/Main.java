import javax.swing.*;

/**
 * Ponto de entrada do sistema de Ranking de Jogadores.
 * Inicializa a interface gráfica Swing.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RankingGUI gui = new RankingGUI();
            gui.setVisible(true);
        });
    }
}