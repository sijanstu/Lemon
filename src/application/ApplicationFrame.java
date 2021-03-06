package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.lemon.filters.basic.GrayScale;
import org.lemon.filters.basic.SharpImage;
import org.lemon.filters.basic.SobelEdge;
import org.lemon.gui.dialogs.DenoiseImageDialog;
import org.lemon.gui.dialogs.NegativeImageDialog;
import org.lemon.gui.dialogs.PixelateImageDialog;
import org.lemon.gui.dialogs.colremover.ColorRemoverDialog;
import org.lemon.gui.drawing.canvas.NewDrawingPanelSetup;
import org.lemon.gui.image.ImageInfoPanel;
import org.lemon.gui.image.ImageView;
import org.lemon.gui.image.ImagePanel.PanelMode;
import org.lemon.gui.menus.EditMenu;
import org.lemon.gui.menus.file.FileMenu;
import org.lemon.gui.panels.ImageAnalyzePanel;
import org.lemon.gui.panels.OpacityControlPanel;
import org.lemon.gui.toolbars.BrushToolBar;
import org.lemon.tools.BrushTool;
import org.lemon.tools.LemonTool;
import org.lemon.tools.brush.NormalBrushTool;
import org.lemon.utils.AppGlobalProperties;
import org.lemon.gui.panels.LemonToolPanel;
import org.piksel.piksel.PPInternalWindow;


