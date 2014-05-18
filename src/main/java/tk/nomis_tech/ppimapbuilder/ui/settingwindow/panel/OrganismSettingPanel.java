package tk.nomis_tech.ppimapbuilder.ui.settingwindow.panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import tk.nomis_tech.ppimapbuilder.data.client.web.interaction.PsicquicService;
import tk.nomis_tech.ppimapbuilder.data.organism.InParanoidOrganismRepository;
import tk.nomis_tech.ppimapbuilder.data.organism.Organism;
import tk.nomis_tech.ppimapbuilder.data.organism.UserOrganismRepository;
import tk.nomis_tech.ppimapbuilder.data.settings.PMBSettings;
import tk.nomis_tech.ppimapbuilder.ui.settingwindow.SettingWindow;
import tk.nomis_tech.ppimapbuilder.ui.util.HelpIcon;
import tk.nomis_tech.ppimapbuilder.ui.util.JSearchTextField;
import tk.nomis_tech.ppimapbuilder.ui.util.LogoIcon;

public class OrganismSettingPanel extends TabContentPanel {

	private final JPanel panSourceOrganism;
	private final JPanel panSearchOrganism;
	private JButton addOrganism;
	private JSearchTextField searchBox;
	
	public OrganismSettingPanel(SettingWindow owner) {
		super(new BorderLayout(), "Organisms");

		setBorder(new EmptyBorder(5, 5, 5, 5));

		final JLabel lblSourceOrganisms = new JLabel("Preferred organisms:");
		add(lblSourceOrganisms, BorderLayout.NORTH);

		panSourceOrganism = new JPanel();
		panSourceOrganism.setBackground(Color.white);
		panSourceOrganism.setBorder(new EmptyBorder(0, 0, 0, 0));

		panSourceOrganism.setLayout(new BoxLayout(panSourceOrganism, BoxLayout.Y_AXIS));

		updatePanSourceOrganism();
		
		// Source databases scrollpane containing a panel that will contain checkbox at display
		final JScrollPane scrollPaneSourceOrganisms = new JScrollPane(panSourceOrganism);
		scrollPaneSourceOrganisms.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		add(scrollPaneSourceOrganisms, BorderLayout.CENTER);

		panSearchOrganism = new JPanel();
		
		searchBox = new JSearchTextField(owner);
		searchBox.setIcon(JSearchTextField.class.getResource("search.png"));
		searchBox.setMinimumSize(new Dimension(200, 30));
		searchBox.setPreferredSize(new Dimension(200, 30));
		searchBox.setMaximumSize(new Dimension(200, 30));
		ArrayList<String> data = new ArrayList<String>();
		data = (ArrayList<String>) InParanoidOrganismRepository.getInstance().getOrganismNames();
		for (Organism o : UserOrganismRepository.getInstance().getOrganisms()) {
			data.remove(o.getScientificName());
		}
		searchBox.setSuggestData(data);
		
		
		searchBox.setSuggestWidth(150);
		searchBox.setPreferredSuggestSize(new Dimension(150, 50));
		searchBox.setMinimumSuggestSize(new Dimension(150, 50));
		searchBox.setMaximumSuggestSize(new Dimension(150, 50));
		
		
		panSearchOrganism.add(searchBox);
		
		addOrganism = new JButton("Add");
		addOrganism.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				UserOrganismRepository.getInstance().addOrganism(searchBox.getText());
				updatePanSourceOrganism();
			}
			
		});
		panSearchOrganism.add(addOrganism);
		
		add(panSearchOrganism, BorderLayout.SOUTH);
	}
	
	public void updatePanSourceOrganism() {
		panSourceOrganism.removeAll();
		for (Organism org : UserOrganismRepository.getInstance().getOrganisms()) {
			panSourceOrganism.add(new JLabel(org.getScientificName()));
		}
	}
}
