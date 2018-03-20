package cs151.whiteboard;

public class DTextModel extends DShapeModel {

	private static final long serialVersionUID = 1L;

	private String text;

	private String fontName;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		notifyModelListeners();
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
		notifyModelListeners();
	}

}
