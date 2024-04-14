package fr.univlille.iutinfo.model.game.strategy;

public class Noeud implements  Comparable<Noeud> {
    public Noeud père;
    int x, y; // Coordonnées du nœud
    int coût; // Coût cumulé depuis le nœud initial
    int heuristique; // Valeur heuristique (distance estimée jusqu'au nœud cible)

    public Noeud(int x, int y, int coût, int heuristique) {
        this.x = x;
        this.y = y;
        this.coût = coût;
        this.heuristique = heuristique;
    }

    @Override
    public int compareTo(Noeud autre) {
        return Integer.compare(coût + heuristique, autre.coût + autre.heuristique);
    }
}
