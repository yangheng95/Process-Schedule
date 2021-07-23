package com.yangheng.OS.Main;

import java.awt.EventQueue;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import com.yangheng.OS.Log.FileUtil;
import com.yangheng.OS.PCBUtil.GenRamPCB;
import com.yangheng.OS.PCBUtil.PRCView;

import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * @author chuan
 *
 */
public class OSmain {

	public JFrame frame;
	OSmain osmain;

	boolean isOver = false;
	boolean onPause = false;

	public static int MaxPID = 1;


	public static JList<String> list1 = new JList<>();
	public static JList<String> list2 = new JList<>();
	public static JList<String> list3 = new JList<>();
	public static JList<String> list4 = new JList<>();
	public static JList<String> list5 = new JList<>();

	public static JRadioButton radiobtn = null;

	public JTextField textField;
	JMenuItem menuItem_2;

	JLabel CurrentProcess = new JLabel();
	JLabel haveRuned = new JLabel();
	JLabel nextInstruction = new JLabel();
	JLabel InstructionQue = new JLabel();
	JLabel nextProcess = new JLabel();

	public JScrollPane scrollPane1;
	public JScrollPane scrollPane2;
	public JScrollPane scrollPane3;
	public JScrollPane scrollPane4;
	public JScrollPane scrollPane5;

	JTextField textField_1 = new JTextField();

	public static JTextArea textArea;
	Thread thread;

