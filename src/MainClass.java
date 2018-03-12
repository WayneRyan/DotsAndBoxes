import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JFrame;


public class MainClass extends JFrame implements Runnable,MouseListener, MouseMotionListener{
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = WIDTH;
	public static final int GRID_SIZE = 6;
	public static final int SPACING = WIDTH/(GRID_SIZE+1);
	
	private BufferedImage offScreen;
	private Graphics bg;
	private Dot press,dragg;
	private HashSet<Line> allLines;
	private int redScore, yellowScore;
	
	Dot[][] grid;
	ArrayList<Box> allBoxes;
	private boolean redsTurn; // true if red's turn false if blue's turn;
	
	ArrayList<Line> possibleMoves;
	
	
	public MainClass(){
		redScore = yellowScore = 0;
		redsTurn = true;
		allLines = new HashSet<Line>();
		grid = new Dot[GRID_SIZE][GRID_SIZE];
		for(int r=0 ; r<GRID_SIZE ; r++){
			for(int c=0 ; c<GRID_SIZE ; c++){
				grid[r][c] = new Dot(r,c);
				
			}
		}
		
		
		possibleMoves = new ArrayList<Line>();
		allBoxes = new ArrayList<Box>();
		for(int r=0 ; r<GRID_SIZE-1 ; r++){
			for(int c=0 ; c<GRID_SIZE-1 ; c++){
				ArrayList<Dot> dots = new ArrayList<Dot>();
				dots.add(grid[r][c]);
				dots.add(grid[r+1][c]);
				dots.add(grid[r][c+1]);
				dots.add(grid[r+1][c+1]);
				allBoxes.add( new Box(r,c,dots));
				possibleMoves.add(new Line(grid[r][c],grid[r+1][c],false));
				possibleMoves.add(new Line(grid[r][c],grid[r][c+1],false));
			}
		}
		for(int i=0 ; i<GRID_SIZE-1 ; i++){
			possibleMoves.add(new Line(grid[GRID_SIZE-1][i],grid[GRID_SIZE-1][i+1],false));
			possibleMoves.add(new Line(grid[i][GRID_SIZE-1],grid[i+1][GRID_SIZE-1],false));
		}
		offScreen = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		bg = offScreen.getGraphics();
		Font f = bg.getFont().deriveFont(30f);
		bg.setFont(f);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		new Thread(this).start();
	}
	
	private Dot nearest(int x, int y){
		Dot retVal = grid[0][0];
		double dist = retVal.distanceTo(x, y);
		for(Dot[] row : grid){
			for(Dot d : row){
				double temp = d.distanceTo(x, y);
				if(temp<dist){
					dist = temp;
					retVal = d;
				}
			}
		}
		return retVal;
	}
	
	public static void main(String[] args) {
		MainClass mc = new MainClass();
		mc.setSize(WIDTH, HEIGHT);
		mc.setResizable(false);
		mc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mc.setVisible(true);
	}
	
	public void paint(Graphics g){
		bg.setColor(Color.black);
		bg.fillRect(0, 0, WIDTH, HEIGHT);
		
		bg.setColor(Color.red);
		bg.drawString(""+redScore, 10, 70);
		bg.setColor(Color.yellow);
		bg.drawString(""+yellowScore, WIDTH-65, 70);
		for(Dot[] row : grid){
			for(Dot d : row) d.draw(bg);
		}
		for(Box b : allBoxes)b.draw(bg);
		for(Line L : allLines)L.draw(bg);
		if(press!=null && dragg!=null && 
		   press.distanceTo(dragg.getX(), dragg.getY())==SPACING){
			bg.setColor(Color.green);
			bg.drawLine(press.getX(), press.getY(), dragg.getX(), dragg.getY());
		}
		if(press!=null)press.draw(bg, Color.red);
		
		g.drawImage(offScreen, 0, 0, null);
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		dragg = nearest(e.getX(),e.getY());
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		press = nearest(e.getX(),e.getY());
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(press==dragg || dragg==null){
			press = null;
			dragg = null;
			return;
		}
		Line l = new Line(press,dragg,redsTurn);
		press = null;
		dragg = null;
		if(l.length()>SPACING)return;
		if(allLines.contains(l))return;
		
		boolean didClose = false;
		for(Box b : allBoxes){
			int test = b.setLine(l);
			if(test!=0)didClose=true;
			if(test==1)redScore++;
			if(test==2)yellowScore++;
		}
	    allLines.add(l);
		if(!didClose)redsTurn = !redsTurn;
		
		while (!redsTurn)  computerMove();
		
	}

	private void computerMove() {
		// find all pairs of dots that are not connected
		// if it is possible to close a box close it
		// if it is possible to prevent red from closing do that
		// otherwise choose one at random
		int index = (int)(Math.random()*possibleMoves.size());
		Line l = possibleMoves.get(index);
		while(true){
			while(allLines.contains(l)){
				possibleMoves.remove(l);
				index = (int)(Math.random()*possibleMoves.size());
				l = possibleMoves.get(index);
			}
			double chance = Math.random();
			boolean closes = false;
			for(Box b : allBoxes){
				if(b.willClose(l))closes = true;
			}
			if(chance<0.001 || closes)break;
			index = (int)(Math.random()*possibleMoves.size());
			l = possibleMoves.get(index);
		}
		boolean didClose = false;
		for(Box b : allBoxes){
			int test = b.setLine(l);
			if(test!=0)didClose=true;
			if(test==1)redScore++;
			if(test==2)yellowScore++;
		}
	    allLines.add(l);
		if(!didClose)redsTurn = !redsTurn;
		
		
	}

}
