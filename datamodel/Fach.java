package datamodel;

public class Fach {
	private int vNr;
	private String bezeichnung;
	
	public Fach(int nr, String bez){
		this.vNr = nr;
		this.bezeichnung = bez;
	}

	public int getvNr() {
		return vNr;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setvNr(int vNr) {
		this.vNr = vNr;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	public String toString() {
		return this.vNr + "-" + this.bezeichnung;
	}
}
