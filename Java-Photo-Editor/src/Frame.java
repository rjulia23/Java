import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;



public class Frame extends JFrame{
	private JPanel window = new JPanel();
	private JPanel sideButtons = new JPanel();
	private JPanel brushMenu = new JPanel();
	private JPanel textMenu = new JPanel();
	private JPanel tempMenu = new JPanel();
	private JPanel photoMenu = new JPanel();
	private JButton brushButton = new JButton("b r u s h");
	private JButton textButton = new JButton("t e x t");
	private JButton photoButton = new JButton("p h o t o");
	private JButton saveButton = new JButton("s a v e");
	private JButton loadButton = new JButton("l o a d");
	private JButton clearButton = new JButton("c l e a r");
	protected static Canvas canvas = new Canvas();
	protected static String currentMenu="temp";
	protected static JSlider brushSizeSlider = new JSlider(1,20);
	protected static JSlider textSizeSlider = new JSlider(1,10);
	protected Graphics2D g2d;
	protected static Color color;
	private JColorChooser colorChooser = new JColorChooser();	
	protected static TextBox addThisText = new TextBox("");
	//protected static JPanel textBox = new JPanel();
	protected static JTextField textInput = new JTextField(17);
	public Point lastPoint;
	private JToggleButton boldButton;
	private JToggleButton italicButton;
	private JToggleButton underlineButton;
	protected static JToggleButton textBrushButton = new JToggleButton("TextBrush");
	protected static JToggleButton  backgroundColorButton = new JToggleButton("BackgroundColor");
	protected static JLabel addPicture = new JLabel();
	protected static LinkedList<TextBox> textList = new LinkedList();
	protected static LinkedList<TextBox> photoList = new LinkedList();
	protected File imageFile;
	private FileChooser fileChooser = new FileChooser();


	Frame(){
		createComponents();
		fileChooser.setCurrentDirectory(new File("C:\\Users\\julia\\Pictures\\"));
	}

