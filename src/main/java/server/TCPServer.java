package server;



import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class TCPServer extends Thread {


ArrayList<String> clients;
    ReentrantLock lock;


public TCPServer(){
    clients = new ArrayList<>();
    lock = new ReentrantLock();


}

        public void run(){

            try (ServerSocket server = new ServerSocket(5020)) {



                            Thread t1 = new Thread(() -> {


                                while(true) {

                                    try (Socket client = server.accept();
                                         InputStream in = client.getInputStream();
                                         OutputStream out = client.getOutputStream();
                                         ObjectOutputStream oout = new ObjectOutputStream(out);
                                         ObjectInputStream oin = new ObjectInputStream(in)) {


                                        if (oin.readObject().equals("musik")) {

                                           lock.lock();

                                            clients.add((String) oin.readObject());

                                            oout.writeObject("dj");

                                            lock.unlock();
                                        }


                                    } catch (IOException e) {
                                    } catch (ClassNotFoundException e) {
                                    }

                                }
                            });

                            t1.run();











                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }










        }



    }

