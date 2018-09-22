import javafx.scene.shape.Circle;

import java.awt.image.BufferedImage;

public class Enemy
{
	private double x;
	private double y;

	private BufferedImage imageUp;
	private BufferedImage imageDown;
	private boolean facingUp;
	private boolean stop = true;

	private Circle hitbox;

	public static final double VELOCITY = 2.2;
	public static final double ANGLE = Math.toRadians(210);

	public static final double SWITCH_DIRECTION_CHANCE = 0.01;

	public Enemy(double x, double y, BufferedImage imageUp, BufferedImage imageDown)
	{
		this.x = x;
		this.y = y;
		this.imageUp = imageUp;
		this.imageDown = imageDown;

		facingUp = true;
		stop = false;

		hitbox = new Circle(x+imageUp.getWidth()/2, y+imageUp.getHeight()/2, imageUp.getWidth()/2.9);
	}

	public void update()
	{
		if(facingUp)
		{
			x += Math.cos(ANGLE) * VELOCITY;
			y += Math.sin(ANGLE) * VELOCITY;
		}
		else
		{
			x -= Math.cos(ANGLE) * VELOCITY;
			y -= Math.sin(ANGLE) * VELOCITY;
		}

		hitbox = new Circle(x+imageUp.getWidth()/2, y+imageUp.getHeight()/2, imageUp.getWidth()/2.9);

		if(y<80)
			facingUp = false;
		else if(y>220)
			facingUp = true;

		if(Math.random()<SWITCH_DIRECTION_CHANCE)
		{
			facingUp = !facingUp;
		}
	}

	public BufferedImage getCurrentImage()
	{
		if(facingUp)
			return imageUp;
		return imageDown;
	}

	public double getX()
	{
		return x;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public Circle getHitbox()
	{
		return hitbox;
	}

	public void setHitbox(Circle hitbox)
	{
		this.hitbox = hitbox;
	}
}
