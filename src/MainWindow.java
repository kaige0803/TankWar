import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public final class MainWindow extends JFrame {

	private DrawPanel dPanel; // 绘图首选JPane类，实现了双缓冲
	private Timer timer;
	private InformationPanel iPanel;

	public MainWindow() {
		this.setUndecorated(true);// 去掉窗口所有的装饰，这句最好写在最前。
		this.setResizable(false);// 窗口不可调整。
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("坦克大战");
		this.dPanel = new DrawPanel();
		this.iPanel = new InformationPanel();
		this.getContentPane().add(dPanel, BorderLayout.CENTER);// 把dpanel添加到窗体的根面板。
		this.getContentPane().add(iPanel, BorderLayout.EAST);
		this.pack();// 一定要加这句。
		this.setLocationRelativeTo(null);// 窗口居中显示。这句要加在窗口添加完所有组件并打包pack()之后。
		this.setVisible(true);
		timer = new Timer(20, e -> dPanel.repaint());// 定时刷新,每20毫秒一次
		new Thread(iPanel).start();
		timer.start();// 启动定时刷新dPanel
	}
}
