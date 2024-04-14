package fr.univlille.iutinfo.model.plateau;

import java.util.*;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

/**
 * La class Maze représente un labyrinthe de jeu avec un chasseur, un monstre et
 * une sortie. Elle permet de gérer les déplacements, les événements et
 * l'affichage du labyrinthe dans un terminal.
 *
 * @author samy.vancalster.etu@unvi-lille.fr
 * @author valentin.hebert.etu@unvi-lille.fr
 */
public class Maze extends Plateau {

    /**
     * Coordinate actuel du chasseur
     */
    private ICoordinate hunterCo;

    /**
     * Coordinate actuel du monster
     */
    private ICoordinate monsterCo;

    /**
     * Coordinate actuel de la sortie
     */
    private ICoordinate exitCo;

    /**
     * Historique des déplacements du monstre
     */
    private Set<ICoordinate> monsterMoves;


    public ICellEvent[][] hunterView;

    public boolean[][] monsterView;

    /**
     * Coordonate par défaut du Monstre
     */
    private static final ICoordinate DEFAULT_MONSTER_COORDINATE = new Coordinate(0, 0);

    /**
     * Coordonate par défaut du Chasseur
     */
    private static final ICoordinate DEFAULT_HUNTER_COORDINATE = new Coordinate(25, 25);

    /**
     * Coordonate par defaut de la sortie
     */
    private static final ICoordinate DEFAULT_EXIT_COORDINATE = new Coordinate(10, 10);

    /**
     * Crée un nouveau labyrinthe avec des dimensions par défaut et des positions
     * par défaut pour le monstre, le chasseur et la sortie.
     */
    public Maze() {
        this(DEFAULT_ROW, DEFAULT_COL, DEFAULT_MONSTER_COORDINATE, DEFAULT_EXIT_COORDINATE);
    }

    /**
     * Crée un nouveau labyrinthe avec des dimensions personnalisées et des
     * positions pour le monstre, le chasseur et la sortie.
     *
     * @param monsterCo Les coordonnées du monstre.
     * @param exitCo    Les coordonnées de la sortie.
     */
    public Maze(ICoordinate monsterCo, ICoordinate exitCo) {
        this(DEFAULT_ROW, DEFAULT_COL, monsterCo, exitCo);
    }

    /**
     * Crée un nouveau labyrinthe avec des dimensions personnalisées et des
     * positions pour le monstre, le chasseur et la sortie.
     *
     * @param height    La hauteur du labyrinthe.
     * @param weight    La largeur du labyrinthe.
     * @param monsterCo Les coordonnées du monstre.
     * @param exitCo    Les coordonnées de la sortie.
     */
    public Maze(int height, int weight, ICoordinate monsterCo, ICoordinate exitCo) {
        this(height, weight, monsterCo, exitCo, wallProbabilities);
    }

    public  Maze(int height,int weight, double newWallProbability){
        this(height,weight, DEFAULT_MONSTER_COORDINATE, DEFAULT_EXIT_COORDINATE, newWallProbability);
    }

    public Maze(int height, int weight, ICoordinate monsterCo, ICoordinate exitCo, double wallProbability) {

        super.cells = createArea(wallProbability, height, weight);
        Random rand = new Random();

        if (!isInMaze(monsterCo) ) {
            if (height>4 && weight>4){
                monsterCo = new Coordinate(rand.nextInt(5), rand.nextInt(5));
            }else {
                monsterCo = new Coordinate(0,0);
            }
        }
        if (!isInMaze(exitCo)){
            exitCo = new Coordinate(rand.nextInt(height/2,height),rand.nextInt(weight/2,weight));
        }
        if (!isPathPossible(monsterCo, exitCo)) {
            replace(createPath(monsterCo, exitCo), CellInfo.WALL, CellInfo.EMPTY);
        }
        int row = exitCo.getRow();
        int col = exitCo.getCol();
        if (cells[row][col].getState() == CellInfo.WALL) {
            replace(exitCo, CellInfo.WALL, CellInfo.EMPTY);
        }
        this.monsterCo = monsterCo;
        this.hunterCo = new Coordinate(height + 5, weight + 5);
        this.exitCo = exitCo;

    }
    /**
     * Crée une copie du labyrinthe Maze spécifié. Cette copie est indépendante du
     * labyrinthe d'origine.
     *
     * @param maze Le labyrinthe à copier.
     * @return Une copie du labyrinthe spécifié.
     */
    public static Maze copyOf(Maze maze) {
        Maze copiedMaze = new Maze();
        copiedMaze.setCells(new ICellEvent[maze.getCells().length][maze.getCells()[0].length]);
        for (int row = 0; row < maze.getCells().length; row++) {
            for (int col = 0; col < maze.getCells()[0].length; col++) {
                copiedMaze.getCells()[row][col] = new Cell(maze.getCells()[row][col].getState(), new Coordinate(row, col));
            }
        }

        copiedMaze.setHunterCo(new Coordinate(maze.getHunterCo().getRow(), maze.getHunterCo().getCol()));
        copiedMaze.setMonsterCo(new Cell(CellInfo.MONSTER, maze.cells[maze.monsterCo.getRow()][maze.monsterCo.getCol()].getTurn(), maze.monsterCo));
        copiedMaze.setExitCo(new Coordinate(maze.getExitCo().getRow(), maze.getExitCo().getCol()));
        copiedMaze.setMonsterMoves(new HashSet<>(maze.getMonsterMoves()));

        return copiedMaze;
    }


