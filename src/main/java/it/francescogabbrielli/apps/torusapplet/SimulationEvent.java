/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.francescogabbrielli.apps.torusapplet;

import java.util.EventObject;

/**
 *
 * @author Francesco Gabbrielli
 */
public class SimulationEvent extends EventObject {

	public enum Property {
		DENSITY, TEMPERATURE, TRANSFORM, N_ATOMS
	}

	private Property property ;

	public SimulationEvent(SimulationSystem source) {
		super(source) ;
	}

	public SimulationEvent(SimulationSystem source, Property p) {
		super(source) ;
		this.property = p ;
	}

	SimulationSystem getSystem() {
		return (SimulationSystem) getSource() ;
	}

	public Property getProperty() {
		return property;
	}

}
