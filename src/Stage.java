import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Stage implements Runnable {
	public int sort;// 当前关卡。
	private Random r = new Random();// 用于随机生成敌方坦克的状态和子弹。
	private Properties stageProperty = new Properties();// 关卡的配置信息(包括障碍物和敌方坦克）
	private String propertyValue;
	private String[] propertyValueXY;// 用于存放障碍物坐标字符串。
	public Base base = null;
	public boolean isCreating = true;
	public DrawPanel drawPanel;
	public Thread thread;// 自动定时生成敌方坦克的线程。
	public int totalEenemyTankCount;
	public List<EnemyTank> enemyTanks = new CopyOnWriteArrayList<>();
	public Queue<EnemyTank> queueOfEnemyTanks = new LinkedList<>();
	public List<Obstacle> obstacles = new CopyOnWriteArrayList<>();// 用于存放当前关卡的所有障碍物。

	public Stage(int sort, DrawPanel drawPanel) {
		this.sort = sort;
		this.drawPanel = drawPanel;

		// 根据关卡加载配置文件
		try {
			stageProperty.load(
					Stage.class.getClassLoader().getResourceAsStream("stageProperties/stage" + sort + ".properies"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 根据配置文件生成敌方坦克队列
		totalEenemyTankCount = Integer.parseInt(stageProperty.getProperty("totalEenemyTankCount"));
		for (int i = 0; i < Integer.parseInt(stageProperty.getProperty("type0")); i++) {
			queueOfEnemyTanks.offer(new EnemyTank(60 * r.nextInt(21), 0, 0, drawPanel));
		}
		for (int i = 0; i < Integer.parseInt(stageProperty.getProperty("type1")); i++) {
			queueOfEnemyTanks.offer(new EnemyTank(60 * r.nextInt(21), 0, 1, drawPanel));
		}
		for (int i = 0; i < Integer.parseInt(stageProperty.getProperty("type2")); i++) {
			queueOfEnemyTanks.offer(new EnemyTank(60 * r.nextInt(21), 0, 2, drawPanel));
		}
		
		// 根据配置文件生成障碍物列表obstacles。
		for (Enumeration<?> enumeration = stageProperty.propertyNames(); enumeration.hasMoreElements();) {
			switch ((String) enumeration.nextElement()) {
			case "steel":
				propertyValue = stageProperty.getProperty("steel");
				propertyValueXY = propertyValue.split(",");
				for (int i = 0; i < propertyValueXY.length; i = i + 2) {
					obstacles.add(new Obstacle(Integer.parseInt(propertyValueXY[i]),
							Integer.parseInt(propertyValueXY[i + 1]), 0));
				}
				break;
			case "stone":
				propertyValue = stageProperty.getProperty("stone");
				propertyValueXY = propertyValue.split(",");
				for (int i = 0; i < propertyValueXY.length; i = i + 2) {
					obstacles.add(new Obstacle(Integer.parseInt(propertyValueXY[i]),
							Integer.parseInt(propertyValueXY[i + 1]), 1));
				}
				break;
			case "grass":
				propertyValue = stageProperty.getProperty("grass");
				propertyValueXY = propertyValue.split(",");
				for (int i = 0; i < propertyValueXY.length; i = i + 2) {
					obstacles.add(new Obstacle(Integer.parseInt(propertyValueXY[i]),
							Integer.parseInt(propertyValueXY[i + 1]), 2));
				}
				break;

			default:
				break;
			}
		}

		base = new Base(600, 840);// 生成主基地。

		// 开启自动定时生成敌方坦克的线程。
		thread = new Thread(this);
		thread.start();
	}

	public void clear() {
		enemyTanks.clear();
		obstacles.clear();
	}

	// 将queueOfEnemyTanks里的敌方坦克逐个定时加入enemyTanks
	@Override
	public void run() {
		while (queueOfEnemyTanks.size() != 0 && isCreating) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//new Thread(() -> new PlayWav().play_tank_born()).start();
			queueOfEnemyTanks.element().thread.start();
			enemyTanks.add(queueOfEnemyTanks.poll());
		}
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
