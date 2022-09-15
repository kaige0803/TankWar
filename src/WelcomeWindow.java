import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class WelcomeWindow extends JWindow implements Runnable {
	
	public JProgressBar progressbar;
	public JLabel label;
	public WelcomeWindow() {
		label = new JLabel();
		label.setIcon(new ImageIcon(WelcomeWindow.class.getResource("images/Splash/Splash.jpg")));
		progressbar = new JProgressBar(0, 100);
		progressbar.setBorderPainted(false);
		progressbar.setStringPainted(true);
		this.add(label, BorderLayout.CENTER);
		this.add(progressbar,BorderLayout.SOUTH);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Thread(new WelcomeWindow()).start());
		try {
			Class.forName("ResourceRepertory");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		while(ResourceRepertory.readedFileCount < ResourceRepertory.fileCount) {
			progressbar.setValue(ResourceRepertory.readedFileCount);
		}
		SwingUtilities.invokeLater(() -> new MainWindow());
		this.dispose();
	}

}
