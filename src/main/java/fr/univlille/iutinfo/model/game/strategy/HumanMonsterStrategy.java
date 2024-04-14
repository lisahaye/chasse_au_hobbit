

package fr.univlille.iutinfo.model.game.strategy;

import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.model.game.Game;

/**
 * La classe HumanMonsterStrategy représente la stratégie d'un monstre humain dans le jeu.
 * Ce monstre est contrôlé par un joueur et effectue des mouvements en fonction des entrées du joueur.
 *  @author valentin.hebert.etu@unvi-lille.fr
 */
public class HumanMonsterStrategy implements IMonsterStrategy {
    private static final boolean ISBOT = false; // Indique que ce monstre n'est pas un bot
    Game observer; // Observateur du jeu
    private ICellEvent choice;

    public HumanMonsterStrategy(Game obs) {
        this.observer = obs;
    }

    /**
     * Méthode appelée pour permettre au monstre humain de jouer un tour.
     * Le monstre affiche sa vue actuelle, puis attend une entrée du joueur pour effectuer un mouvement.
     * Le monstre effectue un mouvement valide et met à jour la case avec l'information du monstre.
     *
     * @return Les coordonnées du mouvement effectué par le monstre.
     */
    @Override
    public ICoordinate play() {
        if ( observer.getBoard().isValidMovement(this.choice.getCoord())){
            //this.observer.changeMonsterView(this.choice.getCoord());  --> pas encore implémenté
            return choice.getCoord();
        }
        return null;
    }

    /**
     * Méthode appelée pour mettre à jour la stratégie du monstre en fonction d'un événement de cellule.
     *
     * @param arg0 L'événement de cellule à traiter.
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
     * Initialise la stratégie du monstre avec la vue initiale.
     *
     * @param arg0 La vue initiale du monstre.
     */
    @Override
    public void initialize(boolean[][] arg0) {
        this.observer.getBoard().monsterView = arg0;
    }

    /**
     * Attache l'observateur du jeu à la stratégie du monstre.
     *
     * @param obs L'observateur du jeu.
     */


public void attach(Game obs) {
    this.observer = obs;
}

/**
 * Détache l'observateur du jeu de la stratégie du monstre.
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
    return "monstre humain";
}
}