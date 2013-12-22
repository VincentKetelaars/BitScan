package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import objects.FileDropTarget;
import objects.MainLogic;
import objects.TicketHolder;
import objects.TicketSort;
import objects.TicketsFile;
import constants.Constants;
import constants.Constants.SortArrayBy;
import constants.GeneralMethods;

public class MainFrame extends JFrame implements IMainFrame {

	private JPanel contentPane;
	private JTextField searchTextField;
	private ArrayList<TicketPanel> ticketPanels;
	private JScrollPane scrollPane;
	private DefaultListModel<TicketHolder> ticketListModel;
	private JPanel showTicketsPanel;
	private JLabel eventTitleLabel;
	private JLabel capacityValueLabel;
	private JLabel checkedInValueLabel;
	private JLabel availableValueLabel;
	private JLabel totalCostLabel;
	private JLabel eventDateLabel;	

	private JButton searchButton;
	private MainLogic mainLogic;
	private SortArrayBy sortBy = SortArrayBy.BARCODE;
	private LoadingNotification loadingNotification;

	/**
	 * Create the frame. Determine basic settings. Initiate build of the main layout.
	 */
	public MainFrame() {
		mainLogic = new MainLogic(this);
		setTitle(Constants.TITLE);
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
		this.addWindowListener(windowAdapter);
	}

	public JFrame getFrame() {
		return (JFrame) this;
	}

	/**
	 * This method creates the basic background JPanel on which everything else is constructed. On top of this is a JTabbedPane which host multiple tabs.
	 * The first tab is the entrance tab. 
	 */
	private void createMainLayout() {
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("FormattedTextField.selectionBackground"));//Constants.MAIN_BACKGROUND_COLOR);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setMinimumSize(new Dimension(780, 540));
		tabbedPane.setMaximumSize(new Dimension(780, 1080));
		tabbedPane.setBackground(Constants.BACKGROUND_COLOR);
		contentPane.add(tabbedPane);

		JComponent entranceTab = createEntranceTab();
		tabbedPane.addTab(Constants.ENTRANCE_TAB_LABEL, null, entranceTab,Constants.ENTRANCE_TAB_TIP);
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

		eventTitleLabel = new JLabel(Constants.EVENT_TITLE_LABEL);
		eventTitleLabel.setFont(Constants.TITLE_FONT);
		titlePanel.add(eventTitleLabel);

		eventDateLabel = new JLabel();
		eventDateLabel.setFont(Constants.TITLE_FONT);
		eventDateLabel.setForeground(Color.GRAY);
		eventDateLabel.setBorder(new EmptyBorder(0, -3, 0, 0));
		titlePanel.add(eventDateLabel);

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
		new FileDropTarget(mainLogic, scrollPane);
		scrollPane.setBorder(new EmptyBorder(5,5,5,5));
		scrollPane.setBackground(Constants.BACKGROUND_COLOR);
		scrollPane.setPreferredSize(new Dimension(800, 1000));
		scrollPane.getViewport().setBackground(Constants.BACKGROUND_COLOR);
		leftPanel.add(scrollPane);
		
		final JList<TicketHolder> list = new JList<TicketHolder>();
		ticketListModel = new DefaultListModel<TicketHolder>();
		list.setModel(ticketListModel);
		list.setCellRenderer(new TicketHolderRenderer());
		list.addMouseListener(new MouseAdapter() {
			public void mouseReleased(final MouseEvent e) {
				int index = list.getSelectedIndex();
				if (index != -1 && index < ticketListModel.size()) {
					TicketHolder th = (TicketHolder) ticketListModel.get(index);
					singleTicketClicked(th);
					// Whatever happens, show the same tickets
					ticketListModel.set(index, mainLogic.getTicketHolderById(th.getId()));
				}
			}
		});
		scrollPane.setViewportView(list);

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
		searchTextField.setColumns(20);
		searchTextField.getDocument().addDocumentListener(searchTextFieldDocumentListener);
		searchTextField.addKeyListener(searchTextFieldActionListener);
		searchPanel.add(searchTextField);	

		searchButton = new JButton(Constants.SEARCH_BUTTON);
		searchButton.addActionListener(searchButtonActionListener);
		searchPanel.add(searchButton);

