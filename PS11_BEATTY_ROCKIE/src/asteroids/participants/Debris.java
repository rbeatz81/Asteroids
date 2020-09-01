package asteroids.participants;


import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class Debris extends Participant
{
    double xLocation;
    double yLocation;
    double direction;
    int swapOn;
    
    public Shape outline;
    Controller c;

    public Debris (Ship sh, Controller controller)
    {
        c = controller;
        // makes it only last a certain amount of time in order to give it its maximum range
        new ParticipantCountdownTimer(this, "limit", 550);
        xLocation = sh.getX();
        yLocation = sh.getY();
        
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(xLocation, yLocation);
//        poly.lineTo(-21, 12);
        poly.lineTo(xLocation-14, yLocation +10);
        poly.closePath();
        
        poly.moveTo(xLocation + 5, yLocation + 5);
        poly.lineTo(xLocation+14, yLocation-10);
        poly.closePath();
        
        poly.moveTo(xLocation -10, yLocation - 10);
        poly.lineTo(xLocation-21, yLocation-12);
        poly.closePath();
        outline = poly;
    }
 

    

    public Debris (AlienShip al, Controller controller)
    {
     // makes it only last a certain amount of time in order to give it its maximum range
        new ParticipantCountdownTimer(this, "limit", 550);
        xLocation = al.getX();
        yLocation = al.getY();
        
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(xLocation, yLocation);
//        poly.lineTo(-21, 12);
        poly.lineTo(xLocation-14, yLocation +10);
        poly.closePath();
        
        poly.moveTo(xLocation, yLocation);
        poly.lineTo(xLocation+14, yLocation-10);
        poly.lineTo(xLocation-21, yLocation-12);
        poly.closePath();
        outline = poly;
        // TODO Auto-generated constructor stub
    }
    public Debris (MiniAlienShip mini, Controller controller)
    {
     // makes it only last a certain amount of time in order to give it its maximum range
        new ParticipantCountdownTimer(this, "limit", 550);
        xLocation = mini.getX();
        yLocation = mini.getY();
        
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(xLocation, yLocation);
//        poly.lineTo(-21, 12);
        poly.lineTo(xLocation-7, yLocation +5);
        poly.closePath();
        
        poly.moveTo(xLocation, yLocation);
        poly.lineTo(xLocation+7, yLocation-5);
        poly.lineTo(xLocation-10, yLocation-6);
        poly.closePath();
        outline = poly;
        
    }
    @Override
    protected Shape getOutline ()
    {
        return outline;

    }

    @Override
    public void collidedWith (Participant p)
    {
        // TODO Auto-generated method stub

    }

    // delay demise
    public void countdownComplete (Object p)
    {

        Participant.expire(this);

    }

}
