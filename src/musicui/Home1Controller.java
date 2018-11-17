/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author SUSHIL
 */
public class Home1Controller implements Initializable {

    @FXML
    private MaterialDesignIconView playPause;

    @FXML
    private MaterialDesignIconView playPrevious;
    @FXML
    private MaterialDesignIconView playNext;
    @FXML
    private JFXSlider slider;
    @FXML
    private JFXButton myMusic;
    @FXML
    private JFXButton recentPlays;
    @FXML
    private JFXButton recommend;
    @FXML
    private JFXButton allSong;
    @FXML
    private JFXButton playlist;
    @FXML
    private JFXButton nowPlaying;
    @FXML
    private JFXButton btnSignIn;
    @FXML
    private AnchorPane main;
    @FXML
    private Pane myMusicPane;
    @FXML
    private Pane playlistPane;
    @FXML
    private Pane nowPlayingPane;
    @FXML
    private Pane recentPlaysPane;
    @FXML
    private Pane signinPane;
    @FXML
    private Pane recommendedPane;
    @FXML
    private Pane afterSign;
    @FXML
    private JFXTextField txtname;
    @FXML
    private JFXPasswordField txtpass;

    @FXML
    private ImageView signinImage;
    @FXML
    private ImageView backgroundImage;

    @FXML
    private Label lblSongName;
    @FXML
    private Label lblAfterSign;
    @FXML
    public Label lblTitle;
    @FXML
    public Label loginFail;
    @FXML
    public MenuItem romantic;
    @FXML
    public MenuItem sad;
    @FXML
    public MenuItem comic;
    @FXML
    public MenuItem folk;
    @FXML
    public MenuItem sufi;
    @FXML
    public MenuItem dance;

    @FXML
    public Label notif;
    @FXML

    JFXListView<Label> listView = new JFXListView<>();
    @FXML

    JFXListView<Label> recListView = new JFXListView<>();

    @FXML
    private Label lblcurrentTime;
    @FXML
    private Label lbltotalDuration;
    public static SongInfo songInfo;
    public static int songIndex;
    boolean isSongPlaying = false;
    boolean isPlayPausebtnPressed = false;
    ArrayList<Image> imageList = new ArrayList<>();

    Media media;
    MediaPlayer mediaPlayer;
    int lastSongIndex;
    static File dataFile;
    static FileWriter fw;
    static BufferedReader br;
    static FileInputStream fis;

    String lastSongPlayed;
    Scanner scn;
    public Image img;
    public String title;
    public String artist;
    public String album;
    String currentSongName;
    boolean signIn = false;
    StringBuilder toBeShown;
    static Connection con;
    String mood = "Romantic";
    boolean recNotAdded = true;

    /**
     * Initializes the controller class.
     */
    static {
        con = DBConnection.getConnection();

    }
    private boolean moodChanged;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
        songInfo = new SongInfo();//get songs list ready
        setSongList();//set the songs in ListView
        myMusicPane.toFront();
        lastSongIndex = songInfo.avlSongFiles.size() - 1;
        lblcurrentTime.setText("0:00");
        lbltotalDuration.setText("0:00");
        createDataFile();
        playLastPlayed();
        slider.setValue(0.0);

    }

    //before closing the application it adds the lastPlayed song into the dataFile
    static void addLastPlayed() {

        int songIndex = Home1Controller.songIndex;
        String name = Home1Controller.songInfo.avlSongFiles.get(songIndex).getName();
        try {
            fw = new FileWriter(dataFile);

            fw.write(name);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fw.close();

            } catch (IOException ex) {

            }
        }

    }

    public void addReact() {

    }

    @FXML
    private void playPauseSong() {

        if (isSongPlaying == false) {
            mediaPlayer.play();
            isPlayPausebtnPressed = true;
            playPause.setGlyphName(MaterialDesignIcon.PAUSE_CIRCLE_OUTLINE.toString());
            System.out.println("Playing...");
            isSongPlaying = true;
        } else {
            mediaPlayer.pause();
            playPause.setGlyphName(MaterialDesignIcon.PLAY_CIRCLE_OUTLINE.toString());
            isPlayPausebtnPressed = false;

            System.out.println("stopped...");

            isSongPlaying = false;
        }
    }

    public void setUserMood(ActionEvent event) {
        MenuItem m = (MenuItem) event.getSource();
        mood = m.getText();
        moodChanged = true;

    }

    @FXML
    private void playPreviousSong() {
        mediaPlayer.stop();
        isSongPlaying = false;
        if (songIndex != 0) {
            initializePlayer(songIndex - 1);
            songIndex--;
        } else {
            initializePlayer(lastSongIndex);
            songIndex = lastSongIndex;
        }

    }

    @FXML
    private void playNextSong() {
        mediaPlayer.stop();
        isSongPlaying = false;
        if (songIndex != lastSongIndex) {
            initializePlayer(songIndex + 1);
            songIndex++;
        } else {
            initializePlayer(0);
            songIndex = 0;
        }
    }

    @FXML
    private void getMusic(ActionEvent event) {
        myMusicPane.toFront();

    }

    @FXML
    private void getRecent(ActionEvent event) {
        recentPlaysPane.toFront();
    }

    @FXML
    private void getPlaylist(ActionEvent event) {
        playlistPane.toFront();
    }

    @FXML
    private void getPlaying(ActionEvent event) {
        nowPlayingPane.toFront();
    }

    @FXML
    private void getSignin(ActionEvent event) {
        // signinImage.setImage(new Image(Home1Controller.class.getResourceAsStream("backgroundImage.jpg")));
        if (!signIn) {
            signinPane.toFront();
        }
        else{
            afterSign.toFront();

        }


    }

    private void setSongList() {
        for (File file : songInfo.avlSongFiles) {
            Label l = new Label();
            l.setText(file.getName());
            listView.getItems().add(l);
        }
    }
