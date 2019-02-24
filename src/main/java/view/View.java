package view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.Playlist;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;




public class View extends BorderPane{

    private Button  play, pause, next, commit, addToPlaylist, addAll, remove, save, load;
    private ListView library, playlist;
    private Label title, artist, album, time;
    private TextField titleField, artistField, albumField;
    private ComboBox<String> comboBox;
    private Timeline timeline;


    public void addEventHandlerPlay(EventHandler<ActionEvent> eventHandler) {
        play.addEventHandler(ActionEvent.ACTION, eventHandler);
    }

    public void addEventHandlerPause(EventHandler<ActionEvent> eventHandler) {
        pause.addEventHandler(ActionEvent.ACTION, eventHandler);
    }

    public void addEventHandlerNext(EventHandler<ActionEvent> eventHandler) {
        next.addEventHandler(ActionEvent.ACTION, eventHandler);
    }

    public void addEventHandlerCommit(EventHandler<ActionEvent> eventHandler) {
        commit.addEventHandler(ActionEvent.ACTION, eventHandler);
    }

    public void addEventHandlerAddToPlaylist(EventHandler<ActionEvent> eventHandler) {
        addToPlaylist.addEventHandler(ActionEvent.ACTION, eventHandler);
    }

    public void addEventHandlerAddAll(EventHandler<ActionEvent> eventHandler)  {
        addAll.addEventHandler(ActionEvent.ACTION, eventHandler);
    }


    public void addEventHandlerRemove(EventHandler<ActionEvent> eventHandler) {
        remove.addEventHandler(ActionEvent.ACTION, eventHandler);
    }

    public void addEventHandlerLoad(EventHandler<ActionEvent> eventHandler) {
        load.addEventHandler(ActionEvent.ACTION, eventHandler);
    }

    public void addEventHandlerSave(EventHandler<ActionEvent> eventHandler) {
        save.addEventHandler(ActionEvent.ACTION, eventHandler);
    }


    public Button getAddAll() {
        return addAll;
    }

    public Button getNext() {
        return next;
    }

    public Button getCommit() {
        return commit;
    }

    public Button getAddToPlaylist() {
        return addToPlaylist;
    }

    public Button getLoad() {
        return load;
    }

    public Button getPause() {
        return pause;
    }

    public Button getPlay() {
        return play;
    }

    public Button getRemove() {
        return remove;
    }

    public Button getSave() {
        return save;
    }

    public ComboBox<String> getComboBox(){
        return comboBox;
    }

    public String getTitleField() {
        return titleField.getText();
    }

    public String getArtistField() {
        return artistField.getText();
    }

    public String getAlbumField() {
        return albumField.getText();
    }

    public String getComboSelection(){ return comboBox.getValue(); }

    public String getTime(){return time.getText();}

    public void setTime(String s){ this.time.setText(s);}

    public void setTitleField(String s) {
        this.titleField.setText(s);
    }

    public void setArtistField(String s) {
        this.artistField.setText(s);
    }

    public void setAlbumField(String s) {
        this.albumField.setText(s);
    }

        public void setLibrary(Playlist p){

        this.library.setItems(p);
        }

        public ListView getLibrary(){
            return library;
        }

        public void setPlaylist(Playlist p){
            this.playlist.setItems(p);


        }

        public ListView getPlaylist(){
            return playlist;
        }

        public void setTimeline(int seconds, EventHandler<ActionEvent> e){

            timeline = new Timeline();
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(seconds),e));
            timeline.play();
        }

    public View(){

        play = new Button("play");
        pause = new Button("pause");
        next = new Button("next");
        commit = new Button("commit");
        remove = new Button("remove");
        addToPlaylist = new Button("Add to Playlist");
        addAll= new Button("Add All");
        save = new Button("save");
        load = new Button("load");
        title = new Label("Title");
        artist = new Label("Artist");
        album = new Label("Album");
        time = new Label("");
        titleField = new TextField();
        artistField = new TextField();
        albumField = new TextField();
        playlist = new ListView();
        library = new ListView();
        comboBox = new ComboBox<String>();



        comboBox.getItems().addAll("Binary","XML","OpenJPA","JDBC");





        BorderPane left = new BorderPane();
        BorderPane player = new BorderPane();
        BorderPane right = new BorderPane();

        left.setLeft(library);
        left.setRight(playlist);

        HBox hbox = new HBox();
        hbox.getChildren().addAll(play,pause,next,commit);
        player.setTop(hbox);

        VBox vbox = new VBox();
        vbox.setPrefSize(100,100);
        vbox.setLayoutX(200);
        HBox ser = new HBox();
        ser.getChildren().addAll(comboBox,save,load);
        vbox.getChildren().addAll(title,titleField,artist,artistField,album,albumField);

        right.setTop(player);
        right.setCenter(vbox);
        right.setBottom(ser);

        HBox hbox2 = new HBox();
        hbox2.getChildren().addAll(addToPlaylist,addAll,remove);

        setLeft(left);
        setBottom(hbox2);
        setRight(right);
        setTop(time);








    }


}


