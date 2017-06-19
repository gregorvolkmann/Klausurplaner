package view;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.TableModel;

import klausurmodel.Termin;
import klausurmodel.Terminkalender;
import datamodel.Klausur;
import datamodel.Tag;
import dragndrop.KlausurFlavor;
import error.DozentException;
import error.KlausurException;

@SuppressWarnings("serial")
public class CalendarTable extends JTable {
	public CalendarTable(final TableModel dataModel) {
		super(dataModel);

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setCellSelectionEnabled(true);
		setDefaultRenderer(Object.class, new CalendarCell());
		getColumnModel().getColumn(4).setCellEditor(new CalendarCellEditor());
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setRowHeight(200);

		setTransferHandler(new TransferHandler() {
			public boolean canImport(TransferSupport support) {

				if (support.isDataFlavorSupported(new KlausurFlavor())) {
					return true;
				}

				return false;
			}

			public boolean importData(TransferSupport support) {
				try {
					Klausur k = (Klausur) support.getTransferable()
							.getTransferData(new KlausurFlavor());
					
					if(k.istGesetzt()){
						return false;
					}
					Tag t = (Tag) dataModel.getValueAt(getSelectedRow(),
							getSelectedColumn());

					try {
						String uhrzeit = JOptionPane
								.showInputDialog("Bitte die Uhrzeit eingeben");
						GregorianCalendar date = (GregorianCalendar) t
								.getDate().clone();
						int hour = Integer.parseInt(uhrzeit.split(":")[0]);
						int minute = Integer.parseInt(uhrzeit.split(":")[1]);

						date.set(Calendar.HOUR_OF_DAY, hour);
						date.set(Calendar.MINUTE, minute);

						t.addTermin(new Termin(date, k));
						k.setGesetzt(true);
					} catch (KlausurException e) {
						JOptionPane.showInputDialog(e.toString());
					} catch (DozentException e) {
						JOptionPane.showInputDialog(e.toString());
					}
					((Terminkalender) dataModel).setzeNeutraleFarben();

					return true;

				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}
}
