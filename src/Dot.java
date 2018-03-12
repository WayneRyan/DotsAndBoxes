import java.awt.Color;
import java.awt.Graphics;


public class Dot {
	private int x,y;
	public static final int SIZE = 15;
	public Dot(int r, int c){
		x = MainClass.SPACING*(c+1);
		y = MainClass.SPACING*(r+1);
	}
	
	public void draw(Graphics g){
		g.setColor(Color.green);
		g.fillOval(x-SIZE/2, y-SIZE/2, SIZE, SIZE);
	}
	
	public void draw(Graphics g, Color c){
		g.setColor(c);
		g.fillOval(x-SIZE/2, y-SIZE/2, SIZE, SIZE);
	}
	public double distanceTo(int x, int y){
		double dX = x - this.x;
		double dY =y- this.y;
		return Math.sqrt(dX*dX+dY*dY);
	}
	public int getX(){return x;}
	public int getY(){return y;}
}
