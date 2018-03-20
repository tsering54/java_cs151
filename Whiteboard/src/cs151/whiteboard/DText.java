package cs151.whiteboard;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

public class DText extends DShape {

	private Font computeFont(Graphics g, DTextModel model) {
		double size = 1.0;
		Rectangle bounds = model.getBounds();
		while (true) {
			Font font = new Font(model.getFontName(), Font.PLAIN, (int) Math.ceil(size));
			FontMetrics metrics = g.getFontMetrics(font);
			if (metrics.getHeight() < bounds.height) {
				size = size * 1.1 + 1;
			} else {
				return font;
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		DTextModel model = (DTextModel) getModel();
		Font f = computeFont(g, model);

		Rectangle bounds = getBounds();
		Shape curClip = g.getClip();
		g.setClip(curClip.getBounds().createIntersection(bounds));
		g.setColor(model.getColor());
		g.setFont(f);
		g.drawString(model.getText(), bounds.x, bounds.y + bounds.height);
		g.setClip(curClip);
	}

}
