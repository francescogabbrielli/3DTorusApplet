package it.francescogabbrielli.apps.torusapplet ;

import java.awt.*;

public class EnergyPanel extends Panel implements SimulationListener {

    private LinePlot plot;
    private static final double NA = 6.0221367e23;
    private static final double e = 1.60217733e-19;

	public EnergyPanel() {
		plot = new LinePlot();
		setLayout(new BorderLayout());
		setFont(new Font("TimesRoman",Font.PLAIN,14));
		add("North",new Label("Energy Plot",Label.CENTER));
		add("West",new Label("E",Label.RIGHT));
		add("South",new Label("timestep",Label.CENTER));
		add("Center",plot);
    }

    private double conv2ev(double n) {
		return (n * (10.0 / ( e * NA )));
    }

	public void update(SimulationEvent evt) {
		if(isEnabled())
			plot.plotData(conv2ev(evt.getSystem().te));
	}

	public void init(SimulationEvent evt) {}
	public void changed(SimulationEvent evt) {}

}
