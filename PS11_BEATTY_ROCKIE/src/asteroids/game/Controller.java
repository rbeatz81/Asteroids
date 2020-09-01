package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import asteroids.participants.AlienBullets;
import asteroids.participants.AlienShip;
import asteroids.participants.Asteroid;
import asteroids.participants.Debris;
import asteroids.participants.Dust;
import asteroids.participants.MiniAlienShip;
import asteroids.participants.MyBullets;
import asteroids.participants.Ship;
import asteroids.participants.ShipFlame;

/**
 * Controls a game of Asteroids.
 */
public class Controller implements KeyListener, ActionListener, Iterable<Participant>
{
    /** The state of all the Participants */
    private ParticipantState pstate;

    /** The ship (if one is active) or null (otherwise) */
    private Ship ship;

    /** The AlienShip (if one is active) or null (otherwise) */
    private AlienShip aliens;

    /** The MINIAlienShip (if one is active) or null (otherwise) */
    private MiniAlienShip minis;

    /** When this timer goes off, it is time to refresh the animation */
    private Timer refreshTimer;

    // private JLabel label = new JLabel ("hello");

    /** A Clip that, when played, sounds like a weapon being fired */
    private Clip fireClip;

    /** A Clip that, when played repeatedly, sounds like a small saucer flying */
    private Clip smallSaucerClip;

    /** A Clip that, when played repeatedly, sounds like a big saucer flying */
    private Clip bigSaucerClip;

    /** A Clip that, when played, sounds like a fire */
    private Clip thrustClip;

    /** A Clip that, when played, sounds like a beat */
    private Clip beat1Clip;

    /** A Clip that, when played, sounds like a an asteroid bang */
    private Clip bangClip;

    /** A Clip that, when played, sounds like an alien bang */
    private Clip alienBangClip;

    /** A Clip that, when played, sounds like a big bang */
    private Clip bangLargeClip;

    /**
     * The time at which a transition to a new stage of the game should be made. A transition is scheduled a few seconds
     * in the future to give the user time to see what has happened before doing something like going to a new level or
     * resetting the current level.
     */
    private long transitionTime;

    /** Number of lives left */
    private int lives;

    // the players current score
    private int score = 0;

    // current level
    private int levelNumber = 1;

    /** The game display */
    private Display display;

    // boolean for alien autoPilot
    private boolean autoP = false;

    // boolean for alien autoPilot
    private boolean miniAutoP = false;

    private boolean alienRebirth = false;

    private boolean miniAlienRebirth = false;

    private boolean shipRebirth = false;

    private int bulletCount = 0;

    private boolean rightDwn = false;
    private boolean leftDwn = false;
    private boolean spaceDwn = false;
    private boolean upDwn = false;

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller ()
    {
        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        // Record the display object
        display = new Display(this);

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        display.setVisible(true);
        refreshTimer.start();

        fireClip = createClip("/sounds/fire.wav");
        smallSaucerClip = createClip("/sounds/saucerSmall.wav");
        bigSaucerClip = createClip("/sounds/saucerBig.wav");
        thrustClip = createClip("/sounds/thrust.wav");
        beat1Clip = createClip("/sounds/beat1.wav");
        bangClip = createClip("/sounds/bangShip.wav");
        alienBangClip = createClip("/sounds/bangAlienShip.wav");
        bangLargeClip = createClip("/sounds/bangLarge.wav");

    }

    /**
     * This makes it possible to use an enhanced for loop to iterate through the Participants being managed by a
     * Controller.
     */
    @Override
    public Iterator<Participant> iterator ()
    {
        return pstate.iterator();
    }

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip ()
    {
        return ship;
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen, reset the level, and display the legend
        clear();
        display.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids(levelNumber);
    }

    /**
     * The game is over. Displays a message to that effect.
     */
    private void finalScreen ()
    {
        display.setLegend(GAME_OVER);
        display.removeKeyListener(this);
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShip ()
    {
        // Place a new ship
        Participant.expire(ship);
        ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);
        addParticipant(ship);
        display.setLegend("");
//        ship.setInert(true);

    }

    /**
     * Place an alien ship and autopilot
     */
    public void placeAlienShip ()
    {
        // placeAlienShip();

        addParticipant(aliens = new AlienShip((int) (SIZE * .25), SIZE, -Math.PI * .66, this));

    }

    /**
     * Place an alien ship and autopilot
     */
    public void placeMiniAlienShip ()
    {

        addParticipant(minis = new MiniAlienShip((int) (SIZE * .25), SIZE, -Math.PI * .66, this));

    }

