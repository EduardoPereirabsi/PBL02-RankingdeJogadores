public class BinarySearchTree {
    private Node root;
    private int size;

    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    public void insert(Player player) {
        if (searchByRanking(root, player.getRanking()) != null) return;
        root = insert(root, player);
        size++;
    }

    private Node insert(Node current, Player player) {
        if (current == null) return new Node(player);
        if (player.getRanking() < current.player.getRanking()) {
            current.left = insert(current.left, player);
        } else if (player.getRanking() > current.player.getRanking()) {
            current.right = insert(current.right, player);
        }
        return current;
    }

    public boolean search(String name) {
        return search(root, name) != null;
    }

    private Node search(Node current, String name) {
        if (current == null) return null;
        if (current.player.getNickname().equalsIgnoreCase(name)) return current;
        Node found = search(current.left, name);
        if (found != null) return found;
        return search(current.right, name);
    }

    public Player remove(String name) {
        Node found = search(root, name);
        if (found == null) return null;
        Player removed = found.player;
        root = remove(root, name);
        size--;
        return removed;
    }

    private Node remove(Node current, String name) {
        if (current == null) return null;
        if (current.player.getNickname().equalsIgnoreCase(name)) {
            if (current.left == null) return current.right;
            if (current.right == null) return current.left;
            Node successor = findMinNode(current.right);
            current.player = successor.player;
            current.right = removeByRanking(current.right, successor.player.getRanking());
            return current;
        }
        current.left = remove(current.left, name);
        current.right = remove(current.right, name);
        return current;
    }

    private Node removeByRanking(Node node, int ranking) {
        if (node == null) return null;
        if (ranking < node.player.getRanking()) {
            node.left = removeByRanking(node.left, ranking);
        } else if (ranking > node.player.getRanking()) {
            node.right = removeByRanking(node.right, ranking);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node successor = findMinNode(node.right);
            node.player = successor.player;
            node.right = removeByRanking(node.right, successor.player.getRanking());
        }
        return node;
    }

    private Node findMinNode(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node searchByRanking(Node node, int ranking) {
        if (node == null) return null;
        if (ranking == node.player.getRanking()) return node;
        if (ranking < node.player.getRanking()) return searchByRanking(node.left, ranking);
        return searchByRanking(node.right, ranking);
    }

    public Player getPlayerByRanking(int ranking) {
        Node result = searchByRanking(root, ranking);
        return result != null ? result.player : null;
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
