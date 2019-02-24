package model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import org.apache.openjpa.persistence.Persistent;
import org.apache.openjpa.persistence.jdbc.Strategy;
import javax.persistence.*;
import java.io.*;


@Entity
@Table(name = "lib")
public class Song implements interfaces.Song, Serializable, Externalizable {

    @Id
    private long id;




    @Persistent
    @Strategy("helper.StringPropertyValueHandler")
    private SimpleStringProperty path = new SimpleStringProperty();

    @Persistent
    @Strategy("helper.StringPropertyValueHandler")
    private SimpleStringProperty title = new SimpleStringProperty();

    @Persistent
    @Strategy("helper.StringPropertyValueHandler")
    private SimpleStringProperty album = new SimpleStringProperty();

    @Persistent
    @Strategy("helper.StringPropertyValueHandler")
    private SimpleStringProperty interpret = new SimpleStringProperty();





    public Song(){ }

    public Song(String path, String title, String album,
                String  interpret){
        this.path.set(path);
        this.title.set(title);
        this.album.set(album);
        this.interpret.set(interpret);
        try{
            id= IDGenerator.getNextID();
        }catch(IDOverFlowException e){
            System.out.println("Cannot initialize an additional Song due to an Excess in IDs");
        }
    }

    @Override
    public String getAlbum() {
        return album.get();
    }


    @Override
    public void setAlbum(String album) {
        this.album.set(album);
    }



    @Override
    public String getInterpret() {
        return interpret.get();
    }

    @Override
    public void setInterpret(String interpret) {
        this.interpret.set(interpret);
    }

    @Override
    public String getPath() {
        return path.get();
    }

    @Override
    public void setPath(String path) {
        this.path.set(path);
    }

    @Override
    public String getTitle() {
        return title.get();
    }

    @Override
    public void setTitle(String title) {
        this.title.set(title);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }




    @Override
    public ObservableValue<String> pathProperty() {
        return path;
    }

    @Override
    public ObservableValue<String> albumProperty() {
        return album;
    }

    @Override
    public ObservableValue<String> interpretProperty() {
        return interpret;
    }

    @Override
    public ObservableValue<String> titleProperty() {
        return title;
    }

    public String toString(){
        return title.getValue();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        out.writeObject(path.get());
        out.writeObject(title.get());
        out.writeObject(album.get());
        out.writeObject(interpret.get());
        out.writeLong(id);


    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        path.set((String)in.readObject());
        title.set((String)in.readObject());
        album.set((String)in.readObject());
        interpret.set((String)in.readObject());
        id = in.readLong();



    }
}
