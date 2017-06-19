package datamodel;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Observable;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import farben.Farben;
import klausurmodel.Termin;

public class Tag extends Observable implements ListModel{
	private GregorianCalendar date;
	private LinkedList<Termin> termine;
	private Farben farbe;

	private static final int MAX_TERMINE = 5;

	public Tag(GregorianCalendar date) {
		this.date = date;
		this.termine = new LinkedList<Termin>();
		
		if(this.date.get(Calendar.DAY_OF_WEEK) == 6 || this.date.get(Calendar.DAY_OF_WEEK) == 7){
			this.farbe = Farben.BLOCKIERT;
		}else {
			this.farbe = Farben.NEUTRAL;
		}
		
	}

	public GregorianCalendar getDate() {
		return date;
	}

	public int getAnzahlKlausuren() {
		return termine.size();
	}

	public LinkedList<Termin> getTermine() {
		return termine;
	}

	public void addTermin(Termin termin) {
		if(!istVoll() && !(getFarbe() == Farben.BLOCKIERT)){
			termine.add(termin);
			termin.getKlausur().setGesetzt(true);
			setChanged();
			notifyObservers();
		}else{
			System.out.println("Termin eintragen nicht moeglich");
		}
	}

	public boolean istVoll() {
		if (termine.size() >= MAX_TERMINE) {
			return true;
		}
		return false;
	}

	public boolean hatKlausurVomSemester(int semester) {
		for (Termin i : termine) {
			if (i.getSemester() == semester) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return date.get(Calendar.DAY_OF_MONTH) + "." + (date.get(Calendar.MONTH) + 1);
	}
	
	public Farben getFarbe(){
		return this.farbe;
	}
	
	public void setFarbe(Farben f){
		this.farbe = f;
	}

	@Override
	public void addListDataListener(ListDataListener arg0) {
		
	}

	@Override
	public Object getElementAt(int index) {
		return termine.get(index);
	}

	@Override
	public int getSize() {
		return termine.size();
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {

	}

	public boolean hatMehrereKlausurenVonSemester(int sem) {
		int count = 0;
		for(Termin t : termine){
			if(t.getSemester() == sem)
				count++;
		}
		return count > 1;
	}
	
	public void setzteTermineNeutral(){
		for(Termin t : termine){
			t.setFlag(Farben.NEUTRAL);
		}
	}
}
