
public class Main {

	public static void main(String[] args) throws Exception {
		MainWindow mainWindow = new MainWindow();
		while(true) {
			Thread.sleep(150);
			mainWindow.repaint();
		}
		
	}

}
