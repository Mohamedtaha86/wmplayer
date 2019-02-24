package client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

public class TCPClient  implements Callable<String> {

    String name;
    String serverName;

    public  TCPClient(String name){

        this.name = name;


    }

    public String call(){
        Connect();
        return serverName;
    }


    public void Connect() {


        try (Socket serverCon = new Socket("localhost", 5020);
             InputStream in = serverCon.getInputStream();
             OutputStream out = serverCon.getOutputStream();
             ObjectOutputStream oout = new ObjectOutputStream(out);
             ObjectInputStream oin = new ObjectInputStream(in)) {



            oout.writeObject("musik");
            oout.writeObject(name);

            serverName = (String)oin.readObject();






        } catch (IOException e) {
            e.printStackTrace();

        }
        catch(ClassNotFoundException e){}

    }
}


