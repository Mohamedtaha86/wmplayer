package model;

import interfaces.Song;
import javafx.collections.ModifiableObservableListBase;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

public class Playlist extends ModifiableObservableListBase<Song> implements interfaces.Playlist,Serializable{

    private ArrayList<Song> manageSongs= new ArrayList<>();

    public Playlist (){}

    @Override
    public boolean addSong(Song s) throws RemoteException {
        manageSongs.add(s);
        return true;
    }

    @Override
    public boolean deleteSong(Song s) throws RemoteException {
        manageSongs.remove(s);
        return true;
    }

    @Override
    public boolean deleteSongByID(long id) throws RemoteException {
        for (Song s : manageSongs) {

            if(s.getId() == id){
                manageSongs.remove(s);

            }

        }
        return true;
    }

    @Override
    public void setList(ArrayList<Song> a) throws RemoteException {


            manageSongs.clear();
            for(Song s:a){
                add(s);
            }
    }

    @Override
    public ArrayList<Song> getList() throws RemoteException {
        return manageSongs;
    }

    @Override
    public void clearPlaylist() throws RemoteException {

        manageSongs.clear();
    }

    @Override
    public int sizeOfPlaylist() throws RemoteException {

        return manageSongs.size();
    }

    @Override
    public Song findSongByPath(String name) throws RemoteException {
        for(Song s : manageSongs){

            if(s.getPath().equals(name)){

                return s;
            }
        }
        return null;
    }

    @Override
    public Song findSongByID(long id) throws RemoteException {
        for (Song s : manageSongs) {

            if(s.getId() == id){
                return s;

            }

        }
        return null;
    }

    @Override
    public Iterator<Song> iterator() {
        return manageSongs.iterator();
    }

    @Override
    public Song get(int index)throws IndexOutOfBoundsException {
        return manageSongs.get(index);
    }

    @Override
    public int size() {
        return manageSongs.size();
    }

    @Override
    protected void doAdd(int index, Song element) throws IndexOutOfBoundsException {
        manageSongs.add(index , element);
    }

    @Override
    protected Song doSet(int index, Song element) throws IndexOutOfBoundsException {
        return manageSongs.set(index , element);
    }

    @Override
    protected Song doRemove(int index) throws IndexOutOfBoundsException {
        return manageSongs.remove(index);
    }
}
