package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;


/**
 * 
 * @author Catree
 *
 */
public class HelpAboutDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public HelpAboutDialog(JFrame parent) {
		super(parent, "Help / License / About", true);
		
		initComponents();
		
		setMinimumSize(new Dimension(600, 500));
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	private void initComponents() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JTextArea textArea1 = new JTextArea();
		textArea1.setEditable(false);
		textArea1.setLineWrap(true);
		StringBuilder sb = new StringBuilder();
		sb.append("Main window :" + System.lineSeparator());
		sb.append("\t- Start button : launch the rendering of the Mandelbrot set" + System.lineSeparator());
		sb.append("\t- Nb threads : number of threads used to render the image" + System.lineSeparator());
		sb.append("\t- Processing time : Time to compute the image in second" + System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("Interaction with the image :" + System.lineSeparator());
		sb.append("\t- Zoom : draw a rectangle (from left ro right) on the area you want to zoom" + System.lineSeparator());
		sb.append("\t- Dezoom : draw a rectangle from right to left anywhere in the canvas" + System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("Save image in Option menu > Save image : save the image on the disk" + System.lineSeparator());
		sb.append("\t- Start button : launch the rendering of the Mandelbrot set" + System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("Parameters in Option menu > Parameters :" + System.lineSeparator());
		sb.append("\t- Nb threads : choose the number of threads you want to use (it is recommanded to choose nbThreads = nbCpuVirtualCores)" + System.lineSeparator());
		sb.append("\t- Color of the fractal :" + System.lineSeparator());
		sb.append("\t\t- Black and white" + System.lineSeparator());
		sb.append("\t\t- Grey Level" + System.lineSeparator());
		sb.append("\t\t- Replace the red channel with the value of the Mandelbrot set" + System.lineSeparator());
		sb.append("\t\t- Replace the green channel with the value of the Mandelbrot set" + System.lineSeparator());
		sb.append("\t\t- Replace the blue channel with the value of the Mandelbrot set" + System.lineSeparator());
		sb.append("\t\t- Alpha channel is not used" + System.lineSeparator());
		sb.append("\t- Multithreading method :" + System.lineSeparator());
		sb.append("\t\t- Regular subdivision : the image is subdivided by the number of threads used" + System.lineSeparator());
		sb.append("\t\t- Multi patch subdivision : the image is subdivided by small patches" + System.lineSeparator());
		sb.append("\t\t- Fork/Join method : use the fork join framework (fork a task in small pieces like divide and conquer method)" + System.lineSeparator());
		sb.append("\t\t- Patch size : choose the size of the patch for the multi patch subdivision method" + System.lineSeparator());
		sb.append("\t\t- Live rendering option : if set you can see the rendering in live, the image is display only when the computation is done" + System.lineSeparator());
		sb.append("\t- Dynamic / Fixe rendering size : if dynamic, the size of the computated image depends on the size of the window, "
				+ "otherwise you can set the width and the height (if you want to save an image of a predefined size for example)" + System.lineSeparator());
		sb.append("\t- xMin, xMax, yMin, yMax : lower and upper bounds for the computation of the Mandelbrot set" + System.lineSeparator());
		textArea1.setText(sb.toString());
		textArea1.setCaretPosition(0);
		
		JScrollPane scrollPane1 = new JScrollPane(textArea1);
		
		JTextArea textArea2 = new JTextArea();
		textArea2.setEditable(false);
		textArea2.setText("LICENSE" + System.lineSeparator() + "The license of this project is GNU GPL v3.0 available here : "
				+ "http://choosealicense.com/licenses/gpl-3.0/");
		
		try (BufferedReader br = new BufferedReader(new FileReader("LICENSE"))) {
			sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			
			textArea2.setText(sb.toString());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		textArea2.setCaretPosition(0);
		JScrollPane scrollPane2 = new JScrollPane(textArea2);
		
		JTextArea textArea3 = new JTextArea();
		textArea3.setEditable(false);
		sb = new StringBuilder();
		sb.append("SimpleMandelbrotDemo by Catree." + System.lineSeparator());
		sb.append("A training project in Java to try to use a multithreading approach (with his pitfalls) in a simple project." + System.lineSeparator());
		sb.append("Main goals : " + System.lineSeparator());
		sb.append("\t- Mandelbrot rendering" + System.lineSeparator());
		sb.append("\t- Multithreading rendering with different approaches" + System.lineSeparator());
		sb.append("\t- Live and offline rendering" + System.lineSeparator());
		textArea3.setText(sb.toString());

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add("Help", scrollPane1);
		tabbedPane.add("License", scrollPane2);
		tabbedPane.add("About", textArea3);
		
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		JPanel interactionPanel = new JPanel();
		interactionPanel.add(okButton);
		
		add("Center", tabbedPane);
		add("South", interactionPanel);
	}
}
