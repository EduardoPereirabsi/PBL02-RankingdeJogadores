import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Painel personalizado que desenha a Árvore Binária de Busca graficamente.
 * Usa posicionamento baseado no percurso In-Order para evitar sobreposição de nós.
 * Nós são coloridos de acordo com a faixa de ranking.
 */
public class TreePanel extends JPanel {
    private final BinarySearchTree bst;
    private int highlightRanking = -1;
    private final Map<Node, Point> positions = new HashMap<>();

    private static final int NODE_W = 92;
    private static final int NODE_H = 42;
    private static final int H_GAP = 10;
    private static final int V_GAP = 55;
    private static final int TOP_MARGIN = 40;
    private static final int LEFT_MARGIN = 20;

    // Cores por faixa de ranking
    private static final Color TOP_10_COLOR  = new Color(220, 50, 50);
    private static final Color TOP_25_COLOR  = new Color(230, 140, 20);
    private static final Color TOP_50_COLOR  = new Color(40, 140, 210);
    private static final Color TOP_75_COLOR  = new Color(40, 180, 70);
    private static final Color REST_COLOR    = new Color(110, 110, 130);
    private static final Color HIGHLIGHT_CLR = new Color(255, 215, 0);
    private static final Color BG_COLOR      = new Color(30, 30, 42);
    private static final Color LINE_COLOR    = new Color(80, 85, 110);

    public TreePanel(BinarySearchTree bst) {
        this.bst = bst;
        setBackground(BG_COLOR);
    }

    /** Destaca o nó com o ranking informado. */
    public void setHighlight(int ranking) {
        highlightRanking = ranking;
        revalidate();
        repaint();
    }

    /** Remove o destaque. */
    public void clearHighlight() {
        highlightRanking = -1;
        revalidate();
        repaint();
    }

    /** Atualiza a visualização da árvore. */
    public void refresh() {
        revalidate();
        repaint();
    }

    // Calcula posição (x, y) de cada nó usando percurso In-Order
    private void computePositions() {
        positions.clear();
        if (bst.getRoot() == null) return;
        int[] counter = {0};
        inOrderPositions(bst.getRoot(), 0, counter);
    }

    private void inOrderPositions(Node node, int depth, int[] counter) {
        if (node == null) return;
        inOrderPositions(node.left, depth + 1, counter);
        int x = LEFT_MARGIN + counter[0] * (NODE_W + H_GAP) + NODE_W / 2;
        int y = TOP_MARGIN + depth * (NODE_H + V_GAP);
        positions.put(node, new Point(x, y));
        counter[0]++;
        inOrderPositions(node.right, depth + 1, counter);
    }

    @Override
    public Dimension getPreferredSize() {
        computePositions();
        if (positions.isEmpty()) return new Dimension(800, 500);
        int maxX = 0, maxY = 0;
        for (Point p : positions.values()) {
            maxX = Math.max(maxX, p.x + NODE_W / 2 + LEFT_MARGIN);
            maxY = Math.max(maxY, p.y + NODE_H + 60);
        }
        return new Dimension(Math.max(800, maxX), Math.max(500, maxY + 50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        computePositions();

        if (positions.isEmpty()) {
            g2.setColor(new Color(150, 150, 180));
            g2.setFont(new Font("SansSerif", Font.ITALIC, 16));
            g2.drawString("Árvore vazia — insira jogadores ou carregue o CSV", 200, 250);
            return;
        }

        // Desenha arestas primeiro (ficam atrás dos nós)
        drawEdges(g2, bst.getRoot());

        // Desenha os nós por cima
        for (Map.Entry<Node, Point> entry : positions.entrySet()) {
            drawNode(g2, entry.getKey(), entry.getValue());
        }

        // Legenda de cores
        drawLegend(g2);
    }

    private void drawEdges(Graphics2D g2, Node node) {
        if (node == null) return;
        Point p = positions.get(node);
        if (p == null) return;

        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        if (node.left != null) {
            Point lp = positions.get(node.left);
            if (lp != null) {
                g2.setColor(LINE_COLOR);
                g2.drawLine(p.x, p.y + NODE_H, lp.x, lp.y);
            }
            drawEdges(g2, node.left);
        }
        if (node.right != null) {
            Point rp = positions.get(node.right);
            if (rp != null) {
                g2.setColor(LINE_COLOR);
                g2.drawLine(p.x, p.y + NODE_H, rp.x, rp.y);
            }
            drawEdges(g2, node.right);
        }
    }

    private void drawNode(Graphics2D g2, Node node, Point pos) {
        boolean hl = node.player.getRanking() == highlightRanking;
        Color fill = getNodeColor(node.player.getRanking(), hl);

        int x = pos.x - NODE_W / 2;
        int y = pos.y;

        // Sombra
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fill(new RoundRectangle2D.Float(x + 3, y + 3, NODE_W, NODE_H, 14, 14));

        // Corpo do nó
        RoundRectangle2D rect = new RoundRectangle2D.Float(x, y, NODE_W, NODE_H, 14, 14);
        g2.setColor(fill);
        g2.fill(rect);

        // Borda
        if (hl) {
            g2.setColor(new Color(200, 170, 0));
            g2.setStroke(new BasicStroke(3f));
        } else {
            g2.setColor(new Color(255, 255, 255, 40));
            g2.setStroke(new BasicStroke(1f));
        }
        g2.draw(rect);

        // Texto do ranking
        g2.setColor(hl ? Color.BLACK : Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        String rankStr = "#" + node.player.getRanking();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(rankStr, pos.x - fm.stringWidth(rankStr) / 2, y + 16);

        // Texto do nickname
        g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
        fm = g2.getFontMetrics();
        String nick = node.player.getNickname();
        if (fm.stringWidth(nick) > NODE_W - 8) {
            while (nick.length() > 3 && fm.stringWidth(nick + "..") > NODE_W - 8) {
                nick = nick.substring(0, nick.length() - 1);
            }
            nick += "..";
        }
        g2.drawString(nick, pos.x - fm.stringWidth(nick) / 2, y + 32);
    }

    private Color getNodeColor(int rank, boolean highlight) {
        if (highlight) return HIGHLIGHT_CLR;
        if (rank <= 10) return TOP_10_COLOR;
        if (rank <= 25) return TOP_25_COLOR;
        if (rank <= 50) return TOP_50_COLOR;
        if (rank <= 75) return TOP_75_COLOR;
        return REST_COLOR;
    }

    private void drawLegend(Graphics2D g2) {
        int legendY = getPreferredSize().height - 35;
        int x = 20;

        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.setColor(new Color(150, 150, 170));
        g2.drawString("Legenda:", x, legendY + 12);
        x += 62;

        Color[] colors = {TOP_10_COLOR, TOP_25_COLOR, TOP_50_COLOR, TOP_75_COLOR, REST_COLOR};
        String[] labels = {"Top 10", "Top 25", "Top 50", "Top 75", "76-100"};

        for (int i = 0; i < colors.length; i++) {
            g2.setColor(colors[i]);
            g2.fillRoundRect(x, legendY, 14, 14, 4, 4);
            g2.setColor(new Color(180, 180, 200));
            g2.drawString(labels[i], x + 18, legendY + 12);
            x += 78;
        }
    }
}
