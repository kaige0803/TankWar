import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public final class MainWindow extends JFrame {
	private static final int GAME_WITH = 1080;
	private static final int GAME_HIGHT = 780;
	private DrawPanel dPanel = new DrawPanel();// 绘图首选JPane类，实现了双缓冲
	private Timer timer = new Timer(20, e -> dPanel.repaint());// 定时刷新,每50毫秒一次

	public MainWindow() throws HeadlessException {
		setSize(GAME_WITH, GAME_HIGHT);
		setLocationRelativeTo(null);// 窗口居中显示
		setResizable(false);
		setTitle("坦克大战");
		setContentPane(dPanel);//把窗体的默认根面板设置成dpanel
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new ImageUtill();
		setVisible(true);
		timer.start();// 启动定时刷新，每50毫秒一次
	}
}
