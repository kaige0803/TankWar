import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.Timer;

public class Stage {
	private static Random r = new Random();// 用于随机生成敌方坦克的状态和子弹。
	public int sort;// 当前关卡。
	private Properties stageProperty = new Properties();// 关卡的配置信息(包括障碍物和敌方坦克）
	private String propertyValue;
	private String[] propertyValueXY;// 用于存放障碍物坐标字符串。
	public Base base = null;
	public boolean isCreating = true;
	public Thread thread;// 自动定时生成敌方坦克的线程。
	public List<EnemyTank> enemyTanks = new CopyOnWriteArrayList<>();
	public Queue<EnemyTank> queueOfEnemyTanks = new LinkedList<>();
	public List<Obstacle> obstacles = new CopyOnWriteArrayList<>();// 用于存放当前关卡的所有障碍物。
	public Timer timer;
	
	public Stage(int sort) {
		this.sort = sort;
		// 根据关卡加载配置文件
		try {
			stageProperty.load(
					Stage.class.getClassLoader().getResourceAsStream("stageProperties/stage" + sort + ".properies"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 根据配置文件生成敌方坦克队列
		for (int i = 0; i < Integer.parseInt(stageProperty.getProperty("type0")); i++) {
			queueOfEnemyTanks.offer(new EnemyTank(60 * r.nextInt(21), 0, EnemyTank.NORMAL));
		}
		for (int i = 0; i < Integer.parseInt(stageProperty.getProperty("type1")); i++) {
			queueOfEnemyTanks.offer(new EnemyTank(60 * r.nextInt(21), 0, EnemyTank.SPEED));
		}
		for (int i = 0; i < Integer.parseInt(stageProperty.getProperty("type2")); i++) {
			queueOfEnemyTanks.offer(new EnemyTank(60 * r.nextInt(21), 0, EnemyTank.ARMOR));
		}
		
		// 根据配置文件生成障碍物列表obstacles。
		for (Enumeration<?> enumeration = stageProperty.propertyNames(); enumeration.hasMoreElements();) {
			switch ((String) enumeration.nextElement()) {
			case "steel":
				propertyValue = stageProperty.getProperty("steel");
				propertyValueXY = propertyValue.split(",");
				for (int i = 0; i < propertyValueXY.length; i = i + 2) {
					obstacles.add(new Obstacle(Integer.parseInt(propertyValueXY[i]),
							Integer.parseInt(propertyValueXY[i + 1]), Obstacle.STEEL));
				}
				break;
			case "stone":
				propertyValue = stageProperty.getProperty("stone");
				propertyValueXY = propertyValue.split(",");
				for (int i = 0; i < propertyValueXY.length; i = i + 2) {
					obstacles.add(new Obstacle(Integer.parseInt(propertyValueXY[i]),
							Integer.parseInt(propertyValueXY[i + 1]), Obstacle.STONE));
				}
				break;
			case "grass":
				propertyValue = stageProperty.getProperty("grass");
				propertyValueXY = propertyValue.split(",");
				for (int i = 0; i < propertyValueXY.length; i = i + 2) {
					obstacles.add(new Obstacle(Integer.parseInt(propertyValueXY[i]),
							Integer.parseInt(propertyValueXY[i + 1]), Obstacle.GRASS));
				}
				break;

			default:
				break;
			}
		}

		base = new Base(600, 840);// 生成主基地。

		// 将queueOfEnemyTanks里的敌方坦克逐个定时加入enemyTanks
		timer = new Timer(3000, new enemyTankCreate());
		timer.start();
	}

	public void clear() {
		enemyTanks.clear();
		obstacles.clear();
	}

	
	private class enemyTankCreate implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (queueOfEnemyTanks.size() != 0 && isCreating) {
				queueOfEnemyTanks.element().thread.start();
				enemyTanks.add(queueOfEnemyTanks.poll());
			}else {
				timer.stop();
			}
		}
	}
}
