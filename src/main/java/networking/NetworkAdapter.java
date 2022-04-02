package networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import state.AppState;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;
import java.util.HashMap;

public class NetworkAdapter {

    Gson gson;
    private byte[] sendBuf, recvBuf;
    private DatagramSocket socket;
    private InetAddress address;
    private String host;
    private int port;
    private int timeout;
    /**requests per second*/
    private int rps;
    private static final NetworkAdapter INSTANCE = new NetworkAdapter();
    private static final AppState APP_STATE = AppState.getInstance();
    private static final Type PLAYER_COORDINATE_MAP_TYPE = new TypeToken<HashMap<String, ServerPositionBroadcast>>() {}.getType();
    private NetworkAdapter() {}

    public void init(String host, int port, int timeout, int rps) throws SocketException, UnknownHostException {
        INSTANCE.host = host;
        INSTANCE.port = port;
        INSTANCE.rps = rps;
        INSTANCE.timeout = timeout;
        socket = new DatagramSocket();
        //socket.setSoTimeout(timeout);
        address = InetAddress.getByName(host);
        gson = new Gson();
        recvBuf = new byte[(2<<15)-1]; //65,535B max UDP packet size
    }

    public static NetworkAdapter getInstance(){
        return INSTANCE;
    }

    public void sendEcho(String msg) throws IOException {
        sendBuf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(sendBuf, sendBuf.length, address, port);
        socket.send(packet);
    }

    public void initServerListener() {
        new Thread(() -> {
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                    socket.receive(packet);
                    String received = new String(
                            packet.getData(), 0, packet.getLength());
                    HashMap<String, ServerPositionBroadcast> result = gson.fromJson(received, PLAYER_COORDINATE_MAP_TYPE);
                    APP_STATE.setPlayerCoordsMap(result);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void initClientHeartbeat(){

        new Thread(() -> {
            while(true){
                try{
                    Platform.runLater(() -> {
                        try {
                            double xCoord = APP_STATE.getPlayerPos()[0];
                            double yCoord = APP_STATE.getPlayerPos()[1];
                            String address = APP_STATE.getAccount().getAddress().toString();
                            NetworkAdapter.getInstance().sendEcho(clientMessageToJson(new PositionMessage(address, xCoord, yCoord, APP_STATE.getActiveMap(), APP_STATE.getSpriteDirFacing(), APP_STATE.getSpriteWalking())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    Thread.sleep(1000L/NetworkAdapter.getInstance().getRps());
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void close() {
        socket.close();
    }
    public int getRps() {
        return rps;
    }


    public String clientMessageToJson(AbstractClientMessage object) throws JsonProcessingException {

        return gson.toJson(object);
    }
}
