import components.maps.Map;
import components.sprites.Sprite;
import config.StaticValues;
import controls.ActivityPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import networking.Account;
import networking.NetworkAdapter;
import org.web3j.abi.datatypes.Address;
import state.AppState;

import static config.StaticValues.df;

public class Main extends Application {

    private final AppState appState;
    public Main(){
        super();
        appState = AppState.getInstance();
    }

    @Override
    public void start(Stage stage) {

        try {

            //TODO: Refactor start method
            //TODO: Make login screen which interfaces with a smart contract
            appState.setAccount(new Account(new Address("0x97E1B0d15a11912959092635BE9803EDb2398EC3")));

            NetworkAdapter networkAdapter = NetworkAdapter.getInstance();
            networkAdapter.init("0.0.0.0", 41234, 1000,1);

            int mapN = 32*StaticValues.TILE_SIZE;
            double[] spriteStartCoords = new double[]{mapN/2d, mapN/2d};
            appState.setPlayerPos(spriteStartCoords);
            Map tileMap = new Map(mapN, mapN, "src/main/resources/maps/spookyworldmap.tmx");
            tileMap.start();

            appState.setCollisionPoints(tileMap.getCollisionPoints());

            ImageView spriteView = new ImageView();
            spriteView.setFitHeight(StaticValues.SPRITE_HEIGHT);
            spriteView.setFitWidth(StaticValues.SPRITE_WIDTH);

            Sprite sprite = new Sprite(
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

            StackPane.setAlignment(spriteView, Pos.CENTER);
            StackPane.setAlignment(coordsLabel, Pos.BOTTOM_RIGHT);
            StackPane.setAlignment(activityPane, Pos.BOTTOM_CENTER);

            //Creating a scene object
            Scene scene = new Scene(root, StaticValues.APP_WIDTH, StaticValues.APP_HEIGHT);

            scene.setOnKeyPressed(sprite.onKeyPress);
            scene.setOnKeyReleased(sprite.onKeyReleased);

            //Setting title to the Stage
            stage.setTitle(StaticValues.APP_NAME);

            //Adding scene to the stage
            stage.setScene(scene);

            //Displaying the contents of the stage
            stage.show();

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
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}