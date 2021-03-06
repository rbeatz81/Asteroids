package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.*;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * Represents ships
 */
public class Ship extends Participant implements AsteroidDestroyer, AlienDestroyer, AlienBulletDestroyer
{
    /** The outline of the ship */
    public Shape outline;

    /** The outline of the ship with small flame */
    private Shape outline2;

    /** The outline of the ship with large flame */
    private Shape outline3;

    /** Game controller */
    private Controller controller;

    // true if the ship is accelerating
    private boolean accelStatus = false;

    // used to switch between flame outlines
    private boolean flameFlipper = false;

    /**
     * Constructs a ship at the specified coordinates that is pointed in the given direction.
     */
    public Ship (int x, int y, double direction, Controller controller)
    {

        this.controller = controller;

        setPosition(x, y);
        setRotation(direction);

        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(21, 0);
        poly.lineTo(-21, 12);
        poly.lineTo(-14, 10);
        poly.lineTo(-14, -10);
        poly.lineTo(-21, -12);
        poly.closePath();
        outline = poly;

        // draw ship with the small flame
        Path2D.Double poly2 = new Path2D.Double();
        poly2.moveTo(21, 0);
        poly2.lineTo(-21, 12);
        poly2.lineTo(-14, 10);
        poly2.lineTo(-14, -10);
        poly2.lineTo(-21, -12);
        poly2.closePath();

        // left side
        poly2.moveTo(-14, -10);
        // middle
        poly2.lineTo(-28, 0);
        // right side
        poly2.lineTo(-14, 10);
        poly2.closePath();

        outline2 = poly2;

        // draw ship with the large flame
        Path2D.Double poly3 = new Path2D.Double();
        poly3.moveTo(21, 0);
        poly3.lineTo(-21, 12);
        poly3.lineTo(-14, 10);
        poly3.lineTo(-14, -10);
        poly3.lineTo(-21, -12);
        poly3.closePath();

        // left side
        poly3.moveTo(-14, -10);
        // middle
        poly3.lineTo(-32, 0);
        // right side
        poly3.lineTo(-14, 10);
        poly3.closePath();

        outline3 = poly3;

        // Schedule an acceleration in two seconds
       // new ParticipantCountdownTimer(this, "move", 2000);
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public void setAccel (boolean accelStatus)
    {
        this.accelStatus = accelStatus;

    }

  
    protected Shape getOutline ()
    {

        if (accelStatus == true)
        {
            //switch back and forth between flame outlines
            if (flameFlipper)
            {
                flameFlipper = false;
                return outline2;

            }
            flameFlipper = true;
            return outline3;
        }

        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        applyFriction(SHIP_FRICTION);
        super.move();
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

    /**
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {

        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(21, 0);
        poly.lineTo(-21, 12);
        poly.lineTo(-14, 10);
        poly.lineTo(-14, -10);
        poly.lineTo(-21, -12);
        poly.closePath();
        outline = poly;

        accelerate(SHIP_ACCELERATION);
    }

    /**
     * When a Ship collides with a ShipDestroyer, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);

            // Tell the controller the ship was destroyed
            controller.shipDestroyed(this);
        }
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // Give a burst of acceleration, then schedule another
        // burst for 200 msecs from now.
        if (payload.equals("move"))
        {
            accelerate();
            new ParticipantCountdownTimer(this, "move", 200);
        }
    }
}
