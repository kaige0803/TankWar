import java.util.ArrayList;
import java.util.List;

public class Stage {
	public int sort;//当前关卡。
	public List<Obstacle> obstacles = new ArrayList<>();
	
	public Stage(int sort) {
		this.sort = sort;
		//根据不同的关卡，构建障碍物和敌方坦克。
		
	}
	
}
