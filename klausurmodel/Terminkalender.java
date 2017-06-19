package klausurmodel;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import datamodel.Klausur;
import datamodel.Tag;
import datamodel.Woche;
import datamodel.Zeitraum;

public class Terminkalender implements TableModel {
	private LinkedList<Woche> wochen;
	private Zeitraum pZeitraum;
	
	public Terminkalender(Zeitraum zeitraum){
		this.pZeitraum = setZeitraum(zeitraum);
		this.wochen = new LinkedList<Woche>();
		addWochen();
	}
	
	private Zeitraum setZeitraum(Zeitraum zr) {
		Zeitraum res = zr;
		
		//Wenn der Zeitraum nicht mit Montag beginnt
		int wochenTagAnfang = res.getVon().get(Calendar.DAY_OF_WEEK);
		
		if(wochenTagAnfang != 1){
			int jahresTagAnfang = res.getVon().get(Calendar.DAY_OF_YEAR);
			jahresTagAnfang = jahresTagAnfang - (wochenTagAnfang - 1);
			GregorianCalendar temp = res.getVon();
			temp.set(Calendar.DAY_OF_YEAR, jahresTagAnfang);
			
			res.setVon(temp);
		}
		
		int wochenTagEnde = res.getBis().get(Calendar.DAY_OF_WEEK);
		
		if(wochenTagEnde != 7) {
			int jahresTagEnde = res.getBis().get(Calendar.DAY_OF_YEAR);
			jahresTagEnde = jahresTagEnde + (7 - wochenTagEnde);
			GregorianCalendar temp = res.getBis();
			temp.set(Calendar.DAY_OF_YEAR, jahresTagEnde);
			
			res.setBis(temp);
		}
		
		return res;
	}
	
	
	// Fuellt sich selbst, anhand des Zeitraums 
	private void addWochen() {
		int anfangsTag = pZeitraum.getVon().get(Calendar.DAY_OF_YEAR);
		int endTag = pZeitraum.getBis().get(Calendar.DAY_OF_YEAR);
				
		for(int i = anfangsTag; i <= endTag; i+=7){
			
			GregorianCalendar anfangWoche = new GregorianCalendar();
			anfangWoche.set(Calendar.DAY_OF_YEAR, i);
			
			GregorianCalendar endeWoche = new GregorianCalendar();
			endeWoche.set(Calendar.DAY_OF_YEAR, i + 6);
			
			
			Zeitraum wochenZeitraum = new Zeitraum(anfangWoche, endeWoche);
			wochen.add(new Woche(wochenZeitraum));
		}
	}

	public void addTermin(Termin t){
		if(pZeitraum.istIn(t.getDate())){
			for(Woche w : wochen){
				if(w.getZeitraum().istIn(t.getDate())){
					for(Tag tag : w.getTage()){
						if(tag.getDate().get(Calendar.DAY_OF_MONTH) == t.getDate().get(Calendar.DAY_OF_MONTH)){
							tag.addTermin(t);
						}
					}
				}
			}
		}else{
			System.out.println("Termin liegt nicht in der Klausurphase");
		}
	}
	
	public void deleteTermin(Termin t){
		t.getKlausur().setGesetzt(false);
	}
	
	public LinkedList<Woche> getWochen(){
		return this.wochen;
	}
	
	//Prueft Termine, die ein eingefuegter, oder geloeschter Termin beeinflusst
	public void checkTermine(){
		for(Woche w : wochen){
			w.checkTermineInWoche();
		}
	}
	
	public void setzeTermineNeutral(){
		for(Woche w : wochen){
			w.setzeTermineNeutral();
		}
	}
	
 
	//Gibt ein KalenderCheckonjekt zurueck, in dem steht, welche Termine moeglich sind und welche nicht
//	public KalenderCheck checkKlausur(Klausur k){
//		return new KalenderCheck(this, k);
//	}
	
	public void checkKlausur(Klausur k){
		for(Woche w : wochen){
			w.checkWoche(k);
		}
	}
	
	public void setzeNeutraleFarben(){
		for(Woche w : wochen){
			w.setzeNeutraleFarben();
		}
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		
	}

	@Override
	public Class<Tag> getColumnClass(int columnIndex) {
		return Tag.class;
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Montag";
		case 1: 
			return "Dienstag";
		case 2:
			return "Mittwoch";
		case 3: 
			return "Donnerstg";
		case 4:
			return "Freitag";
		case 5: 
			return "Samstag";
		case 6:
			return "Sonntag";
		default:
			return "Tag" + (columnIndex + 1);
		}
		
	}

	@Override
	public int getRowCount() {
		return this.wochen.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.wochen.get(rowIndex).getTage().get(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
	}
	
	public GregorianCalendar getVon() {
		return pZeitraum.getVon();
	}
	
	public GregorianCalendar getBis() {
		return pZeitraum.getBis();
	}

	@Override
	public String toString() {
		String s = "";
		for(Woche w : wochen){
			for(Tag t : w.getTage()){
				for(Termin ter : t.getTermine()){
					s = s + ter.toString();
				}
			}
		}
		System.out.println("Jetzt");
		return s;
	}
	
	
}
