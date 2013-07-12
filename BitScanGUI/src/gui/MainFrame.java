package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import objects.CSVFileReader;
import objects.TicketHolder;
import objects.TicketHolderRenderer;
import objects.TicketsFile;
import constants.Constants;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField searchTextField;
	private TicketsFile ticketsFile;
	private JScrollPane scrollPane;
	private JLabel eventTitleLabel;
	private JLabel capacityValueLabel;
	private JLabel checkedInValueLabel;
	private JLabel availableValueLabel;
	
	private String sortBy = "Barcode";

	/**
	 * Create the frame. Determine basic settings. Initiate build of the main layout.
	 */
	public MainFrame() {
		setTitle("BitScan");
		setBackground(Constants.BACKGROUND_COLOR);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		if (Constants.PREFERRED_APP_WIDTH >= width / 2 && Constants.PREFERRED_APP_HEIGHT >= height / 2) {
			setBounds((width - Constants.PREFERRED_APP_WIDTH) / 2, (height - Constants.PREFERRED_APP_HEIGHT) / 2, Constants.PREFERRED_APP_WIDTH, Constants.PREFERRED_APP_HEIGHT);
		} else {
			setBounds(width / 4, height / 4, width / 2, height / 2);
		}

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
		tabbedPane.setBackground(Constants.BACKGROUND_COLOR);
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
		entranceTab.setLayout(new BorderLayout(0, 0));

		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		titlePanel.setBackground(Constants.BACKGROUND_COLOR);
		entranceTab.add(titlePanel, BorderLayout.NORTH);

		eventTitleLabel = new JLabel("Event");
		eventTitleLabel.setFont(Constants.TITLE_FONT);
		titlePanel.add(eventTitleLabel);

		JPanel entranceCenterPanel = new JPanel();
		entranceTab.add(entranceCenterPanel, BorderLayout.CENTER);
		entranceCenterPanel.setLayout(new GridLayout(1, 0, 0, 0));

		entranceCenterPanel.add(createLeftEntranceTab());
		entranceCenterPanel.add(createRightEntranceTab());

		return entranceTab;
	}

	private JPanel createLeftEntranceTab() {
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Constants.BACKGROUND_COLOR);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		leftPanel.add(createSearchByPanel());

		scrollPane = new JScrollPane();
		scrollPane.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

					if (droppedFiles.size() > 1) {
						showTooManyFilesErrorDialog();
					}

					runCSVReader(droppedFiles.get(0));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		scrollPane.setBackground(Constants.BACKGROUND_COLOR);
		scrollPane.setPreferredSize(new Dimension(800, 1000));
		scrollPane.getViewport().setBackground(Constants.BACKGROUND_COLOR);
		leftPanel.add(scrollPane);

		return leftPanel;
	}

	private JPanel createSearchByPanel() {
		JPanel searchByPanel = new JPanel();
		searchByPanel.setLayout(new BoxLayout(searchByPanel, BoxLayout.Y_AXIS));

		searchByPanel.add(createButtonFlowPanel());

		JPanel searchPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) searchPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		searchPanel.setBackground(Constants.BACKGROUND_COLOR);
		searchByPanel.add(searchPanel);

		searchTextField = new JTextField();
		searchPanel.add(searchTextField);
		searchTextField.setMinimumSize(new Dimension(50, 20));
		searchTextField.setMaximumSize(new Dimension(5000, 20));
		searchTextField.setColumns(20);

		JButton searchButton = new JButton("Search");
		searchPanel.add(searchButton);

		return searchByPanel;
	}

	private JPanel createButtonFlowPanel() {
		JPanel buttonFlowPanel = new JPanel();
		buttonFlowPanel.setBackground(Constants.BACKGROUND_COLOR);
		buttonFlowPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JButton barcodeButton = new JButton("Barcode");
		barcodeButton.setFont(Constants.HEADER_FONT);
		barcodeButton.setBorder(Constants.SORT_BUTTON_BORDER);
		barcodeButton.setBackground(Constants.BACKGROUND_COLOR);
		buttonFlowPanel.add(barcodeButton);

		JButton nameButton = new JButton("Name");
		nameButton.setBorder(Constants.SORT_BUTTON_BORDER);
		nameButton.setFont(Constants.HEADER_FONT);
		nameButton.setBackground(Constants.BACKGROUND_COLOR);
		buttonFlowPanel.add(nameButton);

		JButton emailButton = new JButton("Email");
		emailButton.setFont(Constants.HEADER_FONT);
		emailButton.setBorder(Constants.SORT_BUTTON_BORDER);
		emailButton.setBackground(Constants.BACKGROUND_COLOR);
		buttonFlowPanel.add(emailButton);

		return buttonFlowPanel;
	}

	private JPanel createRightEntranceTab() {
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Constants.BACKGROUND_COLOR);
		rightPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel loadPanel = new JPanel();
		loadPanel.setBackground(Constants.BACKGROUND_COLOR);
		rightPanel.add(loadPanel);

		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFileChooser();
			}
		});
		loadPanel.add(loadButton);

		JPanel statisticsPanel = new JPanel();
		statisticsPanel.setBorder(new EmptyBorder(0, Constants.RIGHT_PANEL_SIDE_MARGIN, 0, Constants.RIGHT_PANEL_SIDE_MARGIN));
		statisticsPanel.setBackground(Constants.BACKGROUND_COLOR);
		rightPanel.add(statisticsPanel);
		statisticsPanel.setLayout(new BorderLayout(0, 0));

		JLabel statisticsTitleLabel = new JLabel("Statistics");
		statisticsTitleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
		statisticsTitleLabel.setFont(Constants.HEADER_FONT);
		statisticsPanel.add(statisticsTitleLabel, BorderLayout.NORTH);

		JPanel statisticsTablePanel = new JPanel();
		statisticsTablePanel.setBackground(Constants.BACKGROUND_COLOR);
		statisticsPanel.add(statisticsTablePanel, BorderLayout.WEST);
		statisticsTablePanel.setLayout(new BoxLayout(statisticsTablePanel, BoxLayout.Y_AXIS));

		JLabel capacityTitleLabel = new JLabel("Capacity");
		capacityTitleLabel.setBorder(Constants.STATISTICS_LABEL_BORDER);
		statisticsTablePanel.add(capacityTitleLabel);

		JLabel checkedInTitleLabel = new JLabel("Checked-In");
		checkedInTitleLabel.setBorder(Constants.STATISTICS_LABEL_BORDER);
		statisticsTablePanel.add(checkedInTitleLabel);

		JLabel availableTitleLabel = new JLabel("Available Tickets");
		availableTitleLabel.setBorder(Constants.STATISTICS_LABEL_BORDER);
		statisticsTablePanel.add(availableTitleLabel);

		JPanel statisticsValuePanel = new JPanel();
		statisticsValuePanel.setBackground(Constants.BACKGROUND_COLOR);
		statisticsPanel.add(statisticsValuePanel, BorderLayout.EAST);
		statisticsValuePanel.setLayout(new BoxLayout(statisticsValuePanel, BoxLayout.Y_AXIS));

		capacityValueLabel = new JLabel();
		capacityValueLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		capacityValueLabel.setBorder(Constants.STATISTICS_LABEL_BORDER);
		statisticsValuePanel.add(capacityValueLabel);

		checkedInValueLabel = new JLabel();
		checkedInValueLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		checkedInValueLabel.setBorder(Constants.STATISTICS_LABEL_BORDER);
		statisticsValuePanel.add(checkedInValueLabel);

		availableValueLabel = new JLabel();
		availableValueLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		availableValueLabel.setBorder(Constants.STATISTICS_LABEL_BORDER);
		statisticsValuePanel.add(availableValueLabel);

		return rightPanel;
	}

	private void openFileChooser() {
		JFileChooser fc = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("CSV file", "csv");
		fc.setFileFilter(filter);
		int returnVal = fc.showDialog(this, "Import");		

		if (returnVal == fc.APPROVE_OPTION) {			 // Continue with appropriate path
			File f = fc.getSelectedFile();				
			runCSVReader(f);
		} 
	}

	private void runCSVReader(File f) {
		final CSVFileReader csv = new CSVFileReader(f, this);
		Runnable r = new Runnable() {

			@Override
			public void run() {
				ticketsFile = csv.read();
				if (ticketsFile != null)
					updateListOfTicketsAndLabels();			
			}
		};
		r.run();
	}

	private void updateListOfTicketsAndLabels() {
		capacityValueLabel.setText(Integer.toString(ticketsFile.getCapacity()));
		checkedInValueLabel.setText(Integer.toString(ticketsFile.getCheckedIn()));
		availableValueLabel.setText(Integer.toString(ticketsFile.getCapacity() - ticketsFile.getTicketHolders().size()));
		TicketHolder[] data = ticketsFile.getTicketHolders().values().toArray(new TicketHolder[0]);
		JList list = new JList(data); //data has type Object[]
		list.setCellRenderer(new TicketHolderRenderer());
		scrollPane.setViewportView(list);
	}

	private void showTooManyFilesErrorDialog() {
		JOptionPane.showMessageDialog(this, Constants.LOAD_MULTIPLE_FILES_ERROR_MESSAGE, Constants.LOAD_FILE_ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
	}
	
	ActionListener sortButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			sortBy = b.getText();
			updateListOfTicketsAndLabels();
		}
	};	

}
