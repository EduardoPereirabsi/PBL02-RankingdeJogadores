// No da arvore binaria de busca
// Adaptado do Node do TreeVisualizer.java da professora
// Em vez de guardar letra e codigo morse, guarda um Jogador
// Mantem a mesma logica de filho esquerdo (left) e direito (right)
public class No {
    Jogador jogador;
    No left;   // filho da esquerda (ranking menor)
    No right;  // filho da direita (ranking maior)

    public No(Jogador jogador) {
        this.jogador = jogador;
        this.left = null;
        this.right = null;
    }
}
