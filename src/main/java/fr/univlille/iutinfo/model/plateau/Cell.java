package fr.univlille.iutinfo.model.plateau;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

/**
 * Cette classe représente une cellule dans un jeu. Une cellule peut être soit
 * un mur (CellInfo.WALL) soit vide (CellInfo.EMPTY) ou d'autres états
 * possibles.
 * Chaque cellule est associée à un tour particulier.
 */
public class Cell implements ICellEvent {
    public static final int DEFAULT_TURN_CELL = -1;

    CellInfo state;
    ICoordinate coordinate;
    int turn;

    /**
     * Constructeur de la classe Cell avec des paramètres.
     *
     * @param state L'état de la cellule (CellInfo.WALL pour un mur, CellInfo.EMPTY
     *              pour vide, etc.).
     * @param turn  Le tour associé à cette cellule.
     */
    public Cell(CellInfo state, int turn, ICoordinate coordinate) {
        this.state = state;
        this.turn = turn;
        this.coordinate = coordinate;
    }

    /**
     * Constructeur de la classe Cell avec un état spécifié.
     *
     * @param cellInfo L'état de la cellule (CellInfo).
     */
    public Cell(CellInfo cellInfo, ICoordinate coordinate) {
        this(cellInfo, DEFAULT_TURN_CELL, coordinate);
    }

    /**
     * {@inheritDoc}
     * Cette méthode n'est pas implémentée dans cette classe.
     *
     * @throws UnsupportedOperationException L'exception est levée car cette méthode
     *                                       n'est pas implémentée.
     */
    @Override
    public ICoordinate getCoord() {
        return this.coordinate;
    }

    /**
     * {@inheritDoc}
     * Cette méthode retourne l'état de la cellule (CellInfo) associé à cette
     * instance.
     *
     * @return L'état de la cellule (CellInfo) : CellInfo.WALL pour un mur,
     *         CellInfo.EMPTY pour vide, etc.
     */
    @Override
    public CellInfo getState() {
        return state;
    }

    /**
     * {@inheritDoc}
     * Cette méthode retourne le tour associé à cette cellule.
     *
     * @return Le tour associé à cette cellule.
     */
    @Override
    public int getTurn() {
        return turn;
    }

    /**
     * Convertit l'état de la cellule en une chaîne de caractères pour une
     * représentation visuelle.
     *
     * @return Une chaîne de caractères représentant visuellement l'état de la
     *         cellule.
     */
    public String toString() {
        return switch (state) {
            case WALL -> "\u001B[41m "; // Mur avec fond rouge
            case EMPTY -> "\u001B[47m "; // Cellule vide avec fond blanc
            case EXIT -> "\u001B[42m "; // Sortie avec fond vert
            case HUNTER -> "\u001B[43m "; // Chasseur avec fond jaune
            case MONSTER -> "\u001B[44m "; // Monstre avec fond bleu
        };
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((coordinate == null) ? 0 : coordinate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cell other = (Cell) obj;
        if (coordinate == null) {
            return other.coordinate == null;
        } else return coordinate.equals(other.coordinate);
    }
}
