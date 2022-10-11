import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class TeatJtabel extends JFrame {
	
	//创建一个自己的渲染器，让他添加到整个表格，或者某个列。
	public DefaultTableCellRenderer myTableCellRenderer = new DefaultTableCellRenderer() {
		//将第六列转为Jbutton，其余列不变。
		//重写该方法是用于向表格添加组件，如果是图片imageIcon或是boolean，则需要重写Tablemodel的getColumnClass方法。
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if(column == 5) {
				JButton jButton = (JButton)value;//将数据转化为真是的类型返回。
				jButton.setSize(20, 20);
				return jButton;
			}else {
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
			
		}
	} ;
	
	//准备数据
	public Object[] title = new Object[] {"图片", "姓名", "年龄", "性别", "是否删除", "按钮"};
	public Object[][] data = new Object[][] {
		{new ImageIcon("D:\\javaproject\\TankWar\\src\\icon\\enemytank0.png"), "1", "2", "3", new Boolean(false), new JButton("删除")},
		{new ImageIcon("D:\\javaproject\\TankWar\\src\\icon\\enemytank1.png"), "4", "5", "6", new Boolean(false), new JButton("删除")},
		{new ImageIcon("D:\\javaproject\\TankWar\\src\\icon\\enemytank2.png"), "7", "8", "9", new Boolean(false), new JButton("删除")},
		{new ImageIcon("D:\\javaproject\\TankWar\\src\\icon\\player1.png"), "10", "11", "12", new Boolean(false), new JButton("删除")},
	};
	//创建数据模型，对表格进行增加列、增加行、获取数据、获取总行数和总列数、删除行、删除列、移动行、设置数据
	public DefaultTableModel defaultTableModel = new DefaultTableModel(data, title) {

		//可以指定每个列具体的对象类型，供渲染器使用，也可以在渲染器里直接指定。
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		}
		//指定哪些列是可以修改
		@Override
		public boolean isCellEditable(int row, int column) {
			if(column == 4) {
				return true;
			}else {
				return false;
			}
		}
		
		
		
	};
	
	//根据数据模型创建表格
	public JTable jTable = new JTable(defaultTableModel) {
		//设置jtable的单元格为透明的
//		@Override
//		public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
//			Component c = super.prepareRenderer(renderer, row, column);
//		    if (c instanceof JComponent) {
//		     ((JComponent) c).setOpaque(false);
//		    }
//		    return c;
//		}
		
	};
	//拿到表格的选择模型，可以处理用户对表格的选择和处理
	public ListSelectionModel listSelectionModel = jTable.getSelectionModel();
	//拿到表格的列模型
	public TableColumnModel tableColumnModel = jTable.getColumnModel();
	//根据列的数据模型，拿到表格的具体的某一列对其进行修改和自定义
	public TableColumn tableColumn = tableColumnModel.getColumn(4);
	//对行的操作直接再jtable的行修改方法里里进行
	
	public TeatJtabel() throws HeadlessException {
		super();
		getContentPane().setLayout(new BorderLayout());
		myTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		jTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer());
		//myTableCellRenderer.setOpaque(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jTable.setRowHeight(50);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//单行选择//多行选择//单数行选择
		jTable.setCellSelectionEnabled(true);//不可行选，只能选中单元格
		//jTable.setCellSelectionEnabled(false);//连单元格都不让选
		jTable.getTableHeader().setReorderingAllowed(false);//列不可拖动
		jTable.getTableHeader().setResizingAllowed(false);//列大小不可改变
		jTable.setOpaque(false);//和jtable中的重写的prepareRenderer方法配合使用
		jTable.getTableHeader().setOpaque(false);
		jTable.setShowGrid(false);//去除表格边框
		jTable.setBackground(Color.BLACK);//不透明的表格才可以用，透明的表格该行无效
//		jTable.setShowHorizontalLines(false);
//		jTable.setShowVerticalLines(false);
//System.out.println(jTable.rowAtPoint(new Point(150, 150)));
//System.out.println(jTable.columnAtPoint(new Point(150, 150)));
		//jTable.setEnabled(false);//整张表格不可修改、不可选择（上面的设置都作废了）
		//jTable.getColumnModel().getColumn(5).setCellRenderer(myTableCellRenderer);//给第五列添加“按钮”渲染器
		jTable.setDefaultRenderer(Object.class, myTableCellRenderer);
		//给表格的选择模型增加监听器，用于用户选择的监听。可以根据用户的选择处理数据
		listSelectionModel.addListSelectionListener(new ListSelectionListener() {
			
			@Override//当表格的选择内容发生变化时触发该事件
					//鼠标按下和抬起各触发一次
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()) {//鼠标按下时触发
	//				System.out.println(jTable.getSelectedColumn());
	//				System.out.println(jTable.getSelectedRow());
System.out.println(e.getFirstIndex());
System.out.println(e.getLastIndex());
				}
			}
		});
		
		//获取某一行数据
		jTable.setOpaque(false);//可以看到contentpane的颜色，但是单元格内还是黑的。
		JScrollPane jScrollPane = new JScrollPane(jTable);
		jScrollPane.setOpaque(false);
		jScrollPane.getViewport().setOpaque(false);
		jScrollPane.setBorder(null);
		this.getContentPane().setBackground(Color.GREEN);
		this.getContentPane().add(jScrollPane);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new TeatJtabel());
		
	}

}
