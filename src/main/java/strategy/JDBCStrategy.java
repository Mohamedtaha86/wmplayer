package strategy;

import interfaces.Playlist;
import interfaces.SerializableStrategy;
import interfaces.Song;
import java.io.IOException;
import java.sql.*;

public class JDBCStrategy implements SerializableStrategy {

    private Connection con = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;
    private Statement st = null;


    @Override
    public void openWritableLibrary() throws IOException {

        openConnection();
        ResetPreparedStatement(true);

    }

    @Override
    public void openReadableLibrary() throws IOException {

        openConnection();
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT id,title,album,artist,path FROM library");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void openWritablePlaylist() throws IOException {

        openConnection();
        ResetPreparedStatement(false);
    }

    @Override
    public void openReadablePlaylist() throws IOException {

        openConnection();
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT id,title,album,artist,path FROM playlist");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void writeSong(Song s) throws IOException {

        try{

            preparedStatement.setLong(1,s.getId());
            preparedStatement.setString(2,s.getTitle());
            preparedStatement.setString(3,s.getAlbum());
            preparedStatement.setString(4,s.getInterpret());
            preparedStatement.setString(5,s.getPath());
            preparedStatement.executeUpdate();



        }
        catch(SQLException e){
            System.out.println("Saving a song caused an error");
            e.printStackTrace();
        }




    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {

        Song s=null;
        try{


            long idVal = rs.getLong("id");
            String titleVal = rs.getString("title");
            String albumVal = rs.getString("album");
            String artistVal = rs.getString("artist");
            String pathVal = rs.getString("path");

            s = new model.Song(pathVal,titleVal,albumVal,artistVal);
            s.setId(idVal);





        }
        catch(SQLException e){
            System.out.println("Loading a song caused an error");
            e.printStackTrace();
        }
        finally{
            return s;
        }


    }

    @Override
    public void writeLibrary(Playlist p) throws IOException {
        openWritableLibrary();
        try {
            st = con.createStatement();
            st.executeUpdate("DELETE FROM library;");
        }
        catch(SQLException e){
            System.out.println("Clearing the library table failed");
        }

        for(Song s : p){
            writeSong(s);
            ResetPreparedStatement(true);
        }
        closeWritableLibrary();
    }

    @Override
    public Playlist readLibrary() throws IOException, ClassNotFoundException {
        model.Playlist p= new model.Playlist();
        openReadableLibrary();
        try {
            while(rs.next()) {
                p.add(readSong());
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            closeReadableLibrary();
            return p;
        }

    }

    @Override
    public void writePlaylist(Playlist p) throws IOException {

        openWritablePlaylist();

        try {
            st = con.createStatement();
            st.executeUpdate("DELETE FROM playlist;");
        }
        catch(SQLException e){
            System.out.println("Clearing the playlist table failed");
        }
        for(Song s : p){
            writeSong(s);
            ResetPreparedStatement(false);
        }
        closeWritablePlaylist();

    }

    @Override
    public Playlist readPlaylist() throws IOException, ClassNotFoundException {

        model.Playlist p = new model.Playlist();
        openReadablePlaylist();
        try {
            while(rs.next()) {
                p.add(readSong());
            }
            }

        catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            closeReadablePlaylist();
            return p;
        }

    }

    @Override
    public void closeWritableLibrary() {
        try{

            preparedStatement.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        closeConnection();
    }

    @Override
    public void closeReadableLibrary() {


        try{
            st.close();
            rs.close();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        closeConnection();
    }

    @Override
    public void closeWritablePlaylist() {


        try{
            preparedStatement.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        closeConnection();
    }

    @Override
    public void closeReadablePlaylist() {


        if(rs!=null){
            try {
                st.close();
                rs.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        closeConnection();
    }

    public void openConnection(){

        try{
            Class.forName ("org.sqlite.JDBC");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        try{

             con = DriverManager.getConnection("jdbc:sqlite:lists.db");
             preparedStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS playlist(id BIGINT, title VARCHAR, album VARCHAR," +
                     " artist VARCHAR, path VARCHAR);");
             preparedStatement.executeUpdate();
             preparedStatement =con.prepareStatement("CREATE TABLE IF NOT EXISTS library(id BIGINT, title VARCHAR, album VARCHAR," +
                     " artist VARCHAR, path VARCHAR);");
             preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void closeConnection(){

        try{
            con.close();
        }
        catch(SQLException e){
            System.out.println("Closing the database connection failed");
            e.printStackTrace();
        }
    }
        public void ResetPreparedStatement(boolean b){
        try {
            if (b) {
                preparedStatement = con.prepareStatement("INSERT INTO library VALUES(?,?,?,?,?)");
            } else {
                preparedStatement = con.prepareStatement("INSERT INTO playlist VALUES(?,?,?,?,?)");

            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        }



}
