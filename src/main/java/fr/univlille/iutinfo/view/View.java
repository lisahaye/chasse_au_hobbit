package fr.univlille.iutinfo.view;

import fr.univlille.iutinfo.cam.player.IStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.controler.Controler;
import fr.univlille.iutinfo.model.game.Game;
import fr.univlille.iutinfo.model.game.strategy.HumanHunterStrategy;
import fr.univlille.iutinfo.model.game.strategy.HumanMonsterStrategy;
import fr.univlille.iutinfo.model.game.strategy.IaHunterStrategy;
import fr.univlille.iutinfo.model.game.strategy.IaMonsterStrategy;
import fr.univlille.iutinfo.view.graphic.MazeCellPane;
import fr.univlille.iutinfo.model.plateau.Cell;
import fr.univlille.iutinfo.model.plateau.Coordinate;
import fr.univlille.iutinfo.model.plateau.Maze;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class View extends Application {

    private final BorderPane root = new BorderPane();

    private Game observable;
    private MazeCellPane previousSelection = null;
    private boolean leave = false;
    private boolean fog = false;
    private static final TextField textField = new TextField();
    private int fogRange = 5;

    @Override
    public void start(Stage primaryStage) {
        Scene newScene = new Scene(root, 800, 800);
        primaryStage.setScene(newScene);
        //newScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("Chasse au monstre");
        primaryStage.show();
        media_Intro();
        delay(4400, () -> {
            initializeGame();
            initializeObservable();
            mainMenu();
        });
    }

    private void initializeObservable() {
        observable.setBoard(10, 10, 0.2);
        observable.setHunter(new HumanHunterStrategy(observable));
        observable.setMonster(new HumanMonsterStrategy(observable));
        observable.getControler().start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initializeGame() {
        this.observable = new Game(new Maze());
        this.observable.attach(this);
        Controler controler = new Controler(this.observable);
        this.observable.setControler(controler);
        controler.attach(this);
    }


    public void mainMenu() {
        viewReset();
        Button hostBtn = new Button("Jouer");
        Button confiBtn = new Button("Config de la partie");
        Button resetBtn = new Button("Reset");

        Button leaveBtn = new Button("Quitter l'application");

        leaveBtn.setOnAction(e -> System.exit(0));
        hostBtn.setOnAction(e -> hostMenu());
        confiBtn.setOnAction(e -> configMenu());
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(hostBtn, confiBtn,resetBtn, leaveBtn);
        root.setCenter(vbox);
    }


    public void media_Intro() {

        java.net.URL url = View.class.getResource("/videos/intro.mp4");
        StackPane St = new StackPane();

        if (url != null) {
            javafx.scene.media.Media video = new Media(url.toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(video);
            mediaPlayer.setAutoPlay(true);

            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitHeight(450);
            mediaView.setFitWidth(800);

            St.getChildren().add(mediaView);
        } else {
            // Gérer le cas où l'URL n'a pas été trouvée
            System.err.println("Erreur : Impossible de trouver la ressource intro.mp4");
        }
        root.setStyle("-fx-background-color: #000000;");
        root.setCenter(St);

    }



    private void debugMenu() {
        viewReset();
        Button hunterBtn = new Button("hunter view");
        Button victoryTest = new Button("Test fin de partie");

        Button backBtn = new Button("Retour");

        hunterBtn.setOnAction(e -> displayHunterView(observable.getBoard().getCells()));

        victoryTest.setOnAction(e -> displayVictory(observable.getMonster()));

        VBox vbox = new VBox();

        vbox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(hunterBtn, backBtn, victoryTest);
        root.setCenter(vbox);
    }

    private void hostMenu() {
        viewReset();
        Button playTwoHuman = new Button("Monstre (Joueur) VS Chasseur (Joueur)");
        Button playHumanIA = new Button("Monstre (Joueur) VS Chasseur (IA)");
        Button playIAHuman = new Button("Monstre (IA) VS Chasseur (Joueur)");
        Button playTwoIA = new Button("Monstre (IA) VS Chasseur (IA)");

        Button debug = new Button("Debug Mode");
        Button backBtn = new Button("Retour");
        leave = false;

        debug.setOnAction(e -> debugMenu());
        playTwoHuman.setOnAction(e -> gameStartTwoHuman());
        playHumanIA.setOnAction(e -> gameStartHumanIA());
        playIAHuman.setOnAction(e -> gameStartIAHuman());
        playTwoIA.setOnAction(e -> gameStartTwoIA());
        backBtn.setOnAction(e -> mainMenu());

        VBox vbox = new VBox();

        vbox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(playTwoHuman, playHumanIA, playIAHuman, playTwoIA, debug, backBtn);
        root.setCenter(vbox);
    }
    public void configMenu() {
        viewReset();

        Label textWallProba = new Label("Probabilité d'une case d'être un mur : ");
        Slider wallProba = new Slider(0.0, 1.0, 0.2);
        Label textHeight = new Label("Lignes : ");
        TextField height = new TextField("" + this.observable.getBoard().getCells().length);
        Label textWeight = new Label("Colonnes : ");
        TextField weight = new TextField("" + this.observable.getBoard().getCells()[0].length);
        Label textFog = new Label("Distance de vision : ");
        TextField fogInfo = new TextField("" + this.fogRange);
        CheckBox fogRule = new CheckBox();
        fogRule.setSelected(this.fog);
        Label lb = new Label("Brouillard de guerre");

        textWallProba.setTextFill(Color.WHITE);
        textHeight.setTextFill(Color.WHITE);
        textWeight.setTextFill(Color.WHITE);
        textFog.setTextFill(Color.WHITE);
        lb.setTextFill(Color.WHITE);

        Button btnValide = new Button("Valider");
        Button btnLeave = new Button("Quitter");

        VBox vb = new VBox(10);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(
                textWallProba, wallProba,
                new HBox(10, textHeight, height),
                new HBox(10, textWeight, weight),
                new HBox(10, textFog, fogInfo),
                new HBox(10, lb, fogRule),
                btnValide, btnLeave
        );

        btnLeave.setOnAction(e -> mainMenu());
        btnValide.setOnAction(e -> handleValidation(height.getText(), weight.getText(), wallProba.getValue(), Integer.parseInt(fogInfo.getText()), fogRule.isSelected()));

        root.setCenter(vb);
    }
    private void handleValidation(String heightInput, String weightInput, double wallProbaValue, int newFogRange, boolean FogRule) {
        int rows = validateInput(heightInput, this.observable.getBoard().getCells().length);
        int cols = validateInput(weightInput, this.observable.getBoard().getCells()[0].length);
        this.fogRange = newFogRange;
        this.fog = FogRule;

        if (rows > 0 && cols > 0) {
            this.observable.reset(new Maze(rows, cols, wallProbaValue));
            mainMenu();
        }
    }
    private int validateInput(String input, int defaultValue) {
        try {
            return input.isEmpty() ? defaultValue : Math.max(1, Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    private  void gameStartTwoHuman(){
        observable.setHunter(new HumanHunterStrategy(observable));
        observable.setMonster(new HumanMonsterStrategy(observable));
        observable.getControler().start();
    }
    private  void gameStartIAHuman(){
        observable.setHunter(new HumanHunterStrategy(observable));
        observable.setMonster(new IaMonsterStrategy(observable));
        observable.getControler().start();
    }
    private  void gameStartHumanIA(){
        observable.setHunter(new IaHunterStrategy(observable));
        observable.setMonster(new HumanMonsterStrategy(observable));
        observable.getControler().start();
    }

    private  void gameStartTwoIA(){
        observable.setHunter(new IaHunterStrategy(observable));
        observable.setMonster(new IaMonsterStrategy(observable));
        observable.getControler().start();
    }

    public void viewReset() {
        root.setCenter(null);
        root.setLeft(null);
        root.setBottom(null);
        root.setRight(null);
        root.setTop(null);
        root.setStyle("-fx-background-color: #222222;");
    }

    public void displayMonsterView(boolean[][] view) {
        viewReset();
        initializeGameInterface();
        createMazeMonsterGrid(view);
        setupButtons();
    }

    public void displayHunterView(ICellEvent[][] view) {
        viewReset();
        initializeGameInterface();
        createMazeHunterGrid(view);
        setupButtons();
    }
    private void createMazeHunterGrid(ICellEvent[][] view) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(0);
        gridPane.setHgap(0);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setMaxHeight(500);
        gridPane.setMaxWidth(500);

        for (int col = 0; col < view[0].length; col++) {
            gridPane.add(createIndexLabel((char)('A' + col)), col + 1, 0);
        }

        for (int row = 0; row < view.length ; row++) {
            for (int col = 0; col < view[row].length; col++) {
                MazeCellPane  cellPane = new MazeCellPane(view[row][col].getState(),new Coordinate(col,row));
                cellPane.setMinSize(30, 30);
                cellPane.setMaxSize(30, 30);
                Text nbTour = createTextForCell(view[row][col].getTurn());

                StackPane group = createStackPane(cellPane, nbTour);
                gridPane.add(group, 1 + col, row + 1);
            }
            gridPane.add(createIndexLabel(row), 0, row + 1);
        }

        root.setCenter(gridPane);
    }

    private Text createTextForCell(int turn) {
        Text nbTour = new Text("");
        if (turn != -1) {
            nbTour = new Text("" + turn);
        }
        return nbTour;
    }

    private StackPane createStackPane(MazeCellPane cellPane, Text nbTour) {
        StackPane group = new StackPane();
        group.getChildren().addAll(cellPane, nbTour);

        group.setOnMouseClicked(event -> {
            if (previousSelection != null) {
                previousSelection.refreshCellState();
            }
            String coord;
            ICoordinate id = cellPane.getCoordinate();
            int rowx = id.getRow();
            int coly = id.getCol();
            char lettre = (char) ('A' + coly);
            coord = "" + lettre + rowx;
            View.textField.setText(coord);
            previousSelection = cellPane;
            cellPane.getCellShape().setFill(Color.YELLOW);
        });

        return group;
    }

    private void initializeGameInterface() {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(0);
        gridPane.setHgap(0);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setMaxHeight(500);
        gridPane.setMaxWidth(500);

        TextField textArea = createTextField();
        gridPane.getChildren().addAll(textArea);

        root.setCenter(gridPane);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Button leaveBtn = new Button("Quitter la partie");
        leaveBtn.setOnMouseClicked(e -> {
            leave = true;
            mainMenu();
        });
        vbox.getChildren().addAll(leaveBtn, titleLabel("Au tour du monstre."));
        root.setTop(vbox);

        Button submit = createSubmitButton();
        TextField inputField = createTextField();

        HBox hbox = new HBox();
        hbox.getChildren().addAll(inputField, submit);
        hbox.setAlignment(Pos.CENTER);
        root.setBottom(hbox);
    }

    private void initializeIAGameInterface() {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(0);
        gridPane.setHgap(0);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setMaxHeight(500);
        gridPane.setMaxWidth(500);


        root.setCenter(gridPane);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Button leaveBtn = new Button("Quitter la partie");
        leaveBtn.setOnMouseClicked(e -> {
            leave = true;
            mainMenu();
        });
        vbox.getChildren().addAll(leaveBtn, titleLabel("Au tour du monstre."));
        root.setTop(vbox);
    }

    private void createMazeMonsterGrid(boolean[][] view) {
        GridPane gridPane = (GridPane) root.getCenter();
        for (int col = 0; col < view[0].length; col++) {
            gridPane.add(createIndexLabel((char) ('A' + col)), col + 1, 0);
        }

        for (int row = 0; row < view.length; row++) {
            for (int col = 0; col < view[row].length; col++) {

                int rowDiff = Math.abs(observable.getBoard().getMonsterCo().getRow() - row);
                int colDiff = Math.abs(observable.getBoard().getMonsterCo().getCol() - col);

                if ( fog){
                    MazeCellPane cellPane;
                    if( (rowDiff < fogRange) && rowDiff > -fogRange && (colDiff < fogRange) && colDiff > -fogRange && (colDiff + rowDiff < fogRange)){
                        cellPane = createCellPane(view, row, col);
                    } else {
                        cellPane = new MazeCellPane(null, new Coordinate(col, row));

                    }
                    gridPane.add(cellPane, 1 + col, row + 1);
                }else {
                    MazeCellPane cellPane = createCellPane(view, row, col);
                    gridPane.add(cellPane, 1 + col, row + 1);
                }

            }
            gridPane.add(createIndexLabel(row), 0, row + 1);
        }
    }

    private MazeCellPane createCellPane(boolean[][] view, int row, int col) {

        CellInfo state;
        ICoordinate coordinate = new Coordinate(col, row);

        if (observable.getBoard().getHunterCo().equals(coordinate)) {
            state = CellInfo.HUNTER;
        } else if (observable.getBoard().getMonsterCo().equals(coordinate)) {
            state = CellInfo.MONSTER;
        } else if (observable.getBoard().getExitCo().equals(coordinate)) {
            state = CellInfo.EXIT;
        } else {
            state = view[row][col] ? observable.getBoard().getCells()[row][col].getState() : CellInfo.EMPTY;
        }

        MazeCellPane cellPane = new MazeCellPane(state, new Coordinate(col, row));
        cellPane.setOnMouseClicked(e -> {
            String cord = (char) (cellPane.getCoordinate().getCol() + 'A') + "" + cellPane.getCoordinate().getRow();
            textField.setText(cord);
        });
        return cellPane;
    }

    private void setupButtons() {
        Button leaveBtn = new Button("Quitter la partie");
        leaveBtn.setOnMouseClicked(e -> {
            leave = true;
            mainMenu();
        });

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(leaveBtn, titleLabel("Au tour du monstre."));
        root.setTop(vbox);

        Button submit = createSubmitButton();
        TextField textArea = createTextField();

        HBox hbox = new HBox();
        hbox.getChildren().addAll(textArea, submit);
        hbox.setAlignment(Pos.CENTER);
        root.setBottom(hbox);
    }

    private void setupIAButtons() {
        Button leaveBtn = new Button("Quitter la partie");
        leaveBtn.setOnMouseClicked(e -> {
            leave = true;
            mainMenu();
        });

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(leaveBtn, titleLabel("Au tour du monstre."));
        root.setTop(vbox);
    }

    private Button createSubmitButton() {
        Button submit = new Button("Valider les coordonées");
        submit.setDefaultButton(true);
        submit.setOnAction(e -> handleCoordinateValidation());
        return submit;
    }

    private TextField createTextField() {
        TextField textArea = textField;
        textArea.setText("Saisir une case comme ceci : H5");
        textArea.setPrefWidth(500);
        return textArea;
    }

    private void handleCoordinateValidation() {
        TextField textArea = (TextField) ((HBox) root.getBottom()).getChildren().get(0);
        String inputText = textArea.getText().toUpperCase();

        if (observable.isMonsterTurn()) {
            handleMonsterAction(inputText);
        } else {
            handleHunterAction(inputText);
        }
    }

    private void handleMonsterAction(String inputText) {
        if (inputText.length() >= 2) {
            int col = inputText.charAt(0) - 'A';
            int row = (inputText.length() == 2) ? inputText.charAt(1) - '0' : Integer.parseInt(inputText.substring(1, 3));

            ICellEvent choice = new Cell(CellInfo.MONSTER, this.observable.getTurn(), new Coordinate(col, row));
            observable.updateMonster(choice);
        }
    }

    private void handleHunterAction(String inputText) {
        if (inputText.isEmpty() || inputText.length() > 3) {
            // Si les coordonnées saisies sont vides ou incorrectes, passer le tour du chasseur
            ICellEvent choice = new Cell(CellInfo.HUNTER, this.observable.getTurn(), null);
            observable.updateHunter(choice);
        } else if (inputText.length() >= 2) {
            int col = inputText.charAt(0) - 'A';
            int row = (inputText.length() == 2) ? inputText.charAt(1) - '0' : Integer.parseInt(inputText.substring(1, 3));

            ICellEvent choice = new Cell(CellInfo.HUNTER, this.observable.getTurn(), new Coordinate(col, row));
            observable.updateHunter(choice);
        }
    }

    public static Label titleLabel(String title) {
        Label label = new Label(title);
        label.setStyle("-fx-font-size: 18px;" + "-fx-text-fill: white;");
        return label;
    }

    public static Label createIndexLabel(int number) {
        Label index = new Label("" + number);
        index.setMinSize(30, 30);
        index.setMaxSize(30, 30);
        index.setAlignment(Pos.CENTER);
        index.setStyle("-fx-text-fill: white;");
        return index;
    }
    public static Label createIndexLabel(char lettre) {
        Label index = new Label("" + lettre);
        index.setMinSize(30, 30);
        index.setMaxSize(30, 30);
        index.setAlignment(Pos.CENTER);
        index.setStyle("-fx-text-fill: white;");
        return index;
    }

    public void displayVictory(IStrategy victor) {
        viewReset();
        Button leaveBtn = new Button("Retour au menu principal");
        leaveBtn.setOnAction(e -> {
            try {
                this.observable.reset(new Maze());
                mainMenu();
            }   catch (Exception exception) {
                System.err.println(exception.getMessage());
            }
        });

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(leaveBtn, titleLabel("Victoire du " + victor.toString()));
        root.setCenter(vbox);
    }

    public void displayIAMonsterView(boolean[][] view) {
        viewReset();
        initializeIAGameInterface();
        createMazeMonsterGrid(view);
        setupIAButtons();

        delayedUpdate();
    }



    private void delayedUpdate() {
        delay(500, () -> {
            ICoordinate monsterCoordinate = observable.getBoard().getMonsterCo();
            observable.updateMonster(observable.getBoard().get(monsterCoordinate));
        });
    }


    public void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(millis);
                }
                catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
                return null;
            }
        };

        if (!this.leave){
            sleeper.setOnSucceeded(event -> continuation.run());
            new Thread(sleeper).start();
        } else {
            mainMenu();
        }
    }
}