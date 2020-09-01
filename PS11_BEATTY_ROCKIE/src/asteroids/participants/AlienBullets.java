package asteroids.participants;

import static asteroids.game.Constants.BULLET_DURATION;
import static asteroids.game.Constants.RANDOM;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class AlienBullets extends Participant implements ShipDestroyer, AsteroidDestroyer
{
//    private static final double BULLET_SPEED = 15;
    double xLocation;
    double yLocation;
    double direction;
    Controller c;

    public AlienBullets (AlienShip p, Controller controller) 
        {
            c = controller;
          //makes it only last a certain amount of time in order to give it its maximum range
            new ParticipantCountdownTimer(this,  "limit", BULLET_DURATION);
            xLocation = p.getX();
            yLocation = p.getY();
            
            int num = RANDOM.nextInt(20);
            this.setVelocity(10, Math.PI * num);
            
//            this.setDirection(p.getRotation()); 
//            this.setRotation(p.getRotation());     
//            this.setVelocity(BULLET_SPEED, p.getRotation());
            
            
            
            
            
        }
    public AlienBullets (MiniAlienShip mini, Ship sh, Controller controller) 
    {
        c = controller;
      //makes it only last a certain amount of time in order to give it its maximum range
        new ParticipantCountdownTimer(this,  "limit", 800);
        xLocation = mini.getX();
        yLocation = mini.getY();
        
        int num = RANDOM.nextInt(20);
        
//        this.setVelocity(20, Math.PI * num);
        this.setVelocity(20, sh.getDirection());
        

        
    }

    @Override
    protected Shape getOutline ()
    {
        return new Ellipse2D.Double(xLocation, yLocation, 5,5);
    }

    @Override
    public void collidedWith (Participant p)
    {
        // TODO Auto-generated method stub
        
    }
    //delay demise
    public void countdownComplete (Object p)
    {
        
        Participant.expire(this);
        c.myBulletDestroyed();
    }
}
