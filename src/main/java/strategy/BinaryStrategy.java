package strategy;

import interfaces.Playlist;
import interfaces.SerializableStrategy;
import interfaces.Song;
import java.io.*;


public class BinaryStrategy implements SerializableStrategy {

    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private FileInputStream fislibrary = null;
    private FileOutputStream foslibrary = null;
    private FileInputStream fisplaylist = null;
    private FileOutputStream fosplaylist = null;



    @Override
    public void openWritableLibrary() throws IOException {
        foslibrary = new FileOutputStream("Library.ser");
        oos = new ObjectOutputStream(foslibrary);
    }

    @Override
    public void openReadableLibrary() throws IOException {
        fislibrary = new FileInputStream("Library.ser");
        ois = new ObjectInputStream(fislibrary);
    }

    @Override
    public void openWritablePlaylist() throws IOException {
        fosplaylist = new FileOutputStream("Playlist.ser");
        oos = new ObjectOutputStream(fosplaylist);
    }

    @Override
    public void openReadablePlaylist() throws IOException {
        fisplaylist = new FileInputStream("Playlist.ser");
        ois = new ObjectInputStream(fisplaylist);
    }

    @Override
    public void writeSong(Song s) throws IOException  {

        oos.writeObject(s);
        oos.flush();
        
    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {

       return (Song)ois.readObject();

    }

    @Override
    public void writeLibrary(Playlist p) throws IOException {
        openWritableLibrary();
        for(Song s : p){
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
        }
        catch(EOFException e){
            System.out.println("All Songs loaded");
        }
        finally{
        closeReadableLibrary();
        return p;
    }

    }






    @Override
    public void writePlaylist(Playlist p) throws IOException {
        openWritablePlaylist();
        for(Song s : p){
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
        }
        catch(EOFException e){
            System.out.println("All Songs loaded");
        }
        finally{
            closeReadableLibrary();
            return p;
        }

    }

    @Override
    public void closeWritableLibrary() {

        try {
            if(oos!=null) {
                oos.close();
            }
            if(foslibrary!=null) {
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
            if(ois!=null) {
                ois.close();
            }
            if(fislibrary!=null) {
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
            if(oos!=null) {
                oos.close();
            }
            if(fosplaylist!=null) {
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
            if(ois!=null) {
                ois.close();
            }
            if(fisplaylist!=null) {
                fisplaylist.close();
            }
        } catch (IOException e) {
            System.out.println("Playlist input failed to close");
            e.printStackTrace();
        }

    }
}
