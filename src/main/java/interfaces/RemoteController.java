package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteController extends Remote {

    Playlist getLibrary() throws RemoteException;
    Playlist getPlaylist() throws RemoteException;

    void clickUIButton(String name, int position) throws RemoteException;

    void clickPlayerButton(String name, int position, boolean lastfocusLibrary) throws RemoteException;

    void clickCommit(int position, boolean lasfocusLibrary, String name, String artist, String album) throws RemoteException;

    void clickSerButton(String name, String selection) throws RemoteException;

    int getSelection() throws RemoteException;



}
