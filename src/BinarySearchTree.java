public class BinarySearchTree {
    private Node root;
    private int size;

    public BinarySearchTree() {
        root = null;
        size = 0;
    }

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
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

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

    public Player[] getSearchPath(int ranking) {
        Player[] temp = new Player[getHeight()];
        int[] idx = {0};
        searchPath(root, ranking, temp, idx);
        Player[] resultado = new Player[idx[0]];
        for (int i = 0; i < idx[0]; i++) resultado[i] = temp[i];
        return resultado;
    }

    private boolean searchPath(Node node, int ranking, Player[] path, int[] idx) {
        if (node == null) return false;
        path[idx[0]++] = node.player;
        if (ranking == node.player.getRanking()) return true;
        if (ranking < node.player.getRanking()) return searchPath(node.left, ranking, path, idx);
        return searchPath(node.right, ranking, path, idx);
    }

    public Player[] inOrder() {
        Player[] resultado = new Player[size];
        int[] idx = {0};
        inOrderRec(root, resultado, idx);
        return resultado;
    }

    private void inOrderRec(Node node, Player[] resultado, int[] idx) {
        if (node == null) return;
        inOrderRec(node.left, resultado, idx);
        resultado[idx[0]++] = node.player;
        inOrderRec(node.right, resultado, idx);
    }

    public Player[] preOrder() {
        Player[] resultado = new Player[size];
        int[] idx = {0};
        preOrderRec(root, resultado, idx);
        return resultado;
    }

    private void preOrderRec(Node node, Player[] resultado, int[] idx) {
        if (node == null) return;
        resultado[idx[0]++] = node.player;
        preOrderRec(node.left, resultado, idx);
        preOrderRec(node.right, resultado, idx);
    }

    public Player[] postOrder() {
        Player[] resultado = new Player[size];
        int[] idx = {0};
        postOrderRec(root, resultado, idx);
        return resultado;
    }

    private void postOrderRec(Node node, Player[] resultado, int[] idx) {
        if (node == null) return;
        postOrderRec(node.left, resultado, idx);
        postOrderRec(node.right, resultado, idx);
        resultado[idx[0]++] = node.player;
    }

    public Player[] levelOrder() {
        Player[] resultado = new Player[size];
        if (root == null) return resultado;
        Node[] fila = new Node[size];
        int inicio = 0, fim = 0;
        fila[fim++] = root;
        int idx = 0;
        while (inicio < fim) {
            Node atual = fila[inicio++];
            resultado[idx++] = atual.player;
            if (atual.left != null) fila[fim++] = atual.left;
            if (atual.right != null) fila[fim++] = atual.right;
        }
        return resultado;
    }

    public int getHeight() {
        return getHeightRec(root);
    }

    private int getHeightRec(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(getHeightRec(node.left), getHeightRec(node.right));
    }

    public int getSize() { return size; }

    public Player getMin() {
        if (root == null) return null;
        return findMinNode(root).player;
    }

    public Player getMax() {
        if (root == null) return null;
        Node node = root;
        while (node.right != null) node = node.right;
        return node.player;
    }

    public Node getRoot() { return root; }

    public void clear() {
        root = null;
        size = 0;
    }
}
