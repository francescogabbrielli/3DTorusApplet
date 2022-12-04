package it.francescogabbrielli.apps.torusapplet ;

import java.awt.*;

public class LinePlot extends Canvas {

    private class Coordinate {
	public double x;
	public double y;
	public Coordinate (double x, double y) {
	    this.x = x;
	    this.y = y;
	}
	public Coordinate () {
	    this.x = 0.0;
	    this.y = 0.0;
	}
    }

    private int WIDTH = 400;
    private int HEIGHT = 100;
    private Font axisFont = new Font("Helvetica",Font.PLAIN,9);
    private FontMetrics axisFontMetrics;
    private int tickLength = 4;

    // Number of ticks
    private int xTick = 10;
    private int yTick = 5;

    private Coordinate axisOrigin = new Coordinate();

    private int dataSets = 1;
    private Image plotBuff = null;
    private Graphics graphics;
    private double[] buffer;
    private int buffPoint = 0;
    private int BUFFSIZE = WIDTH;
    private static final double MARGIN = 1.0;

    // The range of the window onto the data. (In data's coordinates)
    // In other words, this is basically the range of values that will
    // be mapped onto the screen.
    private double windowLeft,windowRight;
    private double windowTop,windowBottom;

    // The area of the canvas that will be used for plotting data
    // the rest will be used for the axes!  These are in normalized
    // device coordinates;
    private double viewLeft = 0.0,viewRight = 1.0;
    private double viewTop = 1.0,viewBottom = 0.0;

    private int padx,pady;

    private Color backCol = Color.white;
    private Color gridCol = new Color(200,200,200);
    private Color lineCol = Color.red;
    private Color axesCol = Color.black;

    public LinePlot(int width, int height) {
	this();
	this.WIDTH = width;
	this.HEIGHT = height;
	resize(WIDTH,HEIGHT);
    }

    public LinePlot() {
	buffer = new double[BUFFSIZE];
	resize(WIDTH,HEIGHT);
    }

    public void resetPlot() {
	buffPoint = 0;
    }

    public void setBackColor(Color col) {
	backCol = col;
    }

    public void plotData(double[] data1, double[] data2) {
    }

    public void plotData(double[] data, double deltax,double xzero) {
	clearPlot();
	setupWindow(data,deltax,xzero,0);
	drawAxes();
	drawData(data,deltax,xzero,0);
	repaint();
    }

    public void plotData(double[] data, double deltax, double xzero,
			 int start) {
	clearPlot();
	setupWindow(data,deltax,xzero,start);
	drawAxes();
	drawData(data,deltax,xzero,start);
	repaint();
    }

    public void plotData(double point) {
	clearPlot();
	buffer[buffPoint] = point;
	setupWindowForBuffer();
	drawAxes();
	drawBuffer();

	buffPoint = (buffPoint + 1) % BUFFSIZE;
	repaint();
    }

    public void drawYZero() {
	Dimension d1 = new Dimension();
	Dimension d2 = new Dimension();

	graphics.setColor(Color.black);
	window2device(windowLeft,0.0,d1);
	window2device(windowRight,0.0,d2);
	drawLine(d1,d2);
    }

    private void makeBlankPlot() {
	clearPlot();
	buffer[0] = 1000.0;
	setupWindowForBuffer();
	drawAxes();
    }

