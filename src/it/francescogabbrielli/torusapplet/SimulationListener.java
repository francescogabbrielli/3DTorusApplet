/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.francescogabbrielli.torusapplet;

import java.util.EventListener;

/**
 *
 * @author Francesco Gabbrielli
 */
public interface SimulationListener extends EventListener {

	/** Notifies the simulation has been re-inited */
	public void init(SimulationEvent evt) ;

	/** Notifies the simulation is running and updating atoms */
	public void update(SimulationEvent evt) ;

	/** Notifies one of the simulation parameters has been changed */
	public void changed(SimulationEvent evt) ;

}
