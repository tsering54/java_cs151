package cs151.whiteboard;

public interface CanvasListener {

	public void addShape(DShapeModel model);

	public void removeShape(DShapeModel model);

	public void sendToFront(DShapeModel model);

	public void sendToBack(DShapeModel model);

	public void updateModel(DShapeModel model);

	public void clear();
}
