package scenes;

import config.StaticValues;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Login {

    public static Scene generateLoginScreen(Function<String, String> onLogin){

        Label title = new Label(StaticValues.APP_NAME.toUpperCase());
        title.setFont(new Font(36));
        TextField loginTextField = new TextField();
        loginTextField.setPromptText(StaticValues.USERNAME_TF_HINT);
        Label errorTextField = new Label();
        errorTextField.setStyle("-fx-background-color: #ff0000");
        errorTextField.setVisible(false);

        Button button = new Button(StaticValues.LOGIN_BTN_TEXT);
        button.setOnMouseClicked(e->{
            errorTextField.setVisible(false);
            String result = onLogin.apply(loginTextField.getText());
            if(result != null){
                errorTextField.setText(result);
                errorTextField.setVisible(true);
            }
        });

        VBox vBox = new VBox(title, loginTextField, button, errorTextField);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(50,50,50,50));
        vBox.setSpacing(16);

        return new Scene(vBox, StaticValues.APP_WIDTH, StaticValues.APP_HEIGHT);
    }

}
