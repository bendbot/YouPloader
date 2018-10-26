/* 
 * YouPloader Copyright (c) 2017 genuineparts (itsme@genuineparts.org)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package at.becast.youploader.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import at.becast.youploader.Main;
import at.becast.youploader.account.Account;
import at.becast.youploader.account.AccountManager;
import at.becast.youploader.account.AccountType;
import at.becast.youploader.account.AccountUpdater;
import at.becast.youploader.database.SQLite;
import at.becast.youploader.gui.slider.SideBar;
import at.becast.youploader.gui.slider.SidebarSection;
import at.becast.youploader.gui.spinner.SpeedValuesSpinnerEditor;
import at.becast.youploader.templates.Item;
import at.becast.youploader.templates.Template;
import at.becast.youploader.templates.TemplateManager;
import at.becast.youploader.util.*;
import at.becast.youploader.youtube.Categories;
import at.becast.youploader.youtube.LicenseType;
import at.becast.youploader.youtube.SyndicationType;
import at.becast.youploader.youtube.VisibilityType;
import at.becast.youploader.youtube.data.GameDataItem;
import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.data.VideoMetadata;
import at.becast.youploader.youtube.playlists.Playlist;
import at.becast.youploader.youtube.playlists.PlaylistManager;
import at.becast.youploader.youtube.playlists.PlaylistUpdater;
import at.becast.youploader.youtube.upload.GistUploader;
import at.becast.youploader.youtube.upload.UploadManager;
import ch.qos.logback.classic.LoggerContext;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBoxMenuItem;
import at.becast.youploader.gui.statusbar.StatusBar;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import java.awt.GridLayout;
import javax.swing.border.BevelBorder;

/**
 *
 * @author genuineparts
 */

public class FrmMain extends JFrame implements IMainMenu {

