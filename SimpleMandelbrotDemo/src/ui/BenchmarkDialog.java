package ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


/**
 * 
 * @author Catree
 *
 */
public class BenchmarkDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MandelbrotDemo mandelbrot;
	
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JLabel nbRunLabel;
	private JSpinner nbRunSpinner;
	private JButton okButton;
	private JButton cancelButton;
	
	private int nbRun = 0;
	private long totalTime = 0;
	
	private static final String[] METHOD_NAME = new String[]{"Regular subdivision", "Multi-patch subdivision", "Fork/Join method"};
	

	public BenchmarkDialog(JFrame parent) {
		super(parent, "Benchmark", true);
		
		mandelbrot = (MandelbrotDemo) parent;
		initComponents();
		
		setMinimumSize(new Dimension(500, 400));
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	private void initComponents() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		scrollPane = new JScrollPane(textArea);
		
		nbRunLabel = new JLabel("Nb runs");
		SpinnerModel nbRunModel = new SpinnerNumberModel(10, //initial value
                1, //min
                100, //max
                5); //step
		nbRunSpinner = new JSpinner(nbRunModel);
		
		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				okButton.setEnabled(false);
				cancelButton.setEnabled(false);
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				mandelbrot.zoomX = mandelbrot.width / (mandelbrot.x2 - mandelbrot.x1);
				mandelbrot.zoomY = mandelbrot.height / (mandelbrot.y2 - mandelbrot.y1);
				mandelbrot.imageArray = new int[mandelbrot.width * mandelbrot.height];
				
				nbRun = (int) nbRunSpinner.getValue();
				textArea.append("BENCHMARK :" + System.lineSeparator());
				textArea.append("Method : " + BenchmarkDialog.METHOD_NAME[mandelbrot.multithreadMethod] + System.lineSeparator());
				textArea.append("Image size=" + mandelbrot.width + "x" + mandelbrot.height + System.lineSeparator());
				
				SwingWorker<Object, Object> sw = new SwingWorker<Object, Object>() {
					@Override
					protected Object doInBackground() throws Exception {
						runBenchmark();
				        
						return null;
					}
					
					@Override
					protected void done() {
						okButton.setEnabled(true);
						cancelButton.setEnabled(true);
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				};
		    	sw.execute();
			}
		});
		
		cancelButton = new JButton("Quit");
		cancelButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JPanel interactionPanel = new JPanel();
		interactionPanel.add(okButton);
		interactionPanel.add(cancelButton);
		interactionPanel.add(nbRunLabel);
		interactionPanel.add(nbRunSpinner);

		add("Center", scrollPane);
		add("South", interactionPanel);
	}
	
	private void runBenchmark() {
		totalTime = 0;
		switch(mandelbrot.multithreadMethod) {
		case 0:
		{
			for(int cpt = 0; cpt<nbRun; cpt++) {
				totalTime += mandelbrot.multithread0(true);
			}
			
			final double totalTimeSecond = totalTime / 1000.;
			final double averageTimeSecond = totalTimeSecond / (double) nbRun;
			
			SwingUtilities.invokeLater(new Runnable() {				
				@Override
				public void run() {
					textArea.append("Total time=" + totalTimeSecond + " s ; Average time=" + averageTimeSecond + " s");
					textArea.append(System.lineSeparator() + System.lineSeparator());
				}
			});
		}
			break;
			
		case 1:
		{
			for(int cpt = 0; cpt<nbRun; cpt++) {
				totalTime += mandelbrot.multithread1(true);
			}
			
			final double totalTimeSecond = totalTime / 1000.;
			final double averageTimeSecond = totalTimeSecond / (double) nbRun;
			
			SwingUtilities.invokeLater(new Runnable() {				
				@Override
				public void run() {
					textArea.append("Total time=" + totalTimeSecond + " s ; Average time=" + averageTimeSecond + " s");
					textArea.append(System.lineSeparator() + System.lineSeparator());
				}
			});
		}
			break;
			
		case 2:
		{
			for(int cpt = 0; cpt<nbRun; cpt++) {
				totalTime += mandelbrot.multithread2(true);
			}
			
			final double totalTimeSecond = totalTime / 1000.;
			final double averageTimeSecond = totalTimeSecond / (double) nbRun;
			
			SwingUtilities.invokeLater(new Runnable() {				
				@Override
				public void run() {
					textArea.append("Total time=" + totalTimeSecond + " s ; Average time=" + averageTimeSecond + " s");
					textArea.append(System.lineSeparator() + System.lineSeparator());
				}
			});
		}
			break;
			
		default :
			break;
		}
	}
}