//only initialize player and assigned the song it does not play the song

    private void initializePlayer(int songIndex) {
        media = new Media(songInfo.avlSongFiles.get(songIndex).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songInfo.currentSongData();
        currentSongName = songInfo.avlSongFiles.get(songIndex).getName();
        if (title != null) {
            lblSongName.setText(title);
            lblTitle.setText(title);

        } else {
            lblSongName.setText(songInfo.avlSongFiles.get(songIndex).getName());
            lblTitle.setText(songInfo.avlSongFiles.get(songIndex).getName());

        }
// if(album!=null){
//        lblAlbum.setText(album);
//        }
//        else{
//        lblAlbum.setText("Not Available");
//        }
//        if(artist!=null){
//        lblArtist.setText(artist);
//        
//        }
//        else{
//        lblArtist.setText("Not Available");
//        }

        listView.getSelectionModel().select(songIndex);
        if (isPlayPausebtnPressed) {
            playPauseSong();
        }
        // setSongData();

        sliderOn();
    }

    /**
     * it gets the song from listView selected item.
     */
    @FXML
    public void getSong() {
        int i = listView.getSelectionModel().getSelectedIndex();
        songIndex = i;
        if (isSongPlaying) {
            mediaPlayer.stop();
            isSongPlaying = false;
        }
        initializePlayer(songIndex);
    }
     @FXML
    public void getRecSong() {
        int i = recListView.getSelectionModel().getSelectedIndex();
        songIndex = i;
        if (isSongPlaying) {
            mediaPlayer.stop();
            isSongPlaying = false;
        }
        initializePlayer(songIndex);
    }

    private void playLastPlayed() {
        //get information from file and play last played song if not available play first song
        boolean lastPlayedSongNotAvl = true;
        for (int i = 0; i < lastSongIndex + 1; i++) {
            if (songInfo.avlSongFiles.get(i).getName().equals(lastSongPlayed)) {
                songIndex = i;
                initializePlayer(i);
                lastPlayedSongNotAvl = false;
            }
        }
        if (lastPlayedSongNotAvl) {
            initializePlayer(0);
        }

    }

    private void createDataFile() {
        dataFile = new File("C:\\Users\\SUSHIL\\Documents\\MusicApp\\dataFile.txt");//dataFile contains playlist and last played
        if (!dataFile.exists()) {

            try {
                dataFile.createNewFile();

            } catch (IOException ex) {

                ex.printStackTrace();
            }

        }
        if (dataFile.exists()) {

            try {
                fis = new FileInputStream(dataFile);
                //br = new BufferedReader(new InputStreamReader(fis));
                scn = new Scanner(fis);

                while (scn.hasNextLine()) {
                    lastSongPlayed = scn.nextLine();
                }

            } catch (IOException ex) {

                ex.printStackTrace();
            } finally {
                try {
                    fis.close();
                    //br.close();
                    scn.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            System.out.println("File does not exist");

        }

    }

    public void setUser() {
        
        signIn = Login.login(con, txtname.getText(), txtpass.getText());
        if (signIn) {
            notif.setText("Welcome,Sushil");
            afterSign.toFront();

        }
        else{
                  loginFail.setText("Wrong username or password");

        }

    }

    public void getRecommeded() {
        recommendedPane.toFront();
        if (signIn && recNotAdded||moodChanged ) {
            try {
                if(recListView!=null){
                recListView.getItems().clear();
                }
                PreparedStatement ps = con.prepareStatement("select * from SongMood where songMood=\'" + mood + "\'");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Label l = new Label();
                    l.setText(rs.getString(2));
                    // System.out.println(rs.getString(2));
                    recListView.getItems().add(l);


                }
                recNotAdded = false;
                moodChanged = false;
            } catch (SQLException ex) {
            }

        }

    }

    public void setSongImage(Image img) {
    }

    public void sliderOn() {

        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                slider.setMax(mediaPlayer.getTotalDuration().toSeconds());

                double td = mediaPlayer.getTotalDuration().toMinutes();
                String tds = Double.toString(td);
                int pi = tds.indexOf(".");
                String min = "";

                try {

                    min = tds.substring(0, pi);
                } catch (Exception e) {
                    System.out.println("cought exception from song:");

                }
//Exception raiser
                String sec = "0".concat(tds.substring(pi + 1, tds.length()));
                double secs = Double.valueOf(sec) * 60.0;
                sec = String.valueOf(secs);
                StringBuilder sb = new StringBuilder(sec);
                int pi1 = sec.indexOf(".");
                sb.deleteCharAt(pi1);
                sec = sb.substring(0, 2);
                if (Double.valueOf(sec) > 60.0) {
                    sec = "0".concat(sec.substring(0, 1));
                }
                lbltotalDuration.setText(min + ":" + sec);

                lblcurrentTime.setText("0:00");

                mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                        slider.setValue(newValue.toSeconds());

                        double currentSongMinRaw = mediaPlayer.getCurrentTime().toMinutes();
                        String currentSongMinString = Double.toString(currentSongMinRaw);
                        StringBuilder toBeChecked = new StringBuilder();
                        toBeChecked.append("0.");
                        int pointIndex = currentSongMinString.indexOf(".");

                        toBeChecked.append(currentSongMinString.charAt(pointIndex + 1));
                        if (pointIndex + 2 < currentSongMinString.length()) {
                            toBeChecked.append(currentSongMinString.charAt(pointIndex + 2));
                        }
                        double secs = Double.parseDouble(toBeChecked.toString()) * 60.0;

                        StringBuilder secString = new StringBuilder();
                        if (secs < 10) {
                            secString.append("0");
                            secString.append(Character.toString(Double.toString(secs).charAt(0)));
                        } else if (secs > 10) {
                            secString.append(Character.toString(Double.toString(secs).charAt(0)));
                            secString.append(Character.toString(Double.toString(secs).charAt(1)));
                        }
//                 int   min = (int) (currentSongMinRaw + (currentSongMinRaw - Double.parseDouble(toBeChecked.toString())));

                        toBeShown = new StringBuilder();
                        toBeShown.append(currentSongMinString.substring(0, currentSongMinString.indexOf(".")));
                        toBeShown.append(":");
                        toBeShown.append(secString.toString());
                        lblcurrentTime.setText(toBeShown.toString());

                    }

                });

                slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        mediaPlayer.seek(Duration.seconds(slider.getValue()));

                    }

                });
                slider.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        mediaPlayer.seek(Duration.seconds(slider.getValue()));

                    }

                });

            }

        });

    }

