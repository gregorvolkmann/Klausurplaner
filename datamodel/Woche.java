package datamodel;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import klausurmodel.Termin;
import farben.Farben;

public class Woche implements Observer{
	private Zeitraum zeitraum;
	private LinkedList<Tag> tage;

	public Woche(Zeitraum zeitraum) {
		this.tage = new LinkedList<Tag>();
		this.zeitraum = zeitraum;
		this.setWoche();

	}

	//Setzt die jeweiligen Tage mit korrektem Datum
	private void setWoche() {

		int anfangsTag = zeitraum.getVon().get(Calendar.DAY_OF_YEAR);

		for (int i = 0; i < 7; i++) {

			GregorianCalendar day = new GregorianCalendar();
			day.set(Calendar.DAY_OF_YEAR, anfangsTag + i);
			
			Tag t = new Tag(day);
			//Meldet sich bei den Tagen als Observer an
			t.addObserver(this);
			tage.add(t);
			
		}
	}

	public LinkedList<Tag> getTage() {
		return tage;
	}

	@Override
	public String toString() {
		return "Woche: \nvon: " + zeitraum.getVon().get(Calendar.YEAR) + "/"
				+ zeitraum.getVon().get(Calendar.MONTH) + 1 + "/"
				+ zeitraum.getVon().get(Calendar.DAY_OF_MONTH) + 1 + "\nbis: "
				+ zeitraum.getBis().get(Calendar.YEAR) + "/"
				+ zeitraum.getBis().get(Calendar.MONTH) + 1 + "/"
				+ zeitraum.getBis().get(Calendar.DAY_OF_MONTH) + 1;

	}

	public Zeitraum getZeitraum() {
		return this.zeitraum;
	}

	public void checkWoche(Klausur k) {

		for(Tag t : tage){
			if(t.getFarbe() != Farben.BLOCKIERT)
				t.setFarbe(Farben.GUT);
		}
		
		int sem = k.getSemester();

		// Falls in der Woche ueberhaupt Klausuren aus dem Semester sind
		if (hatKlausurVomSemester(sem)) {
			for (int i = 0; i < tage.size(); i++) {
				// Falls der richtige Tag eine Klausur vom Semester hat
				if (tage.get(i).hatKlausurVomSemester(sem)) {
					// Betroffene Tage markieren
					markiereTage(i);
				}
			}
		}
	}

	private boolean hatKlausurVomSemester(int sem) {
		for (Tag t : tage) {
			if (t.hatKlausurVomSemester(sem)) {
				return true;
			}
		}
		return false;
	}

	private void markiereTage(int index) {
		for (int i = 0; i < tage.size(); i++) {
			// Nur markieren, falls nicht blockiert ist
			if (!(tage.get(i).getFarbe() == Farben.BLOCKIERT)) {
				// gewaehlten Tag rot setzten
				if (abstand(i, index) == 0) {
					tage.get(i).setFarbe(Farben.SCHLECHT);
				}

				// Tage mit Abstand 1-2 geld setzten
				if (abstand(index, i) > 0 && abstand(index, i) < 3) {
					// Nur falls nicht schon rot gesetzt ist
					if (!(tage.get(i).getFarbe() == Farben.SCHLECHT)) {
						tage.get(i).setFarbe(Farben.MITTEL);
					}
				}
			}
		}
	}

	private int abstand(int a, int b) {
		if (a == b) {
			return 0;
		} else if (a < b) {
			return b - a;
		} else {
			return a - b;
		}
	}

	public void setzeNeutraleFarben(){
		for(Tag t : tage){
			if(t.getFarbe() != Farben.BLOCKIERT){
				t.setFarbe(Farben.NEUTRAL);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		checkTermineInWoche();
	}

	public void checkTermineInWoche() {
		//Itereriere ueber alle Tage der Woche
		for(int i = 0; i < tage.size(); i++){
			Tag t = tage.get(i);
			//Iteriere ueber alle Termine des jeweiligen Tages
			for(Termin termin : t.getTermine()){
				int sem = termin.getSemester();
				
				//Falls der Tag mehrere Termine im Semester von dem jeweiligen Termin hat
				if(t.hatMehrereKlausurenVonSemester(sem)){
					//Termin rot setzen
					termin.setFlag(Farben.SCHLECHT);
				//Wenn die Woche ueberhaupt Klausuren des Semester hat
				} else if(hatKlausurVomSemester(sem)){
					int min = 3;
					
					//Ermittle den kleinsten Abstand des Termins zu einem Termin des gleichen Semesters 
					for(int j = 0; j < tage.size(); j++){
						if(j != i){
							if(tage.get(j).hatKlausurVomSemester(sem)){
								int abstand = abstand(i, j);
								if(abstand < min)
									min = abstand;
							}
						}
						
					}
					if(min == 2 || min == 1){
						termin.setFlag(Farben.MITTEL);
					} else {
						termin.setFlag(Farben.GUT);
					}
				} 
			}
		}
	}

	public void setzeTermineNeutral(){
		for(Tag t : tage){
			t.setzteTermineNeutral();
		}
	}
}
