package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.text.NumberFormatter;

import maths.Complex;

/**
 * 
 * @see http://introcs.cs.princeton.edu/java/32class/Mandelbrot.java.html
 *
 */
public class MandelbrotDemo extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected double x1 = -2.1;
	protected double x2 = 0.6;
	protected double y1  = -1.2;
	protected double y2 = 1.2;
	protected double zoomX;
	protected double zoomY;
	
	protected int maxIteration = 255;
	protected int width  = 800;
	protected int height = 800;
	
	protected BufferedImage renderImage = null;
	protected Object lock = new Object();
	
	private Canvas canvas;
	private JButton startButton;
	private JTextField infoTextField;
	private JMenuBar menuBar;
	private JMenu menu1;
	private JMenu menu2;
//	private JFormattedTextField renderWidthTextField;
//	private JFormattedTextField renderHeightTextField;
	
	private MandelbrotDemo instance;
	private NumberFormat format = NumberFormat.getInstance();
	private boolean isComputing = false;
	private int nbCores;
	
	private Deque<double[]> stackOfZoom = null;
	private Deque<BufferedImage> stackOfZoomImage = null;
	
	
	public MandelbrotDemo() {
		super("Mandelbrot");
		
		instance = this;
		stackOfZoomImage = new ArrayDeque<BufferedImage>();
		stackOfZoom = new ArrayDeque<double[]>();
		
		initComponents();
		
		setMinimumSize(new Dimension(640, 480));
		setSize(850, 850);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initComponents() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		canvas = new Canvas();
		
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(50);
	    formatter.setMaximum(100000);
	    
//		renderWidthTextField = new JFormattedTextField(formatter);
//		renderWidthTextField.setColumns(5);
//		
//		renderHeightTextField = new JFormattedTextField(formatter);
//		renderHeightTextField.setColumns(5);
		
		startButton = new JButton("Start !");
		startButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				computeMandelbrot();
				if(stackOfZoomImage.isEmpty()) {
					stackOfZoomImage.add(renderImage);
					stackOfZoom.add(new double[]{x1, y1, x2, y2});
				}
			}
		});

		nbCores = Runtime.getRuntime().availableProcessors();
		infoTextField = new JTextField("nb cores : " + nbCores);
		infoTextField.setColumns(20);
		infoTextField.setEditable(false);
		
		JPanel interactionPanel = new JPanel();
