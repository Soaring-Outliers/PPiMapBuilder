package ch.picard.ppimapbuilder.networkbuilder.query.tasks;

import ch.picard.ppimapbuilder.data.organism.Organism;
import ch.picard.ppimapbuilder.data.protein.UniProtEntry;
import ch.picard.ppimapbuilder.data.protein.UniProtEntrySet;
import ch.picard.ppimapbuilder.data.client.ThreadedClientManager;
import ch.picard.ppimapbuilder.util.AbstractTaskProgressMonitor;
import ch.picard.ppimapbuilder.util.concurrency.ConcurrentExecutor;
import org.cytoscape.work.TaskMonitor;
import psidev.psi.mi.tab.model.BinaryInteraction;

import java.util.*;
import java.util.concurrent.Callable;

public class FetchDirectInteractionOtherOrganismsTask extends AbstractInteractionQueryTask {

	// Input
	private final List<Organism> otherOrganisms;
	private final Organism referenceOrganism;
	private final Double MINIMUM_ORTHOLOGY_SCORE;
	private final Set<UniProtEntry> proteinOfInterestPool;

	// Output
	private final UniProtEntrySet interactorPool;
	private final HashMap<Organism, Collection<BinaryInteraction>> directInteractionsByOrg;

	public FetchDirectInteractionOtherOrganismsTask(
			ThreadedClientManager threadedClientManager,
			List<Organism> otherOrganisms, Organism referenceOrganism, Double minimum_orthology_score, Set<UniProtEntry> proteinOfInterestPool,
			UniProtEntrySet interactorPool,
			HashMap<Organism, Collection<BinaryInteraction>> directInteractionsByOrg
	) {
		super(threadedClientManager);
		this.otherOrganisms = otherOrganisms;
		this.referenceOrganism = referenceOrganism;
		this.MINIMUM_ORTHOLOGY_SCORE = minimum_orthology_score;
		this.proteinOfInterestPool = proteinOfInterestPool;
		this.interactorPool = interactorPool;
		this.directInteractionsByOrg = directInteractionsByOrg;
	}

	@Override
	public void run(final TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setStatusMessage("Fetch direct interaction of input proteins orthologs in other organisms...");

		// Progress indicators
		final Map<Organism, Double> progressByOrganism = new HashMap<Organism, Double>();
		final Double[] progressPercent = new Double[]{0d};
		taskMonitor.setProgress(progressPercent[0] = 0d);

		final double size = otherOrganisms.size();
		new ConcurrentExecutor<PrimaryInteractionQuery>(threadedClientManager.getExecutorServiceManager(), otherOrganisms.size()) {

			@Override
			public Callable<PrimaryInteractionQuery> submitRequests(final int index) {
				final Organism organism = otherOrganisms.get(index);
				return new PrimaryInteractionQuery(
						referenceOrganism, organism, proteinOfInterestPool, interactorPool,
						threadedClientManager, MINIMUM_ORTHOLOGY_SCORE,

						new AbstractTaskProgressMonitor() {
							@Override
							public void setProgress(double currentProgress) {
								synchronized (progressPercent[0]) {
									final Double previousProgress = progressByOrganism.get(organism);
									progressByOrganism.put(organism, currentProgress);

									if (previousProgress != null && currentProgress - previousProgress > 1d / size) {
										double sum = 0d;
										for (Double progress : progressByOrganism.values())
											sum += progress;
										double percent = Math.floor(sum / size * 100) / 100;
										if (percent > progressPercent[0]) {
											taskMonitor.setProgress(progressPercent[0] = percent);
										}
									}
								}
							}
						}
				);
			}

			@Override
			public void processResult(PrimaryInteractionQuery result, Integer index) {
				directInteractionsByOrg.put(otherOrganisms.get(index), result.getNewInteractions());
			}

		}.run();

		if (progressPercent[0] < 1.0) taskMonitor.setProgress(1.0);
	}

}