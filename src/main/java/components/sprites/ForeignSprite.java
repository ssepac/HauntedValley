package components.sprites;

import config.StaticValues;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import networking.PositionMessage;
import networking.ServerPositionBroadcast;
import state.AppState;
import util.CollisionDetection;
import util.DirectionEnum;

import java.util.Arrays;

//based on https://stackoverflow.com/a/22466969/5354268
public class ForeignSprite extends AnimationTimer {

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

    private String foreignSpriteId;
    private ServerPositionBroadcast serverPositionBroadcast;

    //movement variables
    private boolean isWalking;
    private DirectionEnum currentDirection;
    private double[] coords;
    private final static AppState appState = AppState.getInstance();

    public ForeignSprite(ImageView imageView,
                            Image image,
                            int columns,
                            int rows,
                            int totalFrames,
                            int frameWidth,
                            int frameHeight,
                            float framesPerSecond,
                            String foreignSpriteId
    ) {
        this.imageView = imageView;
        imageView.setImage(image);
        imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));

        cols = columns;
        this.rows = rows;
        this.totalFrames = totalFrames;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.foreignSpriteId = foreignSpriteId;
        fps = framesPerSecond;
        lastFrame = System.nanoTime();

        coords = new double[]{appState.getPlayerPos()[0], appState.getPlayerPos()[1]};
        isWalking = false;
        currentDirection=DirectionEnum.SOUTH;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setSpritesheetRow(int rowNumber, int numCols, int frameWidth, int frameHeight){
        currentRow = rowNumber;
        cols = numCols;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    @Override
    public void handle(long now) {
        handleAnimation(now);
        handleState(now);
    }

    private void handleState(long now){
        serverPositionBroadcast = appState.getPlayerServerPosition(foreignSpriteId);
        if(serverPositionBroadcast != null){

            try{
                isWalking = serverPositionBroadcast.isWalking();
                currentDirection = DirectionEnum.valueOf(serverPositionBroadcast.getDirection());
                double foreignX = serverPositionBroadcast.getX();
                double foreignY = serverPositionBroadcast.getY();

                //calculate x and y distance from center

                double deltaX = foreignX - appState.getPlayerPos()[0];
                double deltaY = foreignY - appState.getPlayerPos()[1];

                imageView.setTranslateX(deltaX);
                imageView.setTranslateY(deltaY);

                switch (currentDirection){
                    case NORTH -> setSpritesheetRow(1, 4, 400, 599);
                    case SOUTH -> setSpritesheetRow(0, 4, 400, 599);
                    case WEST -> setSpritesheetRow(2, 4, 400, 599);
                    case EAST -> setSpritesheetRow(3, 4, 400, 599);
                    default -> {}
                }
            }
            catch(Exception ex){
                System.out.println(serverPositionBroadcast.toString());
            }

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

    /** Generates a foreign sprite that is hooked into app state via its id */
    public static ForeignSprite generateForeignSprite(String foreignSpriteId){
        ImageView foreignSpriteView = new ImageView();
        foreignSpriteView.setFitHeight(StaticValues.SPRITE_HEIGHT);
        foreignSpriteView.setFitWidth(StaticValues.SPRITE_WIDTH);

        ForeignSprite foreignSprite = new ForeignSprite(
                foreignSpriteView,
                new Image("/images/spritesheet2.png"),
                4,
                1,
                4,
                400,
                599,
                12,
                foreignSpriteId
        );

        foreignSprite.start();
        return foreignSprite;
    }

}