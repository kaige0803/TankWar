import java.awt.image.BufferedImage;

public class Obstacle {
	public int x, y;
	public int type;
	public BufferedImage show;
	public boolean canDisdroyed;
	public boolean isalive;
	public Stage stage = null;
	
	public Obstacle(int x, int y, int type, Stage stage) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.stage = stage;
		if(type == 0) canDisdroyed = false; else canDisdroyed = true;//只有钢铁无法被摧毁。
		show = ImageUtill.obstacles[type];
	}
	
}
