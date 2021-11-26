import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public final class MainWindow extends JFrame {
	private static final int GAME_WITH = 1000;
	private static final int GAME_HIGHT = 1000;
	private DrawPanel dPanel = new DrawPanel();// 绘图首选JPane类，实现了双缓冲
	Timer timer = new Timer(50, e -> dPanel.repaint());// 定时刷新

	public MainWindow() throws HeadlessException {
		setSize(GAME_WITH, GAME_HIGHT);
		setLocationRelativeTo(null);// 窗口居中显示
		setResizable(false);
		setTitle("坦克大战");
		setContentPane(dPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		timer.start();// 启动定时刷新，每50毫秒一次
	}

}
