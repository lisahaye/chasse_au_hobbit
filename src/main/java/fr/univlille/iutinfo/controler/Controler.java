package fr.univlille.iutinfo.controler;

import fr.univlille.iutinfo.cam.player.IStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.model.game.Game;
import fr.univlille.iutinfo.view.View;
import fr.univlille.iutinfo.model.plateau.Maze;

import java.util.ArrayList;

import static java.lang.System.out;
import static java.lang.Thread.sleep;

public class Controler {

    private Game game;
    private ArrayList<View> observer;





    public Controler(Game g){
        this.game=g;
        this.observer=new ArrayList<View>();
    }

    public void playTurn(IStrategy player)  {
        if (game.getIsmonsterturn()) {
            if (game.isMonsterBot()){
                if(!game.isHunterBot()){
                    ICoordinate monsterCoordinate = game.getBoard().getMonsterCo();
                    game.updateMonster(game.getBoard().get(monsterCoordinate));
                }else {
                    this.displayIAMonsterView();
                }
            } else {
                this.displayMonsterView();
            }
        } else {
            if (game.isHunterBot()){
                game.updateHunter(game.getBoard().get(game.getBoard().getHunterCo()));
            } else {
                this.displayHunterView();
            }
        }
    }


    public void start() {
       game.initialize();
       playTurn(game.getMonster());
    }

    public void displayHunterView() {
        for (View i: observer) {
            i.displayHunterView(game.getBoard().hunterView);
        }

    }
    public void displayIAHunterView() {
        for (View i: observer) {
            i.displayHunterView(game.getBoard().hunterView);
        }

    }
    public void displayMonsterView() {
        for (View o: observer) {
            o.displayMonsterView(game.getBoard().monsterView);
        }

    }

    public void displayIAMonsterView() {
        for (View o: observer) {
            o.displayIAMonsterView(game.getBoard().monsterView);
        }

    }
    public void attach(View obs) {
        this.observer.add(obs);
    }
    public void detach(View obs) {
        this.observer.remove(obs);
    }


    public void update(IStrategy data){
        if (game.victoryTest() != null) {
            for (View v : observer) {
                v.displayVictory(game.victoryTest());
            }
        } else {
            playTurn(data);
        }
    }
    public void poser_la_Trace(Maze board ,ICoordinate icoordinate ){
    int row= icoordinate.getRow();
    int col=icoordinate.getCol() ;
    int nbr_de_fois=1;
    if (game.isMonsterTurn() ){
            

    }



    }
}
