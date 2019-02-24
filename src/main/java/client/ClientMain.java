package client;

import controller.ClientController;
import controller.Controller;
import interfaces.RemoteController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import model.Song;
import view.View;

import java.io.File;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class ClientMain extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }


    public void start(Stage primaryStage) throws Exception {

        Model model = new Model();

        View view = new View();






        Scene s = new Scene(view, 690, 500);
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.setTitle("MediaPlayer");
        primaryStage.show();


        int port = ThreadLocalRandom.current().nextInt(5800, 5999);
        int nameExtension = ThreadLocalRandom.current().nextInt(1, 9999);


            UDPClient udp = new UDPClient(view, port);
            TCPClient tcp = new TCPClient("client "+nameExtension);




        udp.start();


        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String>[] servername = new Future[1];
        servername[0]= executorService.submit(tcp);
        executorService.shutdown();

        RemoteController remote = (RemoteController) Naming.lookup("//Localhost/"+servername[0].get());
        System.out.print("Remoteobject");


        ClientController controller = new ClientController(model, view,remote);











    }
}