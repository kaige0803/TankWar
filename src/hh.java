import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

public class hh extends JWindow implements Runnable {
	// 定义加载窗口大小
	public static final int LOAD_WIDTH = 300;
	public static final int LOAD_HEIGHT = 150;
	// 获取屏幕窗口大小
	public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	// 定义进度条组件
	public JProgressBar progressbar;
	// 定义标签组件
	public JLabel label;
	public hh() {
		// 创建标签,并在标签上放置一张图片
		label = new JLabel();
		label.setIcon(new ImageIcon(hh.class.getResource("images/Splash/Splash.jpg")));
		label.setBounds(0, 0, LOAD_WIDTH, LOAD_HEIGHT - 15);
		// 创建进度条
		progressbar = new JProgressBar();
		// 显示当前进度值信息
		progressbar.setStringPainted(false);
		// 设置进度条边框不显示
		progressbar.setBorderPainted(false);
		// 设置进度条的前景色
		progressbar.setForeground(new Color(0, 210, 40));
		// 设置进度条的背景色
		progressbar.setBackground(new Color(188, 190, 194));
		progressbar.setBounds(0, LOAD_HEIGHT - 15, LOAD_WIDTH, 15);
		// 添加组件
		this.add(label);
		this.add(progressbar);
		// 设置布局为空
		this.setLayout(null);
		// 设置窗口初始位置
		this.setLocation((WIDTH - LOAD_WIDTH) / 2, (HEIGHT - LOAD_HEIGHT) / 2);
		// 设置窗口大小
		this.setSize(LOAD_WIDTH, LOAD_HEIGHT);
		// this.setSize(label.getWidth(),label.getHeight());
		// 设置窗口显示
		this.setVisible(true);
	}
	public static void main(String[] args) {
		hh t = new hh();
		new Thread(t).start();
	}
	public void run() {
		for (int i = 0; i < 110; i += (int) Math.random() * 10 + 1) {
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			progressbar.setValue(i);
		}
		JOptionPane.showMessageDialog(this, "加载完成");
		this.dispose();

	}

}