	public static void Log(String s) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[YYYY/MM/dd HH:mm:ss.SSS]");
		long time = date.getTime();
		String time1 = simpleDateFormat.format(time);
		String log = time1 + s + " \r\n";
		OSmain.textArea.append(log.replaceAll("\n", "\r\n"));

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OSmain window = new OSmain();
					UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName());
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					window.frame.setLocation((dim.width - window.frame.getWidth()) / 2,
							(dim.height - window.frame.getHeight()) / 2);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OSmain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\u6A21\u62DF\u8FDB\u7A0B\u8C03\u5EA6");
		frame.setBounds(-8, -18, 1000, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(34, 426, 145, 228);
		frame.getContentPane().add(scrollPane1);

		scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(224, 426, 145, 228);
		frame.getContentPane().add(scrollPane2);

		scrollPane3 = new JScrollPane();
		scrollPane3.setBounds(411, 426, 145, 228);
		frame.getContentPane().add(scrollPane3);

		scrollPane4 = new JScrollPane();
		scrollPane4.setBounds(605, 426, 145, 228);
		frame.getContentPane().add(scrollPane4);

		scrollPane5 = new JScrollPane();
		scrollPane5.setBounds(796, 426, 145, 228);
		frame.getContentPane().add(scrollPane5);

		JSeparator separator = new JSeparator();
		separator.setBounds(22, 366, 943, 1);
		frame.getContentPane().add(separator);

		JLabel label = new JLabel("\u5C31\u7EEA\u961F\u5217");
		label.setBounds(80, 394, 54, 15);
		frame.getContentPane().add(label);

		JLabel label_1 = new JLabel("\u540E\u5907\u961F\u5217");
		label_1.setBounds(268, 394, 54, 15);
		frame.getContentPane().add(label_1);

		JLabel label_2 = new JLabel("\u8F93\u5165\u7B49\u5F85\u961F\u5217");
		label_2.setBounds(446, 394, 81, 15);
		frame.getContentPane().add(label_2);

		JLabel label_3 = new JLabel("\u8F93\u51FA\u7B49\u5F85\u961F\u5217");
		label_3.setBounds(640, 394, 81, 15);
		frame.getContentPane().add(label_3);

		JLabel textWait = new JLabel("\u5176\u4ED6\u7B49\u5F85\u961F\u5217");
		textWait.setBounds(827, 394, 81, 15);
		frame.getContentPane().add(textWait);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 54, 21);
		frame.getContentPane().add(menuBar);

		JMenu menu = new JMenu("\u9009\u9879");
		menuBar.add(menu);
		osmain = this;
		FileUtil fileUtil = new FileUtil(this);
		JMenuItem menuItem = new JMenuItem("\u6253\u5F00\u8FDB\u7A0B\u63CF\u8FF0\u6587\u4EF6");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("打开PRC文件");
				fileChooser.setApproveButtonText("打开");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(frame);
				if (JFileChooser.APPROVE_OPTION == result) {
					fileUtil.ReadPRC(fileChooser.getSelectedFile().getPath(), osmain);
				}
			}
		});
		menu.add(menuItem);

		JMenuItem menuItem_1 = new JMenuItem("\u968F\u673A\u751F\u6210\u8FDB\u7A0B\u63CF\u8FF0\u6587\u4EF6");
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GenRamPCB.generatePRCQue(20, osmain);
				PRCView frame = new PRCView();
				frame.setVisible(true);
				frame.getContentPane().validate();
			}
		});
		menu.add(menuItem_1);

		JMenuItem mntmNewMenuItem = new JMenuItem("\u6253\u5F00\u65E5\u5FD7\u4FDD\u5B58\u76EE\u5F55");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String cmd = "explorer.exe C:\\Users\\chuan\\Desktop\\Logs";

				try {
					Runtime.getRuntime().exec(cmd);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		menu.add(mntmNewMenuItem);

		JLabel lblcpu = new JLabel("\u5F53\u524D\u4F7F\u7528CPU\u7684\u8FDB\u7A0B");
		lblcpu.setFont(new Font("宋体", Font.PLAIN, 16));
		lblcpu.setBounds(22, 41, 186, 15);
		frame.getContentPane().add(lblcpu);

		JLabel lblcpu_2 = new JLabel("\u5269\u4F59\u6240\u6709\u6307\u4EE4\u65F6\u95F4");
		lblcpu_2.setFont(new Font("宋体", Font.PLAIN, 16));
		lblcpu_2.setBounds(22, 66, 186, 15);
		frame.getContentPane().add(lblcpu_2);

		JLabel lblcpu_1 = new JLabel("\u5F53\u524D\u6307\u4EE4\u540E\u7EE7\u6307\u4EE4");
		lblcpu_1.setFont(new Font("宋体", Font.PLAIN, 16));
		lblcpu_1.setBounds(22, 91, 186, 15);
		frame.getContentPane().add(lblcpu_1);

		JLabel label_8 = new JLabel("\u8FDB\u7A0B\u5269\u4F59\u6307\u4EE4\u961F\u5217");
		label_8.setFont(new Font("宋体", Font.PLAIN, 16));
		label_8.setBounds(22, 116, 186, 15);
		frame.getContentPane().add(label_8);

		JLabel lblcpu_3 = new JLabel("\u4E0B\u4E00\u5373\u5C06\u4F7F\u7528CPU\u7684\u8FDB\u7A0B");
		lblcpu_3.setFont(new Font("宋体", Font.PLAIN, 16));
		lblcpu_3.setBounds(22, 141, 186, 15);
		frame.getContentPane().add(lblcpu_3);

		JLabel label_10 = new JLabel("\u5F53\u524D\u7CFB\u7EDF\u65F6\u95F4\u7247\u8BBE\u7F6E");
		label_10.setBounds(22, 191, 138, 15);
		frame.getContentPane().add(label_10);
		CurrentProcess.setFont(new Font("Bodoni MT Black", Font.PLAIN, 14));

		CurrentProcess.setBounds(218, 41, 229, 15);
		frame.getContentPane().add(CurrentProcess);
		haveRuned.setFont(new Font("Bodoni MT Black", Font.PLAIN, 14));

		haveRuned.setBounds(218, 66, 229, 15);
		frame.getContentPane().add(haveRuned);
		nextInstruction.setFont(new Font("Bodoni MT Black", Font.PLAIN, 14));

		nextInstruction.setBounds(218, 91, 229, 15);
		frame.getContentPane().add(nextInstruction);
		InstructionQue.setFont(new Font("Bodoni MT Black", Font.PLAIN, 14));

		InstructionQue.setBounds(218, 116, 229, 15);
		frame.getContentPane().add(InstructionQue);
		nextProcess.setFont(new Font("Bodoni MT Black", Font.PLAIN, 14));

		nextProcess.setBounds(218, 141, 229, 15);
		frame.getContentPane().add(nextProcess);

		textField = new JTextField();
		textField.setText("1000");
		textField.setBounds(168, 188, 98, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("\u6BEB\u79D2(ms)");
		lblNewLabel_1.setBounds(276, 191, 68, 15);
		frame.getContentPane().add(lblNewLabel_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(508, 42, 432, 301);
		frame.getContentPane().add(scrollPane);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);

		JLabel label_4 = new JLabel("\u4E8B\u4EF6\u8BB0\u5F55");
		label_4.setFont(new Font("楷体", Font.PLAIN, 18));
		label_4.setBounds(508, 8, 104, 27);
		frame.getContentPane().add(label_4);

		menuItem_2 = new JMenuItem("\u5F00\u59CB\u8C03\u5EA6");
		menuItem_2.setSelectedIcon(new ImageIcon(OSmain.class.getResource("/drawable/start.png")));
		menuItem_2.setIcon(new ImageIcon(OSmain.class.getResource("/drawable/start.png")));
		menuItem_2.setFont(new Font("华文琥珀", Font.PLAIN, 16));
		menuItem_2.setHorizontalAlignment(SwingConstants.LEFT);
		menuItem_2.setForeground(Color.GREEN);
		menuItem_2.setBounds(241, 219, 127, 33);
		frame.getContentPane().add(menuItem_2);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(22, 173, 453, 1);
		frame.getContentPane().add(separator_2);

		radiobtn = new JRadioButton(
				"\u8C03\u5EA6\u8FC7\u7A0B\u4E2D\u6A21\u62DF\u65B0\u8FDB\u7A0B\u52A0\u5165\u8C03\u5EA6");
		radiobtn.setBounds(22, 226, 213, 23);
		frame.getContentPane().add(radiobtn);

		JButton button = new JButton("\u6682\u505C/\u6062\u590D");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onPause = !onPause;
				if(onPause){
					button.setText("恢复调度");
				}
				else{
					button.setText("暂停调度");
				}
				if (onPause) {
					textField.setEditable(true);
				} else
					textField.setEditable(false);
			}
		});
		button.setBounds(371, 224, 104, 23);
		frame.getContentPane().add(button);
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				osmain.isOver = false;
				Thread thread = new ProcessSchedule(Integer.parseInt(textField.getText()), osmain);
				thread.start();
				menuItem_2.setEnabled(false);
			}
		});

		DefaultCaret caret = (DefaultCaret) textArea.getCaret(); // 设置日志框实时更新
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
}