	public void createComponents(){
		createBrushMenu();
		createTextMenu();
		createPhotoMenu();
		//main menu listeners 
		canvas.setLayout(null);//sets a null layout so that components dont move


		class BrushButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				currentMenu="brush";
				tempMenu.removeAll();
				tempMenu.add(brushMenu);
				validate();
				setVisible(true);
			}
		}
		class TextButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				currentMenu="text";
				tempMenu.removeAll();
				tempMenu.add(textMenu);
				validate();
				setVisible(true);
			}
		}
		class PhotoButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				currentMenu="photo";
				tempMenu.removeAll();
				tempMenu.add(photoMenu);
				validate();
				setVisible(true);
			}
		}
		class SaveButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				BufferedImage img=null;
				int imgW = 0, imgH = 0;
				if(imageFile!=null && imgW<=canvas.getWidth() && imgH<=canvas.getHeight()){
					try {
						imgW=ImageIO.read(imageFile).getWidth();
						imgH=ImageIO.read(imageFile).getHeight();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					img = new BufferedImage(imgW,imgH, BufferedImage.TYPE_INT_RGB);
				}
				else
					img = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);

				canvas.print(img.getGraphics()); 

				FileNameExtensionFilter pngFilter = new FileNameExtensionFilter(
						"PNG file (*.png)", "png");
				fileChooser.addChoosableFileFilter(pngFilter);
				fileChooser.setFileFilter(pngFilter);

				int status = fileChooser.showSaveDialog(canvas);

				if (status == JFileChooser.APPROVE_OPTION) {
					
					try {
						ImageIO.write(img, "png", new File(fileChooser.getSelectedFile() + ".png"));
						//JOptionPane.showMessageDialog(null, "Image saved to " + fileChooser.getSelectedFile().getName());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}


				//				try {
			//	ImageIO.write();
				//				}
				//				catch (IOException e1) {
				//					// TODO Auto-generated catch block
				//					e1.printStackTrace();
				//				}
			}
		}
		class ClearButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				addThisText.setText(null);
				canvas.clear();				
			}
		}
		class LoadButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {

				fileChooser.setFileFilter(null);

				int returnVal = fileChooser.showOpenDialog(canvas);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					imageFile = fileChooser.getSelectedFile();
					try {
						canvas.paintImage = ImageIO.read(imageFile);
						//canvas.setWidth(canvas.paintImage.getWidth());
						repaint();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		window.setLayout(new BorderLayout());
		sideButtons.setLayout(new GridLayout(6,1));

		sideButtons.add(brushButton);
		sideButtons.add(textButton);
		sideButtons.add(photoButton);
		sideButtons.add(clearButton);
		sideButtons.add(loadButton);
		sideButtons.add(saveButton);


		ActionListener brushButtonListener = new BrushButtonListener();
		brushButton.addActionListener(brushButtonListener);
		ActionListener textButtonListener = new TextButtonListener();
		textButton.addActionListener(textButtonListener);
		ActionListener photoButtonListener = new PhotoButtonListener();
		photoButton.addActionListener(photoButtonListener);
		ActionListener saveButtonListener = new SaveButtonListener();
		saveButton.addActionListener(saveButtonListener);
		ActionListener clearButtonListener = new ClearButtonListener();
		clearButton.addActionListener(clearButtonListener);
		ActionListener loadButtonListener = new LoadButtonListener();
		loadButton.addActionListener(loadButtonListener);


		window.add(sideButtons, BorderLayout.WEST);
		window.add(tempMenu, BorderLayout.SOUTH);
		window.add(canvas, BorderLayout.CENTER);
		add(window);


	}

	public void createBrushMenu(){

		class BrushSliderListener implements ChangeListener{
			public void stateChanged(ChangeEvent event){
				if (g2d!=null)
					g2d.setStroke(new BasicStroke(brushSizeSlider.getValue()));
			}
		}
		class ColorListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				color=colorChooser.getColor();
			}
		}
		class BackgroundListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if (backgroundColorButton.isSelected())
					canvas.changeBackgroundColor(color);
			}
		}
		//JLabel title = new JLabel("Brush");
		JPanel brushSizePanel = new JPanel();
		JPanel colorPanel = new JPanel();
		JPanel colorButtons = bulidColorPanel();
		//colorButtons

		JLabel brushSizeLabel= new JLabel("brush size");
		JLabel colorLabel= new JLabel("color");

		brushSizePanel.setLayout(new GridLayout(1,2));
		colorPanel.setLayout(new GridLayout(1,3));
		brushMenu.setLayout(new GridLayout(3,1));

		brushSizeSlider.addChangeListener(new BrushSliderListener());
		brushSizePanel.add(brushSizeLabel);
		brushSizePanel.add(brushSizeSlider);
		colorPanel.add(colorLabel);
		colorPanel.add(colorButtons);
		colorPanel.add(backgroundColorButton);
		brushMenu.add(brushSizePanel);
		brushMenu.add(colorPanel);

	}

	public void createTextMenu(){
		class BoldItalicListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(Canvas.iterator!=-1){
					if	(boldButton.isSelected()&&italicButton.isSelected())
						textList.get(canvas.iterator).setFont(textList.get(canvas.iterator).getFont().deriveFont(Font.ITALIC + Font.BOLD));
					else if(boldButton.isSelected())
						textList.get(canvas.iterator).setFont(textList.get(canvas.iterator).getFont().deriveFont(Font.BOLD));
					else if (italicButton.isSelected())
						textList.get(canvas.iterator).setFont(textList.get(canvas.iterator).getFont().deriveFont(+ Font.ITALIC));
					else
						textList.get(canvas.iterator).setFont(textList.get(canvas.iterator).getFont().deriveFont(Font.PLAIN));
				}
			}
		}
		class UnderlineListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(Canvas.iterator!=-1){
					if (underlineButton.isSelected()){
						Font font = textList.get(canvas.iterator).getFont();
						Map attributes = font.getAttributes();
						attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
						textList.get(canvas.iterator).setFont(font.deriveFont(attributes));
					}
					else{
						//					addThisText.getFont().getAttributes().put(TextAttribute.UNDERLINE, -1);
						//					addThisText.setFont(addThisText.getFont().deriveFont(addThisText.getFont().getAttributes()));
						Font font = textList.get(canvas.iterator).getFont();
						Map attributes = font.getAttributes();
						attributes.put(TextAttribute.UNDERLINE, -1);
						textList.get(canvas.iterator).setFont(font.deriveFont(attributes));
					}
				}
			}
		}
		class FontSliderListener implements ChangeListener{
			public void stateChanged(ChangeEvent event){
				if(Canvas.iterator!=-1){
					textList.get(canvas.iterator).setFont(textList.get(canvas.iterator).getFont().deriveFont((float)(textSizeSlider.getValue()*10)));
					textList.get(canvas.iterator).setBounds(0, 0, textSizeSlider.getValue()*10*textInput.getText().length(), textSizeSlider.getValue()*15);
					textList.get(canvas.iterator).setLocation(textList.get(canvas.iterator).getPosition());
				}
			}
		}
		class NewTextButtonListener  implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				//	addThisText.setText(textInput.getText());
				//	addThisText.setFont(addThisText.getFont().deriveFont((float)40));
				textList.add(new TextBox(textInput.getText()));
				Canvas.iterator=textList.size()-1;
				//textBox.add(addThisText);
				//canvas.add(addThisText);

				canvas.add(textList.getLast());
				textList.getLast().setBounds(0, 0, textSizeSlider.getValue()*10*textInput.getText().length(), textSizeSlider.getValue()*10);
				textList.getLast().setLocation(textList.getLast().getPosition());

				validate();
				setVisible(true);
			}
		}


		//JLabel title = new JLabel("Text");
		JPanel fontPanel = new JPanel();
		JPanel textSizePanel = new JPanel();
		JPanel colorPanel = new JPanel();
		JLabel fontLabel= new JLabel("font");
		JLabel colorLabel= new JLabel("font color");
		JLabel textSizeLabel= new JLabel("text size");
		JPanel buttonPanel = new JPanel();
		boldButton = new JToggleButton("B");
		italicButton = new JToggleButton("I");
		underlineButton = new JToggleButton("U");
		JPanel colorButtons = bulidColorPanel();
		JButton newTextButton = new JButton("+");
		JLabel addTextLabel = new JLabel("add text");
		JPanel addTextPanel = new JPanel();


		//add dropdown menu

		fontPanel.setLayout(new GridLayout(2,1));
		textSizePanel.setLayout(new GridLayout(3,1));
		addTextPanel.setLayout(new GridLayout(3,1));
		buttonPanel.add(boldButton);
		buttonPanel.add(italicButton);
		buttonPanel.add(underlineButton);

		boldButton.addActionListener(new BoldItalicListener());
		italicButton.addActionListener(new BoldItalicListener());
		underlineButton.addActionListener(new UnderlineListener());
		textSizeSlider.addChangeListener(new FontSliderListener());
		newTextButton.addActionListener(new NewTextButtonListener());

		//fontPanel.add(fontLabel);
		//fontPanel.add(fontDropdown);
		fontPanel.add(textBrushButton);
		fontPanel.add(buttonPanel);

		colorPanel.add(colorLabel);
		colorPanel.add(colorButtons);

		textSizePanel.add(textSizeLabel);
		textSizePanel.add(textSizeSlider);
		textSizePanel.add(colorPanel);

		addTextPanel.add(addTextLabel);
		addTextPanel.add(textInput);
		addTextPanel.add(newTextButton);

		//textMenu.add(title);
		textMenu.add(fontPanel);
		textMenu.add(textSizePanel);
		textMenu.add(addTextPanel);

	}
	public void createPhotoMenu(){
		class AddListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				//opens photo
				Image img;
				if(photoList.size()<5){
					try {
						//						Runtime.getRuntime().exec("explorer.exe /select, path");
						//In response to a button click:
						FileChooser fileChooser = new FileChooser();
						fileChooser.setCurrentDirectory(new File("C:\\Users\\julia\\Pictures\\"));

						int returnVal = fileChooser.showOpenDialog(canvas);
						//Desktop.getDesktop().open(new File("Y:\\curently using JULIA\\java"));
						//File file = fileChooser.getSelectedFile();
						//						Desktop desktop = Desktop.getDesktop();
						//						desktop.open(file);
						//						
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = fileChooser.getSelectedFile();
							img = ImageIO.read(file);
							int h=(int)(200*img.getHeight(null))/img.getWidth(null);
							photoList.add(new TextBox(""));
							//System.out.println(w+","+h);
							photoList.getLast().setIcon(new ImageIcon(img.getScaledInstance(200, h, Image.SCALE_DEFAULT)));
							canvas.add(photoList.getLast());
							photoList.getLast().setBounds(0, 0, 200,h);
						}
						//addPicture.setLocation(addThisText.getPosition());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					validate();
					setVisible(true);
				}
			}
		}
		JLabel title = new JLabel("Photos");
		//import a photo
		JButton addPhoto=new JButton("+");

		ActionListener addPhotoListener = new AddListener();
		addPhoto.addActionListener(addPhotoListener);

		photoMenu.add(title);
		photoMenu.add(addPhoto);


	}


	public static class ColorButton extends JButton{
		ColorButton(Color color){
			setBackground(color);
		}
	}

	public class ColorListener implements ActionListener{
		private Color buttonColor;
		public ColorListener (Color color){
			this.buttonColor = color;
		}
		public void actionPerformed(ActionEvent e) {
			color = buttonColor;
			if (currentMenu.equals("text")){
				if(Canvas.iterator!=-1){
					textList.get(canvas.iterator).setForeground(color);
				}
			}
			if(backgroundColorButton.isSelected()){
				canvas.changeBackgroundColor(buttonColor);
				backgroundColorButton.setSelected(false);
			}
		}
	}
	public JPanel bulidColorPanel(){
		JPanel colorPanel=new JPanel();
		JButton red = new ColorButton(new Color(245, 60, 60));
		JButton green = new ColorButton(new Color(80, 190, 80));
		JButton blue = new ColorButton(new Color(60, 60, 245));
		JButton orange = new ColorButton(new Color(245, 170, 60));
		JButton teal = new ColorButton(new Color(80, 210, 210));
		JButton white = new ColorButton(new Color(255, 255, 255));
		JButton black = new ColorButton(new Color(0, 0, 0));

		red.addActionListener(new ColorListener(new Color(245, 60, 60)));
		blue.addActionListener(new ColorListener(new Color(60, 60, 245)));
		green.addActionListener(new ColorListener(new Color(80, 190, 80)));
		orange.addActionListener(new ColorListener(new Color(245, 170, 60)));
		teal.addActionListener(new ColorListener(new Color(80, 210, 210)));
		white.addActionListener(new ColorListener(new Color(255, 255, 255)));
		black.addActionListener(new ColorListener(new Color(0, 0, 0)));


		colorPanel.add(red);
		colorPanel.add(blue);
		colorPanel.add(green);
		colorPanel.add(orange);
		colorPanel.add(teal);
		colorPanel.add(white);
		colorPanel.add(black);


		return colorPanel;
	}


}
