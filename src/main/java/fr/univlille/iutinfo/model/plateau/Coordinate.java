package fr.univlille.iutinfo.model.plateau;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

/**
 * Cette classe représente une paire de coordonnées (colonne, ligne) dans un système de coordonnées.
 * Elle implémente l'interface ICoordinate pour permettre la gestion des coordonnées.
 */
public class Coordinate implements ICoordinate {
    int col, row;

    /**
     * Crée une instance de Coordinate avec les coordonnées spécifiées.
     *
     * @param col La valeur de la colonne.
     * @param row La valeur de la ligne.
     */
    public Coordinate(int col, int row) {
        this.col = col;
        this.row = row;
    }

    /**
     * Calcule un code de hachage pour l'objet Coordinate.
     *
     * @return Le code de hachage calculé.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + col;
        result = prime * result + row;
        return result;
    }

    /**
     * Compare si l'objet Coordinate est égal à un autre objet.
     *
     * @param obj L'objet à comparer.
     * @return true si les objets sont égaux, sinon false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coordinate other = (Coordinate) obj;
        if (col != other.col)
            return false;
        if (row != other.row)
            return false;
        return true;
    }

    /**
     * Récupère la coordonnée de colonne.
     *
     * @return La coordonnée de colonne.
     */
    @Override
    public int getCol() {
        return col;
    }

    /**
     * Récupère la coordonnée de ligne.
     *
     * @return La coordonnée de ligne.
     */
    @Override
    public int getRow() {
        return row;
    }

     /**
     * Convertit les coordonnées en une chaîne de caractères au format "colonne:ligne".
     *
     * @return Les coordonnées au format "colonne:ligne".
     */
    @Override
    public String toString() {
        return col + ":" + row;
    }

    
    public boolean equals(Coordinate coordinate) {
        return coordinate.getRow() == this.getRow()
                && coordinate.getCol() == this.getCol();
    }
}