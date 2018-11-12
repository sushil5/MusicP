/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicui;

import java.io.File;
import java.util.ArrayList;
import javafx.collections.MapChangeListener;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

/**
 *
 * @author SUSHIL
 */
public class SongInfo {

    public ArrayList<File> avlSongFiles = new ArrayList<>();//it contains all music files .mp3 only
    String dirPath = "E:\\Music"; //head directory path
    String artist;
    String album;
    String title;
    String duration;
    Image albumart;
    Thread t;
    Home1Controller hc=new Home1Controller();


    SongInfo() {

        getAvlSongFiles();
        System.out.println("total music files:" + avlSongFiles.size());

    }
//gives all mp3 files in given directory and its subdirectories

    private void getAvlSongFiles() {
        File directory = new File(dirPath);
        addMp3(directory);

    }
//current:print the names of all files

    private void getAvlSongFileNames() {
        if (avlSongFiles.size() > 1) {
            avlSongFiles.forEach((file) -> {
                System.out.println(file.getName());
            });

        } else {
            System.out.println("No mp3 file found");
        }

    }

    public void getMetaData() {

        try {
            Media media = new Media(avlSongFiles.get(Home1Controller.songIndex).toURI().toString());
            media.getMetadata().addListener(new MapChangeListener<String, Object>() {
                @Override
                public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> ch) {
                    if (ch.wasAdded()) {
                        if (ch.getKey().equals("album")) {
                            album = ch.getValueAdded().toString();
                        }
                        if (ch.getKey().equals("artist")) {
                            artist = ch.getValueAdded().toString();

                        }
                        if (ch.getKey().equals("title")) {
                          hc.title = ch.getValueAdded().toString();
                            

                        }
                        if (ch.getValueAdded() instanceof Image) {
                            Image img = (Image) ch.getValueAdded();
                            hc.setSongImage(img);
                        }
                        if (ch.getKey().equals("duration")) {
                            duration = ch.getValueAdded().toString();
                        }

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//recursive method to add all mp3 from directory and subdirectory
    private void addMp3(File directory) {
        try {
            File[] allFiles = directory.listFiles();

            for (File file : allFiles) {
                if (file.isDirectory()) {
                    addMp3(file);

                } else {
                    if (file.getName().endsWith(".mp3")) {

                        avlSongFiles.add(file);

                    }
                }

            }

        } catch (Exception e) {
            System.out.println("caught exception:" + "cannot find any directory or file at" + dirPath);

        }

    }

    public void currentSongData() {
        getMetaData();
//        System.out.println(album);
//        System.out.println(artist);
//
//        System.out.println(title);
//
//        System.out.println(duration);

    }

}
