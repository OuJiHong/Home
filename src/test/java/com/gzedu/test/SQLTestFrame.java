package com.gzedu.test;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.gzedu.test.service.DaoService;

/**
 * sql测试
 * @author 欧集红 
 * @Date 2018年6月5日
 * @version 1.0
 * 
 */
public class SQLTestFrame {
	
	/**
	 * sql test
	 * @param args
	 */
	public static void main(String[] args) {
		//配置jdbc参数
		for(String arg : args) {
			String[] keyAndValue = arg.split("=");
			System.setProperty(keyAndValue[0], keyAndValue.length > 1 ? keyAndValue[1] : "");
		}
		
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
		final DaoService daoService = applicationContext.getBean(DaoService.class);
		
		final JFrame jframe = new JFrame("sql测试窗口");
		
		final JTextArea sqlArea = new JTextArea();
		final JButton queryButton = new JButton("执行查询");
		final JButton updateButton = new JButton("执行更新");
		final JPanel resultPanel = new JPanel(new BorderLayout());
		JTextArea msg = new JTextArea("输入sql进行查询...");
		msg.setEditable(false);
		resultPanel.add(msg);
		queryButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				queryButton.setEnabled(false);
				try {
					String sql = sqlArea.getText();
					List<Map> dataList = daoService.findListForMap(sql, null);
					Object[][] rowData = new Object[dataList.size()][];
					Object[] columnNames = new Object[0]; 
					if(dataList.size() > 0) {
						Map<String,Object> data = dataList.get(0);
						columnNames = new Object[data.size()];
						int index = 0;
						for(String key : data.keySet()) {
							columnNames[index] = key;
							index++;
						}
						
					}
					
					int row = 0;
					for(Map<String,Object> data : dataList) {
						rowData[row] = new Object[columnNames.length];
						int columnIndex = 0;
						for(Object key : columnNames) {
							rowData[row][columnIndex] = data.get(key);
							columnIndex++;
						}
						row++;
					}
				
					JTable resultTable = new JTable(rowData, columnNames);
					resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					resultPanel.removeAll();
					resultPanel.add(new JScrollPane(resultTable));
					resultPanel.revalidate();
				}catch(Exception ex){
					//有必要时，统一错误处理
					JTextArea errorArea = new JTextArea();
					errorArea.setEditable(false);
					ByteArrayOutputStream outData = new ByteArrayOutputStream();
					PrintStream printStream = new PrintStream(outData);
					ex.printStackTrace(printStream);
					errorArea.setText(new String(outData.toByteArray()));
					resultPanel.removeAll();
					resultPanel.add(new JScrollPane(errorArea));
					resultPanel.revalidate();
				}finally {
					queryButton.setEnabled(true);
				}
			}
		});
		
		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateButton.setEnabled(false);
				
				try {
					String sql = sqlArea.getText();
					int effectCount = daoService.updateData(sql, null);
					JTextArea resultArea = new JTextArea();
					resultArea.setEditable(false);
					resultArea.setText(effectCount + "条记录受影响!!!");
					resultPanel.removeAll();
					resultPanel.add(new JScrollPane(resultArea));
					resultPanel.revalidate();
				}catch(Exception ex) {
					//有必要时，统一错误处理
					JTextArea errorArea = new JTextArea();
					errorArea.setEditable(false);
					ByteArrayOutputStream outData = new ByteArrayOutputStream();
					PrintStream printStream = new PrintStream(outData);
					ex.printStackTrace(printStream);
					errorArea.setText(new String(outData.toByteArray()));
					resultPanel.removeAll();
					resultPanel.add(new JScrollPane(errorArea));
					resultPanel.revalidate();
				}finally {
					updateButton.setEnabled(true);
				}
			}
			
		});
		
		
		jframe.setLayout(new BorderLayout());
		
		
		JPanel contentPanel = new JPanel(new GridLayout(0,1, 2,2));
		contentPanel.add(new JScrollPane(sqlArea));
		contentPanel.add(resultPanel);
		jframe.add(contentPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(updateButton);
		buttonPanel.add(queryButton);
		jframe.add(buttonPanel,BorderLayout.SOUTH);
		
		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(580, 600);
		jframe.setVisible(true);
		
	}
	
	
	
}
