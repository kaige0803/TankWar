import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ResourceRepertory {
	
	//图片
	public static BufferedImage[][] myTanks = new BufferedImage[2][4]; 
	public static BufferedImage[] obstacles = new BufferedImage[3]; 
	public static BufferedImage[][] enemyTank = new BufferedImage[3][4]; 
	public static BufferedImage[] backgrounds = new BufferedImage[10]; 
	public static BufferedImage[] bullet = new BufferedImage[4]; 
	public static BufferedImage[] tankBlasts = new BufferedImage[11];
	public static BufferedImage[] baseBlasts = new BufferedImage[70];
	public static BufferedImage[] steelBlasts = new BufferedImage[8];
	public static BufferedImage[] base = new BufferedImage[2];
	
	//音频
	private static final String AUDIO_DIR = System.getProperty("user.dir") + File.separator + "src" + File.separator + "audio";
	private static int audioFileCount = new File(AUDIO_DIR).list().length;
	public static AudioFormat[] auduiFormat = new AudioFormat[audioFileCount];
	public static byte[][] audioDataArrays = new byte[audioFileCount][];
	private static InputStream inputStream;
	private static ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	 
	static {
		try {
			backgrounds[0] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/backgrounds/background1.jpg"));
			System.out.println(++WelcomeWindow.readedFileCount);
			for(int i = 0; i < 4; i++) {
				bullet[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/bullet/bullet" + i + ".png"));
				System.out.println(++WelcomeWindow.readedFileCount);
			}
			for(int i = 0; i < 3; i++) {
				obstacles[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/obstacles/obstacles" + i + ".png"));
				System.out.println(++WelcomeWindow.readedFileCount);
			}
			for(int i = 0; i < 2; i++) {
				base[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/base/base" + i + ".png"));
				System.out.println(++WelcomeWindow.readedFileCount);
			}
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 4; j++) { 
					enemyTank[i][j] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/enemytanks/enemytank" + i + j + ".png"));
					System.out.println(++WelcomeWindow.readedFileCount);
				}
			}
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 4; j++) {
					myTanks[i][j] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/mytanks/mytank" + i + j + ".png"));
					System.out.println(++WelcomeWindow.readedFileCount);
				}
			}
			for(int i = 0; i < 11; i++) {
				tankBlasts[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/blasts/tank/tank_blast" + i + ".gif"));
				System.out.println(++WelcomeWindow.readedFileCount);
			}
			for(int i = 0; i < 70; i++) {
				baseBlasts[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/blasts/base/base" + (int)(i+1) + ".gif"));
				System.out.println(++WelcomeWindow.readedFileCount);
			}
			for(int i = 0; i < 8; i++) {
				steelBlasts[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/blasts/steel/" + i + ".gif"));
				System.out.println(++WelcomeWindow.readedFileCount);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		for (int i = 0; i < audioFileCount; i++) {
			inputStream = PlayWav.class.getClassLoader().getResourceAsStream("audio" + File.separator + i + ".wav");
			try {
				auduiFormat[i] = AudioSystem.getAudioInputStream(inputStream).getFormat();
				int j;
				while ((j = inputStream.read()) != -1) {
					byteArrayOutputStream.write(j);
				}
				byteArrayOutputStream.flush();
				audioDataArrays[i] = byteArrayOutputStream.toByteArray();
				byteArrayOutputStream.reset();//初始化流内部字节数组指针。
				byteArrayOutputStream.close();
				inputStream.close();
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
			System.out.println(++WelcomeWindow.readedFileCount);
		}
		System.out.println(++WelcomeWindow.readedFileCount);
	}
	
	
}
