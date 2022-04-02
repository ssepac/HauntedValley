package scenes;

import components.maps.Map;
import components.sprites.ControlledSprite;
import config.StaticValues;
import controls.ActivityPane;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import networking.Account;
import networking.NetworkAdapter;
import org.web3j.abi.datatypes.Address;
import state.AppState;
import util.DirectionEnum;

import java.net.SocketException;
import java.net.UnknownHostException;

import static config.StaticValues.df;

public class Game {

    private static final AppState appState = AppState.getInstance();

    public static Scene generateGameScene() throws Exception {

        appState.setAccount(new Account(new Address("0x97E1B0d15a11912959092635BE9803EDb2398EC3")));

        NetworkAdapter networkAdapter = NetworkAdapter.getInstance();
        networkAdapter.init("192.168.0.24", 3000, 1000,15);

        int mapN = 50* StaticValues.TILE_SIZE;
        double[] spriteStartCoords = new double[]{mapN/2d, mapN/2d};
        appState.setPlayerPos(spriteStartCoords);
        appState.setSpriteWalking(false);
        appState.setSpriteDirFacing(DirectionEnum.SOUTH);
        Map tileMap = new Map("map1", mapN, mapN, "src/main/resources/maps/spookyworldmap.tmx");
        tileMap.start();

        appState.setCollisionPoints(tileMap.getCollisionPoints());

        ImageView spriteView = new ImageView();
        spriteView.setFitHeight(StaticValues.SPRITE_HEIGHT);
        spriteView.setFitWidth(StaticValues.SPRITE_WIDTH);

        ControlledSprite sprite = new ControlledSprite(
                spriteView,
                new Image("/images/spritesheet2.png"),
                4,
                1,
                4,
                400,
                599,
                12
        );
        sprite.start();
        //Creating a Group object
        Label coordsLabel = new Label();
        coordsLabel.setPadding(new Insets(16));

        ActivityPane activityPane = new ActivityPane();
        appState.setActivityPane(activityPane);
        appState.dispatchActivityPaneMessage("You wake up with a strange feeling...");

        StackPane root = new StackPane(tileMap.getTileGroup(), spriteView, coordsLabel, activityPane);
        root.setStyle("-fx-background-color: #000");
        appState.setSceneRoot(root);

        StackPane.setAlignment(spriteView, Pos.CENTER);
        StackPane.setAlignment(coordsLabel, Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(activityPane, Pos.BOTTOM_CENTER);

        //Creating a scene object
        Scene scene = new Scene(root, StaticValues.APP_WIDTH, StaticValues.APP_HEIGHT);

        scene.setOnKeyPressed(sprite.onKeyPress);
        scene.setOnKeyReleased(sprite.onKeyReleased);

        //Update UI 30 frames/sec
        new Thread(() -> {
            while (true) {
                try {
                    Platform.runLater(() -> coordsLabel.setText(df.format(appState.getPlayerPos()[0]) + ", " + df.format(appState.getPlayerPos()[1])));
                    Thread.sleep(33);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        networkAdapter.initClientHeartbeat();
        networkAdapter.initServerListener();

        return scene;
    }

}
