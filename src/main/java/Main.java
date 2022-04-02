import components.maps.Map;
import components.sprites.ControlledSprite;
import components.sprites.ForeignSprite;
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
import scenes.Game;
import scenes.Login;
import state.AppState;
import util.DirectionEnum;

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

            //Setting title to the Stage
            stage.setTitle(StaticValues.APP_NAME);

            //Adding scene to the stage
            stage.setScene(Login.generateLoginScreen((account)->{
                try {
                    appState.setAccount(new Account(new Address(account)));
                    stage.setScene(Game.generateGameScene());
                    return null;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return "Invalid account number";
                }
                catch (Exception ex){
                    return "There was an issue logging in";
                }
            }));

            //Displaying the contents of the stage
            stage.show();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}