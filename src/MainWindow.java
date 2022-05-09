import javax.swing.JFrame;

@SuppressWarnings("serial")
public final class MainWindow extends JFrame {
	
	private DrawPanel dPanel = new DrawPanel();// 绘图首选JPane类，实现了双缓冲
	//private Timer timer = new Timer(50, e -> dPanel.repaint());// 定时刷新,每20毫秒一次

	public MainWindow(){
		setUndecorated(true);//去掉窗口所有的装饰，这句最好写在最前。
		setResizable(false);//窗口不可调整。
		getContentPane().add(dPanel);//把dpanel添加到窗体的根面板。
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();//一定要加这句。
		setLocationRelativeTo(null);// 窗口居中显示。这句要加在窗口添加完所有组件并打包之后。
		setTitle("坦克大战");
		setVisible(true);
		new ImageUtill();//加载资源文件。
		new Thread(dPanel).start();// 启动定时刷新dPanel，每50毫秒一次
	}
}
