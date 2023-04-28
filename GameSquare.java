import ToolBox.Nums;

import java.util.Arrays;
import java.util.List;

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


/**
 * Write a description of class GameSquare here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class GameSquare extends Actor {
    // CONSTANTS
    private static final int UP = 0;
    private static final int RIGHT = 1;
    private static final int DOWN = 2;
    private static final int LEFT = 3;
    private static final int SPEED = 60;

    // images
    private static final GreenfootImage[] TILES = {
            new GreenfootImage("images/2048_2.png"),
            new GreenfootImage("images/2048_4.png"),
            new GreenfootImage("images/2048_8.png"),
            new GreenfootImage("images/2048_16.png"),
            new GreenfootImage("images/2048_32.png"),
            new GreenfootImage("images/2048_64.png"),
            new GreenfootImage("images/2048_128.png"),
            new GreenfootImage("images/2048_256.png"),
            new GreenfootImage("images/2048_512.png"),
            new GreenfootImage("images/2048_1024.png"),
            new GreenfootImage("images/2048_2048.png"),
    };

    //Instance Variables
    private int value;
    private int[] slot;
    private int velocity = SPEED;
    private boolean merging = false;
    private boolean moving = false;
    private boolean merged = false;


    // Constructor
    public GameSquare(int value, int[] slot) {
        this.value = value;
        this.slot = slot;
        displayValue();
    }

    public boolean canMove(int direction) {
        GameBoard world = (GameBoard) getWorld();
        setVelocity(direction); // set animation velocity based on direction
        merged = false; // reset merged when checking if it can move

        // only check if it can move if it's in a slot
        if (inSlot()) {

            // if tile at border
            if (atBorder(direction)) {
                moving = false;
                return false; // can't move anymore
            }

            // get tiles at next slot
            int[] coordsNS = world.slotToCoords(getNextSlot(direction));
            List tileList = world.getObjectsAt(coordsNS[0], coordsNS[1], GameSquare.class);
            // if other tile is there
            if (!tileList.isEmpty()) {
                GameSquare otherTile = ((GameSquare) (tileList.get(0)));
                if (otherTile.getValue() == value) { // other tile has same value

                    // determining whether to move
                    if (otherTile.hasMerged()) { // can't move
                        moving = false;
                    } else { // will move, might merge
                        moving = true;
                        if (!otherTile.isMoving()) { // will merge
                            setMerging();
                        }
                    }

                } else { // might move
                    moving = otherTile.isMoving();
                }
            } else { // will move
                moving = true;
            }
        }
        return moving;
    }

    public void moveTile(int direction) {
        GameBoard world = (GameBoard) getWorld();

        // slide tile & display
        int x = (direction % 2 != 0) ? getX() + velocity : getX();
        int y = (direction % 2 == 0) ? getY() + velocity : getY();

        int[] desired_coords = world.slotToCoords(this.slot);

        // fixing overshoot
        if (direction == UP || direction == LEFT) {
            setLocation(Math.max(x, desired_coords[0]), Math.max(y, desired_coords[1]));
        } else {
            setLocation(Math.min(x, desired_coords[0]), Math.min(y, desired_coords[1]));
        }
        displayValue();
    }

    public void dissolve() {
        GameBoard world = (GameBoard) getWorld();
        int[] currentCoords = {getX(), getY()};

        // merge it and delete it
        int tileVal = this.value;
        world.removeObject(this);
        List tileList = world.getObjectsAt(currentCoords[0], currentCoords[1], GameSquare.class);
        if (!tileList.isEmpty()) {
            GameSquare otherTile = ((GameSquare) (tileList.get(0)));
            if (otherTile.merge(tileVal)) {
                world.addScore(tileVal * 2);
            }
        }
    }

    public boolean merge(int mergeVal) {
        GameBoard world = (GameBoard) getWorld();
        // merge values if haven't merged this move and values are the same
        if (mergeVal == value && !merged) {
            value += mergeVal;
            merged = true; // can only merge once per move
            if (value == 4096) { // remove tile once it goes past 2048
                world.removeObject(this);
            } else {
                this.displayValue();
            }
            return true;
        }
        return false;
    }

    // Getters
    public int getValue() {
        return this.value;
    }

    public int[] getNextSlot(int direction) {
        return nextSlot(direction);
    }

    public boolean isMerging() {
        return this.merging;
    }

    public boolean hasMerged() {
        return this.merged;
    }

    public boolean isMoving() {
        return this.moving;
    }

    public boolean inSlot() {
        GameBoard world = (GameBoard) getWorld();
        return Arrays.equals(world.slotToCoords(this.slot), new int[]{getX(), getY()});
    }

    // Setters
    private void setVelocity(int direction) {
        velocity = (direction == DOWN || direction == RIGHT) ? SPEED : -SPEED; // set direction
    }

    public void setMerging() {
        this.merging = true; // cannot be made false from outside
    }

    public void setSlot(int[] newSlot) {
        this.slot = newSlot;
    }

    public boolean atBorder(int direction) {
        int[] nextSlot = nextSlot(direction); // get the next slot to move to

        return Arrays.equals(this.slot, nextSlot); // check if next slot is current slot (tile at border)
    }

    private int[] nextSlot(int direction) {
        GameBoard world = (GameBoard) getWorld();

        // get current slot pos
        int[] currentSlot = this.slot;

        // init next slot position
        int[] nextSlot = {0, 0};

        // constrain next slot to border limits of grid
        if (direction % 2 == 0) { // tile going up or down
            nextSlot[0] = currentSlot[0];
            nextSlot[1] = (int) Nums.constrain(currentSlot[1] + ((direction == DOWN) ? 1 : -1), 0, world.tileSlots[1] - 1);
        } else { // tile going left or right
            nextSlot[0] = (int) Nums.constrain(currentSlot[0] + ((direction == RIGHT) ? 1 : -1), 0, world.tileSlots[0] - 1);
            nextSlot[1] = currentSlot[1];
        }
        return nextSlot;
    }

    public void displayValue() {

        //set square image to tile picture based on value
        setImage(TILES[(int) (Math.log(this.value) / Math.log(2)) - 1]);

    }
}
