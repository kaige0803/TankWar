import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Bullet {
	private int bullet_speed = 30;
	public  int bullet_x, bullet_y;//用于接收生成子弹的这一时刻坦克的位置
	public String owner;
	private State state;//用于接收生成子弹的这一时刻坦克的状态
	public Rectangle rectangle;
	
	public Bullet(int tank_x, int tank_y, State state, String owner) {//需要根据生成这颗子弹的时候的坦克的位置和状态，来确定子弹的初始位置和状态。
		this.state = state;//方向和坦克的保持一致。
		this.owner = owner;
		if(state == State.DOWN) {
			this.bullet_x = tank_x + 27; this.bullet_y = tank_y + 50;
		}
		if(state == State.LEFT) {
			this.bullet_x = tank_x - 16; this.bullet_y = tank_y + 27;
		}
		if(state == State.RIGHT) {
			this.bullet_x = tank_x + 50; this.bullet_y = tank_y + 27;
		}
		if(state == State.UP) {
			this.bullet_x = tank_x + 27; this.bullet_y = tank_y - 16;
		}
		rectangle = new Rectangle(bullet_x, bullet_y, 6, 6);
	}

	public void drawMyself(Graphics2D g2d) {
		// 根据坦克的状态调用不同的子弹图潘，并调整子弹位置。

		if (state == State.DOWN) {
			g2d.drawImage(ImageUtill.bullet[2], bullet_x, bullet_y, null);
			bullet_y += bullet_speed;
			rectangle.y = bullet_y;
		}
		if (state == State.LEFT) {
			g2d.drawImage(ImageUtill.bullet[3], bullet_x, bullet_y, null);
			bullet_x -= bullet_speed;
			rectangle.x = bullet_x;
		}
		if (state == State.RIGHT) {
			g2d.drawImage(ImageUtill.bullet[1], bullet_x, bullet_y, null);
			bullet_x += bullet_speed;
			rectangle.x = bullet_x;
		}
		if (state == State.UP) {
			g2d.drawImage(ImageUtill.bullet[0], bullet_x, bullet_y, null);
			bullet_y -= bullet_speed;
			rectangle.y = bullet_y;
		}

	}

}
