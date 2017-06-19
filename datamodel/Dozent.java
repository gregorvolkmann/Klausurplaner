package datamodel;

import java.util.GregorianCalendar;
import java.util.LinkedList;

public class Dozent {
	@Override
	public String toString() {
		return this.name + ", " + this.vorname + " (" + this.kuerzel + ")";
	}

	private String vorname;
	private String name;
	private String kuerzel;
	private LinkedList<Fach> faecher;
	private LinkedList<Zeitraum> verhindert;
	
	public Dozent(String vorname, String name, String kuerzel){
		this.faecher = new LinkedList<Fach>();
		this.verhindert = new LinkedList<Zeitraum>();
		this.vorname = vorname;
		this.name = name;
		this.kuerzel = kuerzel;
	}
	
	public void addZeitraum(Zeitraum Zr){
		//Evtl vorher ueberpruefen
		this.verhindert.add(Zr);
	}
	
	public void addFach(Fach f){
		this.faecher.add(f);
	}
	
	public LinkedList<Fach> getFaecher(){
		return this.faecher;
	}
	
	public LinkedList<Zeitraum> getZeitraum(){
		return this.verhindert;
	}

	public String getVorname() {
		return vorname;
	}

	public String getName() {
		return name;
	}

	public String getKuerzel() {
		return kuerzel;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setKuerzel(String kuerzel) {
		this.kuerzel = kuerzel;
	}
	
	public boolean istVerhindert(GregorianCalendar date){
		for(Zeitraum zr : verhindert){
			if(zr.istIn(date))
				return true;
		}
		return false;
	}
	
	public void setZeitraeume(LinkedList<Zeitraum> zts) {
		this.verhindert = zts;
	}
	
	public void setFaecher(LinkedList<Fach> fer) {
		this.faecher = fer;
	}
	
}
