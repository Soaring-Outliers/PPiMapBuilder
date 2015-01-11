package ch.picard.ppimapbuilder.ui.settingwindow;

import ch.picard.ppimapbuilder.data.interaction.client.web.PsicquicService;
import ch.picard.ppimapbuilder.data.ontology.goslim.GOSlimRepository;
import ch.picard.ppimapbuilder.data.organism.UserOrganismRepository;
import ch.picard.ppimapbuilder.data.settings.PMBSettingSaveTaskFactory;
import ch.picard.ppimapbuilder.data.settings.PMBSettings;
import ch.picard.ppimapbuilder.ui.settingwindow.panel.*;
import ch.picard.ppimapbuilder.ui.util.PMBUIStyle;
import ch.picard.ppimapbuilder.ui.util.focus.FocusPropagator;
import ch.picard.ppimapbuilder.ui.util.focus.FocusPropagatorListener;
import ch.picard.ppimapbuilder.ui.util.tabpanel.TabContent;
import ch.picard.ppimapbuilder.ui.util.tabpanel.TabPanel;
import org.cytoscape.util.swing.OpenBrowser;
import org.cytoscape.work.TaskManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * PPiMapBuilder setting window
 */
public class SettingWindow extends JDialog implements FocusPropagatorListener {

	private static final long serialVersionUID = 1L;

	private final JPanel mainPanel;
	private final JPanel bottomPanel;


	private final TabPanel settingTabPanel;
	private final DatabaseSettingPanel databaseSettingPanel;
	private final OrganismSettingPanel organismSettingPanel;
	private final OrthologySettingPanel orthologySettingPanel;
	private final GOSlimSettingPanel goSlimSettingPanel;

	private final JButton saveSettings;
	private final JButton close;

	private PMBSettingSaveTaskFactory saveSettingFactory;
	private TaskManager taskManager;

	private boolean modificationMade;

	private final Border focusBorder = new CompoundBorder(
			new MatteBorder(5, 5, 5, 5, PMBUIStyle.focusActiveTabColor),
			PMBUIStyle.fancyPanelBorder
	);
	private final Border blurBorder = new CompoundBorder(
			new MatteBorder(5, 5, 5, 5, PMBUIStyle.blurActiveTabColor),
			PMBUIStyle.fancyPanelBorder
	);

	public SettingWindow(OpenBrowser openBrowser) {
		setTitle("PPiMapBuilder Settings");
		setLayout(new BorderLayout());

		final FocusPropagator focusPropagator = new FocusPropagator(this);
		addWindowFocusListener(focusPropagator);

		{// Main panel
			mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

			mainPanel.add(settingTabPanel = new TabPanel<TabContent>(
					focusPropagator,
					databaseSettingPanel = new DatabaseSettingPanel(this),
					organismSettingPanel = new OrganismSettingPanel(this),
					orthologySettingPanel = new OrthologySettingPanel(openBrowser, this),
					goSlimSettingPanel = new GOSlimSettingPanel(this)
			));
			settingTabPanel.getViewportPanel().setBorder(focusBorder);
			add(mainPanel, BorderLayout.CENTER);
		}

		{// Bottom panel
			bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			bottomPanel.setBackground(PMBUIStyle.focusActiveTabColor);

			bottomPanel.add(close = new JButton("Close"));
			close.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
				}

			});

			bottomPanel.add(saveSettings = new JButton("Save"));
			saveSettings.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					saveSettings();
				}

			});

			add(bottomPanel, BorderLayout.SOUTH);
		}

		//getRootPane().setDefaultButton(saveSettings);

		setModificationMade(false);

		Dimension d = new Dimension(552, 420);
		setBounds(new Rectangle(d));
		setMinimumSize(d);
		setResizable(true);
		setLocationRelativeTo(null);
	}

	public void newModificationMade() {
		setModificationMade(true);
	}

	private void setModificationMade(boolean modificationMade) {
		saveSettings.setEnabled(this.modificationMade = modificationMade);
		getRootPane().setDefaultButton(modificationMade ? saveSettings : null);
	}

	private void saveSettings() {
		// DATABASE SETTINGS SAVE
		ArrayList<String> databases = new ArrayList<String>();
		for (PsicquicService s : databaseSettingPanel.getSelectedDatabases()) {
			databases.add(s.getName());
		}
		PMBSettings.getInstance().setDatabaseList(databases);

		// ORGANISM SETTINGS SAVE
		PMBSettings.getInstance().setOrganismList(UserOrganismRepository.getInstance().getOrganisms());

		// GO slim settings save
		PMBSettings.getInstance().setGoSlimList(GOSlimRepository.getInstance().getGOSlims());

		// SAVING TASK
		taskManager.execute(saveSettingFactory.createTaskIterator());

		setModificationMade(false);
	}

	public void setSaveSettingFactory(
			PMBSettingSaveTaskFactory saveSettingFactory) {
		this.saveSettingFactory = saveSettingFactory;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	private boolean silent = false;

	public void closeSilently() {
		silent = true;
		setVisible(false);
	}

	@Override
	public void setVisible(boolean opening) {
		if (opening) {
			settingTabPanel.getActivePanel().getComponent().validate();
		} else {
			if (!silent) {
				if (modificationMade) {
					int res = JOptionPane.showOptionDialog(
							this,
							"Are you sure you want to close the settings window without saving?",
							"Unsaved settings",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null,
							new String[]{"Save and close", "Close", "Cancel"},
							"Save and close"
					);
					if (res == 0) saveSettings();
					else if (res == 2) return;
				}
				setModificationMade(false);

				//PMBSettings.load();
				UserOrganismRepository.resetToSettings();

				GOSlimRepository.resetToSettings();
				//this.dispose();
			}
			silent = false;
		}
		setModal(opening);
		super.setVisible(opening);
	}

	@Override
	public void gainedFocus() {
		settingTabPanel.getViewportPanel().setBorder(focusBorder);
		bottomPanel.setBackground(PMBUIStyle.focusActiveTabColor);
	}

	@Override
	public void lostFocus() {
		settingTabPanel.getViewportPanel().setBorder(blurBorder);
		bottomPanel.setBackground(PMBUIStyle.blurActiveTabColor);
	}
}
