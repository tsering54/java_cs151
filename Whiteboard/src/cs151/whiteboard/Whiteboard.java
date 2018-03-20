package cs151.whiteboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Whiteboard extends JFrame implements ShapeListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private Canvas canvas;
	private JTextField textField;
	private DTextModel textModel;
	private JComboBox<Object> fontComboBox;
	private DShapeTableModel tableModel;
	private boolean serverMode;
	private boolean clientMode;
	private JButton rectButton;
	private JButton ovalButton;
	private JButton lineButton;
	private JButton textButton;
	private JButton shapeColorButton;
	private JButton sendFrontButton;
	private JButton sendBackButton;
	private JButton removeShapeButton;
	private JButton saveButton;
	private JButton loadButton;
	private JButton savePngButton;
	private JLabel nwStatus;
	private JButton startServerButton;
	private JButton startClientButton;

	public Whiteboard() {
		setLayout(new BorderLayout());

		canvas = new Canvas();
		getContentPane().add(canvas, BorderLayout.CENTER);
		canvas.addShapeListener(this);

		Box controlPanel = createControlPanel();
		getContentPane().add(controlPanel, BorderLayout.WEST);
	}

	private Rectangle getDefaultBounds() {
		int x = (int) (Math.random() * 300);
		int y = (int) (Math.random() * 300);
		int w = (int) (Math.random() * 200);
		int h = (int) (Math.random() * 100);
		w = Math.max(w, 50);
		h = Math.max(h, 50);
		Rectangle bounds = new Rectangle(x, y, w, h);
		return bounds;
	}

	private void addShapeToCanvas(DShapeModel model) {
		Rectangle bounds = getDefaultBounds();
		model.setBounds(bounds);
		model.addModelListener(tableModel);
		canvas.addShape(model);
	}

	private Box createControlPanel() {
		Box verticalBox = new Box(BoxLayout.PAGE_AXIS);

		Box addShapeBox = new Box(BoxLayout.LINE_AXIS);
		verticalBox.add(addShapeBox);
		addShapeBox.add(new JLabel("Add Shapes:"));
		rectButton = new JButton("Rect");
		rectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DShapeModel model = new DRectModel();
				addShapeToCanvas(model);
			}
		});
		addShapeBox.add(rectButton);
		ovalButton = new JButton("Oval");
		ovalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DShapeModel model = new DOvalModel();
				addShapeToCanvas(model);
			}
		});
		addShapeBox.add(ovalButton);
		lineButton = new JButton("Line");
		lineButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DShapeModel model = new DLineModel();
				addShapeToCanvas(model);
			}
		});
		addShapeBox.add(lineButton);
		textButton = new JButton("Text");
		textButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DTextModel model = new DTextModel();
				model.setFontName(Font.DIALOG);
				model.setText("Hello");
				addShapeToCanvas(model);
			}
		});
		addShapeBox.add(textButton);

		Box editTextBox = new Box(BoxLayout.LINE_AXIS);
		verticalBox.add(editTextBox);
		editTextBox.add(new JLabel("Edit Text: "));
		textField = new JTextField("", 20);
		textField.setEnabled(false);
		textField.setMaximumSize(new Dimension(200, 20));
		textField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				textModel.setText(textField.getText());
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				textModel.setText(textField.getText());
			}
		});
		editTextBox.add(textField);
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		fontComboBox = new JComboBox<Object>(fonts);
		fontComboBox.setEnabled(false);
		fontComboBox.setSelectedItem("Dialog");
		fontComboBox.addActionListener(this);
		fontComboBox.setMaximumSize(new Dimension(200, 20));
		editTextBox.add(fontComboBox);

		Box shapeColorBox = new Box(BoxLayout.LINE_AXIS);
		verticalBox.add(shapeColorBox);
		shapeColorBox.add(new JLabel("Set Shape Color: "));
		shapeColorButton = new JButton("Set Color");
		shapeColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (canvas.shapeSelected()) {
					Color curColor = canvas.getSelectedShapeColor();
					Color newColor = JColorChooser.showDialog(Whiteboard.this, "Select the shape's color", curColor);
					if (newColor != null && newColor != curColor) {
						canvas.setSelectedShapeColor(newColor);
					}
				}
			}
		});
		shapeColorBox.add(shapeColorButton);

		Box editShapeBox = new Box(BoxLayout.LINE_AXIS);
		verticalBox.add(editShapeBox);
		editShapeBox.add(new JLabel("Edit Shapes:"));
		sendFrontButton = new JButton("Send to Front");
		sendFrontButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.sendToFront();
			}
		});
		editShapeBox.add(sendFrontButton);
		sendBackButton = new JButton("Send to Back");
		sendBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.sendToBack();
			}
		});
		editShapeBox.add(sendBackButton);
		removeShapeButton = new JButton("Remove This Shape");
		removeShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.removeSelected();
			}
		});
		editShapeBox.add(removeShapeButton);

		Box contentBox = new Box(BoxLayout.LINE_AXIS);
		verticalBox.add(contentBox);
		contentBox.add(new JLabel("Edit/Save Content:"));
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveXml();
			}
		});
		contentBox.add(saveButton);
		loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadXml();
			}
		});
		contentBox.add(loadButton);
		savePngButton = new JButton("Save as PNG");
		savePngButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				savePng();
			}
		});
		contentBox.add(savePngButton);

		Box nwBox = new Box(BoxLayout.LINE_AXIS);
		verticalBox.add(nwBox);
		nwBox.add(new JLabel("Networking:"));
		startServerButton = new JButton("Start Server");
		startServerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (serverMode || clientMode) {
					return;
				}

				String input = JOptionPane.showInputDialog("Enter port to listen", "39587");
				if (input == null) {
					return;
				}

				int port;
				try {
					port = Integer.parseInt(input);
				} catch (IllegalArgumentException ex) {
					showErrorMessage("Invalid port");
					return;
				}

				serverMode = true;
				NetworkManager mgr = new NetworkManager(canvas);
				canvas.setCanvasListener(mgr);

				startServerButton.setEnabled(false);
				startClientButton.setEnabled(false);

				Runnable job = new WhiteboardServer(port, mgr);
				new Thread(job).start();

				nwStatus.setText("Running in server mode");
				Whiteboard.this.setTitle("Whiteboard (SERVER)");
			}
		});
		nwBox.add(startServerButton);
		startClientButton = new JButton("Start Client");
		startClientButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (serverMode || clientMode) {
					return;
				}

				String input = JOptionPane.showInputDialog("Enter host:port to connect", "localhost:39587");
				if (input == null) {
					return;
				}

				String[] comps = input.split(":");
				if (comps.length != 2) {
					showErrorMessage("Invalid input");
					return;
				}

				int port;
				try {
					port = Integer.parseInt(comps[1]);
				} catch (IllegalArgumentException ex) {
					showErrorMessage("Invalid port");
					return;
				}

				Socket conn = null;
				try {
					conn = new Socket(comps[0], port);
				} catch (Exception ex) {
					showErrorMessage("Connection error.");
					return;
				}

				Runnable job = new WhiteboardClient(canvas, conn);

				clientMode = true;
				canvas.clear();
				canvas.enableClientMode();
				disableControls();

				new Thread(job).start();

				nwStatus.setText("Running in client mode");
				Whiteboard.this.setTitle("Whiteboard (CLIENT)");

			}
		});
		nwBox.add(startClientButton);
		nwStatus = new JLabel();
		nwBox.add(nwStatus);

		tableModel = new DShapeTableModel();
		JTable shapesTable = new JTable(tableModel);
		verticalBox.add(new JScrollPane(shapesTable));
		canvas.addShapeListener(tableModel);

		for (Component comp : verticalBox.getComponents()) {
			((JComponent) comp).setAlignmentX(Box.LEFT_ALIGNMENT);
		}

		return verticalBox;
	}

	private void showErrorMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void disableControls() {
		rectButton.setEnabled(false);
		ovalButton.setEnabled(false);
		lineButton.setEnabled(false);
		textButton.setEnabled(false);
		shapeColorButton.setEnabled(false);
		sendFrontButton.setEnabled(false);
		sendBackButton.setEnabled(false);
		removeShapeButton.setEnabled(false);
		saveButton.setEnabled(false);
		loadButton.setEnabled(false);
		savePngButton.setEnabled(false);
		startClientButton.setEnabled(false);
		startServerButton.setEnabled(false);
	}

	public static void main(String[] args) {
		Whiteboard wb = new Whiteboard();
		wb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		wb.pack();
		wb.setTitle("Whiteboard");
		wb.setVisible(true);
	}

	@Override
	public void shapeSelected(DShapeModel model) {

		if (model == textModel && !clientMode) {
			return;
		}

		fontComboBox.removeActionListener(this);

		if (model == null || !(model instanceof DTextModel)) {
			textModel = null;
			textField.setText("");
			textField.setEnabled(false);
			fontComboBox.setEnabled(false);
			fontComboBox.setSelectedItem("Dialog");
			return;
		} else {
			textModel = (DTextModel) model;
			textField.setText(textModel.getText());
			if (!clientMode) {
				textField.setEnabled(true);
				fontComboBox.setEnabled(true);
			}
			fontComboBox.setSelectedItem(textModel.getFontName());
		}

		fontComboBox.addActionListener(this);
	}

	@Override
	public void shapesChanged(List<DShapeModel> models) {

	}

	public void savePng() {
		JFileChooser jf = new JFileChooser();
		int ret = jf.showSaveDialog(this);

		if (ret == JFileChooser.APPROVE_OPTION) {

			File f = jf.getSelectedFile();

			BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			canvas.printAll(g);
			g.dispose();
			try {
				ImageIO.write(image, "png", f);
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	public void saveXml() {
		JFileChooser jf = new JFileChooser();
		int ret = jf.showSaveDialog(this);

		if (ret == JFileChooser.APPROVE_OPTION) {
			File f = jf.getSelectedFile();
			List<DShapeModel> models = canvas.getModels();
			XMLEncoder encoder;
			try {
				encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(f)));
				encoder.writeObject(models);
				encoder.close();
			} catch (FileNotFoundException e) {
				System.err.println(e);
			}
		}
	}

	public void loadXml() {
		JFileChooser jf = new JFileChooser();
		int ret = jf.showOpenDialog(this);

		if (ret == JFileChooser.APPROVE_OPTION) {
			File f = jf.getSelectedFile();
			XMLDecoder decoder;
			try {
				decoder = new XMLDecoder(new FileInputStream(f));
				@SuppressWarnings("unchecked")
				List<DShapeModel> models = (List<DShapeModel>) decoder.readObject();
				decoder.close();

				canvas.clear();
				textModel = null;

				for (DShapeModel model : models) {
					model.addModelListener(tableModel);
					canvas.addShape(model);
				}
			} catch (FileNotFoundException e) {
				System.err.println(e);
			}
		}
	}

	// only for font combo box
	@Override
	public void actionPerformed(ActionEvent e) {
		if (textModel != null) {
			String fontName = (String) fontComboBox.getSelectedItem();
			textModel.setFontName(fontName);
		}
	}

}