    /**
     * Places an asteroid near one corner of the screen. Gives it a random velocity and rotation.
     */
    private void placeAsteroids (int level)
    {
        int temp;
        // randomly generate the correct number of asteroids to begin a level.
        for (int i = 1; i < 4 + level; i++)
        {
            temp = RANDOM.nextInt(6);

            switch (temp)
            {
                case 0:
                    addParticipant(new Asteroid(0, 2, EDGE_OFFSET + RANDOM.nextInt(1500), EDGE_OFFSET, 3, this));
                    break;
                case 1:
                    addParticipant(new Asteroid(1, 2, EDGE_OFFSET, EDGE_OFFSET + RANDOM.nextInt(1500), 3, this));
                    break;
                case 2:
                    addParticipant(new Asteroid(2, 2, EDGE_OFFSET - RANDOM.nextInt(1500), EDGE_OFFSET, 3, this));
                    break;
                case 3:
                    addParticipant(new Asteroid(0, 2, EDGE_OFFSET + RANDOM.nextInt(1500),
                            EDGE_OFFSET - RANDOM.nextInt(500), 3, this));
                    break;
                case 4:
                    addParticipant(new Asteroid(1, 2, EDGE_OFFSET, EDGE_OFFSET + RANDOM.nextInt(1500), 3, this));
                    break;
                case 5:
                    addParticipant(new Asteroid(2, 2, EDGE_OFFSET, EDGE_OFFSET, 3, this));
                    break;
            }
            if (level == 2)
            {
                scheduleTransition(ALIEN_DELAY);
            }

        }

    }

    /**
     * Clears the screen so that nothing is displayed
     */
    private void clear ()
    {
        pstate.clear();
        display.setLegend("");
        ship = null;
    }

    /**
     * Sets things up and begins a new game.
     */
    private void initialScreen ()
    {
        // Clear the screen
        clear();

        // Place asteroids
        placeAsteroids(levelNumber);

        // Place the ship
        placeShip();

        // Reset statistics
        lives = 3;

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pstate.addParticipant(p);
    }

    /**
     * The ship has been destroyed
     */
    public void shipDestroyed (Ship sh)
    {
        // Null out the ship
        ship = null;

        // Display a legend
        display.setLegend("Ouch!");

        if (bangClip.isRunning())
        {
            bangClip.stop();
        }
        bangClip.setFramePosition(0);
        bangClip.start();

        // Decrement lives
        lives--;
        if (lives >= 1)
        {
            shipRebirth = true;
        }
        Debris m;
        this.addParticipant(m = new Debris(sh, this));

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }

    /**
     * The ship has been destroyed
     */
    public void AliensDestroyed (AlienShip al)
    {
        aliens = null;
        alienRebirth = true;

        if (alienBangClip.isRunning())
        {
            alienBangClip.stop();
        }
        alienBangClip.setFramePosition(0);
        alienBangClip.start();

        if (levelNumber == 2)
        {
            score += 200;
        }

        // testing
        System.out.println("Aliens eradicated");

        Debris m;
        this.addParticipant(m = new Debris(al, this));

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }

    /**
     * The ship has been destroyed
     */
    public void MiniAliensDestroyed (MiniAlienShip mini)
    {
        minis = null;
        miniAlienRebirth = true;

        if (alienBangClip.isRunning())
        {
            alienBangClip.stop();
        }
        alienBangClip.setFramePosition(0);
        alienBangClip.start();

        if (levelNumber == 3)
        {
            score += 1000;
        }

        // testing
        System.out.println("Mini Aliens eradicated");

        Debris n;
        this.addParticipant(n = new Debris(mini, this));

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }

    /**
     * The bullet has been destroyed
     */
    public void myBulletDestroyed ()
    {
        // decrement bullet count
        bulletCount--;

    }

    /**
     * An asteroid has been destroyed
     */
    public void asteroidDestroyed (Asteroid ast)
    {
        int num = ast.getSize();
        addScore(num);

        if (bangLargeClip.isRunning())
        {
            bangLargeClip.stop();
        }
        bangLargeClip.setFramePosition(0);
        bangLargeClip.start();

        // testing
        System.out.println(score);
        System.out.println("" + countAsteroids());

        // make 6 random dust specs
        for (int i = 0; i < 6; i++)
        {
            Dust m;
            this.addParticipant(m = new Dust(ast, this));

        }

        // If all the asteroids are gone, schedule a transition
        if (countAsteroids() == 0)
        {
            // testing
            System.out.println("Asteroids are done for");
            // levelNumber++;
            System.out.println("Level " + levelNumber);

            scheduleTransition(END_DELAY);

        }
    }

