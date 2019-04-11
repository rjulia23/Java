import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextBox extends JLabel{

	Point position=new Point(0,0);
	Dimension dimentions = new Dimension(0,0);
	//JLabel text = new JLabel();
	//JTextField object = new JTextField();
	//Point lastPoint=null;
	TextBox(String text){
		setLocation(position);
		setText(text);
		setFont(getFont().deriveFont((float)40));
		
		//this.text.setText(text);
		//this.text.setFont(getFont().deriveFont((float)40));
		//this.addMouseMotionListener(new TextHandler());
		//this.addMouseListener(new TextHandler());
	}

		public boolean contains(Point point){
			for(int x=(int) position.getX();x<position.getX()+getWidth();x++){
				for(int y=(int) position.getY();x<position.getY()+getHeight();y++){
					if (point.getX()==x && point.getY()==y)
						return true;
				}
			}
			return false;	
		}
	

	private class TextHandler extends MouseAdapter
	{  
		@Override
		public void mousePressed(MouseEvent e) {
			//if (getLocation().getX() <e.getX())
			//Point lastPoint = new Point(e.getX(), e.getY());
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			//Graphics g = paintImage.createGraphics();
			if (Frame.currentMenu.equals("text")){
				//if (lastPoint!=null)
				setLocation(new Point(e.getX(), e.getY()));
			}

		}

		//		public void mouseReleased(MouseEvent e) {
		//			lastPoint = new Point(e.getX(), e.getY());
		//		}
	}
	public void setPosition(Point point){
		position=point;
	}
	public Point getPosition(){
		return position;
	}
	
	
}
