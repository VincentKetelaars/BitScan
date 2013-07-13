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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
		scrollPane.setBorder(new EmptyBorder(5,5,5,5));
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
		searchTextField.setColumns(20);

		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(searchButtonActionListener);
		searchPanel.add(searchButton);

		return searchByPanel;
	}	

	private JPanel createButtonFlowPanel() {
		JPanel buttonFlowPanel = new JPanel();
		buttonFlowPanel.setBackground(Constants.BACKGROUND_COLOR);
		buttonFlowPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		BufferedImage stopPicture = null;
		try {
			stopPicture = ImageIO.read(new File(Constants.WHITE_BACKGROUND));
		} catch (IOException ex) { }
		JLabel picLabel = new JLabel(new ImageIcon( stopPicture ));

		JLabel barcodeButton = new JLabel("Barcode");
		barcodeButton.setFont(Constants.HEADER_FONT);
		barcodeButton.setBorder(Constants.SORT_BUTTON_BORDER);
		barcodeButton.setBackground(Constants.BACKGROUND_COLOR);
		//barcodeButton.addActionListener(sortButtonActionListener);
		barcodeButton.addMouseListener(sortButtonActionListener);
		buttonFlowPanel.add(barcodeButton);

		JLabel nameButton = new JLabel("Name");
		nameButton.setFont(Constants.HEADER_FONT);
		nameButton.setBorder(Constants.SORT_BUTTON_BORDER);
		nameButton.addMouseListener(sortButtonActionListener);
		buttonFlowPanel.add(nameButton);

		JLabel emailButton = new JLabel("Email");
		emailButton.setFont(Constants.HEADER_FONT);
		emailButton.setBorder(Constants.SORT_BUTTON_BORDER);
		emailButton.addMouseListener(sortButtonActionListener);
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

		rightPanel.add(createStatisticsPanel());	
		
		rightPanel.add(createBuyTicketPanel());
		
		return rightPanel;
	}

	private JPanel createStatisticsPanel() {
		JPanel statisticsPanel = new JPanel();
		statisticsPanel.setBorder(new EmptyBorder(0, Constants.RIGHT_PANEL_SIDE_MARGIN, 0, Constants.RIGHT_PANEL_SIDE_MARGIN));
		statisticsPanel.setBackground(Constants.BACKGROUND_COLOR);
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
		
		return statisticsPanel;
	}
	
	private Component createBuyTicketPanel() {
		JPanel buyTicketPanel = new JPanel();
		buyTicketPanel.setBackground(Constants.BACKGROUND_COLOR);
		buyTicketPanel.setLayout(new BorderLayout(0, 0));
		buyTicketPanel.setBorder(new EmptyBorder(0, Constants.RIGHT_PANEL_SIDE_MARGIN, 0, Constants.RIGHT_PANEL_SIDE_MARGIN));
		
		JLabel purchaseTicketLabel = new JLabel("Purchase Ticket");
		buyTicketPanel.add(purchaseTicketLabel, BorderLayout.NORTH);
		purchaseTicketLabel.setFont(Constants.HEADER_FONT);
		
		return buyTicketPanel;
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
					updateListOfTicketsAndLabels(ticketsFile.getTicketHolders().values().toArray(new TicketHolder[0]));			
			}
		};
		r.run();
	}

	private void updateListOfTicketsAndLabels(TicketHolder[] data) {
		eventTitleLabel.setText(ticketsFile.getEventName());
		capacityValueLabel.setText(Integer.toString(ticketsFile.getCapacity()));
		checkedInValueLabel.setText(Integer.toString(ticketsFile.getCheckedIn()));
		availableValueLabel.setText(Integer.toString(ticketsFile.getCapacity() - ticketsFile.getTicketHolders().size()));
		switch (sortBy) {
		case "Barcode": 
			Arrays.sort(data, IDComparator);
			break;
		case "Name": 
			Arrays.sort(data, NameComparator);
			break;
		case "Email": 
			Arrays.sort(data, EmailComparator);
			break;
		default : break;
		}

		JList list = new JList(data); //data has type Object[]
		list.setCellRenderer(new TicketHolderRenderer());
		scrollPane.setViewportView(list);
	}

	private void showTooManyFilesErrorDialog() {
		JOptionPane.showMessageDialog(this, Constants.LOAD_MULTIPLE_FILES_ERROR_MESSAGE, Constants.LOAD_FILE_ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
	}

	MouseListener sortButtonActionListener = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
			JLabel b = (JLabel) e.getSource();
			sortBy = b.getText();
			if (ticketsFile != null) {
				updateListOfTicketsAndLabels(ticketsFile.getTicketHolders().values().toArray(new TicketHolder[0]));
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	};

	ActionListener searchButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<TicketHolder> ths = search(searchTextField.getText());
			updateListOfTicketsAndLabels(ths.toArray(new TicketHolder[0]));
		}
	};

	Comparator<TicketHolder> IDComparator = new Comparator<TicketHolder>() {

		@Override
		public int compare(TicketHolder o1, TicketHolder o2) {
			return o1.getId().compareTo(o2.getId());
		}

	};

	Comparator<TicketHolder> NameComparator = new Comparator<TicketHolder>() {

		@Override
		public int compare(TicketHolder o1, TicketHolder o2) {
			return o1.getName().compareTo(o2.getName());
		}

	};

	Comparator<TicketHolder> EmailComparator = new Comparator<TicketHolder>() {

		@Override
		public int compare(TicketHolder o1, TicketHolder o2) {
			return o1.getEmail().compareTo(o2.getEmail());
		}

	};

	private ArrayList<TicketHolder> search(String s) {
		ArrayList<TicketHolder> l = new ArrayList<TicketHolder>();
		switch (sortBy) {
		case "Barcode": 
			TicketHolder th = ticketsFile.getTicketHolders().get(s);
			if (th != null) l.add(th);
			break;
		case "Name": 
			Collection<TicketHolder> c = ticketsFile.getTicketHolders().values();
			for (TicketHolder t : c) {
				if (t.getName().toLowerCase().contains(s.toLowerCase())) {
					l.add(t);
				}
			}
			break;
		case "Email":
			Collection<TicketHolder> col = ticketsFile.getTicketHolders().values();
			for (TicketHolder t : col) {
				if (t.getEmail().toLowerCase().contains(s.toLowerCase())) {
					l.add(t);
				}
			}
			break;
		default: 
			return null;
		}
		return l;
	}

}
