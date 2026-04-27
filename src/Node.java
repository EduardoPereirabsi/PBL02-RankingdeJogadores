/**
 * Nó da Árvore Binária de Busca.
 * Armazena um jogador e referências para os filhos esquerdo e direito.
 */
public class Node {
    Player player;
    Node left;
    Node right;

    public Node(Player player) {
        this.player = player;
        this.left = null;
        this.right = null;
    }
}
