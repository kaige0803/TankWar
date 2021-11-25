import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public final class MainWindow extends JFrame {
	private static final int GAME_WITH = 1000;
	private static final int GAME_HIGHT = 1000;
	private DrawPanel dPanel = new DrawPanel();

	public MainWindow() throws HeadlessException {
		setSize(GAME_WITH, GAME_HIGHT);
		setBackground(Color.BLACK);//无效
		setLocationRelativeTo(null);//窗口居中显示
		setResizable(false);
		setTitle("坦克大战");
		dPanel.setBackground(Color.BLUE);// 重写dPanel的paintcomponent后此行无效
		setContentPane(dPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void startgame() throws InterruptedException {
		while(true) {
			Thread.sleep(10);
			dPanel.repaint();
		}
	}
}
