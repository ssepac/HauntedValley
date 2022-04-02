package util;

import org.mapeditor.core.Tile;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class CollisionDetection {

/*    public static boolean isPlayerWithinBounds(double margin, DirectionEnum currentDirection, double[] coords, double[] mapDimensions){
        switch (currentDirection) {
            case NORTH:
                if(coords[1]-margin < 0) return true;
                break;
            case SOUTH:
                if(coords[1] + margin > mapDimensions[1]) return true;
                break;
            case EAST:
                if(coords[0] + margin > mapDimensions[0]) return true;
                break;
            case WEST:
                if(coords[0]-margin < 0) return true;
                break;
        }
        return false;
    }*/

    public static boolean isPlayerWithinBounds(DirectionEnum currentDirection, double[] exactCoords, HashSet<Point> illegalPoints){
        double[] coords = Arrays.copyOf(exactCoords, exactCoords.length);
        switch (currentDirection) {
            case NORTH -> coords[1] -= 8;
            case SOUTH -> coords[1] += 16;
            case EAST -> coords[0] += 8;
            case WEST -> coords[0] -= 8;
        }

        int[] newCoords = new int[]{((int)(coords[0]/32.0))*32, ((int)(coords[1]/32.0))*32};
        //System.out.println(Arrays.toString(newCoords));
        //System.out.println(illegalPoints);
        return illegalPoints.contains(new Point(newCoords[0], newCoords[1]));
    }
}
