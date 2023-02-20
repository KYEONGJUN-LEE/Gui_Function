import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.*;
import javax.swing.event.*;

public class lab2_2022_lab2 extends JFrame implements ActionListener{
	// 네개의 버튼
	JButton startButton;
	JButton stopButton;
	JButton resumeButton;
	JButton exitButton;
	Timer t;					// 애니메이션을 위한 타이머
	final int SIZE = 50;		// 그림의 크기
	ArrayList<PosImageIcon> list = new ArrayList<PosImageIcon>();	// 그림객체가 들어가는 리스트	
	boolean isInside = true;	// 화면에서 마우스가 나가있는지의 여부를 나타냄
	JRadioButton small;
	JRadioButton medium;
	JRadioButton large;
	JPanel topPanel = new JPanel();
	JPanel sizePanel = new JPanel();
	JSlider slider;
	JPanel sliderPanel = new JPanel();
	


	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== small) {
			this.setSize(350,350);
		}
		if(e.getSource()== medium) {
			this.setSize(500, 500);
		}
		if(e.getSource()== large) {
			this.setSize(650,650);
		}
	}

	// 생성자에서 GUI구성등 초기화 진행
	public lab2_2022_lab2() {
		DrawPanel panel = new DrawPanel();			// 그림이 그려질 패널
		JPanel buttonsPanel = new JPanel();			// 버튼이 들어갈 패널
		// 버튼들의 준비 및 panel 배치
		startButton = new JButton("시작");
		stopButton = new JButton("중지");
		resumeButton = new JButton("계속");
		exitButton = new JButton("종료");
		buttonsPanel.add(startButton);
		buttonsPanel.add(stopButton);
		buttonsPanel.add(resumeButton);
		buttonsPanel.add(exitButton);
		

		//라디오 버튼
		small = new JRadioButton("소형");
		medium = new JRadioButton("일반");
		large = new JRadioButton("대형");

		ButtonGroup size = new ButtonGroup();
		size.add(small);
		size.add(medium);
		size.add(large);

		small.addActionListener(this);
		medium.addActionListener(this);
		large.addActionListener(this);

		topPanel.add(small);
		topPanel.add(medium);
		topPanel.add(large);
		

		sizePanel.add(topPanel);

		//슬라이더
		slider = new JSlider(30,70,50);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.addChangeListener(new Slider());
		sliderPanel.add(slider);
		//sPanel.setLayout(grid);

		sizePanel.setLayout(new GridLayout(2,0));
		sizePanel.add(sliderPanel);


		// Frame에 패널들 비치
		this.add(BorderLayout.CENTER, panel);
		this.getContentPane().add(BorderLayout.SOUTH, buttonsPanel);
		this.getContentPane().add(BorderLayout.NORTH, sizePanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,500);
		this.setVisible(true);

		// 움직임을 위한 타이머 생성, 핸들러 장착, 및 시작. 10밀리초마다 actionPerformed() 메소드가 실행됨
		t = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				repaint();					// 프레임이 포함하는 모든 요소들이 다시 그려지게 함.
			}								// 이에 의해 DrawPanel의 paintComponent 메소드가 실행됨

		});
		t.start();							// 타이머 실행

		// 버튼을 눌렀을 때, 작동하는 핸들러 장착 
		ButtonListener bl = new ButtonListener();
		startButton.addActionListener(bl);
		resumeButton.addActionListener(bl);
		stopButton.addActionListener(bl);
		exitButton.addActionListener(bl);

		// panel이 눌렸을 때, 작동하는 핸들러 장착
		panel.addMouseListener(new DrawPanel());
	}

	// 여기서부터 프로그램 실행
	public static void main(String[] args) {
		new lab2_2022_lab2();
	}

	// 버튼들과 연관된 핸들러
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == startButton)
				listInit();				// 그리기 다시 준비
			else if (source == exitButton)
				System.exit(0);			// 프로그램 종료	
			else if (source == stopButton)
				t.stop();				// 타이머 멈춤
			else if (source == resumeButton)
				t.restart();			// 타이머 다시시작
		}	
	}


	class Slider implements ChangeListener{
		public void stateChanged(ChangeEvent e){
			JSlider source = (JSlider)e.getSource();
			if(!source.getValueIsAdjusting()) {
				for(PosImageIcon pi : list) {
					int value = (int)source.getValue();
					new PosImageIcon((int)(Math.random()*300),  (int) (Math.random()*300), value, value);
					pi.width = value;
					pi.height = value;
				}
			}
		}
	}

	// ArrayList에 초기 5개의 Happyface 그림을 넣어주는 메소드
	// 기존에 있는 것은 모두 없애고 시작함
	private void listInit() {
		list.clear();  					// 기존의 리스트는 전부 없앰
		for (int i=0; i<5; i++) {
			int x = (int) (Math.random()*300);
			int y = (int) (Math.random()*300);		
			list.add(new PosImageIcon(x, y, SIZE, SIZE));
		}		
	}
	// 중앙위치 패널. 마우스리스너를 통해 마우스 액션에 반응하도록 함

	class DrawPanel extends JPanel implements MouseListener{
		public void paintComponent(Graphics g) {	// 이 메소드는 화면이 리프레쉬될때마다 실행됨
			int w = this.getWidth();				// 현재의 패널 넓이 획득
			int h= this.getHeight();				// 현재의 패널 높이 획득
			g.setColor(Color.white);				
			g.fillRect(0,0,w,h);					// 흰색으로 칠해 줌
			g.setColor(Color.black);
			g.drawLine(0, 0, w, 0);					// 구분선도 칠해 줌

			// 좌표를 조정하고 그림을 그려줌
			for (PosImageIcon pi : list) {
				pi.move(pi.moveX, pi.moveY);
				if (pi.pX <= 0 || pi.pX >= w-SIZE)
					pi.moveX = pi.moveX * -1;
				if (pi.pY <= 0 || pi.pY >= h-SIZE)
					pi.moveY = pi.moveY * -1;
				
				
				
				// 마우스가 패널의 안에 있는지 아닌지에 따라 그림는 방법을 달리함
				if (isInside)
					pi.drawBlue(g);		// 화면 안에 있으면 그림그림
				else
					pi.drawRed(g); 	// 화면 밖이면 붉은색 그림그림
			}		
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mousePressed(MouseEvent e) {
			Point p = e.getPoint();			// 클릭된 위치의 좌표를 구해옴
			for (PosImageIcon icon : list) {
				if (icon.distance(p) <= SIZE/2) {
					list.remove(icon);		// 루프 안에서 list의 요소를 제거하면 문제 생김
					break;					// 따라서 loop으로 다시 돌아가기전 바로 빠져나옴
				}
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			isInside = true;	// 화면에서 마우스가 들어와 있음
		}
		@Override
		public void mouseExited(MouseEvent e) {
			isInside = false;	// 화면에서 마우스가 나가 있음
		}
	}



}
class PosImageIcon extends ImageIcon{
	int pX;				// 그림의 X좌표
	int pY;				// 그림의 y좌표
	int width;			// 그림의 넓이
	int height;			// 그림의 높이
	int moveX=1, moveY=1; // 그림의 이동방향 및 보폭

	// 생성자
	public PosImageIcon(int x, int y, int width, int height) {
		pX=x;
		pY=y;
		this.width = width;
		this.height = height;
	}
	

	// 그림의 이동
	public void move(int x, int y) {
		pX += x;
		pY += y;
	}
	
	// 그림대신 붉은색 원 그리기
	public void drawRed(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval(pX, pY, width, height);
	}
	// 그림대신 붉은색 원 그리기
	public void drawBlue(Graphics g) {
		g.setColor(Color.blue);
		g.fillOval(pX, pY, width, height);
	}
	// 어떤 주어진 좌표 p 와 그림의 중점과의 거리 (그림의 좌표는 왼쪽 모서리이므로... 폭과 높이를 감안하여 중점을 사용)
	public double distance (Point p) {
		return Math.sqrt(Math.pow((pX+width/2)-p.x, 2)+ Math.pow((pY+height/2)-p.y, 2));
	}
}
