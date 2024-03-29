/*
 * SimulationPanel.java
 *
 * Created on 22-nov-2010, 0.23.28
 */

package it.francescogabbrielli.apps.torusapplet;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import javax.swing.JApplet;

/**
 *
 * @author Francesco Gabbrielli
 */
public class SimulationPanel extends javax.swing.JPanel 
		implements MouseListener, MouseMotionListener, MouseWheelListener, SimulationListener {

	class Edge {
	   public int a, b;
	   public Edge( int A, int B ) {
		  a = A;  b = B;
	   }
	}

	JApplet applet;
	SimulationSystem system;

	int width, height;
	int mx, my;  // the most recently recorded mouse coordinates
	Image backbuffer;
	int azimuth = 35, elevation = 30;
	PointND[] vertices, axis;
	String[] axisLabels = new String[]{"0","x","y","z"};
	Edge[] edges;
	double userScale = 5;
	double perspective = 50;
	
    /** Creates new form SimulationPanel */
    public SimulationPanel() {
        initComponents();

		vertices = new PointND[8];
		axis = new PointND[4];
		
		edges = new Edge[12];
		edges[ 0] = new Edge(0, 1);
		edges[ 1] = new Edge(0, 2);
		edges[ 2] = new Edge(0, 4);
		edges[ 3] = new Edge(1, 3);
		edges[ 4] = new Edge(1, 5);
		edges[ 5] = new Edge(2, 3);
		edges[ 6] = new Edge(2, 6);
		edges[ 7] = new Edge(3, 7);
		edges[ 8] = new Edge(4, 5);
		edges[ 9] = new Edge(4, 6);
		edges[10] = new Edge(5, 7);
		edges[11] = new Edge(6, 7);

		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		setOpaque(false);

    }

	public void setApplet(JApplet applet) {
		this.applet = applet;
	}

	public JApplet getApplet() {
		return applet;
	}

	public void setSystem(SimulationSystem s) {
		this.system = s;
		s.addSimulationListener(this);
	}

	public void setPerspective(double perspective) {
		this.perspective = perspective;
		near = perspective * system.getRadius();
		refresh();
	}

	public void init() {
		if(system!=null) {
			width = getSize().width;
			height = getSize().height;

			double d = system.getRadius();
			PointND e1 = new PointND(1d,0d,0d).times(system.transform);
			PointND e2 = new PointND(0d,1d,0d).times(system.transform);
			PointND e3 = new PointND(0d,0d,1d).times(system.transform);
			scaleFactor = Math.min(width, height) / d / userScale;
			near = perspective * d;  // distance from eye to near plane
			nearToObj = 1.5;  // distance from near plane to center of object
			
			//System.out.println(yShearY);
			vertices[0] = new PointND(3).add(e1, -1d).add(e2, -1d).add(e3, -1d);
			vertices[1] = new PointND(3).add(e1, -1d).add(e2, -1d).add(e3,  1d);
			vertices[2] = new PointND(3).add(e1, -1d).add(e2,  1d).add(e3, -1d);
			vertices[3] = new PointND(3).add(e1, -1d).add(e2,  1d).add(e3,  1d);
			vertices[4] = new PointND(3).add(e1,  1d).add(e2, -1d).add(e3, -1d);
			vertices[5] = new PointND(3).add(e1,  1d).add(e2, -1d).add(e3,  1d);
			vertices[6] = new PointND(3).add(e1,  1d).add(e2,  1d).add(e3, -1d);
			vertices[7] = new PointND(3).add(e1,  1d).add(e2,  1d).add(e3,  1d);
			//System.out.println(vertices[0]);

			axis[0] = new PointND(3);
			axis[1] = new PointND(3).add(e1, 1.5d);
			axis[2] = new PointND(3).add(e2, 1.5d);
			axis[3] = new PointND(3).add(e3, 1.5d);

			backbuffer = createImage(width, height);
			if(backbuffer!=null)
				refresh();
		}
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

	private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
		init();
	}//GEN-LAST:event_formComponentResized

	double cosT, sinT, cosP, sinP, cosTcosP, cosTsinP, sinTcosP, sinTsinP;
	double scaleFactor, near, nearToObj;

	void drawWireframe(Graphics g) {

		// compute coefficients for the projection
		double theta = Math.PI * azimuth / 180.0;
		double phi = Math.PI * elevation / 180.0;
		cosT = Math.cos(theta);
		sinT = Math.sin(theta);
		cosP = Math.cos(phi);
		sinP = Math.sin(phi);
		cosTcosP = cosT * cosP;
		cosTsinP = cosT * sinP;
		sinTcosP = sinT * cosP;
		sinTsinP = sinT * sinP;

		// project vertices onto the 2D viewport
		Point[] points;
		Point[] axisPoints;
		Point[] atomPoints;
		axisPoints = new Point[axis.length];
		atomPoints = new Point[system.atoms.size()];
		points = new Point[vertices.length];
		int j;

		for (j = 0; j < vertices.length; ++j)
			points[j] = project2D(vertices[j]);
		for (j = 0; j < axis.length; ++j)
			axisPoints[j] = project2D(axis[j]);
		// draw the wireframe
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		for(j=1; j<axis.length;j++) {
			g.setColor(Color.lightGray);
			g.drawLine( axisPoints[0].x, axisPoints[0].y, axisPoints[j].x, axisPoints[j].y);
			g.setColor(Color.yellow);
			g.drawString(axisLabels[j], axisPoints[j].x, axisPoints[j].y);
		}
		g.setColor(Color.white);
		for (j = 0; j < edges.length; ++j)
			g.drawLine(
				points[edges[j].a].x, points[edges[j].a].y,
				points[edges[j].b].x, points[edges[j].b].y);


		if(system!=null) {

			for (j = 0; j < atomPoints.length; j++)
				atomPoints[j] = project2D(system.atoms.get(j));

			// draw points
			g.setColor(Color.red);
			Graphics2D g2 = (Graphics2D) g;
			g2.setPaint(new GradientPaint(-3, -3, Color.white, 3,3, Color.red));
			for (Point p : atomPoints) {
				AffineTransform t = g2.getTransform();
				g.translate(p.x, p.y);
				//g.fillOval(p.x- 3, p.y- 3, 7, 7);
				g2.fillOval(- 3, - 3, 7, 7);
				g2.setTransform(t);
			}
		}

	}

	/** TODO: reverse projection for collision detection */
	private PointND get3D(Point point) {
		PointND ret = new PointND(cosT, cosT, cosT);
		double x2 = (point.x - width/2 - .5) / scaleFactor;
		double y2 = (height/2 - point.y + .5) / scaleFactor;

//		double z1 = cosTcosP * z0 - sinTcosP * x0 - sinP * y0;
//		z1 = x1 / x2 * near - near - nearToObj;
//		z1 = y1 / y2 * near - near - nearToObj;
		return ret;
	}

	private final Point project2D(PointND point) {
		double x0 = point.get(SimulationSystem.X);
		double y0 = point.get(SimulationSystem.Y);
		double z0 = point.get(SimulationSystem.Z);

		// compute an orthographic projection
		double x1 = cosT * x0 + sinT * z0;
		double y1 = -sinTsinP * x0 + cosP * y0 + cosTsinP * z0;

		// now adjust things to get a perspective projection
		double z1 = cosTcosP * z0 - sinTcosP * x0 - sinP * y0;
		x1 = x1 * near / (z1 + near + nearToObj);
		y1 = y1 * near / (z1 + near + nearToObj);

		// the 0.5 is to round off when converting to int
		return new Point(
				(int) (width / 2 + scaleFactor * x1 + 0.5),
				(int) (height / 2 - scaleFactor * y1 + 0.5));
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		e.consume();
	}

	public void mouseReleased(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {
		// get the latest mouse position
		int new_mx = e.getX();
		int new_my = e.getY();

		// adjust angles according to the distance travelled by the mouse
		// since the last event
		azimuth -= new_mx - mx;
		elevation += new_my - my;

		// update the backbuffer
		drawWireframe(backbuffer.getGraphics());

		// update our data
		mx = new_mx;
		my = new_my;

		repaint();
		e.consume();
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		userScale += e.getWheelRotation() * 2 * Math.pow(Math.abs(userScale), 2/3d) *.1d;
		init();
	}

	public void refresh() {
		drawWireframe(backbuffer.getGraphics());
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(backbuffer, 0, 0, this);
		if(applet!=null)
			applet.showStatus(
				"Elev: " + elevation + " deg, " +
				"Azim: " + azimuth + " deg; " +
				"Scale: " + String.format("%2.2f", userScale) + (userScale<0?" (...delving into Antimatter)":"")
			);
		//super.paint(g);
	}

	public void update(SimulationEvent evt) {
		refresh();
	}

	public void init(SimulationEvent evt) {
		init();
	}

	public void changed(SimulationEvent evt) {
		switch(evt.getProperty()) {
			case DENSITY:
			case N_ATOMS:
			case TRANSFORM:
				init();
				break;
		}
	}


}
