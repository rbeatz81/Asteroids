package asteroids.participants;

import static asteroids.game.Constants.SHIP_FRICTION;
import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class ShipFlame extends Participant
{
    double xLocation;
    double yLocation;
    Controller con;

    private Shape outline;

    public ShipFlame (Ship p, Controller controller)
    {
        con = controller;
        xLocation = p.getX();
       
        yLocation = p.getY();
        this.setDirection(p.getRotation());
        // makes it only last a certain amount of time
        new ParticipantCountdownTimer(this, 50);
    }

    @Override
    protected Shape getOutline ()
    {
        Path2D.Double poly = new Path2D.Double();

        // left side
        poly.moveTo(xLocation, yLocation + 12);

        poly.lineTo(xLocation - 3.5, yLocation + 20);
        poly.lineTo(xLocation - 7, yLocation + 12);

        // left side big
        poly.lineTo(xLocation, yLocation + 30);
        poly.lineTo(xLocation - 3.5, yLocation + 12);

        // right side
        poly.moveTo(xLocation, yLocation + 12);

        poly.lineTo(xLocation + 3.5, yLocation + 20);
        poly.lineTo(xLocation + 7, yLocation + 12);

        // right side big

        poly.lineTo(xLocation, yLocation + 30);
        poly.lineTo(xLocation + 3.5, yLocation + 12);

        poly.closePath();
        outline = poly;
        
        

        return outline;

    }

    @Override
    public void collidedWith (Participant p)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void move ()
    {
        applyFriction(SHIP_FRICTION);
        super.move();
    }

    // delay demise
    public void countdownComplete (Object p)
    {
        Participant.expire(this);
    }
    /**
     * Turns right by Pi/16 radians
     */
    public void turnRight ()
    {
        rotate(Math.PI / 16);
    }

    /**
     * Turns left by Pi/16 radians
     */
    public void turnLeft ()
    {
        rotate(-Math.PI / 16);
    }

}
