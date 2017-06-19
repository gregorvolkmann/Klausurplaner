package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import datamodel.Tag;
import farben.Farben;

public class CalendarCell implements TableCellRenderer {
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object day,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		if(day instanceof Tag == false) {
			System.out.println("Object is not a <Tag> object.");
			return null;
		}

		table.setRowHeight(150);
		table.getColumnModel().getColumn(column).setPreferredWidth(150);
		
		Collections.sort(((Tag) day).getTermine());
		JList examList = new JList(((Tag) day).getTermine().toArray());
		examList.setDragEnabled(true);
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		JLabel l = new JLabel(((Tag) day).toString());
		l.setFont(new Font(l.getFont().getName(), l.getFont().getStyle(), 10));
		p.add(l);
		examList.setDragEnabled(true);
		examList.setOpaque(true);
		examList.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		p.add(examList);
		
		p.setBorder(BorderFactory.createLineBorder(Color.black));
		examList.setCellRenderer(new TerminCell());
		
		
		examList.setBackground(Color.GRAY);
		if(((Tag) day).getFarbe() == Farben.GUT){
			p.setBackground(new Color(170, 255, 170));
		} else if(((Tag) day).getFarbe() == Farben.MITTEL){
			p.setBackground(new Color(220, 255, 170));
		} else if(((Tag) day).getFarbe() == Farben.SCHLECHT){
			p.setBackground(new Color(255, 170, 170));
		} else if(((Tag) day).getFarbe() == Farben.BLOCKIERT){
			p.setBackground(Color.GRAY);
		} else if(((Tag) day).getFarbe() == Farben.NEUTRAL && hasFocus) {
			p.setBackground(new Color(170, 170, 170));
		} else {
			p.setBackground(Color.LIGHT_GRAY);
		}
		return p;
	}
}