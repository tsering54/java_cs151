package cs151.whiteboard;

import java.awt.Point;
import java.awt.Rectangle;

public class DLineModel extends DShapeModel {

	private static final long serialVersionUID = 1L;

	private Point p1;

	private Point p2;

	public void setPoints(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;

		int x = Math.min(p1.x, p2.x);
		int y = Math.min(p1.y, p2.y);
		int w = Math.abs(p1.x - p2.x);
		int h = Math.abs(p1.y - p2.y);
		super.setBounds(new Rectangle(x, y, w, h));
	}

	public Point getP1() {
		return p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	@Override
	public void setBounds(Rectangle bounds) {
		p1 = new Point(bounds.x, bounds.y);
		p2 = new Point(bounds.x + bounds.width, bounds.y + bounds.height);
		super.setBounds(bounds);
	}

}
