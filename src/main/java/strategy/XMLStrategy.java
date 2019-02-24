package strategy;

import interfaces.Playlist;
import interfaces.SerializableStrategy;
import interfaces.Song;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

import java.io.IOException;

public class XMLStrategy implements SerializableStrategy {
    private XMLEncoder xmlEncoder = null;
    private XMLDecoder xmlDecoder = null;
    private FileInputStream fislibrary = null;
    private FileOutputStream foslibrary = null;
    private FileInputStream fisplaylist = null;
    private FileOutputStream fosplaylist = null;


    @Override
    public void openWritableLibrary() throws IOException {
        foslibrary = new FileOutputStream("Library.xml");
        xmlEncoder = new XMLEncoder(foslibrary);
    }

    @Override
    public void openReadableLibrary() throws IOException {
        fislibrary = new FileInputStream("Library.xml");
        xmlDecoder = new XMLDecoder(fislibrary);
    }

    @Override
    public void openWritablePlaylist() throws IOException {
        fosplaylist = new FileOutputStream("Playlist.xml");
        xmlEncoder = new XMLEncoder(fosplaylist);
    }

    @Override
    public void openReadablePlaylist() throws IOException {
        fisplaylist = new FileInputStream("Playlist.xml");
        xmlDecoder = new XMLDecoder(fisplaylist);
    }

    @Override
    public void writeSong(Song s) throws IOException {

        xmlEncoder.writeObject(s);
        xmlEncoder.flush();

    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {

        return (Song) xmlDecoder.readObject();

    }

    @Override
    public void writeLibrary(Playlist p) throws IOException {
        openWritableLibrary();
        for (Song s : p) {
            writeSong(s);
        }

        closeWritableLibrary();


    }

    @Override
    public Playlist readLibrary() throws IOException, ClassNotFoundException {
        openReadableLibrary();

        Playlist p = new model.Playlist();


        try {
            while (true) {

                Song s;

                s = readSong();
                p.addSong(s);

            }
        } catch (EOFException e) {
            System.out.println("All Songs loaded");
        } finally {
            closeReadableLibrary();
            return p;
        }

    }


    @Override
    public void writePlaylist(Playlist p) throws IOException {
        openWritablePlaylist();
        for (Song s : p) {
            writeSong(s);
        }

        closeWritablePlaylist();

    }

    @Override
    public Playlist readPlaylist() throws IOException, ClassNotFoundException {
        openReadablePlaylist();

        Playlist p = new model.Playlist();
        Song s;


        try {
            while (true) {

                s = readSong();
                p.addSong(s);


            }
        } catch (EOFException e) {
            System.out.println("All Songs loaded");
        } finally {
            closeReadableLibrary();
            return p;
        }

    }

    @Override
    public void closeWritableLibrary() {

        try {
            if (xmlEncoder != null) {
                xmlEncoder.close();
            }
            if (foslibrary != null) {
                foslibrary.close();
            }
        } catch (IOException e) {
            System.out.println("Library output failed to close");
            e.printStackTrace();
        }

    }

    @Override
    public void closeReadableLibrary() {
        try {
            if (xmlDecoder != null) {
                xmlDecoder.close();
            }
            if (fislibrary != null) {
                fislibrary.close();
            }
        } catch (IOException e) {
            System.out.println("Library input failed to close");
            e.printStackTrace();
        }
    }

    @Override
    public void closeWritablePlaylist() {
        try {
            if (xmlEncoder != null) {
                xmlEncoder.close();
            }
            if (fosplaylist != null) {
                fosplaylist.close();
            }
        } catch (IOException e) {
            System.out.println("Playlist output failed to close");
            e.printStackTrace();
        }
    }

    @Override
    public void closeReadablePlaylist() {
        try {
            if (xmlDecoder!= null) {
                xmlDecoder.close();
            }
            if (fisplaylist != null) {
                fisplaylist.close();
            }
        } catch (IOException e) {
            System.out.println("Playlist input failed to close");
            e.printStackTrace();
        }

    }

}
