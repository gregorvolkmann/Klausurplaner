package filemanager;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import klausurmodel.Termin;
import klausurmodel.Terminkalender;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import datamodel.Daten;
import datamodel.Dozent;
import datamodel.Fach;
import datamodel.Klausur;
import datamodel.Projekt;
import datamodel.Raum;
import datamodel.Zeitraum;
import error.DozentException;
import error.KlausurException;
import error.ProjektLadenException;

/**
 * Klasse zum Laden der Raeume, Faecher, Dozenten oder gesammten Projekte.
 * @author
 *
 */
public class XmlLoadManager implements LoadManager {

	@Override
	/**
	 * In dieser Methode werden Raeume aus einer Xml-Datei in das Programm geladen.
	 * @param pfad		Der Pfad im Dateisystem, von dem aus die Xml-Datei geladen wird. 
	 */
	public void loadRaeume(String pfad) throws ProjektLadenException {
		Document dokument=null;

		try {
			dokument = parse(pfad);
		} catch (Exception e) {
			throw new ProjektLadenException("Fehler beim Laden der R�ume.");
		}

		NodeList list = dokument.getElementsByTagName("raum");
		Daten.getInstance().getRaeume().clear();
		for (int i=0; i < list.getLength(); i++){
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE){
				Element aktuellerRaum = (Element)list.item(i);
				int nummer = Integer.parseInt(aktuellerRaum.getElementsByTagName("nummer").item(0).getTextContent());
				int kapazitaet = Integer.parseInt(aktuellerRaum.getElementsByTagName("kapazitaet").item(0).getTextContent());
				Daten.getInstance().addRaum(new Raum(nummer, kapazitaet));
			}
		}
	}

	@Override
	/**
	 * In dieser Methode werden Faecher aus einer Xml-Datei in das Programm geladen.
	 * @param pfad		Der Pfad im Dateisystem, von dem aus die Xml-Datei geladen wird. 
	 */
	public void loadFaecher(String pfad) throws ProjektLadenException {
		Document dokument=null;

		try {
			dokument = parse(pfad);
		} catch (Exception e) {
			throw new ProjektLadenException("Fehler beim Laden der F�cher.");
		}	
		
		NodeList list = dokument.getElementsByTagName("fach");
		Daten.getInstance().getFaecher().clear();
		for (int i=0; i < list.getLength(); i++){
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE){
				Element aktuellesFach = (Element)list.item(i);
				int vlnr = Integer.parseInt(aktuellesFach.getElementsByTagName("vlnr").item(0).getTextContent());
				String bezeichnung = aktuellesFach.getElementsByTagName("bezeichnung").item(0).getTextContent();
				Daten.getInstance().addFach(new Fach(vlnr, bezeichnung));
			}
		}
	}

	@Override
	/**
	 * In dieser Methode werden Dozenten aus einer Xml-Datei in das Programm geladen.
	 * @param pfad		Der Pfad im Dateisystem, von dem aus die Xml-Datei geladen wird. 
	 */
	public void loadDozenten(String pfad) throws ProjektLadenException {
		Document dokument=null;

		try {
			dokument = parse(pfad);
		} catch (Exception e) {
			throw new ProjektLadenException("Fehler beim Laden der Dozenten.");
		}		
		
		NodeList list = dokument.getElementsByTagName("dozent");
		Daten.getInstance().getDozenten().clear();
		for (int i=0; i < list.getLength(); i++){
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE){
				Element aktuellerDozent = (Element)list.item(i);
				String name = aktuellerDozent.getElementsByTagName("name").item(0).getTextContent();
				String vorname = aktuellerDozent.getElementsByTagName("vorname").item(0).getTextContent();
				String kuerzel = aktuellerDozent.getElementsByTagName("kuerzel").item(0).getTextContent();
				NodeList zeitraeume = aktuellerDozent.getElementsByTagName("zeitraum");
				Dozent aktDozent = new Dozent(vorname, name, kuerzel);
				
				for (int j=0; j < zeitraeume.getLength(); j++){
					if (zeitraeume.item(j).getNodeType() == Node.ELEMENT_NODE){
						Element zeitraum = (Element)zeitraeume.item(i);
						String von = zeitraum.getElementsByTagName("von").item(0).getTextContent();
						String bis = zeitraum.getElementsByTagName("bis").item(0).getTextContent();
						aktDozent.addZeitraum(new Zeitraum(erstelleKalender(von), erstelleKalender(bis)));		
					}
				}
				Daten.getInstance().addDozent(aktDozent);
			}
		}			
	}

	@Override
	/**
	 * In dieser Methode werden komplette Projekte aus einer Xml-Datei in das Programm geladen.
	 * @param pfad		Der Pfad im Dateisystem, von dem aus die Xml-Datei geladen wird. 
	 * @return Projekt	Das Projekt, welches aus den Raeumen Faecher Dozenten und Klausuren besteht.
	 */
	public Projekt loadProjekt(String pfad) throws ProjektLadenException {
		loadRaeume(pfad);
		loadFaecher(pfad);
		loadDozenten(pfad);
		
		Document dokument=null;
		try {
			dokument = parse(pfad);
		} catch (Exception e) {
			throw new ProjektLadenException("Fehler beim Laden des Projekts.");
		}	
		
		// Alte Klausuren clearen
		Daten.getInstance().getKlausuren().clear();
		
		// Den Dozenten die Faecher zuweisen
		NodeList dozentenElemente = dokument.getElementsByTagName("dozent");	
		
		// Ueber alle Dozenten-Elemente iterieren 
		for(int i=0; i < dozentenElemente.getLength(); i++){
			// Kuerzel des Dozents auslesen
			Element aktuellesDozentenEle = (Element)dozentenElemente.item(i);
			String kuerzel =  aktuellesDozentenEle.getElementsByTagName("kuerzel").item(0).getTextContent();
			
			// Passende Dozenten-Instanz suchen
			Dozent dozentInstanz = Daten.getInstance().getDozentByKuerzel(kuerzel); 
			
			if(dozentInstanz != null){
				// Dazugehoerige Dozent-Instanz gefunden und jetzt das Fach-Element auslesen
				NodeList dozentenFaecher = aktuellesDozentenEle.getElementsByTagName("dozentenfach");
				for(int j=0; j < dozentenFaecher.getLength(); j++){
					// Passende Fach-Instanz suchen
					String aktuelleVorlesungsNummer = ((Element)dozentenFaecher.item(j)).getTextContent();
					Fach fach = Daten.getInstance().getFachByVorlesungsnummer(Integer.parseInt(aktuelleVorlesungsNummer));
					if(fach != null){
						dozentInstanz.addFach(fach);	// Fach zum Dozenten hinzufuegen
					}
					else{
						// TODO keine passende Fachinstanz gefunden
						throw new ProjektLadenException("Fehler beim Laden des Projekts.");
					}
				}
			}
		}
		
		//Pruefungsphasen-Terminkalender erstellen
		Terminkalender kalender = null;
		Projekt projekt = null;
		
		NodeList pruefungsZeitraum = dokument.getElementsByTagName("pruefungszeitraum");
		for (int i=0; i < pruefungsZeitraum.getLength(); i++){
			if (pruefungsZeitraum.item(i).getNodeType() == Node.ELEMENT_NODE){
				Element zeitraum = (Element)pruefungsZeitraum.item(i);
				String von = zeitraum.getElementsByTagName("von").item(0).getTextContent();
				String bis = zeitraum.getElementsByTagName("bis").item(0).getTextContent();
				kalender = new Terminkalender(new Zeitraum(erstelleKalender(von), erstelleKalender(bis)));
				projekt = new Projekt(kalender);
			}
		}
		
		// Klausuren hinzufuegen
		NodeList klausurenElemente = dokument.getElementsByTagName("klausur");
		
		for (int i=0; i < klausurenElemente.getLength(); i++){
			LinkedList<Raum> raeume = new LinkedList<Raum>();
			LinkedList<Dozent> dozenten = new LinkedList<Dozent>();
			Fach fach;
			int laenge, semester, anzahlStudenten;
			String typ, gesetzt;
			
			Element aktuelleKlausur = (Element)klausurenElemente.item(i);
			
			// Werte der Klausur auslesen
			laenge = Integer.parseInt(aktuelleKlausur.getElementsByTagName("laenge").item(0).getTextContent());
			typ = aktuelleKlausur.getElementsByTagName("typ").item(0).getTextContent();
			semester = Integer.parseInt(aktuelleKlausur.getElementsByTagName("semester").item(0).getTextContent());
			anzahlStudenten = Integer.parseInt(aktuelleKlausur.getElementsByTagName("anzahl_studenten").item(0).getTextContent());
			gesetzt = aktuelleKlausur.getElementsByTagName("gesetzt").item(0).getTextContent();
			String datum = aktuelleKlausur.getElementsByTagName("datum").item(0).getTextContent();	// kann leer sein
			
			// Fachinstanz anhand der Vorlesungsnummer finden (1 Fach pro Klausur)
			int klausurenFachnummer = Integer.parseInt(aktuelleKlausur.getElementsByTagName("klausuren_fach_nummer").item(0).getTextContent());
			fach = Daten.getInstance().getFachByVorlesungsnummer(klausurenFachnummer);
			
			// Liste der Dozentinstanzen der Klausur anhand der Kuerzel finden
			NodeList kuerzelElemente = aktuelleKlausur.getElementsByTagName("klausuren_dozent_kuerzel");
			
			for (int j=0; j < kuerzelElemente.getLength(); j++){
				String kuerzel = kuerzelElemente.item(j).getTextContent();
				Dozent dozent = Daten.getInstance().getDozentByKuerzel(kuerzel);
				if(dozent != null){
					dozenten.add(dozent);
				}
			}
			
			// Raeume der Klausur anhand der Raumnummer finden
			NodeList raeumeDozenten = aktuelleKlausur.getElementsByTagName("klausuren_raum_nummer");
			
			for (int j=0; j < raeumeDozenten.getLength(); j++){
				String raumNummer = raeumeDozenten.item(j).getTextContent();
				Raum raum = Daten.getInstance().getRaumByNummer(Integer.parseInt(raumNummer));
				if(raum != null){
					raeume.add(raum);
				}
			}
			
			// Dann eine Klausur erstellen
			if(fach != null && raeume.size() > 0 && dozenten.size() > 0){
				Klausur klausur = new Klausur(laenge, typ, semester, anzahlStudenten, raeume, dozenten, fach);
				Daten.getInstance().addKlausur(klausur);
				boolean setzen = gesetzt.equals("true") ? true : false;
				//klausur.setGesetzt(setzen);
				Termin klausurTermin = null;
				// Wenn es ein Datum-Element zur Klausur gibt, wird ein Termin erstellt
				if(setzen && !datum.isEmpty()){
					try {
						klausurTermin = new Termin(erstelleKalender(datum), klausur);
					} catch (KlausurException e) {
						throw new ProjektLadenException("Fehler beim Laden eines Termins, Klausurtermin konnte nicht gesetzt werden.");
					} catch (DozentException e) {
						throw new ProjektLadenException("Fehler beim Laden eines Termins, Dozent nicht verfuegbar.");
					}
					kalender.addTermin(klausurTermin);	// set gesetzt der Klausur wird beim setzen des Termins auf true gesetzt (wenn erfolgreich)
					System.out.println("add Termin to cal");
				}
			}
		}	
		return projekt;
	}
	/**
	 * Methode zum umwandeln einer Xml-Datei in ein file-Objekt.
	 * @param pfad		Dateipfad der zu lesenden Xml-Datei
	 * @return file		File-Objekt mit der Xml-Struktur.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document parse(String pfad) throws ParserConfigurationException, SAXException, IOException{
		File file = new File(pfad);
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return builder.parse(file);
    }
	/**
	 * Methode erstellt aus einem Datum im String-Format einen GregorianCalendar.
	 * @param datum					Datum im String-Format
	 * @return	GregorianCalendar	Datum im Gregoian-Calendar-Format
	 */
	public GregorianCalendar erstelleKalender(String datum) {
		String[] date = datum.split(" ");
		int tag = Integer.parseInt(date[2].trim());
		int jahr = Integer.parseInt(date[5].trim());
		String[] time = date[3].split(":");
		int stunde = Integer.parseInt(time[0].trim());
		int minute = Integer.parseInt(time[1].trim());
		int monat=0;
		if (date[1].trim().equals("Jan"))
			monat = 0;
		else if (date[1].trim().equals("Feb"))
			monat = 1;	
		else if (date[1].trim().equals("Mar"))
			monat = 2;	
		else if (date[1].trim().equals("Apr"))
			monat = 3;	
		else if (date[1].trim().equals("May"))
			monat = 4;	
		else if (date[1].trim().equals("Jun"))
			monat = 5;	
		else if (date[1].trim().equals("Jul"))
			monat = 6;	
		else if (date[1].trim().equals("Aug"))
			monat = 7;	
		else if (date[1].trim().equals("Sep"))
			monat = 8;	
		else if (date[1].trim().equals("Oct"))
			monat = 9;	
		else if (date[1].trim().equals("Nov"))
			monat = 10;	
		else if (date[1].trim().equals("Dec"))
			monat = 11;
		
//		System.out.println("monat :"+monat);		
		return new GregorianCalendar(jahr, monat, tag, stunde, minute);
	}
}
