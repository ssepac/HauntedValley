package components.sprites;

import config.StaticValues;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import util.CollisionDetection;
import util.DirectionEnum;

/**
 * The sprite that the user is controlling with their keyboard.
 */
public class ControlledSprite extends Sprite {

    public ControlledSprite(ImageView imageView,
                            Image image,
                            int columns,
                            int rows,
                            int totalFrames,
                            int frameWidth,
                            int frameHeight,
                            float framesPerSecond
    ) {
        super(imageView, image, columns, rows, totalFrames, frameWidth, frameHeight, framesPerSecond);
        coords = new double[]{APP_STATE.getPlayerPos()[0], APP_STATE.getPlayerPos()[1]};
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    /** Changes the animation on key press */
    public javafx.event.EventHandler<? super javafx.scene.input.KeyEvent> onKeyPress = e -> {
        if (e.getCode() == KeyCode.UP) {
            setWalking(true);
            APP_STATE.setSpriteWalking(true);
            setCurrentDirection(DirectionEnum.NORTH);
            APP_STATE.setSpriteDirFacing(DirectionEnum.NORTH);
            setSpritesheetRow(1, 4, 400, 599);
        }
        if (e.getCode() == KeyCode.DOWN) {
            setWalking(true);
            APP_STATE.setSpriteWalking(true);
            setCurrentDirection(DirectionEnum.SOUTH);
            APP_STATE.setSpriteDirFacing(DirectionEnum.SOUTH);
            setSpritesheetRow(0, 4, 400, 599);
        }
        if (e.getCode() == KeyCode.LEFT) {
            setWalking(true);
            APP_STATE.setSpriteWalking(true);
            setCurrentDirection(DirectionEnum.WEST);
            APP_STATE.setSpriteDirFacing(DirectionEnum.WEST);
            setSpritesheetRow(2, 4, 400, 599);
        }
        if (e.getCode() == KeyCode.RIGHT) {
            setWalking(true);
            APP_STATE.setSpriteWalking(true);
            setCurrentDirection(DirectionEnum.EAST);
            APP_STATE.setSpriteDirFacing(DirectionEnum.EAST);
            setSpritesheetRow(3, 4, 400, 599);
        }
    };

    public javafx.event.EventHandler<? super javafx.scene.input.KeyEvent> onKeyReleased = e -> {
        setWalking(false);
        APP_STATE.setSpriteWalking(false);
    };

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
            if(CollisionDetection.isPlayerWithinBounds(currentDirection, coords, APP_STATE.getCollisionPoints())){
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
            APP_STATE.setPlayerPos(coords);
        }
    }
}