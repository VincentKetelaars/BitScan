package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import objects.CSVFileReader;
import objects.TicketHolder;
import objects.TicketsFile;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField searchTextField;
	private TicketsFile ticketsFile;
	private JScrollPane scrollPane;

	/**
	 * Create the frame. Determine basic settings. Initiate build of the main layout.
	 */
	public MainFrame() {
		setTitle("BitScan");
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 711, 518);

		createMainLayout();
	}

	/**
	 * This method creates the basic background JPanel on which everything else is constructed. On top of this is a JTabbedPane which host multiple tabs.
	 * The first tab is the entrance tab. 
	 */
	private void createMainLayout() {
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("FormattedTextField.selectionBackground"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		contentPane.add(tabbedPane);

		JComponent entranceTab = createEntranceTab();
		tabbedPane.addTab("Entrance", null, entranceTab,"Entrance");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
	}

	/**
	 * This method creates the entrance tab. It has a left and right panel. The left panel has the search functionality. The right panel has the load and statistics functionality.
	 * @return entranceTab
	 */
	protected JPanel createEntranceTab() {
		JPanel entranceTab = new JPanel(false);
		entranceTab.setLayout(new GridLayout(1, 1));        

		entranceTab.add(createLeftEntranceTab());
		entranceTab.add(createRightEntranceTab());

		return entranceTab;
	}

	private JPanel createLeftEntranceTab() {
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		leftPanel.add(createSearchByPanel());

		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setPreferredSize(new Dimension(400, 500));
		leftPanel.add(scrollPane);

		return leftPanel;
	}

	private JPanel createSearchByPanel() {
		JPanel searchByPanel = new JPanel();
		searchByPanel.setLayout(new BoxLayout(searchByPanel, BoxLayout.Y_AXIS));

		searchByPanel.add(createButtonFlowPanel());

		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(Color.WHITE);
		searchByPanel.add(searchPanel);

		searchTextField = new JTextField();
		searchPanel.add(searchTextField);
		searchTextField.setMinimumSize(new Dimension(50, 20));
		searchTextField.setMaximumSize(new Dimension(5000, 20));
		searchTextField.setColumns(10);

		JButton searchButton = new JButton("Search");
		searchPanel.add(searchButton);

		return searchByPanel;
	}

	private JPanel createButtonFlowPanel() {
		JPanel buttonFlowPanel = new JPanel();
		buttonFlowPanel.setBackground(Color.WHITE);
		buttonFlowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton barcodeButton = new JButton("Barcode");
		barcodeButton.setBorder(null);
		barcodeButton.setBackground(Color.WHITE);
		barcodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		buttonFlowPanel.add(barcodeButton);

		JButton nameButton = new JButton("Name");
		nameButton.setBackground(Color.WHITE);
		nameButton.setBorder(null);
		buttonFlowPanel.add(nameButton);

		JButton emailButton = new JButton("Email");
		emailButton.setBackground(Color.WHITE);
		emailButton.setBorder(null);
		buttonFlowPanel.add(emailButton);

		return buttonFlowPanel;
	}

	private JPanel createRightEntranceTab() {
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel loadPanel = new JPanel();
		loadPanel.setBackground(Color.WHITE);
		rightPanel.add(loadPanel);

		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFileChooser();
			}
		});
		loadPanel.add(loadButton);

		JPanel statisticsPanel = new JPanel();
		statisticsPanel.setBackground(Color.WHITE);
		rightPanel.add(statisticsPanel);
		statisticsPanel.setLayout(new BorderLayout(0, 0));

		JTextPane txtpnStatistics = new JTextPane();
		txtpnStatistics.setText("Statistics");
		statisticsPanel.add(txtpnStatistics, BorderLayout.NORTH);

		return rightPanel;
	}
	
	private void openFileChooser() {
		JFileChooser fc = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("CSV file", "csv");
		fc.setFileFilter(filter);
		int returnVal = fc.showDialog(this, "Import");		
		
		if (returnVal == fc.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			CSVFileReader csv = new CSVFileReader(f);		
			ticketsFile = csv.read();
			setListOfTickets();
		} 
	}

	private void setListOfTickets() {
		TicketHolder[] data = ticketsFile.getTicketHolders().values().toArray(new TicketHolder[0]);
		JList list = new JList(data); //data has type Object[]
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		scrollPane.getViewport().add(list);		
	}

}
