package klausurmodel;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;

import datamodel.Dozent;
import datamodel.Klausur;
import error.DozentException;
import error.KlausurException;
import farben.Farben;


public class Termin extends Observable implements Comparable<Termin> {
	private GregorianCalendar date;
	private Klausur klausur;
	//Zeigt an, ob der Termin gut/mittel/schlecht liegt
	private Farben farbFlag;
	
	public Termin(GregorianCalendar date, Klausur k) throws KlausurException, DozentException{
		this.klausur = k;

		this.date = date;

		if(!k.isCheck()){
			throw new KlausurException("Klausur kann noch nicht gesetzt werden\nBitte Anzahl der Dozenten oder Raueme pruefen");
		}
		
		for(Dozent d : k.getDozenten()){
			if(d.istVerhindert(date)){
				throw new DozentException("Dozent " + d + " ist zu dem Termin verhindert");
			}
		}
	}
	
	public GregorianCalendar getDate(){
		return this.date;
	}
	
	public Klausur getKlausur(){
		return this.klausur;
	}

	public int getSemester() {
		return klausur.getSemester();
	}

	public Farben getFlag(){
		return this.farbFlag;
	}
	
	public void setFlag(Farben f){
		this.farbFlag = f;
		notifyObservers(this);
	}

	@Override
	public String toString() {
		
		return this.klausur.getFach().getBezeichnung() + " - " + this.date.get(Calendar.HOUR_OF_DAY) + ":" + this.date.get(Calendar.MINUTE);
	}

	@Override
	public int compareTo(Termin o) {
		return this.date.compareTo(o.getDate());
	}
}
