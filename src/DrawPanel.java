import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	private static final int GAME_WITH = 1260;
	private static final int GAME_HIGHT = 900;
	public static final int totalSort = 2;//一共3关
	public static boolean isRuning;
	public static boolean[] keyboardPressing;// 记录正在按的键
	public static Stage nowStage = null;// 当前关卡
	public static int sort;
	public BufferedImage backgroundImage = null;
	public static List<Player> players = new CopyOnWriteArrayList<>();
	public static List<Bullet> bullets = new CopyOnWriteArrayList<>();
	public static List<Blast> blasts = new CopyOnWriteArrayList<>();
	public static int fps = 0;
	public Timer timer;//用于控制面板刷新频率
	private long begin, temp, time;// 用于计算帧率
	
	public DrawPanel() {
		setPreferredSize(new Dimension(GAME_WITH, GAME_HIGHT));// 当上一级容器不是绝对布局的时候，这里最好使用setPreferredSize。
		setLayout(null);
		// 下面两步很关键，否则dpanel无法响应键盘事件
		requestFocus(true);
		setFocusable(true);
		keyboardPressing = new boolean[256];
		addKeyListener(new ControlKeyListener());// 给面板添加键盘事件
		backgroundImage = ResourceRepertory.backgrounds[0];// 背景图片。
		timer = new Timer(20, e -> repaint());
		timer.start();
		gameStart();
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);// 绘制背景（注意：背景需要最先画，否则背景处于最上层，看不到其他图形了）
		
		// 画出我方坦克。
		for (Iterator<Player> iterator = players.iterator(); iterator.hasNext();) {
			Player player = iterator.next();
			if(player.fightingTank != null) player.fightingTank.drawMyself(g);
		}

		// 画出敌方坦克。
		for (Iterator<EnemyTank> iterator = nowStage.enemyTanks.iterator(); iterator.hasNext();) {
			iterator.next().drawMyself(g);
		}

		// 子弹在碰撞检测后画出。
		outer: for (Bullet bullet : bullets) {
			for (Player player : players) {
				if ((player.fightingTank != null) && !bullet.isOurs && (player.fightingTank.rectangle.contains(bullet.rectangle))) {
					blasts.add(new Blast(player.fightingTank.tank_x, player.fightingTank.tank_y, 0));
					new Thread(() -> new PlayWav(PlayWav.TANK_BLAST).play()).start();
					bullets.remove(bullet);
					player.fightingTank.blood -= 1;
					if (player.fightingTank.blood <= 0) {
						player.fightTankDestroyed();
						player.creatFightTank();
					}
					if (Player.totalCount <= 0) new Thread(() -> gameOver()).start();
					break outer;
				}
			}

			for (EnemyTank enemyTank : nowStage.enemyTanks) {
				if (bullet.isOurs && (enemyTank.rectangle.contains(bullet.rectangle))) {
					blasts.add(new Blast(enemyTank.tank_x, enemyTank.tank_y, 0));
					new Thread(() -> new PlayWav(PlayWav.TANK_BLAST).play()).start();
					bullets.remove(bullet);
					enemyTank.blood -= 1;
					if (enemyTank.blood <= 0) {
						for(Player player : players) {
							if(player.name.equals(bullet.owner)) {
								player.score += enemyTank.score;
							}
						}
						enemyTank.isAlive = false;
						nowStage.enemyTanks.remove(enemyTank);
						if (nowStage.enemyTanks.isEmpty() && (nowStage.queueOfEnemyTanks.size() == 0)) 
							new Thread(() -> sortWin()).start();
					}
					break outer;
				}
			}
			for (Obstacle obstacle : nowStage.obstacles) {
				if (obstacle.rectangle.intersects(bullet.rectangle)) {
					if (!obstacle.canCrossIn) {
						if (obstacle.canDisdroyed) {
							blasts.add(new Blast(obstacle.x, obstacle.y, 0));
							new Thread(() -> new PlayWav(PlayWav.OBSTACLE_BLAST).play()).start();
							nowStage.obstacles.remove(obstacle);
							bullets.remove(bullet);
						} else {
							blasts.add(new Blast(bullet.bulletHeader.x, bullet.bulletHeader.y, 2));
							new Thread(() -> new PlayWav(PlayWav.STEEL_BLAST).play()).start();
							bullets.remove(bullet);
						}
						break outer;
					}

				}
			}
			if (nowStage.base.isalive && nowStage.base.rectangle.contains(bullet.rectangle)) {
				blasts.add(new Blast(nowStage.base.x, nowStage.base.y, 1));
				new Thread(() -> new PlayWav(PlayWav.BASE_BLAST).play()).start();
				nowStage.base.isalive = false;
				bullets.remove(bullet);
				new Thread(() -> gameOver()).start();
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

		// 计算帧率
		begin = System.currentTimeMillis();
		time = begin - temp;
		if (time != 0)
			fps = (int) (1000 / (time));
		temp = begin;
	}

	// 处理dpanel接收到的keyPressed键盘事件e，并改变键盘数组相应的值，供其他类访问。
	private class ControlKeyListener extends KeyAdapter {
		@Override // 用于坦克移动以及发射子弹
		public void keyPressed(KeyEvent e) {
			keyboardPressing[e.getKeyCode()] = true;
			
			//当按下Esc键时游戏暂停和调出主菜单
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				if (isRuning) {
					gameStop();
					int option = JOptionPane.showOptionDialog(DrawPanel.this, "请选择游戏进度", "游戏进度选择",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
							new String[] { "重新游戏", "返回游戏" }, null);
					switch (option) {
					case 0:
						gameResume();
						endAllTanksThread();
						clearScrean();
						gameStart();
						break;
					case 1:
						gameResume();
						break;
					case -1:
						gameResume();
						break;
					default:
						break;
					}
				}
			}
		}

		@Override // 用于坦克恢复静止
		public void keyReleased(KeyEvent e) {// 键盘抬起触发一次
			keyboardPressing[e.getKeyCode()] = false;
		}
	}
	
	private void gameStart() {
		sort = 0;
		nowStage = new Stage(sort);
		players.add(new Player(MyTank.RED_TANK, "player1"));
		players.add(new Player(MyTank.GREEN_TANK, "player2"));
		isRuning = true;
	}

	@SuppressWarnings("deprecation")
	public void gameStop() {
		nowStage.timer.stop();
		this.timer.stop();
		for(Player player : players) {
			if(player.fightingTank != null) player.fightingTank.keyboardThread.suspend();
		}
		for(EnemyTank enemyTank : nowStage.enemyTanks) {
			enemyTank.thread.suspend();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void gameResume() {
		nowStage.timer.start();
		this.timer.start();
		for(Player player : players) {
			if(player.fightingTank != null) player.fightingTank.keyboardThread.resume();
		}
		for(EnemyTank enemyTank : nowStage.enemyTanks) {
			enemyTank.thread.resume();
		}
	}

	private void sortWin() {
		System.out.println("sort win!!!");
		isRuning = false;
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (sort + 1 > totalSort) {
			System.out.println("game win!!!");
			gameWin();
		} else {
			cleanStage();
			sort += 1;
			nowStage = new Stage(sort);
			for (Player player : players)
				if (player.fightingTank != null)
					player.fightingTank.rest();
			isRuning = true;
		}
	}

	private void gameWin() {
		int option = JOptionPane.showOptionDialog(DrawPanel.this, "请选择游戏进度", "恭喜！你已经通关了！", 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, new String[] {"重新游戏", "返回主菜单"}, null);
		switch (option) {
		case 0:
			gameResume();
			endAllTanksThread();
			clearScrean();
			gameStart();
			break;
		case 1:
			gameResume();
			break;
		case -1:
			gameResume();
			break;

		default:
			break;
		}
		
	}

	private void gameOver() {
		System.out.println("game over!!!");
		isRuning = false;
		endAllTanksThread();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int option = JOptionPane.showOptionDialog(DrawPanel.this, "请选择游戏进度", "你输了", 
												 JOptionPane.YES_NO_OPTION, 
												 JOptionPane.QUESTION_MESSAGE, null, new String[] {"重新游戏", "返回主菜单"}, null);
		switch (option) {
		case 0:
			clearScrean();
			gameStart();
			break;
		case 1:
			break;
		case -1:
			System.out.println("返回主菜单");
			break;

		default:
			break;
		}
	}

	private void endAllTanksThread() {
		nowStage.isCreating = false;
		for (Player player : players) {
			if (player.fightingTank != null) {
				player.fightingTank.isAlive = false;
				player.fightingTank.isMoving = false;
			}
		}
		for (EnemyTank enemyTank : nowStage.enemyTanks) {
			enemyTank.isAlive = false;
			enemyTank.isMoving = false;
		}
	}

	//清除屏幕所有元素
	private void clearScrean() {
		Player.totalCount = 0;
		players.clear();
		cleanStage();
	}
    
    //清除关卡元素
	private void cleanStage() {
		bullets.clear();
		blasts.clear();
		nowStage.clear();
	}
}
