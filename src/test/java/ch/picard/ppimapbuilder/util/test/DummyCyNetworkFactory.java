/*   
 * This file is part of PPiMapBuilder.
 *
 * PPiMapBuilder is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PPiMapBuilder is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PPiMapBuilder.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2015 Echeverria P.C., Dupuis P., Cornut G., Gravouil K., Kieffer A., Picard D.
 * 
 */    	
    
package ch.picard.ppimapbuilder.util.test;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.SavePolicy;

import java.util.ArrayList;
import java.util.List;

public class DummyCyNetworkFactory implements CyNetworkFactory {

	private final List<DummyCyNetwork> networks = new ArrayList<DummyCyNetwork>();

	@Override
	public CyNetwork createNetwork() {
		DummyCyNetwork network = new DummyCyNetwork();
		networks.add(network);
		return network;
	}

	@Override
	public CyNetwork createNetwork(SavePolicy savePolicy) {
		return createNetwork();
	}

	@Override
	public CyNetwork createNetworkWithPrivateTables() {
		return createNetwork();
	}

	@Override
	public CyNetwork createNetworkWithPrivateTables(SavePolicy savePolicy) {
		return createNetwork();
	}

	public List<DummyCyNetwork> getNetworks() {
		return networks;
	}
}
