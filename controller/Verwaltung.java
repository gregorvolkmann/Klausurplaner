package controller;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.table.TableModel;

import com.itextpdf.text.DocumentException;

import pdfcontroller.PDFManager;

import klausurmodel.Terminkalender;
import view.CalendarTable;
import view.GUI;
import datamodel.Daten;
import datamodel.Projekt;
import datamodel.Zeitraum;
import error.ProjektLadenException;
import filemanager.LoadManager;
import filemanager.SaveManager;
import filemanager.XmlLoadManager;
import filemanager.XmlSaveManager;

public class Verwaltung {
	private SaveManager sm;
	private LoadManager lm;
	private PDFManager pdfM;
	private Projekt projekt;
	
	public Verwaltung(Zeitraum zr) {
		this.sm = new XmlSaveManager();
		this.lm = new XmlLoadManager();
		this.pdfM = new PDFManager();
		
		// Leeres Projekt instanziieren
		Terminkalender kalender = newCal(zr);
		projekt = new Projekt(kalender);
	}
	
	public void loadProjekt(String pfad) throws ProjektLadenException{
		if(!pfad.endsWith(".xml")) {
			throw new ProjektLadenException("Falscher Dateityp. Bitte waehlen Sie eine XML-Datei zum Laden.");
		}
		
		this.projekt = lm.loadProjekt(pfad);
		System.out.println(projekt.getTerminkalender());
	}
	
	public void loadRaeume(String pfad) throws ProjektLadenException{
		if(!pfad.endsWith(".xml")) {
			throw new ProjektLadenException("Falscher Dateityp. Bitte waehlen Sie eine XML-Datei zum Laden.");
		}
		lm.loadRaeume(pfad);
	}
	
	public void loadFaecher(String pfad) throws ProjektLadenException{
		if(!pfad.endsWith(".xml")) {
			throw new ProjektLadenException("Falscher Dateityp. Bitte waehlen Sie eine XML-Datei zum Laden.");
		}
		lm.loadFaecher(pfad);		
	} 
	
	public void loadDozenten(String pfad) throws ProjektLadenException{
		if(!pfad.endsWith(".xml")) {
			throw new ProjektLadenException("Falscher Dateityp. Bitte waehlen Sie eine XML-Datei zum Laden.");
		}
		lm.loadDozenten(pfad);		
	} 
	
	public void speichereRaeume(String pfad) {
		if(!pfad.endsWith(".xml")) {
			pfad += ".xml";
		}
		sm.speicherRaeume(pfad);
	}
	
	public void speichereFaecher(String pfad) {
		if(!pfad.endsWith(".xml")) {
			pfad += ".xml";
		}
		sm.speicherFaecher(pfad);
	}
	
	public void speichereDozenten(String pfad) {
		if(!pfad.endsWith(".xml")) {
			pfad += ".xml";
		}
		sm.speicherDozenten(pfad);
	}
	
	public void saveProjekt(String pfad) {
		if(!pfad.endsWith(".xml")) {
			pfad += ".xml";
		}
		
		this.sm.speicherProjekt(pfad, this.projekt.getTerminkalender());
	}
	
	public Terminkalender newCal(Zeitraum zr) {
		Terminkalender kal = new Terminkalender(zr);
		
		return kal;
	}
	
	public static void main(String[] args) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, 21);
		Verwaltung verwaltung = new Verwaltung(new Zeitraum(new GregorianCalendar(), cal));
		
		GUI gui = new GUI(verwaltung);
		gui.setVisible(true);
	}


	public TableModel getCal() {
		return this.projekt.getTerminkalender();
	}

	public Daten getDaten() {
		return Daten.getInstance();
	}
	
	public Projekt getProjekt(){
		return projekt;
	}

	public void exportPDF(String pfad, CalendarTable calendarTable) throws FileNotFoundException, DocumentException {
		pdfM.exportPDF(pfad, calendarTable);
	}


}
