import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlayWav {
	public static final int BASE_BLAST = 0, BULLET_FLYING = 1, OBSTACLE_BLAST = 2, STEEL_BLAST = 3, TANK_BLAST = 4;
	public static final String AUDIO_DIR = System.getProperty("user.dir") + File.separator + "src" + File.separator + "audio";
	private static int file_count = new File(AUDIO_DIR).list().length;
	private static AudioFormat[] audui_format = new AudioFormat[file_count];
	private static byte[][] audio_data_arrays = new byte[file_count][];
	private static InputStream inputStream;
	private static ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	static {
		int j;
		for (int i = 0; i < file_count; i++) {
			inputStream = PlayWav.class.getClassLoader().getResourceAsStream("audio" + File.separator + i + ".wav");
			try {
				audui_format[i] = AudioSystem.getAudioInputStream(inputStream).getFormat();
				while ((j = inputStream.read()) != -1) {
					byteArrayOutputStream.write(j);
				}
				byteArrayOutputStream.flush();
				audio_data_arrays[i] = byteArrayOutputStream.toByteArray();
				byteArrayOutputStream.reset();//初始化字节数组指针。
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
		}
		try {
			byteArrayOutputStream.close();
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PlayWav(int audio_type) {
		try {
			//每个线程都必须有独立的Clip（播放器）
			Clip clip = AudioSystem.getClip();
			clip.open(audui_format[audio_type], audio_data_arrays[audio_type], 0, audio_data_arrays[audio_type].length);
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