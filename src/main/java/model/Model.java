package model;

import model.Playlist;

public class Model {

private Playlist playlist = new Playlist();
private Playlist library = new Playlist();

public Playlist getPlaylist() {

        return playlist;


}

public Playlist getLibrary()  {

    return library;
}



}
