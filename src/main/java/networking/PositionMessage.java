package networking;

import lombok.Getter;
import lombok.Setter;
import util.DirectionEnum;

@Setter
@Getter
public class PositionMessage extends AbstractClientMessage {

    private String address;
    private double xCoord;
    private double yCoord;
    private String mapId;
    private DirectionEnum direction;
    private boolean isWalking;

    public PositionMessage(String address, double xCoord, double yCoord, String mapId, DirectionEnum direction, boolean isWalking) {
        super(MessageTypesEnum.POSITION_UPDATE);
        this.address = address;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.mapId = mapId;
        this.direction = direction;
        this.isWalking = isWalking;
    }
}
