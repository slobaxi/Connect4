

import java.util.Random;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetAnimation;
import mars.geometry.Transformation;
import mars.geometry.Vector;
import mars.input.InputEvent;
import mars.input.InputState;


public class Graphic implements Drawing {
	
	double boardY=600;
	double boardX=700;
	
	
	private int selctedRow=0;
	
	@GadgetAnimation(start=true)
	private double animationT=0.0;
	
		
	private boolean mainMenu=true;
	private boolean gameMenu=false;
	
	private Random randomGen = new Random();
	private double[] xRandom = new double[300];
	private double[] yRandom = new double[300]; 
	{
		for (int i=0;i<300;i++) {
			xRandom[i]=(randomGen.nextDouble()*1200-300)*5;
			yRandom[i]=(randomGen.nextDouble()*2200-200)*5;		
		}
	}
	
	
	private boolean karlo=false;
	
	private ConnectFour board = new ConnectFour();
		
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.WHITE);
		if(gameMenu)
			drawGameMenu(view);
		if(mainMenu)
			drawMainMenu(view);
		view.setTransformation(Transformation.translation(new Vector(-300,-300)));
		
	}
	
	public void drawGameMenu(View view) {
		drawTabale(view);
		drawSky(view);
		drawBoard(view);
		drawPedestal(view);
		drawTokens(view);
		if(!board.isOver())
			drawMarker(view);
		drawWinner(view);
	}
	
	public void drawMainMenu(View view) {
		DrawingUtils.clear(view,Color.DEEPSKYBLUE);
		drawRainDrop(view);
		drawSelectionBars(view);
		drawGrass(view);
	}
	
	
	public void drawSelectionBars(View view) {
		view.setFill(Color.ORANGE);
		view.fillRect(new Vector(0-20,boardY-20), new Vector(650,70));
		view.fillRect(new Vector(0-20,boardY-120), new Vector(650,70));
		
		view.fillRect(new Vector(0-20,50), new Vector(650,250));
		
		Font font = new Font("Times New Roman",50);
		view.setFont(font);
		view.setFill(Color.BLACK);
		view.fillText("Press P to play practice mode", new Vector(0,boardY));
		view.fillText("Press K to play against Karlo", new Vector(0,boardY-100));
		view.fillText("Controls:", new Vector(220,250));
		view.fillText("Use arrows to play", new Vector(0,200));
		view.fillText("Press R to restart", new Vector(0,150));
		view.fillText("Press Backspace to return", new Vector(0,100));	
	}
	
	public void drawGrass(View view) {
		view.setFill(Color.GREEN);
		view.fillRect(new Vector(-300,-200),(new Vector(1300,100)));
	}
	public void drawRainDrop(View view) {		
		double k = 0.2;
		double t = (animationT/5-(int)(animationT/5));
		
		Vector p = Vector.lerp(new Vector(0,0),new Vector(0,-1000),t);
		
		for(int i=0;i<300;i++) {
			if(!(yRandom[i]*k+p.y<-80)) {
				view.setFill(Color.DARKBLUE);
				view.beginPath();		
				view.moveTo(new Vector(xRandom[i],yRandom[i]).mul(k).add(p));
				view.bezierCurveTo(new Vector(xRandom[i]+5,yRandom[i]-30).mul(k).add(p), new Vector(xRandom[i]+30,yRandom[i]-30).mul(k).add(p), new Vector(xRandom[i]+35,yRandom[i]-70).mul(k).add(p));
				view.quadraticCurveTo(new Vector(xRandom[i]+30,yRandom[i]-90).mul(k).add(p),new Vector(xRandom[i],yRandom[i]-100).mul(k).add(p));
				view.closePath();
				view.fill();
				view.beginPath();
				view.moveTo(new Vector(xRandom[i],yRandom[i]).mul(k).add(p));
				view.bezierCurveTo(new Vector(xRandom[i]-5,yRandom[i]-30).mul(k).add(p), new Vector(xRandom[i]-30,yRandom[i]-30).mul(k).add(p), new Vector(xRandom[i]-35,yRandom[i]-70).mul(k).add(p));
				view.quadraticCurveTo(new Vector(xRandom[i]-30,yRandom[i]-90).mul(k).add(p),new Vector(xRandom[i],yRandom[i]-100).mul(k).add(p));
				view.closePath();
				view.fill();
			}
		}
	}
	
	//Game menu functions for drawing objects
	
	//(add rain)
	public void drawSky(View view) {
		view.setFill(Color.DEEPSKYBLUE);
		view.fillRect(new Vector(-300,100),new Vector(1200,1500));
	}
		
	//(add tokens on tabale)
	public void drawTabale(View view) {
		view.setFill(new Color(81.0/360.0,63.0/360,4.0/360,1));
		view.fillRect(new Vector(-300,-200),new Vector(1200,boardY/2));
	}
	
	
	public void drawPedestal(View view) {
		view.setFill(new Color(199.0/360.0,156.0/360,39.0/360.0,1));
		view.fillPolygon(new Vector(0,0),new Vector(boardX,0),new Vector(boardX+40,-60),new Vector(-40,-60));
		view.fillPolygon(new Vector(boardX,0),new Vector(boardX+40,-60),new Vector(boardX+20+40,10-60),new Vector(boardX+20,10));
	}
	

	public void drawBoard(View view) {
		view.setFill(new Color(255.0/360.0,204.0/360.0,1.0/360.0,1));
		view.fillRect(new Vector(0,0),new Vector(boardX,boardY));
		view.fillPolygon(new Vector(boardX,0),new Vector(boardX+20,10),new Vector(boardX+20,boardY+10),new Vector(boardX,boardY));
		view.fillPolygon(new Vector(0,boardY),new Vector(20,boardY+10),new Vector(boardX+20,boardY+10),new Vector(boardX,boardY));
		view.setStroke(Color.BLACK);
		for(int i=0;i<7;i++) {
			view.setFill(new Color(240.0/360.0,180.0/360.0,1.0/360.0,1));
			view.fillRect(new Vector(boardX/7*i+10,boardY), new Vector(80,5));
		}
	}
	
	//add better text font 
	public void drawWinner(View view) {
		Font font = new Font("Times New Roman",50);
		view.setFont(font);
		if(board.isOver()) {			
			if(board.getWinner()==-1) {
				view.setFill(new Color(192.0/360.0,3.0/360,3.0/360,1));
				view.fillText("PLAYER 1 WINS", new Vector(boardX/4,boardY+50));
			}
			else if(board.getWinner()==1) {
				view.setFill(new Color(0,36.0/360.0,142.0/360.0,1));
				view.fillText("PLAYER 2 WINS", new Vector(boardX/4,boardY+50));  
			}
			else {
				view.setFill(new Color(234.0/360.0,218/360.0,27/360.0,1));
				view.fillText("DRAW", new Vector(boardX/2,boardY+50));  				
			}
		}
		else {
			view.setFill(Color.BLACK);
			view.fillText(board.getFinalInsult(), new Vector(-220,boardY+50));
		}
		
	}
	
	public void drawMarker(View view) {
		view.setFill(Color.GREENYELLOW);
		view.fillPolygon(new Vector(selctedRow*100+50,630),new Vector(selctedRow*100+80,650),
		new Vector(selctedRow*100+70,650),new Vector(selctedRow*100+70,750),new Vector(selctedRow*100+30,750),
		new Vector(selctedRow*100+30,650),new Vector(selctedRow*100+20,650));	
	}
	
	//add animation on tokens
	public void drawTokens(View view) {
		for(int i=0;i<board.getBoard().length;i++) {
			for(int j=0;j<board.getBoard()[i].length;j++) {
				if(board.getBoard()[i][j]==1) {
					Color blue = new Color(0,36.0/360.0,142.0/360.0,1);
					Color darkBlue = new Color(0,21.0/360.0,91.0/360.0,1);					
					view.setFill(darkBlue);
					view.fillCircleCentered(new Vector(boardX/7*(i)+50,boardY/6*(j)+50),40.0);
					view.setFill(blue);
					
				}
				else if (board.getBoard()[i][j]==-1) {
					Color red = new Color(192.0/360.0,3.0/360,3.0/360,1);
					Color darkRed = new Color(152.0/360.0,0,0,1);					
					view.setFill(darkRed);
					view.fillCircleCentered(new Vector(boardX/7*(i)+50,boardY/6*(j)+50),40.0);
					view.setFill(red);
				}
				else {	
					if(j==0) { 
						view.setFill(new Color(81.0/360.0,63.0/360,4.0/360,1));
					}
					else {
						view.setFill(Color.DEEPSKYBLUE);
					}
					view.fillCircleCentered(new Vector(boardX/7*(i)+50,boardY/6*(j)+50),40.0);	
				}
				view.fillCircleCentered(new Vector(boardX/7*(i)+50,boardY/6*(j)+50),30.0);	
			}
		}
	}
	
	// Keyboard
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		if(event.isKeyPress(KeyCode.DOWN)) {
			if(gameMenu) {												
				if(!board.isOver() && board.getBoard()[selctedRow][5]==0) {
					
					board.play(selctedRow);
					if(karlo && !board.isOver() ) {
						board.play(board.monteCarlo());
					}
				}
			}
		}
		if(event.isKeyPress(KeyCode.LEFT)) {
			if(gameMenu) {
				if(!board.isOver()) {
					selctedRow--;
					if(selctedRow==-1) {
						selctedRow=6;
					}
				}
			}
		}
		if(event.isKeyPress(KeyCode.RIGHT)) {
			if(gameMenu) {
				if(!board.isOver()) {
					selctedRow++;
					if(selctedRow==7) {
						selctedRow=0;
					}
				}
			}
		}
		if(event.isKeyPress(KeyCode.R)) {
			if(gameMenu)
				board= new ConnectFour();
		}
		if(event.isKeyPress(KeyCode.K)) {
			if(mainMenu) {
				gameMenu=true;
				mainMenu=false;
				karlo=true;
			}
		}
		if(event.isKeyPress(KeyCode.P)) {
			if(mainMenu){
				gameMenu=true;
				mainMenu=false;
			}
		}
		if(event.isKeyPress(KeyCode.BACK_SPACE)) {
			if(gameMenu) {
				board= new ConnectFour();
				gameMenu=false;
				mainMenu=true;
			}
		}
	}
	
	public static void main(String[] args) {
		DrawingApplication.launch(1100,950);
	}	
}