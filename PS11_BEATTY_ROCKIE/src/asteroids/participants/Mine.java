package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.MineDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class Mine extends Participant implements AsteroidDestroyer, ShipDestroyer

{
    public Mine () 
    {
        //makes it only last a certain amount of time 
        new ParticipantCountdownTimer(this,  "limit", 1000);
    }


    @Override
    protected Shape getOutline ()
    {
        //initial upper left corner and radius of 25 a circle.
        return new Ellipse2D.Double(50, 50, 25,25);
        
        
      
    }

    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof MineDestroyer) 
        {
            //live for a second before it goes away
            new ParticipantCountdownTimer(this, "explode", 1000);
            
        }
        
    }
    
    
    //delay demise
    public void countdownComplete (Object p)
    {
        Participant.expire(this);
    }

}
