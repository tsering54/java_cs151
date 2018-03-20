package cs151.whiteboard;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DShapeModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Rectangle bounds;

	private Color color = Color.GRAY;

	private transient List<ModelListener> listeners = new ArrayList<ModelListener>();

	private int id;

	private static int nextId = 0;

	public DShapeModel() {
		id = nextId++;
	}

	@Override
	public int hashCode() {
		return id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		return ((DShapeModel) o).id == id;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
		notifyModelListeners();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		notifyModelListeners();
	}

	public void addModelListener(ModelListener listener) {
		listeners.add(listener);
	}

	public void removeModelListener(ModelListener listener) {
		listeners.remove(listener);
	}

	protected void notifyModelListeners() {
		for (ModelListener listener : listeners) {
			listener.modelChanged(this);
		}
	}

}
