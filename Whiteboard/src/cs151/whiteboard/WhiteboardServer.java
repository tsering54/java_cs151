package cs151.whiteboard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class WhiteboardServer implements Runnable {

	private int port;
	private NetworkManager nwManager;

	public WhiteboardServer(int port, NetworkManager nwManager) {
		this.port = port;
		this.nwManager = nwManager;
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				Socket conn = serverSocket.accept();
				nwManager.addConnection(conn);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot start server: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
