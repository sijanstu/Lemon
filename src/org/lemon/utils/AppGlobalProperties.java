package org.lemon.utils;

import java.awt.Color;

import org.lemon.tools.LemonTool;

import application.ApplicationFrame;


/**
 * This is the class which handles all the global properties of application.
 * Like which color or tool is currently selected. This will only used once in application.
 * Object is defined in {@code ApplicationFrame} class and from there, it will be circulated to
 * all over the application. All the UI components of application will take it as a first parameter.
 * */
public class AppGlobalProperties {

	
	private Color 						gColor = Color.yellow;
	private LemonTool					gTool;
	
	
	private ApplicationFrame			parent;
	
	
	public AppGlobalProperties(ApplicationFrame parent) {
		this.parent = parent;
	}
	
	
	public void revalidate() {
		parent.revalidate();
	}
	
	
	public Color getGLobalColor() {
		return gColor;
	}
	
	
	public void setGlobalColor(Color c) {
		this.gColor = c;
	}
	
	
	public LemonTool getGlobalTool() {
		return gTool;
	}
	
	
	public void setGlobalTool(LemonTool t) {
		this.gTool = t;
	}
	
	
}
