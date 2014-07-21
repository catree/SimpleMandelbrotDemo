package ui;

public class MyRect {
	
	private int x1 = -1;
	private int x2 = -1;
	private int y1 = -1;
	private int y2 = -1;
	
	
	public MyRect(int x, int y) {
		x1 = x;
		y1 = y;
	}
	
	public MyRect(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void setSize(int x, int y) {
		x2 = x;
		y2 = y;
	}
	
	public int getX() {
		return Math.min(x1, x2);
	}
	
	public int getY() {
		return Math.min(y1, y2);
	}
	
	public int getWidth() {
		if(x2 != -1) {
			return Math.abs(x2 - x1);
		}
		return 0;
	}

	public int getHeight() {
		if(y2 != -1) {
			return Math.abs(y2 - y1);
		}
		return 0;
	}
	
	public boolean isZooming() {
		return (x1 < x2);
	}
}
