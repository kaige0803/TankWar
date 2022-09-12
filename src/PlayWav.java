import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlayWav {
	//SampleRate:44100.0   SampleSizeInBits:16   Channels:1   FrameSize:2   FrameRate:44100.0
	private static AudioFormat format_base_blast;
	private static AudioFormat format_bullet_flying;
	private static AudioFormat format_obstacle_blast;
	private static AudioFormat format_steel_blast;
	private static AudioFormat format_tank_blast;
	private static AudioFormat format_tank_born;
	private static byte[] base_blast_array, bullet_flying_array, obstacle_blast_array, 
							steel_blast_array, tank_blast_array, tank_born_array;
	private static BufferedInputStream bufferedInputStream;
	private static ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	static {
		int i;
		try {
			format_base_blast = AudioSystem.getAudioInputStream(
					PlayWav.class.getClassLoader().getResourceAsStream("audio/base_blast.wav")).getFormat();
			format_bullet_flying = AudioSystem.getAudioInputStream(
					PlayWav.class.getClassLoader().getResourceAsStream("audio/bullet_flying.wav")).getFormat();
			format_obstacle_blast = AudioSystem.getAudioInputStream(
					PlayWav.class.getClassLoader().getResourceAsStream("audio/obstacle_blast.wav")).getFormat();
			format_steel_blast = AudioSystem.getAudioInputStream(
					PlayWav.class.getClassLoader().getResourceAsStream("audio/steel_blast.wav")).getFormat();
			format_tank_blast = AudioSystem.getAudioInputStream(
					PlayWav.class.getClassLoader().getResourceAsStream("audio/tank_blast.wav")).getFormat();
			format_tank_born = AudioSystem.getAudioInputStream(
					PlayWav.class.getClassLoader().getResourceAsStream("audio/tank_born.wav")).getFormat();
			
			bufferedInputStream = new BufferedInputStream(
					PlayWav.class.getClassLoader().getResourceAsStream("audio/bullet_flying.wav"));
			while ((i = bufferedInputStream.read()) != -1) {
				byteArrayOutputStream.write(i);
			}
			bullet_flying_array = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.reset();
			
			bufferedInputStream = new BufferedInputStream(
					PlayWav.class.getClassLoader().getResourceAsStream("audio/base_blast.wav"));
			while ((i = bufferedInputStream.read()) != -1) {
				byteArrayOutputStream.write(i);
			}
			base_blast_array = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.reset();
			
			
			bufferedInputStream = new BufferedInputStream(
					PlayWav.class.getClassLoader().getResourceAsStream("audio/obstacle_blast.wav"));
			while ((i = bufferedInputStream.read()) != -1) {
				byteArrayOutputStream.write(i);
			}
			obstacle_blast_array = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.reset();
			
			bufferedInputStream = new BufferedInputStream(
					PlayWav.class.getClassLoader().getResourceAsStream("audio/steel_blast.wav"));
			while ((i = bufferedInputStream.read()) != -1) {
				byteArrayOutputStream.write(i);
			}
			steel_blast_array = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.reset();
			
			bufferedInputStream = new BufferedInputStream(
					PlayWav.class.getClassLoader().getResourceAsStream("audio/tank_blast.wav"));
			while ((i = bufferedInputStream.read()) != -1) {
				byteArrayOutputStream.write(i);
			}
			tank_blast_array = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.reset();
			
			bufferedInputStream = new BufferedInputStream(
					PlayWav.class.getClassLoader().getResourceAsStream("audio/tank_born.wav"));
			while ((i = bufferedInputStream.read()) != -1) {
				byteArrayOutputStream.write(i);
			}
			tank_born_array = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.reset();
			
		} catch (IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	public void play_base_blast() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(format_base_blast, base_blast_array, 0, base_blast_array.length);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void play_bullet_flying() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(format_bullet_flying, bullet_flying_array, 0, bullet_flying_array.length);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void play_obstacle_blast() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(format_obstacle_blast, obstacle_blast_array, 0, obstacle_blast_array.length);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void play_steel_blast() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(format_steel_blast, steel_blast_array, 0, steel_blast_array.length);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void play_tank_blast() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(format_tank_blast, tank_blast_array, 0, tank_blast_array.length);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void play_tank_born() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(format_tank_born, tank_born_array, 0, tank_born_array.length);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	// public PlayWav(String file) {
	// try {
	// AudioInputStream audioInputStream = AudioSystem
	// .getAudioInputStream(PlayWav.class.getClassLoader().getResourceAsStream(file));
	// AudioFormat audioFormat = audioInputStream.getFormat();
	// Info dataLineInfo = new Info(SourceDataLine.class, audioFormat);
	// SourceDataLine sourceDataLine = (SourceDataLine)
	// AudioSystem.getLine(dataLineInfo);
	// byte[] b = new byte[1024];
	// int len = 0;
	// sourceDataLine.open(audioFormat, 1024);
	// sourceDataLine.start();
	// while ((len = audioInputStream.read(b)) > 0) {
	// sourceDataLine.write(b, 0, len);
	// }
	// audioInputStream.close();
	// sourceDataLine.drain();
	// sourceDataLine.close();
	// } catch (UnsupportedAudioFileException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (LineUnavailableException e) {
	// e.printStackTrace();
	// }
	//
	// }
}