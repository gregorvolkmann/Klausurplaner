package datamodel;

import java.util.Collections;
import java.util.LinkedList;


//Verwaltet alle Daten
public class Daten {
	private static Daten singleton = null;
	private LinkedList<Raum> raeume;
	private LinkedList<Fach> faecher;
	private LinkedList<Dozent> dozenten;
	private LinkedList<Klausur> klausuren;
	
	public static Daten getInstance(){
		if (singleton == null){
			singleton = new Daten();
			return singleton;
		}
		else
			return singleton;
	}
	
	private Daten(){
		raeume = new LinkedList<Raum>();
		faecher = new LinkedList<Fach>();
		dozenten = new LinkedList<Dozent>();
		klausuren = new LinkedList<Klausur>();
		
		//TEST
		faecher = new LinkedList<Fach>();
		faecher.add(new Fach(1337, "SWT"));
		faecher.add(new Fach(1338, "EiBo"));
		
		dozenten = new LinkedList<Dozent>();
		dozenten.add(new Dozent("Hans", "Meier", "HM"));
		dozenten.add(new Dozent("Peter", "Lustig", "PL"));
		
		raeume = new LinkedList<Raum>();
		raeume.add(new Raum(12, 12));
		raeume.add(new Raum(11, 60));
		
		klausuren.add(new Klausur(90, "mdl", 4, 10, raeume, dozenten, new Fach(100, "Testfach")));
		klausuren.add(new Klausur(90, "mdl", 3, 12, raeume, dozenten, new Fach(101, "Testfach")));
		klausuren.add(new Klausur(90, "mdl", 2, 5, raeume, dozenten, new Fach(102, "Testfach")));
	}
	
	public static void resetDaten(){
		singleton = new Daten();
	}

	public LinkedList<Raum> getRaeume() {
		return raeume;
	}

	public LinkedList<Fach> getFaecher() {
		return faecher;
	}

	public LinkedList<Dozent> getDozenten() {
		return dozenten;
	}

	public LinkedList<Klausur> getKlausuren() {
		return klausuren;
	}

	public void addKlausur(Klausur k){
		this.klausuren.add(k);
		Collections.sort(this.klausuren);
	}
	
	public boolean deleteKlausur(Klausur k){
		return this.klausuren.remove(k);
	}
	
	public void addFach(Fach f){
		this.faecher.add(f);
	}
	
	public boolean deleteFach(Fach f){
		return this.faecher.remove(f);
	}
	
	public void addDozent(Dozent d){
		this.dozenten.add(d);
	}
	
	public boolean deleteDozent(Dozent d){
		return this.dozenten.remove(d);
	}
	
	public void addRaum(Raum r){
		this.raeume.add(r);
	}
	
	public boolean deleteRaum(Raum r){
		return this.raeume.remove(r);
	}
	
	public Fach getFachByVorlesungsnummer(int vorlesungsNummer){
		for(Fach fach: faecher){
			if(fach.getvNr() == vorlesungsNummer)
				return fach;
		}
		return null;
	}
	
	public Dozent getDozentByKuerzel(String kuerzel){
		for(Dozent dozent: dozenten){
			if(dozent.getKuerzel().equals(kuerzel)){
				return dozent;
			}
		}
		return null;
	}
	
	public Raum getRaumByNummer(int raumNummer){
		for(Raum raum: raeume){
			if(raum.getNummer() == raumNummer){
				return raum;
			}
		}
		return null;
	}
}