import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public class Projectile
{
	private double x;
	private double y;

	private double velocity;
	private double angle;

	private BufferedImage image;
	private Circle hitbox;

	private int updates = 0;
	public static final int MAX_UPDATES = 50;


	public Projectile(int x, int y, double velocity, double angle)
	{
		this.x = x;
		this.y = y;
		this.velocity = velocity;
		if(angle<0)
			angle+=Math.PI*2;
		this.angle = -angle;

		try
		{
			image = ImageIO.read(new File("DarkBinding.PNG"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		hitbox = new Circle(x+image.getWidth()/2, y+image.getHeight()/2, image.getWidth()/1.8);
		image = rotate(image, angle);
	}

	public void update()
	{
		updates++;

		x += Math.cos(angle) * velocity;
		y += Math.sin(angle) * velocity;

		hitbox = new Circle(x+image.getWidth()/2, y+image.getHeight()/2, image.getWidth()/1.8);

	}


	/* Pre: Receives a buffered image and a double angle
	 * Post: 	creates and returns a rotated copy of the received image
	 * null is returned if the received image is null*/
	public static BufferedImage rotate(BufferedImage img, double angle)
	{
		if(img==null)
			return null;

		angle = -Math.toDegrees(angle)%360;

		AffineTransform affineTransform = new AffineTransform();
		affineTransform.setToTranslation(0,0);
		affineTransform.rotate(Math.toRadians(angle), img.getWidth()/2, img.getHeight()/2);

		int transparency = img.getColorModel().getTransparency();

		BufferedImage rotated =	new BufferedImage( img.getWidth(), img.getHeight(), transparency);

		Graphics2D g = (Graphics2D) (rotated.getGraphics());

		g.drawImage(img, affineTransform, null);

		return rotated;
	}

	public Circle getHitbox()
	{
		return hitbox;
	}

	public void setHitbox(Circle hitbox)
	{
		this.hitbox = hitbox;
	}

	public int getUpdates()
	{
		return updates;
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

	public double getVelocity()
	{
		return velocity;
	}

	public void setVelocity(double velocity)
	{
		this.velocity = velocity;
	}

	public double getAngle()
	{
		return angle;
	}

	public void setAngle(double angle)
	{
		this.angle = angle;
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
	}
}
