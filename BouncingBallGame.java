import acm.program.*;
import acm.graphics.*;
import java.awt.*;

/**
 * BouncingBallGame.java <p>
 * 
 * Create a ball that bounces inside the window. <p>
 * 
 * @author ...
 */
public class BouncingBallGame extends GraphicsProgram {
    // initial size of the window
    public static int
    APPLICATION_WIDTH = 600,
    APPLICATION_HEIGHT = 600;
    
    // constants
    private static final double
    SIZE = 50,
    DELAY = 30; // delay between two consecutive frames

    // instance variables 
    private GOval bouncingBall;
    private GLabel label;
    private double width, height; // width, height of the window
    private double // initial speed and angle of the bouncing ball
    speed = 20,
    angle = 65;

    private int gamescore = 0; // instant variable for game score 
    private GOval centerBall; // ball in the center of window
    boolean gameOver = false;

    /** the run method, control the animation */
    public void run() {
        drawGraphics(); // draw the inital graphics
        addMouseListeners();
        waitForClick(); // wait for a mouse click to start the animation
        label.setLabel("SCORE: 0" );
        double y = bouncingBall.getY();

        // use a while-loop to control the animation
        while (gameOver == false) {
            oneTimeStep(); // do the work in a one time step
            pause(DELAY);  
            double labelx = label.getWidth()/2; //used to position the label 
            if(bouncingBall.getY()> APPLICATION_HEIGHT){
                gameOver = true;
                label.setLabel("Game Over. Your final score is " + gamescore);
                label.setColor(Color.RED);
                label.move(labelx-label.getWidth()/2, label.getHeight());
                break;
            }
        }
    }

    // initially draw the background, a ball and a label
    private void drawGraphics() {
        width = getWidth(); // width of the window
        height = getHeight(); // height of the window
        
        add(new GRect(0,0,width-1,height- 1)); // draw the boundary

        // draw a label
        label = new GLabel("Click to start.", width/2, 40);
        label.setFont(new Font("Sanserif", Font.BOLD, 20));
        label.move(-label.getWidth()/2, label.getHeight());
        add(label);

        // draw a bouncing ball
        bouncingBall = new GOval(width/2-SIZE/2, height/3-SIZE/2, SIZE, SIZE);
        add(bouncingBall);
        bouncingBall.setFilled(true);
        bouncingBall.setFillColor(Color.GREEN);

        // draw a ball in the center of window
        centerBall = new GOval(width/2-SIZE, height-SIZE, 
            SIZE*2, SIZE*2);
        centerBall.setFilled(true);
        centerBall.setFillColor(Color.BLUE);
        add(centerBall);
    }

    // in each time step, move the ball and check for collision
    private void oneTimeStep() {
        bouncingBall.movePolar(speed, angle); // move the ball
        checkCollision(); // check for collision
    }

    // check for collision 
    private void checkCollision() {
        /* check for collision between the bouncing ball and the boundary */
        // first, calculate the x, y-coodinates of the bouncing ball
        double x = bouncingBall.getX(); 
        double y = bouncingBall.getY();

        // if the ball hits the left wall, 
        if (x < 0 ) {
            bouncingBall.setLocation(1,y); //moves ball to left edge to avoid it being stuck
            angle = 180-angle; // reflect angle along the y-axis
        }

        // if the ball hits the right wall, 
        if (x+SIZE > width) {
            bouncingBall.setLocation(width-SIZE,y); //moves ball to right edge to avoid it being stuck
            angle = 180-angle; // reflect angle along the y-axis
        }

        // if the ball hits the top or the bottom wall, 
        if (y < 0 ) { 
            bouncingBall.setLocation(x,0);//moves ball to the top edge to avoid it being stuck
            angle = -angle; // reflect angle along the x-axis
        }

        /* check for collision between two balls */
        // first calculate the distance between two balls
        double dist = getDistance(centerBall, bouncingBall);
        // amount of overlap between two balls
        double overlap = SIZE/2+SIZE - dist; 

        if (overlap > 0) { // two balls collide
            // angle from center of centerBall to center of bouncingBall
            double angle = getAngle(centerBall, bouncingBall);
            // move bouncingBall away to so that they just touch each other
            bouncingBall.movePolar(overlap, angle);
            // new angle of bouncingBall
            //this.angle = angle;
            // a more realistic way to calculate the angle
            this.angle = 2*(angle+90)-this.angle;
            speed = speed* 1.03;
            gamescore = gamescore + 1; //increase game score on every collision 
            label.setLabel("SCORE:" + gamescore); //make label reflect new score every time score changes 

        }
    }

    // distance between centers of two balls
    private double getDistance(GOval ball1, GOval ball2) {
        return GMath.distance(ball1.getX()+ball1.getWidth()/2, 
            ball1.getY()+ball1.getWidth()/2,
            ball2.getX()+ball2.getWidth()/2, 
            ball2.getY()+ball2.getWidth()/2);
    }

    // angle from center of ball1 to center of ball2
    private double getAngle(GOval ball1, GOval ball2) {
        return GMath.angle(ball1.getX()+ball1.getWidth()/2, 
            ball1.getY()+ball1.getWidth()/2,
            ball2.getX()+ball2.getWidth()/2, 
            ball2.getY()+ball2.getWidth()/2);
    }

    //move the bigger ball with the mouse pointer while game is not over 
    public void mouseMoved(GPoint point){
        if (gameOver == false){
            centerBall.setLocation(point.getX()-centerBall.getWidth()/2, height-SIZE-2);
        }          
    }
}