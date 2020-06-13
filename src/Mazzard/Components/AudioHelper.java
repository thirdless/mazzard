package Mazzard.Components;

import Mazzard.Resources;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioHelper {
    private Clip backgroundAudio;
    private long backgroundTime;
    private int swing;

    private Clip jumpClip;
    private Clip swing1Clip;
    private Clip swing2Clip;

    public AudioHelper(){
        swing = 0;
        try{
            backgroundAudio = AudioSystem.getClip();
            AudioInputStream input = AudioSystem.getAudioInputStream(Resources.getInstance().getStream("audio/background.wav"));
            backgroundAudio.open(input);
            backgroundTime = 0;

            swing1Clip = getClip("swing1");
            swing2Clip = getClip("swing2");
            jumpClip = getClip("jump");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private Clip getClip(String file){
        Clip clip = null;
        try{
            clip = AudioSystem.getClip();
            AudioInputStream input = AudioSystem.getAudioInputStream(Resources.getInstance().getStream("audio/" + file + ".wav"));
            clip.open(input);
            return clip;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return clip;
    }

    private void playClip(Clip clip){
        if(!Resources.getInstance().getSettings().getMuted()){
            clip.setMicrosecondPosition(0);
            clip.start();
        }
    }

    public void backgroundStart(){
        if(!Resources.getInstance().getSettings().getMuted()){
            backgroundAudio.setMicrosecondPosition(backgroundTime);
            backgroundAudio.start();
            backgroundAudio.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void resetBackground(){
        backgroundTime = 0;
    }

    public void backgroundPause(){
        backgroundTime = backgroundAudio.getMicrosecondPosition();
        backgroundAudio.stop();
    }

    public void playSwing(){
        if(swing == 1){
            swing = 0;
            playClip(swing1Clip);
        }
        else{
            swing = 1;
            playClip(swing2Clip);
        }
    }

    public void playJump(){
        playClip(jumpClip);
    }
}
