/*
 * TorusAppletPanel.java
 *
 * Created on 22-nov-2010, 17.46.37
 */

package it.francescogabbrielli.torusapplet;

import java.awt.event.KeyEvent;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 *
 * @author Francesco Gabbrielli
 */
public class TorusAppletPanel extends javax.swing.JPanel implements SimulationListener {

	private final static double DENSITY_SLIDER_TICK = 0.001d ;

	private TorusApplet applet ;

	private SimulationThread simulation ;
	private SimulationSystem system ;

    /** Creates new form TorusAppletPanel */
    public TorusAppletPanel() {
		initComponents();
    }

	public SimulationPanel getSimulationPanel() {
		return simulationPanel;
	}

	void setApplet(TorusApplet applet) {
		this.applet = applet ;
		simulationPanel.setApplet(applet) ;
	}

	public SimulationSystem getSystem() {
		return system ;
	}

	public SimulationThread getSimulation() {
		return simulation ;
	}

	public void setSimulation(SimulationThread st) {
		this.simulation = st ;
		system = st.getSystem() ;

		//------------------------ init values --------------------------
		scalingCheck.setSelected(system.isScaling());//scaling
		init(new SimulationEvent(system)) ;//atoms
		changed(new SimulationEvent(system, SimulationEvent.Property.N_ATOMS)) ;//atoms
		changed(new SimulationEvent(system, SimulationEvent.Property.DENSITY)) ;//density
		changed(new SimulationEvent(system, SimulationEvent.Property.TEMPERATURE)) ;//temperature
//
//		//ratio's
//		ySliderField.setValue(s.getY_xRatio()) ;
//		zSliderField.setValue(s.getZ_xRatio()) ;
		tilePanel.setVisible(false) ;
		//---------------------------------------------------------------

		simulationPanel.setSystem(system) ;
		system.addSimulationListener(this) ;

		tab.removeAll() ;
		tab.add("Transform", new TransformPanel(system)) ;
		TempPanel tp = new TempPanel() ;
		system.addSimulationListener(tp) ;
		tab.add("Temperature", tp) ;
		EnergyPanel ep = new EnergyPanel() ;
		system.addSimulationListener(ep) ;
		tab.add("Energy", ep) ;
		RDFPanel rp = new RDFPanel() ;
		system.addSimulationListener(rp) ;
		tab.add("RDF", rp) ;
		tab.add("Potential", new PotentialPanel(system)) ;
		tab.setTabComponentAt(1, new TabEnabler(tab, "Temperature")) ;
		tab.setTabComponentAt(2, new TabEnabler(tab, "Energy")) ;
		tab.setTabComponentAt(3, new TabEnabler(tab, "RDF")) ;
		tab.setSelectedIndex(0);

	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panel = new javax.swing.JPanel();
        simPanel = new javax.swing.JPanel();
        perspectiveSlider = new javax.swing.JSlider();
        simulationPanel = new it.francescogabbrielli.torusapplet.SimulationPanel();
        tab = new javax.swing.JTabbedPane();
        leftPanel = new javax.swing.JPanel();
        controlScroll = new javax.swing.JScrollPane();
        controlPanel = new javax.swing.JPanel();
        tempField = new javax.swing.JTextField();
        tempLabel = new javax.swing.JLabel();
        tempSlider = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        densityField = new javax.swing.JTextField();
        densitySlider = new javax.swing.JSlider();
        scalingCheck = new javax.swing.JCheckBox();
        btnsPanel = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        atomsSlider = new javax.swing.JSlider();
        atomsLabel = new javax.swing.JLabel();
        atomsField = new javax.swing.JTextField();
        angstromLabel = new javax.swing.JLabel();
        tilePanel = new javax.swing.JPanel();
        ySliderField = new it.francescogabbrielli.torusapplet.JSliderField();
        zSliderField = new it.francescogabbrielli.torusapplet.JSliderField();
        angstromLabel2 = new javax.swing.JLabel();
        angstromLabel1 = new javax.swing.JLabel();
        yShearSliderField = new it.francescogabbrielli.torusapplet.JSliderField();
        zShearSliderField = new it.francescogabbrielli.torusapplet.JSliderField();

        setLayout(new java.awt.BorderLayout());

        panel.setLayout(new java.awt.BorderLayout());

        simPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.darkGray, java.awt.Color.darkGray));
        simPanel.setLayout(new java.awt.BorderLayout());

        perspectiveSlider.setBackground(new java.awt.Color(0, 0, 0));
        perspectiveSlider.setForeground(new java.awt.Color(204, 204, 255));
        perspectiveSlider.setMaximum(50);
        perspectiveSlider.setMinimum(10);
        perspectiveSlider.setOrientation(javax.swing.JSlider.VERTICAL);
        perspectiveSlider.setSnapToTicks(true);
        perspectiveSlider.setToolTipText("Perspective");
        perspectiveSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                perspectiveSliderStateChanged(evt);
            }
        });
        simPanel.add(perspectiveSlider, java.awt.BorderLayout.WEST);

        simulationPanel.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout simulationPanelLayout = new javax.swing.GroupLayout(simulationPanel);
        simulationPanel.setLayout(simulationPanelLayout);
        simulationPanelLayout.setHorizontalGroup(
            simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 442, Short.MAX_VALUE)
        );
        simulationPanelLayout.setVerticalGroup(
            simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 529, Short.MAX_VALUE)
        );

        simPanel.add(simulationPanel, java.awt.BorderLayout.CENTER);

        panel.add(simPanel, java.awt.BorderLayout.CENTER);
        panel.add(tab, java.awt.BorderLayout.SOUTH);

        add(panel, java.awt.BorderLayout.CENTER);

        leftPanel.setLayout(new java.awt.GridBagLayout());

        controlScroll.setBorder(javax.swing.BorderFactory.createTitledBorder("Controls"));

        controlPanel.setMinimumSize(new java.awt.Dimension(200, 264));
        controlPanel.setLayout(new java.awt.GridBagLayout());

        tempField.setColumns(5);
        tempField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tempFieldKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        controlPanel.add(tempField, gridBagConstraints);

        tempLabel.setText("Applied Temp:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        controlPanel.add(tempLabel, gridBagConstraints);

        tempSlider.setMajorTickSpacing(500);
        tempSlider.setMaximum(1500);
        tempSlider.setMinimum(1);
        tempSlider.setMinorTickSpacing(100);
        tempSlider.setPaintTicks(true);
        tempSlider.setPreferredSize(new java.awt.Dimension(120, 24));
        tempSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tempSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        controlPanel.add(tempSlider, gridBagConstraints);

        jLabel2.setText("Density:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        controlPanel.add(jLabel2, gridBagConstraints);

        densityField.setColumns(5);
        densityField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                densityFieldKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        controlPanel.add(densityField, gridBagConstraints);

        densitySlider.setMaximum(150);
        densitySlider.setMinimum(1);
        densitySlider.setMinorTickSpacing(10);
        densitySlider.setPaintTicks(true);
        densitySlider.setValue(12);
        densitySlider.setPreferredSize(new java.awt.Dimension(120, 24));
        densitySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                densitySliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        controlPanel.add(densitySlider, gridBagConstraints);

        scalingCheck.setText("Velocity Scaling");
        scalingCheck.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                scalingCheckStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        controlPanel.add(scalingCheck, gridBagConstraints);

        startButton.setText("Start");
        startButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });
        btnsPanel.add(startButton);

        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        stopButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        btnsPanel.add(stopButton);

        resetButton.setText("Reset");
        resetButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        btnsPanel.add(resetButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        controlPanel.add(btnsPanel, gridBagConstraints);

        atomsSlider.setMajorTickSpacing(50);
        atomsSlider.setMinimum(1);
        atomsSlider.setMinorTickSpacing(10);
        atomsSlider.setPaintTicks(true);
        atomsSlider.setValue(20);
        atomsSlider.setPreferredSize(new java.awt.Dimension(120, 24));
        atomsSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                atomsSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        controlPanel.add(atomsSlider, gridBagConstraints);

        atomsLabel.setText("Atoms:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        controlPanel.add(atomsLabel, gridBagConstraints);

        atomsField.setColumns(5);
        atomsField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                atomsFieldKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        controlPanel.add(atomsField, gridBagConstraints);

        angstromLabel.setForeground(new java.awt.Color(204, 0, 0));
        angstromLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        angstromLabel.setText("Angstrom X");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        controlPanel.add(angstromLabel, gridBagConstraints);

        controlScroll.setViewportView(controlPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        leftPanel.add(controlScroll, gridBagConstraints);

        tilePanel.setMinimumSize(new java.awt.Dimension(200, 148));
        tilePanel.setLayout(new java.awt.GridBagLayout());

        //jSliderField1.setTickScale(0.01) ;
        ySliderField.setBorder(javax.swing.BorderFactory.createTitledBorder("Y Ratio"));
        ySliderField.setColumns(4);
        ySliderField.setMax(2.0);
        ySliderField.setMin(0.5);
        ySliderField.setMinorTickSpacing(5);
        ySliderField.setOrientation(1);
        ySliderField.setPreferredSize(new java.awt.Dimension(98, 90));
        ySliderField.setTickScale(0.01);
        ySliderField.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ySliderFieldStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        tilePanel.add(ySliderField, gridBagConstraints);

        //jSliderField1.setTickScale(0.01) ;
        zSliderField.setBorder(javax.swing.BorderFactory.createTitledBorder("Z Ratio"));
        zSliderField.setColumns(4);
        zSliderField.setMax(2.0);
        zSliderField.setMin(0.5);
        zSliderField.setMinorTickSpacing(5);
        zSliderField.setOrientation(1);
        zSliderField.setPreferredSize(new java.awt.Dimension(98, 90));
        zSliderField.setTickScale(0.01);
        zSliderField.setValue(1.2);
        zSliderField.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                zSliderFieldStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        tilePanel.add(zSliderField, gridBagConstraints);

        angstromLabel2.setForeground(new java.awt.Color(204, 0, 0));
        angstromLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        angstromLabel2.setText("Angstrom Z");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        tilePanel.add(angstromLabel2, gridBagConstraints);

        angstromLabel1.setForeground(new java.awt.Color(204, 0, 0));
        angstromLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        angstromLabel1.setText("Angstrom Y");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        tilePanel.add(angstromLabel1, gridBagConstraints);

        yShearSliderField.setBorder(javax.swing.BorderFactory.createTitledBorder("Y Shear"));
        yShearSliderField.setMajorTickSpacing(15);
        yShearSliderField.setMax(90.0);
        yShearSliderField.setMin(1.0);
        yShearSliderField.setMinorTickSpacing(5);
        yShearSliderField.setPreferredSize(new java.awt.Dimension(200, 58));
        yShearSliderField.setValue(90.0);
        yShearSliderField.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                yShearSliderFieldStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        tilePanel.add(yShearSliderField, gridBagConstraints);

        zShearSliderField.setBorder(javax.swing.BorderFactory.createTitledBorder("Z Shear"));
        zShearSliderField.setMajorTickSpacing(15);
        zShearSliderField.setMax(90.0);
        zShearSliderField.setMin(1.0);
        zShearSliderField.setMinorTickSpacing(5);
        zShearSliderField.setPreferredSize(new java.awt.Dimension(200, 58));
        zShearSliderField.setValue(90.0);
        zShearSliderField.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                zShearSliderFieldStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        tilePanel.add(zShearSliderField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        leftPanel.add(tilePanel, gridBagConstraints);

        add(leftPanel, java.awt.BorderLayout.WEST);
    }// </editor-fold>//GEN-END:initComponents

	private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
		start() ;
	}//GEN-LAST:event_startButtonActionPerformed

	private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
		stop() ;
	}//GEN-LAST:event_stopButtonActionPerformed

	private void densitySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_densitySliderStateChanged
		if((int) (system.getDensity() / DENSITY_SLIDER_TICK) != densitySlider.getValue())
			system.setDensity(densitySlider.getValue() * DENSITY_SLIDER_TICK) ;
	}//GEN-LAST:event_densitySliderStateChanged

	private void perspectiveSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_perspectiveSliderStateChanged
		simulationPanel.setPerspective(Math.exp(perspectiveSlider.getValue()*.1d));
	}//GEN-LAST:event_perspectiveSliderStateChanged

	private void atomsSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_atomsSliderStateChanged
		system.setAtomsNumber(atomsSlider.getValue()) ;
	}//GEN-LAST:event_atomsSliderStateChanged

	private void atomsFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_atomsFieldKeyReleased
		if(evt.getKeyCode()==KeyEvent.VK_ENTER) {
			String old = atomsField.getText() ;
			try {
				system.setAtomsNumber(Integer.parseInt(atomsField.getText())) ;
			} catch(Exception e) {
				atomsField.setText(old) ;
			}
		}
	}//GEN-LAST:event_atomsFieldKeyReleased

	private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
		reset() ;
	}//GEN-LAST:event_resetButtonActionPerformed

	private void scalingCheckStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_scalingCheckStateChanged
		system.setScaling(scalingCheck.isSelected());
	}//GEN-LAST:event_scalingCheckStateChanged

	private void tempSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tempSliderStateChanged
		system.setApplTemp(tempSlider.getValue()) ;
	}//GEN-LAST:event_tempSliderStateChanged

	private void tempFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tempFieldKeyReleased
		if(evt.getKeyCode()==KeyEvent.VK_ENTER) {
			String old = tempField.getText() ;
			try {
				system.setApplTemp(Double.parseDouble(tempField.getText())) ;
			} catch(Exception e) {
				tempField.setText(old) ;
			}
		}
	}//GEN-LAST:event_tempFieldKeyReleased

	private void densityFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_densityFieldKeyReleased
		if(evt.getKeyCode()==KeyEvent.VK_ENTER) {
			String old = densityField.getText() ;
			try {
				system.setDensity(Double.parseDouble(densityField.getText())) ;
			} catch(Exception e) {
				densityField.setText(old) ;
			}
		}
	}//GEN-LAST:event_densityFieldKeyReleased

	private void ySliderFieldStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ySliderFieldStateChanged
		//simulation.setY_xRatio(ySliderField.getValue()) ;
	}//GEN-LAST:event_ySliderFieldStateChanged

	private void zSliderFieldStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_zSliderFieldStateChanged
		//simulation.setZ_xRatio(zSliderField.getValue()) ;
	}//GEN-LAST:event_zSliderFieldStateChanged

	private void yShearSliderFieldStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_yShearSliderFieldStateChanged
		double angle = (90 - yShearSliderField.getValue()) * Math.PI / 180d ;
		//simulation.setYShear(angle);
	}//GEN-LAST:event_yShearSliderFieldStateChanged

	private void zShearSliderFieldStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_zShearSliderFieldStateChanged
		double angle = (90 - zShearSliderField.getValue()) * Math.PI / 180d ;
		//simulation.setZShear(angle);
	}//GEN-LAST:event_zShearSliderFieldStateChanged

	public boolean isStarted() {
		return startButton.isEnabled() ;
	}

	public void toggle() {
		if(startButton.isEnabled())
			start() ;
		else
			stop() ;
	}

	public void start() {
		if(!simulation.isAlive())
			simulation.start();
		else
			simulation.setRunning(true);
		enableAtoms(false) ;
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
		applet.getMenu().getStartItem().setText("Stop"); ;
	}

	public void stop() {
		simulation.setRunning(false);
		enableAtoms(true) ;
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		applet.getMenu().getStartItem().setText("Start"); ;
	}

	public void reset() {
		if(simulation.isRunning())
			stop() ;
		system.reset();
	}

	private void enableAtoms(boolean enabled) {
		atomsField.setEnabled(enabled);
		atomsSlider.setEnabled(enabled);
	}

	private void displayAngstroms() {
		angstromLabel.setText(String.format(Locale.US, "X = %3.4f Angstroms",system.getRadius()));
		//angstromLabel1.setText(String.format(Locale.US, "Y = %3.4f",r.y));
		//angstromLabel2.setText(String.format(Locale.US, "Z = %3.4f",r.z));
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel angstromLabel;
    private javax.swing.JLabel angstromLabel1;
    private javax.swing.JLabel angstromLabel2;
    private javax.swing.JTextField atomsField;
    private javax.swing.JLabel atomsLabel;
    private javax.swing.JSlider atomsSlider;
    private javax.swing.JPanel btnsPanel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JScrollPane controlScroll;
    private javax.swing.JTextField densityField;
    private javax.swing.JSlider densitySlider;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel panel;
    private javax.swing.JSlider perspectiveSlider;
    private javax.swing.JButton resetButton;
    private javax.swing.JCheckBox scalingCheck;
    private javax.swing.JPanel simPanel;
    private it.francescogabbrielli.torusapplet.SimulationPanel simulationPanel;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JTabbedPane tab;
    private javax.swing.JTextField tempField;
    private javax.swing.JLabel tempLabel;
    private javax.swing.JSlider tempSlider;
    private javax.swing.JPanel tilePanel;
    private it.francescogabbrielli.torusapplet.JSliderField yShearSliderField;
    private it.francescogabbrielli.torusapplet.JSliderField ySliderField;
    private it.francescogabbrielli.torusapplet.JSliderField zShearSliderField;
    private it.francescogabbrielli.torusapplet.JSliderField zSliderField;
    // End of variables declaration//GEN-END:variables


	public void update(SimulationEvent evt) {
		//nothing to update
	}

	public void init(SimulationEvent evt) {
		displayAngstroms();
		//reset doesn't change density and temperature but may result in atoms change
		//(could manage this differently: make atoms an on-line feature with random placement)
	}

	public void changed(SimulationEvent evt) {
		switch(evt.getProperty()) {
			case DENSITY:
				String txt = String.format(Locale.US, "%3.3f", evt.getSystem().getDensity()) ;
				densitySlider.setValue((int) (evt.getSystem().getDensity() / DENSITY_SLIDER_TICK)) ;
				densitySlider.setToolTipText(txt) ;
				densityField.setText(txt) ;
				displayAngstroms();
				break ;
			case TEMPERATURE:
				int t = (int) evt.getSystem().getApplTemp() ;
				tempSlider.setValue(t) ;
				tempSlider.setToolTipText(String.valueOf(t)) ;
				tempField.setText(String.valueOf(t)) ;
				displayAngstroms();
				break ;
			case N_ATOMS:
				int n = system.getAtomsNumber() ;
				atomsSlider.setValue(n);
				atomsSlider.setToolTipText(String.valueOf(n));
				atomsField.setText(String.valueOf(n)) ;
				break ;
		}
	}

	void about() {
		JOptionPane.showMessageDialog(this, "Based on Java Molecular Dynamics applet by David Wolff\nFrancesco Gabbrielli, 2010", "3D Torus Applet v"+TorusApplet.VERSION, JOptionPane.INFORMATION_MESSAGE);
	}

	void help() {
		JOptionPane.showMessageDialog(this, 
				"Alter parameters and start a molecular simulation in a 3D torus:\n"+
				"- Atoms: total number of atoms in the region\n"+
				"- Applied temp: temperature applied to the region\n"+
				"- Density: number of atoms per region unit. This is fixed if you alter the total number\n"+
				"- Y/Z Ratio: ratio of y (i.e. z) dimension with respect to x dimension\n"+
				"- Shear: strecth the region along y axis by a variable angle"+
				"",
				"Mini-Help",
				JOptionPane.INFORMATION_MESSAGE);
	}

}
