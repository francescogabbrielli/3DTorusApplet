/*
 * TabEnabler.java
 *
 * Created on 23-nov-2010, 12.47.37
 */

package it.francescogabbrielli.apps.torusapplet;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Francesco Gabbrielli
 */
public class TabEnabler extends JPanel {

	private JTabbedPane tab;

    /** Creates new form BeanForm */
    public TabEnabler(JTabbedPane tab, String title) {
		this.tab = tab;
        initComponents();
		setTitle(title);
    }

	public void setTitle(String txt) {
		titleLabel.setText(txt);
	}

	public String getTitle() {
		return titleLabel.getText();
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        check = new javax.swing.JCheckBox();
        titleLabel = new javax.swing.JLabel();

        jCheckBox1.setText("jCheckBox1");

        setOpaque(false);
        setLayout(new java.awt.FlowLayout(0, 2, 0));

        check.setSelected(true);
        check.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                checkStateChanged(evt);
            }
        });
        add(check);

        titleLabel.setText("TabTitle");
        add(titleLabel);
    }// </editor-fold>//GEN-END:initComponents

	private void checkStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_checkStateChanged
		tab.getComponentAt(tab.indexOfTabComponent(this)).setEnabled(check.isSelected());
	}//GEN-LAST:event_checkStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox check;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

}
