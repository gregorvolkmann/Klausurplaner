package datamodel;

import klausurmodel.Terminkalender;

public class Projekt {
	private Terminkalender cal;
	
	public Projekt(Terminkalender cal) {
		this.cal = cal;
	}
	
	public Terminkalender getTerminkalender() {
		return this.cal;
	}
	
	public void resetDaten() {
		Daten.resetDaten();
	}
}
