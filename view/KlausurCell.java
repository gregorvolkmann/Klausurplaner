package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import datamodel.Klausur;

public class KlausurCell extends JLabel implements ListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object k,
			int index, boolean selected, boolean hasFocus) {

		if (k instanceof Klausur == false) {
			System.out.println("Object is not a <Klausur> object.");
			return null;
		}
		
		setForeground(Color.BLACK);
		
		if(hasFocus)
			setForeground(Color.BLUE);
		
		
		
		setText(((Klausur)k).toString());
		setPreferredSize(new Dimension(150, 25));
		setBorder(BorderFactory.createLineBorder(Color.WHITE));
		return this;

	}

}
