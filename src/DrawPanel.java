import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements Runnable {
	private static final int GAME_WITH = 1260;
	private static final int GAME_HIGHT = 900;
	public static int player1_count, player2_count;
	public boolean[] keyboardPressing;// 记录正在按的键
	public Stage nowStage = null;// 当前关卡
	public int sort;
	public BufferedImage backgroundImage = null;
	public List<MyTank> myTanks = new CopyOnWriteArrayList<>();
	public List<Bullet> bullets = new CopyOnWriteArrayList<>();
	public List<Blast> blasts = new CopyOnWriteArrayList<>();
	private long temp, begin, time;// 用于计算帧率
	private Font font = new Font("微软雅黑", Font.BOLD, 18);

	public DrawPanel() {
		setPreferredSize(new Dimension(GAME_WITH, GAME_HIGHT));// 当上一级容器不是绝对布局的时候，这里最好使用setPreferredSize。
		setLayout(null);
		// 下面两步很关键，否则dpanel无法响应键盘事件
		requestFocus(true);
		setFocusable(true);
		keyboardPressing = new boolean[256];
		addKeyListener(new ControlKeyListener());// 给面板添加键盘事件
		sort = 0;
		nowStage = new Stage(0, this);
		backgroundImage = ImageUtill.backgrounds[0];// 根据关卡生成该关卡的背景图片。
		player1_count = 3;
		player2_count = 3;
		myTanks.add(new MyTank(0, this));// 生成一辆我方坦克
		myTanks.add(new MyTank(1, this));// 生成一辆我方坦克
		new Thread(this).start();
	}

	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.drawImage(backgroundImage, 0, 0, null);// 绘制背景（注意：背景需要最先画，否则背景处于最上层，看不到其他图形了）
		
		// 画出我方坦克。
		for (Iterator<MyTank> iterator = myTanks.iterator(); iterator.hasNext();) {
			iterator.next().drawMyself(g);
		}

		// 画出敌方坦克。
		for (Iterator<EnemyTank> iterator = nowStage.enemyTanks.iterator(); iterator.hasNext();) {
			iterator.next().drawMyself(g);
		}

		// 子弹在碰撞检测后画出。
		outer: for (Bullet bullet : bullets) {
			for (MyTank myTank : myTanks) {
				if ((bullet.owner.equals("enemytank")) && (myTank.rectangle.contains(bullet.rectangle))) {
					blasts.add(new Blast(myTank.tank_x, myTank.tank_y, 0));
					new Thread(() -> new PlayWav(PlayWav.TANK_BLAST)).start();
					bullets.remove(bullet);
					myTank.blood -= 1;
					if (myTank.blood <= 0) {
						myTank.isalive = false;
						myTanks.remove(myTank);
						switch (myTank.player) {
						case 0:
							player1_count -= 1;
							if (player1_count > 0) {
								new Thread(() -> {
									try {
										Thread.sleep(3000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if(nowStage.base.isalive) myTanks.add(new MyTank(myTank.player, this));
								}).start();
							}
							break;
						case 1:
							player2_count -= 1;
							if (player2_count > 0) {
								new Thread(() -> {
									try {
										Thread.sleep(3000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if(nowStage.base.isalive) myTanks.add(new MyTank(myTank.player, this));
								}).start();
							}
							break;
						default:
							break;
						}
					}
					break outer;
				}
			}

			for (EnemyTank enemyTank : nowStage.enemyTanks) {
				if ((bullet.owner.equals("mytank")) && (enemyTank.rectangle.contains(bullet.rectangle))) {
					blasts.add(new Blast(enemyTank.tank_x, enemyTank.tank_y, 0));
					new Thread(() -> new PlayWav(PlayWav.TANK_BLAST)).start();
					enemyTank.isalive = false;
					bullets.remove(bullet);
					enemyTank.blood -= 1;
					if (enemyTank.blood <= 0) {
						enemyTank.isalive = false;
						nowStage.enemyTanks.remove(enemyTank);
					}
					break outer;
				}
			}
			for (Obstacle obstacle : nowStage.obstacles) {
				if (obstacle.rectangle.intersects(bullet.rectangle)) {
					if (!obstacle.canCrossIn) {
						if (obstacle.canDisdroyed) {
							blasts.add(new Blast(obstacle.x, obstacle.y, 0));
							new Thread(() -> new PlayWav(PlayWav.OBSTACLE_BLAST)).start();
							nowStage.obstacles.remove(obstacle);
							bullets.remove(bullet);
						} else {
							blasts.add(new Blast(obstacle.x, obstacle.y, 2));
							new Thread(() -> new PlayWav(PlayWav.STEEL_BLAST)).start();
							bullets.remove(bullet);
						}
						break outer;
					}

				}
			}
			if (nowStage.base.isalive && nowStage.base.rectangle.contains(bullet.rectangle)) {
				blasts.add(new Blast(nowStage.base.x, nowStage.base.y, 1));
				new Thread(() -> new PlayWav(PlayWav.BASE_BLAST)).start();
				nowStage.base.isalive = false;
				bullets.remove(bullet);
				break outer;
			}
			if (bullet.bullet_x < 0 || bullet.bullet_x > 1260 || bullet.bullet_y < 0 || bullet.bullet_y > 900) {
				bullets.remove(bullet);
				break outer;
			}
			bullet.drawMyself(g);
		}
		
		// 画出当前关卡障碍物。
		for (Obstacle obstacle : nowStage.obstacles) {
			g.drawImage(obstacle.show, obstacle.x, obstacle.y, this);
		}

		// 画出主基地。
		g.drawImage(nowStage.base.getshow(), nowStage.base.x, nowStage.base.y, this);

		// 画出爆炸，每一帧画一张。
		for (Blast blast : blasts) {
			if (blast.step <= blast.sum - 1)
				blast.drawMyself(g);
			else {
				blasts.remove(blast);
			}
		}

		// 计算帧率并绘制字符串
		g.setColor(Color.DARK_GRAY);
		g.setFont(font);
		begin = System.currentTimeMillis();
		time = begin - temp;
		if (time != 0)
			g.drawString("fps：" + (int) (1000 / (time)), 30, 740);
		temp = begin;
		
		//绘制游戏信息字符串
		g.drawString("player1剩余数量：" + player1_count, 30, 770);
		g.drawString("player2剩余数量：" + player2_count, 30, 800);
		g.drawString("敌方剩余坦克数量：" + nowStage.queueOfEnemyTanks.size(), 30, 830);
		g.drawString("我方坦克数量：" + myTanks.size(), 30, 860);
		g.setColor(c);
	}

	// 处理dpanel接收到的keyPressed键盘事件e，并改变键盘数组相应的值，供其他类访问。
	private class ControlKeyListener extends KeyAdapter {

		@Override // 用于坦克移动以及发射子弹
		public void keyPressed(KeyEvent e) {
			keyboardPressing[e.getKeyCode()] = true;
		}

		@Override // 用于坦克恢复静止
		public void keyReleased(KeyEvent e) {// 键盘抬起触发一次
			keyboardPressing[e.getKeyCode()] = false;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {// 用于控制游戏模式和进度
		while (true) {
			if (nowStage.base.isalive == false || (player1_count + player2_count) <= 0) {
				System.out.println("game over!!!");
				nowStage.isCreating = false;
				nowStage.thread.stop();
				for (MyTank myTank : myTanks)
					myTank.isalive = false;
				for(EnemyTank enemyTank : nowStage.enemyTanks) {
					enemyTank.isalive = false;
				}
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				myTanks.clear();
				cleanScrean();
				nowStage = new Stage(sort, this);
				player1_count = 3;
				player2_count = 3;
				myTanks.add(new MyTank(0, this));
				myTanks.add(new MyTank(1, this));
			}
			if (nowStage.enemyTanks.isEmpty() && (nowStage.queueOfEnemyTanks.size() == 0)) {
				System.out.println("you win!!!");
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cleanScrean();
				sort++;
				nowStage = new Stage(sort, this);
				for (MyTank myTank : myTanks)
					myTank.rest();
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void cleanScrean() {
		bullets.clear();
		blasts.clear();
		nowStage.clear();
	}
}
