package cs151.whiteboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public abstract class DShape {

	private DShapeModel model;

	public abstract void draw(Graphics g);

	public DShape() {

	}

	public DShapeModel getModel() {
		return model;
	}

	public void setModel(DShapeModel model) {
		this.model = model;
	}

	public Rectangle getBounds() {
		return model.getBounds();
	}

	public List<Point> getKnobs() {
		List<Point> knobs = new ArrayList<Point>();
		Rectangle bounds = model.getBounds();
		knobs.add(new Point(bounds.x, bounds.y));
		knobs.add(new Point(bounds.x + bounds.width, bounds.y));
		knobs.add(new Point(bounds.x + bounds.width, bounds.y + bounds.height));
		knobs.add(new Point(bounds.x, bounds.y + bounds.height));
		return knobs;
	}

	public Color getColor() {
		return model.getColor();
	}

	public void resize(Point anchorPoint, Point movingPoint) {
		int x = Math.min(anchorPoint.x, movingPoint.x);
		int y = Math.min(anchorPoint.y, movingPoint.y);
		int w = Math.abs(anchorPoint.x - movingPoint.x);
		int h = Math.abs(anchorPoint.y - movingPoint.y);
		Rectangle newBounds = new Rectangle(x, y, w, h);
		model.setBounds(newBounds);
	}

	public void move(int dx, int dy) {
		Rectangle curBound = model.getBounds();
		Rectangle newBounds = new Rectangle(curBound.x + dx, curBound.y + dy, curBound.width, curBound.height);
		model.setBounds(newBounds);
	}

}
