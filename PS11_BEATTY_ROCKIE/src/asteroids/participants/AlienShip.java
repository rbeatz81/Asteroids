package asteroids.participants;

import static asteroids.game.Constants.BULLET_DURATION;
import static asteroids.game.Constants.END_DELAY;
import static asteroids.game.Constants.RANDOM;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.MyBulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class AlienShip extends Participant implements ShipDestroyer, AsteroidDestroyer, MyBulletDestroyer
{
    /** The outline of the ship */
    private Shape outline;
    private double direction;
    
    /** Game controller */
    private Controller controller;
    
    public AlienShip (int x, int  y, double direction, Controller controller)
    {
        //going to be used to fire a bullet
       
//        new ParticipantCountdownTimer(this,  "limit", BULLET_DURATION);
      //makes it only last a certain amount of time 
//        new ParticipantCountdownTimer(this,  "limit", 1500);
        
        this.direction = direction;

        this.controller = controller;

        setPosition(750, 550);
        setRotation(direction);
        setVelocity(3, direction);

        Path2D.Double poly = new Path2D.Double();
        //top hat
        poly.moveTo(21, -12);
        poly.lineTo(21, 12);
        
        poly.lineTo(16, 12);
        poly.lineTo(16, -12);
        
        //middle section
        poly.lineTo(8, -20);
        poly.lineTo(8, 20);
        
        poly.lineTo(17, 12);
        poly.lineTo(21, 12);
        poly.closePath();
        
        //bottom belly
        poly.moveTo(9, 20);
        poly.lineTo(1, 14);
        
        poly.lineTo(1, -14);
        poly.lineTo(9, -20);
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
        if (p instanceof AlienDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);

            // Tell the controller the ship was destroyed
            controller.AliensDestroyed(this);
        }
        
    }
    /**
     * Set the aliens autopilot
     */
    public void AlienAutoPilot ()
    {
        
        
        int num = RANDOM.nextInt(3);
        
        switch (num) {
        
        case 0:
            this.setVelocity(3, -Math.PI * .66);
        break;
        
        case 1:
            this.setVelocity(3, -Math.PI * 1.33);
            break;
        case 2:
            this.setVelocity(3, -Math.PI);
            break;
        }

    }
    /**
     * Returns the opposite angle previously traveled in
     */
    public double directionSwap (double dir)
    {
        if(dir == direction) {
            return -Math.PI * 1.25;
        }
        return direction;
        
    }
  //prepare a turn, restart timer
    public void countdownComplete (Object p)
    {
//        this.setVelocity(3, direction);
//        controller.placeAlienShip();
        Participant.expire(this);
        
      
        
//        
    }

}
