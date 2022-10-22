import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Bullet {
	private int bulletSpeed = 30;
	public  int bullet_x, bullet_y;//用于接收生成子弹的这一时刻坦克的位置
	public String owner;
	private TankState state;//用于接收生成子弹的这一时刻坦克的状态
	public Rectangle rectangle;
	public Point bulletHeader = new Point();
	public boolean isOurs;
	public Bullet(int tank_x, int tank_y, TankState state, String owner, boolean isOurs) {//需要根据生成这颗子弹的时候的坦克的位置和状态，来确定子弹的初始位置和状态。
		this.state = state;//方向和坦克的保持一致。
		this.owner = owner;
		this.isOurs = isOurs;
		switch (this.state) {
			case DOWN:
				this.bullet_x = tank_x + 27; this.bullet_y = tank_y + 50;
				rectangle = new Rectangle(bullet_x, bullet_y, 6, 26);
				bulletHeader.x = bullet_x + 3;
				bulletHeader.y = bullet_y + 26;
				break;
			case LEFT:
				this.bullet_x = tank_x - 16; this.bullet_y = tank_y + 27;
				rectangle = new Rectangle(bullet_x, bullet_y, 26, 6);
				bulletHeader.x = bullet_x;
				bulletHeader.y = bullet_y + 3;
				break;
			case RIGHT:
				this.bullet_x = tank_x + 50; this.bullet_y = tank_y + 27;
				rectangle = new Rectangle(bullet_x, bullet_y, 26, 6);
				bulletHeader.x = bullet_x + 26;
				bulletHeader.y = bullet_y + 3;
				break;
			case UP:
				this.bullet_x = tank_x + 27; this.bullet_y = tank_y - 16;
				rectangle = new Rectangle(bullet_x, bullet_y, 6, 26);
				bulletHeader.x = bullet_x + 3;
				bulletHeader.y = bullet_y;
				break;
	
			default:
				break;
		}
	}

	public void drawMyself(Graphics g) {
		// 根据坦克的状态调用不同的子弹图。
		switch (this.state) {
		case DOWN:
			g.drawImage(ResourceRepertory.bullet[2], bullet_x, bullet_y, null);
			bullet_y += bulletSpeed;
			rectangle.y = bullet_y;
			bulletHeader.y += bulletSpeed;
			break;
		case LEFT:
			g.drawImage(ResourceRepertory.bullet[3], bullet_x, bullet_y, null);
			bullet_x -= bulletSpeed;
			rectangle.x = bullet_x;
			bulletHeader.x -= bulletSpeed;
			break;
		case RIGHT:
			g.drawImage(ResourceRepertory.bullet[1], bullet_x, bullet_y, null);
			bullet_x += bulletSpeed;
			rectangle.x = bullet_x;
			bulletHeader.x += bulletSpeed;
			break;
		case UP:
			g.drawImage(ResourceRepertory.bullet[0], bullet_x, bullet_y, null);
			bullet_y -= bulletSpeed;
			rectangle.y = bullet_y;
			bulletHeader.y -= bulletSpeed;
			break;

		default:
			break;
		}

	}

}
