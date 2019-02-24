package controller;

import interfaces.RemoteController;
import model.Model;
import view.View;
import java.rmi.RemoteException;
import java.util.concurrent.locks.ReentrantLock;

public class ClientController {


    private View view;
    private Model model;
    private RemoteController remote;
    private boolean lastFocusLibrary=false;
    public ReentrantLock lock;


    public ClientController(Model model, View view, RemoteController remote){
        this.model = model;
        this.view = view;
        this.remote = remote;
        lock= new ReentrantLock();
        link();

    }

    public void link(){


        view.setLibrary(model.getLibrary());
        view.setPlaylist(model.getPlaylist());


        view.getPlaylist().setOnMouseClicked(e ->{
            try {

                if (view.getPlaylist().getSelectionModel().getSelectedItem() != null) {
                    model.Song s = (model.Song) view.getPlaylist().getSelectionModel().getSelectedItem();

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
                    model.Song s = (model.Song) view.getLibrary().getSelectionModel().getSelectedItem();

                    getSongInfo(s);


                }
                lastFocusLibrary = true;
            }
            catch(RemoteException r){
                r.printStackTrace();
            }

        });


        view.setTimeline(1,f->{
            try{
                lock.lock();
                    int libraryPos = view.getLibrary().getSelectionModel().getSelectedIndex();
                    int playlistPos = view.getPlaylist().getSelectionModel().getSelectedIndex();

                    if(remote.getLibrary().getList().size()==0){
                        model.getLibrary().clear();
                    }
                    else {
                        model.getLibrary().setList(remote.getLibrary().getList());
                    }

                    if(remote.getPlaylist().getList().size()==0){
                        model.getPlaylist().clear();
                    }
                    else {
                        model.getPlaylist().setList(remote.getPlaylist().getList());
                    }

                    if(libraryPos>=model.getLibrary().sizeOfPlaylist()){
                        view.getLibrary().getSelectionModel().select(model.getLibrary().sizeOfPlaylist()-1);
                    }
                    else{
                        view.getLibrary().getSelectionModel().select(libraryPos);
                    }

                if(playlistPos>=model.getPlaylist().sizeOfPlaylist()){
                    view.getPlaylist().getSelectionModel().select(model.getPlaylist().sizeOfPlaylist()-1);
                }
                else{
                    view.getPlaylist().getSelectionModel().select(playlistPos);
                }
                lock.unlock();



            }
            catch(RemoteException r){r.printStackTrace();
            }

        });

        view.addEventHandlerAddAll(f->{
            try {
                lock.lock();
                remote.clickUIButton("addall", 0);
                lock.unlock();
            }
            catch(RemoteException r){
                r.printStackTrace();
            }
        });

        view.addEventHandlerAddToPlaylist(f->{
            try {
                lock.lock();
                remote.clickUIButton("addtoplaylist",view.getLibrary().getSelectionModel().getSelectedIndex());
                lock.unlock();
            }
            catch(RemoteException r){
                r.printStackTrace();
            }
        });

        view.addEventHandlerRemove(f->{
            try {
                lock.lock();
                remote.clickUIButton("remove", view.getPlaylist().getSelectionModel().getSelectedIndex());
                lock.unlock();
            }
            catch(RemoteException r){
                r.printStackTrace();
            }
        });

        view.addEventHandlerPlay(f->{
            try {
                lock.lock();
                if(lastFocusLibrary) {
                    remote.clickPlayerButton("play", view.getLibrary().getSelectionModel().getSelectedIndex(), true);
                }
                else{
                    remote.clickPlayerButton("play", view.getPlaylist().getSelectionModel().getSelectedIndex(), false);
                }
                lock.unlock();
            }
            catch(RemoteException r){
                r.printStackTrace();
            }
        });

        view.addEventHandlerPause(f->{
            try {
                lock.lock();
                remote.clickPlayerButton("pause",0,false);
                lock.unlock();
            }
            catch(RemoteException r){
                r.printStackTrace();
            }
        });

        view.addEventHandlerNext(f->{
            try {
                lock.lock();
                remote.clickPlayerButton("next", 0,false);
                lock.unlock();

            }
            catch(RemoteException r){
                r.printStackTrace();
            }
        });

        view.addEventHandlerCommit(f->{

            try{
                lock.lock();
                if(lastFocusLibrary){

                    remote.clickCommit(view.getLibrary().getSelectionModel().getSelectedIndex(),lastFocusLibrary,view.getTitleField(),view.getArtistField(),view.getAlbumField());
                }
                else{

                    remote.clickCommit(view.getPlaylist().getSelectionModel().getSelectedIndex(),lastFocusLibrary,view.getTitleField(),view.getArtistField(),view.getAlbumField());
                }
                lock.unlock();

            }
            catch(RemoteException r){
                r.printStackTrace();
            }
        });

        view.addEventHandlerLoad(f->{
            try{
                lock.lock();
                remote.clickSerButton("load", view.getComboSelection());
                lock.unlock();
            }
            catch(RemoteException r){
                r.printStackTrace();
            }
        });

        view.addEventHandlerSave(f->{
            try{
                lock.lock();
                remote.clickSerButton("save", view.getComboSelection());
                lock.unlock();
            }
            catch(RemoteException r){
                r.printStackTrace();
            }
        });






    }

    public void getSongInfo(model.Song s) throws RemoteException{

        view.setTitleField(s.getTitle());
        view.setArtistField(s.getInterpret());
        view.setAlbumField(s.getAlbum());

    }
}
