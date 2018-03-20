package cs151.whiteboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener, ModelListener {

	private static final long serialVersionUID = 1L;

	private List<DShape> shapes = new ArrayList<DShape>();

	private DShape selected = null;

	private Point origLoc = null;

	private List<Rectangle> knobRects = new ArrayList<Rectangle>();

	private boolean knobSelected = false;

	private int movingKnobPos = -1;

	private Point movingPoint = null;

	private Point anchorPoint = null;

	private List<ShapeListener> listeners = new ArrayList<ShapeListener>();

	private CanvasListener cListener;

	private boolean clientMode;

	public Canvas() {
		setPreferredSize(new Dimension(400, 400));
		setBackground(Color.WHITE);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void setCanvasListener(CanvasListener cListener) {
		this.cListener = cListener;
	}

	public void enableClientMode() {
		this.clientMode = true;
	}

	public void addShapeListener(ShapeListener listener) {
		listeners.add(listener);
	}

	public void addShape(DShapeModel model) {
		DShape shape = null;

		if (model instanceof DOvalModel) {
			shape = new DOval();
		} else if (model instanceof DRectModel) {
			shape = new DRect();
		} else if (model instanceof DLineModel) {
			shape = new DLine();
		} else if (model instanceof DTextModel) {
			shape = new DText();
		}

		shape.setModel(model);
		shapes.add(shape);
		model.addModelListener(this);
		if (!clientMode) {
			selected = shape;
			notifyShapeSelection(selected);
		}
		notifyShapesChanged();

		if (cListener != null) {
			cListener.addShape(model);
		}
		repaint();
	}

	public void clear() {
		shapes.clear();
		selected = null;
		notifyShapeSelection(null);
		notifyShapesChanged();
		if (cListener != null) {
			cListener.clear();
		}
		repaint();
	}

	// TODO: make this thread-safe; NetworkManager (running in server thread) may
	// call this for initializing a new client canvas when the canvas model list is
	// getting updated
	public List<DShapeModel> getModels() {
		List<DShapeModel> models = new ArrayList<DShapeModel>();
		for (DShape shape : shapes) {
			models.add(shape.getModel());
		}
		return models;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (DShape shape : shapes) {
			shape.draw(g);
		}
		if (selected != null) {
			drawKnobs(selected, g);
		} else {
			knobRects.clear();
		}
	}

	private void drawKnobs(DShape shape, Graphics g) {
		g.setColor(Color.BLACK);
		List<Point> knobs = shape.getKnobs();
		knobRects.clear();
		for (Point p : knobs) {
			Rectangle r = new Rectangle(p.x - 4, p.y - 4, 9, 9);
			g.fillRect(r.x, r.y, r.width, r.height);
			knobRects.add(r);
		}
	}

	private void notifyShapeSelection(DShape selected) {
		DShapeModel model = null;
		if (selected != null) {
			model = selected.getModel();
		}

		for (ShapeListener listener : listeners) {
			listener.shapeSelected(model);
		}
	}

	private DShape selectShape(Point p) {

		DShape newSel = null;
		knobSelected = false;

		for (int i = shapes.size() - 1; i >= 0; i--) {
			DShape shape = shapes.get(i);
			Rectangle bounds = shape.getBounds();
			if (bounds.contains(p)) {
				newSel = shape;
				break;
			}
		}

		// check if knob is selected
		if ((newSel == null || newSel == selected) && !knobRects.isEmpty()) {
			for (int i = 0; i < knobRects.size(); i++) {
				if (knobRects.get(i).contains(p)) {
					newSel = selected;
					knobSelected = true;
					movingKnobPos = i;
					break;
				}
			}
		}

		notifyShapeSelection(newSel);
		return newSel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		selected = selectShape(e.getPoint());
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		origLoc = e.getPoint();
		selected = selectShape(e.getPoint());

		if (knobSelected) {
			List<Point> knobs = selected.getKnobs();
			int delta = knobs.size() / 2;
			movingPoint = knobs.get(movingKnobPos);
			anchorPoint = knobs.get((movingKnobPos + delta) % knobs.size());
		} else {
			movingPoint = anchorPoint = null;
		}

		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (clientMode) {
			return;
		}

		if (selected != null) {
			Point curLoc = e.getPoint();
			int dx = curLoc.x - origLoc.x;
			int dy = curLoc.y - origLoc.y;
			origLoc = curLoc;

			if (knobSelected) {
				movingPoint.x += dx;
				movingPoint.y += dy;
				selected.resize(anchorPoint, movingPoint);
			} else {
				selected.move(dx, dy);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	private void notifyShapesChanged() {
		List<DShapeModel> models = new ArrayList<DShapeModel>();
		for (DShape shape : shapes) {
			models.add(shape.getModel());
		}

		for (ShapeListener listener : listeners) {
			listener.shapesChanged(models);
		}
	}

	private void removeShapeInt(DShape shape) {
		shapes.remove(shape);
		if (cListener != null) {
			cListener.removeShape(shape.getModel());
		}
		notifyShapesChanged();
		repaint();
	}

	public void removeSelected() {
		if (selected != null) {
			DShape tmp = selected;
			selected = null;
			removeShapeInt(tmp);
		}
	}

	public void removeShape(DShapeModel model) {
		DShape shape = findShape(model);
		if (shape == null) {
			return;
		}

		if (shape == selected) {
			selected = null;
			if (shape instanceof DText) {
				notifyShapeSelection(null);
			}
		}

		removeShapeInt(shape);
	}

	private void sendToFrontInt(DShape shape) {

		shapes.remove(shape);
		shapes.add(shape);
		if (cListener != null) {
			cListener.sendToFront(shape.getModel());
		}
		notifyShapesChanged();
		repaint();
	}

	public void sendToFront() {
		if (selected != null) {
			sendToFrontInt(selected);
		}
	}

	public void sendToFront(DShapeModel model) {
		DShape shape = findShape(model);
		if (shape == null) {
			return;
		}
		sendToFrontInt(shape);
	}

	private void sendToBackInt(DShape shape) {
		shapes.remove(shape);
		shapes.add(0, shape);
		if (cListener != null) {
			cListener.sendToBack(shape.getModel());
		}
		notifyShapesChanged();
		repaint();
	}

	public void sendToBack() {
		if (selected != null) {
			sendToBackInt(selected);
		}
	}

	public void sendToBack(DShapeModel model) {
		DShape shape = findShape(model);
		if (shape == null) {
			return;
		}
		sendToBackInt(shape);
	}

	public boolean shapeSelected() {
		return selected != null;
	}

	public Color getSelectedShapeColor() {
		if (selected != null) {
			return selected.getColor();
		}
		return null;
	}

	public void setSelectedShapeColor(Color newColor) {
		selected.getModel().setColor(newColor);
	}

	@Override
	public void modelChanged(DShapeModel model) {
		repaint();
		if (cListener != null) {
			cListener.updateModel(model);
		}
	}

	private DShape findShape(DShapeModel model) {
		for (DShape shape : shapes) {
			if (model.equals(shape.getModel())) {
				return shape;
			}
		}

		return null;
	}

	public void updateModel(DShapeModel model) {
		DShape shape = findShape(model);
		if (shape == null) {
			return;
		}
		shape.setModel(model);
		notifyShapesChanged();

		if (shape == selected && shape instanceof DText) {
			notifyShapeSelection(selected);
		}

		repaint();
	}

}
