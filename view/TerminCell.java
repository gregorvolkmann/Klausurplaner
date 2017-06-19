package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import klausurmodel.Termin;
import farben.Farben;

public class TerminCell extends JLabel implements ListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object termin,
			int index, boolean selected, boolean hasFocus) {

		if (termin instanceof Termin == false) {
			System.out.println("Object is not a <Termin> object.");
			return null;
		}


		setText(((Termin) termin).toString());
		if (((Termin) termin).getFlag() == Farben.GUT) {
			setForeground(Color.GREEN);
		} else if (((Termin) termin).getFlag() == Farben.MITTEL) {
			setForeground(Color.YELLOW);
		} else if (((Termin) termin).getFlag() == Farben.SCHLECHT) {
			setForeground(Color.RED);
		} else if (((Termin) termin).getFlag() == Farben.NEUTRAL) {
			setForeground(Color.BLACK);
		}
		
		if(selected){
			setForeground(Color.CYAN);
		}
		
		setPreferredSize(new Dimension(150, 25));
		setBorder(BorderFactory.createLineBorder(Color.WHITE));
		return this;

	}

}
