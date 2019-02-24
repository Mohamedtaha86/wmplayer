package strategy;

import interfaces.Playlist;
import interfaces.SerializableStrategy;
import interfaces.Song;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class OpenJPAStrategy implements SerializableStrategy {

    private EntityManagerFactory fac = null;
    private EntityManager e = null;


    @Override
    public void openWritableLibrary() throws IOException {


        openConnection();

    }

    @Override
    public void openReadableLibrary() throws IOException {

        openConnection();

    }

    @Override
    public void openWritablePlaylist() throws IOException {


    }

    @Override
    public void openReadablePlaylist() throws IOException {


    }

    @Override
    public void writeSong(Song s) throws IOException {
        EntityTransaction t = e.getTransaction();
        t.begin();
        e.persist(s);
        t.commit();


    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public void writeLibrary(Playlist p) throws IOException {

        openWritableLibrary();
        EntityTransaction t = e.getTransaction();
        t.begin();
        Query query= e.createNativeQuery("CREATE TABLE IF NOT EXISTS lib(id bigint," +
                "title varchar, album varchar, interpret varchar, path varchar)");
        query.executeUpdate();
        query =  e.createNativeQuery("DELETE FROM lib");
        query.executeUpdate();
        t.commit();
        for(Song s: p){
            writeSong(s);
        }
        closeWritableLibrary();

    }

    @Override
    public Playlist readLibrary() throws IOException, ClassNotFoundException {

        openReadableLibrary();
        model.Playlist p = new model.Playlist();

        EntityTransaction t = e.getTransaction();
        t.begin();
        for (Object o : e.createQuery("SELECT x FROM Song x").getResultList()) {
            Song s = (Song) o;
            p.add(s);

        }
        t.commit();
        closeReadableLibrary();
        return p;



    }

    @Override
    public void writePlaylist(Playlist p) throws IOException {


    }

    @Override
    public Playlist readPlaylist() throws IOException, ClassNotFoundException {


        return new model.Playlist();
    }

    @Override
    public void closeWritableLibrary() {
        closeConnection();
    }

    @Override
    public void closeReadableLibrary() {
        closeConnection();
    }

    @Override
    public void closeWritablePlaylist() {


    }

    @Override
    public void closeReadablePlaylist() {


    }

    private void openConnection(){

        fac = getWithoutConfig();
        e = fac.createEntityManager();

    }

    private void closeConnection(){

        e.close();
        fac.close();
    }

    private  EntityManagerFactory getWithoutConfig() {

        Map<String, String> map = new HashMap<String, String>();

        map.put("openjpa.ConnectionURL","jdbc:sqlite:lists.db");
        map.put("openjpa.ConnectionDriverName", "org.sqlite.JDBC");
        map.put("openjpa.RuntimeUnenhancedClasses", "supported");
        map.put("openjpa.jdbc.SynchronizeMappings", "false");
        map.put("openjpa.MetaDataFactory", "jpa(Types=" + model.Song.class.getName().toString()+ ")");

        return OpenJPAPersistence.getEntityManagerFactory(map);

    }


}
