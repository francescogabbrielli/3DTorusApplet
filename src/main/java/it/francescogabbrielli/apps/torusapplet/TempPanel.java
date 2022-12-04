package it.francescogabbrielli.apps.torusapplet;

import java.awt.*;

public class TempPanel extends Panel implements SimulationListener {

    private LinePlot plot;

	public TempPanel() {
		plot = new LinePlot();
		setLayout(new BorderLayout());
		setFont(new Font("TimesRoman",Font.PLAIN,14));
		add("North",new Label("Temperature Plot",Label.CENTER));
		add("West",new Label("T",Label.RIGHT));
		add("South",new Label("timestep",Label.CENTER));
		add("Center",plot);
	}

	public void update(SimulationEvent evt) {
		if(isEnabled())
			plot.plotData(evt.getSystem().temp);
	}

	public void init(SimulationEvent evt) {}

	public void changed(SimulationEvent evt) {}

}
