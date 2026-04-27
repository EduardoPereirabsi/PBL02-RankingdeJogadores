import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// Arvore Binaria de Busca para organizar os jogadores pelo ranking
// Adaptada a partir da MorseBST do TreeVisualizer.java
// Em vez de inserir por codigo morse, insere pelo ranking do jogador
// ranking menor vai pra esquerda (left), maior pra direita (right)
public class ArvoreBinaria {
    private No root;
    private int tamanho;

    public ArvoreBinaria() {
        root = null;
        tamanho = 0;
    }

    // --- INSERIR ---

    // insere um jogador na arvore, nao deixa ranking repetido
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

    // --- REMOVER ---

    // remove o jogador pelo ranking, retorna true se conseguiu
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
            // achou o no pra remover

            // se so tem um filho ou nenhum
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // se tem dois filhos pega o sucessor (menor da direita)
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

    // --- BUSCA ---

    // busca jogador pelo ranking
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

    // busca pelo nickname do jogador (percorre a arvore toda pq nao esta ordenado por nome)
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

    // retorna o caminho que a busca percorreu da raiz ate o no
    public List<Jogador> getCaminhoBusca(int ranking) {
        List<Jogador> caminho = new ArrayList<>();
        percorrerCaminho(root, ranking, caminho);
        return caminho;
    }

    private boolean percorrerCaminho(No node, int ranking, List<Jogador> caminho) {
        if (node == null) return false;
        caminho.add(node.jogador);
        if (ranking == node.jogador.getRanking()) return true;
        if (ranking < node.jogador.getRanking()) return percorrerCaminho(node.left, ranking, caminho);
        return percorrerCaminho(node.right, ranking, caminho);
    }

    // --- PERCURSOS ---

    // em ordem: left -> raiz -> right (mostra ranking ordenado crescente)
    public List<Jogador> emOrdem() {
        List<Jogador> lista = new ArrayList<>();
        emOrdemRec(root, lista);
        return lista;
    }

    private void emOrdemRec(No node, List<Jogador> lista) {
        if (node == null) return;
        emOrdemRec(node.left, lista);
        lista.add(node.jogador);
        emOrdemRec(node.right, lista);
    }

    // pre ordem: raiz -> left -> right
    public List<Jogador> preOrdem() {
        List<Jogador> lista = new ArrayList<>();
        preOrdemRec(root, lista);
        return lista;
    }

    private void preOrdemRec(No node, List<Jogador> lista) {
        if (node == null) return;
        lista.add(node.jogador);
        preOrdemRec(node.left, lista);
        preOrdemRec(node.right, lista);
    }

    // pos ordem: left -> right -> raiz
    public List<Jogador> posOrdem() {
        List<Jogador> lista = new ArrayList<>();
        posOrdemRec(root, lista);
        return lista;
    }

    private void posOrdemRec(No node, List<Jogador> lista) {
        if (node == null) return;
        posOrdemRec(node.left, lista);
        posOrdemRec(node.right, lista);
        lista.add(node.jogador);
    }

    // em nivel: percorre por nivel usando fila (BFS)
    public List<Jogador> emNivel() {
        List<Jogador> lista = new ArrayList<>();
        if (root == null) return lista;

        Queue<No> fila = new LinkedList<>();
        fila.add(root);

        while (!fila.isEmpty()) {
            No atual = fila.poll();
            lista.add(atual.jogador);
            if (atual.left != null) fila.add(atual.left);
            if (atual.right != null) fila.add(atual.right);
        }
        return lista;
    }

    // --- INFORMACOES DA ARVORE ---

    // calcula a altura da arvore (mesma logica do getHeight do TreeVisualizer)
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

    // pega o jogador com melhor ranking (menor numero = melhor)
    public Jogador getMelhor() {
        if (root == null) return null;
        return acharMenorNo(root).jogador;
    }

    // pega o jogador com pior ranking (maior numero)
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

    // limpa a arvore inteira
    public void limpar() {
        root = null;
        tamanho = 0;
    }
}
