package cs151.whiteboard;

import java.util.List;

public interface ShapeListener {

	public void shapeSelected(DShapeModel model);

	public void shapesChanged(List<DShapeModel> models);
}
