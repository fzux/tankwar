package com.tank.util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;

public class MusicUtil {
    private static AudioClip start;
    private static AudioClip bomb;
    private static AudioClip backgroundmusic;

    //装载音乐资源
    static {
        try {
            start = Applet.newAudioClip(new File("res/start.wav").toURL());
            bomb = Applet.newAudioClip(new File("res/fire.wav").toURL());
            backgroundmusic = Applet.newAudioClip(new File("res/war.wav").toURL());

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void playStart(){
        start.play();//播放音频
    }

    public static void playBomb(){
        bomb.play();//播放音频
    }

    public static void playBackgroundmusic(){
        backgroundmusic.play();
    }

    public static void stopMusic(){backgroundmusic.stop();}


}
