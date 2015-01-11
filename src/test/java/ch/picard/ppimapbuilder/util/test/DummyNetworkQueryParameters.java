package ch.picard.ppimapbuilder.util.test;

import ch.picard.ppimapbuilder.data.interaction.client.web.PsicquicService;
import ch.picard.ppimapbuilder.data.organism.Organism;
import ch.picard.ppimapbuilder.networkbuilder.NetworkQueryParameters;

import java.util.List;

public class DummyNetworkQueryParameters implements NetworkQueryParameters {
	private final List<PsicquicService> selectedDatabases;
	private final List<String> proteinOfInterestUniprotId;
	private final Organism referenceOrganism;
	private final List<Organism> otherOrganisms;

	public DummyNetworkQueryParameters(
			List<PsicquicService> selectedDatabases,
			List<String> proteinOfInterestUniprotId,
			Organism referenceOrganism,
			List<Organism> otherOrganisms
	) {
		this.selectedDatabases = selectedDatabases;
		this.proteinOfInterestUniprotId = proteinOfInterestUniprotId;
		this.referenceOrganism = referenceOrganism;
		this.otherOrganisms = otherOrganisms;
	}

	@Override
	public List<String> getProteinOfInterestUniprotId() {
		return proteinOfInterestUniprotId;
	}

	@Override
	public Organism getReferenceOrganism() {
		return referenceOrganism;
	}

	@Override
	public List<Organism> getOtherOrganisms() {
		return otherOrganisms;
	}

	@Override
	public List<PsicquicService> getSelectedDatabases() {
		return selectedDatabases;
	}
}