package asteroids.participants;

import static asteroids.game.Constants.ASTEROID_SCALE;
import static asteroids.game.Constants.RANDOM;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.MyBulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;

public class MiniAlienShip extends Participant implements ShipDestroyer, AsteroidDestroyer, MyBulletDestroyer
{
    /** The outline of the ship */
    private Shape outline;
    private double direction;

    /** Game controller */
    private Controller controller;

    public MiniAlienShip (int x, int y, double direction, Controller controller)
    {

        this.direction = direction;

        this.controller = controller;

        setPosition(750, 550);
        setRotation(direction);
        setVelocity(5, direction);

        Path2D.Double poly = new Path2D.Double();
        // top hat
        poly.moveTo(21, -12);
        poly.lineTo(21, 12);

        poly.lineTo(16, 12);
        poly.lineTo(16, -12);

        // middle section
        poly.lineTo(8, -20);
        poly.lineTo(8, 20);

        poly.lineTo(17, 12);
        poly.lineTo(21, 12);
        poly.closePath();

        // bottom belly
        poly.moveTo(9, 20);
        poly.lineTo(1, 14);

        poly.lineTo(1, -14);
        poly.lineTo(9, -20);
        poly.closePath();

        // Scale to the desired size
        double scale = .3;
        poly.transform(AffineTransform.getScaleInstance(scale, scale));

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
            controller.MiniAliensDestroyed(this);
        }

    }

    /**
     * Set the minialiens autopilot
     */
    public void MiniAlienAutoPilot ()
    {

        int num = RANDOM.nextInt(3);

        switch (num)
        {

            case 0:
                this.setVelocity(5, -Math.PI * .66);
                break;

            case 1:
                this.setVelocity(5, -Math.PI * 1.33);
                break;
            case 2:
                this.setVelocity(5, -Math.PI);
                break;
        }

    }

    /**
     * Returns the opposite angle previously traveled in
     */
    public double directionSwap (double dir)
    {
        if (dir == direction)
        {
            return -Math.PI * 1.25;
        }
        return direction;

    }
    // TODO Auto-generated method stub

}
