package fr.univlille.iutinfo.model.game.strategy;

import fr.univlille.iutinfo.cam.player.hunter.IHunterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.model.game.Game;
import fr.univlille.iutinfo.model.plateau.Cell;
import fr.univlille.iutinfo.model.plateau.Coordinate;

import java.util.Random;

public class IaHunterStrategy implements IHunterStrategy {
    private static final boolean ISBOT = true; // Indique que ce chasseur n'est pas un bot
    private Game observer; // Observateur du jeu

    private ICellEvent choice;

    public IaHunterStrategy(Game obs) {
        this.observer = obs;
    }

    @Override
    public void initialize(int rows, int cols) {
        this.observer.getBoard().hunterView = new Cell[rows][cols];
        for (int x = 0 ; x < rows ; x++) {
            for (int y = 0 ; y < cols ; y++) {
                this.observer.getBoard().hunterView[x][y] = new Cell(null, new Coordinate(x,y));
            }
        }
    }


    @Override
    public ICoordinate play() {
        Random rand =new Random();
        int b =-1;
        int a= -1;
        while (!observer.getBoard().isValidShot(new Coordinate(b,a))){
           b= rand.nextInt(0,observer.getBoard().getSizeCol()+1);
           a= rand.nextInt(0,observer.getBoard().getSizeRow()+1);
        }
            ICoordinate coordinate= new Coordinate(b,a);
            this.observer.changeHunterView(coordinate);
            this.choice = new Cell(ICellEvent.CellInfo.HUNTER, coordinate);
            return coordinate;
    }



    /**
     * Méthode appelée pour mettre à jour la stratégie du chasseur en fonction d'un événement de cellule.
     *
     * @param iCellEvent L'événement de cellule à traiter.
     */
    @Override
    public void update(ICellEvent iCellEvent) {
        if (this.play() != null) {
            this.observer.update(choice);
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

    public boolean isBot() {
        return ISBOT;
    }

    public Game getObserver() {
        return observer;
    }

    @Override
    public String toString(){
        return "chasseur IA";
    }

}
