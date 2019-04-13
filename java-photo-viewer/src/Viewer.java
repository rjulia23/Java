

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;




import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//Once this functionality has been implemented, you will add one more feature of your choosing
//which makes this program more useful or improves the user experience.

public class Viewer extends JFrame{

	//when you make it smalled it should have scroll bars

	//list of files
	private static String path = System.getProperty("user.home") + "/Pictures";
	private static File file = new File(path);
	private static File[] listOfFiles= file.listFiles(new FilenameFilter() {
		public boolean accept(File file, String name) {
			return name.endsWith(".jpg");
		}
	});

	private int count=0;

	private JFrame viewer;
	private JPanel main;
	private JPanel bottomPanel;
	private JLabel pictureArea;
	private JButton backArrow;
	private JButton frontArrow;
	private JCheckBox checkBox;
	private JSlider slider;
	private JLabel secondsLabel;
	private ActionListener autoplayListener = new FrontArrowListener();
	private Timer timer = new Timer(0,autoplayListener);


	public static void main(String[] args){
		JFrame viewer = new Viewer();
		viewer.setSize(900,700);
		viewer.setDefaultCloseOperation(EXIT_ON_CLOSE);
		viewer.setVisible(true);
	}

	public Viewer(){
		createComponents();
	}

	private void setLabelImage(File imgFile, int width, int height) throws IOException {
		Image img = ImageIO.read(imgFile);
		//figure out how to scale img
		//resizing stuff
		if (this.getWidth()==0||this.getHeight()==0)
			pictureArea.setIcon(new ImageIcon(img.getScaledInstance(img.getWidth(this), img.getHeight(this), Image.SCALE_DEFAULT)));
		else{
			width=this.getWidth();
			height=this.getHeight();
			if (img.getHeight(this)>this.getHeight())
				width=(this.getHeight()*img.getWidth(this))/img.getHeight(this);
			else
				height=(this.getWidth()*img.getHeight(this))/img.getWidth(this);
			pictureArea.setIcon(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_DEFAULT)));

		}}

	public class BackArrowListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			try {
				if (count==0)
					count=listOfFiles.length-1;
				else
					count--;
				setLabelImage(listOfFiles[count],700,500);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class FrontArrowListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			try {
				if (count==listOfFiles.length-1)
					count=0;
				else
					count++;
				setLabelImage(listOfFiles[count],700,500);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class AutoplayListener implements ChangeListener{
		public void stateChanged(ChangeEvent event){
			if (checkBox.isSelected()){
				timer.setDelay(slider.getValue()*100);
				timer.start();
			}
			else
				timer.stop();
		}
	}

	public class CheckboxListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if (checkBox.isSelected()){
				timer.setDelay(slider.getValue()*100);
				timer.start();
			}
			else
				timer.stop();
		}
	}



	public void createComponents(){
		main = new JPanel();
		main.setLayout(new BorderLayout());
		bottomPanel = new JPanel();
		pictureArea = new JLabel();
		secondsLabel=new JLabel("sec");
		try {
			setLabelImage(listOfFiles[0],700,500);
		} catch (IOException e) {
			e.printStackTrace();
		}

		checkBox = new JCheckBox("Autoplay");
		slider = new JSlider(0,10,1);

		ChangeListener autoplayListener = new AutoplayListener();
		slider.addChangeListener(autoplayListener);

		backArrow = new JButton("<--");
		frontArrow = new JButton("-->");

		checkBox.addActionListener(new CheckboxListener());

		ActionListener backArrowListener = new BackArrowListener();
		ActionListener frontArrowListener = new FrontArrowListener();
		backArrow.addActionListener(backArrowListener);
		frontArrow.addActionListener(frontArrowListener);

		main.add(backArrow,BorderLayout.WEST);
		main.add(pictureArea,BorderLayout.CENTER);
		main.add(frontArrow,BorderLayout.EAST);
		bottomPanel.add(checkBox);
		bottomPanel.add(slider);
		bottomPanel.add(secondsLabel);
		main.add(bottomPanel,BorderLayout.SOUTH);
		add(main);

	}
}

