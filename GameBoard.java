import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.ArrayList;
import java.util.List;


public class GameBoard extends World {

    // controls
    private static final int UP = 0;
    private static final int RIGHT = 1;
    private static final int DOWN = 2;
    private static final int LEFT = 3;

    // button vals
    private final int[] buttonCoords = {196, 280, 312, 319};

    private double spawnModifier = 0.9;

    // background & grid vars
    private static final GreenfootImage background = new GreenfootImage("images/2048_lazy.png");
    private final int paddingSize = 15;
    private final int tileSize = 106;
    public final int[] tileSlots = {4, 4};
    private final int center = paddingSize + tileSize / 2;

    // instance vars
    private ArrayList<GameSquare> movers = new ArrayList<GameSquare>();
    private int lastDir;
    private boolean placeBlock = false;
    private boolean gameOver = false;
    private boolean goodToGo = false;
    private int score = 0;


    // Constructor
    public GameBoard() {
        // create new world from grid image
        super(background.getWidth(), background.getHeight(), 1);
        this.setBackground(background);


        // start with two 2's on the board
        double temp = spawnModifier;
        spawnModifier = 1;
        for (int i = 0; i < 2; i++) {
            placeRandomBlock();
        }
        spawnModifier = temp;
    }


    // place random block on board
    private void placeRandomBlock() {
        // get list of empty slots
        ArrayList<int[]> locations = new
                ArrayList<int[]>();
        for (int i = 0; i < this.tileSlots[0]; i++) {
            for (int j = 0; j < this.tileSlots[1]; j++) {
                int[] coords = slotToCoords(new int[]{i, j});
                if (this.getObjectsAt(coords[0], coords[1], GameSquare.class).isEmpty()) {
                    locations.add(new int[]{i, j});
                }
            }
        }

        // pick random empty slot and spawn new tile
        int[] slot = locations.get((int) (Math.random() * locations.size()));
        int[] coords = slotToCoords(slot);
        this.addObject(new GameSquare((Math.random() > spawnModifier) ? 4 : 2, slot), coords[0], coords[1]);
    }

    // get all tiles who are going to slide
    private ArrayList<GameSquare> getMovers(int direction) {
        // setting inner loop based on direction
        int jStart = (direction == UP || direction == LEFT) ? 0 : this.tileSlots[1] - 1;
        int jEnd = (direction == UP || direction == LEFT) ? this.tileSlots[1] : -1;
        int jInc = (direction == UP || direction == LEFT) ? 1 : -1;

        // init list of tiles who need to move
        ArrayList<GameSquare> movers = new
                ArrayList<GameSquare>();

        for (int i = 0; i != this.tileSlots[0]; i++) {
            for (int j = jStart; j != jEnd; j += jInc) {

                // get coords for the object at tile current tile pos
                int[] coords = slotToCoords(new int[]{i, j});

                // get tile at those coords
                // If statement to switch i and j we are going column by column instead of row by row
                List tileList = (direction % 2 == 0) ? getObjectsAt(coords[0], coords[1], GameSquare.class) :
                        getObjectsAt(coords[1], coords[0], GameSquare.class);

                if (tileList.size() == 1) {
                    // move first (and only) entry
                    GameSquare tile = (GameSquare) tileList.get(0);

                    // try to move tile, add it to mover if successful
                    if (tile.canMove(direction)) {
                        movers.add(tile);
                    }
                }
            }
        }
        return movers; // return list of tiles to move

    }

    // convert slot coordinate to pixel coordinate
    public int[] slotToCoords(int[] slot) {
        return new int[]{slot[0] * (tileSize + paddingSize) + center, slot[1] * (tileSize + paddingSize) + center};
    }

    public void addScore(int score) {
        this.score += score;
    }

    // loops
    public void act() {
        if (!gameOver) {
            // get key presses
            String key = Greenfoot.getKey();

            // if there are tiles to move, move them and update list
            if (movers.size() > 0) {
                GameSquare[] moversArr = movers.toArray(new GameSquare[0]);
                goodToGo = false;
                for (GameSquare tile : moversArr) {
                    if (tile.inSlot()) {
                        boolean tileMoved = tile.canMove(lastDir);
                        if (tileMoved) {
                            tile.setSlot(tile.getNextSlot(lastDir)); // get next slot coord
                        } else if (tile.isMerging()) {
                            tile.dissolve();
                            movers.remove(tile);
                            continue;
                        } else {
                            movers.remove(tile);
                            continue;
                        }
                    }
                    tile.moveTile(lastDir);
                }

                placeBlock = true; // place a block once things have finished moving
                return;
            } else if (placeBlock) {
                //place random block
                placeRandomBlock();
                placeBlock = false;
            }

            // check if all slots are full and there are no valid moves
            // if so it's game over
            if (getObjects(GameSquare.class).size() == 16 && !goodToGo) {
                if (getMovers(UP).size() == 0 && getMovers(DOWN).size() == 0 &&
                        getMovers(LEFT).size() == 0 && getMovers(RIGHT).size() == 0) {
                    gameOver = true;
                    addObject(new GameOverScreen(score), 250, 250); // draw game over screen
                } else {
                    goodToGo = true; // only check once per move
                }
            }

            //If a key was pressed...do something
            if (key != null) {
                // get which tiles need to move in whichever direction user picked
                switch (key) {
                    case "up":
                        movers = getMovers(UP);
                        lastDir = UP;
                        break;
                    case "right":
                        movers = getMovers(RIGHT);
                        lastDir = RIGHT;
                        break;
                    case "down":
                        movers = getMovers(DOWN);
                        lastDir = DOWN;
                        break;
                    case "left":
                        movers = getMovers(LEFT);
                        lastDir = LEFT;
                        break;

                }

            }
        } else {
            // check if player is clicking on the mouse
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null) {
                boolean insideButton = mouse.getX() > buttonCoords[0] &&
                        mouse.getX() < buttonCoords[2] &&
                        mouse.getY() > buttonCoords[1] &&
                        mouse.getY() < buttonCoords[3];
                if (mouse.getButton() != 0 && insideButton) {
                    Greenfoot.setWorld(new GameBoard()); // restart the game
                }
            }
        }
    }
}
