package fr.univlille.iutinfo.model.plateau;

import java.util.List;
import java.util.Random;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

/**
 * Classe représentant un plateau de jeu.
 * @author samy.vancaslter.etu@univ-lille.fr
 */
public class Plateau {

    /** Probabilité par défaut de création de murs sur le plateau. */
    protected static double wallProbabilities = 0.2;

    /** Générateur de nombres aléatoires utilisé pour la création du plateau. */
    private static final Random rand = new Random();

    /** Grille de cellules représentant le plateau. */
    protected ICellEvent[][] cells;

    /** Nombre de lignes par défaut du plateau. */
    protected static final int DEFAULT_ROW = 20;

    /** Nombre de colonnes par défaut du plateau. */
    protected static final int DEFAULT_COL = 20;


    /**
     * Initialise un plateau avec un nombre spécifié de lignes et de colonnes.
     *
     * @param nbRow Le nombre de lignes du plateau.
     * @param nbCol Le nombre de colonnes du plateau.
     */
    public Plateau(int nbRow, int nbCol) {
        this.cells = new ICellEvent[nbRow][nbCol];
    }


    /**
     * Initialise un plateau avec les dimensions par défaut.
     */
    public Plateau() {
        this(getDefaultRow(), getDefaultCol());
    }

    /**
     * Renvoie l'événement de cellule à la coordonnée spécifiée.
     *
     * @param coordinate Les coordonnées de la cellule.
     * @return L'événement de cellule à la coordonnée spécifiée, ou null si la coordonnée est en dehors de la grille.
     */
    public ICellEvent get(ICoordinate coordinate) {
        try {
            return this.cells[coordinate.getRow()][coordinate.getCol()];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Renvoie l'événement de cellule à la position spécifiée dans la grille.
     *
     * @param line La ligne de la cellule.
     * @param col  La colonne de la cellule.
     * @return L'événement de cellule à la position spécifiée, ou null si la position est en dehors de la grille.
     */
    public ICellEvent get(int line, int col) {
        return get(new Coordinate(line, col));
    }

    /**
     * Obtient les cellules du labyrinthe.
     *
     * @return Un tableau d'ICellEvent représentant les cellules du labyrinthe.
     */
    public ICellEvent[][] getCells() {
        return cells;
    }

    /**
     * Renvoie l'état de la cellule à la position spécifiée dans la grille.
     *
     * @param line   La ligne de la cellule.
     * @param column La colonne de la cellule.
     * @return L'état de la cellule à la position spécifiée, ou null si la position n'est pas dans la grille.
     */
    public CellInfo getState(int line, int column) {
        if (isInMaze(line, column)) {
            return cells[line][column].getState();
        }
        return null;
    }

    /**
     * Définit la grille de cellules avec une nouvelle matrice d'événements de cellules.
     *
     * @param cells La nouvelle matrice d'événements de cellules.
     */
    public void setCells(ICellEvent[][] cells) {
        this.cells = cells;
    }

    /**
     * Définit la cellule spécifiée par les coordonnées avec le type de cellule
     * spécifié.
     *
     * @param info       Type de cellule à définir (HUNTER, MONSTER, EXIT, EMPTY).
     * @param coordinate Les coordonnées où définir la cellule.
     */
    public void set(CellInfo info, ICoordinate coordinate) {
        try {
            cells[coordinate.getRow()][coordinate.getCol()] = new Cell(info, coordinate);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Remplace les cellules aux coordonnées spécifiées par un nouvel état, uniquement si
     * l'état actuel des cellules correspond à l'état avant spécifié.
     *
     * @param coordinates La liste des coordonnées des cellules à remplacer.
     * @param before      L'état des cellules avant la modification.
     * @param after       Le nouvel état à attribuer aux cellules.
     */
    public void replace(List<ICoordinate> coordinates, CellInfo before, CellInfo after) {
        for (ICoordinate coordinate : coordinates) {
            if (isInMaze(coordinate)) {
                int row = coordinate.getRow();
                int col = coordinate.getCol();
                if (cells[row][col].getState() == before) {
                    cells[row][col] = new Cell(after, new Coordinate(row, col));
                }
            }
        }
    }

    /**
     * Remplace la cellule à la coordonnée spécifiée par un nouvel état, uniquement si
     * l'état actuel de la cellule correspond à l'état avant spécifié.
     *
     * @param coordinate La coordonnée de la cellule à remplacer.
     * @param before     L'état de la cellule avant la modification.
     * @param after      Le nouvel état à attribuer à la cellule.
     */
    public void replace(ICoordinate coordinate, CellInfo before, CellInfo after) {
        if (isInMaze(coordinate)) {
            int row = coordinate.getRow();
            int col = coordinate.getCol();
            if (cells[row][col].getState() == before) {
                cells[row][col] = new Cell(after, new Coordinate(row, col));
            }
        }
    }
    
    /**
     * Vérifie si les coordonnées spécifiées sont situées dans le labyrinthe.
     *
     * @param coordinate Les coordonnées à vérifier.
     * @return True si les coordonnées sont valides, sinon False.
     */
    public boolean isInMaze(ICoordinate coordinate) {
        int row = coordinate.getRow();
        int col = coordinate.getCol();

        return 0 <= row && row < cells.length && 0 <= col && col < cells[row].length;
    }

    /**
     * Vérifie si les coordonnées spécifiées se trouvent dans le labyrinthe.
     *
     * @param line   Coordonnée de ligne (X).
     * @param column Coordonnée de colonne (Y).
     * @return True si les coordonnées sont valides dans le labyrinthe, sinon False.
     */
    public boolean isInMaze(int line, int column) {
        return isInMaze(new Coordinate(line, column));
    }


    /**
     * Obtient la hauteur par défaut du labyrinthe.
     *
     * @return La hauteur par défaut du labyrinthe.
     */
    public static int getDefaultRow() {
        return DEFAULT_ROW;
    }

    /**
     * Obtient la largeur par défaut du labyrinthe.
     *
     * @return La largeur par défaut du labyrinthe.
     */
    public static int getDefaultCol() {
        return DEFAULT_COL;
    }

    /**
     * Retourne la taille du nombre de lignes de la grille de cellules.
     *
     * @return Le nombre de lignes de la grille.
     */
    public int getSizeRow() {
        return cells.length;
    }

    /**
     * Retourne la taille du nombre de colonnes de la grille de cellules.
     *
     * @return Le nombre de colonnes de la grille.
     */
    public int getSizeCol() {
        return cells[0].length;
    }

    /**
     * Vérifie si un chemin est possible entre deux coordonnées dans le labyrinthe.
     *
     * @param start Les coordonnées de départ.
     * @param end   Les coordonnées de destination.
     * @return Vrai si un chemin est possible, sinon faux.
     */
    public boolean isPathPossible(ICoordinate start, ICoordinate end) {
        return isPathPossible(start.getRow(), start.getCol(), end.getRow(), end.getCol());
    }

    /**
     * Vérifie si un chemin est possible entre deux coordonnées spécifiées dans le
     * labyrinthe.
     *
     * @param startLine   Ligne de départ.
     * @param startColumn Colonne de départ.
     * @param endLine     Ligne de fin.
     * @param endColumn   Colonne de fin.
     * @return True si un chemin est possible, sinon False.
     */
    public boolean isPathPossible(int startLine, int startColumn, int endLine, int endColumn) {
        if (!isInMaze(startLine, startColumn) || !isInMaze(endLine, endColumn)) {
            return false;
        }
        boolean[][] visited = new boolean[cells.length][cells[0].length];

        return isPathPossibleDFS(startLine, startColumn, endLine, endColumn, visited);
    }

    /**
     * Utilise une recherche en profondeur (DFS) pour déterminer si un chemin est
     * possible entre deux cellules du labyrinthe.
     *
     * @param currentLine   La ligne de la cellule actuelle.
     * @param currentColumn La colonne de la cellule actuelle.
     * @param endLine       La ligne de la cellule de destination.
     * @param endColumn     La colonne de la cellule de destination.
     * @param visited       Un tableau pour marquer les cellules déjà visitées.
     * @return Vrai si un chemin est possible, sinon faux.
     */
    private boolean isPathPossibleDFS(int currentLine, int currentColumn, int endLine, int endColumn, boolean[][] visited) {
        if (currentLine == endLine && currentColumn == endColumn) {
            return true;
        }

        visited[currentLine][currentColumn] = true;

        int[] rowOffsets = { -1, 1, 0, 0 };
        int[] colOffsets = { 0, 0, -1, 1 };

        for (int i = 0; i < 4; i++) {
            int newRow = currentLine + rowOffsets[i];
            int newColumn = currentColumn + colOffsets[i];

            if (isInMaze(newRow, newColumn) && !visited[newRow][newColumn] && cells[newRow][newColumn].getState() != CellInfo.WALL) {
                return isPathPossibleDFS(newRow, newColumn, endLine, endColumn, visited);
            }
        }
        return false;
    }

    /**
     * Crée une zone avec des événements de cellule en fonction d'une probabilité donnée.
     *
     * @param probability La probabilité qu'une cellule soit un mur (comprise entre 0 et 1 inclus).
     * @param height      La hauteur de la zone.
     * @param weight      La largeur de la zone.
     * @return Une matrice d'événements de cellules représentant la zone créée.
     */
    public ICellEvent[][] createArea(double probability, int height, int weight) {
        double test;
        ICellEvent[][] area = new ICellEvent[height][weight];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < weight; col++) {
                test = rand.nextDouble();
                area[row][col] = (test <= probability)? new Cell(CellInfo.WALL, new Coordinate(row, col)) : new Cell(CellInfo.EMPTY, new Coordinate(row, col));
            }
        }
        return area;
    }
}