    public void setHunterCo(ICoordinate hunterCo) {
        this.hunterCo = hunterCo;
    }

    public void setMonsterCo(ICellEvent arg0) {
        if(this.monsterCo != null){
            cells[this.monsterCo.getRow()][this.monsterCo.getCol()] = new Cell(CellInfo.EMPTY, arg0.getTurn(), this.monsterCo);
            monsterCo = arg0.getCoord();
            cells[monsterCo.getRow()][monsterCo.getCol()] = arg0;
        } else {
            this.monsterCo = arg0.getCoord();
            cells[monsterCo.getRow()][monsterCo.getCol()] = arg0;
        }
        
    }

    public void setMonsterCo(ICoordinate monsterCo) {
        this.setMonsterCo(new Cell(CellInfo.MONSTER, monsterCo));
    }

    public void setExitCo(ICoordinate exitCo) {
        this.exitCo = exitCo;
    }

    public void setMonsterMoves(Set<ICoordinate> monsterMoves) {
        this.monsterMoves = monsterMoves;
    }

    /**
     * Récupère les coordonnées du chasseur dans le labyrinthe.
     *
     * @return Les coordonnées du chasseur.
     */
    public ICoordinate getHunterCo() {
        return hunterCo;
    }

    /**
     * Récupère les coordonnées du monstre dans le labyrinthe.
     *
     * @return Les coordonnées du monstre.
     */
    public ICoordinate getMonsterCo() {
        return monsterCo;
    }

    /**
     * Récupère les coordonnées de la sortie dans le labyrinthe.
     *
     * @return Les coordonnées de la sortie.
     */
    public ICoordinate getExitCo() {
        return exitCo;
    }

    /**
     * Récupère l'historique des mouvements du monstre dans le labyrinthe.
     *
     * @return L'ensemble des coordonnées de mouvements du monstre.
     */
    public Set<ICoordinate> getMonsterMoves() {
        return monsterMoves;
    }

    /**
     * Obtient les coordonnées par défaut du monstre dans le labyrinthe.
     *
     * @return Les coordonnées par défaut du monstre.
     */
    public static ICoordinate getDefaultMonsterCoordinate() {
        return DEFAULT_MONSTER_COORDINATE;
    }

    /**
     * Obtient les coordonnées par défaut du chasseur dans le labyrinthe.
     *
     * @return Les coordonnées par défaut du chasseur.
     */
    public static ICoordinate getDefaultHunterCoordinate() {
        return DEFAULT_HUNTER_COORDINATE;
    }

    /**
     * Obtient les coordonnées par défaut de la sortie dans le labyrinthe.
     *
     * @return Les coordonnées par défaut de la sortie.
     */
    public static ICoordinate getDefaultExitCoordinate() {
        return DEFAULT_EXIT_COORDINATE;
    }

    

    /**
     * Obtient la hauteur par défaut du labyrinthe.
     *
     * @return La hauteur par défaut du labyrinthe.
     */
    public static int getDefaultHeight() {
        return DEFAULT_ROW;
    }

    /**
     * Obtient la largeur par défaut du labyrinthe.
     *
     * @return La largeur par défaut du labyrinthe.
     */
    public static int getDefaultWieght() {
        return DEFAULT_COL;
    }

    /**
     * Affiche le labyrinthe dans la console.
     */
    public void displayMaze() {
        StringBuilder res = new StringBuilder();
        for (ICellEvent[] cell : cells) {
            for (ICellEvent iCellEvent : cell) {
                res.append(iCellEvent).append(" \u001B[0m");
            }
            res.append("\n");
        }
        System.out.println(res);
    }

