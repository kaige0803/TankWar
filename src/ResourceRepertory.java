import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
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
	public static BufferedImage[][] enemyTanks = new BufferedImage[3][4]; 
	public static BufferedImage[] backgrounds = new BufferedImage[10]; 
	public static BufferedImage[] bullet = new BufferedImage[4]; 
	public static BufferedImage[] tankBlasts = new BufferedImage[11];
	public static BufferedImage[] baseBlasts = new BufferedImage[70];
	public static BufferedImage[] steelBlasts = new BufferedImage[8];
	public static BufferedImage[] base = new BufferedImage[2];
	public static BufferedImage[] myTankIcon = new BufferedImage[2];
	public static BufferedImage[] enemyTankIcon = new BufferedImage[3];
	
	//音频
	private static int audioFileCount = 5;
	public static AudioFormat[] auduiFormatArray = new AudioFormat[audioFileCount];//存放音频文件的格式
	public static byte[][] audioDataArrays = new byte[audioFileCount][];//存放音频文件的字节数组
	 
	static {
		try {
			backgrounds[0] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/backgrounds/background1.jpg"));
			++WelcomeWindow.readedFileCount;
			for(int i = 0; i < 4; i++) {
				bullet[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/bullet/bullet" + i + ".png"));
				++WelcomeWindow.readedFileCount;
			}
			for(int i = 0; i < 3; i++) {
				obstacles[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/obstacles/obstacles" + i + ".png"));
				++WelcomeWindow.readedFileCount;
			}
			for(int i = 0; i < 2; i++) {
				base[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/base/base" + i + ".png"));
				++WelcomeWindow.readedFileCount;
			}
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 4; j++) { 
					enemyTanks[i][j] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/enemytanks/enemytank" + i + j + ".png"));
					++WelcomeWindow.readedFileCount;
				}
			}
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 4; j++) {
					myTanks[i][j] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/mytanks/mytank" + i + j + ".png"));
					++WelcomeWindow.readedFileCount;
				}
			}
			for(int i = 0; i < 11; i++) {
				tankBlasts[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/blasts/tank/tank_blast" + i + ".gif"));
				++WelcomeWindow.readedFileCount;
			}
			for(int i = 0; i < 70; i++) {
				baseBlasts[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/blasts/base/base" + (int)(i+1) + ".gif"));
				++WelcomeWindow.readedFileCount;
			}
			for(int i = 0; i < 8; i++) {
				steelBlasts[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/blasts/steel/" + i + ".gif"));
				++WelcomeWindow.readedFileCount;
			}
			for(int i = 0; i < 2; i++) {
				myTankIcon[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/icon/player" + (i+1) + ".png"));
				++WelcomeWindow.readedFileCount;
			}
			for(int i = 0; i < 3; i++) {
				enemyTankIcon[i] = ImageIO.read(ResourceRepertory.class.getClassLoader().getResourceAsStream("images/icon/enemytank" + i + ".png"));
				++WelcomeWindow.readedFileCount;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < audioFileCount; i++) {
			InputStream inputStream = PlayWav.class.getClassLoader().getResourceAsStream("audio/" + i + ".wav");
			BufferedInputStream bi = new BufferedInputStream(inputStream);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				auduiFormatArray[i] = AudioSystem.getAudioInputStream(bi).getFormat();
				int j;
				while ((j = bi.read()) != -1) {
					byteArrayOutputStream.write(j);
				}
				byteArrayOutputStream.flush();
				audioDataArrays[i] = byteArrayOutputStream.toByteArray();
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					inputStream.close();
					bi.close();
					byteArrayOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			++WelcomeWindow.readedFileCount;
		}
		++WelcomeWindow.readedFileCount;
	}
	
	
}
