package controls;

import config.StaticValues;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.util.stream.Collectors;

public class ActivityPane extends StackPane {

    public static final int PRIMARY_LABEL = 0;

    public ActivityPane(){
        super();
        setMaxHeight(StaticValues.ACTIVITY_PANE_HEIGHT);
        //setBackground(new Background(new BackgroundFill(Paint.valueOf("#fff"), new CornerRadii(10), new Insets(16,16,16,16))));
        setStyle("-fx-border-width: 1; -fx-border-color: #000; -fx-background-color: #fff; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-insets: 16; -fx-border-insets: 16");
        Label primaryLabel = new Label();
        primaryLabel.setUserData(PRIMARY_LABEL);
        getChildren().add(primaryLabel);

        setVisible(false);
        setOnMouseClicked(e->setVisible(false));
    }

    public Label getPrimaryLabel(){
        return (Label)getChildren().filtered(node->node.getUserData().equals(PRIMARY_LABEL)).get(0);
    }

}