//		interactionPanel.add(renderWidthTextField);
//		interactionPanel.add(renderHeightTextField);
		interactionPanel.add(startButton);
		interactionPanel.add(infoTextField);
		
		menu1 = new JMenu("File");
		JMenuItem saveItem = new JMenuItem("Save Image");
		saveItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(null != renderImage) {
					JFileChooser fc = new JFileChooser();
					int returnVal = fc.showSaveDialog(MandelbrotDemo.this);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            if(file.getName().toLowerCase().endsWith(".png")) {
			            	try {
								ImageIO.write(renderImage, "png", file);
							} catch (IOException e) {
								e.printStackTrace();
							}
			            } else {
			            	if(!file.getName().toLowerCase().endsWith(".jpg")) {
			            		file = new File(file.getAbsolutePath() + ".jpg");
			            	}
			            	try {
								ImageIO.write(renderImage, "jpg", file);
							} catch (IOException e) {
								e.printStackTrace();
							}
			            }
			        }
				}
			}
		});
		
		JMenuItem paramItem = new JMenuItem("Parameters");
		menu1.add(saveItem);
		menu1.add(paramItem);
		
		menu2 = new JMenu("Help");
		menuBar = new JMenuBar();
		menuBar.add(menu1);
		menuBar.add(menu2);
		
		setJMenuBar(menuBar);
		
		add("Center", canvas);
		add("South", interactionPanel);
	}

    // return number of iterations to check if c = a + ib is in Mandelbrot set
    public static int mandelbrot(Complex z0, int max) {
        Complex z = z0;
        for (int t = 0; t < max; t++) {
            if (z.abs() > 2.0) return t;
            z = z.times(z).plus(z0);
        }
        return max;
    }
    
    public static int grayMandelbrotFormula(Complex z0, int maxIteration) {
    	return maxIteration - MandelbrotDemo.mandelbrot(z0, maxIteration);
    }
    
    public static int colorMandelbrotFormula(Complex z0, int maxIteration) {
    	if(maxIteration - MandelbrotDemo.mandelbrot(z0, maxIteration) == 0) {
    		return maxIteration - MandelbrotDemo.mandelbrot(z0, maxIteration);
    	}
    	return MandelbrotDemo.mandelbrot(z0, maxIteration) * 255 / maxIteration;
    }
    
    public static Color blackAndWhiteMandelbrot(Complex z0, int maxIteration) {
    	int colorValue = MandelbrotDemo.grayMandelbrotFormula(z0, maxIteration);
        return new Color(colorValue, colorValue, colorValue);
    }
    
    public static Color grayMandelbrot(Complex z0, int maxIteration) {
    	int colorValue = MandelbrotDemo.colorMandelbrotFormula(z0, maxIteration);
        return new Color(colorValue, colorValue, colorValue);
    }
    
    public static Color redMandelbrot(Complex z0, int maxIteration) {
    	int colorValue = MandelbrotDemo.colorMandelbrotFormula(z0, maxIteration);
        return new Color(colorValue, 0, 0);
    }
    
    public static Color greenMandelbrot(Complex z0, int maxIteration) {
    	int colorValue = MandelbrotDemo.colorMandelbrotFormula(z0, maxIteration);
        return new Color(0, colorValue, 0);
    }
    
    public static Color blueMandelbrot(Complex z0, int maxIteration) {
    	int colorValue = MandelbrotDemo.colorMandelbrotFormula(z0, maxIteration);
        return new Color(0, 0, colorValue);
    }
    
    private void computeMandelbrot() {
    	isComputing = true;
    	startButton.setEnabled(false);
    	menu1.setEnabled(false);
    	menu2.setEnabled(false);
    	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    	
    	width  	= canvas.getWidth();
    	height  = canvas.getHeight();
    	
//    	try {
//    		width = (int) renderWidthTextField.getValue();
//    		height = (int) renderHeightTextField.getValue();
//    	} catch(NumberFormatException nfe) {
//    	} catch(NullPointerException npe) {
//    	}
		
    	zoomX 	= width / (x2 - x1);
    	zoomY 	= height / (y2 - y1);
    	
    	renderImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	
    	SwingWorker<Object, Object> sw = new SwingWorker<Object, Object>() {			
			@Override
			protected Object doInBackground() throws Exception {
				long before = Calendar.getInstance().getTimeInMillis();
				ExecutorService executor = Executors.newFixedThreadPool(nbCores);
				
				int sqrNbCores = (int) Math.sqrt(nbCores);
				int stepI, stepJ;
				
				if(sqrNbCores*sqrNbCores == nbCores) {
					stepI = (int) Math.floor(height / (double)sqrNbCores);
					stepJ = (int) Math.floor(width / (double)sqrNbCores);
					
					for(int i = 0; i<sqrNbCores; i++) {
						for(int j = 0; j<sqrNbCores; j++) {
							int startI 	 = stepI * i;
							int endI	 = startI + stepI;
							int startJ 	 = stepJ * j;
							int endJ 	 = startJ + stepJ;
							
							if(j == sqrNbCores-1) {
								endJ = width - 1;
							}
							if(i == sqrNbCores-1) {
								endI = height - 1;
							}
							executor.submit(new RenderThread(instance, startI, endI, startJ, endJ));
						}
					}
				} else if(nbCores % 2 == 0) {
					stepI = (int) Math.floor(height / 2.0);
					stepJ = (int) Math.floor(width / (double)(nbCores/2));
					
					for(int i = 0; i<2; i++) {
						for(int j = 0; j<nbCores/2; j++) {
							int startI 	 = stepI * i;
							int endI	 = startI + stepI;
							int startJ 	 = stepJ * j;
							int endJ 	 = startJ + stepJ;
							
							if(j == nbCores/2 - 1) {
								endJ = width - 1;
							}
							if(i == 1) {
								endI = height - 1;
							}
							executor.submit(new RenderThread(instance, startI, endI, startJ, endJ));
						}
					}
				} else {
					stepI = height;
					stepJ = (int) Math.floor(width / (double)nbCores);
					
					for(int j = 0; j<nbCores; j++) {
						int startJ 	 = stepJ * j;
						int endJ 	 = startJ + stepJ;
						
						if(j == nbCores-1) {
							endJ = width - 1;
						}
						executor.submit(new RenderThread(instance, 0, height, startJ, endJ));
					}
				}
				
				executor.shutdown();
				try {
					executor.awaitTermination(Long.MAX_VALUE,
							TimeUnit.NANOSECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				final double time = (Calendar.getInstance().getTimeInMillis() - before) / 1000.0;
				final DecimalFormat df = new DecimalFormat("#.###");
				SwingUtilities.invokeLater(new Runnable() {					
					@Override
					public void run() {
						infoTextField.setText("nb cores : " + nbCores + "  /  tps traitement : " + df.format(time) + " s");
					}
				});
		        
				return null;
			}
			
			@Override
			protected void done() {
				startButton.setEnabled(true);
		    	menu1.setEnabled(true);
		    	menu2.setEnabled(true);
				isComputing = false;
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		};
    	sw.execute();  	
    }
    
    private class Canvas extends JPanel {    	
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private MyRect currentRect = null;
		private boolean mouseDragged = false;
		
		public Canvas() {
			addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					if(null != renderImage && !isComputing && mouseDragged) {
						renderImage = null;
						
						if(currentRect.isZooming()) {
							int x = e.getX();
				            int y = e.getY();
				            
				        	zoomX 	= width / (x2 - x1);
				        	zoomY 	= height / (y2 - y1);
				            
							x1 = currentRect.getX() / zoomX + x1;
							x2 = x / zoomX + x1;
							y1 = currentRect.getY() / zoomY + y1;
							y2 = y / zoomY + y1;
							
							stackOfZoom.add(new double[]{x1, y1, x2, y2});
							
							computeMandelbrot();
							stackOfZoomImage.add(renderImage);
						} else {
							stackOfZoom.pollLast();
							if(!stackOfZoom.isEmpty()) {
								x1 = stackOfZoom.getLast()[0];
								y1 = stackOfZoom.getLast()[1];
								x2 = stackOfZoom.getLast()[2];
								y2 = stackOfZoom.getLast()[3];
							}
							
							stackOfZoomImage.pollLast();
							if(!stackOfZoomImage.isEmpty()) {
								renderImage = stackOfZoomImage.getLast();
							}							
						}
						
//						System.out.println("x1=" + x1);
//						System.out.println("x2=" + x2);
//						System.out.println("y1=" + y1);
//						System.out.println("y2=" + y2);
					}
					
					currentRect = null;
					mouseDragged = false;
					repaint();
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					int x = e.getX();
		            int y = e.getY();
		            currentRect = new MyRect(x, y);
		            repaint();
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			
			addMouseMotionListener(new MouseMotionListener() {				
				@Override
				public void mouseMoved(MouseEvent e) {					
				}
				
				@Override
				public void mouseDragged(MouseEvent e) {
					mouseDragged = true;
					updateSize(e);
				}
			});
		}
		
		void updateSize(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            currentRect.setSize(x, y);
            repaint();
        }

		@Override
    	public void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		Graphics2D g2 = (Graphics2D) g;
    		
    		if(null != renderImage) {
    			g2.drawImage(renderImage, 0, 0, getWidth(), getHeight(), this);
    		}
    		
    		if(null != currentRect) {
    			g2.drawRect(currentRect.getX(), currentRect.getY(), currentRect.getWidth(), currentRect.getHeight());
    		}
    	}
    }
    

    public static void main(String[] args)  {
        SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				new MandelbrotDemo();
			}
		});
    }
}

