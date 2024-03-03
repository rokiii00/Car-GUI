import javax.sound.sampled.*;
import java.io.File;

public class Music
{
    Clip clip;
    float volume;
    FloatControl fc;
    boolean mute = false;

    public void setFile(File path)
    {
        try
        {
            if(path.exists())
            {
                AudioInputStream audio_input = AudioSystem.getAudioInputStream(path);
                clip = AudioSystem.getClip();
                clip.open(audio_input);
                fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }
            else
            {
                System.out.println("cannot find file");
            }
        }

        catch (Exception e)
        {

        }
    }



    public void play()
    {
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop()
    {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop()
    {
        clip.stop();
    }

    public void change_volume()
    {
        fc.setValue(volume);
    }


}
