package com.shadhin;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //socket io connection configurations
        Configuration config = new Configuration();
        config.setHostname("0.0.0.0");
//        config.setAddVersionHeader();
        config.setPort(9090);

        //start the server
        SocketIOServer server = new SocketIOServer(config);

        server.addConnectListener((client) -> {
            System.out.println("a client is connected : "+client.getSessionId());
            client.sendEvent("message", "hello");
        });

        server.addDisconnectListener((client) -> {
            System.out.println("a client is disconnected : "+client.getSessionId());
        });

        server.addEventListener("message", String.class,
                (socketIOClient, data, ackRequest) -> {
                    System.out.println("received message : "+data);
                    System.out.println("received message from : "+socketIOClient.getSessionId());
                    socketIOClient.sendEvent("replay", "received replay : "+data);
                }
        );

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            System.out.println("server stopped");
        }));

        server.start();
        System.out.println("server started in port 9090");
    }
}