    /**
     * returns the lives left
     */
    public int getNumLives ()
    {
        return lives;
    }

    /**
     * returns the current level
     */
    public int getLevel ()
    {
        return levelNumber;
    }

    /**
     * returns the current score
     */
    public int getScore ()
    {
        return score;
    }

    /**
     * Schedules a transition m msecs in the future
     */
    private void scheduleTransition (int m)
    {
        transitionTime = System.currentTimeMillis() + m;

    }

    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if (e.getSource() instanceof JButton)
        {
            initialScreen();
        }

        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
            if (rightDwn && ship != null)
            {

                ship.turnRight();
                if (beat1Clip.isRunning())
                {
                    beat1Clip.stop();
                }
                beat1Clip.setFramePosition(0);
                beat1Clip.start();

            }
            if (leftDwn && ship != null)
            {

                ship.turnLeft();
                if (beat1Clip.isRunning())
                {
                    beat1Clip.stop();
                }
                beat1Clip.setFramePosition(0);
                beat1Clip.start();
            }
            if (upDwn && ship != null)
            {
                ship.accelerate();
                if (thrustClip.isRunning())
                {
                    thrustClip.stop();
                }
                thrustClip.setFramePosition(0);
                thrustClip.start();
            }
            if (spaceDwn && ship != null)
            {

                if (bulletCount > 7)
                {

                }
                else
                {

                    addParticipant(new MyBullets(ship, this));
                    if (fireClip.isRunning())
                    {
                        fireClip.stop();
                    }
                    fireClip.setFramePosition(0);
                    fireClip.start();
                    bulletCount++;

                }

            }

            // It may be time to make a game transition
            performTransition();

            // Move the participants to their new locations
            pstate.moveParticipants();

