/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TorusAppletMenuBar.java
 *
 * Created on 23-nov-2010, 14.48.26
 */

package it.francescogabbrielli.apps.torusapplet;

import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import static it.francescogabbrielli.apps.torusapplet.SimulationSystem.*;

/**
 *
 * @author Francesco Gabbrielli
 */
public class TorusAppletMenuBar extends JMenuBar {

	Properties props;

	File homeDir;

    /** Creates new form BeanForm */
    public TorusAppletMenuBar() {
        initComponents();
		props = new Properties();
		try {
			props.load(new FileInputStream("torusapplet.conf"));
			homeDir = new File(props.getProperty("homeDir"));
		} catch(Exception e) {
			//JOptionPane.showMessageDialog(this, "Cannot open config file", "");
		}
    }

	TorusApplet getApplet() {
		return (TorusApplet) getTopLevelAncestor();
	}

	public JMenuItem getStartItem() {
		return startItem;
	}

	public void initApplet() {
		exportItem.setVisible(false);
		separator1.setVisible(false);
		exitItem.setVisible(false);
	}

	public void close() {
		if(homeDir!=null) {
			try {
				props.setProperty("homeDir", homeDir.getAbsolutePath());
				props.store(new PrintWriter("torusapplet.conf"), null);
			} catch(Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Cannot save config", JOptionPane.ERROR_MESSAGE);
			}
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

        fileMenu = new javax.swing.JMenu();
        startItem = new javax.swing.JMenuItem();
        resetItem = new javax.swing.JMenuItem();
        exportItem = new javax.swing.JMenuItem();
        separator1 = new javax.swing.JPopupMenu.Separator();
        exitItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        guideItem = new javax.swing.JMenuItem();
        aboutItem = new javax.swing.JMenuItem();

        fileMenu.setText("File");

        startItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        startItem.setText("Start");
        startItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startItemActionPerformed(evt);
            }
        });
        fileMenu.add(startItem);

        resetItem.setText("Reset");
        resetItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetItemActionPerformed(evt);
            }
        });
        fileMenu.add(resetItem);

        exportItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        exportItem.setText("Export Points");
        exportItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportItemActionPerformed(evt);
            }
        });
        fileMenu.add(exportItem);
        fileMenu.add(separator1);

        exitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        exitItem.setText("Exit");
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitItem);

        add(fileMenu);

        helpMenu.setText("?");

        guideItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        guideItem.setText("Guide");
        guideItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guideItemActionPerformed(evt);
            }
        });
        helpMenu.add(guideItem);

        aboutItem.setText("About");
        aboutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutItem);

        add(helpMenu);
    }// </editor-fold>//GEN-END:initComponents

	private void exportItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportItemActionPerformed
		TorusApplet t = getApplet();
		t.getPanel().stop();
		SimulationSystem s = t.getSystem();
		try {
			Frame v = null;
			if(t.getParent() instanceof JComponent)
				v = (Frame) ((JComponent) t.getParent()).getTopLevelAncestor();
			else
				v = (Frame) t.getParent();
			ExportDialog d = new ExportDialog(v, true);
			d.setDir(homeDir);
			d.setLocationRelativeTo(v);
			d.setSystem(s);
			d.setVisible(true);
			if(d.getFile() != null) {
                s.export(d.getFile(), d.isTranslated(), d.getRadius());
                homeDir = d.getDir();
                if (d.isRun())
                    Main.runVCS(d.getFile(), homeDir);
            }
            
		} catch(Exception e) {
			JOptionPane.showMessageDialog(t, e.getMessage(), "Cannot export", JOptionPane.ERROR_MESSAGE);
		}

	}//GEN-LAST:event_exportItemActionPerformed

    
	private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitItemActionPerformed
		try {
			TorusApplet t = getApplet();
			t.stop();
			if(t.getParent() instanceof JComponent) {
				AppletViewer v = (AppletViewer) ((JComponent) t.getParent()).getTopLevelAncestor();
				v.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}//GEN-LAST:event_exitItemActionPerformed

	private void startItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startItemActionPerformed
		TorusAppletPanel p = getApplet().getPanel();
		p.toggle();
	}//GEN-LAST:event_startItemActionPerformed

	private void resetItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetItemActionPerformed
		TorusAppletPanel p = getApplet().getPanel();
		p.reset();
	}//GEN-LAST:event_resetItemActionPerformed

	private void aboutItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutItemActionPerformed
		TorusAppletPanel p = getApplet().getPanel();
		p.about();
	}//GEN-LAST:event_aboutItemActionPerformed

	private void guideItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guideItemActionPerformed
		TorusAppletPanel p = getApplet().getPanel();
		p.help();
	}//GEN-LAST:event_guideItemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutItem;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenuItem exportItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem guideItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem resetItem;
    private javax.swing.JPopupMenu.Separator separator1;
    private javax.swing.JMenuItem startItem;
    // End of variables declaration//GEN-END:variables

}
