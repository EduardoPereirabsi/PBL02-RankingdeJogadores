import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;

public class RankingGUI extends JFrame {
    private final BinarySearchTree bst;
    private final TreePanel treePanel;
    private final JTextArea outputArea;
    private final JTextField nicknameField;
    private final JTextField rankingField;
    private final JLabel statsLabel;

    public RankingGUI() {
        bst = new BinarySearchTree();

        setTitle("Ranking de Jogadores \u2014 \u00c1rvore Bin\u00e1ria de Busca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(new EmptyBorder(8, 10, 4, 10));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        row1.add(new JLabel("Nickname:"));
        nicknameField = new JTextField(14);
        row1.add(nicknameField);
        row1.add(new JLabel("Ranking:"));
        rankingField = new JTextField(6);
        row1.add(rankingField);

        JButton insertBtn = createButton("Inserir", new Color(46, 125, 50));
        JButton removeBtn = createButton("Remover", new Color(198, 40, 40));
        row1.add(insertBtn);
        row1.add(removeBtn);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        JButton searchRankBtn = createButton("Buscar Ranking", new Color(21, 101, 192));
        JButton searchNameBtn = createButton("Buscar Nome", new Color(21, 101, 192));
        JButton loadCsvBtn   = createButton("Carregar CSV", new Color(56, 142, 60));
        JButton clearBtn     = createButton("Limpar \u00c1rvore", new Color(120, 120, 120));
        row2.add(searchRankBtn);
        row2.add(searchNameBtn);
        row2.add(loadCsvBtn);
        row2.add(clearBtn);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        JButton inOrderBtn    = createButton("In-Order", new Color(106, 27, 154));
        JButton preOrderBtn   = createButton("Pr\u00e9-Order", new Color(106, 27, 154));
        JButton postOrderBtn  = createButton("P\u00f3s-Order", new Color(106, 27, 154));
        JButton levelOrderBtn = createButton("Level-Order", new Color(106, 27, 154));
        statsLabel = new JLabel("N\u00f3s: 0 | Altura: 0");
        statsLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        row3.add(new JLabel("Percursos: "));
        row3.add(inOrderBtn);
        row3.add(preOrderBtn);
        row3.add(postOrderBtn);
        row3.add(levelOrderBtn);
        row3.add(Box.createHorizontalStrut(20));
        row3.add(statsLabel);

        topPanel.add(row1);
        topPanel.add(row2);
        topPanel.add(row3);
        add(topPanel, BorderLayout.NORTH);

        treePanel = new TreePanel(bst);
        JScrollPane treeScroll = new JScrollPane(treePanel);
        treeScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 200)),
                "Visualiza\u00e7\u00e3o da \u00c1rvore",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12)));
        treeScroll.getVerticalScrollBar().setUnitIncrement(16);
        treeScroll.getHorizontalScrollBar().setUnitIncrement(16);
        add(treeScroll, BorderLayout.CENTER);

        outputArea = new JTextArea(8, 80);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBackground(new Color(245, 245, 250));
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Sa\u00edda"));
        outputScroll.setPreferredSize(new Dimension(0, 180));
        add(outputScroll, BorderLayout.SOUTH);

        insertBtn.addActionListener(e -> doInsert());
        removeBtn.addActionListener(e -> doRemove());
        searchRankBtn.addActionListener(e -> doSearchByRanking());
        searchNameBtn.addActionListener(e -> doSearchByName());
        loadCsvBtn.addActionListener(e -> doLoadCSV());
        clearBtn.addActionListener(e -> doClear());
        inOrderBtn.addActionListener(e -> showTraversal("In-Order", bst.inOrder()));
        preOrderBtn.addActionListener(e -> showTraversal("Pr\u00e9-Order", bst.preOrder()));
        postOrderBtn.addActionListener(e -> showTraversal("P\u00f3s-Order", bst.postOrder()));
        levelOrderBtn.addActionListener(e -> showTraversal("Level-Order", bst.levelOrder()));

        rankingField.addActionListener(e -> doInsert());

        updateStats();
        log("[INFO] Sistema iniciado. Carregue o CSV ou insira jogadores manualmente.");
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void doInsert() {
        String nick = nicknameField.getText().trim();
        String rankStr = rankingField.getText().trim();
        if (nick.isEmpty() || rankStr.isEmpty()) {
            log("[AVISO] Preencha nickname e ranking.");
            return;
        }
        try {
            int rank = Integer.parseInt(rankStr);
            if (rank <= 0) {
                log("[AVISO] Ranking deve ser maior que 0.");
                return;
            }
            if (bst.search(rank) != null) {
                log("[AVISO] Ranking #" + rank + " j\u00e1 existe na \u00e1rvore.");
                return;
            }
            bst.insert(new Player(nick, rank));
            log("[OK] Inserido: " + nick + " (#" + rank + ")");
            nicknameField.setText("");
            rankingField.setText("");
            refreshTree();
        } catch (NumberFormatException ex) {
            log("[AVISO] Ranking inv\u00e1lido: " + rankStr);
        }
    }

    private void doRemove() {
        String rankStr = rankingField.getText().trim();
        if (rankStr.isEmpty()) {
            log("[AVISO] Informe o ranking para remover.");
            return;
        }
        try {
            int rank = Integer.parseInt(rankStr);
            Player p = bst.search(rank);
            if (bst.remove(rank)) {
                log("[OK] Removido: " + p);
                treePanel.clearHighlight();
                refreshTree();
            } else {
                log("[AVISO] Ranking #" + rank + " n\u00e3o encontrado.");
            }
        } catch (NumberFormatException ex) {
            log("[AVISO] Ranking inv\u00e1lido.");
        }
    }

    private void doSearchByRanking() {
        String rankStr = rankingField.getText().trim();
        if (rankStr.isEmpty()) {
            log("[AVISO] Informe o ranking para buscar.");
            return;
        }
        try {
            int rank = Integer.parseInt(rankStr);
            Player found = bst.search(rank);
            if (found != null) {
                log("[BUSCA] Encontrado: " + found);
                treePanel.setHighlight(rank);

                Player[] path = bst.getSearchPath(rank);
                StringBuilder sb = new StringBuilder("   Caminho: ");
                for (int i = 0; i < path.length; i++) {
                    if (i > 0) sb.append(" -> ");
                    sb.append(path[i]);
                }
                log(sb.toString());
            } else {
                log("[BUSCA] Ranking #" + rank + " n\u00e3o encontrado.");
                treePanel.clearHighlight();
            }
        } catch (NumberFormatException ex) {
            log("[AVISO] Ranking inv\u00e1lido.");
        }
    }

    private void doSearchByName() {
        String nick = nicknameField.getText().trim();
        if (nick.isEmpty()) {
            log("[AVISO] Informe o nickname para buscar.");
            return;
        }
        Player found = bst.searchByNickname(nick);
        if (found != null) {
            log("[BUSCA] Encontrado: " + found);
            treePanel.setHighlight(found.getRanking());
        } else {
            log("[BUSCA] '" + nick + "' n\u00e3o encontrado.");
            treePanel.clearHighlight();
        }
    }

    private void doLoadCSV() {
        JFileChooser fc = new JFileChooser(new File(".."));
        fc.setDialogTitle("Selecione o arquivo CSV de jogadores");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos CSV", "csv"));
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            int count = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String nick = parts[0].trim();
                    int rank = Integer.parseInt(parts[1].trim());
                    if (bst.search(rank) == null) {
                        bst.insert(new Player(nick, rank));
                        count++;
                    }
                }
            }
            log("[OK] Carregados " + count + " jogadores de " + file.getName());
            refreshTree();
        } catch (Exception ex) {
            log("[ERRO] Falha ao ler CSV: " + ex.getMessage());
        }
    }

    private void doClear() {
        bst.clear();
        treePanel.clearHighlight();
        refreshTree();
        log("[OK] \u00c1rvore limpa.");
    }

    private void showTraversal(String name, Player[] list) {
        if (list.length == 0) {
            log("[AVISO] \u00c1rvore vazia.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(name).append("] ").append(list.length).append(" jogadores:\n");
        for (int i = 0; i < list.length; i++) {
            sb.append("   ").append(String.format("%3d", i + 1)).append(". ").append(list[i]).append("\n");
        }
        log(sb.toString());
    }

    private void refreshTree() {
        treePanel.refresh();
        updateStats();
    }

    private void updateStats() {
        int n = bst.getSize();
        int h = bst.getHeight();
        Player min = bst.getMin();
        Player max = bst.getMax();
        statsLabel.setText(String.format(
                "N\u00f3s: %d | Altura: %d | Melhor: %s | Pior: %s",
                n, h,
                min != null ? min.toString() : "\u2014",
                max != null ? max.toString() : "\u2014"));
    }

    private void log(String msg) {
        outputArea.append(msg + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
}
