public class ArvoreBinaria {
    private No root;
    private int tamanho;

    public ArvoreBinaria() {
        root = null;
        tamanho = 0;
    }

    public boolean inserir(Jogador jogador) {
        if (buscar(jogador.getRanking()) != null) {
            return false;
        }
        root = inserirRec(root, jogador);
        tamanho++;
        return true;
    }

    private No inserirRec(No node, Jogador jogador) {
        if (node == null) {
            return new No(jogador);
        }
        if (jogador.getRanking() < node.jogador.getRanking()) {
            node.left = inserirRec(node.left, jogador);
        } else if (jogador.getRanking() > node.jogador.getRanking()) {
            node.right = inserirRec(node.right, jogador);
        }
        return node;
    }

    public boolean remover(int ranking) {
        if (buscar(ranking) == null) {
            return false;
        }
        root = removerRec(root, ranking);
        tamanho--;
        return true;
    }

    private No removerRec(No node, int ranking) {
        if (node == null) return null;

        if (ranking < node.jogador.getRanking()) {
            node.left = removerRec(node.left, ranking);
        } else if (ranking > node.jogador.getRanking()) {
            node.right = removerRec(node.right, ranking);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            No sucessor = acharMenorNo(node.right);
            node.jogador = sucessor.jogador;
            node.right = removerRec(node.right, sucessor.jogador.getRanking());
        }
        return node;
    }

    private No acharMenorNo(No node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public Jogador buscar(int ranking) {
        No resultado = buscarRec(root, ranking);
        if (resultado != null) {
            return resultado.jogador;
        }
        return null;
    }

    private No buscarRec(No node, int ranking) {
        if (node == null) return null;
        if (ranking == node.jogador.getRanking()) return node;
        if (ranking < node.jogador.getRanking()) return buscarRec(node.left, ranking);
        return buscarRec(node.right, ranking);
    }

    public Jogador buscarPorNickname(String nickname) {
        return buscarPorNicknameRec(root, nickname);
    }

    private Jogador buscarPorNicknameRec(No node, String nickname) {
        if (node == null) return null;
        if (node.jogador.getNickname().equalsIgnoreCase(nickname)) {
            return node.jogador;
        }
        Jogador achado = buscarPorNicknameRec(node.left, nickname);
        if (achado != null) return achado;
        return buscarPorNicknameRec(node.right, nickname);
    }

    public Jogador[] getCaminhoBusca(int ranking) {
        Jogador[] temp = new Jogador[getHeight()];
        int[] idx = {0};
        percorrerCaminho(root, ranking, temp, idx);
        Jogador[] resultado = new Jogador[idx[0]];
        for (int i = 0; i < idx[0]; i++) resultado[i] = temp[i];
        return resultado;
    }

    private boolean percorrerCaminho(No node, int ranking, Jogador[] caminho, int[] idx) {
        if (node == null) return false;
        caminho[idx[0]++] = node.jogador;
        if (ranking == node.jogador.getRanking()) return true;
        if (ranking < node.jogador.getRanking()) return percorrerCaminho(node.left, ranking, caminho, idx);
        return percorrerCaminho(node.right, ranking, caminho, idx);
    }

    public Jogador[] emOrdem() {
        Jogador[] resultado = new Jogador[tamanho];
        int[] idx = {0};
        emOrdemRec(root, resultado, idx);
        return resultado;
    }

    private void emOrdemRec(No node, Jogador[] resultado, int[] idx) {
        if (node == null) return;
        emOrdemRec(node.left, resultado, idx);
        resultado[idx[0]++] = node.jogador;
        emOrdemRec(node.right, resultado, idx);
    }

    public Jogador[] preOrdem() {
        Jogador[] resultado = new Jogador[tamanho];
        int[] idx = {0};
        preOrdemRec(root, resultado, idx);
        return resultado;
    }

    private void preOrdemRec(No node, Jogador[] resultado, int[] idx) {
        if (node == null) return;
        resultado[idx[0]++] = node.jogador;
        preOrdemRec(node.left, resultado, idx);
        preOrdemRec(node.right, resultado, idx);
    }

    public Jogador[] posOrdem() {
        Jogador[] resultado = new Jogador[tamanho];
        int[] idx = {0};
        posOrdemRec(root, resultado, idx);
        return resultado;
    }

    private void posOrdemRec(No node, Jogador[] resultado, int[] idx) {
        if (node == null) return;
        posOrdemRec(node.left, resultado, idx);
        posOrdemRec(node.right, resultado, idx);
        resultado[idx[0]++] = node.jogador;
    }

    public Jogador[] emNivel() {
        Jogador[] resultado = new Jogador[tamanho];
        if (root == null) return resultado;

        No[] fila = new No[tamanho];
        int inicio = 0, fim = 0;
        fila[fim++] = root;
        int idx = 0;

        while (inicio < fim) {
            No atual = fila[inicio++];
            resultado[idx++] = atual.jogador;
            if (atual.left != null) fila[fim++] = atual.left;
            if (atual.right != null) fila[fim++] = atual.right;
        }
        return resultado;
    }

    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(No node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    public int getTamanho() {
        return tamanho;
    }

    public Jogador getMelhor() {
        if (root == null) return null;
        return acharMenorNo(root).jogador;
    }

    public Jogador getPior() {
        if (root == null) return null;
        No node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.jogador;
    }

    public No getRoot() {
        return root;
    }

    public void limpar() {
        root = null;
        tamanho = 0;
    }
}
