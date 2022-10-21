import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

@SuppressWarnings("serial")
public class MyInformationPanel extends JPanel implements Runnable {
	
	public DefaultTableModel dm = new DefaultTableModel(new Object[][] {}, new Object[] {"Player", "Life", "Score"}){
		//可以指定每个列具体的对象类型，供渲染器使用，也可以在渲染器里直接指定。
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		}
		//指定哪些列是可以修改
		@Override
		public boolean isCellEditable(int row, int column) {
				return false;
		}
	};
	
	public JTable jTable = new JTable(dm);
	private JScrollPane jScrollPane =  new JScrollPane(jTable);
	private JTableHeader jTableHeader = jTable.getTableHeader();
	//创建默认渲染器，使得表格和表头的样式一致
	private DefaultTableCellRenderer myTableCellRenderer = new DefaultTableCellRenderer();
	private Font font1 = new Font("微软雅黑", Font.BOLD, 18);
	private Font font2 = new Font("微软雅黑", Font.BOLD, 14);
	private Thread thread;
	
	public MyInformationPanel() {
		//设置并组装myInformationPanel面板。
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(InformationPanel.WIDTH,180));
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.WHITE, 1, true), 
                                                 "My_Tanks", 
                                                 TitledBorder.DEFAULT_JUSTIFICATION, 
                                                 TitledBorder.DEFAULT_POSITION, 
                                                 font2, Color.WHITE));
		myTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		jTableHeader.setReorderingAllowed(false);//列不可拖动
		jTableHeader.setResizingAllowed(false);//列大小不可改变
		jTableHeader.setFont(font1);
		jTableHeader.setBackground(Color.DARK_GRAY);
		jTableHeader.setForeground(Color.WHITE);
		jTableHeader.setDefaultRenderer(myTableCellRenderer);
		jTable.setEnabled(false);//禁止用户编辑和选择
		jTable.setGridColor(Color.DARK_GRAY);
		jTable.setBackground(Color.DARK_GRAY);
		jTable.setRowHeight(60);
		jTable.setForeground(Color.WHITE);
		jTable.setFont(font1);
		jTable.setDefaultRenderer(Object.class, myTableCellRenderer);
		//添加玩家数据行
		for (Player player : DrawPanel.players) {
			dm.addRow(new Object[] {new ImageIcon(ResourceRepertory.myTankIcon[player.myTankType]), "" + player.count, "" + player.score});
		}
		jScrollPane.setBorder(new LineBorder(Color.WHITE, 0, true));
		jScrollPane.setOpaque(false);
		jScrollPane.getViewport().setOpaque(false);
		add(jScrollPane);
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		//更新myInformationPanel面板数据
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			for (Player player : DrawPanel.players) {
				if(player.count == 0) {
					dm.setValueAt("Over", player.myTankType, 1);
					continue;
				}else {
					dm.setValueAt("" + player.count, player.myTankType, 1);
				}
				dm.setValueAt("" + player.score, player.myTankType, 2);
			}
		}
	}
}
