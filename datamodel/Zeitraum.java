package datamodel;

import java.util.GregorianCalendar;

public class Zeitraum {
	private GregorianCalendar von;
	private GregorianCalendar bis;
	
	public Zeitraum(GregorianCalendar von, GregorianCalendar bis){
		this.von = von;
		this.bis = bis;
	}
	
	public GregorianCalendar getVon() {
		return von;
	}
	
	public GregorianCalendar getBis() {
		return bis;
	}
	
	public void setVon(GregorianCalendar date) {
		von = date;
	}
	
	public void setBis(GregorianCalendar date) {
		bis = date;
	}

	public boolean istIn(GregorianCalendar date){
		if(date.after(von) && date.before(bis)){
			return true;
		}
		return false;
	}
	
	public String toString() {
		String s = String.valueOf(this.von.get(GregorianCalendar.DAY_OF_MONTH))+"."+String.valueOf(this.von.get(GregorianCalendar.MONTH+1))+"."+String.valueOf(this.von.get(GregorianCalendar.YEAR))+
				" "+
				String.valueOf(this.von.get(GregorianCalendar.HOUR_OF_DAY))+":"+String.valueOf(this.von.get(GregorianCalendar.MINUTE))+
				" - "+
				String.valueOf(this.bis.get(GregorianCalendar.DAY_OF_MONTH))+"."+String.valueOf(this.bis.get(GregorianCalendar.MONTH+1))+"."+String.valueOf(this.bis.get(GregorianCalendar.YEAR))+
				" "+
				String.valueOf(this.bis.get(GregorianCalendar.HOUR_OF_DAY))+":"+String.valueOf(this.bis.get(GregorianCalendar.MINUTE));
		return s;
	}
}
