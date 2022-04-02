package components.sprites;

import config.StaticValues;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import networking.ServerMessage;
import state.AppState;
import util.DirectionEnum;

/**
 * The sprite that is generated for any sprite other than the one the user is controlling with their keyboard.
 */
public class ForeignSprite extends Sprite {

    private final String foreignSpriteId;

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
        super(imageView, image, columns, rows, totalFrames, frameWidth, frameHeight, framesPerSecond);
        this.foreignSpriteId = foreignSpriteId;
    }

    public ImageView getImageView() {
        return imageView;
    }

    @Override
    public void handle(long now) {
        handleAnimation(now);
        handleState(now);
    }

    private void handleState(long now){
        ServerMessage serverPositionBroadcast = APP_STATE.getPlayerServerPosition(foreignSpriteId);
        if(serverPositionBroadcast != null){

            try{
                isWalking = serverPositionBroadcast.isWalking();
                currentDirection = DirectionEnum.valueOf(serverPositionBroadcast.getDirection());
                coords = new double[]{serverPositionBroadcast.getX(), serverPositionBroadcast.getY()};

                //calculate x and y distance from center

                double deltaX = coords[0] - APP_STATE.getPlayerPos()[0];
                double deltaY = coords[1] - APP_STATE.getPlayerPos()[1];

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