package ajb.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ajb.app.App;
import ajb.domain.Asset;
import ajb.domain.Pixel;
import ajb.enums.AssetType;
import ajb.factory.StationGeneratorFactory;
import ajb.framework.Base2DFramework;
import ajb.utils.ColorUtils;

public class StationsPanel extends Base2DFramework implements ActionListener {

	private App app;

	StationGeneratorFactory factory = new StationGeneratorFactory();
	List<Asset> assets = new ArrayList<Asset>();

	Asset selectedAsset = null;
	
	public StationsPanel(App app) {
		super();
		super.allowDrag = false;

		this.app = app;
		createAssets(100);
	}
	
	private void createAssets(int amount) {

		assets.clear();

		Color primaryColor = Color.decode("#2A2A2A");
		
		for (int i = 0; i < amount; i++) {

			Pixel[][] grid = factory.create();

			Asset asset = new Asset(null, AssetType.VESSEL, grid, primaryColor, Color.decode(ColorUtils.getRandomColour()));

			// add asset to list
			assets.add(asset);
		}

	}	

	@Override
	public void paint(Graphics g1) {
		super.paint(g1);
		
		Graphics2D g = (Graphics2D) g1;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Background
		g.setColor(background);
		Dimension d = getSize();
		g.fillRect(0, 0, (int) d.getWidth(), (int) d.getHeight());

		// Other drawing here

		int x = 10;
		int y = 10;
		int maxYForLine = 0;

		for (Asset asset : assets) {

			if (x + (asset.getImg().getWidth() + 10) > this.getWidth()) {
				x = 10;
				y += maxYForLine + 10;
				maxYForLine = 0;
			}

			if (y + (asset.getImg().getHeight() + 10) > this.getHeight()) {
				continue;
			}

			g.drawImage(asset.getImg(), x, y, null);
			asset.getBounds().setLocation(x, y);

			x += asset.getImg().getWidth() + 10;

			if (asset.getImg().getHeight() > maxYForLine) {
				maxYForLine = asset.getImg().getHeight();
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);

		for (Asset asset : assets) {
			try {
				if (asset.getBounds().contains(this.transformPoint(e.getPoint()))) {
					selectedAsset = asset;					
					break;
				}
			} catch (NoninvertibleTransformException e1) {
				e1.printStackTrace();
			}
		}		
		
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
			
			if (selectedAsset != null) {
				app.showDesignerFrame(selectedAsset, this);
			}
			
		} else if (e.getButton() == MouseEvent.BUTTON3) {

			JPopupMenu menu = new JPopupMenu();
			menu.setFont(app.font);			

			JMenuItem item = new JMenuItem("Regenerate");
			item.addActionListener(this);

			menu.add(item);
			menu.show(this, e.getX(), e.getY());

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Regenerate")) {
			createAssets(100);
			repaint();
		}
	}	
}