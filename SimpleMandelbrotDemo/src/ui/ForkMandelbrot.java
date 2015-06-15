package ui;

import java.awt.Color;
import java.util.concurrent.RecursiveAction;

import maths.Complex;
import maths.Mandelbrot;


/**
 * 
 * @author Catree
 * @see http://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html
 * @see http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/essential/concurrency/examples/ForkBlur.java
 *
 */
public class ForkMandelbrot extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MandelbrotDemo mandelbrot;
	private int start;
	private int length;
	private boolean isBenchmarking;
	
	
	public ForkMandelbrot(MandelbrotDemo mandelbrot, int start, int length, boolean isBenchmarking) {
		this.mandelbrot = mandelbrot;
		this.start = start;
		this.length = length;
		this.isBenchmarking = isBenchmarking;
	}
	
	protected void computeDirectly() {
		for (int cpt = start; cpt < start + length; cpt++) {
			int i = (int) (cpt / (double) mandelbrot.width);
			int j = cpt % mandelbrot.width;
    	   
			double x0 = j / mandelbrot.zoomX + mandelbrot.x1;
			double y0 = i / mandelbrot.zoomY + mandelbrot.y1;

			Complex z0 = new Complex(x0, y0);
			Color color = new Color(0, 0, 0);
			switch (mandelbrot.colorMethod) {
			case 0:
				color = Mandelbrot.blackAndWhiteMandelbrot(z0, mandelbrot.maxIteration);
				break;
			case 1:
				color = Mandelbrot.greyMandelbrot(z0, mandelbrot.maxIteration);
				break;
			case 2:
				color = Mandelbrot.redMandelbrot(z0, mandelbrot.maxIteration, mandelbrot.color);
				break;
			case 3:
				color = Mandelbrot.greenMandelbrot(z0, mandelbrot.maxIteration,	mandelbrot.color);
				break;
			case 4:
				color = Mandelbrot.blueMandelbrot(z0, mandelbrot.maxIteration, mandelbrot.color);
				break;
			default:
				color = Mandelbrot.blackAndWhiteMandelbrot(z0, mandelbrot.maxIteration);
				break;
			}

			if (!isBenchmarking && mandelbrot.isLiveRendering) {
				mandelbrot.renderImage.setRGB(j, i, color.getRGB());
				mandelbrot.repaint();
			} else {
				mandelbrot.imageArray[i * mandelbrot.width + j] = color.getRGB();
			}
		}
	}

	@Override
	protected void compute() {
		if (length < mandelbrot.threshold) {
            computeDirectly();
            return;
        }
 
        int split = length / 2;
 
        invokeAll(new ForkMandelbrot(mandelbrot, start, split, isBenchmarking),
        		new ForkMandelbrot(mandelbrot, start + split, length - split, isBenchmarking));
	}
}
