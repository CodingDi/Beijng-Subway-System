package dijkstra;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Gui implements ActionListener {

	JFrame frame=new JFrame();
	JPanel type=new JPanel();
	JPanel picture=new JPanel();
	JPanel result=new JPanel();
	JTextField start=new JTextField(12);
	JTextField end=new JTextField(12);
	JLabel startWord;
	JLabel endWord;
	JLabel resultString=new JLabel(" ");
	JButton submit=new JButton("search");

	public void buildGUI(){

		
		
		submit.addActionListener(this);

		type.setBackground(new Color(135,206,235));//make the background color to be sky blue

		startWord=new JLabel("Starting Point :");
		endWord=new JLabel("End point :");
		
		Font bigFont=new Font("serif",Font.BOLD,20);
		startWord.setFont(bigFont);
		endWord.setFont(bigFont);
		submit.setFont(bigFont);
		start.setFont(bigFont);
		end.setFont(bigFont);
		
		type.setLayout(new BoxLayout(type,BoxLayout.Y_AXIS));
		
		type.add(startWord);
		type.add(start);
		type.add(endWord);
		type.add(end);
		type.add(submit);
		
		frame.getContentPane().add(BorderLayout.EAST,type);
		/*
		 * 插入北京地铁图片
		 * insert the Beijing subway map
		 */
		Image iamge = new ImageIcon(JImagePane.class.getResource("subway_map.png")).getImage();
        JImagePane imagePane = new JImagePane(iamge, JImagePane.SCALED);
 
        
		result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
		result.add(imagePane);
		result.add(resultString);
		frame.getContentPane().add(BorderLayout.CENTER,result); 
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,500);
		frame.setVisible(true);
		
		
	}
	
	public void actionPerformed(ActionEvent event){
		String startStationName=start.getText();//obtain the starting point 获得起始点名字
		String endStationName=end.getText();//obtain the end point 获得终点名字
		Dijkstra dij=new Dijkstra();
		try {
			int landInt[][]=dij.getWayID(startStationName);
			String way=dij.getWay(landInt, endStationName);
			String exchangeLineName=dij.getExchangeLineName(landInt, endStationName);
			String exchangeStationName=dij.getExchangeStationName(landInt, endStationName);
			int exchangeLineNumber=dij.getExchangeLineNumber(landInt, endStationName);
			int exchangeStationNumber=dij.getExchangeStationNumber(startStationName, endStationName, exchangeLineNumber);
			
			String jieguo="<html>Station Route:"+way+"<br>Line Route:"+
					exchangeLineName+"<br>The station you need to change line :"
							+ exchangeStationName+"<br>Exchange line number :"+
					exchangeLineNumber+" "+"Total station number :"+exchangeStationNumber+"<html>";
			
			resultString.setText(jieguo);
			System.out.println(way);
			System.out.println(exchangeLineName);
			System.out.println(exchangeStationName);
			System.out.println(exchangeLineNumber);
			System.out.println(exchangeStationNumber);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
