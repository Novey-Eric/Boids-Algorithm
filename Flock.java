import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;
import javax.swing.Timer;


public class Flock extends JComponent implements ActionListener {


	//cool presets radius 90, avoidRad 25, maxSpeed 5, avoidW .3, coaw .018, comw .002

	private static final long serialVersionUID = 1L;
	//CONSTANTS==========================
	private float size= 10;
	private int numBirds=300;
	private int numOwls=2;

	private int owlRad=120;
	private int radius=90;
	private int avoidRad=14;

	private double maxSpeed = 5;
	private double maxOwlSpeed=4;

	private double avoidOwlsW=1;
	private double avoidW=.3;
	private double wallW=10;
	private double COAW=.018;
	private double COMW=.002;
	//=================================


	private Bird[] owls;
	private Bird[] birds;
	//private int time=0;
	private Timer timer;
	private boolean GUI;


	Flock(boolean GUI){
		this.GUI=GUI;
		birds = new Bird[numBirds];
		owls =new Owl[numOwls];

		for(int i = 0; i<numBirds; i++) {
			birds[i]=new Bird(Math.random()*10-5, Math.random()*10-5, new Point((int)(Math.random()*(600)+100), (int)(Math.random()*(600)+100)));
		}

		for(int i = 0; i<numOwls; i++) {
			owls[i]=new Owl(Math.random()*10-5, Math.random()*10-5, new Point((int)(Math.random()*(600)+100), (int)(Math.random()*(600)+100)));
		}

		//birds[0]=new Bird(1, 0, new Point(100,100));
		//birds[1]=new Bird(1, 0, new Point(100,200));
		//birds[2]=new Bird(1, 0, new Point(150,150));
		timer = new Timer(15, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {

		for(Bird b : birds) {
			//System.out.println(b.pos);
			Graphics2D g2 = (Graphics2D) g;
			//variable for how oval the bird is
			float stretch=2;

			Shape oval = new Ellipse2D.Float((float) (b.posX-(size/2)),(float) (b.posY-stretch*(size/2)), size, (size*stretch));
			double theta = Math.PI/2+b.angle;
			g2.rotate(theta, b.posX, b.posY);
			g2.setColor(Color.BLUE);
			g2.fill(oval);
			g2.draw(oval);
			g2.rotate(-theta, b.posX, b.posY);

			//circle on center of boid
			//g.setColor(Color.blue);
			//g.fillOval(b.pos.x, b.pos.y, 2, 2);
		}

		for(Bird o : owls) {
			//System.out.println(b.pos);
			Graphics2D g2 = (Graphics2D) g;
			//variable for how oval the bird is
			float stretch=2;

			Shape oval = new Ellipse2D.Float((float) (o.posX-(size/2)),(float) (o.posY-stretch*(size/2)), size, (size*stretch));
			double theta = Math.PI/2+o.angle;
			g2.rotate(theta, o.posX, o.posY);
			g2.setColor(Color.RED);
			g2.fill(oval);
			g2.draw(oval);
			g2.rotate(-theta, o.posX, o.posY);

			//circle on center of boid
			//g.setColor(Color.blue);
			//g.fillOval(o.pos.x, o.pos.y, 2, 2);



		}

	}

















	@Override
	public void actionPerformed(ActionEvent e) {
		//checks the speed of a bird
		/*
		time++;
		if(time%100==0) {
			double totalSpeed=0;
			Bird b = birds[0];
			totalSpeed+=Math.sqrt(Math.pow(b.xSpeed, 2)+Math.pow(b.ySpeed, 2));
			//System.out.println(totalSpeed);
		}
		 */

		if(GUI) {
			radius=SetupFrame.rad.getValue();
			avoidRad=SetupFrame.avoidRad.getValue();
			maxSpeed=SetupFrame.maxSpeed.getValue();
			avoidW=(double)SetupFrame.avoidW.getValue()/100;
			COAW=(double)SetupFrame.COAW.getValue()/1000;
			COMW=(double)SetupFrame.COMW.getValue()/1000;
		}

		for(Bird b : birds) {
			Bird[] near = b.findNearBirds(avoidRad);
			b.updateAvoid(near);
			near = b.findNearBirds(radius);
			b.updateCOM(near);
			b.updateCOA(near);
			//b.checkWallsRef();
			b.checkWallsBounce();
			b.overClock(maxSpeed);

			b.updateAng();
			b.updatePos();

			Bird[] nearO = b.getNearOwls();
			b.updateAvoidOwls(nearO);
		}


		for(Bird b : owls) {
			Bird[] near = b.findNearBirds(owlRad);
			b.updateCOM(near);
			//b.checkWallsRef();
			b.checkWallsBounce();
			b.overClock(maxOwlSpeed);

			b.updateAng();
			b.updatePos();

		}


		repaint();

	}


	public class Owl extends Bird{
		Owl(double initSpeedx, double initSpeedy, Point initPos){
			super(initSpeedx, initSpeedy, initPos);
		}
	}



	public class Bird {

		private void updateAvoidOwls(Bird[] nearO){
			int nearOwls = nearO.length;
			if(nearOwls>0) {

				double locX=0;
				double locY=0;
				for(Bird d : nearO) {
					locX+=d.posX;
					locY+=d.posY;
				}
				locX/=nearOwls;
				locY/=nearOwls;

				//draw vector from local COM to bird and add that to the birds speed
				double xDiff = posX-locX;
				double yDiff = posY-locY;

				double distCOM = Math.sqrt(Math.pow(xDiff, 2)+Math.pow(yDiff, 2));


				//inverse square
				//b.xSpeed+=avoidW*Math.pow((1/distCOM),2)*xDiff;
				//b.ySpeed+=avoidW*Math.pow((1/distCOM),2)*yDiff;
				xSpeed+=avoidOwlsW*1/distCOM*xDiff;
				ySpeed+=avoidOwlsW*1/distCOM*yDiff;

			}
		}


		private Bird[] getNearOwls() {
			Bird[] tempAns = new Owl[owls.length];
			int nearOwls=0;
			for(int i = 0; i<owls.length; i++) {
				Bird o = owls[i];
				double[] diffVec = {posX-o.posX, posY-o.posY};
				double dist = Math.sqrt(Math.pow(diffVec[0],2)+Math.pow(diffVec[1], 2));
				if(Math.abs(dist)<radius) {
					tempAns[i]=o;
					nearOwls++;	
				}
			}
			Bird[] ans = new Owl[nearOwls];
			int missing=0;
			for(int i = 0; i<numOwls; i++) {
				if(tempAns[i]!=null) {
					ans[i-missing]=tempAns[i];
				}else
					missing++;
			}
			return ans;
		}


		private Bird[] findNearBirds(int radius){
			int nearBirds=0;
			Bird[] birdPosNear= new Bird[numBirds];

			for(int i = 0; i<numBirds; i++) {

				Bird otherB=birds[i];
				if(!otherB.equals(this)) {
					double[] diffVec = {posX-otherB.posX, posY-otherB.posY};
					double dist = Math.sqrt(Math.pow(diffVec[0],2)+Math.pow(diffVec[1], 2));
					if(Math.abs(dist)<radius) {
						birdPosNear[i]=otherB;
						nearBirds++;
					}
				}
			}//end bird loop

			Bird[] ans = new Bird[nearBirds];
			int missing=0;
			for(int i = 0; i<numBirds; i++) {
				if(birdPosNear[i]!=null) {
					ans[i-missing]=birdPosNear[i];
				}else
					missing++;
			}
			return ans;
		}




		//Center of mass
		//make a vector from the birds pos to COM,
		//add x and y comps to b speed
		//multiply by weight
		private void updateCOM(Bird[] near) {
			if(near.length>0) {
				double avgX=0;
				double avgY=0;
				for(Bird one : near) {
					avgX+=one.posX;
					avgY+=one.posY;
				}
				avgX/=(near.length);
				avgY/=(near.length);


				double BtCVX =avgX- posX;
				double BtCVY =avgY- posY;
				xSpeed+=COMW*BtCVX;
				ySpeed+=COMW*BtCVY;
			}
		}

		//center of angle
		private void updateCOA(Bird[] near) {
			if(near.length>0) {

				double avgXS=0;
				double avgYS=0;
				for(Bird d : near) {
					avgXS+=d.xSpeed;
					avgYS+=d.ySpeed;

				}
				avgXS/=near.length;
				avgYS/=near.length;

				double xDiff= xSpeed-avgXS;
				double yDiff= ySpeed-avgYS;


				xSpeed-=COAW*xDiff;
				ySpeed-=COAW*yDiff;
			}
		}






		private void updateAng(){
			angle=Math.atan(ySpeed/xSpeed);
			//System.out.println("x: "+b.pos.x+" y: "+b.pos.y);
		}

		private void updatePos() {
			posX+=xSpeed;
			posY+=ySpeed;
		}



		private void checkWallsBounce() {
			if( posX>750) {
				xSpeed-=wallW*1/(800- posX);

				// posX=799;
				// xSpeed=- xSpeed;
			}else if( posX<50) {
				xSpeed+=wallW*1/( posX);

				// posX=1;
				// xSpeed=- xSpeed;
			}

			if( posY>750) {
				ySpeed-=wallW*1/(800- posY);

				// posY=799;
				// ySpeed=- ySpeed;
			}else if( posY<50) {
				ySpeed+=wallW*1/( posY);

				// posY=1;
				// ySpeed=- ySpeed;
			}

		}



/*
		private void checkWallsRef() {
			if( posX>800) {
				// xSpeed-=wallW*1/(800- posX);

				posX=799;
				xSpeed=- xSpeed;
			}else if( posX<0) {
				// xSpeed+=wallW*1/( posX);

				posX=1;
				xSpeed=- xSpeed;
			}

			if( posY>800) {
				// ySpeed-=wallW*1/(800- posY);

				posY=799;
				ySpeed=- ySpeed;
			}else if( posY<0) {
				// ySpeed+=wallW*1/( posY);

				posY=1;
				ySpeed=- ySpeed;
			}
		}

*/

		private void overClock(double maxSpeed) {
			double speed = Math.sqrt(Math.pow(xSpeed, 2)+Math.pow(ySpeed, 2));

			if(speed>maxSpeed) {
				double ratio=maxSpeed/speed;
				xSpeed*=ratio;
				ySpeed*=ratio;
			}
		}


		//will return the appropriate angle change for a bird after looking at birds within a radius
		private void updateAvoid(Bird[] birdPosNear) {
			int nearBirds = birdPosNear.length;

			if(nearBirds>0) {

				//get the local COM for the other birds
				double locX=0;
				double locY=0;
				for(Bird d : birdPosNear) {
					locX+=d.posX;
					locY+=d.posY;
				}

				locX/=nearBirds;
				locY/=nearBirds;

				//draw vector from local COM to bird and add that to the birds speed
				double xDiff =  posX-locX;
				double yDiff =  posY-locY;

				double distCOM = Math.sqrt(Math.pow(xDiff, 2)+Math.pow(yDiff, 2));


				//inverse square
				// xSpeed+=avoidW*Math.pow((1/distCOM),2)*xDiff;
				// ySpeed+=avoidW*Math.pow((1/distCOM),2)*yDiff;
				xSpeed+=avoidW*1/distCOM*xDiff;
				ySpeed+=avoidW*1/distCOM*yDiff;

			}

		}

		private double xSpeed;
		private double ySpeed;
		private double posX;
		private double posY;
		private double angle;

		Bird(double initSpeedx, double initSpeedy, Point initPos){
			xSpeed= initSpeedx;
			ySpeed= initSpeedy;
			angle= 0;
			posX=initPos.x;
			posY=initPos.y;
		}

	}

}