		return searchByPanel;
	}	

	private JPanel createButtonFlowPanel() {
		JPanel buttonFlowPanel = new JPanel();
		buttonFlowPanel.setBackground(Constants.BACKGROUND_COLOR);
		buttonFlowPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel barcodeButton = new JLabel(GeneralMethods.convertSortArrayByToString(SortArrayBy.BARCODE));
		barcodeButton.setFont(Constants.HEADER_FONT);
		barcodeButton.setBorder(Constants.SORT_BUTTON_BORDER);
		barcodeButton.setBackground(Constants.BACKGROUND_COLOR);
		//barcodeButton.addActionListener(sortButtonActionListener);
		barcodeButton.addMouseListener(sortButtonActionListener);
		buttonFlowPanel.add(barcodeButton);

		JLabel nameButton = new JLabel(GeneralMethods.convertSortArrayByToString(SortArrayBy.NAME));
		nameButton.setFont(Constants.HEADER_FONT);
		nameButton.setBorder(Constants.SORT_BUTTON_BORDER);
		nameButton.addMouseListener(sortButtonActionListener);
		buttonFlowPanel.add(nameButton);

		JLabel emailButton = new JLabel(GeneralMethods.convertSortArrayByToString(SortArrayBy.EMAIL));
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

		JButton loadButton = new JButton(Constants.LOAD_BUTTON);
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainLogic.openFileChooser();
			}
		});
		new FileDropTarget(mainLogic, loadButton);
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

		JLabel statisticsTitleLabel = new JLabel(Constants.STATISTICS_LABEL_TITLE);
		statisticsTitleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
		statisticsTitleLabel.setFont(Constants.HEADER_FONT);
		statisticsPanel.add(statisticsTitleLabel, BorderLayout.NORTH);

		JPanel statisticsTablePanel = new JPanel();
		statisticsTablePanel.setBackground(Constants.BACKGROUND_COLOR);
		statisticsPanel.add(statisticsTablePanel, BorderLayout.WEST);
		statisticsTablePanel.setLayout(new BoxLayout(statisticsTablePanel, BoxLayout.Y_AXIS));

		JLabel capacityTitleLabel = new JLabel(Constants.CAPACITY_LABEL_TITLE);
		capacityTitleLabel.setBorder(Constants.STATISTICS_LABEL_BORDER);
		statisticsTablePanel.add(capacityTitleLabel);

		JLabel checkedInTitleLabel = new JLabel(Constants.CHECKED_IN_LABEL_TITLE);
		checkedInTitleLabel.setBorder(Constants.STATISTICS_LABEL_BORDER);
		statisticsTablePanel.add(checkedInTitleLabel);

		JLabel availableTitleLabel = new JLabel(Constants.AVAILABLE_LABEL_TITLE);
		availableTitleLabel.setBorder(Constants.STATISTICS_LABEL_BORDER);
		statisticsTablePanel.add(availableTitleLabel);

		JPanel statisticsValuePanel = new JPanel();
		statisticsValuePanel.setBackground(Constants.BACKGROUND_COLOR);
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

		statisticsPanel.add(statisticsValuePanel, BorderLayout.EAST);
		return statisticsPanel;
	}

	private Component createBuyTicketPanel() {
		JPanel buyTicketPanel = new JPanel();
		buyTicketPanel.setBackground(Constants.BACKGROUND_COLOR);
		buyTicketPanel.setLayout(new BorderLayout(0, 0));
		buyTicketPanel.setBorder(new EmptyBorder(0, Constants.RIGHT_PANEL_SIDE_MARGIN, 0, Constants.RIGHT_PANEL_SIDE_MARGIN));

		JLabel purchaseTicketLabel = new JLabel(Constants.PURCHASE_TICKET_LABEL_TITLE);
		buyTicketPanel.add(purchaseTicketLabel, BorderLayout.NORTH);
		purchaseTicketLabel.setFont(Constants.HEADER_FONT);

		showTicketsPanel = new JPanel();
		showTicketsPanel.setBackground(Constants.BACKGROUND_COLOR);
		showTicketsPanel.setLayout(new BoxLayout(showTicketsPanel, BoxLayout.Y_AXIS));
		buyTicketPanel.add(showTicketsPanel, BorderLayout.CENTER);

		JPanel purchaseTicketsPanel = new JPanel();
		purchaseTicketsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		purchaseTicketsPanel.setBackground(Constants.BACKGROUND_COLOR);
		buyTicketPanel.add(purchaseTicketsPanel, BorderLayout.SOUTH);
		purchaseTicketsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JButton purchaseTicketsButton = new JButton(Constants.PURCHASE_TICKET_BUTTON_TITLE);
		purchaseTicketsButton.addActionListener(addTicketButtonListener);
		purchaseTicketsPanel.add(purchaseTicketsButton);

		totalCostLabel = new JLabel(GeneralMethods.convertPriceIntToEuroString(0));
		totalCostLabel.setBorder(new EmptyBorder(0, 45, 0, 0));
		purchaseTicketsPanel.add(totalCostLabel);

		return buyTicketPanel;
	}

	/**
	 * Update the statisticsPanel and scrollPane, with all available ticketholders
	 */
	public void updateListOfTicketsAndLabels(TicketsFile ticketsFile) {
		updateListOfTicketsAndLabels(ticketsFile.getTicketHolders().values(), ticketsFile);
	}

	/**
	 * Update the statisticsPanel and scrollPane
	 * @param data : array of ticketholders that will be shown
	 */
	public void updateListOfTicketsAndLabels(Collection<TicketHolder> data, final TicketsFile ticketsFile) {
		eventTitleLabel.setText(ticketsFile.getEventName()); // Event label
		eventDateLabel.setText(GeneralMethods.getEventDateString(ticketsFile));

		// Statistics
		capacityValueLabel.setText(Integer.toString(ticketsFile.getCapacity()));
		checkedInValueLabel.setText(Integer.toString(ticketsFile.getCheckedIn()));
		availableValueLabel.setText(Integer.toString(ticketsFile.getAvailable()));

		final Collection<TicketHolder> finalData = mainLogic.sortArraybySortBy(data, sortBy, true);

		// Remove previous stuff
		ticketListModel.removeAllElements();
		// Set list
		for (TicketHolder th : finalData)
			ticketListModel.addElement(th);

		// Add the tickets
		showTicketsPanel.removeAll(); // First remove any present components
		ticketPanels = new ArrayList<TicketPanel>();
		for (TicketSort ts : ticketsFile.getTicketSorts().values()) {
			if (ts.isDoorSale()) { // Add only if doorsale is allowed!
				TicketPanel tp = new TicketPanel(ts);
				tp.setDocumentListener(numberTicketsDocumentListener);
				ticketPanels.add(tp);
				showTicketsPanel.add(tp);
			}
		}
	}

	protected int singleTicketClicked(TicketHolder ticketHolder) {
		int ret = mainLogic.singleTicketClicked(ticketHolder);
		if (ret == 1)
			showGreenNotificiation(ticketHolder);
		return ret;
	}

	protected void showGreenNotificiation(TicketHolder ticketHolder) {
		final GreenNotification notification = new GreenNotification(this, ticketHolder);
		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(Constants.TIMESHOWNOTIFICATION); // time after which pop up will disappear
					notification.dispose();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	public void showLoadingNotification() {
		loadingNotification = new LoadingNotification(this);
	}

	public void stopLoadingNotification() {
		loadingNotification.dispose();
	}

	DocumentListener numberTicketsDocumentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			totalCostLabel.setText(GeneralMethods.convertPriceIntToEuroString(getTotalPriceOfOrderedTickets()));			
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			totalCostLabel.setText(GeneralMethods.convertPriceIntToEuroString(getTotalPriceOfOrderedTickets()));
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
		}
	};

	private int getTotalPriceOfOrderedTickets() {
		int total = 0;
		for (TicketPanel tp : ticketPanels) {
			total += tp.numberOfTickets() * tp.getTicketSort().getPrice();
		}
		return total;
	}

	ActionListener addTicketButtonListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Add door sale tickets
			HashMap<String, Integer> doorSale = new HashMap<String, Integer>();
			for (TicketPanel tp : ticketPanels) {
				doorSale.put(tp.getTicketSort().getName(), tp.numberOfTickets());
			}
			int ret = mainLogic.addDoorSoldTicket(doorSale);
			if (ret != 0) { // Nothing has been changed, so leave as is.
				return;
			}
			// Update views
			updateListOfTicketsAndLabels(mainLogic.getTicketsFile()); // Update list of ticketHolders and statisticsPanel
			for (TicketPanel tp : ticketPanels) { 
				tp.updatePanel(); // Update ticketPanel
			}			
		}
	};

	ActionListener searchButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<TicketHolder> ths = mainLogic.search(searchTextField.getText(), sortBy);
			if (ths == null) {
				return;
			} else if (ths.size() == 1) { // Single result
				singleTicketClicked(ths.get(0));
			} else { // If one or more results, update the list with the result
				updateListOfTicketsAndLabels(ths, mainLogic.getTicketsFile());
			}
		}
	};	

	MouseListener sortButtonActionListener = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
			JLabel b = (JLabel) e.getSource();
			sortBy = GeneralMethods.convertStringToSortArrayBy(b.getText());
			if (mainLogic.getTicketsFile() != null) {
				updateListOfTicketsAndLabels(mainLogic.getTicketsFile().getTicketHolders().values(), mainLogic.getTicketsFile());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseClicked(MouseEvent e) {}
	};

	WindowAdapter windowAdapter = new WindowAdapter()
	{
		public void windowClosing(WindowEvent we)
		{
			mainLogic.windowClosing();
		}
	};

	public DocumentListener searchTextFieldDocumentListener = new DocumentListener() {

		@Override
		public void changedUpdate(DocumentEvent e) {}

		@Override
		public void insertUpdate(DocumentEvent e) {
			ArrayList<TicketHolder> ths = mainLogic.search(searchTextField.getText(), sortBy);
			if (ths == null)
				return;
			updateListOfTicketsAndLabels(ths, mainLogic.getTicketsFile());	
		}

		@Override
		public void removeUpdate(DocumentEvent e) {}

	};	

	public KeyListener searchTextFieldActionListener = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				searchButton.doClick();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {}
	};	
}
