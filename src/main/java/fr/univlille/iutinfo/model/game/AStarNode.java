package fr.univlille.iutinfo.model.game;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.model.plateau.Coordinate;
import fr.univlille.iutinfo.model.plateau.Maze;

import java.util.*;

/**
 * Représente un nœud utilisé dans l'algorithme de recherche de chemin A*.
 */
public class AStarNode {

    /** Directions possibles pour se déplacer (droite, gauche, haut, bas). */
    private static final int[][] DIRECTIONS = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    /** Coût de mouvement depuis le point de départ jusqu'à ce nœud. */

    private final int gCost;

    /** Coût heuristique estimé depuis ce nœud jusqu'à la destination. */
    private final int hCost;

    /**
     * Initialise un nœud A* avec les coûts spécifiés.
     *
     * @param gCost Le coût de mouvement depuis le point de départ.
     * @param hCost Le coût heuristique estimé jusqu'à la destination.
     */
    public AStarNode(int gCost, int hCost) {
        this.gCost = gCost;
        this.hCost = hCost;
    }

    /**
     * Renvoie le coût total (fCost) du nœud, égal à la somme du coût de mouvement
     * depuis le point de départ (gCost) et du coût heuristique estimé jusqu'à la destination (hCost).
     *
     * @return Le coût total (fCost) du nœud.
     */
    public int getFCost() {
        return gCost + hCost;
    }

    /**
     * Effectue une recherche de chemin A* sur le plateau spécifié.
     *
     * @param board Le plateau sur lequel effectuer la recherche de chemin.
     * @return Une liste de coordonnées représentant le chemin trouvé.
     */
    public static List<ICoordinate> aStarPathfinding(Maze board) {
        ICoordinate start = board.getMonsterCo();
        ICoordinate goal = board.getExitCo();

        if (start == null || goal == null) {
            return Collections.emptyList();
        }

        List<ICoordinate> openSet = new ArrayList<>();
        List<ICoordinate> closedSet = new ArrayList<>();
        openSet.add(start);

        Map<ICoordinate, ICoordinate> cameFrom = new HashMap<>();

        Map<ICoordinate, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);
        Map<ICoordinate, Integer> fScore = new HashMap<>();
        fScore.put(start, heuristic(start, goal));

        while (!openSet.isEmpty()) {
            ICoordinate current = getLowestFScore(openSet, fScore);
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, goal);
            }

            openSet.remove(current);
            closedSet.add(current);

            for (int[] direction : DIRECTIONS) {
                int x = current.getCol() + direction[0];
                int y = current.getRow() + direction[1];
                ICoordinate neighbor = new Coordinate(x, y);

                if (!isValidMove(board, neighbor) || closedSet.contains(neighbor)) {
                    continue;
                }

                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if (tentativeGScore >= gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    continue;
                }

                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, tentativeGScore + heuristic(neighbor, goal));
            }
        }
        return Collections.emptyList();
    }

    private static ICoordinate getLowestFScore(List<ICoordinate> openSet, Map<ICoordinate, Integer> fScore) {
        ICoordinate lowest = null;
        int lowestFScore = Integer.MAX_VALUE;

        for (ICoordinate coord : openSet) {
            int fScoreValue = fScore.getOrDefault(coord, Integer.MAX_VALUE);
            if (fScoreValue < lowestFScore) {
                lowestFScore = fScoreValue;
                lowest = coord;
            }
        }

        return lowest;
    }

    private static boolean isValidMove(Maze board, ICoordinate coord) {
        int col = coord.getCol();
        int row = coord.getRow();
        int sizeCol = board.getSizeCol();
        int sizeRow = board.getSizeRow();

        boolean withinBounds = col >= 0 && col < sizeCol && row >= 0 && row < sizeRow;
        return withinBounds && board.get(row, col).getState().equals(ICellEvent.CellInfo.EMPTY);
    }

    private static int heuristic(ICoordinate a, ICoordinate b) {
        return Math.abs(a.getCol() - b.getCol()) + Math.abs(a.getRow() - b.getRow());
    }

    private static List<ICoordinate> reconstructPath(Map<ICoordinate, ICoordinate> cameFrom, ICoordinate target) {
        List<ICoordinate> path = new ArrayList<>();
        ICoordinate current = target;
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }
}