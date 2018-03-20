package cs151.whiteboard;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkManager implements CanvasListener {

	public static String ADD_SHAPE = "add";
	public static String REMOVE_SHAPE = "remove";
	public static String SEND_TO_FRONT = "front";
	public static String SEND_TO_BACK = "back";
	public static String UPDATE_MODEL = "update";
	public static String CLEAR = "clear";

	private List<ObjectOutputStream> streams = new ArrayList<ObjectOutputStream>();
	private Canvas canvas;

	public NetworkManager(Canvas canvas) {
		this.canvas = canvas;
	}

	public synchronized void addConnection(Socket socket) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			initClientCanvas(out);
			streams.add(out);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void initClientCanvas(ObjectOutputStream out) {
		for (DShapeModel model : canvas.getModels()) {
			String obj = encode(model);
			send(out, NetworkManager.ADD_SHAPE, obj);
		}
	}

	private String encode(DShapeModel model) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(baos);
		encoder.writeObject(model);
		encoder.close();
		return new String(baos.toByteArray());
	}

	private void send(ObjectOutputStream out, String action, String obj) {
		try {
			out.writeObject(action);
			out.writeObject(obj);
		} catch (IOException e) {
			// TODO: remove broken connection
		}
	}

	private synchronized void notify(String action, DShapeModel model) {
		String obj = "";
		if (model != null) {
			obj = encode(model);
		}

		for (ObjectOutputStream out : streams) {
			send(out, action, obj);
		}
	}

	@Override
	public void addShape(DShapeModel model) {
		notify(NetworkManager.ADD_SHAPE, model);
	}

	@Override
	public void removeShape(DShapeModel model) {
		notify(NetworkManager.REMOVE_SHAPE, model);
	}

	@Override
	public void sendToFront(DShapeModel model) {
		notify(NetworkManager.SEND_TO_FRONT, model);
	}

	@Override
	public void sendToBack(DShapeModel model) {
		notify(NetworkManager.SEND_TO_BACK, model);
	}

	@Override
	public void updateModel(DShapeModel model) {
		notify(NetworkManager.UPDATE_MODEL, model);
	}

	@Override
	public void clear() {
		notify(NetworkManager.CLEAR, null);
	}

}
