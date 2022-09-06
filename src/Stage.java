import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Stage implements Runnable{
	public int sort;//当前关卡。
	private Random r = new Random();
	private Properties stageProperty = new Properties();//关卡的配置信息(包括障碍物和敌方坦克）
	public Base base = null;
	public boolean isCreating = true;
	public DrawPanel drawPanel;
	public Thread thread;
	public int totalEenemyTankCount;
	public List<EnemyTank> enemyTanks = new CopyOnWriteArrayList<>();
	public Queue<EnemyTank> queueOfEnemyTanks = new LinkedList<>();
	public List<Obstacle> obstacles = new CopyOnWriteArrayList<>();//用于存放当前关卡的所有障碍物。
	public static final int[][][] obstacleArray = { //一共10个关卡，每个关卡把屏幕分割成21✖15个单元。每个单元60✖60像素。用于标记障碍物的位置和type。
			                                       //第一关   1:钢铁  2：石头 3：草丛
			                                      {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,2,2,2,1,2,0,0,0,0,0,0,0,2,2,2,2,0,2,0,0},
			                                       {3,3,3,1,3,3,3,1,3,1,1,3,1,3,1,3,1,1,3,1,3},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,2,2,2,2,2,2,0,0,0,0,0,0,2,2,2,2,2,2,2,0},
			                                       {0,0,0,0,0,0,3,3,3,0,3,3,3,3,3,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,0,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,2,0,1,0,0,0,0,0,0,0,0,0}},
			                                       //第二关
			                                      {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				                                   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				                                   {0,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,0,1,0,0},
				                                   {0,0,0,3,0,3,0,0,0,3,0,3,0,3,0,3,0,0,3,0,0},
				                                   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				                                   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				                                   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				                                   {0,2,2,2,2,2,0,0,0,0,0,0,0,0,2,2,2,2,2,2,0},
				                                   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				                                   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				                                   {0,0,1,1,3,1,1,1,3,0,0,0,0,3,1,1,3,1,0,0,0},
				                                   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},
				                                   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				                                   {0,0,0,0,0,0,0,0,0,2,2,2,0,0,0,0,0,0,0,0,0},
				                                   {0,0,0,0,0,0,0,0,0,2,0,2,0,0,0,0,0,0,0,0,0}},
			                                       //第三关
			                                      {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					                               {0,0,0,0,0,0,0,3,0,0,0,0,3,0,0,0,0,0,0,0,0},
					                               {0,1,2,1,2,0,2,0,0,0,0,0,0,1,3,2,3,0,2,0,0},
					                               {0,0,0,3,0,3,2,0,0,3,2,3,0,3,2,2,0,0,3,0,0},
					                               {0,0,0,0,3,3,3,3,2,2,2,2,0,2,0,2,0,0,0,0,0},
					                               {0,3,0,0,3,3,3,0,3,0,2,2,0,0,0,3,0,0,0,0,0},
					                               {0,0,0,3,3,0,0,2,2,0,2,2,0,0,0,0,0,0,0,0,0},
					                               {0,2,2,2,2,2,0,0,2,0,2,2,0,0,2,2,2,2,2,2,0},
					                               {0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0},
					                               {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					                               {0,0,1,1,3,1,1,1,3,0,0,0,0,3,1,1,3,1,0,0,0},
					                               {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},
					                               {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					                               {0,0,0,0,0,0,0,0,0,2,2,2,0,0,0,0,0,0,0,0,0},
					                               {0,0,0,0,0,0,0,0,0,2,0,2,0,0,0,0,0,0,0,0,0}},
			                                      
			                                      {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}},//第四关
			                                      {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}},//第五关
			                                      {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}},//第六关
			                                      {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}},//第七关
			                                      {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}},//第八关
			                                      {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}},//第九关
			                                      {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}} //第十关
			                                      };
	public Stage(int sort, DrawPanel drawPanel) {
		this.sort = sort;
		this.drawPanel = drawPanel;
		
		//根据关卡加载配置文件
		try {
			stageProperty.load(Stage.class.getClassLoader().getResourceAsStream("stageProperties/stage" + sort + ".properies"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//根据配置文件生成障碍物列表obstacles。
		
		
		for(int i = 0; i < 15; i++) {//根据关卡生成障碍物列表obstacles<Obstacle>。
			for(int j = 0; j < 21; j++) {
				if(obstacleArray[sort][i][j] != 0) {
					obstacles.add(new Obstacle(j*60, i*60, obstacleArray[sort][i][j] - 1));
				}
			}
		}
		
		
		//根据配置文件生成敌方坦克队列。
		totalEenemyTankCount = 10 + 4 * (sort + 1);
		for (int i = 0; i < totalEenemyTankCount; i++) {
			queueOfEnemyTanks.offer(new EnemyTank(60*r.nextInt(21), 0, r.nextInt(3), drawPanel));
		}
		
		base = new Base(600, 840);//生成主基地。
		
		//开启自动定时生成敌方坦克的线程。
		thread = new Thread(this);
		thread.start();
	}
	
	public void clear() {
		for(EnemyTank enemyTank : enemyTanks) enemyTank.isalive = false;
		enemyTanks.clear();
		obstacles.clear();
	}
	
	//将queueOfEnemyTanks里的敌方坦克逐个定时加入enemyTanks
	@Override
	public void run() {
		while (queueOfEnemyTanks.size() != 0 && isCreating) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
