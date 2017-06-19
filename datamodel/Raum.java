package datamodel;

public class Raum {
	@Override
	public String toString() {
		return "Raum: " + this.nummer + "    Kapazitaet: " + this.kapazitaet;
	}

	private int nummer;
	private int kapazitaet;
	
	public Raum(int nr, int kap){
		this.nummer = nr;
		this.kapazitaet = kap;
	}

	public int getNummer() {
		return nummer;
	}

	public int getKapazitaet() {
		return kapazitaet;
	}

	public void setNummer(int nummer) {
		this.nummer = nummer;
	}

	public void setKapazitaet(int kapazitaet) {
		this.kapazitaet = kapazitaet;
	}
}
