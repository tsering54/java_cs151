package cs151.whiteboard;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

public class WhiteboardClient implements Runnable {

	private Canvas canvas;

	private Socket socket;

	public WhiteboardClient(Canvas canvas, Socket socket) {
		this.canvas = canvas;
		this.socket = socket;
	}

	private DShapeModel decode(String obj) {
		ByteArrayInputStream bais = new ByteArrayInputStream(obj.getBytes());
		XMLDecoder decoder = new XMLDecoder(bais);
		DShapeModel model = (DShapeModel) decoder.readObject();
		decoder.close();
		return model;
	}

	private class MimicAction implements Runnable {

		private String action;
		private DShapeModel model;

		public MimicAction(String action, DShapeModel model) {
			this.action = action;
			this.model = model;
		}

		@Override
		public void run() {
			if (action.equals(NetworkManager.ADD_SHAPE)) {
				canvas.addShape(model);
			} else if (action.equals(NetworkManager.REMOVE_SHAPE)) {
				canvas.removeShape(model);
			} else if (action.equals(NetworkManager.SEND_TO_FRONT)) {
				canvas.sendToFront(model);
			} else if (action.equals(NetworkManager.SEND_TO_BACK)) {
				canvas.sendToBack(model);
			} else if (action.equals(NetworkManager.UPDATE_MODEL)) {
				canvas.updateModel(model);
			} else if (action.equals(NetworkManager.CLEAR)) {
				canvas.clear();
			}
		}

	}

	@Override
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			while (true) {
				String action = (String) ois.readObject();
				String obj = (String) ois.readObject();
				DShapeModel model = null;
				if (!action.equals(NetworkManager.CLEAR)) {
					model = decode(obj);
				}
				java.awt.EventQueue.invokeLater(new MimicAction(action, model));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Connection error, client will exit now...");
			System.exit(-1);
		}
	}

}
