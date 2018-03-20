package cs151.whiteboard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class DShapeTableModel extends AbstractTableModel implements ModelListener, ShapeListener {

	private static final long serialVersionUID = 1L;

	private List<DShapeModel> models = new ArrayList<DShapeModel>();

	private String[] colNames = { "X", "Y", "WIDTH", "HEIGHT" };

	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public int getRowCount() {
		return models.size();
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		DShapeModel model = models.get(row);
		switch (col) {
		case 0:
			return new Integer(model.getBounds().x);
		case 1:
			return new Integer(model.getBounds().y);
		case 2:
			return new Integer(model.getBounds().width);
		case 3:
			return new Integer(model.getBounds().height);
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void modelChanged(DShapeModel model) {
		int row = models.indexOf(model);
		fireTableRowsUpdated(row, row);
	}

	@Override
	public void shapeSelected(DShapeModel model) {
	}

	@Override
	public void shapesChanged(List<DShapeModel> models) {
		this.models = new ArrayList<DShapeModel>(models);
		fireTableDataChanged();
	}

}