    private void setupWindowForBuffer() {
	double min, max;
	double yrange;
	Coordinate c = new Coordinate();

	// Determine the max and min y values.
	min = buffer[0];
	max = buffer[0];
	for(int i = 1 ; i <= buffPoint ; i++) {
	    if(buffer[i] < min) min = buffer[i];
	    if(buffer[i] > max) max = buffer[i];
	}

	// Add/subtract MARGIN% 

	if(max < 0.0) max = max * (1 - (0.01 * MARGIN));
        else max = max * (1 + (0.01 * MARGIN));
	if(min < 0.0) min = min * (1 + (0.01 * MARGIN));
	else  min = min * (1 - (0.01 * MARGIN));

	// The window boundaries in the x direction are
	// just the length of the data.
	windowLeft = 0.0;
	windowRight = (double)BUFFSIZE;

	// Window boundaries in the y direction.
	windowTop = max;
	windowBottom = min;

	// Now, we add padding for the axes!
	pady = axisFontMetrics.stringWidth(double2string(max)) 
	    + (2 * tickLength) + 3;
	padx = (2 * tickLength) + axisFontMetrics.getAscent() + 3;

	device2ndc(pady,0,c);
	viewLeft = c.x;
	device2ndc(0,HEIGHT - padx,c);
	viewBottom = c.y;

	// Setup origin for axes.
	axisOrigin.x = 0.0;
	axisOrigin.y = min;
    }

    private void setupWindow(double[] data, double deltax, double xzero,int start) {
	double min, max;
	double yrange;
	Coordinate c = new Coordinate();

	// Determine the max and min y values.
	min = data[start];
	max = data[start];
	for(int i = start + 1 ; i < data.length ; i++) {
	    if(data[i] < min) min = data[i];
	    if(data[i] > max) max = data[i];
	}

	// Add/subtract MARGIN% 
	if(max < 0.0) max = max * (1 - (0.01 * MARGIN));
        else max = max * (1 + (0.01 * MARGIN));
	if(min < 0.0) min = min * (1 + (0.01 * MARGIN));
	else  min = min * (1 - (0.01 * MARGIN));

	// The window boundaries in the x direction are
	// just the length of the data.
	windowLeft = xzero;
	windowRight = deltax * (double)data.length;

	// Window boundaries in the y direction.
	windowTop = max;
	windowBottom = min;

	// Now, we add padding for the axes!
	pady = axisFontMetrics.stringWidth(double2string(max)) 
	    + (2 * tickLength) + 3;
	padx = (2 * tickLength) + axisFontMetrics.getAscent() + 3;

	device2ndc(pady,0,c);
	viewLeft = c.x;
	device2ndc(0,HEIGHT - padx,c);
	viewBottom = c.y;

	// Setup origin for axes.
	axisOrigin.x = 0.0;
	axisOrigin.y = min;
    }

    private void drawAxes() {
	Dimension o = new Dimension(); // origin in device coordinates
	Dimension d1 = new Dimension();
	Dimension d2 = new Dimension();
	double fac1,fac2,label;
	int pos;

	graphics.setColor(axesCol);
	graphics.setFont(axisFont);

	// Draw the axes.

	o.width = pady;
	o.height = HEIGHT - (padx);
	d2.width = WIDTH;
	d2.height = o.height;
	drawLine(o,d2);

	d2.width = o.width;
	d2.height = 0;	
	drawLine(o,d2);

	// Draw the ticks and labels.

	fac1 = (double)(WIDTH - o.width) / xTick;
	fac2 = (double)(windowRight - windowLeft) / xTick;
	for(int i = 0; i < xTick; i++) {
	    pos = (int)Math.round(fac1 * i) + o.width;
	    label = (fac2 * i) + windowLeft;
	    d1.width = pos; d2.width = pos;
	    d1.height = o.height - tickLength;
	    d2.height = o.height + tickLength;
	    graphics.setColor(axesCol);
	    drawLine(d1,d2);
	    drawXLabel(pos,double2string(label));
	    if(i != 0) {
		graphics.setColor(gridCol);
		d2.height = 1;
		drawLine(d1,d2);
	    }
	}

	fac1 = (double)(HEIGHT - padx) / yTick;
	fac2 = (double)(windowTop - windowBottom) / yTick;
	for(int i = 0; i < yTick; i++) {
	    pos = HEIGHT - (int)Math.round(fac1 * i) - padx;
	    label = (fac2 * i) + windowBottom;
	    d1.height = pos; d2.height = pos;
	    d1.width = o.width + tickLength;
	    d2.width = o.width - tickLength;
	    graphics.setColor(axesCol);
	    drawLine(d1,d2);
	    drawYLabel(pos,double2string(label));
	    if(i != 0) {
		graphics.setColor(gridCol);
		d2.width = WIDTH - 1;
		drawLine(d1,d2);
	    }
	}
    }

