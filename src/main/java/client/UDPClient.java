package client;

import javafx.application.Platform;
import view.View;
import java.io.IOException;
import java.net.*;


public class UDPClient extends Thread {

    View view;
    int port;


    public UDPClient(View view, int port) {

        this.view = view;
        this.port = port;


    }

    public void run() {


        InetAddress ia = null;
        try {
            ia = InetAddress.getByName("localhost");
        } catch (UnknownHostException e2) {
            e2.printStackTrace();
        }

        try (DatagramSocket dSocket = new DatagramSocket(port);) {

            try {

                while (true) {
                    String command = "cmd:time";

                    byte buffer[] = command.getBytes();


                    DatagramPacket packet = new DatagramPacket(buffer,
                            buffer.length, ia, 5000);

                    dSocket.send(packet);

                    byte answer[] = new byte[1024];

                    packet = new DatagramPacket(answer, answer.length);

                    dSocket.receive(packet);

                    String time = new String(packet.getData(), 0, packet.getLength());
                    Platform.runLater(()->{ view.setTime(time);});

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (SocketException e1) {
            e1.printStackTrace();
        }

    }
}


