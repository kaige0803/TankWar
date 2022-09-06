import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);// 调用父类构造函数，绘制出基本框架（这句必须写，且必须写在第一句）
		Color c = g.getColor();
		// System.out.println(g.hashCode());通过调用g和g2d的hashcode值我们发现是相等的，强转后指向同一对象。
		// System.out.println(g2d.hashCode());
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(backgroundImage, 0, 0, null);// 绘制背景（注意：背景需要最先画，否则背景处于最上层，看不到其他图形了）

		for (Iterator<MyTank> iterator = myTanks.iterator(); iterator.hasNext();) {// 画出我方坦克。
			MyTank myTank = iterator.next();
			myTank.paintMyself(g2d);
		}

		for (Iterator<EnemyTank> iterator = nowStage.enemyTanks.iterator(); iterator.hasNext();) {// 画出地方坦克。
			EnemyTank enemyTank = iterator.next();
			enemyTank.drawMyself(g2d);
		}

		// 子弹的碰撞检测后画出。
		outer: for (Bullet bullet : bullets) {
			for (MyTank myTank : myTanks) {
				if ((bullet.owner.equals("enemytank")) && (myTank.rectangle.contains(bullet.rectangle))) {
					blasts.add(new Blast(myTank.tank_x, myTank.tank_y, 0));
					new Thread(() -> new PlayWav("audio/tank_blast.wav")).start();
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
									myTanks.add(new MyTank(myTank.player, this));
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
									myTanks.add(new MyTank(myTank.player, this));
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
					new Thread(() -> new PlayWav("audio/tank_blast.wav")).start();
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
							new Thread(() -> new PlayWav("audio/obstacle_blast.wav")).start();
							nowStage.obstacles.remove(obstacle);
							bullets.remove(bullet);
						} else {
							blasts.add(new Blast(obstacle.x, obstacle.y, 2));
							new Thread(() -> new PlayWav("audio/steel_blast.wav")).start();
							bullets.remove(bullet);
						}
						break outer;
					}

				}
			}
			if (nowStage.base.isalive && nowStage.base.rectangle.contains(bullet.rectangle)) {
				blasts.add(new Blast(nowStage.base.x, nowStage.base.y, 1));
				new Thread(() -> new PlayWav("audio/base_blast.wav")).start();
				nowStage.base.isalive = false;
				bullets.remove(bullet);
				break outer;
			}
			if (bullet.bullet_x < 0 || bullet.bullet_x > 1260 || bullet.bullet_y < 0 || bullet.bullet_y > 900) {
				bullets.remove(bullet);
				break outer;
			}
			bullet.drawMyself(g2d);
		}

		for (Iterator<Obstacle> iterator = nowStage.obstacles.iterator(); iterator.hasNext();) {// 画出当前关卡障碍物。
			Obstacle obstacle = iterator.next();
			g2d.drawImage(obstacle.show, obstacle.x, obstacle.y, this);
		}

		g2d.drawImage(nowStage.base.getshow(), nowStage.base.x, nowStage.base.y, this);// 画出主基地。

		for (Blast blast : blasts) {// 画出爆炸，每一帧画一张。
			if (blast.step <= blast.sum - 1)
				blast.drawMyself(g2d);
			else {
				blasts.remove(blast);
			}
		}

		// 计算帧率并绘制字符串
		g2d.setColor(Color.DARK_GRAY);
		g2d.setFont(font);
		begin = System.currentTimeMillis();
		time = begin - temp;
		if (time != 0)
			g.drawString("fps：" + (int) (1000 / (time)), 30, 740);
		temp = begin;
		g2d.drawString("player1剩余数量：" + player1_count, 30, 770);
		g2d.drawString("player2剩余数量：" + player2_count, 30, 800);
		g2d.drawString("敌方剩余坦克数量：" + nowStage.queueOfEnemyTanks.size(), 30, 830);
		g2d.drawString("我方坦克数量：" + myTanks.size(), 30, 860);
		g.setColor(c);
	}

	// 处理dpanel接收到的keyPressed键盘事件e，并改变控制坦克所需的state，供drawMyself函数画图
	private class ControlKeyListener extends KeyAdapter {

		@Override // 用于坦克移动
		public void keyPressed(KeyEvent e) {
			keyboardPressing[e.getKeyCode()] = true;
		}

		@Override // 用于坦克恢复静止以及发射子弹
		public void keyReleased(KeyEvent e) {// 键盘抬起触发一次
			keyboardPressing[e.getKeyCode()] = false;
		}
	}

	@Override
	public void run() {// 用于控制游戏模式和进度
		while (true) {
			if (nowStage.base.isalive == false || (player1_count + player2_count) <= 0) {
				System.out.println("game over!!!");
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (MyTank myTank : myTanks)
					myTank.isalive = false;
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
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void cleanScrean() {
		nowStage.isCreating = false;
		nowStage.thread.stop();
		bullets.clear();
		blasts.clear();
		nowStage.clear();
	}
}
