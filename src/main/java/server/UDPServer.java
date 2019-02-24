package server;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import view.View;




public class UDPServer extends Thread {


    View view;
    Thread t1;




    public UDPServer(View view){

        this.view = view;


    }




    public void run() {

        try (DatagramSocket socket =  new DatagramSocket(5000);) {

            while (true) {

                t1 = new Thread(()->{
                    DatagramPacket packet = new DatagramPacket(new byte[8], 8);

                    try {
                        socket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    int len = packet.getLength();
                    byte[] data = packet.getData();



                    String da = new String(data);



                    if (da.equals("cmd:time")) {


                        byte[] myTime = view.getTime().getBytes();

                        packet = new DatagramPacket(myTime, myTime.length, address, port);
                        try {
                            socket.send(packet);
                        }
                        catch(IOException e){
                            e.printStackTrace();
                        }

                    }

                });
                t1.run();


            }
        }


        catch(IOException e) {
            e.printStackTrace();
        }

    }
}



