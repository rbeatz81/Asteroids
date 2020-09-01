package asteroids.participants;

import java.awt.Shape;
import static asteroids.game.Constants.*;
import java.awt.geom.Ellipse2D;

import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.MyBulletDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import asteroids.game.Constants.*;


public class MyBullets extends Participant implements AsteroidDestroyer, AlienDestroyer
{
    private static final double BULLET_SPEED = 15;
    double xLocation;
    double yLocation;
    double direction;
    Controller c;
    
    public MyBullets (Ship p, Controller controller) 
    {
        c = controller;
      //makes it only last a certain amount of time in order to give it its maximum range
        new ParticipantCountdownTimer(this,  "limit", BULLET_DURATION);
        xLocation = p.getXNose();
        yLocation = p.getYNose();
        this.setDirection(p.getRotation()); 
//        this.setRotation(p.getRotation());     
        this.setVelocity(BULLET_SPEED, p.getRotation());
        
        
        
        
        
    }


    @Override
    protected Shape getOutline ()
    {
        return new Ellipse2D.Double(xLocation, yLocation, 3,3);
    }

    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof MyBulletDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);

            // Tell the controller the ship was destroyed
            c.myBulletDestroyed();
        }
        // TODO Auto-generated method stub
        
    }
   
    //delay demise
    public void countdownComplete (Object p)
    {
        
        Participant.expire(this);
        c.myBulletDestroyed();
    }
   
}
