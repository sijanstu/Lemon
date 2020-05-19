package org.lemon.tools.brush;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public interface BrushTool {
	
	public static final int defaultStrokeSize = 5;
	public static final Color defaultStrokeColor = Color.black;
	public static final Stroke defaultStroke = new BasicStroke(defaultStrokeSize);
	
	
	void setStrokeSize(int size);
	
	int getStrokeSize();
	
	void setStrokeColor(Color color);
	
	Color getStrokeColor();
	
	Stroke getStroke();
	
	void draw(int newX, int newY, int oldX, int oldY);
	
	
	
	
	
	
	
	
	
}
