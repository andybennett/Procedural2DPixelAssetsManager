package ajb.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import ajb.domain.Asset;
import ajb.domain.Pixel;
import ajb.framework.Base2DFramework;
import ajb.utils.PixelGridUtils;

public class DesignPanel extends Base2DFramework {

	private Asset asset = null;
	private JPanel caller = null;

	public DesignPanel() {
		super();
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public void setCaller(JPanel caller) {
		this.caller = caller;
	}

	@Override
	public void paint(Graphics g1) {
		super.paint(g1);

		Graphics2D g = (Graphics2D) g1;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setStroke(new BasicStroke(1f));

		int x = 0;
		int y = 0;

		for (int r = 0; r < asset.getGrid().length; r++) {
			for (int c = 0; c < asset.getGrid()[0].length; c++) {

				if (asset.getGrid()[r][c].value == Pixel.EMPTY) {
					g.setColor(background);
				} else if (asset.getGrid()[r][c].value == Pixel.BORDER) {
					g.setColor(Color.BLACK);
				} else if (asset.getGrid()[r][c].value == Pixel.FILLED) {
					g.setColor(asset.getPrimaryColor());
				} else if (asset.getGrid()[r][c].value == Pixel.SECONDARY) {
					g.setColor(asset.getSecondaryColor());
				}

				g.fillRect(x, y, 20, 20);

				g.setColor(Color.decode("#2A2A2A"));
				g.drawRect(x, y, 20, 20);
				x += 20;
			}

			y += 20;
			x = 0;
		}		
	}
	
	

	@Override
	public void drawBeforeTransform(Graphics2D g) {
		g.drawImage(asset.getImg(), this.getWidth() - asset.getImg().getWidth(), 0, null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		
		try {
			Point2D.Double point = transformPoint(e.getPoint());
			
			int y = (int) (point.x / 20);
			int x = (int) (point.y / 20);
			
			if (y == 0 || x == 0 || y == (asset.getGrid()[0].length - 1) || x == (asset.getGrid().length - 1)) {
				return;
			}
			
			if (asset.getGrid()[x][y] != null) {
			
				if (e.getButton() == MouseEvent.BUTTON1) {
					
					if (asset.getGrid()[x][y].value == Pixel.EMPTY) {
						asset.getGrid()[x][y].value = Pixel.FILLED;
					} else if (asset.getGrid()[x][y].value == Pixel.FILLED) {
						asset.getGrid()[x][y].value = Pixel.SECONDARY;
					} else if (asset.getGrid()[x][y].value == Pixel.SECONDARY) {
						asset.getGrid()[x][y].value = Pixel.EMPTY;
					} else if (asset.getGrid()[x][y].value == Pixel.BORDER) {
						asset.getGrid()[x][y].value = Pixel.FILLED;
					} 
	
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					asset.getGrid()[x][y].value = Pixel.EMPTY;
				}
				
				PixelGridUtils.removePixelsByType(asset.getGrid(), Pixel.BORDER);
				PixelGridUtils.addBorders(asset.getGrid());
				PixelGridUtils.setPixelDepth(asset.getGrid());
				asset.redrawImg();
				
				repaint();
				caller.repaint();
			}
			
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			//aioobe.printStackTrace();
		} catch (NoninvertibleTransformException e1) {
			e1.printStackTrace();
		}
	}
}
