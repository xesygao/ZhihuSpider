package ZhihuSpider;

import javax.swing.*;
import java.awt.Desktop;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class WindowUI extends JFrame implements ActionListener,Callback{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String url;
	DefaultListModel<String> list=new DefaultListModel<String>();
	JPanel button;
	JLabel lurl,lthreadnum;
	JTextArea info;
	JTextField turl;
	JButton submit,add,delete;
	JList<String> urllist;
	JComboBox<String> threadnum;
	int numofurl=0,total=0,count=0,ithreadnum=0;
	boolean finished=true;
	static WindowUI ui;
	
	WindowUI(){
		super("知乎图片抓取器");
		init();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,300);
		setVisible(true);
		button.setVisible(true);
		
	}
	void init(){
		String[] num={"1","2","3","4","5"};
		lurl=new JLabel("帖子url");
		lthreadnum=new JLabel("线程数");
		button=new JPanel();
		threadnum=new JComboBox<String>(num);
		threadnum.setSelectedIndex(2);
		urllist=new JList<String>(list);
		info=new JTextArea();
		info.setEditable(false);
		info.setLineWrap(true);
		info.setBackground(Color.WHITE);
		JScrollPane jsp=new JScrollPane(info);
		jsp.setVerticalScrollBarPolicy( 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		jsp.setVisible(true);
		turl=new JTextField();
		submit=new JButton("开始抓取");
		submit.setActionCommand("submit");
		submit.addActionListener(this);
		
		add=new JButton("加入队列");
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if((url=turl.getText())!=""){
					if(!list.contains(url))
					list.addElement(url);
					turl.setText("");;
					urllist.setModel(list);
				}
			}
		});
		
		delete=new JButton("移除选中");
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!urllist.isSelectionEmpty()){
					list.remove(urllist.getSelectedIndex());
					urllist.setModel(list);
				}
			}
		});
		
		
		GridBagLayout layout=new GridBagLayout();
		GridBagConstraints cons=new GridBagConstraints();
		setLayout(layout);
		button.setLayout(new FlowLayout(FlowLayout.CENTER,10,5));
		button.add(lthreadnum);
		button.add(threadnum);
		button.add(submit);
		
		cons.fill=GridBagConstraints.BOTH;
		cons.gridwidth=1;
		cons.weightx=0;
		cons.weighty=0;
		add(lurl,cons);
		cons.gridwidth=1;
		cons.weightx=1;
		cons.weighty=0;
		add(turl,cons);
		cons.gridwidth=1;
		cons.weightx=0;
		cons.weighty=0;
		add(add,cons);
		cons.gridwidth=0;
		cons.weightx=0;
		cons.weighty=0;
		add(delete,cons);
		
		cons.gridwidth=0;
		cons.weightx=0;
		cons.weighty=0;
		add(urllist,cons);
		
		cons.gridwidth=0;
		cons.weightx=1;
		cons.weighty=1;
		add(jsp,cons);
		
		cons.gridwidth=0;
		cons.weightx=0;
		cons.weighty=0;
		add(button,cons);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("submit")){
			info.setText("");
			numofurl=list.getSize();
			ithreadnum=threadnum.getSelectedIndex()+1;
			Mythread my=new Mythread(ui,numofurl,ithreadnum);
			Thread thread=new Thread(my);
			thread.start();
		}
	}
	
	public void allFinished(){
		total++;
		if(numofurl==total){
			total=0;
			Object[] options={"确定","查看文件"};
			int n=JOptionPane.showOptionDialog(
					getContentPane(), "抓取完成","*^_^*", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,options[0]);
			if(n==1){
				try{
					Desktop.getDesktop().open(new File("Picture"));
				}catch(Exception e1){
					e1.printStackTrace();  
				}
			}
		}
	}
	
	public void threadfinished(){}
	
	public static void main(String[] args){
		WindowUI myUI=new WindowUI();
		ui=myUI;
	}

}
