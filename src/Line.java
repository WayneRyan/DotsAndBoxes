import java.awt.Color;
import java.awt.Graphics;


public class Line {
	public Dot a,b;
	boolean isRed;
	
	public Line(Dot a, Dot b, boolean owner){
		this.a = a;
		this.b = b;
		isRed = owner;
	}
	
	public void draw(Graphics g){
		if(isRed)g.setColor(Color.red);
		else g.setColor(Color.yellow);
		g.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
	}
	
	@ Override
	public boolean equals(Object input){
		System.out.println("Checking equal");
		if(input instanceof Line){
			Line other = (Line)input;
			if(other.a==this.a && other.b==this.b)return true;
			if(other.b==this.a && other.a==this.b)return true;
		}
		return false;
	}
	public int length(){
		return (int)a.distanceTo(b.getX(), b.getY());
	}
	
	@ Override
	public int hashCode(){
		return a.getX()+a.getY()+b.getX()+b.getY();
	}
	
	
}
