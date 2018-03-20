package cs151.whiteboard;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class DOval extends DShape {

	@Override
	public void draw(Graphics g) {
		g.setColor(getColor());
		Rectangle bounds = getBounds();
		((Graphics2D) g).fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
	}

}
