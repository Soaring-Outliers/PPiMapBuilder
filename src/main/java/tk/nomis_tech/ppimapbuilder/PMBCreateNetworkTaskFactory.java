package tk.nomis_tech.ppimapbuilder;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * PPiMapBuilder app sub menu
 */
public class PMBCreateNetworkTaskFactory extends AbstractTaskFactory {
	private final CyNetworkManager netMgr;
    private final CyNetworkFactory cnf;
	private final CyNetworkNaming namingUtil;
    private final CyNetworkViewFactory cnvf;
    private final CyNetworkViewManager networkViewManager;	
	
    
    public PMBCreateNetworkTaskFactory(final CyNetworkNaming cyNetworkNaming, final CyNetworkFactory cnf, 
    		final CyNetworkManager networkManager, final CyNetworkViewFactory cnvf, final CyNetworkViewManager networkViewManager){
            this.netMgr = networkManager;
            this.namingUtil = cyNetworkNaming;
            this.cnf = cnf;
            this.cnvf = cnvf;
            this.networkViewManager = networkViewManager;
    }
	
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new PMBCreateNetworkTask(netMgr, namingUtil, cnf, cnvf, networkViewManager)) ;
	}

}
