import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;

public class InterfaceRanking extends JFrame {
    private ArvoreBinaria arvore;
    private PainelArvore painelArvore;
    private JTextArea areaSaida;
    private JTextField campoNickname;
    private JTextField campoRanking;
    private JLabel labelEstatisticas;

    public InterfaceRanking() {
        arvore = new ArvoreBinaria();
        montarInterface();
    }

    private void montarInterface() {
        setTitle("Ranking de Jogadores - Arvore Binaria de Busca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        JPanel painelControles = new JPanel();
        painelControles.setLayout(new BoxLayout(painelControles, BoxLayout.Y_AXIS));
        painelControles.setBorder(new EmptyBorder(8, 10, 4, 10));

        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        linha1.add(new JLabel("Nickname:"));
        campoNickname = new JTextField(14);
        linha1.add(campoNickname);
        linha1.add(new JLabel("Ranking:"));
        campoRanking = new JTextField(6);
        linha1.add(campoRanking);

        JButton btnInserir = criarBotao("Inserir", new Color(46, 125, 50));
        JButton btnRemover = criarBotao("Remover", new Color(198, 40, 40));
        linha1.add(btnInserir);
        linha1.add(btnRemover);

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        JButton btnBuscarRank = criarBotao("Buscar Ranking", new Color(21, 101, 192));
        JButton btnBuscarNome = criarBotao("Buscar Nickname", new Color(21, 101, 192));
        JButton btnCarregarCsv = criarBotao("Carregar CSV", new Color(56, 142, 60));
        JButton btnLimpar = criarBotao("Limpar Arvore", new Color(120, 120, 120));
        linha2.add(btnBuscarRank);
        linha2.add(btnBuscarNome);
        linha2.add(btnCarregarCsv);
        linha2.add(btnLimpar);

        JPanel linha3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        JButton btnEmOrdem = criarBotao("Em Ordem", new Color(106, 27, 154));
        JButton btnPreOrdem = criarBotao("Pre-Ordem", new Color(106, 27, 154));
        JButton btnPosOrdem = criarBotao("Pos-Ordem", new Color(106, 27, 154));
        JButton btnEmNivel = criarBotao("Em Nivel", new Color(106, 27, 154));
        labelEstatisticas = new JLabel("Nos: 0 | Altura: 0");
        labelEstatisticas.setFont(new Font("SansSerif", Font.BOLD, 12));

        linha3.add(new JLabel("Percursos: "));
        linha3.add(btnEmOrdem);
        linha3.add(btnPreOrdem);
        linha3.add(btnPosOrdem);
        linha3.add(btnEmNivel);
        linha3.add(Box.createHorizontalStrut(20));
        linha3.add(labelEstatisticas);

        painelControles.add(linha1);
        painelControles.add(linha2);
        painelControles.add(linha3);
        add(painelControles, BorderLayout.NORTH);

        painelArvore = new PainelArvore(arvore);
        JScrollPane scrollArvore = new JScrollPane(painelArvore);
        scrollArvore.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 200)),
                "Visualizacao da Arvore",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12)));
        scrollArvore.getVerticalScrollBar().setUnitIncrement(16);
        scrollArvore.getHorizontalScrollBar().setUnitIncrement(16);
        add(scrollArvore, BorderLayout.CENTER);

        areaSaida = new JTextArea(8, 80);
        areaSaida.setEditable(false);
        areaSaida.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaSaida.setBackground(new Color(245, 245, 250));
        JScrollPane scrollSaida = new JScrollPane(areaSaida);
        scrollSaida.setBorder(BorderFactory.createTitledBorder("Saida"));
        scrollSaida.setPreferredSize(new Dimension(0, 180));
        add(scrollSaida, BorderLayout.SOUTH);

        btnInserir.addActionListener(e -> acaoInserir());
        btnRemover.addActionListener(e -> acaoRemover());
        btnBuscarRank.addActionListener(e -> acaoBuscarPorRanking());
        btnBuscarNome.addActionListener(e -> acaoBuscarPorNickname());
        btnCarregarCsv.addActionListener(e -> acaoCarregarCSV());
        btnLimpar.addActionListener(e -> acaoLimpar());
        btnEmOrdem.addActionListener(e -> mostrarPercurso("Em Ordem", arvore.emOrdem()));
        btnPreOrdem.addActionListener(e -> mostrarPercurso("Pre-Ordem", arvore.preOrdem()));
        btnPosOrdem.addActionListener(e -> mostrarPercurso("Pos-Ordem", arvore.posOrdem()));
        btnEmNivel.addActionListener(e -> mostrarPercurso("Em Nivel", arvore.emNivel()));

        campoRanking.addActionListener(e -> acaoInserir());

        atualizarEstatisticas();
        escreverSaida("Sistema iniciado. Carregue o CSV ou insira jogadores manualmente.");
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void acaoInserir() {
        String nickname = campoNickname.getText().trim();
        String rankStr = campoRanking.getText().trim();

        if (nickname.isEmpty() || rankStr.isEmpty()) {
            escreverSaida("[AVISO] Preencha o nickname e o ranking.");
            return;
        }

        try {
            int rank = Integer.parseInt(rankStr);
            if (rank <= 0) {
                escreverSaida("[AVISO] Ranking deve ser maior que 0.");
                return;
            }
            if (arvore.buscar(rank) != null) {
                escreverSaida("[AVISO] Ranking #" + rank + " ja existe na arvore.");
                return;
            }

            arvore.inserir(new Jogador(nickname, rank));
            escreverSaida("[OK] Inserido: " + nickname + " (#" + rank + ")");
            campoNickname.setText("");
            campoRanking.setText("");
            atualizarTela();

        } catch (NumberFormatException ex) {
            escreverSaida("[AVISO] Ranking invalido: " + rankStr);
        }
    }

    private void acaoRemover() {
        String rankStr = campoRanking.getText().trim();
        if (rankStr.isEmpty()) {
            escreverSaida("[AVISO] Informe o ranking para remover.");
            return;
        }

        try {
            int rank = Integer.parseInt(rankStr);
            Jogador jogador = arvore.buscar(rank);

            if (arvore.remover(rank)) {
                escreverSaida("[OK] Removido: " + jogador);
                painelArvore.limparDestaque();
                atualizarTela();
            } else {
                escreverSaida("[AVISO] Ranking #" + rank + " nao encontrado.");
            }

        } catch (NumberFormatException ex) {
            escreverSaida("[AVISO] Ranking invalido.");
        }
    }

    private void acaoBuscarPorRanking() {
        String rankStr = campoRanking.getText().trim();
        if (rankStr.isEmpty()) {
            escreverSaida("[AVISO] Informe o ranking para buscar.");
            return;
        }

        try {
            int rank = Integer.parseInt(rankStr);
            Jogador encontrado = arvore.buscar(rank);

            if (encontrado != null) {
                escreverSaida("[BUSCA] Encontrado: " + encontrado);
                painelArvore.destacar(rank);

                Jogador[] caminho = arvore.getCaminhoBusca(rank);
                StringBuilder sb = new StringBuilder("   Caminho: ");
                for (int i = 0; i < caminho.length; i++) {
                    if (i > 0) sb.append(" -> ");
                    sb.append(caminho[i]);
                }
                escreverSaida(sb.toString());

            } else {
                escreverSaida("[BUSCA] Ranking #" + rank + " nao encontrado.");
                painelArvore.limparDestaque();
            }

        } catch (NumberFormatException ex) {
            escreverSaida("[AVISO] Ranking invalido.");
        }
    }

    private void acaoBuscarPorNickname() {
        String nickname = campoNickname.getText().trim();
        if (nickname.isEmpty()) {
            escreverSaida("[AVISO] Informe o nickname para buscar.");
            return;
        }

        Jogador encontrado = arvore.buscarPorNickname(nickname);
        if (encontrado != null) {
            escreverSaida("[BUSCA] Encontrado: " + encontrado);
            painelArvore.destacar(encontrado.getRanking());
        } else {
            escreverSaida("[BUSCA] '" + nickname + "' nao encontrado.");
            painelArvore.limparDestaque();
        }
    }

    private void acaoCarregarCSV() {
        JFileChooser seletorArquivo = new JFileChooser(new File(".."));
        seletorArquivo.setDialogTitle("Selecione o arquivo CSV de jogadores");
        seletorArquivo.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter("Arquivos CSV", "csv"));

        if (seletorArquivo.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File arquivo = seletorArquivo.getSelectedFile();
        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha = leitor.readLine();
            int quantidade = 0;

            while ((linha = leitor.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] partes = linha.split(",");
                if (partes.length >= 2) {
                    String nickname = partes[0].trim();
                    int rank = Integer.parseInt(partes[1].trim());
                    if (arvore.buscar(rank) == null) {
                        arvore.inserir(new Jogador(nickname, rank));
                        quantidade++;
                    }
                }
            }

            escreverSaida("[OK] Carregados " + quantidade + " jogadores de " + arquivo.getName());
            atualizarTela();

        } catch (Exception ex) {
            escreverSaida("[ERRO] Falha ao ler CSV: " + ex.getMessage());
        }
    }

    private void acaoLimpar() {
        arvore.limpar();
        painelArvore.limparDestaque();
        atualizarTela();
        escreverSaida("[OK] Arvore limpa.");
    }

    private void mostrarPercurso(String nome, Jogador[] lista) {
        if (lista.length == 0) {
            escreverSaida("[AVISO] Arvore vazia.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[").append(nome).append("] ").append(lista.length).append(" jogadores:\n");
        for (int i = 0; i < lista.length; i++) {
            sb.append("   ").append(String.format("%3d", i + 1)).append(". ").append(lista[i]).append("\n");
        }
        escreverSaida(sb.toString());
    }

    private void atualizarTela() {
        painelArvore.atualizar();
        atualizarEstatisticas();
    }

    private void atualizarEstatisticas() {
        int nos = arvore.getTamanho();
        int altura = arvore.getHeight();
        Jogador melhor = arvore.getMelhor();
        Jogador pior = arvore.getPior();

        String textoMelhor = melhor != null ? melhor.toString() : "--";
        String textoPior = pior != null ? pior.toString() : "--";

        labelEstatisticas.setText("Nos: " + nos + " | Altura: " + altura
                + " | Melhor: " + textoMelhor + " | Pior: " + textoPior);
    }

    private void escreverSaida(String mensagem) {
        areaSaida.append(mensagem + "\n");
        areaSaida.setCaretPosition(areaSaida.getDocument().getLength());
    }
}
