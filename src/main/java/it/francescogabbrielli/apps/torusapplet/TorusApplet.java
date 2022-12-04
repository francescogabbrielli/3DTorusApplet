package it.francescogabbrielli.apps.torusapplet;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 *
 * @author Francesco Gabbrielli
 */
public class TorusApplet extends JApplet {

	private TorusAppletPanel panel ;

	private TorusAppletMenuBar menubar ;

	public TorusApplet() {
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }

		panel = new TorusAppletPanel();
		panel.setApplet(this);
		panel.setSimulation(new SimulationThread());
		getContentPane().add(panel);

		menubar = new TorusAppletMenuBar() ;
		setJMenuBar(menubar);
	}

	TorusAppletMenuBar getMenu() {
		return (TorusAppletMenuBar) getJMenuBar() ;
	}

	TorusAppletPanel getPanel() {
		return panel ;
	}

	SimulationSystem getSystem() {
		return panel.getSystem() ;
	}

	SimulationThread getSimulation() {
		return panel.getSimulation() ;
	}

    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
	@Override
    public void init() {
		if(!(getParent() instanceof JComponent))
			menubar.initApplet() ;
    }

	@Override
	public void stop() {
		menubar.close() ;
		getSimulation().close() ;
	}

}
