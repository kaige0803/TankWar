import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

public class PlayWav {
	
	public static final int BASE_BLAST = 0, BULLET_FLYING = 1, OBSTACLE_BLAST = 2, STEEL_BLAST = 3, TANK_BLAST = 4;
	Clip clip;// 每个线程都必须有独立的Clip（播放器）
	
	public PlayWav(int audio_type) {
		try {
			clip = AudioSystem.getClip();
			clip.open(ResourceRepertory.auduiFormat[audio_type], 
					  ResourceRepertory.audioDataArrays[audio_type], 
					  0,
					  ResourceRepertory.audioDataArrays[audio_type].length);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		//调节音量
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(-20.0f);
	}
	
	public void play() {
		clip.start();
	}
	public void loop(int count) {
		clip.loop(count);
	}
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stop() {
		clip.stop();
	}
}