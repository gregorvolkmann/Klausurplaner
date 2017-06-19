package datamodel;

import java.util.LinkedList;

public class Klausur implements Comparable<Klausur>{
	private long laenge;
	private String typ;
	private int semester;
	private int anzStudenten;
	
	private boolean gesetzt;

	private LinkedList<Raum> raeume;
	private LinkedList<Dozent> dozenten;
	private Fach fach;
	
	public Klausur(long laenge2, String typ, int semester, int anzStudenten, LinkedList<Raum> rooms, LinkedList<Dozent> profs, Fach f) {
		this.raeume = rooms;
		this.dozenten = profs;
		
		this.laenge = laenge2;
		this.typ = typ;
		this.semester = semester;
		this.anzStudenten = anzStudenten;
		
		this.fach = f;
		
		this.gesetzt = false;		
	}

	public int getSemester() {
		return semester;
	}
	
	public void addDozent(Dozent d) {
		this.dozenten.add(d);
	}

	public void addRaum(Raum r) {
		this.raeume.add(r);
	}
	
	public boolean istGesetzt(){
		return this.gesetzt;
	}
	
	public LinkedList<Dozent> getDozenten(){
		return this.dozenten;
	}
	
	public LinkedList<Raum> getRaeume(){
		return this.raeume;
	}
	
	public long getLaenge() {
		return laenge;
	}

	public String getTyp() {
		return typ;
	}

	public Fach getFach() {
		return fach;
	}
	
	public int getAnzStudenten() {
		return anzStudenten;
	}
	
	public void setGesetzt(boolean setze){
		gesetzt=setze;
	}


	//Testet ob genug Raueme fuer die Anzahl der Studs zu verfuegung ist
	//und testet ob genug Dozenten zugeteilt sind
	public boolean isCheck(){
		if(this.raeume.size() > this.dozenten.size()){
			System.out.println("Zu wenige Dozenten!!");
			return false;
		}
		
		int kapRaueme = 0;
		for(Raum raum : raeume){
			kapRaueme += raum.getKapazitaet();
		}
		
		if(kapRaueme < anzStudenten){
			System.out.println("Mehr Raueme benoetigt!!");
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return " -" + this.semester + "- " + this.fach.getBezeichnung();
	}
	
	@Override
	public int compareTo(Klausur o) {
		if(o.getSemester() < this.getSemester()){
			return -1;
		} else if(o.getSemester() > this.getSemester()){
			return 1;
		}
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Klausur){
			Klausur klausur = (Klausur)obj;
			if(klausur.getFach().getvNr() == getFach().getvNr()){
				return true;
			}
		}
		return false;
	}
}
