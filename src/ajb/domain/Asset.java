package ajb.domain;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.UUID;

import ajb.enums.AssetType;
import ajb.utils.ImageUtils;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class Asset {

	private AssetType type;
	
	@XStreamOmitField
	private BufferedImage img;
	@XStreamOmitField
	private Rectangle bounds;
	
	private Pixel[][] grid;
	private Color primaryColor;
	private Color secondaryColor;

	private String uuid = UUID.randomUUID().toString();
	private String name;

	public Asset(String name, AssetType type, Pixel[][] grid, Color primaryColor, Color secondaryColor) {
		this.name = name;
		this.type = type;
		this.grid = grid;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
	}
	
	private void calculateBounds() {
		bounds = new Rectangle(this.getImg().getWidth() -1, this.getImg().getHeight() -1);
	}
	
	public BufferedImage getImg() {
		
		if (img == null) {
			img = ImageUtils.outputToImage(grid, primaryColor, secondaryColor);
		}
		
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;		
	}
	
	public void redrawImg() {
		img = ImageUtils.outputToImage(grid, primaryColor, secondaryColor);
	}	

	public Rectangle getBounds() {
		if (bounds == null) {
			calculateBounds();
		}
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AssetType getType() {
		return type;
	}

	public void setType(AssetType type) {
		this.type = type;
	}

	public Pixel[][] getGrid() {
		return grid;
	}

	public void setGrid(Pixel[][] grid) {
		this.grid = grid;
	}

	public Color getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(Color primaryColor) {
		this.primaryColor = primaryColor;
	}

	public Color getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(Color secondaryColor) {
		this.secondaryColor = secondaryColor;
	}	
}
