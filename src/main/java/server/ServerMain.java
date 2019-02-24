package server;

import controller.Controller;
import interfaces.RemoteController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.Model;
import model.Song;
import view.View;

import java.io.File;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain extends Application {

    public static void main(String[] args){Application.launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {

        Model model = new Model();
        File dir = null;

        DirectoryChooser dc = new DirectoryChooser();

        dir = dc.showDialog(primaryStage);
        if(dir==null){

            Platform.exit();
        }
        else {

            String[] songs = dir.list();


            for (String str : songs) {
                if (str.endsWith(".mp3") || str.endsWith(".wav")) {
                    if(str.contains("/")){
                        Song s = new Song(dir.getAbsolutePath() + "/" + str, str, "", "");
                        model.getLibrary().addSong(s);
                    }
                    else {
                        Song s = new Song(dir.getAbsolutePath() + "\\" + str, str, "", "");
                        model.getLibrary().addSong(s);
                    }
                }
            }
            if(model.getLibrary().isEmpty()){
                Platform.exit();
            }
        }

        View view = new View();

        Remote controller = new Controller(model,view);







        Scene s= new Scene(view,690,500);
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.setTitle("MediaPlayer");
        primaryStage.show();




            UDPServer udp = new UDPServer(view);




            TCPServer tcp = new TCPServer();


        LocateRegistry.createRegistry(1099);

        Naming.rebind("dj",controller);


        udp.start();
        tcp.start();











    }
}
