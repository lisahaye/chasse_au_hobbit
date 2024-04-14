package fr.univlille.iutinfo.view.graphic;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.model.plateau.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.util.HashMap;

public class MazeCellPane extends StackPane {
    private static final String ERROR_PATH = "/images/error.png";
    private static final String IMG_PATH = "/images/";
    private static final HashMap<CellInfo, Image> images = new HashMap<>();
    private static final int CELL_TURN = Cell.DEFAULT_TURN_CELL;
    private static final double CELL_SIZE = 30;
    private final Rectangle shape;
    private final Cell cell;

    static {
        for (CellInfo cellInfo : CellInfo.values()) {
            images.put(cellInfo, loadImage(IMG_PATH + cellInfo.toString().toLowerCase() + ".png"));
        }
    }

    /**
     * Initialise une instance de MazeCellPane avec une cellule donnée.
     * @param cell La cellule associée à la MazeCellPane.
     */
    public MazeCellPane(Cell cell) {
        shape = new Rectangle(CELL_SIZE, CELL_SIZE);
        getChildren().add(shape);
        this.cell = cell;
        ImageView imageView = new ImageView(getImageForState(cell.getState()));
        imageView.setFitWidth(CELL_SIZE);
        imageView.setFitHeight(CELL_SIZE);
        getChildren().add(imageView);
        updateCellDisplay();
    }
    /**
     * Initialise une instance de MazeCellPane avec un état, des coordonnées et un nombre de tours.
     * @param state L'état de la cellule.
     * @param coordinate Les coordonnées de la cellule.
     * @param turn Le nombre de tours.
     */
    public MazeCellPane(CellInfo state, ICoordinate coordinate, int turn) {
        this(new Cell(state, turn, coordinate));
    }

    /**
     * Initialise une instance de MazeCellPane avec un état et des coordonnées.
     * @param state L'état de la cellule.
     * @param coordinate Les coordonnées de la cellule.
     */
    public MazeCellPane(CellInfo state, ICoordinate coordinate) {
        this(state, coordinate, CELL_TURN);
    }


    private static Image loadImage(String imagePath) {
        InputStream inputStream = MazeCellPane.class.getResourceAsStream(imagePath);
        if (inputStream == null) {
            inputStream = MazeCellPane.class.getResourceAsStream(ERROR_PATH);
        }
        assert inputStream != null;
        return new Image(inputStream);
    }

    /**
     * Récupère les coordonnées de la cellule.
     * @return Les coordonnées de la cellule.
     */
    public ICoordinate getCoordinate() {
        return cell.getCoord();
    }

    /**
     * Récupère la forme graphique de la cellule.
     * @return La forme graphique de la cellule.
     */
    public Rectangle getCellShape() {
        return shape;
    }

    /**
     * Définit l'état visuel de la cellule.
     * @param state L'état visuel de la cellule.
     */
    public void setCellState(CellInfo state) {
        if (state == null) {
            shape.setFill(Color.BLACK);
        }
        else {
            switch (state) {
                case WALL:
                    shape.setFill(Color.BLACK);
                    break;
                case EMPTY:
                    shape.setFill(Color.WHITE);
                    break;
                case HUNTER:
                    shape.setFill(Color.GREEN);
                    break;
                case MONSTER:
                    shape.setFill(Color.RED);
                    break;
                case EXIT:
                    shape.setFill(Color.BLUE);
                    break;
                default:
                    shape.setFill(Color.PURPLE);
                    break;
            }
        }
    }

    private Image getImageForState(CellInfo state) {
        return images.get(state);
    }

    /**
     * Récupère l'état visuel de la cellule.
     * @return L'état visuel de la cellule.
     */
    public CellInfo getState() {
        return cell.getState();
    }

    /**
     * Récupère la cellule associée à cette MazeCellPane.
     * @return La cellule associée.
     */
    public Cell getCell() {
        return cell;
    }

    /**
     * Rafraîchit l'état visuel de la cellule.
     */
    public void refreshCellState() {
        setCellState(getState());
    }

    /**
     * Met à jour l'affichage de la cellule en fonction des tours.
     */
    public void updateCellDisplay() {
        if (cell.getTurn() > 0) {
            Text text = new Text(Integer.toString(cell.getTurn()));
            text.setFill(Color.WHITE);
            getChildren().add(text);
        }
    }
}