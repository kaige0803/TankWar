import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Stage {
	public int sort;//当前关卡。
	public Base base = null;
	public List<EnemyTank> enemyTanks = new ArrayList<>();
	public BufferedImage backgroundImage = null;
	public List<Obstacle> obstacles = new ArrayList<>();//用于存放当前关卡的所有障碍物。
	public static final int[][][] obstacleArray = { //一共10个关卡，每个关卡把屏幕分割成21✖15个单元。每个单元60✖60像素。用于标记障碍物的位置和type。
			                                      {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,0,1,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,2,2,2,2,2,2,0,0,0,0,0,0,2,2,2,2,2,2,2,0},
			                                       {0,0,0,0,0,0,3,3,3,0,3,3,3,3,3,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,0,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,2,2,2,0,0,0,0,0,0,0,0,0},
			                                       {0,0,0,0,0,0,0,0,0,2,0,2,0,0,0,0,0,0,0,0,0}},//第一关
			                                      {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}},//第二关
			                                      {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}},//第三关
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
		for(int i = 0; i < 15; i++) {//根据关卡生成障碍物列表obstacles<Obstacle>。
			for(int j = 0; j < 21; j++) {
				if(obstacleArray[sort][i][j] != 0) {
					obstacles.add(new Obstacle(j*60, i*60, obstacleArray[sort][i][j] - 1));
				}
			}
		}
		backgroundImage = ImageUtill.backgrounds[sort];//根据关卡生成该关卡的背景图片。
		base = new Base(600, 840);//生成主基地。
		enemyTanks.add(new EnemyTank(0, 0, 0, drawPanel));
		enemyTanks.add(new EnemyTank(180, 0, 1, drawPanel));
		enemyTanks.add(new EnemyTank(360, 0, 2, drawPanel));
		
	}
}
