package fr.univlille.iutinfo.model.game.strategy;

import fr.univlille.iutinfo.model.game.Game;
import fr.univlille.iutinfo.model.plateau.Cell;
import fr.univlille.iutinfo.model.plateau.Coordinate;
import fr.univlille.iutinfo.cam.player.hunter.IHunterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

/**
 * La classe HumanHunterStrategy représente la stratégie d'un chasseur humain dans le jeu.
 * Ce chasseur est contrôlé par un joueur et effectue des actions en fonction des entrées du joueur.
 * @author abdallah.toumki.etu@univ-lille.fr
 *  @author valentin.hebert.etu@unvi-lille.fr
 */
public class HumanHunterStrategy implements IHunterStrategy {
    private static final boolean ISBOT = false; // Indique que ce chasseur n'est pas un bot
    private Game observer; // Observateur du jeu
    private ICellEvent choice;

    public HumanHunterStrategy(Game obs) {
        this.observer = obs;
    }

    /**
     * Initialise la stratégie du chasseur avec les dimensions du plateau.
     *
     * @param rows Le nombre de lignes du plateau.
     * @param cols Le nombre de colonnes du plateau.
     */
    @Override
    public void initialize(int rows, int cols) {
        this.observer.getBoard().hunterView = new Cell[rows][cols];
        for (int x = 0 ; x < rows ; x++) {
            for (int y = 0 ; y < cols ; y++) {
                this.observer.getBoard().hunterView[x][y] = new Cell(null, new Coordinate(x,y));
            }
        }
    }

    /**
     * Méthode appelée pour permettre au chasseur humain de jouer un tour.
     * Le chasseur affiche sa vue actuelle, puis attend une entrée du joueur pour tirer.
     * Le chasseur effectue un tir valide et met à jour la case avec l'information du chasseur.
     *
     * @return Les coordonnées du tir effectué par le chasseur.
     */
    @Override
    public ICoordinate play() {
        if ( observer.getBoard().isValidShot(this.choice.getCoord())){
            this.observer.changeHunterView(this.choice.getCoord());
            return choice.getCoord();
        }
        return null;
    }



    /**
     * Méthode appelée pour mettre à jour la stratégie du chasseur en fonction d'un événement de cellule.
     *
     * @param iCellEvent L'événement de cellule à traiter.
     */
    @Override
    public void update(ICellEvent iCellEvent) {
        this.choice = iCellEvent;
        if (this.play() != null) {
            this.observer.update(iCellEvent);
            this.observer.swapTurn();
        }
        
    }


    

    /**
     * Attache l'observateur du jeu à la stratégie du chasseur.
     *
     * @param obs L'observateur du jeu.
     */
    public void attach(Game obs) {
        this.observer = obs;
    }

    /**
     * Détache l'observateur du jeu de la stratégie du chasseur.
     */
    public void detach() {
        observer = null;
    }

    public boolean isISBOT() {
        return ISBOT;
    }

    public Game getObserver() {
        return observer;
    }

    @Override
    public String toString(){
        return "chasseur humain";
    }
}