	private static final long serialVersionUID = 6965358827253585528L;
	private static final Logger LOG = LoggerFactory.getLogger(FrmMain.class);
	public static UploadManager UploadMgr;
	public static TemplateManager TemplateMgr;
	private static final String DEFAULT_WIDTH = "900";
	private static final String DEFAULT_HEIGHT = "600";
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());
	private Boolean loading = false;
	private AccountManager accMng = AccountManager.getInstance();
	private Boolean tos;
	private JButton btnAddToQueue;
	private ModalDialog modal;
	private IMainMenu self;
	private JTextArea txtDescription;
	private JTabbedPane TabbedPane;
	private JComboBox<String> cmbFile;
	private JCheckBox chckbxTitleFromFilename;
	private JMenu mnuAcc;
	private JTextField txtTitle;
	private JCheckBoxMenuItem chckbxmntmCheckForUpdates,chkstartintray;
	private JLabel lblTagslenght, lbltitlelenght, lblDesclenght;
	private JSpinner spinner;
	private JPanel QueuePanel;
	private JComboBox<Categories> cmbCategory;
	private JTextArea txtTags;
	private static JComboBox<AccountType> cmbAccount;
	private SidebarSection ss1, ss2, ss3;
	private static JComboBox<Object> cmbAfterFinish;
	public transient static HashMap<Integer, JMenuItem> _accounts = new HashMap<Integer, JMenuItem>();
	private int editItem = -1;
	private StatusBar statusBar;
	private JList<AccountType> AccList;
	private JPanel PlayPanel;
	private TrayManager tray;
	protected static int speed = 0;
	private DefaultContextMenu contextMenu = new DefaultContextMenu();
	private DefaultListModel<AccountType> AccListModel = new DefaultListModel<AccountType>();
	private JTextField txtEpisode;

	/**
	 * Creates new form frmMain
	 */
	public FrmMain() {
		self = this;
		this.tos = false;
		this.setMinimumSize(new Dimension(900, 620));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LOG.info(Main.APP_NAME + " " + Main.VERSION + " closing.", FrmMain.class);
				Main.s.put("left", String.valueOf(getX()));
				Main.s.put("top", String.valueOf(getY()));
				Main.s.put("width", String.valueOf(getWidth()));
				Main.s.put("height", String.valueOf(getHeight()));
				LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
				loggerContext.stop();
				e.getWindow().dispose();
			}
		});
		UploadMgr = UploadManager.getInstance();
		TemplateMgr = TemplateManager.getInstance();
		UploadMgr.setParent(this);
		String sspeed = Main.s.setting.get("speed");
		if(sspeed != null){
			speed = Integer.parseInt(sspeed);
		}else{
			speed = 0;
		}
		initComponents();
		initMenuBar();
		loadAccounts();
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png")));
		try {
			loadQueue();
		} catch (SQLException | IOException e) {
			LOG.error("Error: ", e);
		}
		
		tray = new TrayManager(this);
		addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if(e.getNewState()==ICONIFIED){
                    tray.add();
					setVisible(false);
                }
        if(e.getNewState()==7){
            tray.add();
			setVisible(false);
        }
        if(e.getNewState()==MAXIMIZED_BOTH){
                    tray.remove();
                    setVisible(true);
                }
                if(e.getNewState()==NORMAL){
                    tray.remove();
                    setVisible(true);
                }
            }
        });
		if(Integer.parseInt(Main.s.get("startintray","0"))==1){
			tray.add();
		} else {
			this.setVisible(true);
		}
			
		if (Main.firstlaunch) {
			int n = JOptionPane.showConfirmDialog(null, LANG.getString("frmMain.initialAccount.Message"),
					LANG.getString("frmMain.initialAccount.title"), JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (n == JOptionPane.YES_OPTION) {
				mntmAddAccountActionPerformed();
			}
		}else{
			PlaylistUpdater pu = new PlaylistUpdater(this);
			Thread updater = new Thread(pu);
			updater.start();
			AccountUpdater au = new AccountUpdater(this);
			Thread aupdater = new Thread(au);
			aupdater.start();
		}
		EditPanel edit = (EditPanel) ss1.contentPane;
		if (edit.getCmbTemplate().getModel().getSize() > 0) {
			edit.getCmbTemplate().setSelectedIndex(0);
		}
		
	}

	/**
	 * 
	 */
	public void initComponents() {
		if(Main.debug)
			LOG.debug("init Components", FrmMain.class);

		int left = Integer.parseInt(Main.s.get("left","0"));
		int top = Integer.parseInt(Main.s.get("top","0"));
		int width = Integer.parseInt(Main.s.get("width",DEFAULT_WIDTH));
		int height = Integer.parseInt(Main.s.get("height", DEFAULT_HEIGHT));
		setBounds(left, top, width, height);
		TabbedPane = new JTabbedPane();
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle(Main.APP_NAME + " " + Main.VERSION);
		setName("frmMain");
		//Main Tab Creation
		initMainTab();
		
		//Queue Tab creation
		initQueuetab();
		
		//Playlist Settings Tab creation
		initPlaylistSettingsTab();
		
		statusBar = new StatusBar();

		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addComponent(statusBar, GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
				.addComponent(TabbedPane, GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
					.addComponent(TabbedPane, GroupLayout.PREFERRED_SIZE, 498, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(statusBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		getContentPane().setLayout(layout);
		
		cmbCategory.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				changeCategory();
			}
		});
		QueuePanel.revalidate();
	}
	
	public void initMainTab(){
		cmbCategory = new JComboBox<Categories>();
		cmbCategory.setModel(new DefaultComboBoxModel<Categories>(Categories.values()));
		SideBar sideBar = new SideBar(SideBar.SideBarMode.TOP_LEVEL, true, 300, true);
		ss1 = new SidebarSection(sideBar, LANG.getString("frmMain.Sidebar.Settings"), new EditPanel(this), new ImageIcon(getClass().getResource("/pencil.png")));
		ss2 = new SidebarSection(sideBar, LANG.getString("frmMain.Sidebar.Playlists"), new PlaylistPanel(this), new ImageIcon(getClass().getResource("/layers.png")));
		ss3 = new SidebarSection(sideBar, LANG.getString("frmMain.Sidebar.Monetisation"), new MonetPanel(), new ImageIcon(getClass().getResource("/money.png")));
		sideBar.addSection(ss1, false);
		sideBar.addSection(ss2);
		sideBar.addSection(ss3);
		JPanel mainTab = new JPanel();
		JPanel panel = new JPanel();
		GroupLayout mainTabLayout = new GroupLayout(mainTab);
		mainTabLayout.setHorizontalGroup(
			mainTabLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(mainTabLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sideBar, GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE))
		);
		mainTabLayout.setVerticalGroup(
			mainTabLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
				.addGroup(mainTabLayout.createSequentialGroup()
					.addComponent(sideBar, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("2px"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("20px:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("23px:grow"),
				ColumnSpec.decode("33px"),
				FormSpecs.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("61px"),
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("24px"),
				ColumnSpec.decode("28px"),
				ColumnSpec.decode("40px"),
				ColumnSpec.decode("36px"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("28px"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("58px"),},
			new RowSpec[] {
				RowSpec.decode("2px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				RowSpec.decode("25px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				RowSpec.decode("25px"),
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				RowSpec.decode("25px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				RowSpec.decode("64dlu:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				RowSpec.decode("max(64dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("24px"),
				RowSpec.decode("23px"),}));

		JLabel lblEpisode = new JLabel(LANG.getString("frmMain.Episode"));
		//JLabel lblEpisode = new JLabel("Episode:");
		panel.add(lblEpisode, "3, 6, left, default");
		
		txtEpisode = new JTextField();
		panel.add(txtEpisode, "4, 6, 2, 1, fill, default");
		txtEpisode.setColumns(10);
		
		chckbxTitleFromFilename = new JCheckBox(LANG.getString("frmMain.TitleFromFilename"));
		//chckbxTitleFromFilename = new JCheckBox("Title from Filename");
		panel.add(chckbxTitleFromFilename, "11, 6, 6, 1");

		lbltitlelenght = new JLabel("(0/100)");
		panel.add(lbltitlelenght, "14, 8, 3, 1, right, top");

		txtTitle = new JTextField();
		contextMenu.add(txtTitle);
		panel.add(txtTitle, "3, 9, 14, 1, fill, fill");
		txtTitle.setColumns(10);
		txtTitle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				calcNotifies();
			}
		});

		JLabel lblCategory = new JLabel(LANG.getString("frmMain.Category"));
		panel.add(lblCategory, "3, 11, 4, 1, left, bottom");
		panel.add(cmbCategory, "3, 12, 14, 1, fill, fill");

		JLabel lblDescription = new JLabel(LANG.getString("frmMain.Description"));
		panel.add(lblDescription, "3, 14, 4, 1, left, bottom");

		lblDesclenght = new JLabel("(0/5000)");
		panel.add(lblDesclenght, "14, 14, 3, 1, right, bottom");

		JScrollPane DescriptionScrollPane = new JScrollPane();
		panel.add(DescriptionScrollPane, "3, 15, 14, 1, fill, fill");

		txtDescription = new JTextArea();
		contextMenu.add(txtDescription);
		txtDescription.setFont(new Font("SansSerif", Font.PLAIN, 13));
		DescriptionScrollPane.setViewportView(txtDescription);
		txtDescription.setWrapStyleWord(true);
		txtDescription.setLineWrap(true);
		txtDescription.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				calcNotifies();
			}
		});
		
		JLabel lblTags = new JLabel(LANG.getString("frmMain.Tags"));
		panel.add(lblTags, "3, 17, 4, 1, left, bottom");

		lblTagslenght = new JLabel("(0/500)");
		panel.add(lblTagslenght, "14, 17, 3, 1, right, top");

		JScrollPane TagScrollPane = new JScrollPane();
		panel.add(TagScrollPane, "3, 18, 14, 1, fill, fill");

		txtTags = new JTextArea();
		contextMenu.add(txtTags);
		txtTags.setFont(new Font("SansSerif", Font.PLAIN, 13));
		TagScrollPane.setViewportView(txtTags);
		txtTags.setWrapStyleWord(true);
		txtTags.setLineWrap(true);
		txtTags.setBorder(BorderFactory.createEtchedBorder());
		txtTags.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				calcNotifies();
			}
		});

		JLabel lblAccount = new JLabel(LANG.getString("frmMain.Account"));
		panel.add(lblAccount, "3, 20, 4, 1, left, bottom");
		cmbAccount = new JComboBox<AccountType>();
		panel.add(getCmbAccount(), "3, 21, 14, 1, fill, fill");
		cmbAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeUser();
			}
		});
		btnAddToQueue = new JButton(LANG.getString("frmMain.addtoQueue"));
		btnAddToQueue.setEnabled(false);
		panel.add(btnAddToQueue, "3, 23, 6, 1, fill, fill");
		btnAddToQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queueButton();
			}
		});
		JLabel lblSelectVideo = new JLabel();
		panel.add(lblSelectVideo, "3, 3, 4, 1, left, bottom");

		lblSelectVideo.setText(LANG.getString("frmMain.selectVideoFile"));
		cmbFile = new JComboBox<String>();
		cmbFile.setDropTarget(new DropTarget() {
			private static final long serialVersionUID = 8809983794742040683L;
			public synchronized void drop(DropTargetDropEvent evt) {
	            try {
	                evt.acceptDrop(DnDConstants.ACTION_COPY);
	                @SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt
	                        .getTransferable().getTransferData(
	                                DataFlavor.javaFileListFlavor);
	                for (File file : droppedFiles) {
                		cmbFile.removeAllItems();
    					cmbFile.addItem(file.getAbsolutePath());
	                }
	            } catch (Exception ex) {
					LOG.error("Error dropping video file", ex);
	            }
	        }
	    });
		panel.add(cmbFile, "3, 4, 14, 1, fill, fill");
		JButton btnSelectMovie = new JButton();
		btnSelectMovie.setToolTipText("Select Video File");
		panel.add(btnSelectMovie, "18, 4, center, top");
		btnSelectMovie.setIcon(new ImageIcon(getClass().getResource("/film_add.png")));

		JLabel lblTitle = new JLabel(LANG.getString("frmMain.Title"));
		panel.add(lblTitle, "3, 8, 4, 1, left, bottom");

		JButton btnReset = new JButton(LANG.getString("frmMain.Reset"));
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetEdit();
			}
		});
		panel.add(btnReset, "11, 23, 6, 1, fill, fill");
		btnSelectMovie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditPanel edit = (EditPanel) ss1.contentPane;
				NativeJFileChooser chooser;
				if (edit.getTxtStartDir() != null && !edit.getTxtStartDir().equals("")) {
					chooser = new NativeJFileChooser(edit.getTxtStartDir().getText().trim());
				}else{
					chooser = new NativeJFileChooser();
				}
				int returnVal = chooser.showOpenDialog((Component) self);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					cmbFile.removeAllItems();
					cmbFile.addItem(chooser.getSelectedFile().getAbsolutePath().toString());
					if(chckbxTitleFromFilename.isSelected()){
						txtTitle.setText(chooser.getSelectedFile().getName().replaceAll("\\..*", ""));
					}
				}
			}
		});
		mainTab.setLayout(mainTabLayout);
		mainTab.revalidate();
		mainTab.repaint();
		TabbedPane.addTab(LANG.getString("frmMain.Tabs.VideoSettings"), mainTab);
	}
	
	public void initQueuetab(){
		JScrollPane TabQueues = new JScrollPane();
		QueuePanel = new JPanel();
		TabQueues.setViewportView(QueuePanel);
		
		QueuePanel.setLayout(new MigLayout("", "[875px,grow,fill]", "[][][][]"));
		JPanel buttonPanel = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 1;
		buttonPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(39dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(92dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(46dlu;min)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(22dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(0dlu;default)"),}));

		JButton btnStart = new JButton(LANG.getString("frmMain.start"));
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startUploads();
			}
		});
		
		JLabel lblAfterUploadsFinish = new JLabel(LANG.getString("frmMain.afterfinish"));
		buttonPanel.add(lblAfterUploadsFinish, "10, 2, 7, 1");
		buttonPanel.add(btnStart, "2, 4");

		JButton btnStop = new JButton(LANG.getString("frmMain.stop"));
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UploadMgr.stop();
			}
		});
		buttonPanel.add(btnStop, "6, 4");
		
		cmbAfterFinish = new JComboBox<Object>();
		cmbAfterFinish.setModel(new DefaultComboBoxModel<Object>(new String[] {LANG.getString("frmMain.afterfinish.donothing"), LANG.getString("frmMain.afterfinish.suspend"),LANG.getString("frmMain.afterfinish.shutdown"),LANG.getString("frmMain.afterfinish.quit")}));
		buttonPanel.add(cmbAfterFinish, "10, 4, 7, 1, fill, default");

		JLabel lblUploads = new JLabel(LANG.getString("frmMain.uploads")+":");
		buttonPanel.add(lblUploads, "18, 4, right, fill");

		JSlider slider = new JSlider();
		slider.setPaintTicks(true);
		slider.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				JSlider s = (JSlider) evt.getSource();
				UploadMgr.setUploadlimit(s.getValue());
			}
		});
		slider.setMajorTickSpacing(1);
		slider.setMinorTickSpacing(1);

		slider.setMinimum(1);
		slider.setMaximum(5);
		slider.setValue(1);
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		buttonPanel.add(slider, "20, 4, fill, fill");

		JLabel lblUploadSpeed = new JLabel(LANG.getString("frmMain.uploadspeed")+":");
		lblUploadSpeed.setHorizontalAlignment(SwingConstants.TRAILING);
		buttonPanel.add(lblUploadSpeed, "24, 4");

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(10)));
		spinner.setEditor(new SpeedValuesSpinnerEditor(spinner));
		spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				speed = Integer.parseInt(s.getValue().toString());
				Main.s.put("speed",String.valueOf(speed));
				UploadMgr.setLimit(speed);
			}
		});
		spinner.setValue(speed);
		buttonPanel.add(spinner, "26, 4");

		JLabel lblKbps = new JLabel("kbps");
		buttonPanel.add(lblKbps, "28, 4");

		JPanel TabQueue = new JPanel();
		TabbedPane.addTab(LANG.getString("frmMain.Tabs.Queue"), null, TabQueue, null);
		TabQueue.setLayout(new BorderLayout(0, 0));

		TabQueue.add(buttonPanel, BorderLayout.SOUTH);

		TabQueue.add(TabQueues, BorderLayout.CENTER);
	}
	
	public void initPlaylistSettingsTab(){
		JPanel TabPlaylistSettings = new JPanel();
		TabbedPane.addTab(LANG.getString("frmMain.Tabs.PlaylistSettings"), null, TabPlaylistSettings, null);
		
		JPanel panel_1 = new JPanel();
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_TabPlaylistSettings = new GroupLayout(TabPlaylistSettings);
		gl_TabPlaylistSettings.setHorizontalGroup(
			gl_TabPlaylistSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_TabPlaylistSettings.createSequentialGroup()
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
					.addGap(8)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_TabPlaylistSettings.setVerticalGroup(
			gl_TabPlaylistSettings.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_TabPlaylistSettings.createSequentialGroup()
					.addGroup(gl_TabPlaylistSettings.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
						.addGroup(gl_TabPlaylistSettings.createSequentialGroup()
							.addGap(11)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)))
					.addContainerGap())
		);
		
		PlayPanel = new JPanel();
		scrollPane.setViewportView(PlayPanel);
		PlayPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		AccList = new JList<AccountType>(AccListModel);
		AccList.setOpaque(false);
		AccList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		AccList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(AccList, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(AccList, GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
		TabPlaylistSettings.setLayout(gl_TabPlaylistSettings);
		AccList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				selectPlaylistAccount();
			}
		});
	}
	
	public void initMenuBar(){
		JMenuBar mnuBar = new JMenuBar();
		JMenu mnuFile = new JMenu();
		JMenuItem mnuQuit = new JMenuItem();
		mnuAcc = new JMenu();
		
		mnuFile.setText(LANG.getString("frmMain.menu.File"));

		mnuQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnuQuit.setText(LANG.getString("frmMain.menu.Quit"));
		mnuQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mnuQuitActionPerformed();
			}
		});
		
		chkstartintray = new JCheckBoxMenuItem(LANG.getString("frmMain.menu.StartinTray"));
		if(Main.s.get("startintray").equals("1")){
			chkstartintray.setSelected(true);
		}
		chkstartintray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				togglestartintray();
			}
		});
		mnuFile.add(chkstartintray);
		mnuFile.add(mnuQuit);

		mnuBar.add(mnuFile);

		mnuAcc.setText(LANG.getString("frmMain.menu.Account"));
		mnuBar.add(mnuAcc);
				
		JSeparator separator = new JSeparator();
		JMenuItem mntmAddAccount = new JMenuItem(LANG.getString("frmMain.menu.AddAccount"));
		mntmAddAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mntmAddAccountActionPerformed();
			}
		});

		mnuAcc.add(mntmAddAccount);
		mnuAcc.add(separator);
		
		JMenu mnLanguage = new JMenu("Language");
		mnLanguage.setEnabled(false);
		mnLanguage.setVisible(false);
		
		JMenu mnTemplates = new JMenu(LANG.getString("frmMain.menu.Templates"));
		mnuBar.add(mnTemplates);
		
		JMenuItem mntmAddTemplate = new JMenuItem(LANG.getString("frmMain.menu.addTemplate"));
		mnTemplates.add(mntmAddTemplate);
		mntmAddTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AddTemplateActionPerformed();
			}
		});
		mnuBar.add(mnLanguage);

		JMenu mnHelp = new JMenu(LANG.getString("frmMain.menu.Help"));
		//JMenu menu = new JMenu("?");
		mnuBar.add(mnHelp);

		/**
		JMenuItem mntmDonate = new JMenuItem(LANG.getString("frmMain.menu.Donate"));
		menu.add(mntmDonate);
		mntmDonate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				donateButton();
			}
		});
		*/
		
		JMenuItem mntmAbout = new JMenuItem(LANG.getString("frmMain.menu.About"));
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				FrmAbout about = new FrmAbout();
				about.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);
		
		JMenuItem mntmShowLogfile = new JMenuItem(LANG.getString("frmMain.menu.ShowLogfile"));
		mntmShowLogfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				DesktopUtil.openDir(new File(System.getProperty("user.home")+"/YouPloader"));
			}
		});
		mnHelp.add(mntmShowLogfile);
		
		JMenuItem mntmUploadLogfile = new JMenuItem(LANG.getString("frmMain.menu.UploadLatestLogfile"));
		mntmUploadLogfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				uploadLog();
			}
		});
		mnHelp.add(mntmUploadLogfile);
		
		chckbxmntmCheckForUpdates = new JCheckBoxMenuItem(LANG.getString("frmMain.menu.CheckforUpdates"));
		mnHelp.add(chckbxmntmCheckForUpdates);
		if(Main.s.setting.get("notify_updates").equals("1")){
			chckbxmntmCheckForUpdates.setSelected(true);
		}
		chckbxmntmCheckForUpdates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				toggleUpdateNotifier();
			}
		});
		setJMenuBar(mnuBar);
	}

	protected void toggleUpdateNotifier() {
		if(chckbxmntmCheckForUpdates.isSelected()){
			Main.s.setting.put("notify_updates", "1");
		}else{
			Main.s.setting.put("notify_updates", "0");
		}
		Main.s.save("notify_updates");
	}
	
	protected void togglestartintray() {
		if(chkstartintray.isSelected()){
			Main.s.setting.put("startintray", "1");
		}else{
			Main.s.setting.put("startintray", "0");
		}
		Main.s.save("startintray");
	}
	
	protected void donateButton() {
		DesktopUtil.openBrowser("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AZ42BHSUTGPT6");
	}
	
	protected void startUploads() {
		UploadMgr.setSpeed_limit(speed);
		if ("0".equals(Main.s.setting.get("tos_agreed")) && !this.tos) {
			if(Main.debug)
				LOG.debug("Asking about ToS Agreement");
			
			//Dummy JFrame to keep Dialog on top
			JFrame frmOpt = new JFrame();
		    frmOpt.setAlwaysOnTop(true);
			JCheckBox checkbox = new JCheckBox(LANG.getString("frmMain.tos.Remember"));
			String message = LANG.getString("frmMain.tos.Message");
			Object[] params = { message, checkbox };
			int n;
			do {
				n = JOptionPane.showConfirmDialog(frmOpt, params, LANG.getString("frmMain.tos.Title"),
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			} while (n == JOptionPane.CLOSED_OPTION);
			if (n == JOptionPane.OK_OPTION) {
				if(Main.debug)
					LOG.debug("Agreed to ToS");
				
				if (checkbox.isSelected()) {
					Main.s.setting.put("tos_agreed", "1");
					Main.s.save("tos_agreed");
				}
				this.tos = true;
				UploadMgr.start();
			}
			frmOpt.dispose();
		} else {
			if(Main.debug)
				LOG.debug("Previously agreed to ToS");
			
			UploadMgr.start();
		}
	}

	private void mnuQuitActionPerformed() {
		System.exit(0);
	}

	private void mntmAddAccountActionPerformed() {
		AddAccount acc = new AddAccount(this);
		acc.setVisible(true);

	}
	
	private void AddTemplateActionPerformed() {
		AddTemplate addTemplate = new AddTemplate(this);
		addTemplate.setVisible(true);

	}
	
	public void prepModal(Account Account, String code, AddAccount accd) {
		modal = new ModalDialog((Frame) this, Account, code, accd);
	}

	public void showModal() {
		modal.setVisible(true);
	}

	public void closeModal() {
		modal.success();
	}

	public void refreshAccounts() {
		this.loading = true;
		int s = _accounts.size();
		for (int i = 0; i < s; i++) {
			mnuAcc.remove(_accounts.get(i));
			_accounts.remove(i);
		}
		getCmbAccount().removeAllItems();
		AccListModel.clear();
		btnAddToQueue.setEnabled(false);
		loadAccounts();
		this.loading = false;
	}
	
	public void changeUser() {
		if(!this.loading){
			PlaylistPanel pl = (PlaylistPanel) ss2.contentPane;
			pl.clearPlaylists();
			pl.loadPlaylists();
			TabbedPane.repaint();
			TabbedPane.revalidate();
		}
	}

	public void loadAccounts() {
		int i = 0;
		HashMap<AccountType, Integer> accounts = accMng.load();
		for (Entry<AccountType, Integer> entry : accounts.entrySet()) {
			getCmbAccount().addItem(entry.getKey());
			AccListModel.addElement(entry.getKey());
			JMenuItem rdoBtn = new JMenuItem(entry.getKey().toString());
			if (entry.getValue() == 1) {
				rdoBtn.setSelected(true);
				getCmbAccount().setSelectedItem(entry.getKey());
			}
			rdoBtn.setActionCommand(entry.getKey().toString());
			rdoBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editAccount(e.getActionCommand());
				}
			});
			_accounts.put(i, rdoBtn);
			mnuAcc.add(rdoBtn);
			i++;
		}
		if (!accounts.isEmpty()) {
			btnAddToQueue.setEnabled(true);
		}

	}

	protected void editAccount(String name) {
		int id = accMng.getId(name);
		EditAccount accedit = new EditAccount(this, name, id);
		accedit.setVisible(true);
	}
	
	private void loadQueue() throws SQLException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		PreparedStatement prest = null;
		Connection c = SQLite.getInstance();
		String sql = "SELECT * FROM `uploads` ORDER BY `id`";
		prest = c.prepareStatement(sql);
		ResultSet rs = prest.executeQuery();
		boolean started = false;
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				UploadItem f = new UploadItem();
				Video v = mapper.readValue(rs.getString("data"), new TypeReference<Video>() {});
				VideoMetadata metadata;
				try{
					metadata = mapper.readValue(rs.getString("metadata"), new TypeReference<VideoMetadata>() {});
				}catch(NullPointerException e){
					metadata = new VideoMetadata();
					AccountType acc = (AccountType) getCmbAccount().getSelectedItem();
					metadata.setAccount(acc.getValue());
				}
				f.upload_id = rs.getInt("id");
				String url = rs.getString("url");
				String yt_id = rs.getString("yt_id");
				Date startAt;
				if(rs.getString("starttime") == null || rs.getString("starttime").equals("")){
					startAt = null;
				}else{
					startAt = rs.getDate("starttime");
				}
				f.getlblUrl().setText("https://www.youtube.com/watch?v=" + yt_id);
				f.getlblName().setText(v.snippet.title);
				metadata.setFrame(f);
				File data = new File(rs.getString("file"));
				String status = rs.getString("status");
				long position = rs.getLong("uploaded");
				long size = rs.getLong("lenght");
				if (url != null && !url.equals("") && !"FINISHED".equals(status)) {
					started = true;
					UploadMgr.addResumeableUpload(data, v, metadata, url, yt_id);
					f.getProgressBar().setString(String.format("%6.2f%%", (float) position / size * 100));
					f.getProgressBar().setValue((int) ((float) position / size * 100));
					f.getProgressBar().revalidate();
					f.revalidate();
					f.repaint();
				} else if ("NOT_STARTED".equals(status)) {
					UploadMgr.addUpload(data, v, metadata,startAt);
				} else if ("FAILED".equals(status)) {
					f.getBtnEdit().setEnabled(false);
					f.getProgressBar().setValue(0);
					f.getProgressBar().setString(LANG.getString("Upload.Failed"));
				} else {
					f.getBtnEdit().setEnabled(false);
					f.getProgressBar().setValue(100);
					f.getProgressBar().setString(LANG.getString("Upload.Finished"));
				}
				this.getQueuePanel().add(f, new CC().wrap());
				this.getQueuePanel().revalidate();

			}
		}
		if(started){
			UploadMgr.setSpeed_limit(speed);
			UploadMgr.start();
		}
		rs.close();
		prest.close();
	}

	/**
	 * Creates the Upload and the Uploadframe
	 * 
	 * 
	 * @param File path to the file that should be uploaded
	 * 
	 * @param Name The title of the upload
	 * 
	 * @param acc_id The Account id of the Account that should upload the Video
	 * 
	 * @return UploadItem The Upload Frame
	 *  
	 */
	public UploadItem createUpload(String File, String Name, int acc_id) {
		UploadItem f = new UploadItem();
		EditPanel edit = (EditPanel) ss1.contentPane;
		f.getlblName().setText(Name);
		this.getQueuePanel().add(f, new CC().wrap());
		this.getQueuePanel().revalidate();
		Video v = new Video();
		v.snippet.title = Name;
		Categories cat = (Categories) cmbCategory.getSelectedItem();
		v.snippet.categoryId = cat.getID();
		v.snippet.description = txtDescription.getText();
		if (txtTags != null && !txtTags.getText().equals("")) {
			v.snippet.tags = TagUtil.trimTags(txtTags.getText());
		}
		VideoMetadata metadata = createMetadata();
		VisibilityType visibility = (VisibilityType) edit.getCmbVisibility().getSelectedItem();
		if (visibility == VisibilityType.SCHEDULED) {
			if (edit.getDateTimePicker().getEditor().getValue() != null
					&& !edit.getDateTimePicker().getEditor().getValue().equals("")) {
				v.status.privacyStatus = VisibilityType.SCHEDULED.getData();
				Date date = edit.getDateTimePicker().getDate();
				String pattern = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
				SimpleDateFormat formatter = new SimpleDateFormat(pattern);
				f.getlblRelease().setText(edit.getDateTimePicker().getEditor().getValue().toString());
				v.status.publishAt = formatter.format(date);
			} else {
				v.status.privacyStatus = VisibilityType.PRIVATE.getData();
			}
		} else {
			v.status.privacyStatus = visibility.getData();
			if (visibility == VisibilityType.PUBLIC) {
				f.getlblRelease().setText(LANG.getString("Visibility.Public"));
			}
		}
		v.status.embeddable = edit.getChckbxAllowEmbedding().isSelected();
		v.status.publicStatsViewable = edit.getChckbxMakeStatisticsPublic().isSelected();
		LicenseType license = (LicenseType) edit.getCmbLicense().getSelectedItem();
		v.status.license = license.getData();
		File data = new File(File);
		metadata.setFrame(f);
		metadata.setAccount(acc_id);
		JFormattedTextField tf = edit.getDateTimePickerStart().getEditor();
		System.out.println(tf.getValue());
		if (tf.getValue() == null || tf.getValue().toString().equals("")) {
			UploadMgr.addUpload(data, v, metadata,null);
		}else{
			UploadMgr.addUpload(data, v, metadata, edit.getDateTimePickerStart().getDate());
		}
		return f;
	}

	/**
	 * Updates an existing upload 
	 * 
	 * @param File File path to the file that should be uploaded
	 * @param acc_id The Account id of the Account that should upload the Video
	 * @param upload_id The id of the Upload that should be updated
	 * @throws SQLException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void update(String File, int acc_id, int upload_id) throws SQLException, IOException {
		EditPanel edit = (EditPanel) ss1.contentPane;
		String sql = "SELECT `yt_id` FROM `uploads` WHERE `id`=" + upload_id;
		PreparedStatement prest = null;
		Connection c = SQLite.getInstance();
		prest = c.prepareStatement(sql);
		ResultSet rs = prest.executeQuery();
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				String video_id = rs.getString("yt_id");
				Video v = new Video();
				v.snippet.title = txtTitle.getText();
				Categories cat = (Categories) cmbCategory.getSelectedItem();
				v.snippet.categoryId = cat.getID();
				v.snippet.description = txtDescription.getText();
				if (txtTags != null && !txtTags.getText().equals("")) {
					v.snippet.tags = TagUtil.trimTags(txtTags.getText());
				}
				VideoMetadata metadata = createMetadata();
				VisibilityType visibility = (VisibilityType) edit.getCmbVisibility().getSelectedItem();
				if (visibility == VisibilityType.SCHEDULED) {
					if (edit.getDateTimePicker().getEditor().getValue() != null
							&& !edit.getDateTimePicker().getEditor().getValue().equals("")) {
						v.status.privacyStatus = VisibilityType.SCHEDULED.getData();
						Date date = edit.getDateTimePicker().getDate();
						String pattern = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
						SimpleDateFormat formatter = new SimpleDateFormat(pattern);
						v.status.publishAt = formatter.format(date);
					} else {
						v.status.privacyStatus = VisibilityType.PRIVATE.getData();
					}
				} else {
					v.status.privacyStatus = visibility.getData();
				}
				LicenseType license = (LicenseType) edit.getCmbLicense().getSelectedItem();
				String enddir = edit.getTxtEndDir().getText();
				v.status.embeddable = edit.getChckbxAllowEmbedding().isSelected();
				v.status.publicStatsViewable = edit.getChckbxMakeStatisticsPublic().isSelected();
				v.status.license = license.getData();
				metadata.setAccount(acc_id);
				if (video_id != null && !video_id.equals("")) {
					SQLite.updateUploadData(v,metadata, upload_id);
				} else {
					File data = new File(File);
					SQLite.updateUpload(acc_id, data, v, enddir, metadata, upload_id);
				}
				File data = new File(File);
				UploadMgr.updateUpload(upload_id, data, v, metadata, acc_id);
			}
		}
	}

	protected void resetEdit() {
		if (this.editItem != -1) {
			btnAddToQueue.setText(LANG.getString("frmMain.addtoQueue"));
			cmbFile.removeAllItems();
			getCmbAccount().setEnabled(true);
			this.editItem = -1;
		}
	}
	
	private void queueButton() {
		AccountType acc = (AccountType) getCmbAccount().getSelectedItem();
		if(txtTitle.getText()==null || txtTitle.getText().equals("")){
			JOptionPane.showMessageDialog(null, LANG.getString("frmMain.titleerror.text"), LANG.getString("frmMain.titleerror.title"),
					JOptionPane.ERROR_MESSAGE);
		}else{
			if (this.editItem != -1) {
				try {
					update(cmbFile.getSelectedItem().toString(), acc.getValue(), this.editItem);
				} catch (SQLException | IOException e) {
					LOG.error("Error updating upload",e);
				}
				cmbFile.removeAllItems();
				btnAddToQueue.setText(LANG.getString("frmMain.addtoQueue"));
				getCmbAccount().setEnabled(true);
				this.editItem = -1;
			} else {
				if (cmbFile.getSelectedItem() != null && !cmbFile.getSelectedItem().toString().equals("")) {
					String title = txtTitle.getText();
					if(!txtEpisode.getText().equals("")){
						title = title.replaceAll("%ep%", txtEpisode.getText());
					}
					createUpload(cmbFile.getSelectedItem().toString(), title, acc.getValue());
					cmbFile.removeAllItems();
				} else {
					JOptionPane.showMessageDialog(null, LANG.getString("frmMain.fileerror.text"), LANG.getString("frmMain.fileerror.title"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public void removeItem(int upload_id) {
		for (int i = 0; i < this.getQueuePanel().getComponentCount(); i++) {
			UploadItem item = (UploadItem) this.getQueuePanel().getComponent(i);
			if (item.upload_id == upload_id) {
				this.getQueuePanel().remove(i);
				this.getQueuePanel().revalidate();
				this.getQueuePanel().repaint();
			}
		}
	}

	public void editUpload(int upload_id) throws SQLException, IOException {
		this.editItem = upload_id;
		Connection c = SQLite.getInstance();
		btnAddToQueue.setText(LANG.getString("frmMain.updateUpload"));
		TabbedPane.setSelectedIndex(0);
		ObjectMapper mapper = new ObjectMapper();
		PreparedStatement prest = null;
		EditPanel edit = (EditPanel) ss1.contentPane;
		String sql = "SELECT * FROM `uploads` WHERE `id`=" + upload_id;
		prest = c.prepareStatement(sql);
		ResultSet rs = prest.executeQuery();
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				Video v = mapper.readValue(rs.getString("data"), new TypeReference<Video>() {
				});
				VideoMetadata metadata = mapper.readValue(rs.getString("metadata"), new TypeReference<VideoMetadata>() {
				});
				resetMetadata(metadata);
				cmbFile.removeAllItems();
				cmbFile.addItem(rs.getString("file"));
				this.setCategory(v.snippet.categoryId);
				txtTitle.setText(v.snippet.title);
				getCmbAccount().setSelectedItem(metadata.getAccount());
				getCmbAccount().setEnabled(false);
				txtDescription.setText(v.snippet.description);
				txtTags.setText(TagUtil.prepareTagsfromArray(v.snippet.tags));
				edit.setLicence(v.status.license);
				edit.setVisibility(v.status.privacyStatus, v.status.publishAt);
				edit.getChckbxAllowEmbedding().setSelected(v.status.embeddable);
				edit.getChckbxMakeStatisticsPublic().setSelected(v.status.publicStatsViewable);
				edit.getTxtEndDir().setText(metadata.getEndDirectory());
				if (v.status.publishAt != null && !v.status.publishAt.equals("")) {
					edit.getDateTimePicker().setEnabled(true);
					edit.getDateTimePicker().getEditor().setEnabled(true);
					String pattern = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
					SimpleDateFormat formatter = new SimpleDateFormat(pattern);
					Date date;
					try {
						date = formatter.parse(v.status.publishAt);
						edit.getDateTimePicker().setDate(date);
					} catch (ParseException e) {
						LOG.error("Error: ", e);
					}
				}
				edit.revalidate();
				edit.repaint();

			}
			rs.close();
			prest.close();
		} else {
			rs.close();
			prest.close();
		}

	}

	public void createTemplate(String name) {
		EditPanel edit = (EditPanel) ss1.contentPane;
		Template t = new Template(name);
		Video v = new Video();
		VideoMetadata metadata = createMetadata();
		v.snippet.title = txtTitle.getText();
		Categories cat = (Categories) cmbCategory.getSelectedItem();
		v.snippet.categoryId = cat.getID();
		v.snippet.description = txtDescription.getText();
		if (txtTags != null && !txtTags.getText().equals("")) {
			v.snippet.tags = TagUtil.trimTags(txtTags.getText());
		}
		t.setTitlefromfile(chckbxTitleFromFilename.isSelected());
		VisibilityType visibility = (VisibilityType) edit.getCmbVisibility().getSelectedItem();
		if (visibility == VisibilityType.SCHEDULED) {
			v.status.publishAt = edit.getDateTimePicker().getEditor().getText();
		} else {
			v.status.publishAt = "0";
		}
		v.status.privacyStatus = visibility.getData();
		LicenseType license = (LicenseType) edit.getCmbLicense().getSelectedItem();
		v.status.license = license.getData();
		v.status.embeddable = edit.getChckbxAllowEmbedding().isSelected();
		v.status.publicStatsViewable = edit.getChckbxMakeStatisticsPublic().isSelected();
		if (edit.getTxtStartDir() != null) {
			t.setStartdir(edit.getTxtStartDir().getText());
		}
		t.setMetadata(metadata);
		t.setVideodata(v);
		TemplateMgr.save(t);
	}

	public void loadTemplate(Item item) {
		EditPanel edit = (EditPanel) ss1.contentPane;
		Template t = item.getTemplate();
		edit.setUpdating(true);
		this.setCategory(t.videodata.snippet.categoryId);
		txtTitle.setText(t.videodata.snippet.title);
		txtDescription.setText(t.videodata.snippet.description);
		txtTags.setText(TagUtil.prepareTagsfromArray(t.videodata.snippet.tags));
		chckbxTitleFromFilename.setSelected(t.isTitlefromfile());
		edit.setLicence(t.videodata.status.license);
		if (t.videodata.status.publishAt != null && !t.videodata.status.publishAt.equals("0")
				&& t.videodata.status.privacyStatus.equals("private")) {
			edit.getCmbVisibility().setSelectedItem(VisibilityType.SCHEDULED);
			edit.getDateTimePicker().getEditor().setText(t.videodata.status.publishAt);
		} else if (t.videodata.status.publishAt != null && t.videodata.status.publishAt.equals("0")
				&& t.videodata.status.privacyStatus.equals("private")) {
			edit.getCmbVisibility().setSelectedItem(VisibilityType.PRIVATE);
			edit.getDateTimePicker().getEditor().setText("");
		} else {
			edit.setVisibility(t.videodata.status.privacyStatus, "");
		}
		edit.getChckbxAllowEmbedding().setSelected(t.videodata.status.embeddable);
		edit.getChckbxMakeStatisticsPublic().setSelected(t.videodata.status.publicStatsViewable);
		if (t.startdir != null) {
			edit.getTxtStartDir().setText(t.startdir);
		}
		if(t.getMetadata()!=null){
			resetMetadata(t.getMetadata());
			txtEpisode.setText(t.metadata.getEp());
		}
		calcNotifies();
		edit.setUpdating(false);
	}

	private void calcNotifies() {
		// Special handling for Tags
		int taglength = TagUtil.calculateTagLenght(txtTags.getText());

		if (taglength > 450) {
			lblTagslenght.setForeground(Color.ORANGE);
		} else {
			lblTagslenght.setForeground(Color.BLACK);
		}
		if (taglength >= 500) {
			lblTagslenght.setForeground(Color.RED);
		}
		lblTagslenght.setText("(" + taglength + "/500)");

		if (txtDescription.getText().length() > 4900) {
			lblDesclenght.setForeground(Color.ORANGE);
		} else {
			lblDesclenght.setForeground(Color.BLACK);
		}
		if (txtDescription.getText().length() >= 5000) {
			lblDesclenght.setForeground(Color.RED);
		}
		if (txtDescription.getText().length() >= 5001) {
			txtDescription.setText(txtDescription.getText().substring(0, 5000));
		}
		lblDesclenght.setText("(" + txtDescription.getText().length() + "/5000)");

		if (txtTitle.getText().length() > 90) {
			lbltitlelenght.setForeground(Color.ORANGE);
		} else {
			lbltitlelenght.setForeground(Color.BLACK);
		}
		if (txtTitle.getText().length() >= 100) {
			lbltitlelenght.setForeground(Color.RED);
		}
		if (txtTitle.getText().length() >= 101) {
			txtTitle.setText(txtTitle.getText().substring(0, 100));

		}
		lbltitlelenght.setText("(" + txtTitle.getText().length() + "/100)");
	}

	public void saveTemplate(int id) {
		EditPanel edit = (EditPanel) ss1.contentPane;
		Video v = new Video();
		VideoMetadata metadata = createMetadata();
		Template t = TemplateMgr.get(id);
		t.setMetadata(metadata);
		v.snippet.title = txtTitle.getText();
		t.setTitlefromfile(chckbxTitleFromFilename.isSelected());
		Categories cat = (Categories) cmbCategory.getSelectedItem();
		v.snippet.categoryId = cat.getID();
		v.snippet.description = txtDescription.getText();
		if (txtTags != null && !txtTags.getText().equals("")) {
			v.snippet.tags = TagUtil.trimTags(txtTags.getText());
		}
		VisibilityType visibility = (VisibilityType) edit.getCmbVisibility().getSelectedItem();
		if (visibility == VisibilityType.SCHEDULED) {
			v.status.publishAt = edit.getDateTimePicker().getEditor().getText();
		} else {
			v.status.publishAt = "0";
		}
		v.status.privacyStatus = visibility.getData();
		LicenseType license = (LicenseType) edit.getCmbLicense().getSelectedItem();
		v.status.license = license.getData();
		v.status.embeddable = edit.getChckbxAllowEmbedding().isSelected();
		v.status.publicStatsViewable = edit.getChckbxMakeStatisticsPublic().isSelected();
		if (edit.getTxtStartDir() != null) {
			t.setStartdir(edit.getTxtStartDir().getText());
		}
		t.setVideodata(v);
		TemplateMgr.update(id, t);
	}

	public void setCategory(int catId) {
		for (int i = 0; i < cmbCategory.getItemCount(); i++) {
			if (cmbCategory.getItemAt(i).getID() == catId) {
				cmbCategory.setSelectedIndex(i);
			}
		}
	}
	
	protected void changeCategory() {
		EditPanel edit = (EditPanel) ss1.contentPane;
		edit.getCmbGameTitle().setEnabled(false);
		if (cmbCategory.getSelectedItem() == Categories.GAMES) {
			edit.getCmbGameTitle().setEnabled(true); //Disabled until further Test
		}
	}
	
	protected void monetisation(boolean disable) {
		MonetPanel monet = (MonetPanel) ss3.contentPane;
		if (disable) {
			monet.getChckbxMonetize().setSelected(false);
			monet.getChckbxMonetize().setEnabled(false);
			monet.getChckbxOverlayads().setSelected(false);
			monet.getChckbxOverlayads().setEnabled(false);
			monet.getChckbxSkippableVideoads().setSelected(false);
			monet.getChckbxSkippableVideoads().setEnabled(false);
			monet.getChckbxSponsoredCards().setSelected(false);
			monet.getChckbxSponsoredCards().setEnabled(false);
			for (int i = 0; i < monet.getCmbContentSyndication().getItemCount(); i++) {
				if (monet.getCmbContentSyndication().getItemAt(i).getData().equals("everywhere")) {
					monet.getCmbContentSyndication().setSelectedIndex(i);
				}
			}
			monet.getCmbContentSyndication().setEnabled(false);
		}else{
			monet.getChckbxMonetize().setEnabled(true);
			monet.getCmbContentSyndication().setEnabled(true);
		}
	}
	
	public VideoMetadata createMetadata() {
		VideoMetadata meta = new VideoMetadata();
		EditPanel edit = (EditPanel) ss1.contentPane;
		PlaylistPanel play = (PlaylistPanel) ss2.contentPane;
		MonetPanel monet = (MonetPanel) ss3.contentPane;
		meta.setPlaylists(play.getSelectedPlaylists());
		meta.setEp(txtEpisode.getText());
		meta.setMonetized(monet.getChckbxMonetize().isSelected());
		SyndicationType syn = (SyndicationType) monet.getCmbContentSyndication().getSelectedItem();
		meta.setSyndication(syn.getData());
		meta.setInstream(monet.getChckbxSkippableVideoads().isSelected());
		meta.setOverlay(monet.getChckbxOverlayads().isSelected());
		meta.setProduct(monet.getChckbxSponsoredCards().isSelected());
		meta.setThumbnail(edit.getTxtThumbnail().getText().trim());
		meta.setProductplacement(monet.getChckbxProductplacement().isSelected());
		if(edit.getCmbVisibility().getSelectedItem() == VisibilityType.PUBLIC || edit.getCmbVisibility().getSelectedItem() == VisibilityType.SCHEDULED){
			meta.setMessage(edit.getTxtMessage().getText());
		}
		if(edit.getCmbGameTitle().getSelectedItem()!=null && !edit.getCmbGameTitle().getSelectedItem().equals("")){
			GameDataItem gd = (GameDataItem) edit.getCmbGameTitle().getSelectedItem();
			meta.setGametitle(gd.name);
			if(gd.id!=null){
				meta.setGameid(gd.id);
			}
		}
		meta.setShare_fb(edit.getChckbxFacebook().isSelected());
		meta.setShare_gplus(edit.getChckbxGoogle().isSelected());
		meta.setShare_twitter(edit.getChckbxTwitter().isSelected());
		meta.setCommentsEnabled(edit.getChckbxAllowComments().isSelected());
		meta.setRestricted(edit.getChckbxAgeRestriction().isSelected());
		meta.setEndDirectory(edit.getTxtEndDir().getText());
		return meta;
	}
	
	private void resetMetadata(VideoMetadata metadata) {
		EditPanel edit = (EditPanel) ss1.contentPane;
		PlaylistPanel play = (PlaylistPanel) ss2.contentPane;
		MonetPanel monet = (MonetPanel) ss3.contentPane;		
		for (int i = 0; i < monet.getCmbContentSyndication().getItemCount(); i++) {
			if (monet.getCmbContentSyndication().getItemAt(i).getData().equals(metadata.getSyndication())) {
				monet.getCmbContentSyndication().setSelectedIndex(i);
			}
		}
		edit.getCmbGameTitle().getEditor().setItem("");
		edit.getCmbGameTitle().removeAllItems();
		if(metadata.getGametitle()!=null){
			if(metadata.getGameid()!=null){
				edit.getCmbGameTitle().addItem(new GameDataItem(metadata.getGametitle(),metadata.getGameid()));
			}else{
				edit.getCmbGameTitle().addItem(new GameDataItem(metadata.getGametitle(),null));
			}
		}
		txtEpisode.setText(metadata.getEp());
		edit.getTxtThumbnail().setText(metadata.getThumbnail());
		monet.getChckbxMonetize().setSelected(metadata.isMonetized());
		monet.getChckbxSkippableVideoads().setSelected(metadata.isInstream());
		monet.getChckbxOverlayads().setSelected(metadata.isOverlay());
		monet.getChckbxSponsoredCards().setSelected(metadata.isProduct());
		monet.getChckbxProductplacement().setSelected(metadata.getProductplacement());
		play.setSelectedPlaylists(metadata.getPlaylists());
		edit.getTxtMessage().setText(metadata.getMessage());
		edit.getChckbxFacebook().setSelected(metadata.isShare_fb());
		edit.getChckbxGoogle().setSelected(metadata.isShare_gplus());
		edit.getChckbxTwitter().setSelected(metadata.isShare_twitter());
		edit.getChckbxAllowComments().setSelected(metadata.isCommentsEnabled());
		edit.getChckbxAgeRestriction().setSelected(metadata.isRestricted());
		if (metadata.getEndDirectory() != null) {
			edit.getTxtEndDir().setText(metadata.getEndDirectory());
		}
	}
	private void uploadLog(){
		GistUploader gistUploader = new GistUploader(this);
		Thread gistUp = new Thread(gistUploader);
		gistUp.start();
	}
	protected void selectPlaylistAccount() {
    	for(Component c : PlayPanel.getComponents()){
    		PlayPanel.remove(c);	
    	}
    	PlayPanel.repaint();
		AccountType acc = AccList.getSelectedValue();
		PlaylistManager pl = PlaylistManager.getInstance();
		if(pl.getPlaylists().isEmpty()){
			pl.load();
		}
		if(pl.getPlaylists().get(acc.getValue())!=null && !pl.getPlaylists().get(acc.getValue()).isEmpty()){
			for(Playlist p : pl.getPlaylists().get(acc.getValue())){
				PlaylistPanelItem i = new PlaylistPanelItem(p.name, p.id, p.shown, this);
				PlayPanel.add(i);
			}
		}
		PlayPanel.revalidate();
		PlayPanel.repaint();
	}
	
	public void removePlaylist(PlaylistPanelItem playlistPanelItem) {
		PlayPanel.remove(playlistPanelItem);	
		PlayPanel.revalidate();
		PlayPanel.repaint();
	}
	
	public void refreshTemplates(){
		EditPanel edit = (EditPanel) ss1.contentPane;
		edit.refreshTemplates();
	}
	
	public void deleteTemplate(int id) {
		EditPanel edit = (EditPanel) ss1.contentPane;
		TemplateMgr.delete(id);
		edit.refreshTemplates();
	}

	public PlaylistPanel getPlaylistPanel() {
		return (PlaylistPanel) ss2.contentPane;
	}
	
	private JPanel getQueuePanel() {
		return QueuePanel;
	}

	public JTextArea getTxtTags() {
		return txtTags;
	}

	public JTextArea getTxtDescription() {
		return txtDescription;
	}

	public JSpinner getSpinner() {
		return spinner;
	}
	
	public static int getSpeedlimit() {
		return speed;
	}
	
	public static JComboBox<Object> getCmbAfterFinish() {
		return cmbAfterFinish;
	}
	
	public static JComboBox<AccountType> getCmbAccount() {
		return cmbAccount;
	}
	public StatusBar getStatusBar() {
		return statusBar;
	}
	public JList<AccountType> getAccList() {
		return AccList;
	}

}
