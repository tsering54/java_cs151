package cs151.whiteboard;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DLine extends DShape {

	@Override
	public void draw(Graphics g) {
		g.setColor(getColor());
		DLineModel model = (DLineModel) getModel();
		Point p1 = model.getP1();
		Point p2 = model.getP2();
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}

	@Override
	public List<Point> getKnobs() {
		List<Point> knobs = new ArrayList<Point>();
		DLineModel model = (DLineModel) getModel();
		knobs.add(new Point(model.getP1()));
		knobs.add(new Point(model.getP2()));
		return knobs;
	}

	@Override
	public void resize(Point anchorPoint, Point movingPoint) {
		DLineModel model = (DLineModel) getModel();
		model.setPoints(new Point(anchorPoint), new Point(movingPoint));
	}

	@Override
	public void move(int dx, int dy) {
		DLineModel model = (DLineModel) getModel();
		Point p1 = new Point(model.getP1());
		Point p2 = new Point(model.getP2());
		p1.x += dx;
		p1.y += dy;
		p2.x += dx;
		p2.y += dy;
		model.setPoints(p1, p2);
	}

}
