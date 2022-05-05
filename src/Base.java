import java.awt.image.BufferedImage;

public class Base {
	public int x, y;
	public boolean isalive;
	public BufferedImage show = null;
	public Base(int x, int y) {
		this.x = x;
		this.y = y;
		show = ImageUtill.base;
	}
	
}
