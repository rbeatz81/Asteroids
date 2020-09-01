package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import static asteroids.game.Constants.*;

public class Dust extends Participant
{
    
    double xLocation;
    double yLocation;
    double direction;
    int swapOn;
    Controller c;
    
    public Dust (Asteroid ast, Controller controller) 
    {
        c = controller;
      //makes it only last a certain amount of time in order to give it its maximum range
        new ParticipantCountdownTimer(this,  "limit", 550);
        xLocation = ast.getX();
        yLocation = ast.getY();
        
        swapOn = RANDOM.nextInt(6);
        
        switch(swapOn) {
            case 0:
                xLocation += 10;
                this.setVelocity (swapOn, RANDOM.nextDouble() * 2 * Math.PI);
                
              break;
            case 1:
                this.setVelocity (swapOn, RANDOM.nextDouble() * 2 * Math.PI);
                yLocation += 10;
              break;
            case 2:
                xLocation += 5;
                yLocation -= 5;
                this.setVelocity (swapOn, RANDOM.nextDouble() * 2 * Math.PI);
                break;
            case 3:
                this.setVelocity (swapOn, RANDOM.nextDouble() * 2 * Math.PI);
                yLocation += 20;
                break;
            case 4:
                xLocation += 15;
                yLocation -= 15;
                this.setVelocity (swapOn, RANDOM.nextDouble() * 2 * Math.PI);
                break;
            case 5:
                
                break;           
//            default:
              // code block
          }
        
        
        
        
    }

    @Override
    protected Shape getOutline ()
    {
        return new Ellipse2D.Double(xLocation, yLocation, 1,1);
        
        
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
       
    }

}
