package it.francescogabbrielli.apps.torusapplet ;

import java.awt.*;

public class PotentialPanel extends Panel {

    private LinePlot plot;
    private TextField epsField,sigField,massField,cutField;
    private Button applButton,graphButton,defButton;
    private double[] data;
    private SimulationSystem s;
    private Font f = new Font("TimesRoman",Font.BOLD,14);
    private static final double NA = 6.0221367e23;
    private static final double e = 1.60217733e-19;
    private Color tffg = new Color(0,0,0);
    private Font tffont = new Font("TimesRoman",Font.BOLD,12);

    public PotentialPanel(SimulationSystem s) {
		this.s = s;
		data = new double[100];
		setLayout(new BorderLayout());

		Panel pan = new Panel();
		pan.setLayout(new GridLayout(1,6));

		Label l1 = new Label("Epsilon:",Label.RIGHT);
		l1.setFont(f);
		//l1.setBackground(labelColor);
		pan.add(l1);

		epsField = new TextField(10);
		epsField.setText(double2string(conv2ev(s.getEpsilon())));
		epsField.setForeground(tffg);
		//epsField.setBackground(tfbg);
		epsField.setFont(tffont);
		pan.add(epsField);

		Label l2 = new Label("Sigma:",Label.RIGHT);
		l2.setFont(f);
		//l2.setBackground(labelColor);
		pan.add(l2);

		sigField = new TextField(10);
		sigField.setText(double2string(s.getSigma()));
		sigField.setForeground(tffg);
		//sigField.setBackground(tfbg);
		sigField.setFont(tffont);
		pan.add(sigField);

		Label l3 = new Label("Cutoff:",Label.RIGHT);
		l3.setFont(f);
		//l3.setBackground(labelColor);
		pan.add(l3);

		cutField = new TextField(10);
		cutField.setText(double2string(s.getCutoff()));
		cutField.setForeground(tffg);
		//cutField.setBackground(tfbg);
		cutField.setFont(tffont);
		pan.add(cutField);

		add("North",pan);

		plot = new LinePlot();
		add("Center",plot);

		Label l4 = new Label("V(r)",Label.RIGHT);
		add("West",l4);

		Panel p = new Panel();
		p.setLayout(new FlowLayout());

		applButton = new Button("Apply");
		p.add(applButton);

		graphButton = new Button("Graph");
		p.add(graphButton);

		defButton = new Button("Defaults");
		p.add(defButton);

		add("South",p);
    }

    private String double2string(double n) {
		String str = (new Double(n)).toString();
		int len = str.length();
		if(len > 8) len = 8;
		return str.substring(0,len);
    }
    
    private double string2double(String str) {
		Double d;
		try {
			d = Double.valueOf(str);
		} catch(NumberFormatException e) {
			System.out.println("Ivalid format.");
			return 0.0;
		}
		return (d.doubleValue());
    }

    private double calcPot(double r, double eps, double sig) {
		double ri2,ri6,ri12;

		ri2 = 1.0 / (r * r);
		ri6 = sig * sig * ri2;
		ri6 = ri6 * ri6 * ri6;
		ri12 = ri6 * ri6;
		return ( eps * (ri12 - ri6) );
    }

	@Override
    public boolean action(Event e,Object arg) {
		// Handle the buttons
		if(e.target instanceof Button) {
			if(arg.equals("Graph")) {
			plotPotential();
			return true;
			}
			if(arg.equals("Apply")) {
			applParameters();
			return true;
			}
			if(arg.equals("Defaults")) {
			setDefaults();
			return true;
			}
		}
		return true;
    }

    private void plotPotential() {
		int len = data.length,i;
		double r;
		double deltax,eps,sig;
		double max = 0.01;

		deltax = string2double(cutField.getText()) / len;
		eps = ev2pu(string2double(epsField.getText()));
		sig = string2double(sigField.getText());

		for(i = 1; i < len; i++) {
			r = (double)i * deltax;
			data[i] = conv2ev(calcPot(r,eps,sig));
		}

		i = 1;
		while(data[i] > max) i++;
		plot.plotData(data,deltax,0.0,i);
		plot.drawYZero();

    }

    private void applParameters(){
		s.setSigma(string2double(sigField.getText()));
		s.setEpsilon(ev2pu(string2double(epsField.getText())));
		s.setCutoff(string2double(cutField.getText()));
    }

    private void setDefaults() {
		epsField.setText(double2string(conv2ev(398.4)));
		sigField.setText("3.41");
		cutField.setText("8.5");
    }

    private double conv2ev(double n) {
		return (n * (10.0 / ( e * NA )));
    }

    private double ev2pu(double n) {
		return (n * ( (e * NA) / 10.0 ));
    }

}
