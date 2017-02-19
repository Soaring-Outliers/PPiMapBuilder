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
    
package ch.picard.ppimapbuilder.networkbuilder.query;

import ch.picard.ppimapbuilder.data.interaction.Interaction;
import ch.picard.ppimapbuilder.data.organism.Organism;
import ch.picard.ppimapbuilder.data.protein.Protein;
import ch.picard.ppimapbuilder.data.protein.UniProtEntry;
import ch.picard.ppimapbuilder.data.protein.client.web.UniProtEntryClient;
import ch.picard.ppimapbuilder.networkbuilder.NetworkQueryParameters;
import ch.picard.ppimapbuilder.util.concurrent.ExecutorServiceManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.util.*;

public class PMBInteractionQueryTaskFactory extends AbstractTask {

	private final ExecutorServiceManager executorServiceManager;

	// Data input
	private final List<String> inputProteinIDs;
	private final Organism referenceOrganism;
	private final boolean interactomeQuery;
	private final UniProtEntryClient uniProtEntryClient;
	// Data output
	private final Set<UniProtEntry> proteinOfInterestPool; // not the same as user input
	private final NetworkQueryParameters networkQueryParameters;

	private final Collection<Interaction> interactions;
	private final List<Organism> otherOrganisms;
	private final List<Organism> allOrganisms;

	// Option
	private final Double MINIMUM_ORTHOLOGY_SCORE;

	public PMBInteractionQueryTaskFactory(
			Collection<Interaction> interactions,
			Set<UniProtEntry> proteinOfInterestPool,
			NetworkQueryParameters networkQueryParameters,
			ExecutorServiceManager executorServiceManager) {
		this.executorServiceManager = executorServiceManager;
		this.networkQueryParameters = networkQueryParameters;

		this.uniProtEntryClient = new UniProtEntryClient(executorServiceManager);

		this.interactions = interactions;
		this.proteinOfInterestPool = proteinOfInterestPool;

		this.inputProteinIDs = networkQueryParameters.getProteinOfInterestUniprotId();
		this.interactomeQuery = networkQueryParameters.isInteractomeQuery();

		this.referenceOrganism = networkQueryParameters.getReferenceOrganism();

		this.otherOrganisms = new ArrayList<Organism>(networkQueryParameters.getOtherOrganisms());
		this.otherOrganisms.remove(referenceOrganism);

		this.allOrganisms = new ArrayList<Organism>();
		allOrganisms.addAll(this.otherOrganisms);
		allOrganisms.add(referenceOrganism);

		MINIMUM_ORTHOLOGY_SCORE = 0.85;
	}

	@Override
	public void run(TaskMonitor monitor) {
		monitor.setTitle("PPiMapBuilder interaction query");

		monitor.setStatusMessage("Fetch UniProt data for input proteins...");
		HashMap<String, UniProtEntry> uniProtEntries = uniProtEntryClient.retrieveProteinsData(inputProteinIDs);
		for (double i = 0, size = inputProteinIDs.size(); i < size; ++i) {
			String proteinID = inputProteinIDs.get((int) i);
			UniProtEntry entry = uniProtEntries.get(proteinID);
			proteinOfInterestPool.add(entry);
		}

        monitor.setStatusMessage("Fetch direct interactions of input proteins in reference organism...");

		monitor.setStatusMessage("Fetch orthologs of interactors in other organisms...");

		monitor.setStatusMessage("Fetch direct interaction of input proteins orthologs in other organisms...");

		monitor.setStatusMessage("Fetch secondary interactions in all organisms...");

        Protein protA = new Protein("P04040", referenceOrganism);
        Protein protB = new Protein("Q08752", referenceOrganism);

        ArrayList<String> sourceDatabases = new ArrayList<String>();
        sourceDatabases.add("IntAct");

        Interaction interaction = new Interaction(protA, protB, sourceDatabases, referenceOrganism);
        interactions.add(interaction);

		monitor.setProgress(1.0);
	}


}