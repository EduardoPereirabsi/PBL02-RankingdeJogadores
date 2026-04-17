import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Árvore Binária de Busca (BST) para o ranking de jogadores.
 * Chave de ordenação: ranking do jogador (int).
 * Valores menores vão para a esquerda, maiores para a direita.
 */
public class BinarySearchTree {
    private Node root;
    private int size;

    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    // ==================== INSERÇÃO ====================

    /**
     * Insere um jogador na árvore. Não permite rankings duplicados.
     */
    public boolean insert(Player player) {
        if (search(player.getRanking()) != null) return false;
        root = insertRec(root, player);
        size++;
        return true;
    }

    private Node insertRec(Node node, Player player) {
        if (node == null) return new Node(player);

        if (player.getRanking() < node.player.getRanking()) {
            node.left = insertRec(node.left, player);
        } else if (player.getRanking() > node.player.getRanking()) {
            node.right = insertRec(node.right, player);
        }
        return node;
    }

    // ==================== REMOÇÃO ====================

    /**
     * Remove o jogador com o ranking especificado.
     * Retorna true se removido, false se não encontrado.
     */
    public boolean remove(int ranking) {
        if (search(ranking) == null) return false;
        root = removeRec(root, ranking);
        size--;
        return true;
    }

    private Node removeRec(Node node, int ranking) {
        if (node == null) return null;

        if (ranking < node.player.getRanking()) {
            node.left = removeRec(node.left, ranking);
        } else if (ranking > node.player.getRanking()) {
            node.right = removeRec(node.right, ranking);
        } else {
            // Caso 1: nó folha ou com um único filho
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Caso 2: nó com dois filhos — substitui pelo sucessor in-order
            Node successor = findMinNode(node.right);
            node.player = successor.player;
            node.right = removeRec(node.right, successor.player.getRanking());
        }
        return node;
    }

    private Node findMinNode(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // ==================== BUSCA ====================

    /** Busca jogador por ranking. Retorna null se não encontrado. */
    public Player search(int ranking) {
        Node result = searchRec(root, ranking);
        return result != null ? result.player : null;
    }

    private Node searchRec(Node node, int ranking) {
        if (node == null) return null;
        if (ranking == node.player.getRanking()) return node;
        if (ranking < node.player.getRanking()) return searchRec(node.left, ranking);
        return searchRec(node.right, ranking);
    }

    /** Busca jogador por nickname (busca completa na árvore). */
    public Player searchByNickname(String nickname) {
        return searchByNicknameRec(root, nickname);
    }

    private Player searchByNicknameRec(Node node, String nickname) {
        if (node == null) return null;
        if (node.player.getNickname().equalsIgnoreCase(nickname)) return node.player;
        Player found = searchByNicknameRec(node.left, nickname);
        if (found != null) return found;
        return searchByNicknameRec(node.right, nickname);
    }

    /** Retorna o caminho de busca da raiz até o nó com o ranking dado. */
    public List<Player> getSearchPath(int ranking) {
        List<Player> path = new ArrayList<>();
        searchPath(root, ranking, path);
        return path;
    }

    private boolean searchPath(Node node, int ranking, List<Player> path) {
        if (node == null) return false;
        path.add(node.player);
        if (ranking == node.player.getRanking()) return true;
        if (ranking < node.player.getRanking()) return searchPath(node.left, ranking, path);
        return searchPath(node.right, ranking, path);
    }

    // ==================== PERCURSOS (TRAVERSALS) ====================

    /** Percurso Em Ordem (esquerda → raiz → direita) — retorna ranking ordenado. */
    public List<Player> inOrder() {
        List<Player> result = new ArrayList<>();
        inOrderRec(root, result);
        return result;
    }

    private void inOrderRec(Node node, List<Player> result) {
        if (node == null) return;
        inOrderRec(node.left, result);
        result.add(node.player);
        inOrderRec(node.right, result);
    }

    /** Percurso Pré-Ordem (raiz → esquerda → direita). */
    public List<Player> preOrder() {
        List<Player> result = new ArrayList<>();
        preOrderRec(root, result);
        return result;
    }

    private void preOrderRec(Node node, List<Player> result) {
        if (node == null) return;
        result.add(node.player);
        preOrderRec(node.left, result);
        preOrderRec(node.right, result);
    }

    /** Percurso Pós-Ordem (esquerda → direita → raiz). */
    public List<Player> postOrder() {
        List<Player> result = new ArrayList<>();
        postOrderRec(root, result);
        return result;
    }

    private void postOrderRec(Node node, List<Player> result) {
        if (node == null) return;
        postOrderRec(node.left, result);
        postOrderRec(node.right, result);
        result.add(node.player);
    }

    /** Percurso em Nível (BFS — Breadth-First Search). */
    public List<Player> levelOrder() {
        List<Player> result = new ArrayList<>();
        if (root == null) return result;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            result.add(current.player);
            if (current.left != null) queue.add(current.left);
            if (current.right != null) queue.add(current.right);
        }
        return result;
    }

    // ==================== INFORMAÇÕES DA ÁRVORE ====================

    /** Retorna a altura da árvore (0 se vazia). */
    public int getHeight() {
        return getHeightRec(root);
    }

    private int getHeightRec(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(getHeightRec(node.left), getHeightRec(node.right));
    }

    /** Retorna o número de nós na árvore. */
    public int getSize() { return size; }

    /** Retorna o jogador com menor ranking (melhor posição). */
    public Player getMin() {
        if (root == null) return null;
        return findMinNode(root).player;
    }

    /** Retorna o jogador com maior ranking (pior posição). */
    public Player getMax() {
        if (root == null) return null;
        Node node = root;
        while (node.right != null) node = node.right;
        return node.player;
    }

    /** Retorna a raiz da árvore. */
    public Node getRoot() { return root; }

    /** Limpa toda a árvore. */
    public void clear() {
        root = null;
        size = 0;
    }
}
