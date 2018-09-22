import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class Panel extends JPanel implements KeyListener, MouseMotionListener, Runnable
{
	//Images
	private BufferedImage background;

	private BufferedImage morgana;
	private BufferedImage morganaSpell;
	private BufferedImage darkBinding;

	private BufferedImage kaisaUp;
	private BufferedImage kaisaDown;

	private BufferedImage buffer;

	private int mouseX;
	private int mouseY;

	private Projectile projectile;

	private Enemy enemy;

	private int spellCooldown;
	private boolean spellCast;
	private boolean spellAnimation;

	private int score = 0;

	private double startTime;

	public static final int PLAYER_X = 230;
	public static final int PLAYER_Y = 560;

	public static final double VELOCITY_CONSTANT = 4.2;
	public static final int MAX_SPELL_COOLDOWN = 250;
	public static final int SPELL_ANIMATION_COOLDOWN = 30;

	public Panel()
	{
		setSize(957,767);

		try
		{
			background = ImageIO.read(new File("Botlane.PNG"));
			morgana = ImageIO.read(new File("Morgana.PNG"));
			morganaSpell = ImageIO.read(new File("MorganaSpell.PNG"));
			darkBinding = ImageIO.read(new File("DarkBinding.PNG"));
			kaisaUp = ImageIO.read(new File("Kai'sa_UpLeft.PNG"));
			kaisaDown = ImageIO.read(new File("Kai'sa_DownRight.PNG"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		buffer = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

		spellAnimation = false;

		enemy = new Enemy(500, 220, kaisaUp, kaisaDown);

		startTime = System.nanoTime();

		addKeyListener(this);
		addMouseMotionListener(this);

		Thread t = new Thread(this);
		t.start();
	}

	public void paint(Graphics g)
	{
		Graphics bg = buffer.getGraphics();

		bg.drawImage(background,0,0,null);

		if(spellAnimation)
			bg.drawImage(morganaSpell, PLAYER_X-55,PLAYER_Y-105,null);
		else
			bg.drawImage(morgana, PLAYER_X-55,PLAYER_Y-105,null);

		if(projectile!=null)
		{
			bg.drawImage(projectile.getImage(), (int)projectile.getX(), (int)projectile.getY(), null);

//			if(projectile.getHitbox()!=null)
//				bg.drawOval((int)projectile.getHitbox().getCenterX()-(int)projectile.getHitbox().getRadius(),
//						(int)projectile.getHitbox().getCenterY()-(int)projectile.getHitbox().getRadius(),
//						(int)projectile.getHitbox().getRadius()*2,
//						(int)projectile.getHitbox().getRadius()*2);
		}

		bg.drawImage(enemy.getCurrentImage(), (int)enemy.getX(), (int)enemy.getY(), null);
//		bg.drawOval((int)enemy.getHitbox().getCenterX()-(int)enemy.getHitbox().getRadius(),
//				(int)enemy.getHitbox().getCenterY()-(int)enemy.getHitbox().getRadius(),
//				(int)enemy.getHitbox().getRadius()*2,
//				(int)enemy.getHitbox().getRadius()*2);

		bg.setColor(Color.BLUE);
		bg.fillRect(887, 0, 70, 30);
		bg.setColor(Color.YELLOW);
		bg.drawString(""+score, 897, 20);

		int curMin = (int)((System.nanoTime()-startTime)/Math.pow(10, 9)/60);
		int curSec = (int)((System.nanoTime()-startTime)/Math.pow(10, 9)%60);
		String time = String.format("%01d:%02d", curMin, curSec);

		bg.drawString(time, 925, 20);

		g.drawImage(buffer,0,0,null);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyChar() == 'q' && !spellCast)
		{
			projectile = new Projectile(PLAYER_X-50, PLAYER_Y-100, VELOCITY_CONSTANT, Math.atan2(PLAYER_Y-mouseY,mouseX-PLAYER_X), darkBinding);
			spellCast = true;
			spellAnimation = true;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(10);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			update();
		}
	}

	public void update()
	{
		enemy.update();

		if(projectile != null)
		{
			projectile.update();


			if(projectile.getUpdates()>Projectile.MAX_UPDATES)
				projectile = null;
		}

		if(projectile != null && intersect(projectile.getHitbox(), enemy.getHitbox()))
		{
			projectile = null;
			score++;
		}

		if(spellCast)
		{
			spellCooldown++;
			if(spellCooldown>MAX_SPELL_COOLDOWN)
			{
				spellCooldown = 0;
				spellCast = false;
			}
			if(spellCooldown==SPELL_ANIMATION_COOLDOWN)
			{
				spellAnimation = false;
			}
		}

		repaint();
	}

	public void addNotify()
	{
		super.addNotify();
		requestFocus();
	}

	public boolean intersect(Circle hitbox1, Circle hitbox2)
	{
		double distance = Math.pow((Math.pow(hitbox1.getCenterX() - hitbox2.getCenterX(), 2)) + Math.pow(hitbox1.getCenterY() - hitbox2.getCenterY(), 2), 0.5);

		System.out.println(distance);

		if(distance < hitbox1.getRadius() + hitbox2.getRadius())
			return true;
		return false;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyReleased(KeyEvent e)
	{

	}

	@Override
	public void mouseDragged(MouseEvent e)
	{

	}
}
