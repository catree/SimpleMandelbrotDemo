package ui;

import java.awt.Color;

import maths.Complex;


public class RenderThread implements Runnable {

	private MandelbrotDemo mandelbrot;
	private int startI;
	private int endI;
	private int startJ;
	private int endJ;
	
	
	public RenderThread(MandelbrotDemo mandelbrot, int startI, int endI, int startJ, int endJ) {
		this.mandelbrot = mandelbrot;
		this.startI 	= startI;
		this.endI 		= endI;
		this.startJ 	= startJ;
		this.endJ 		= endJ;
	}

	@Override
	public void run() {
		for (int i = startI; i < endI; i++) {
            for (int j = startJ; j <endJ; j++) {
                double x0 = j / mandelbrot.zoomX + mandelbrot.x1;
                double y0 = i / mandelbrot.zoomY + mandelbrot.y1;
                
                Complex z0 = new Complex(x0, y0);
                Color color = MandelbrotDemo.redMandelbrot(z0, mandelbrot.maxIteration);
                
                synchronized (mandelbrot.lock) {
                	mandelbrot.renderImage.setRGB(j, /*mandelbrot.renderImage.getHeight() -*/ i, color.getRGB());
				}                
            }
            mandelbrot.repaint();
        }
	}

}
