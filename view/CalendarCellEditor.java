package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import datamodel.Klausur;
import datamodel.Tag;
import dragndrop.KlausurTransferable;
import farben.Farben;

public class CalendarCellEditor extends AbstractCellEditor implements TableCellEditor {

	private List<CellEditorListener> listeners = new LinkedList<CellEditorListener>();
	private JList examList;


	@Override
	public Component getTableCellEditorComponent(JTable table, Object day,
			boolean isSelected, int row, int column) {
		if (day instanceof Tag == false) {
			System.out.println("Object is not a <Tag> object.");
			return null;
		}

		System.out.println("getTableCellEditorComponent");
		table.setRowHeight(200);
		table.getColumnModel().getColumn(column).setPreferredWidth(200);

		examList = new JList(((Tag) day).getTermine().toArray());
		examList.setBorder(BorderFactory.createLineBorder(Color.black));
		examList.setCellRenderer(new TerminCell());
		examList.setDragEnabled(true);
		
		examList.setTransferHandler(new TransferHandler() {

			public Transferable createTransferable(JComponent c) {
				JList lis = (JList) c;
				Klausur k = (Klausur) lis.getSelectedValue();

				Transferable trans = new KlausurTransferable(k);
				return trans;
			}

			public int getSourceActions(JComponent c) {
				return COPY_OR_MOVE;
			}

			public void exportDone(JComponent c, Transferable t, int action) {
				
			}

			public Icon getVisualRepresentation(Transferable t) {
				return super.getVisualRepresentation(t);
			}

		});

//		if (((Tag) day).getFarbe() == Farben.GUT) {
//			examList.setBackground(Color.GREEN);
//		} else if (((Tag) day).getFarbe() == Farben.MITTEL) {
//			examList.setBackground(Color.YELLOW);
//		} else if (((Tag) day).getFarbe() == Farben.SCHLECHT) {
//			examList.setBackground(Color.RED);
//		} else if (((Tag) day).getFarbe() == Farben.BLOCKIERT) {
//			examList.setBackground(Color.GRAY);
//		} else if (((Tag) day).getFarbe() == Farben.NEUTRAL) {
//			examList.setBackground(Color.WHITE);
//		}
		
		examList.setBackground(Color.BLUE);

		return new JLabel("Test");
	}

	@Override
	public void addCellEditorListener(CellEditorListener arg0) {
		listeners.add(arg0);
	}

	@Override
	public void cancelCellEditing() {
		ChangeEvent event = new ChangeEvent( this );
		for( CellEditorListener listener : 
			listeners.toArray( new CellEditorListener[ listeners.size() ] )){
 
			listener.editingCanceled( event );
		}
	}

	@Override
	public boolean isCellEditable(EventObject arg0) {
		return true;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener arg0) {
		listeners.remove(arg0);
	}

	@Override
	public boolean shouldSelectCell(EventObject arg0) {
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		ChangeEvent event = new ChangeEvent( this );
		for( CellEditorListener listener : 
			listeners.toArray( new CellEditorListener[ listeners.size() ] )){
 
			listener.editingStopped( event );
		}
 
		// informiert den Aufrufer, dass der Input korrekt ist.
		return true;
	}

	@Override
	public Object getCellEditorValue() {
		return examList;
	}

}