//    private void setSongData() {
//if (title != null) {
//            lblSongName.setText(title);
//            lblTitle.setText(title);
//            
//
//        } else {
//            lblSongName.setText(songInfo.avlSongFiles.get(songIndex).getName());
//            lblTitle.setText(songInfo.avlSongFiles.get(songIndex).getName());
//            
//        }
//        if(album!=null){
//        lblAlbum.setText(album);
//        }
//        else{
//        lblAlbum.setText("Not Available");
//        }
//        if(artist!=null){
//        lblArtist.setText(artist);
//        
//        }
//        else{
//        lblArtist.setText("Not Available");
//        }
//
//    }
    public String setMood(int songIndex, String songName, String songMood) {
        if(songName.contains("\'")){
       songName= songName.replace("\'", "");
        }
        try {
            PreparedStatement ps1 = con.prepareStatement("select * from SongMood where songName=?");
            ps1.setString(1, songName);
            ResultSet rs = ps1.executeQuery();
            boolean isAlreadyAdded;

            if (isAlreadyAdded = rs.next()) {
                return "Song \'" + songName + "\' is already added with category \'" + rs.getString(3) + "\'";

            } else {

                PreparedStatement ps2 = con.prepareStatement("insert into SongMood values(" + songIndex + ",\'" + songName + "\',\'" + songMood + "\')");
                int k = ps2.executeUpdate();
                if (k == 1) {
                    return "\'" + songName + "\' added to category \'" + songMood;
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "failed";
    }

    @FXML
    private void addReaction(ActionEvent event) {
        JFXButton b = (JFXButton) event.getSource();
        if (b.getText().equals("Romantic")) {
            String returned = setMood(songIndex, currentSongName, "Romantic");
            System.out.println(returned);
        } else if (b.getText().equals("Sad")) {
            String returned = setMood(songIndex, currentSongName, "Sad");
            System.out.println(returned);}
        else if (b.getText().equals("Comic")) {
            String returned = setMood(songIndex, currentSongName, "Comic");
            System.out.println(returned);
        } else if (b.getText().equals("Folk")) {
            String returned = setMood(songIndex, currentSongName, "Folk");
            System.out.println(returned);
        } else if (b.getText().equals("Sufi")) {
            String returned = setMood(songIndex, currentSongName, "Sufi");
            System.out.println(returned);
        } else if (b.getText().equals("Dance")) {
            String returned = setMood(songIndex, currentSongName, "Dance");
            System.out.println(returned);
        }
    }

}
