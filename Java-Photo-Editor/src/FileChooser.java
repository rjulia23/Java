import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FileChooser extends JFileChooser{
	public FileChooser(){
	    this(null);
	}

	public  FileChooser(String path) {
	    super(path);
	    try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	        SwingUtilities.updateComponentTreeUI(this);
	        this.pack();
	    } catch (Exception ex) {
	        Logger.getLogger(FileChooser.class.getName()).log(Level.SEVERE, null, ex);
	    }

	    JFileChooser chooser = new JFileChooser();
	}

	private void pack() {
	    try{
	    }catch(UnsupportedOperationException eu){
	    };
	}
}
