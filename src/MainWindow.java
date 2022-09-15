import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public final class MainWindow extends JFrame {

	private DrawPanel dPanel; // 绘图首选JPane类，实现了双缓冲
	private Timer timer;

	public MainWindow() {
		setUndecorated(true);// 去掉窗口所有的装饰，这句最好写在最前。
		setResizable(false);// 窗口不可调整。
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("坦克大战");
		dPanel = new DrawPanel();
		getContentPane().add(dPanel);// 把dpanel添加到窗体的根面板。
		pack();// 一定要加这句。
		setLocationRelativeTo(null);// 窗口居中显示。这句要加在窗口添加完所有组件并打包之后。
		setVisible(true);
		timer = new Timer(20, e -> dPanel.repaint());// 定时刷新,每20毫秒一次
		timer.start();// 启动定时刷新dPanel
	}
}
