package components.sprites;

import config.StaticValues;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import networking.NetworkAdapter;
import state.AppState;
import util.CollisionDetection;
import util.DirectionEnum;

//based on https://stackoverflow.com/a/22466969/5354268
public class Sprite extends AnimationTimer {

    private final ImageView imageView; //Image view that will display our sprite

    private final int totalFrames; //Total number of frames in the sequence
    private final float fps; //frames per second I.E. 24

    private int cols; //Number of columns on the sprite sheet
    private final int rows; //Number of rows on the sprite sheet

    private int frameWidth; //Width of an individual frame
    private int frameHeight; //Height of an individual frame

    private int currentCol = 0;
    private int currentRow = 0;

    private long lastFrame = 0;

    //movement variables
    private boolean isWalking = false;
    private DirectionEnum currentDirection = DirectionEnum.SOUTH;
    private double[] coords;
    private final static AppState appState = AppState.getInstance();

    public Sprite(ImageView imageView,
                  Image image,
                  int columns,
                  int rows,
                  int totalFrames,
                  int frameWidth,
                  int frameHeight,
                  float framesPerSecond
    ) {
        this.imageView = imageView;
        imageView.setImage(image);
        imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));

        cols = columns;
        this.rows = rows;
        this.totalFrames = totalFrames;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        fps = framesPerSecond;
        lastFrame = System.nanoTime();

        coords = new double[]{appState.getPlayerPos()[0], appState.getPlayerPos()[1]};
    }

    public double[] getCoords() {
        return coords;
    }

    public void setCurrentDirection(DirectionEnum currentDirection) {
        this.currentDirection = currentDirection;
    }

    public void setSpritesheetRow(int rowNumber, int numCols, int frameWidth, int frameHeight){
        currentRow = rowNumber;
        cols = numCols;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public javafx.event.EventHandler<? super javafx.scene.input.KeyEvent> onKeyPress = e -> {
        if (e.getCode() == KeyCode.UP) {
            setWalking(true);
            setCurrentDirection(DirectionEnum.NORTH);
            setSpritesheetRow(1, 4, 400, 599);
        }
        if (e.getCode() == KeyCode.DOWN) {
            setWalking(true);
            setCurrentDirection(DirectionEnum.SOUTH);
            setSpritesheetRow(0, 4, 400, 599);
        }
        if (e.getCode() == KeyCode.LEFT) {
            setWalking(true);
            setCurrentDirection(DirectionEnum.WEST);
            setSpritesheetRow(2, 4, 400, 599);
        }
        if (e.getCode() == KeyCode.RIGHT) {
            setWalking(true);
            setCurrentDirection(DirectionEnum.EAST);
            setSpritesheetRow(3, 4, 400, 599);
        }
    };

    public javafx.event.EventHandler<? super javafx.scene.input.KeyEvent> onKeyReleased = e -> setWalking(false);

    @Override
    public void handle(long now) {
        handleAnimation(now);
        handleState(now);
    }

    private void handleState(long now){
        //getDir
        if(isWalking) {

            double difSec = (now - lastFrame) / 1000000000.0;

            //Collision detection with edges of map.
            if(CollisionDetection.isPlayerWithinBounds(currentDirection, coords, appState.getCollisionPoints())){
                setWalking(false);
                return;
            }

            switch (currentDirection) {
                case NORTH -> coords[1] -= (difSec * StaticValues.WALK_SPEED);
                case SOUTH -> coords[1] += (difSec * StaticValues.WALK_SPEED);
                case EAST -> coords[0] += (difSec * StaticValues.WALK_SPEED);
                case WEST -> coords[0] -= (difSec * StaticValues.WALK_SPEED);
                default -> {
                }
            }
            appState.setPlayerPos(coords);
        }
    }

    /**
     * Delegated responsibility to update sprite visually from handle method.
     * @param now
     */
    private void handleAnimation(long now){
        int frameJump = (int) Math.floor((now - lastFrame) / (1000000000 / fps)); //Determine how many frames we need to advance to maintain frame rate independence

        //Do a bunch of math to determine where the viewport needs to be positioned on the sprite sheet
        if (frameJump >= 1) {
            lastFrame = now;
            int addRows = (int) Math.floor((float) frameJump / (float) cols);
            int frameAdd = frameJump - (addRows * cols);

            if (currentCol + frameAdd >= cols) {
                //currentRow += addRows + 1;
                currentCol = frameAdd - (cols - currentCol);
            } else {
                //currentRow += addRows;
                currentCol += frameAdd;
            }
            //currentRow = (currentRow >= rows) ? currentRow - ((int) Math.floor((float) currentRow / rows) * rows) : currentRow;

            //The last row may or may not contain the full number of columns
            if (isWalking && cols + currentCol >= totalFrames) {
                //currentRow = 0;
                currentCol = Math.abs(currentCol - (totalFrames - (int) (Math.floor((float) totalFrames / cols) * cols)));
            }
            else if(!isWalking){
                currentCol = 0;
            }

            imageView.setViewport(new Rectangle2D(currentCol * frameWidth, currentRow * frameHeight, frameWidth, frameHeight));
        }
    }
}