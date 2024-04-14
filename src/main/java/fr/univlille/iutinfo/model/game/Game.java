package fr.univlille.iutinfo.model.game;

import fr.univlille.iutinfo.controler.Controler;
import fr.univlille.iutinfo.model.game.strategy.IaHunterStrategy;
import fr.univlille.iutinfo.model.game.strategy.IaMonsterStrategy;
import fr.univlille.iutinfo.model.plateau.Maze;
import fr.univlille.iutinfo.view.View;
import fr.univlille.iutinfo.cam.player.IStrategy;
import fr.univlille.iutinfo.cam.player.hunter.IHunterStrategy;
import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

import java.util.ArrayList;

/**
 * La classe Game représente un jeu impliquant un monstre et un chasseur. Les
 * joueurs, le monstre et le chasseur, utilisent des stratégies spécifiques pour
 * jouer leur tour. La classe gère le déroulement du jeu, le tour des joueurs,
 * et la mise à jour du plateau.
 * 
 * @author abdallah.toumji.etu@univ-lille.fr
 * @author valentin.hebert.etu@univ-lille.fr
 */
public class Game  {
    private boolean isMonsterTurn = true;
    private IMonsterStrategy monster;
    private IHunterStrategy hunter;
    private final ArrayList<View> observer;

    private boolean monsterBot;
    private boolean hunterBot;
    private Maze board;
    private int turn;

    private Controler controler;


    /**
     * Constructeur de la classe Game.
     *
     * @param monster La stratégie du monstre
     * @param hunter  La stratégie du chasseur
     * @param board   Le plateau de jeu
     */
    public Game(IMonsterStrategy monster, IHunterStrategy hunter, Maze board) {
        this.monster = monster;
        this.hunter = hunter;
        this.board = board;
        this.turn = 0;

        this.observer = new ArrayList<>();
    }

    /**
     * Constructeur de la classe Game pour les tests avec des stratégies humaines.
     */
    public Game(Maze board) {
        this.turn = 0;
        this.board = board;
        this.observer = new ArrayList<>();
    }


    /**
     * Initialise le champ visuel du monstre et du chasseur.
     */
    public void initialize() {
        int row = board.getCells().length;
        int col = board.getCells()[0].length;
        boolean[][] monsterView = new boolean[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col ; j++) {
                monsterView[i][j] = true;
            }
        }
        monster.initialize(monsterView);
        hunter.initialize(row, col);
    }
    public void setControler(Controler c){
        this.controler=c;
    }

    public void reset(Maze board){
        this.turn = 0;
        this.isMonsterTurn = true;
        this.board = board;
        this.hunter = null;
        this.monster = null;
    }

    /**
     * Incrémente le tour et fait jouer le monstre et le chasseur.
     */
    public void swapTurn() {
        isMonsterTurn = !isMonsterTurn;
            
        if (isMonsterTurn) {
            this.turn = turn + 1;
        }
        notifyAllController();
    }
    public void notifyAllController(){
        if (isMonsterTurn) {
            controler.update(monster);
        } else {
            controler.update(hunter);
        }
    }



    /**
     * Récupère le numéro du tour en cours.
     *
     * @return Le numéro du tour
     */
    public int getTurn() {
        return turn;
    }
    public Controler getControler(){
        return this.controler;
    }

    /**
     * Modifie le plateau en fonction de l'événement de cellule passé en paramètre.
     *
     * @param arg0 Informations sur la case
     */
    public void update(ICellEvent arg0) {
        board.update(arg0);
    }

    public void updateHunter(ICellEvent arg0) {
        hunter.update(arg0);
    }


    /**
     * Ajoute à la liste de l'observateur la vue.
     *
     * @param obs L'observateur
     */
    public void attach(View obs) {
        this.observer.add(obs);
    }

    public boolean isMonsterTurn() {
        return isMonsterTurn;
    }



    public IMonsterStrategy getMonster() {
        return monster;
    }

    public Maze getBoard() {
        return board;
    }


    public void changeHunterView(ICoordinate coord) {
        board.hunterView[coord.getRow()][coord.getCol()] = board.getCells()[coord.getRow()][coord.getCol()];
    }

    public void updateMonster(ICellEvent choice) {
        monster.update(choice);
    }
    public boolean getIsmonsterturn(){return this.isMonsterTurn;}

    public IStrategy victoryTest(){
        if(this.board.getExitCo().equals(board.getMonsterCo()) && !this.isMonsterTurn){
            return this.monster;
        }if(this.board.getMonsterCo().equals(board.getHunterCo()) && this.isMonsterTurn){
            return this.hunter;
        }
        // Victoire meme si tir tour d'avant
        
        return null;
    }

    public void setMonster(IMonsterStrategy newMonster){
        this.monster = newMonster;
        this.monsterBot = newMonster instanceof IaMonsterStrategy;
    }

    public void setHunter(IHunterStrategy newHunter){
        this.hunter = newHunter;
        this.hunterBot = newHunter instanceof IaHunterStrategy;
    }

    public void setBoard(int height, int weight, double wallProbabilities) {
        this.board = new Maze(height,weight,wallProbabilities);
    }

    public boolean isMonsterBot() {
        return monsterBot;
    }

    public boolean isHunterBot() {
        return hunterBot;
    }
}
