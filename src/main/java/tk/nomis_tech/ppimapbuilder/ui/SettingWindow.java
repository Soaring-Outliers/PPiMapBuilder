package tk.nomis_tech.ppimapbuilder.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.cytoscape.work.TaskManager;

import tk.nomis_tech.ppimapbuilder.settings.PMBSettingSaveTaskFactory;
import tk.nomis_tech.ppimapbuilder.settings.PMBSettings;
import tk.nomis_tech.ppimapbuilder.ui.panel.DatabaseSettingPanel;
import tk.nomis_tech.ppimapbuilder.util.PsicquicService;

/**
 * PPiMapBuilder interaction query window
 */
public class SettingWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton saveSettings;
	private JButton cancel;
	private DatabaseSettingPanel dsp;
	private PMBSettingSaveTaskFactory saveSettingFactory;
	private TaskManager taskManager;

	public SettingWindow() {
		setTitle("PPiMapBuilder Settings");
		setLayout(new BorderLayout());

		add(initMainPanel(), BorderLayout.CENTER);
		add(initBottomPanel(), BorderLayout.SOUTH);
		getRootPane().setDefaultButton(saveSettings);

		initListeners();
		
		Dimension d = new Dimension(300, 200);
		setBounds(new Rectangle(d));
		setMinimumSize(d);
		setResizable(true);
		setLocationRelativeTo(JFrame.getFrames()[0]);
	}
	
	public void updateLists(List<PsicquicService> dbs) {
		dsp.updateList(dbs);
	}

	private JPanel initMainPanel() {
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

		dsp = new DatabaseSettingPanel();
		main.add(dsp);
		
		return main;
	}

	private JPanel initBottomPanel() {
		JPanel bottom = new JPanel(new GridLayout(1, 1));

		cancel = new JButton("Cancel");
		saveSettings = new JButton("Save");

		bottom.add(cancel);
		bottom.add(saveSettings);

		return bottom;
	}

	private void initListeners() {
		saveSettings.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				SettingWindow.this.setVisible(false);
				SettingWindow.this.dispose();

				ArrayList<String> databases = new ArrayList<String>();
				for (PsicquicService s : getSelectedDatabases()) {
					databases.add(s.getName());
				}
				PMBSettings.setDatabaseList(databases);
				taskManager.execute(saveSettingFactory.createTaskIterator());

			}

		});
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SettingWindow.this.setVisible(false);
				SettingWindow.this.dispose();
			}

		});
	}
	
	public List<PsicquicService> getSelectedDatabases() {
		return dsp.getSelectedDatabases();
	}
	
	public void setSaveSettingFactory(
			PMBSettingSaveTaskFactory saveSettingFactory) {
		this.saveSettingFactory = saveSettingFactory;
	}
	
	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

}