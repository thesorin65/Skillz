import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Panel extends JPanel implements KeyListener, MouseMotionListener, Runnable
{
	//Images
	private BufferedImage background;
	private BufferedImage morgana;
	private BufferedImage morganaSpell;

	private BufferedImage buffer;

	private int mouseX;
	private int mouseY;

	private Projectile projectile;

	private int spellCooldown;
	private boolean spellCast;
	private boolean spellAnimation;

	public static final int PLAYER_X = 230;
	public static final int PLAYER_Y = 560;

	public static final double VELOCITY_CONSTANT = 6;
	public static final int MAX_SPELL_COOLDOWN = 150;
	public static final int SPELL_ANIMATION_COOLDOWN = 30;

	public Panel()
	{
		setSize(957,767);

		try
		{
			background = ImageIO.read(new File("Botlane.PNG"));
			morgana = ImageIO.read(new File("Morgana.PNG"));
			morganaSpell = ImageIO.read(new File("MorganaSpell.PNG"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		buffer = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

		spellAnimation = false;

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
		}

		g.drawImage(buffer,0,0,null);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyChar() == 'q' && !spellCast)
		{
			projectile = new Projectile(PLAYER_X-50, PLAYER_Y-100, VELOCITY_CONSTANT, Math.atan2(PLAYER_Y-mouseY,mouseX-PLAYER_X));
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
		if(projectile != null)
		{
			projectile.update();
			if(projectile.getUpdates()>Projectile.MAX_UPDATES)
				projectile = null;
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
