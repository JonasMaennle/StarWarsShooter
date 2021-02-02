package pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import core.GameScreen;
import objects.GameEntity;
import objects.ants.AntAbstract;
import objects.ants.task.WalkToTask;

import java.text.spi.CollatorProvider;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static helper.Const.TILE_HEIGHT;
import static helper.Functions.*;


public class Pathfinder extends Thread{

    private Graph graph;
    private GameScreen gameScreen;
    private long timer;
    private boolean running;
    private WalkToTask walkToTask;
    private Node blockedNode;

    public Pathfinder(Graph graph, GameScreen gameScreen, WalkToTask walkToTask) {
        this.graph = graph;
        this.gameScreen = gameScreen;
        this.running = true;
        this.walkToTask = walkToTask;
    }

    public Pathfinder(Graph graph, GameScreen gameScreen, WalkToTask walkToTask, Node blockedNode) {
        this(graph, gameScreen, walkToTask);
        this.blockedNode = blockedNode;
    }


    @Override
    public void run() {
        Vector2 start = transformCoordinatesToIso(new Vector2(walkToTask.getOwner().getX(), walkToTask.getOwner().getY()), gameScreen.getMapWidth(), gameScreen.getMapHeight());
        walkToTask.setPath(getPath(start.x, start.y, walkToTask.getxTarget(), walkToTask.getyTarget(),blockedNode));
    }

    public LinkedList<Node> getPath(float xStart, float yStart, float xTarget, float yTarget, Node blockedNode) {
        LinkedList<Node> tempPath = null;
        // remove node
        if(blockedNode != null) {
            graph.removeNode(blockedNode);
            graph.createMatrix();
        }

        try {
            Vector2 start =  transformCoordinatesToGrid(xStart, yStart, gameScreen.getMapWidth(), gameScreen.getMapHeight());
            Vector2 end = transformCoordinatesToGrid(xTarget, yTarget, gameScreen.getMapWidth(), gameScreen.getMapHeight());
            // System.out.println(start + ":" + end);
            if(start == null || end == null) {
                return null;
            }

            int enemyX = (int) start.x;
            int enemyY = (int) start.y;
            int playerX = (int) end.x;
            int playerY = (int) end.y;

            // System.out.println(enemyX + "  " + enemyY + "    :     " + playerX + "  " + playerY);
            if(graph.getNodeID(enemyX, enemyY) != -1 && graph.getNodeID(playerX, playerY) != -1) {
                timer = System.currentTimeMillis();
                tempPath = graph.astar(graph.getNodeID(enemyX, enemyY), graph.getNodeID(playerX, playerY));
                // System.out.println("Graph found in " + (System.currentTimeMillis() - timer) + "ms");
                // tempPath.remove(tempPath.size() - 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // add node again
        if(blockedNode != null) {
            graph.addNode(blockedNode);
            graph.createMatrix();
        }

        return tempPath;
    }
}
