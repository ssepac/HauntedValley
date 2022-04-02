package components.maps;

import config.StaticValues;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import org.mapeditor.core.Tile;
import org.mapeditor.core.TileLayer;
import org.mapeditor.io.TMXMapReader;
import state.AppState;
import util.Conversion;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Map extends AnimationTimer {

    private final Group group;
    private org.mapeditor.core.Map map;
    private static AppState appState;
    private final double[] initialCoords;
    private final HashSet<Point> collisionPoints;

    public Map(String mapId, int mapWidth, int mapHeight, String uri) throws Exception{
        appState = AppState.getInstance();
        appState.setActiveMap(mapId);
        TMXMapReader mapReader = new TMXMapReader();
        List<WrappedTile> wrappedTileList;
        initialCoords = appState.getPlayerPos();
        group = new Group();
        collisionPoints = new HashSet<>();

        // we how have a mainPane that we can hang stuff on and see it
        // in a window...

        // loading tmx and expanding it into various java structures
        // it also loads the tile map images
        try {
            map = mapReader.readMap(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // as libtiled provides awt images we must convert them to
        // javafx images but we don't want a new image for every
        // single tile on the map
        HashMap<Integer, javafx.scene.image.Image> tileHash = new HashMap<>();
        javafx.scene.image.Image tileImage = null;
        wrappedTileList = new ArrayList<>();

        for(int layerIndex=0; layerIndex<map.getLayerCount();layerIndex++) {

            // assume just the one layer
            // you could have different layers on different
            // javafx nodes sitting on top of each other...
            TileLayer layer = (TileLayer) map.getLayer(layerIndex);
            if (layer ==null) {
                System.out.println("can't get map layer");
                System.exit(-1);
            }

            int width = layer.getWidth();
            int height = layer.getHeight();

            Tile tile = null;
            int tid;

            for (int gridRow = 0; gridRow < height; gridRow++) {
                for (int gridCol = 0; gridCol < width; gridCol++) {
                    tile = layer.getTileAt(gridCol, gridRow);
                    if (tile == null) continue;
                    tid = tile.getId();
                    if (tileHash.containsKey(tid)) {
                        // if we have already used the image get it from the hashmap
                        tileImage = tileHash.get(tid);
                    } else {
                        // if we haven't seen it before convert and cache it
                        try {
                            tileImage = Conversion.convertAwtToJavaFxImage(tile.getImage());
                        } catch (Exception e) {
                            e.printStackTrace(); // TODO!
                        }
                        tileHash.put(tid, tileImage);
                    }

                    ImageView iv = new ImageView(tileImage);

                    // assuming staggered if Y is odd move it right 1/2 a tile
                    // also assuming 64x64 tile
                    iv.setTranslateX(gridCol * StaticValues.TILE_SIZE);
                    iv.setTranslateY(gridRow * StaticValues.TILE_SIZE);

                    int finalX = gridCol;
                    int finalY = gridRow;
                    WrappedTile wrappedTile = new WrappedTile(iv, tile, new Point(gridCol, gridRow), event -> appState.dispatchActivityPaneMessage("X= " + finalX + ", Y= " + finalY));
                    wrappedTileList.add(wrappedTile);
                }
            }
        }
        for(WrappedTile wrappedTile: wrappedTileList) {
            group.getChildren().add(wrappedTile.getImageView());
            wrappedTile.getImageView().setOnMouseClicked(wrappedTile.getOnClicked());
        }


        //Get collision tiles
/*        TileLayer collisionLayer = (TileLayer) map.getLayer(1);
        for(int y=0; y<collisionLayer.getHeight();y++){
            for(int x=0; x<collisionLayer.getWidth();x++){
                Tile t = collisionLayer.getTileAt(x, y);
                if(t!=null) collisionPoints.add(new Point(x*32,y*32));
            }
        }*/
    }

    public HashSet<Point> getCollisionPoints(){
        return collisionPoints;
    }

    public Group getTileGroup(){
        return group;
    }

    @Override
    public void handle(long now) {
        double[] coords = appState.getPlayerPos();
        double diffX = coords[0] - initialCoords[0];
        double diffY = coords[1] - initialCoords[1];
        group.setTranslateX(-diffX);
        group.setTranslateY(-diffY);
    }

}