    /**
     * Crée un chemin entre les coordonnées du monstre et de la sortie.
     *
     * @param monsterCell Les coordonnées actuelles du monstre.
     * @param exitCell    Les coordonnées de la sortie.
     * @return Une liste de coordonnées représentant le chemin entre le monstre et
     *         la sortie.
     */
    private List<ICoordinate> createPath(ICoordinate monsterCell, ICoordinate exitCell) {
        /*
        List<ICoordinate> res = new ArrayList<>();
        ICoordinate currentCoordinate = monsterCell;

        if (isInMaze(monsterCell) && isInMaze(exitCell)) {
            while (!currentCoordinate.equals(exitCell)) {
                res.add(currentCoordinate);
                currentCoordinate = shortPathCellFor(currentCoordinate, exitCell);
            }
            res.add(currentCoordinate);
        }
        */

        return new ArrayList<>();
    }

    /**
     * Détermine la prochaine cellule du chemin le plus court entre deux
     * coordonnées.
     *
     * @param start Les coordonnées de départ.
     * @param end   Les coordonnées de destination.
     * @return Les coordonnées de la prochaine cellule du chemin.
     */
    private ICoordinate shortPathCellFor(ICoordinate start, ICoordinate end) {
        int dx = end.getCol() - start.getCol();
        int dy = end.getRow() - start.getRow();

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                int newRow = start.getRow();
                int newCol = start.getCol() + 1;
                return new Coordinate(newRow, newCol);
            } else if (dx < 0) {
                int newRow = start.getRow();
                int newCol = start.getCol() - 1;
                return new Coordinate(newRow, newCol);
            }
        } else {
            if (dy > 0) {
                int newRow = start.getRow() + 1;
                int newCol = start.getCol();
                return new Coordinate(newRow, newCol);
            } else if (dy < 0) {
                int newRow = start.getRow() - 1;
                int newCol = start.getCol();
                return new Coordinate(newRow, newCol);
            }
        }
        return start;
    }

    /**
     * Copie les cellules du labyrinthe dans un nouvel ensemble d'objets pour les
     * cellules.
     *
     * @param maze Le labyrinthe à partir duquel copier les cellules.
     * @return Un nouvel ensemble de cellules copié depuis le labyrinthe spécifié.
     */
    public static ICellEvent[][] copyCellsOf(Maze maze) {
        ICellEvent[][] copiedCells = new ICellEvent[maze.cells.length][maze.cells[0].length];
        for (int row = 0; row < maze.getCells().length; row++) {
            for (int col = 0; col < maze.getCells()[0].length; col++) {
                copiedCells[row][col] = new Cell(maze.getCells()[row][col].getState(), new Coordinate(col, row));
            }
        }
        return copiedCells;
    }

    /**
     * Met à jour l'état du labyrinthe en fonction d'un événement de cellule donné.
     *
     * @param arg0 L'événement de cellule à prendre en compte pour la mise à jour.
     */
    public void update(ICellEvent arg0) {
        if (arg0.getState().equals(CellInfo.MONSTER)) {
            int turn = arg0.getTurn() + 1;
            cells[monsterCo.getRow()][monsterCo.getCol()] = new Cell(CellInfo.EMPTY, arg0.getTurn(), monsterCo);
            monsterCo = arg0.getCoord();
            ICellEvent cell = new Cell(arg0.getState(),arg0.getTurn()+1,arg0.getCoord());
            cells[monsterCo.getRow()][monsterCo.getCol()] = cell;
        } else if (arg0.getState().equals(CellInfo.HUNTER)) {
            this.hunterCo = arg0.getCoord();
        }
    }

    /**
     * Vérifie si un tir à une coordonnée spécifiée est valide (dans le labyrinthe).
     *
     * @param shot Les coordonnées du tir.
     * @return True si le tir est valide, sinon False.
     */
    public boolean isValidShot(ICoordinate shot) {
        return isInMaze(shot);
    }


    /**
     * Vérifie si un mouvement à une coordonnée spécifiée est valide (dans le labyrinthe).
     *
     * @param movement Les coordonnées du movuvement.
     * @return True si le mouvement est valide, sinon False.
     */
    public boolean isValidMovement(ICoordinate movement) {
        boolean movementRight = (movement.getCol() - monsterCo.getCol() == 1);
        boolean movementLeft = (movement.getCol() - monsterCo.getCol() == -1);
        boolean movementUp = (movement.getRow() - monsterCo.getRow() == -1);
        boolean movementDown = (movement.getRow() - monsterCo.getRow() == 1);
        return !movement.equals(this.monsterCo) && isInMaze(movement) && cells[movement.getRow()][movement.getCol()].getState() != CellInfo.WALL && (movementRight ^ movementLeft ^ movementUp ^ movementDown);
    }


    /**
     * Méthode principale pour tester la classe Maze.
     *
     * @param arg0 Les arguments de la ligne de commande (non utilisés ici).
     */
    public static void main(String[] arg0) {
        Maze maze = new Maze();
        maze.displayMaze();
    }
}