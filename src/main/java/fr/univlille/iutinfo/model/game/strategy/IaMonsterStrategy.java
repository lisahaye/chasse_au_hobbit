package fr.univlille.iutinfo.model.game.strategy;

import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.model.game.Game;
import fr.univlille.iutinfo.model.plateau.*;

import java.util.*;

public class IaMonsterStrategy implements IMonsterStrategy {
    private static final boolean ISBOT = true;
    private Game observer; // Observateur du jeu

    private ICellEvent choice;

    private ICellEvent monsterPosition;

    public IaMonsterStrategy(Game obs){this.observer=obs;}

    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // Mouvements possibles (haut, droite, bas, gauche)




    /**
     * Méthode appelée pour permettre au chasseur humain de jouer un tour.
     * Le chasseur affiche sa vue actuelle, puis attend une entrée du joueur pour tirer.
     * Le chasseur effectue un tir valide et met à jour la case avec l'information du chasseur.
     *
     * @return Les coordonnées du tir effectué par le chasseur.
     */

    public ICoordinate play() {
        /*List<Coordinate> list= trouverChemin(this.observer.getBoard());
        for (Coordinate c : list){
            System.out.println(c);

        if(!list.isEmpty()){
            this.choice = new Cell(ICellEvent.CellInfo.MONSTER, this.observer.getTurn(), new Coordinate(list.get(1).getCol(), list.get(1).getRow()));
        }
        else{
            this.choice = new Cell(ICellEvent.CellInfo.MONSTER, this.observer.getTurn(), this.observer.getBoard().getMonsterCo());
        }
    }
*/



       this.choice= this.monsterPosition;
        Random rand = new Random();
        Double d ;
        while(!observer.getBoard().isValidMovement(this.choice.getCoord())){
            this.choice = this.monsterPosition;
            d = rand.nextDouble();
            if (d <= 0.25) {
                this.choice = new Cell(ICellEvent.CellInfo.MONSTER, this.observer.getTurn(), new Coordinate(this.choice.getCoord().getCol() + 1, this.choice.getCoord().getRow()));
            } else if (d <= 0.5) {
                this.choice = new Cell(ICellEvent.CellInfo.MONSTER, this.observer.getTurn(),  new Coordinate(this.choice.getCoord().getCol() - 1, this.choice.getCoord().getRow()));
            } else if (d <= 0.75) {
                this.choice = new Cell(ICellEvent.CellInfo.MONSTER, this.observer.getTurn(),  new Coordinate(this.choice.getCoord().getCol() , this.choice.getCoord().getRow() + 1));;
            } else {
                this.choice = new Cell(ICellEvent.CellInfo.MONSTER, this.observer.getTurn(),  new Coordinate(this.choice.getCoord().getCol(), this.choice.getCoord().getRow() - 1));
                ;
            }
        }




        return this.choice.getCoord();
    }









    /**
     * Méthode appelée pour mettre à jour la stratégie du chasseur en fonction d'un événement de cellule.
     *
     * @param iCellEvent L'événement de cellule à traiter.
     */

    public void update(ICellEvent iCellEvent) {
        this.monsterPosition = iCellEvent;
        if (this.play() != null) {
            this.observer.update(choice);
            this.observer.swapTurn();
        }

    }

    @Override
    public void initialize(boolean[][] arg0) {
        this.observer.getBoard().monsterView = arg0;
    }
    public void attach(Game obs) {
        this.observer = obs;
    }

    /**
     * Détache l'observateur du jeu de la stratégie du monstre.
     */
    public void detach() {
        observer = null;
    }

    public boolean isBot() {
        return ISBOT;
    }

    public Game getObserver() {
        return observer;
    }

    @Override
    public String toString(){
        return "monstre IA";
    }






    public static List<Coordinate> trouverChemin(Maze grille) {

        // Initialisation
        PriorityQueue<Noeud> frontière = new PriorityQueue<>();
        Map<Coordinate, Noeud> exploré = new HashMap<>();
        Noeud initial = new Noeud(grille.getMonsterCo().getCol(), grille.getMonsterCo().getRow(), 0, calculerHeuristique(grille));
        frontière.add(initial);
        exploré.put(new Coordinate(initial.x, initial.y), initial);

        // Recherche
        while (!frontière.isEmpty()){
            Noeud actuel = frontière.poll();
            if (actuel.x == grille.getExitCo().getCol() && actuel.y == grille.getExitCo().getRow()) {
                return reconstruireChemin(actuel);
            }
            for (int[] direction : DIRECTIONS) {
                int x = actuel.x + direction[0];
                int y = actuel.y + direction[1];
                if (estValide(grille, x, y)) {
                    Noeud voisin = exploré.get(new Coordinate(x, y));
                    if (voisin == null) {
                        voisin = new Noeud(x, y, actuel.coût + 1, calculerHeuristique(grille));
                        frontière.add(voisin);
                        exploré.put(new Coordinate(x, y), voisin);
                    } else if (voisin.coût > actuel.coût + 1) {
                        frontière.remove(voisin);
                        voisin.coût = actuel.coût + 1;
                        frontière.add(voisin);
                    }
                }
            }
        }


        // Aucun chemin trouvé
        return Collections.emptyList();
    }

    private static boolean estValide(Maze grille, int x, int y) {
        return x >= 0 && x < grille.getSizeCol() && y >= 0 && y < grille.getSizeRow() && grille.get(y,x).equals(ICellEvent.CellInfo.EMPTY) ;
    }

    private static int calculerHeuristique(Maze co) {
        // Heuristique utilisée : distance de Manhattan
    return Math.abs(co.getMonsterCo().getCol() - co.getExitCo().getCol()) + Math.abs(co.getMonsterCo().getRow() - co.getExitCo().getRow());
    }

    private static List<Coordinate> reconstruireChemin(Noeud cible) {
        List<Coordinate> chemin = new ArrayList<>();
        Noeud actuel = cible;
        while (actuel.père != null) {
            chemin.add(new Coordinate(actuel.x, actuel.y));
            actuel = actuel.père;
        }
        Collections.reverse(chemin);
        return chemin;
    }
}