public class ApplicationFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	
	
	private JMenu fileMenu, editMenu;
	
	
	
	//menus and submenus
	private JMenu 				filter, extras, fileSubMenu, noiseSubMenu;
	
	//filters
	private JMenuItem 			grayScale, sobelEdge,
								sharpImg, pixelateImg, cropImg,
								invertImg, denoiseImg, colorRange, plainDrawingPage, pixelDrawingPage;
	
	
	//blend modes
	//To read more about blend mode go to text_files -> BlendModes.txt
	//or wikipedia page https://en.wikipedia.org/wiki/Blend_Modes
	private JMenu 				blendModes;
	private JMenuItem 			multiplyBmode, addBmode, subtractBmode;
	
	//main menu bar
	private JMenuBar 			menuBar;
	
	//middle panel where drawing and image panel will be added.
	//central panel
	private JPanel 				editingPanel = new JPanel();
	
	//test img
	private BufferedImage 		choosenImage = null;
	private String 				choosenImgName = null;
	
	//default filter panel
	private ImageAnalyzePanel 			analyzeMenu;
	
	//main editing panel
	private Workspace 				mainWorkspace;
	
	
	//image controllers and analyze panels
	private OpacityControlPanel 		opacityPanel;
	private ImageInfoPanel 				imgInfoPanel;
	private ImageView 					imageView;
	
	/*All tools container*/
	private LemonToolPanel 				mainToolPanel;
	
	
	/*current tool*/
	private JToolBar					toolBar;
	private LemonTool		 			currentTool;
	
	
	private AppGlobalProperties 		gProperties;
	
	/*
	 * Map<ImageView, BufferedImage> selectedImgsStorage = new HashMap<>();
	 * Stores currently opened images of mainPanel with their corresponding ImageView object.
	 * When applying any filter, program needs to know which image is selected currently.
	 * */
	private Map<ImageView, BufferedImage> selectedImgsStorage = new HashMap<>();
	
	
	
	public ApplicationFrame() throws IOException {
		
		gProperties = new AppGlobalProperties(this);;
		
		fileMenu = new FileMenu(this);
		editMenu = new EditMenu(this);
		
		File f = new File("C:\\Users\\Ramesh\\Documents\\3D Images\\dog2.jpg");
		//test images: dog.jpg, flow.jpg, (color) mack.jpg
		this.choosenImage = ImageIO.read(f);
		this.choosenImgName = f.getName();
		
		currentTool = new NormalBrushTool(choosenImage.createGraphics(), gProperties.getGLobalColor());
		
		/*important panels in app*/
		this.mainWorkspace = new Workspace();
		this.mainToolPanel = new LemonToolPanel(this);
		
		
		this.initToolBar(currentTool);
		
		
		this.imageView = new ImageView(this.choosenImage, gProperties, this.choosenImgName, true, PanelMode.canvasMode);
		this.imgInfoPanel = new ImageInfoPanel(this.choosenImage);
		this.opacityPanel = new OpacityControlPanel(this.choosenImage);
		
		//analyze panel properties
		analyzeMenu = new ImageAnalyzePanel();
		
		//main sub menus
		fileSubMenu = new JMenu("New");
		noiseSubMenu = new JMenu("Noise");
		
		//different blend modes
		blendModes = new JMenu("Blend");
		this.initBlendModes();
		
		//main menu items
		filter = new JMenu("Filter");
		extras = new JMenu("Extras");
		
		//file properties
		plainDrawingPage = new JMenuItem("New Page");
		pixelDrawingPage = new JMenuItem("New Pixel Page");

		//filter properties
		grayScale = new JMenuItem("B&W");
		sobelEdge = new JMenuItem("Find Edges");
		sharpImg = new JMenuItem("Sharp");
		pixelateImg = new JMenuItem("Pixelate");
		invertImg = new JMenuItem("Invert");
		
		//edit properties
		cropImg = new JMenuItem("Crop");
		denoiseImg = new JMenuItem("Denoise");
		colorRange = new JMenuItem("Color Range");
		
		//main menu bar
		menuBar = new JMenuBar();
	
		//file sub menu
		fileSubMenu.add(plainDrawingPage);
		fileSubMenu.add(pixelDrawingPage);
		//noise sub menu
		noiseSubMenu.add(denoiseImg);
		//file options
		//file
		fileMenu.add(fileSubMenu);
		//filter options
		filter.add(grayScale);
		filter.add(sobelEdge);
		filter.add(sharpImg);
		filter.add(pixelateImg);
		filter.add(noiseSubMenu);
		filter.add(invertImg);
		filter.add(blendModes);
		//filter.add(specialEffects);
		//filter.add(clipArts);
		//extras option
		extras.add(colorRange);
		//main menu options
		menuBar.add(this.fileMenu);
		//menuBar.add(file);
		menuBar.add(editMenu);
		menuBar.add(filter);
		menuBar.add(extras);
		
		//all buttons events are intialized in this method
		events();
		
		//screen size
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		//frame style and properties
		setBackground(Color.GRAY);
		setTitle("Lemon Image Editor");
		setSize(screen.width - 50, screen.height - 50);
		setResizable(true);
		setLayout(new BorderLayout());
		setJMenuBar(menuBar);
		addPanelsToFrame();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		

	}
	
	
	
	private void initBlendModes() {
		multiplyBmode = new JMenuItem("Multiply");
		addBmode = new JMenuItem("Add");
		subtractBmode = new JMenuItem("Difference");
		blendModes.add(multiplyBmode);
		blendModes.add(addBmode);
		blendModes.add(subtractBmode);
	}
	
	
	
	/**
	 * init the tool bar according to selected tool
	 * */
	private void initToolBar(LemonTool tool) {
		
		if(tool instanceof BrushTool) {
			gProperties.setGlobalTool(tool);
			this.toolBar = new BrushToolBar(this.gProperties, (BrushTool) tool);
		}
		
	}
	
	
	
	//applying click event to every JMenuItem
	private void events(){
		plainDrawingPage.addActionListener(this);
		grayScale.addActionListener(this);
		sobelEdge.addActionListener(this);
		sharpImg.addActionListener(this);
		pixelateImg.addActionListener(this);
		cropImg.addActionListener(this);
		invertImg.addActionListener(this);
		denoiseImg.addActionListener(this);
		colorRange.addActionListener(this);
		pixelDrawingPage.addActionListener(this);
	}
	
	
	
	//adding all required panels to frame
	private void addPanelsToFrame() throws IOException {
		Container c = this.getContentPane();
		
		/***********************************TESTING**************************************/
		this.mainWorkspace.add(this.imageView);
		this.selectedImgsStorage.put(imageView, choosenImage);
		this.analyzeMenu.add(this.imgInfoPanel);
		this.analyzeMenu.add(this.opacityPanel);
		/*************************************************************************/
		

		c.add(this.toolBar, BorderLayout.NORTH);
		c.add(this.mainWorkspace, BorderLayout.CENTER);
		c.add(this.analyzeMenu, BorderLayout.EAST);
		c.add(this.mainToolPanel, BorderLayout.WEST);
		
	}

	
	
	//frame action performed
	@Override
	public void actionPerformed(ActionEvent action) {
		
		if(this.choosenImage == null) {
			this.noImgSelectedDialog();
			return;
		}
		
		//new page in the editingPanel
		if(action.getSource() == plainDrawingPage) {
			//panel where user can draw
			new NewDrawingPanelSetup(this.mainWorkspace);
			this.revalidate();
		}
		
		
		//sharping the image
		else if(action.getSource() == this.sharpImg) {
			try {
				SharpImage simg = new SharpImage(this.choosenImage);
				this.editingPanel.add(new ImageView(simg.getSharpedImg(), this.choosenImgName));
				this.choosenImage = simg.getSharpedImg();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				this.add(this.editingPanel, BorderLayout.CENTER);
				this.revalidate();
			}
		}

		
		//gray scaling the image
		else if(action.getSource() == this.grayScale) {
			try {
				this.editingPanel.add(new ImageView(new GrayScale(this.choosenImage).getGrayScaledImg(), this.choosenImgName));
				this.add(editingPanel, BorderLayout.CENTER);
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				this.revalidate();
			}
		}
		
		//detecting edges of image
		else if(action.getSource() == this.sobelEdge) {
			try {
				this.editingPanel.add(new ImageView(new SobelEdge(this.choosenImage).getFinalImg(), this.choosenImgName));
				this.add(editingPanel, BorderLayout.CENTER);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//pixelating the img
		else if(action.getSource() == this.pixelateImg) {
			//if only ImageView frame selected
			if(this.mainWorkspace.getSelectedFrame() instanceof ImageView) {
				/*Gets the currently selected ImageView object from selectedImgsStorage 
				 * and pass its corresponding selected image*/
				new PixelateImageDialog(this.selectedImgsStorage.get(this.mainWorkspace.getSelectedFrame()));
				return;
			}
			else
				JOptionPane.showMessageDialog(this, "Please choose image panel");
		}
		
		
		else if(action.getSource() == this.invertImg) {
			new NegativeImageDialog(this.choosenImage, this.editingPanel);
		}
		
		//removing noise from image
		else if(action.getSource() == this.denoiseImg) {
			new DenoiseImageDialog(this.imageView, this.choosenImage);
		}
		
		//removing color of image using simple AI
		else if(action.getSource() == this.colorRange) {
			
			if(this.mainWorkspace.getSelectedFrame() instanceof ImageView) {
				new ColorRemoverDialog(this.selectedImgsStorage.get(this.mainWorkspace.getSelectedFrame()));
			}
		}
		
		
		//pixel drawing panel using PikselPainter Library
		else if(action.getSource() == this.pixelDrawingPage) {
			this.mainWorkspace.add(new PPInternalWindow(300, 300, "Pixel Drawing"));
			this.mainWorkspace.repaint();
		}
		
	}
	
	
	
	/**
	 * Get current tool which has been selected.
	 * @return {@code String} current tool
	 * */
	public LemonTool getTool() {
		return this.gProperties.getGlobalTool();
	}
	
	
	
	/**
	 * Set new tool.
	 * @param newTool new tool
	 * */
	public void setTool(LemonTool newTool) {
		this.gProperties.setGlobalTool(newTool);
	}
	
	
	/**
	 * Get global properties of application.
	 * @return properties
	 * */
	public AppGlobalProperties getGlobalProperties() {
		return this.gProperties;
	}
	
	
	
	/**
	 * Get the main scene of application. Main scene of application is handled by main frame of application.
	 * @return mainScene {@code MainApplicationScene}
	 * */
	public Workspace getMainScene() {
		return this.mainWorkspace;
	}
	
	
	
	/**
	 * Get main tool bar of application. Main tool bar of application is also handled by main frame.
	 * @return mainToolBar {@code MainToolBar}
	 * */
	public JToolBar getCurrentToolBar() {
		return this.toolBar;
	}
	
	
	
	public Map<ImageView, BufferedImage> getImageStorage(){
		return this.selectedImgsStorage;
	}
	
	
	/*if image is not selected, this dialog will pop up*/
	private void noImgSelectedDialog() {
		JOptionPane.showMessageDialog(this, "No image selected!");
	}
	
}
