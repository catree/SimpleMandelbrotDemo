package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JComboBox<String> colorComboBox;
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
		
		nbCoresSlider = new JSlider(1, 2 * mandelbrot.nbCores, mandelbrot.nbThreads);
		nbCoresSlider.setMajorTickSpacing(mandelbrot.nbCores / 2 );
		nbCoresSlider.setMinorTickSpacing(1);
		nbCoresSlider.setPaintTicks(true);
		nbCoresSlider.setPaintLabels(true);
		
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
		methodComboBox = new JComboBox<String>(new String[]{"Regular subdivision", "Multi-patch subdivision"});
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
		JPanel gridPanel = new JPanel(new GridLayout(2, 2));
		
		widthLabel = new JLabel("Rendering width :");
		heightLabel = new JLabel("Rendering height :");
		
		SpinnerModel modelWidth = new SpinnerNumberModel(mandelbrot.width, //initial value
		                               100, //min
		                               10000, //max
		                               50);
		SpinnerModel modelHeight = new SpinnerNumberModel(mandelbrot.height, //initial value
						                100, //min
						                10000, //max
						                50);
		
		widthSpinner = new JSpinner(modelWidth);
		widthSpinner.setEnabled(!dynamicSizeRadioButton.isSelected());
		
		heightSpinner = new JSpinner(modelHeight);
		heightSpinner.setEnabled(!dynamicSizeRadioButton.isSelected());
		
		gridPanel.add(dynamicSizeRadioButton);
		gridPanel.add(widthLabel);
		gridPanel.add(widthSpinner);
		gridPanel.add(fixeSizeRadioButton);
		gridPanel.add(heightLabel);
		gridPanel.add(heightSpinner);
		
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
		
		GridBagConstraints c32 = new GridBagConstraints();
		c32.gridx = 1;
		c32.gridy = 1;
		c32.gridwidth = 2;
		c32.anchor = GridBagConstraints.CENTER;
		c32.insets = new Insets(5, 10, 0, 5);
		
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
		c9.gridwidth = 4;
		c9.gridheight = 2;
		c9.weightx = 50;
		c9.weighty = 0;
		c9.anchor = GridBagConstraints.CENTER;
		c9.insets = new Insets(10, 10, 10, 10);

		centerPanel.add(sliderLabel, c1);
		centerPanel.add(nbCoresSlider, c2);
		centerPanel.add(colorLabel, c3);
		centerPanel.add(colorComboBox, c32);
		centerPanel.add(tcc, c4);
		centerPanel.add(patchSize, c5);
		centerPanel.add(patchComboBox, c6);
		centerPanel.add(methodLabel, c7);
		centerPanel.add(methodComboBox, c8);
		centerPanel.add(gridPanel, c9);

		
		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				mandelbrot.setNbCores(nbCoresSlider.getValue());
				mandelbrot.colorMethod = colorComboBox.getSelectedIndex();
				mandelbrot.color = tcc.getColor();
				mandelbrot.patchSizeIndex = patchComboBox.getSelectedIndex();
				mandelbrot.multithreadMethod = methodComboBox.getSelectedIndex();
				mandelbrot.isFixeSize = fixeSizeRadioButton.isSelected();
				mandelbrot.width = (int) widthSpinner.getValue();
				mandelbrot.height = (int) heightSpinner.getValue();				
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
