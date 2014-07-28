package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;


/**
 * 
 * @author Catree
 *
 */
public class ParametersDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel sliderLabel;
	private JSlider nbThreadsSlider;
	
	private JLabel colorLabel;
	private JComboBox<String> colorComboBox;
	private JColorChooser tcc;
	
	private JLabel patchSize;
	private JComboBox<String> patchComboBox;
	
	private JLabel methodLabel;
	private JComboBox<String> methodComboBox;
	
	private JCheckBox liveRenderingCheckBox;
	
	private JLabel forkJoinLabel;
	private JSpinner forkJoinSpinner;
	
	private ButtonGroup buttonGroup;
	private JRadioButton dynamicSizeRadioButton;
	private JRadioButton fixeSizeRadioButton;
	
	private JLabel widthLabel;
	private JLabel heightLabel;
	private JSpinner widthSpinner;
	private JSpinner heightSpinner;
	
	private JLabel xMinLabel;
	private JLabel yMinLabel;
	private JLabel xMaxLabel;
	private JLabel yMaxLabel;
	private JSpinner xMinSpinner;
	private JSpinner yMinSpinner;
	private JSpinner xMaxSpinner;
	private JSpinner yMaxSpinner;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private MandelbrotDemo mandelbrot;
	
	
	public ParametersDialog(JFrame parent) {
		super(parent, "Properties", true);
		
		mandelbrot = (MandelbrotDemo) parent;
		initComponents();
		
		setMinimumSize(new Dimension(650, 550));
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	private void initComponents() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel centerPanel = new JPanel(new GridBagLayout());
		sliderLabel = new JLabel("Nb threads :");
		
		nbThreadsSlider = new JSlider(1, 2 * mandelbrot.nbCores, mandelbrot.nbThreads);
		nbThreadsSlider.setMajorTickSpacing(mandelbrot.nbCores / 2 );
		nbThreadsSlider.setMinorTickSpacing(1);
		nbThreadsSlider.setPaintTicks(true);
		nbThreadsSlider.setPaintLabels(true);
		
		colorLabel = new JLabel("Color of the fractal :");
		colorComboBox = new JComboBox<String>(new String[]{"Black & White", "Grey level", "Replace red channel", "Replace green channel", "Replace blue channel"});
		colorComboBox.setSelectedIndex(mandelbrot.colorMethod);
		colorComboBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(colorComboBox.getSelectedIndex() == 0 || colorComboBox.getSelectedIndex() == 1) {
					tcc.setEnabled(false);
				} else {
					tcc.setEnabled(true);
				}
			}
		});
		
		tcc = new JColorChooser(mandelbrot.color);
		tcc.removeChooserPanel(tcc.getChooserPanels()[0]);
		tcc.removeChooserPanel(tcc.getChooserPanels()[0]);
		tcc.removeChooserPanel(tcc.getChooserPanels()[0]);
		tcc.removeChooserPanel(tcc.getChooserPanels()[1]);
		tcc.setPreviewPanel(new JPanel());
		tcc.setEnabled(mandelbrot.colorMethod != 0 && mandelbrot.colorMethod != 1);
		
		patchSize = new JLabel("Patch size :");
		patchComboBox = new JComboBox<String>();
		for(int[] patch : mandelbrot.patchSize) {
			patchComboBox.addItem(String.valueOf(patch[0]) + "x" + String.valueOf(patch[0]));
		}
		patchComboBox.setSelectedIndex(mandelbrot.patchSizeIndex);
		
		methodLabel = new JLabel("Multithreading method :");
		methodComboBox = new JComboBox<String>(new String[]{"Regular subdivision", "Multi-patch subdivision", "Fork/Join method"});
		methodComboBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(methodComboBox.getSelectedIndex() == 0) {
					patchComboBox.setEnabled(false);
				} else {
					patchComboBox.setEnabled(true);
				}
			}
		});
		methodComboBox.setSelectedIndex(mandelbrot.multithreadMethod);
		
		liveRenderingCheckBox = new JCheckBox("Live rendering");
		liveRenderingCheckBox.setSelected(mandelbrot.isLiveRendering);
		
		
		forkJoinLabel = new JLabel("Fork/Join threshold :");
		SpinnerModel modelForkJoin = new SpinnerNumberModel(mandelbrot.threshold, //initial value
                100, //min
                1000000, //max
                100); //step
		forkJoinSpinner = new JSpinner(modelForkJoin);
		
		
		dynamicSizeRadioButton = new JRadioButton("Dynamic size");
		dynamicSizeRadioButton.setSelected(!mandelbrot.isFixeSize);
		dynamicSizeRadioButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				widthSpinner.setEnabled(false);
				heightSpinner.setEnabled(false);
			}
		});
		
		fixeSizeRadioButton = new JRadioButton("Fixe size");
		fixeSizeRadioButton.setSelected(mandelbrot.isFixeSize);
		fixeSizeRadioButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				widthSpinner.setEnabled(true);
				heightSpinner.setEnabled(true);
			}
		});
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(dynamicSizeRadioButton);
		buttonGroup.add(fixeSizeRadioButton);
		JPanel gridPanel = new JPanel(new GridBagLayout());
		
		widthLabel = new JLabel("Rendering width :");
		heightLabel = new JLabel("Rendering height :");
		
		SpinnerModel modelWidth = new SpinnerNumberModel(mandelbrot.width, //initial value
		                               100, //min
		                               10000, //max
		                               50); //step
		SpinnerModel modelHeight = new SpinnerNumberModel(mandelbrot.height, //initial value
						                100, //min
						                10000, //max
						                50); //step
		
		widthSpinner = new JSpinner(modelWidth);
		widthSpinner.setEnabled(!dynamicSizeRadioButton.isSelected());
		
		heightSpinner = new JSpinner(modelHeight);
		heightSpinner.setEnabled(!dynamicSizeRadioButton.isSelected());

		xMinLabel = new JLabel("x min :");
		yMinLabel = new JLabel("y min :");
		xMaxLabel = new JLabel("x max :");
		yMaxLabel = new JLabel("y max :");
	
		SpinnerModel xMinModel = new SpinnerNumberModel(mandelbrot.x1, //initial value
			                -2.1, //min
			                0.6, //max
			                0.05); //step
		SpinnerModel yMinModel = new SpinnerNumberModel(mandelbrot.y1, //initial value
			                -1.2, //min
			                1.2, //max
			                0.05); //step
		SpinnerModel xMaxModel = new SpinnerNumberModel(mandelbrot.x2, //initial value
		                -2.1, //min
		                0.6, //max
		                0.05); //step
		SpinnerModel yMaxModel = new SpinnerNumberModel(mandelbrot.y2, //initial value
		                -1.2, //min
		                1.2, //max
		                0.05); //step
		
		xMinSpinner = new JSpinner(xMinModel);
		yMinSpinner = new JSpinner(yMinModel);
		xMaxSpinner = new JSpinner(xMaxModel);
		yMaxSpinner = new JSpinner(yMaxModel);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 5);
		gridPanel.add(dynamicSizeRadioButton, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.WEST;
		gridPanel.add(widthLabel, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		gridPanel.add(widthSpinner, c);
		
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		gridPanel.add(xMinLabel, c);
		
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		gridPanel.add(xMinSpinner, c);
		
		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		gridPanel.add(xMaxLabel, c);
		
		c = new GridBagConstraints();
		c.gridx = 6;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 10);
		gridPanel.add(xMaxSpinner, c);
		
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 10, 5);
		gridPanel.add(fixeSizeRadioButton, c);		
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 10, 5);
		gridPanel.add(heightLabel, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 10, 5);
		gridPanel.add(heightSpinner, c);
		
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 10, 5);
		gridPanel.add(yMinLabel, c);
		
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 10, 5);
		gridPanel.add(yMinSpinner, c);
		
		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 10, 5);
		gridPanel.add(yMaxLabel, c);
		
		c = new GridBagConstraints();
		c.gridx = 6;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 10, 10);
		gridPanel.add(yMaxSpinner, c);
		
		
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.gridy = 0;
		c1.weightx = 0;
		c1.weighty = 0;
		c1.anchor = GridBagConstraints.WEST;
		c1.insets = new Insets(10, 10, 5, 5);
		
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 1;
		c2.gridy = 0;
		c2.gridwidth = 5;
		c2.weightx = 100;
		c2.weighty = 0;
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.insets = new Insets(10, 5, 10, 10);
		
		GridBagConstraints c3 = new GridBagConstraints();
		c3.gridx = 0;
		c3.gridy = 1;
		c3.weightx = 0;
		c3.weighty = 0;
		c3.anchor = GridBagConstraints.WEST;
		c3.insets = new Insets(5, 10, 5, 5);
		
		GridBagConstraints c32 = new GridBagConstraints();
		c32.gridx = 1;
		c32.gridy = 1;
		c32.gridwidth = 2;
		c32.anchor = GridBagConstraints.CENTER;
		c32.insets = new Insets(5, 5, 5, 10);
		
		GridBagConstraints c4 = new GridBagConstraints();
		c4.gridx = 0;
		c4.gridy = 2;
		c4.gridwidth = 6;
		c4.gridheight = 6;
		c4.anchor = GridBagConstraints.CENTER;
		c4.insets = new Insets(5, 10, 5, 10);
		
		GridBagConstraints c7 = new GridBagConstraints();
		c7.gridx = 0;
		c7.gridy = 8;
		c7.weightx = 0;
		c7.weighty = 0;
		c7.gridwidth = 2;
		c7.anchor = GridBagConstraints.CENTER;
		c7.insets = new Insets(5, 10, 5, 5);
		
		GridBagConstraints c8 = new GridBagConstraints();
		c8.gridx = 2;
		c8.gridy = 8;
		c8.weightx = 0;
		c8.weighty = 0;
		c8.gridwidth = 2;
		c8.anchor = GridBagConstraints.CENTER;
		c8.insets = new Insets(5, 5, 5, 5);
		
		GridBagConstraints c9 = new GridBagConstraints();
		c9.gridx = 4;
		c9.gridy = 8;
		c9.weightx = 0;
		c9.weighty = 0;
		c9.anchor = GridBagConstraints.CENTER;
		c9.insets = new Insets(5, 5, 5, 10);
		
		GridBagConstraints c5 = new GridBagConstraints();
		c5.gridx = 0;
		c5.gridy = 9;
		c5.weightx = 0;
		c5.weighty = 0;
		c5.anchor = GridBagConstraints.CENTER;
		c5.insets = new Insets(5, 10, 5, 5);
		
		GridBagConstraints c6 = new GridBagConstraints();
		c6.gridx = 1;
		c6.gridy = 9;
		c6.weightx = 0;
		c6.weighty = 0;
		c6.anchor = GridBagConstraints.CENTER;
		c6.insets = new Insets(5, 5, 5, 5);
		
		GridBagConstraints c11 = new GridBagConstraints();
		c11.gridx = 2;
		c11.gridy = 9;
		c11.weightx = 0;
		c11.weighty = 0;
		c11.anchor = GridBagConstraints.CENTER;
		c11.insets = new Insets(5, 5, 5, 5);
		
		GridBagConstraints c12 = new GridBagConstraints();
		c12.gridx = 3;
		c12.gridy = 9;
		c12.weightx = 0;
		c12.weighty = 0;
		c12.anchor = GridBagConstraints.CENTER;
		c12.insets = new Insets(5, 5, 5, 10);
		
		GridBagConstraints c10 = new GridBagConstraints();
		c10.gridx = 0;
		c10.gridy = 10;
		c10.gridwidth = 6;
		c10.gridheight = 2;
		c10.weightx = 50;
		c10.weighty = 0;
		c10.anchor = GridBagConstraints.CENTER;
		c10.insets = new Insets(5, 10, 10, 10);

		centerPanel.add(sliderLabel, c1);
		centerPanel.add(nbThreadsSlider, c2);
		centerPanel.add(colorLabel, c3);
		centerPanel.add(colorComboBox, c32);
		centerPanel.add(tcc, c4);
		centerPanel.add(methodLabel, c7);
		centerPanel.add(methodComboBox, c8);
		centerPanel.add(liveRenderingCheckBox, c9);
		centerPanel.add(patchSize, c5);
		centerPanel.add(patchComboBox, c6);
		centerPanel.add(forkJoinLabel, c11);
		centerPanel.add(forkJoinSpinner, c12);
		centerPanel.add(gridPanel, c10);

		
		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				mandelbrot.setNbCores(nbThreadsSlider.getValue());
				
				mandelbrot.colorMethod = colorComboBox.getSelectedIndex();
				mandelbrot.color = tcc.getColor();
				
				mandelbrot.patchSizeIndex = patchComboBox.getSelectedIndex();
				mandelbrot.multithreadMethod = methodComboBox.getSelectedIndex();
				mandelbrot.isLiveRendering = liveRenderingCheckBox.isSelected();
				
				mandelbrot.threshold = (int) forkJoinSpinner.getValue();
				
				mandelbrot.isFixeSize = fixeSizeRadioButton.isSelected();
				mandelbrot.width = (int) widthSpinner.getValue();
				mandelbrot.height = (int) heightSpinner.getValue();
				
				if((double) xMinSpinner.getValue() < (double) xMaxSpinner.getValue()) {
					mandelbrot.x1 = (double) xMinSpinner.getValue();
					mandelbrot.x2 = (double) xMaxSpinner.getValue();
				}
				
				if((double) yMinSpinner.getValue() < (double) yMaxSpinner.getValue()) {
					mandelbrot.y1 = (double) yMinSpinner.getValue();
					mandelbrot.y2 = (double) yMaxSpinner.getValue();
				}
				dispose();
			}
		});
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JPanel interactionPanel = new JPanel();
		interactionPanel.add(okButton);
		interactionPanel.add(cancelButton);
		
		add("Center", centerPanel);
		add("South", interactionPanel);
	}

}
