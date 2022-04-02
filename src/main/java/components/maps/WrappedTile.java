package components.maps;

import javafx.scene.image.ImageView;
import org.mapeditor.core.Tile;

import java.awt.*;

/** A bundle that includes a Tiled Tile, associated ImageView, and its onclick listener */
public class WrappedTile {

    private final ImageView imageView;
    private final Tile tile;
    private final Point gridPosition;
    private final javafx.event.EventHandler<? super javafx.scene.input.MouseEvent> onClicked;

    public ImageView getImageView() {
        return imageView;
    }

    public Tile getTile() {
        return tile;
    }

    public Point getGridPosition() {
        return gridPosition;
    }

    public  javafx.event.EventHandler<? super javafx.scene.input.MouseEvent> getOnClicked() {
        return onClicked;
    }

    public WrappedTile(ImageView imageView, Tile tile, Point position,  javafx.event.EventHandler<? super javafx.scene.input.MouseEvent> onClicked) {
        this.imageView = imageView;
        this.tile = tile;
        this.gridPosition = position;
        this.onClicked = onClicked;
    }
}
