package ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
	private JLabel nbThreadLabel;
	private JComboBox<Integer> nbThreadComboBox;
	private JComboBox<String> multithreadMethodComboBox;
	
	private int nbRun = 0;
	private long totalTime = 0;
	
	private static final String[] METHOD_NAME = new String[]{"Regular subdivision", "Multi-patch subdivision", "Fork/Join method"};
	

	public BenchmarkDialog(JFrame parent) {
		super(parent, "Benchmark", true);
		
		mandelbrot = (MandelbrotDemo) parent;
		initComponents();
		
		setMinimumSize(new Dimension(550, 400));
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
		
		nbThreadLabel = new JLabel("Nb threads :");
		nbThreadComboBox = new JComboBox<Integer>();
		for(int i = 1; i <= 2 * mandelbrot.nbCores; i++) {
			nbThreadComboBox.addItem(new Integer(i));
		}
		nbThreadComboBox.setSelectedIndex(mandelbrot.nbThreads - 1);
		
		multithreadMethodComboBox = new JComboBox<String>(METHOD_NAME);

		
		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				okButton.setEnabled(false);
				cancelButton.setEnabled(false);
				nbRunSpinner.setEnabled(false);
				nbThreadComboBox.setEnabled(false);
				multithreadMethodComboBox.setEnabled(false);
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				mandelbrot.zoomX = mandelbrot.width / (mandelbrot.x2 - mandelbrot.x1);
				mandelbrot.zoomY = mandelbrot.height / (mandelbrot.y2 - mandelbrot.y1);
				mandelbrot.imageArray = new int[mandelbrot.width * mandelbrot.height];
				
				nbRun = (int) nbRunSpinner.getValue();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				textArea.append("BENCHMARK at " + sdf.format(Calendar.getInstance().getTime()) + System.lineSeparator());
				textArea.append("Image size=" + mandelbrot.width + "x" + mandelbrot.height + System.lineSeparator());
				textArea.append("Nb threads=" + nbThreadComboBox.getSelectedItem() + System.lineSeparator());
				textArea.append("Method : " + multithreadMethodComboBox.getSelectedItem() + System.lineSeparator());
				if(multithreadMethodComboBox.getSelectedIndex() == 1) {
					textArea.append("Patch size=" + mandelbrot.patchSize[mandelbrot.patchSizeIndex][0] + "x" + 
							mandelbrot.patchSize[mandelbrot.patchSizeIndex][1]+ System.lineSeparator());
				}				
				
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
						nbRunSpinner.setEnabled(true);
						nbThreadComboBox.setEnabled(true);
						multithreadMethodComboBox.setEnabled(true);
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
		interactionPanel.add(nbThreadLabel);
		interactionPanel.add(nbThreadComboBox);
		interactionPanel.add(multithreadMethodComboBox);

		add("Center", scrollPane);
		add("South", interactionPanel);
	}
	
	private void runBenchmark() {
		totalTime = 0;
		int saveNbThreads = mandelbrot.nbThreads;
		mandelbrot.nbThreads = (int) nbThreadComboBox.getSelectedItem();
		
		switch(multithreadMethodComboBox.getSelectedIndex()) {
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
					textArea.append(System.lineSeparator() + System.lineSeparator() + System.lineSeparator());
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
					textArea.append(System.lineSeparator() + System.lineSeparator() + System.lineSeparator());
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
					textArea.append(System.lineSeparator() + System.lineSeparator() + System.lineSeparator());
				}
			});
		}
			break;
			
		default :
			break;
		}

		mandelbrot.nbThreads = saveNbThreads;
	}
}
