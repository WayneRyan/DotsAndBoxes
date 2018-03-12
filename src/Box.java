import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Box {
	private int count;
	private boolean isRed;
	private int x, y;
	private ArrayList<Dot> myDots;
	
	public Box(int r, int c, ArrayList<Dot> myDots){
		x = (int)((1+c)*MainClass.SPACING);
		y = (int)((1+r)*MainClass.SPACING);
		this.myDots = myDots;
		count=0;
	}
	
	public boolean willClose(Line L){
		if(!myDots.contains(L.a))return false;
		if(!myDots.contains(L.b))return false;
		return count==3;
	}
	
	public int setLine(Line L){
		if(!myDots.contains(L.a))return 0;
		if(!myDots.contains(L.b))return 0;
		count++;
		if(count==4){
			isRed = L.isRed;
			if(isRed)return 1;
			else return 2;
		}
		return 0;
	}
	
	public void draw(Graphics g){
		if(count>=4){
			if(isRed){
				g.setColor(Color.red);
			}else{
				g.setColor(Color.yellow);
			}
			g.fillOval(x, y, 50, 50);
		}
	}

}
