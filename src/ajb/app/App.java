package ajb.app;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import ajb.domain.Asset;
import ajb.panels.AsteroidsPanel;
import ajb.panels.ConsolesPanel;
import ajb.panels.DesignPanel;
import ajb.panels.StationsPanel;
import ajb.panels.TilesPanel;
import ajb.panels.VesselsPanel;

public class App {

	public final Font font = new Font("Verdana", 0, 11);
	private VesselsPanel vesselsPanel = new VesselsPanel(this);
	private AsteroidsPanel asteroidsPanel = new AsteroidsPanel(this);
	private StationsPanel stationsPanel = new StationsPanel(this);
	private TilesPanel tilesPanel = new TilesPanel(this);
	private ConsolesPanel consolesPanel = new ConsolesPanel(this);
	
	private JFrame designerFrame = null;
	private DesignPanel designPanel = null;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		App app = new App();		
	}
	
	public void showDesignerFrame(Asset asset, JPanel caller) {
		if (designerFrame == null) {
			designerFrame = new JFrame();			
			designerFrame.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
			designerFrame.setSize(800, 600);
			designerFrame.setLocationRelativeTo(null);			
		}
		
		designerFrame.setTitle("Designing " + asset.getUuid());
		
		if (designPanel != null) {
			designerFrame.remove(designPanel);
		}		
		
		designPanel = new DesignPanel();
		designPanel.setAsset(asset);
		designPanel.setCaller(caller);

		designerFrame.add(designPanel);
		designerFrame.repaint();
		designerFrame.setVisible(true);
		designerFrame.requestFocus();
	}

	public App() {
		super();

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame();
		frame.setTitle("Procedural 2D Pixel Assets Manager");
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(1024, 768);
		frame.setLocationRelativeTo(null);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(font);
		tabbedPane.setBackground(Color.decode("#1E1E1E"));
		tabbedPane.setBorder(null);
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		tabbedPane.add("Vessels", vesselsPanel);
		tabbedPane.add("Asteroids", asteroidsPanel);
		tabbedPane.add("Stations", stationsPanel);
		tabbedPane.add("Tiles", tilesPanel);
		tabbedPane.add("Consoles", consolesPanel);

		frame.add(tabbedPane);

		frame.setVisible(true);
	}
}
