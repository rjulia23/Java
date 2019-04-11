import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.awt.event.KeyListener;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Canvas extends JPanel {
	//VARIABLES  
	protected static BufferedImage paintImage = new BufferedImage(1111, 1111, BufferedImage.TYPE_3BYTE_BGR);
	private Point lastPoint;	//point for drawing
	//////////////////////////////////////temporary things
	//private JButton save_btn = new JButton("save");
	protected Graphics g;
	private Color currentBackgroundColor;
	private Boolean draggable=false;
	protected static int iterator = -1;
	int dX;
	int dY;

	private Graphics2D g2d;

	Canvas(){
		MouseHandler handler = new MouseHandler();
		this.addMouseMotionListener(handler);
		this.addMouseListener(handler);
		setBackgroundColor(new Color(255,255,255));

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) {
		   // handle exception
		}
	}

	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(paintImage, 0, 0, null);
	}

	//keeps track of the mouse movements so we can draw lines and move things
	private class MouseHandler extends MouseAdapter
	{  
		public void mousePressed(MouseEvent e) {
			System.out.println(iterator);

			g = paintImage.createGraphics();
			if (Frame.currentMenu.equals("brush")){
				lastPoint = new Point(e.getX(), e.getY());
			}
			//			if (e.getX()<Frame.textBox.getLocation().getX()+Frame.brushSizeSlider.getValue()*5*Frame.textInput.getText().length()&& e.getX()>Frame.textBox.getLocation().getX())
			//				Frame.textBox.setLocation( new Point(e.getX(), e.getY()));
			if (Frame.currentMenu.equals("text")){
				//System.out.println(Frame.textList.size());
				if(Frame.textList.size()>0){
					check(Frame.textList,e);
				}
				if(draggable){
					if(Frame.textBrushButton.isSelected()){
						g.setColor(Frame.color);
						g.setFont(g.getFont().deriveFont((float)(Frame.textSizeSlider.getValue()*10)));
						g.drawString(Frame.textList.get(iterator).getText().toString(), e.getX(), e.getY());
						repaint();
					}
				}
			}
			if (Frame.currentMenu.equals("photo")){
				if(Frame.photoList.size()>0)
					check(Frame.photoList,e);
			}
		}

		public void mouseDragged(MouseEvent e) {
			g = paintImage.createGraphics();
			if (Frame.currentMenu.equals("photo")){
				//Frame.addPicture.setLocation( new Point(e.getX(), e.getY()));
				if(draggable){
					Frame.photoList.get(iterator).setLocation(e.getLocationOnScreen().x - dX, e.getLocationOnScreen().y - dY);
					dX = e.getLocationOnScreen().x - Frame.photoList.get(iterator).getX();
					dY = e.getLocationOnScreen().y - Frame.photoList.get(iterator).getY();
				}
			}
			if (Frame.currentMenu.equals("text")){
				if(draggable){
					Frame.textList.get(iterator).setLocation(e.getLocationOnScreen().x - dX, e.getLocationOnScreen().y - dY);
					dX = e.getLocationOnScreen().x - Frame.textList.get(iterator).getX();
					dY = e.getLocationOnScreen().y - Frame.textList.get(iterator).getY();
				}

				if(Frame.textBrushButton.isSelected()){
					g.setColor(Frame.color);
					g.setFont(g.getFont().deriveFont((float)(Frame.textSizeSlider.getValue()*10)));
					g.drawString(Frame.textList.get(iterator).getText().toString(), e.getX(), e.getY());
				}
			}
			// draw on image using Graphics
			if (Frame.currentMenu.equals("brush")){
				g2d = (Graphics2D)g;
				g2d.setColor(Frame.color);
				g2d.setStroke(new BasicStroke(Frame.brushSizeSlider.getValue()));
				g2d.drawLine(lastPoint.x, lastPoint.y, e.getX(), e.getY());
				g2d.dispose();
			}
			//if (getLocation().getX() <e.getX())
			g.dispose();
			// repaint panel with new modified paint
			repaint();
			lastPoint = new Point(e.getX(), e.getY());
		}

		public void mouseReleased(MouseEvent e) {
			if (Frame.currentMenu.equals("text")){
				if(draggable){
					Frame.textList.get(iterator).setPosition(Frame.textList.get(iterator).getLocation());
					//System.out.println(Frame.textList.get(iterator).getText());
					if(e.getX()>Frame.canvas.getWidth() || e.getX()<0 || e.getY()>Frame.canvas.getHeight() || e.getY()<0 ){
						deleteItem(Frame.textList);
					}
					}
			}	
			if (Frame.currentMenu.equals("photo")){
				if(draggable){
					Frame.photoList.get(iterator).setPosition(Frame.photoList.get(iterator).getLocation());
					//System.out.println(Frame.textList.get(iterator).getText());
					if(e.getX()>Frame.canvas.getWidth() || e.getX()<0 || e.getY()>Frame.canvas.getHeight() || e.getY()<0 ){
						deleteItem(Frame.photoList);
					}
				}
			}

		}
	}
	private class ButtonHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			try {
				save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//doesnt save jlabels
	public void save() throws IOException{
		ImageIO.write(paintImage, "PNG", new File("filename.png"));
	}

	public static void load(File file) throws IOException {
		paintImage = ImageIO.read(file);
		//repaint();
	}

	public void clear(){
		paintImage=null; //not sure if this deletes it or just points it somewhere else
		paintImage = new BufferedImage(1111, 1111, BufferedImage.TYPE_3BYTE_BGR);
		Frame.textList.clear();
		this.removeAll();
		iterator=Frame.textList.size()-1;
		setBackgroundColor(new Color(255,255,255));
		repaint();
	}
	public void setBackgroundColor(Color color){
		int width = paintImage.getWidth();
		int height = paintImage.getHeight();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				paintImage.setRGB(col, row, color.getRGB());
			}
		}
		currentBackgroundColor = color;
	}
	public void changeBackgroundColor(Color color){
		int width = paintImage.getWidth();
		int height = paintImage.getHeight();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if(paintImage.getRGB(col, row)==currentBackgroundColor.getRGB())
					paintImage.setRGB(col, row, color.getRGB());
			}
		}
		repaint();
		System.out.println(currentBackgroundColor);
		currentBackgroundColor = color;
	}
	public void check(LinkedList<TextBox> list, MouseEvent e){
		for (int i=0;i<list.size();i++){
			if(e.getX()<=list.get(i).getPosition().getX()+list.get(i).getWidth()&&
					e.getX()>=list.get(i).getPosition().getX()&&
					e.getY()>=list.get(i).getPosition().getY()&&
					e.getY()<=list.get(i).getPosition().getY()+list.get(i).getHeight()){

				dX = e.getLocationOnScreen().x - list.get(i).getX();
				dY = e.getLocationOnScreen().y - list.get(i).getY();
				draggable=true;
				iterator=i;
				if (e.isShiftDown()){
					deleteItem(list);
				}
				break;
			}
			draggable=false;
		}
	}

	public void deleteItem(LinkedList<TextBox> list){
		Frame.canvas.remove(list.get(iterator));
		list.remove(iterator);
		iterator=list.size()-1;
		draggable=false;
		repaint();
	}

}


