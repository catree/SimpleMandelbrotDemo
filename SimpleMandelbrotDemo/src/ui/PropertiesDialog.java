package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
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

public class PropertiesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel sliderLabel;
	private JSlider nbCoresSlider;
	
	private JLabel colorLabel;
	private JColorChooser tcc;
	
	private JLabel patchSize;
	private JComboBox<String> patchComboBox;
	
	private JLabel methodLabel;
	private JComboBox<String> methodComboBox;
	
	private ButtonGroup buttonGroup;
	private JRadioButton dynamicSizeRadioButton;
	private JRadioButton fixeSizeRadioButton;
	
	private JLabel widthLabel;
	private JLabel heightLabel;
	private JSpinner widthSpinner;
	private JSpinner heightSpinner;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private MandelbrotDemo mandelbrot;
	
	
	public PropertiesDialog(JFrame parent) {
		super(parent, "Properties", true);
		
		mandelbrot = (MandelbrotDemo) parent;
		initComponents();
		
		setMinimumSize(new Dimension(600, 500));
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	private void initComponents() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel centerPanel = new JPanel(new GridBagLayout());
		sliderLabel = new JLabel("Nb threads :");
		
		int nbCores = Runtime.getRuntime().availableProcessors();
		nbCoresSlider = new JSlider(1, 2 * nbCores, nbCores);
		nbCoresSlider.setMajorTickSpacing(nbCores / 2 );
		nbCoresSlider.setMinorTickSpacing(1);
		nbCoresSlider.setPaintTicks(true);
		nbCoresSlider.setPaintLabels(true);
		
		colorLabel = new JLabel("Color of the fractal :");
		
		tcc = new JColorChooser();
		tcc.removeChooserPanel(tcc.getChooserPanels()[0]);
		tcc.removeChooserPanel(tcc.getChooserPanels()[0]);
		tcc.removeChooserPanel(tcc.getChooserPanels()[0]);
		tcc.removeChooserPanel(tcc.getChooserPanels()[1]);
		tcc.setPreviewPanel(new JPanel());
		
		patchSize = new JLabel("Patch size :");
		patchComboBox = new JComboBox<String>(new String[]{"5x5", "10x10", "25x25", "50x50"});
		patchComboBox.setSelectedIndex(1);
		
		methodLabel = new JLabel("Multithreading method :");
		methodComboBox = new JComboBox<String>(new String[]{"Regular subdivision", "Multi-patch subdivision"});
		methodComboBox.setSelectedIndex(1);
		
		dynamicSizeRadioButton = new JRadioButton("Dynamic size");
		dynamicSizeRadioButton.setSelected(true);
		fixeSizeRadioButton = new JRadioButton("Fixe size");
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(dynamicSizeRadioButton);
		buttonGroup.add(fixeSizeRadioButton);
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		radioPanel.add(dynamicSizeRadioButton);
		radioPanel.add(fixeSizeRadioButton);
		
		widthLabel = new JLabel("Rendering width :");
		heightLabel = new JLabel("Rendering height :");
		
		SpinnerModel model = new SpinnerNumberModel(1000, //initial value
		                               100, //min
		                               10000, //max
		                               50);  
		widthSpinner = new JSpinner(model);
		heightSpinner = new JSpinner(model);
		
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
		c2.insets = new Insets(10, 5, 10, 5);
		
		GridBagConstraints c3 = new GridBagConstraints();
		c3.gridx = 0;
		c3.gridy = 1;
		c3.weightx = 0;
		c3.weighty = 0;
		c3.anchor = GridBagConstraints.WEST;
		c3.insets = new Insets(5, 10, 0, 5);
		
		GridBagConstraints c4 = new GridBagConstraints();
		c4.gridx = 0;
		c4.gridy = 2;
		c4.gridwidth = 6;
		c4.gridheight = 6;
		c4.anchor = GridBagConstraints.CENTER;
		c4.insets = new Insets(5, 10, 10, 5);
		
		GridBagConstraints c5 = new GridBagConstraints();
		c5.gridx = 0;
		c5.gridy = 8;
		c5.weightx = 0;
		c5.weighty = 0;
		c5.anchor = GridBagConstraints.WEST;
		c5.insets = new Insets(5, 10, 5, 5);
		
		GridBagConstraints c6 = new GridBagConstraints();
		c6.gridx = 1;
		c6.gridy = 8;
		c6.weightx = 0;
		c6.weighty = 0;
		c6.anchor = GridBagConstraints.CENTER;
		c6.insets = new Insets(5, 10, 10, 5);
		
		GridBagConstraints c7 = new GridBagConstraints();
		c7.gridx = 2;
		c7.gridy = 8;
		c7.weightx = 0;
		c7.weighty = 0;
		c7.anchor = GridBagConstraints.WEST;
		c7.insets = new Insets(5, 5, 5, 5);
		
		GridBagConstraints c8 = new GridBagConstraints();
		c8.gridx = 3;
		c8.gridy = 8;
		c8.weightx = 0;
		c8.weighty = 0;
		c8.anchor = GridBagConstraints.CENTER;
		c8.insets = new Insets(5, 5, 10, 5);
		
		GridBagConstraints c9 = new GridBagConstraints();
		c9.gridx = 0;
		c9.gridy = 9;
		c9.gridwidth = 1;
		c9.gridheight = 2;
		c9.weightx = 0;
		c9.weighty = 0;
		c9.anchor = GridBagConstraints.EAST;
		c9.insets = new Insets(20, 5, 5, 0);
		
		GridBagConstraints c10 = new GridBagConstraints();
		c10.gridx = 1;
		c10.gridy = 9;
		c10.gridwidth = 2;
		c10.gridheight = 2;
		c10.weightx = 0;
		c10.weighty = 0;
		c10.anchor = GridBagConstraints.CENTER;
		c10.insets = new Insets(5, 0, 5, 0);
		
		GridBagConstraints c11 = new GridBagConstraints();
		c11.gridx = 3;
		c11.gridy = 9;
		c11.gridwidth = 1;
		c11.gridheight = 2;
		c11.weightx = 0;
		c11.weighty = 0;
		c11.anchor = GridBagConstraints.CENTER;
		c11.insets = new Insets(5, 0, 5, 0);
		
		GridBagConstraints c12 = new GridBagConstraints();
		c12.gridx = 1;
		c12.gridy = 10;
		c12.gridwidth = 2;
		c12.gridheight = 2;
		c12.weightx = 2;
		c12.weighty = 2;
		c12.anchor = GridBagConstraints.CENTER;
		c12.insets = new Insets(5, 0, 5, 0);
		
		GridBagConstraints c13 = new GridBagConstraints();
		c13.gridx = 3;
		c13.gridy = 10;
		c13.gridwidth = 1;
		c13.gridheight = 2;
		c13.weightx = 0;
		c13.weighty = 0;
		c13.anchor = GridBagConstraints.CENTER;
		c13.insets = new Insets(5, 0, 5, 0);
		
		GridBagConstraints c14 = new GridBagConstraints();
		c14.gridx = 4;
		c14.gridy = 9;
		c14.gridwidth = 2;
		c14.gridheight = 1;
		c14.weightx = 100;
		c14.weighty = 0;
		c14.fill = GridBagConstraints.BOTH;
		c14.insets = new Insets(5, 5, 5, 5);

		centerPanel.add(sliderLabel, c1);
		centerPanel.add(nbCoresSlider, c2);
		centerPanel.add(colorLabel, c3);
		centerPanel.add(tcc, c4);
		centerPanel.add(patchSize, c5);
		centerPanel.add(patchComboBox, c6);
		centerPanel.add(methodLabel, c7);
		centerPanel.add(methodComboBox, c8);
		centerPanel.add(radioPanel, c9);
		centerPanel.add(widthLabel, c10);
		centerPanel.add(widthSpinner, c11);
		centerPanel.add(heightLabel, c12);
		centerPanel.add(heightSpinner, c13);
		centerPanel.add(Box.createHorizontalGlue(), c14);
		
		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				mandelbrot.setNbCores(nbCoresSlider.getValue());
				mandelbrot.color = tcc.getColor();
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
