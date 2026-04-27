import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
