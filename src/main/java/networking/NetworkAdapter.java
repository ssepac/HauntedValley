package networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import state.AppState;

import java.io.IOException;
import java.net.*;

public class NetworkAdapter {

    ObjectMapper mapper;
    private byte[] buf;
    private volatile int numberRetries;
    private DatagramSocket socket;
    private InetAddress address;
    private String host;
    private int port;
    private int timeout;
    /**requests per second*/
    private int rps;
    private static final NetworkAdapter INSTANCE = new NetworkAdapter();
    private static final AppState appState = AppState.getInstance();
    private static volatile boolean heartBeatActive;
    private NetworkAdapter() {}

    public void init(String host, int port, int timeout, int rps) throws SocketException, UnknownHostException {
        INSTANCE.host = host;
        INSTANCE.port = port;
        INSTANCE.rps = rps;
        INSTANCE.timeout = timeout;
        socket = new DatagramSocket();
        //socket.setSoTimeout(timeout);
        address = InetAddress.getByName(host);
        mapper = new ObjectMapper();
    }

    public static NetworkAdapter getInstance(){
        return INSTANCE;
    }

    public void sendEcho(String msg) throws IOException {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }

    public void initServerListener() {
        new Thread(() -> {
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String received = new String(
                            packet.getData(), 0, packet.getLength());
                    System.out.println("from server:" + received);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    //TODO: Update beginHeartBeat to receive an ACK from the server every 10 seconds.
    public void initClientHeartbeat(){
        heartBeatActive = true;
        numberRetries = 0;

        new Thread(() -> {
            while(heartBeatActive && numberRetries <= 10){
                try{
                    Platform.runLater(() -> {
                        try {
                            double xCoord = appState.getPlayerPos()[0];
                            double yCoord = appState.getPlayerPos()[1];
                            String address = appState.getAccount().getAddress().toString();
                            NetworkAdapter.getInstance().sendEcho(toJson(new HeartbeatClient(address, xCoord, yCoord)));
                        } catch (IOException e) {
                            e.printStackTrace();
                            incrementRetries();
                        }
                    });
                    Thread.sleep(NetworkAdapter.getInstance().getRps()* 1000L);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    incrementRetries();

                    if(numberRetries > 10){
                        heartBeatActive = false;
                    }
                }
            }
            System.out.println("Stopping network adapter heart beat.");
        }).start();
    }

    private synchronized void incrementRetries(){
        numberRetries++;
    }

    public void close() {
        socket.close();
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRps() {
        return rps;
    }

    public void setRps(int rps) {
        this.rps = rps;
    }

    public String toJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
