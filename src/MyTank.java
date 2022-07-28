import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class MyTank implements Runnable {

	public int tank_x, tank_y;// 坦克位置
	private int tank_speed = 5;// 坦克速度
	public int player;
	public String name;
	private DrawPanel drawPanel = null;
	public State state = State.UP_STAY;// 坦克初始状态为向上静止
	public Rectangle rectangle;
	public boolean isalive = true;
	private int[][] controlkeys = {{KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_H},
								   {KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_KP_DOWN}};
	private Thread keyboardThread;
	private boolean canFire = true;

	public MyTank(int tank_x, int tank_y, int player, DrawPanel drawPanel) {
		super();
		this.drawPanel = drawPanel;
		this.player = player;
		this.tank_x = tank_x;
		this.tank_y = tank_y;
		rectangle = new Rectangle(tank_x, tank_y, 60, 60);
		keyboardThread = new Thread(this);
		keyboardThread.start();
	}

	public void paintMyself(Graphics2D g2d) {// 接受dpanel传来的画笔g2d，将坦克自己画在dpanel上
		switch (state) {
		case LEFT_MOVING:
			g2d.drawImage(ImageUtill.myTanks[player][3], tank_x, tank_y, null);
			if (tank_x > 0 && canMoveLeft()) {// 判断坦克是否到达边界或者遇到障碍物。
				tank_x -= tank_speed;
				rectangle.x = tank_x;
			}
			break;
		case RIGHT_MOVING:
			g2d.drawImage(ImageUtill.myTanks[player][1], tank_x, tank_y, null);
			if (tank_x < 1200 && canMoveRight()) {
				tank_x += tank_speed;
				rectangle.x = tank_x;
			}
			break;
		case UP_MOVING:
			g2d.drawImage(ImageUtill.myTanks[player][0], tank_x, tank_y, null);
			if (tank_y > 0 && canMoveUp()) {
				tank_y -= tank_speed;
				rectangle.y = tank_y;
			}
			break;
		case DOWN_MOVING:
			g2d.drawImage(ImageUtill.myTanks[player][2], tank_x, tank_y, null);
			if (tank_y < 840 && canMoveDown()) {
				tank_y += tank_speed;
				rectangle.y = tank_y;
			}
			break;
		case LEFT_STAY:
			g2d.drawImage(ImageUtill.myTanks[player][3], tank_x, tank_y, null);
			break;
		case RIGHT_STAY:
			g2d.drawImage(ImageUtill.myTanks[player][1], tank_x, tank_y, null);
			break;
		case UP_STAY:
			g2d.drawImage(ImageUtill.myTanks[player][0], tank_x, tank_y, null);
			break;
		case DOWN_STAY:
			g2d.drawImage(ImageUtill.myTanks[player][2], tank_x, tank_y, null);
			break;

		default:
			break;
		}
		if (drawPanel.keyboardPressing[controlkeys[player][4]]) fire();
	}

	private boolean canMoveDown() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if ((tank_y == obstacle.y - 60) && (tank_x < obstacle.x + 60) && (tank_x > obstacle.x - 60)
					&& (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if ((tank_y == enemyTank.tank_y - 60) && (tank_x < enemyTank.tank_x + 60)
					&& (tank_x > enemyTank.tank_x - 60))
				return false;
		}
		return true;
	}

	private boolean canMoveUp() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if ((tank_y == obstacle.y + 60) && (tank_x < obstacle.x + 60) && (tank_x > obstacle.x - 60)
					&& (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if ((tank_y == enemyTank.tank_y + 60) && (tank_x < enemyTank.tank_x + 60)
					&& (tank_x > enemyTank.tank_x - 60))
				return false;
		}
		return true;
	}

	private boolean canMoveRight() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if ((tank_x == obstacle.x - 60) && (tank_y < obstacle.y + 60) && (tank_y > obstacle.y - 60)
					&& (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if ((tank_x == enemyTank.tank_x - 60) && (tank_y < enemyTank.tank_y + 60)
					&& (tank_y > enemyTank.tank_y - 60))
				return false;
		}
		return true;
	}

	private boolean canMoveLeft() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if ((tank_x == obstacle.x + 60) && (tank_y < obstacle.y + 60) && (tank_y > obstacle.y - 60)
					&& (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if ((tank_x == enemyTank.tank_x + 60) && (tank_y < enemyTank.tank_y + 60)
					&& (tank_y > enemyTank.tank_y - 60))
				return false;
		}
		return true;
	}

	public void fire() {
		if (canFire) {
			drawPanel.bullets.add(new Bullet(tank_x, tank_y, state, "mytank"));
			new Thread(() -> new PlayWav("audio/bullet_flying.wav")).start();
			new Thread(() -> {
				canFire = false;
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(canFire);
				canFire = true;
			}).start();
		}
	}

	@Override
	public void run() {
		while (isalive) {
			if (drawPanel.keyboardPressing[controlkeys[player][0]])
				state = State.UP_MOVING;
			else if (drawPanel.keyboardPressing[controlkeys[player][1]])
				state = State.RIGHT_MOVING;
			else if (drawPanel.keyboardPressing[controlkeys[player][2]])
				state = State.DOWN_MOVING;
			else if (drawPanel.keyboardPressing[controlkeys[player][3]])
				state = State.LEFT_MOVING;
			else  {
				if (drawPanel.keyboardPressed[controlkeys[player][0]])
					state = State.UP_STAY;
				if (drawPanel.keyboardPressed[controlkeys[player][1]])
					state = State.RIGHT_STAY;
				if (drawPanel.keyboardPressed[controlkeys[player][2]])
					state = State.DOWN_STAY;
				if (drawPanel.keyboardPressed[controlkeys[player][3]])
					state = State.LEFT_STAY;
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			keyboardThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
