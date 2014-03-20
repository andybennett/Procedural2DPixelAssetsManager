package ajb.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import ajb.app.App;
import ajb.domain.Asset;
import ajb.domain.Pixel;
import ajb.enums.AssetSize;
import ajb.enums.AssetType;
import ajb.factory.AsteroidGeneratorFactory;
import ajb.framework.Base2DFramework;
import ajb.utils.ColorUtils;
import ajb.utils.ImageUtils;
import ajb.utils.XStreamUtils;

public class AsteroidsPanel extends Base2DFramework implements ActionListener {

	private App app;

	AsteroidGeneratorFactory factory = new AsteroidGeneratorFactory();
	List<Asset> assets = new ArrayList<Asset>();

	Asset selectedAsset = null;
	
	public AsteroidsPanel(App app) {
		super();
		super.allowDrag = false;
		
		this.app = app;
		createAssets(20);
	}
	
	private void createAssets(int amount) {

		assets.clear();

		Color primaryColor = Color.decode("#2A2A2A");
		
		for (int i = 0; i < amount; i++) {

			Pixel[][] grid = factory.create(AssetSize.MEDIUM);

			Asset asset = new Asset(null, AssetType.ASTEROID, grid, primaryColor, Color.decode(ColorUtils.getRandomColour()));

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

		selectedAsset = null;
		
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
			
			if (selectedAsset != null) {
				JSeparator separator = new JSeparator();
				menu.add(separator);
				
				JMenuItem save = new JMenuItem("Save as PNG");
				save.addActionListener(this);
				menu.add(save);
				
				JMenuItem saveXML = new JMenuItem("Save as XML");
				saveXML.addActionListener(this);
				menu.add(saveXML);				
			}

			menu.show(this, e.getX(), e.getY());

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Regenerate")) {
			createAssets(20);
			repaint();
		} else if (e.getActionCommand().equals("Save as PNG")) {
			ImageUtils.save(ImageUtils.createImage(selectedAsset.getGrid(), selectedAsset.getPrimaryColor(), selectedAsset.getSecondaryColor()), "png", selectedAsset.getUuid() + "_RAW");
			ImageUtils.save(selectedAsset.getImg(), "png", selectedAsset.getUuid());
		} else if (e.getActionCommand().equals("Save as XML")) {
			XStreamUtils.outputAsset(selectedAsset);
		}
	}	
}
