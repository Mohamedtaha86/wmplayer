package controller;


import interfaces.RemoteController;
import model.Model;
import model.Playlist;
import model.Song;
import view.View;
import interfaces.SerializableStrategy;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import strategy.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class Controller extends UnicastRemoteObject implements RemoteController{

    private View view;
    private Model model;
    private boolean lastFocusLibrary =false;
    private boolean playlibrary = false;
    private MediaPlayer mediaPlayer = null;
    private int playingIndex;
    private SerializableStrategy strat;

    public Controller(Model model, View view) throws RemoteException{
        this.view=view;
        this.model=model;
        link();
    }

    public void link() throws RemoteException  {







        view.setPlaylist(model.getPlaylist());
        view.setLibrary(model.getLibrary());


 view.getPlaylist().setOnMouseClicked(e ->{
     try {
         if (view.getPlaylist().getSelectionModel().getSelectedItem() != null) {
             Song s = (Song) view.getPlaylist().getSelectionModel().getSelectedItem();

             getSongInfo(s);


         }
         lastFocusLibrary = false;
     }
     catch(RemoteException r){
         r.printStackTrace();
     }
 });
 view.getLibrary().setOnMouseClicked(event -> {

     try {

         if (view.getLibrary().getSelectionModel().getSelectedItem() != null) {
             Song s = (Song) view.getLibrary().getSelectionModel().getSelectedItem();

             getSongInfo(s);


         }
         lastFocusLibrary = true;
     }
     catch(RemoteException r){
         r.printStackTrace();
     }

 });


        view.addEventHandlerSave(f -> {
            if(view.getComboSelection()=="Binary"){
                strat = new BinaryStrategy();
            }
            if(view.getComboSelection()=="XML"){
                strat = new XMLStrategy();
            }
            if(view.getComboSelection()=="JDBC"){
                strat = new JDBCStrategy();
            }
            if(view.getComboSelection()=="OpenJPA"){
                strat = new OpenJPAStrategy();
            }

            try {
                strat.writeLibrary(model.getLibrary());
                strat.writePlaylist(model.getPlaylist());

            }
            catch(IOException e){
                System.out.print("Saving your config has failed");
                e.printStackTrace();
            }
        });

        view.addEventHandlerLoad(f->{
            interfaces.Playlist p;

            if(view.getComboSelection()=="Binary"){
                strat = new BinaryStrategy();
            }
            if(view.getComboSelection()=="XML"){
                strat = new XMLStrategy();
            }
            if(view.getComboSelection()=="JDBC"){
                strat = new JDBCStrategy();
            }
            if(view.getComboSelection()=="OpenJPA"){
                strat = new OpenJPAStrategy();
            }
            try {
                p = strat.readLibrary();
                model.getLibrary().clear();
                for (interfaces.Song s : p) {
                    model.getLibrary().add(s);
                }


                p = strat.readPlaylist();
                model.getPlaylist().clear();
                for(interfaces.Song s : p){
                    long id = s.getId();
                    interfaces.Song result = model.getLibrary().findSongByID(id);
                    model.getPlaylist().add(result);
                }

            }
            catch(FileNotFoundException e){
                System.out.println("There's no saved config.");

            }
            catch(ClassNotFoundException e){
                System.out.println("Couldnt find class.");
                e.printStackTrace();
            }
            catch(RemoteException e){
                System.out.println("An error occured");
                e.printStackTrace();
            }
            catch(IOException e){
                System.out.println("Loading config failed");
                e.printStackTrace();
            }





        });


        view.addEventHandlerAddToPlaylist(f -> {
            if(view.getLibrary().getSelectionModel().getSelectedItem() != null)
                model.getPlaylist().add((Song) view.getLibrary().getSelectionModel().getSelectedItem());
        });

        view.addEventHandlerAddAll(f -> {
            try {
                model.getPlaylist().setList(model.getLibrary().getList());
            } catch (RemoteException e) {
                System.out.println("RemoteException occured");
            }
        });

        view.addEventHandlerRemove(f -> {

            try {

                   int index= view.getPlaylist().getSelectionModel().getSelectedIndex();
                    model.getPlaylist().deleteSong((Song) view.getPlaylist().getSelectionModel().getSelectedItem());



                view.getPlaylist().refresh();
                if(model.getPlaylist().size()>index) {
                    view.getPlaylist().getSelectionModel().select(index);
                }
                else{
                    view.getPlaylist().getSelectionModel().selectPrevious();
                }



            }
            catch(RemoteException e){

            }
            });

        view.addEventHandlerCommit(f->{
            if(view.getPlaylist().getSelectionModel().getSelectedItem()!=null || view.getLibrary().getSelectionModel().getSelectedItem()!=null) {

                Song s = null;
                if (lastFocusLibrary) {
                    s = (Song) view.getLibrary().getSelectionModel().getSelectedItem();
                } else {
                    s = (Song) view.getPlaylist().getSelectionModel().getSelectedItem();
                }
                try {

                    model.getLibrary().findSongByPath(s.getPath()).setTitle(view.getTitleField());
                    model.getLibrary().findSongByPath(s.getPath()).setInterpret(view.getArtistField());
                    model.getLibrary().findSongByPath(s.getPath()).setAlbum(view.getAlbumField());
                    if(!(model.getPlaylist().isEmpty())) {
                        model.getPlaylist().findSongByPath(s.getPath()).setTitle(view.getTitleField());
                        model.getPlaylist().findSongByPath(s.getPath()).setInterpret(view.getArtistField());
                        model.getPlaylist().findSongByPath(s.getPath()).setAlbum(view.getAlbumField());
                    }

                    view.getLibrary().refresh();
                    view.getPlaylist().refresh();
                } catch (RemoteException e) {

                }


            }


        });

        view.addEventHandlerPlay(f->{

            try {


                if (!lastFocusLibrary && view.getPlaylist().getSelectionModel().getSelectedItem() != null) {
                    int newIndex = view.getPlaylist().getSelectionModel().getSelectedIndex();
                    Song s = (Song) model.getPlaylist().get(newIndex);

                    playlibrary = false;

                    PlaySong(s);


                    playingIndex = newIndex;


                }
                if (lastFocusLibrary && view.getLibrary().getSelectionModel().getSelectedItem() != null) {
                    int newIndex = view.getLibrary().getSelectionModel().getSelectedIndex();
                    Song s = (Song) model.getLibrary().get(newIndex);

                    playlibrary = true;
                    PlaySong(s);
                    playingIndex = newIndex;

                }


            }
            catch(RemoteException r){r.printStackTrace();}
        }

        );

        view.addEventHandlerPause(f->{

            if(mediaPlayer!=null) {
                mediaPlayer.pause();
                mediaPlayer.setStartTime(mediaPlayer.getCurrentTime());
            }
        });

        view.addEventHandlerNext(f->{

            try {

                if (view.getPlaylist().getSelectionModel().getSelectedItem() != null) {
                    if (playlibrary) {

                        mediaPlayer.dispose();
                        playNext();
                    }
                    if (!playlibrary) {
                        playNext();
                    }
                }
            }
            catch(RemoteException r){
                r.printStackTrace();
            }
        });

       view.setTimeline(1,f->{

           if(mediaPlayer!= null){

               int duration = (int)mediaPlayer.getCurrentTime().toMillis()/1000;

               int mins = duration/60;
               int secs = duration % 60;

               String s1 = mins+"";
               String s2 = secs+"";

               while(s1.length()<2){
                   s1 = "0"+s1;
               }
               while(s2.length()<2){
                   s2 = "0"+s2;
               }






               view.setTime(s1+":"+s2);
           }
           else{
               view.setTime("");
           }
       });




        }




    public void PlaySong(Song s) throws RemoteException{

        if(mediaPlayer!=null) {

            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.stop();
                mediaPlayer.dispose();

            }

            if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED ) {
                if((playingIndex != view.getLibrary().getSelectionModel().getSelectedIndex() && lastFocusLibrary) || (playingIndex != view.getPlaylist().getSelectionModel().getSelectedIndex() && !lastFocusLibrary)){
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                }

            }



        }


        if(mediaPlayer == null || mediaPlayer.getStatus()== MediaPlayer.Status.DISPOSED){


                File file = new File(s.getPath());
                Media media = new Media(file.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnEndOfMedia(() -> {
                    if (playlibrary) {
                        mediaPlayer.stop();
                        mediaPlayer.dispose();
                        playlibrary = false;
                    } else {
                        try {
                            playNext();
                        }
                        catch(RemoteException r){
                        }
                    }
                });


        }
            if(mediaPlayer!=null) {
                mediaPlayer.play();
            }








            }







    public void playNext() throws RemoteException{
        if(view.getPlaylist().getSelectionModel().getSelectedIndex()<model.getPlaylist().size()) {
        if(view.getPlaylist().getSelectionModel().getSelectedItem() != null) {
            if (playlibrary) {
                playingIndex = view.getPlaylist().getSelectionModel().getSelectedIndex() + 1;
            } else {
                playingIndex += 1;
            }
            view.getPlaylist().getSelectionModel().select(playingIndex);

            Song s = (Song) view.getPlaylist().getSelectionModel().getSelectedItem();
            if (!lastFocusLibrary) {
                getSongInfo(s);

            }


            PlaySong(s);
        }

        }




    }

    public void getSongInfo(Song s) throws RemoteException{

        view.setTitleField(s.getTitle());
        view.setArtistField(s.getInterpret());
        view.setAlbumField(s.getAlbum());

    }

    public Playlist getLibrary() throws RemoteException{
        return model.getLibrary();
    }
    public Playlist getPlaylist() throws RemoteException{
        return model.getPlaylist();
    }
    public void clickUIButton(String name, int position) throws RemoteException {


        switch (name) {


            case "addall":
                view.getAddAll().fire();
                break;

            case "addtoplaylist":
                view.getLibrary().getSelectionModel().select(position)
                ;
                view.getAddToPlaylist().fire();
                break;

            case "remove":
                view.getPlaylist().getSelectionModel().select(position);
                view.getRemove().fire();
                break;


        }

    }

        public void clickPlayerButton(String name, int position, boolean lastfocuslibrary) throws RemoteException{

        switch(name){

            case "play": if(lastfocuslibrary){
                            view.getLibrary().getSelectionModel().select(position);
                            lastFocusLibrary=true;

                         }
                         else{
                            view.getPlaylist().getSelectionModel().select(position);
                            lastFocusLibrary=false;
                         }
                        view.getPlay().fire();
                        break;

            case "pause": view.getPause().fire();
                          break;

            case "next": view.getNext().fire();

        }

    }

    public void clickCommit(int position, boolean lastfocusLibrary, String name, String artist, String album) throws RemoteException{
        if(lastfocusLibrary){
            view.getLibrary().getSelectionModel().select(position);
            lastFocusLibrary=true;
        }
        else{
            view.getPlaylist().getSelectionModel().select(position);
            lastFocusLibrary=false;
        }
        view.setTitleField(name);
        view.setAlbumField(album);
        view.setArtistField(artist);
        view.getCommit().fire();
    }

    public void clickSerButton(String name, String selection) throws RemoteException{


        view.getComboBox().getSelectionModel().select(selection);

        switch(name){

            case "save":
                         view.getSave().fire();

            case "load": view.getLoad().fire();



        }
    }

    public int getSelection(){
       return view.getPlaylist().getSelectionModel().getSelectedIndex();
    }








 }








