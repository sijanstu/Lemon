package org.lemon.gui.drawing.image;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.lemon.gui.image.ImagePanel;
import org.lemon.tools.BrushTool;
import org.lemon.tools.brush.NormalBrushTool;

/**
 * Class description: This class takes an image as param and applies canvas in it.<p>
 * This class is mainly used for drawing in image.<p>
 * Canvas is java class which is mainly used for drawing paint purpose.<p>
 * */
public class DrawingCanvasOnImage extends MouseAdapter {
	
	//global
	private Graphics2D g2d;
	private ImagePanel ip;
	
	private BrushTool brushTool;
	
	private int newX, newY, oldX, oldY;
	private Color sColor;
	
	
	public DrawingCanvasOnImage(ImagePanel panel, Color c) {
		//assigning globals
		this.ip = panel;
		this.sColor = c;
		this.g2d = ip.getImage().createGraphics();
		brushTool = new NormalBrushTool(g2d, c);
		brushTool.setStrokeColor(sColor);
	}
	
	
	
	public void setBrush(BrushTool brush) {
		this.brushTool = brush;
	}
	
	
	public BrushTool getBrush() {
		return brushTool;
	}
	
	
	public void setColor(Color c) {
		this.sColor = c;
	}
	
	
	public Color getColor() {
		return sColor;
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		
		newX = e.getX();
		newY = e.getY();
		oldX = newX;
		oldY = newY;
	}
	

	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		brushTool.setStrokeSize(10);
		
		newX = e.getX();
		newY = e.getY();
		
		//drawing line wherever the mouse goes
		brushTool.draw(newX, newY, oldX, oldY);
		//this.g2d.dispose();
		this.ip.repaint();
		
		oldX = newX;
		oldY = newY;
		
	}

	
	
	@Override
	public void mouseMoved(MouseEvent e) {
		var cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
		this.ip.setCursor(cursor);
	}
	
}
