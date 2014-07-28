package ui;

import java.awt.Color;

import maths.Complex;
import maths.Mandelbrot;


/**
 * 
 * @author Catree
 *
 */
public class RenderThread implements Runnable {

	private MandelbrotDemo mandelbrot;
	private int startI;
	private int endI;
	private int startJ;
	private int endJ;
	private boolean isBenchmarking;
	
	
	public RenderThread(MandelbrotDemo mandelbrot, int startI, int endI, int startJ, int endJ, boolean isBenchmarking) {
		this.mandelbrot = mandelbrot;
		this.startI = startI;
		this.endI = endI;
		this.startJ = startJ;
		this.endJ = endJ;
		this.isBenchmarking = isBenchmarking;
	}

	@Override
	public void run() {
		for (int i = startI; i < endI; i++) {
            for (int j = startJ; j <endJ; j++) {
                double x0 = j / mandelbrot.zoomX + mandelbrot.x1;
                double y0 = i / mandelbrot.zoomY + mandelbrot.y1;
                
                Complex z0 = new Complex(x0, y0);
                Color color = new Color(0, 0, 0);
                switch(mandelbrot.colorMethod) {
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
                	color = Mandelbrot.greenMandelbrot(z0, mandelbrot.maxIteration, mandelbrot.color);
                	break;
                case 4:
                	color = Mandelbrot.blueMandelbrot(z0, mandelbrot.maxIteration, mandelbrot.color);
                	break;
            	default:
            		color = Mandelbrot.blackAndWhiteMandelbrot(z0, mandelbrot.maxIteration);
            		break;
                }
                
                if(!isBenchmarking && mandelbrot.isLiveRendering) {
                    synchronized (mandelbrot.lock) {
                    	mandelbrot.renderImage.setRGB(j, i, color.getRGB());
    				}
                } else {
                	mandelbrot.imageArray[i * mandelbrot.width + j] = color.getRGB();
                }
            }
            
            if(!isBenchmarking && mandelbrot.isLiveRendering) {
            	mandelbrot.repaint();
            }            
        }
	}

}