            // Refresh screen
            display.refresh();
        }
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    private void performTransition ()
    {
        // Do something only if the time has been reached
        if (transitionTime <= System.currentTimeMillis())
        {

            // Clear the transition time
            transitionTime = Long.MAX_VALUE;

            // If there are no lives left, the game is over. Show the final
            // screen.
            if (lives <= 0)
            {
                finalScreen();
            }

            // if (lives > 0) {
            // if (beat1Clip.isRunning())
            // {
            // beat1Clip.stop();
            // }
            // beat1Clip.setFramePosition(0);
            // beat1Clip.start();
            // }

            if (countAsteroids() == 0)
            {
                this.placeAsteroids(levelNumber);
                levelNumber++;
                System.out.println("" + countAsteroids());
                ;

            }
            if (shipRebirth && ship == null)
            {
                placeShip();

                shipRebirth = false;

            }
            // big alienship
            if (aliens == null && alienRebirth && transitionTime == Long.MAX_VALUE && levelNumber == 2)
            {
                if (bigSaucerClip.isRunning())
                {
                    bigSaucerClip.stop();
                }
                bigSaucerClip.setFramePosition(0);
                bigSaucerClip.loop(10);
                placeAlienShip();
                autoP = true;
                alienRebirth = false;
                scheduleTransition(END_DELAY);
            }
            // wait to display big alien ship
            if (levelNumber == 2 && !autoP && transitionTime == Long.MAX_VALUE)
            {
                if (bigSaucerClip.isRunning())
                {
                    bigSaucerClip.stop();
                }
                bigSaucerClip.setFramePosition(0);
                bigSaucerClip.loop(10);
                placeAlienShip();
                autoP = true;
                scheduleTransition(1000);

            }
            // wait to move big alien ship in random 3 directions
            if (levelNumber == 2 && autoP && transitionTime == Long.MAX_VALUE)
            {
                if (bigSaucerClip.isRunning())
                {
                    bigSaucerClip.stop();
                }
                bigSaucerClip.setFramePosition(0);
                bigSaucerClip.loop(10);

                aliens.AlienAutoPilot();
                addParticipant(new AlienBullets(aliens, this));
                scheduleTransition(1000);

            }
            // mini alienship
            if (minis == null && miniAlienRebirth && transitionTime == Long.MAX_VALUE && levelNumber == 3)
            {
                if (smallSaucerClip.isRunning())
                {
                    smallSaucerClip.stop();
                }
                smallSaucerClip.setFramePosition(0);
                smallSaucerClip.loop(10);
                placeMiniAlienShip();
                miniAutoP = true;
                miniAlienRebirth = false;
                scheduleTransition(END_DELAY);
            }
            // wait to display MINI alien ship
            if (levelNumber == 3 && !miniAutoP && transitionTime == Long.MAX_VALUE)
            {
                if (smallSaucerClip.isRunning())
                {
                    smallSaucerClip.stop();
                }
                smallSaucerClip.setFramePosition(0);
                smallSaucerClip.loop(10);
                placeMiniAlienShip();
                miniAutoP = true;
                scheduleTransition(1000);

            }
            // wait to move MINI alien ship in random 3 directions
            if (levelNumber == 3 && miniAutoP && transitionTime == Long.MAX_VALUE)
            {
                if (smallSaucerClip.isRunning())
                {
                    smallSaucerClip.stop();
                }
                smallSaucerClip.setFramePosition(0);
                smallSaucerClip.loop(10);

                minis.MiniAlienAutoPilot();
                if (ship != null)
                {
                    addParticipant(new AlienBullets(minis, ship, this));
                }
                scheduleTransition(1000);

            }

        }
    }

    /**
     * Returns the number of asteroids that are active participants
     */
    private int countAsteroids ()
    {
        int count = 0;
        for (Participant p : this)
        {
            if (p instanceof Asteroid)
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the number of asteroids that are active participants
     */
    private void addScore (int plus)
    {
        if (plus == 0)
        {

            score += 100;

        }
        if (plus == 1)
        {

            score += 50;
        }
        if (plus == 2)
        {

            score += 20;
        }

    }

    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {

        // turn right
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null)
        {
            rightDwn = true;

        }
        // // turn right
        // if (e.getKeyCode() == KeyEvent.VK_D && ship != null)
        // {
        // rightDwn = true;
        //
        // }
        // turn left
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship != null)
        {
            leftDwn = true;

        }
        // // turn left
        // if (e.getKeyCode() == KeyEvent.VK_A && ship != null)
        // {
        // leftDwn = true;
        //
        // }
        // accelerate
        if (e.getKeyCode() == KeyEvent.VK_UP && ship != null)
        {

            upDwn = true;
            ship.setAccel(upDwn);

        }
        // // accelerate
        // if (e.getKeyCode() == KeyEvent.VK_W && ship != null)
        // {
        //
        // upDwn = true;
        // ship.setAccel(upDwn);
        //
        // }
        // shoot
        if (e.getKeyCode() == KeyEvent.VK_SPACE && ship != null)
        {
            if (bulletCount > 7)
            {

            }
            else
            {

                spaceDwn = true;

            }

        }
        // shoot
        // if (e.getKeyCode() == KeyEvent.VK_S && ship != null)
        // {
        // if (bulletCount > 7)
        // {
        //
        // }
        // else
        // {
        //
        // spaceDwn = true;
        //
        // }
        //
        // }
        // shoot
        // if (e.getKeyCode() == KeyEvent.VK_DOWN && ship != null)
        // {
        // if (bulletCount > 7)
        // {
        //
        // }
        // else
        // {
        //
        // spaceDwn = true;
        //
        // }
        //
        // }
    }

    @Override
    public void keyTyped (KeyEvent e)
    {
    }

    @Override
    public void keyReleased (KeyEvent e)
    {
        // set rightDwn to false so it is smooth when refreshed
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null)
        {
            rightDwn = false;

        }
        // set leftDwn to false so it is smooth when refreshed
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship != null)
        {
            leftDwn = false;

        }
        if (e.getKeyCode() == KeyEvent.VK_UP && ship != null)
        {
            upDwn = false;
            ship.setAccel(upDwn);

        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE && ship != null)
        {

            spaceDwn = false;

        }

    }

    /**
     * Creates an audio clip from a sound file.
     */
    public Clip createClip (String soundFile)
    {
        // Opening the sound file this way will work no matter how the
        // project is exported. The only restriction is that the
        // sound files must be stored in a package.
        try (BufferedInputStream sound = new BufferedInputStream(getClass().getResourceAsStream(soundFile)))
        {
            // Create and return a Clip that will play a sound file. There are
            // various reasons that the creation attempt could fail. If it
            // fails, return null.
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            return clip;
        }
        catch (LineUnavailableException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        catch (UnsupportedAudioFileException e)
        {
            return null;
        }
    }
}