    private void drawData(double[] data, double deltax, 
			  double xzero,int start){
	int len;
	Dimension d1 = new Dimension();
	Dimension d2 = new Dimension();
	double x,y;

	window2device((double)start * deltax,data[start],d1);
	len = data.length;
	graphics.setColor(lineCol);
	
	for(int i = start + 1; i< len ; i++) {
	    window2device((((double)i*deltax) + xzero),data[i],d2);
	    drawLine(d1,d2);
	    d1.width = d2.width;
	    d1.height = d2.height;
	}
    }

    private void drawBuffer() {
	int len;
	Dimension d1 = new Dimension();
	Dimension d2 = new Dimension();

	if(buffPoint == 0) return;

	window2device(0.0,buffer[0],d1);
	len = buffPoint;
	graphics.setColor(lineCol);
	
	for(int i = 1; i<= len ; i++) {
	    window2device((double)i,buffer[i],d2);
	    drawLine(d1,d2);
	    d1.width = d2.width;
	    d1.height = d2.height;
	}
    }

    private void drawXLabel(int pos, String label) {
	double len = axisFontMetrics.stringWidth(label);
	double height = axisFontMetrics.getAscent();
	Dimension d = new Dimension();
	int x,y;
       
	x = (int) (pos - (len / 2));
	y = HEIGHT - (int) (padx - ((2 * tickLength) + height));
	graphics.drawString(label,x,y);
    }

    private void drawYLabel(int pos, String label) {
	double len = axisFontMetrics.stringWidth(label);
	double height = axisFontMetrics.getAscent();
	Dimension d = new Dimension();
	int x,y;
       
	x = (int)(pady - (2 * tickLength) - len);
	y = (int)(pos + (height / 2));
	graphics.drawString(label,x,y);
    }

    private void drawLine(Dimension d1, Dimension d2) {
	graphics.drawLine(d1.width,d1.height,d2.width,d2.height);
    }

    private void window2device(double x, double y, Dimension d) {
	d.width = (int)Math.round( ((((x - windowLeft) / 
				   (windowRight - windowLeft)) * 
				   (viewRight - viewLeft)) + viewLeft) 
				   * WIDTH);
	d.height = (int)Math.round( (1 - ((((y - windowBottom) /
				   (windowTop - windowBottom)) * 
				   (viewTop - viewBottom)) + viewBottom))
				    * HEIGHT);
    }

    private void device2window(int x, int y, Coordinate c) {
	c.x = (((((double)x / WIDTH) - viewLeft)/(viewRight - viewLeft)) * 
	       (windowRight - windowLeft)) + windowLeft;
	c.y = (((1 - ((double)y / HEIGHT) - viewBottom)/
		(viewTop - viewBottom)) * (windowTop - windowBottom)) +
	    windowBottom;
    }

    private void device2ndc(int x, int y, Coordinate c) {
	c.x = (double)x / WIDTH;
	c.y = 1 - ((double)y / HEIGHT);
    }

    private String double2string(double n) {
	double i = Math.rint(n * 1000);

	i = i / 1000;
	return( new Double(i).toString() );
    }
	    
    public void paint(Graphics g) {
	update(g);
    }

    public void update(Graphics g) {
	if(plotBuff == null) {
	    makeBlankPlot();
	}
	g.drawImage(plotBuff,0,0,this);
    }

    private void clearPlot() {
	if(plotBuff == null) {
	    plotBuff = createImage(WIDTH,HEIGHT);
	    graphics = plotBuff.getGraphics();
	    axisFontMetrics = graphics.getFontMetrics(axisFont);
	}
	graphics.setColor(backCol);
	graphics.fillRect(0,0,WIDTH,HEIGHT);
    }
    
}

