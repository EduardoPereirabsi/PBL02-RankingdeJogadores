import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PainelArvore extends JPanel {
    private ArvoreBinaria arvore;
    private int rankingDestacado = -1;
    private No[] nosArray = new No[0];
    private Point[] pontosArray = new Point[0];
    private int posCount = 0;

    private static final int LARGURA_NO = 92;
    private static final int ALTURA_NO = 42;
    private static final int ESPACO_H = 10;
    private static final int ESPACO_V = 55;
    private static final int MARGEM_TOPO = 40;
    private static final int MARGEM_ESQ = 20;

    private static final Color COR_TOP10 = new Color(220, 50, 50);
    private static final Color COR_TOP25 = new Color(230, 140, 20);
    private static final Color COR_TOP50 = new Color(40, 140, 210);
    private static final Color COR_TOP75 = new Color(40, 180, 70);
    private static final Color COR_RESTO = new Color(110, 110, 130);
    private static final Color COR_DESTAQUE = new Color(255, 215, 0);
    private static final Color COR_FUNDO = new Color(30, 30, 42);
    private static final Color COR_LINHA = new Color(80, 85, 110);

    public PainelArvore(ArvoreBinaria arvore) {
        this.arvore = arvore;
        setBackground(COR_FUNDO);
    }

    public void destacar(int ranking) {
        rankingDestacado = ranking;
        revalidate();
        repaint();
    }

    public void limparDestaque() {
        rankingDestacado = -1;
        revalidate();
        repaint();
    }

    public void atualizar() {
        revalidate();
        repaint();
    }

    private Point getPosicao(No node) {
        for (int i = 0; i < posCount; i++) {
            if (nosArray[i] == node) return pontosArray[i];
        }
        return null;
    }

    private void calcularPosicoes() {
        int tam = arvore.getTamanho();
        nosArray = new No[tam];
        pontosArray = new Point[tam];
        posCount = 0;
        if (arvore.getRoot() == null) return;
        int[] contador = {0};
        percorrerEmOrdem(arvore.getRoot(), 0, contador);
    }

    private void percorrerEmOrdem(No node, int profundidade, int[] contador) {
        if (node == null) return;
        percorrerEmOrdem(node.left, profundidade + 1, contador);

        int x = MARGEM_ESQ + contador[0] * (LARGURA_NO + ESPACO_H) + LARGURA_NO / 2;
        int y = MARGEM_TOPO + profundidade * (ALTURA_NO + ESPACO_V);
        nosArray[posCount] = node;
        pontosArray[posCount] = new Point(x, y);
        posCount++;
        contador[0]++;

        percorrerEmOrdem(node.right, profundidade + 1, contador);
    }

    @Override
    public Dimension getPreferredSize() {
        calcularPosicoes();
        if (posCount == 0) {
            return new Dimension(800, 500);
        }
        int maxX = 0;
        int maxY = 0;
        for (int i = 0; i < posCount; i++) {
            Point p = pontosArray[i];
            if (p.x + LARGURA_NO / 2 + MARGEM_ESQ > maxX)
                maxX = p.x + LARGURA_NO / 2 + MARGEM_ESQ;
            if (p.y + ALTURA_NO + 60 > maxY)
                maxY = p.y + ALTURA_NO + 60;
        }
        return new Dimension(Math.max(800, maxX), Math.max(500, maxY + 50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        calcularPosicoes();

        if (posCount == 0) {
            g2.setColor(new Color(150, 150, 180));
            g2.setFont(new Font("SansSerif", Font.ITALIC, 16));
            g2.drawString("Arvore vazia - insira jogadores ou carregue o CSV", 200, 250);
            return;
        }

        desenharLinhas(g2, arvore.getRoot());

        for (int i = 0; i < posCount; i++) {
            desenharNo(g2, nosArray[i], pontosArray[i]);
        }

        desenharLegenda(g2);
    }

    private void desenharLinhas(Graphics2D g2, No node) {
        if (node == null) return;
        Point posicaoPai = getPosicao(node);
        if (posicaoPai == null) return;

        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        if (node.left != null) {
            Point posFilho = getPosicao(node.left);
            if (posFilho != null) {
                g2.setColor(COR_LINHA);
                g2.drawLine(posicaoPai.x, posicaoPai.y + ALTURA_NO, posFilho.x, posFilho.y);
            }
            desenharLinhas(g2, node.left);
        }

        if (node.right != null) {
            Point posFilho = getPosicao(node.right);
            if (posFilho != null) {
                g2.setColor(COR_LINHA);
                g2.drawLine(posicaoPai.x, posicaoPai.y + ALTURA_NO, posFilho.x, posFilho.y);
            }
            desenharLinhas(g2, node.right);
        }
    }

    private void desenharNo(Graphics2D g2, No node, Point pos) {
        boolean destacado = node.jogador.getRanking() == rankingDestacado;
        Color corNo = pegarCorDoNo(node.jogador.getRanking(), destacado);

        int x = pos.x - LARGURA_NO / 2;
        int y = pos.y;

        g2.setColor(new Color(0, 0, 0, 50));
        g2.fill(new RoundRectangle2D.Float(x + 3, y + 3, LARGURA_NO, ALTURA_NO, 14, 14));

        RoundRectangle2D retangulo = new RoundRectangle2D.Float(x, y, LARGURA_NO, ALTURA_NO, 14, 14);
        g2.setColor(corNo);
        g2.fill(retangulo);

        if (destacado) {
            g2.setColor(new Color(200, 170, 0));
            g2.setStroke(new BasicStroke(3f));
        } else {
            g2.setColor(new Color(255, 255, 255, 40));
            g2.setStroke(new BasicStroke(1f));
        }
        g2.draw(retangulo);

        g2.setColor(destacado ? Color.BLACK : Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        String textoRank = "#" + node.jogador.getRanking();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(textoRank, pos.x - fm.stringWidth(textoRank) / 2, y + 16);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
        fm = g2.getFontMetrics();
        String nickname = node.jogador.getNickname();
        if (fm.stringWidth(nickname) > LARGURA_NO - 8) {
            while (nickname.length() > 3 && fm.stringWidth(nickname + "..") > LARGURA_NO - 8) {
                nickname = nickname.substring(0, nickname.length() - 1);
            }
            nickname = nickname + "..";
        }
        g2.drawString(nickname, pos.x - fm.stringWidth(nickname) / 2, y + 32);
    }

    private Color pegarCorDoNo(int ranking, boolean destacado) {
        if (destacado) return COR_DESTAQUE;
        if (ranking <= 10) return COR_TOP10;
        if (ranking <= 25) return COR_TOP25;
        if (ranking <= 50) return COR_TOP50;
        if (ranking <= 75) return COR_TOP75;
        return COR_RESTO;
    }

    private void desenharLegenda(Graphics2D g2) {
        int legendaY = getPreferredSize().height - 35;
        int x = 20;

        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.setColor(new Color(150, 150, 170));
        g2.drawString("Legenda:", x, legendaY + 12);
        x += 62;

        Color[] cores = {COR_TOP10, COR_TOP25, COR_TOP50, COR_TOP75, COR_RESTO};
        String[] nomes = {"Top 10", "Top 25", "Top 50", "Top 75", "76-100"};

        for (int i = 0; i < cores.length; i++) {
            g2.setColor(cores[i]);
            g2.fillRoundRect(x, legendaY, 14, 14, 4, 4);
            g2.setColor(new Color(180, 180, 200));
            g2.drawString(nomes[i], x + 18, legendaY + 12);
            x += 78;
        }
    }
}
