package com.sokoban.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;

/**
 * The class MusicModel stores all the functionality with music playing.
 * @author Yuyang LIN
 */
public class MusicModel {

    private MediaPlayer player;


    /**
     * Setup the player to play the music constantly.
     * @throws LineUnavailableException     Cannot find the music
     */
    public void createPlayer() throws LineUnavailableException {
        File filePath = new File("src/main/resources/music/puzzle_theme.wav");
        Media music = new Media(filePath.toURI().toString());
        player = new MediaPlayer(music);
        player.setAutoPlay(true);
        player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
    }

    /**
     * Play the music.
     */
    public void playMusic() {
        player.play();
    }

    /**
     * Stop the music.
     */
    public void stopMusic() {
        player.stop();
    }

    /**
     * To check if the music is playing.
     * @return {@code true} if the music is playing, {@code false} otherwise
     */
    public boolean isPlayingMusic() {
        return player.getStatus() == MediaPlayer.Status.PLAYING;
    }

}
