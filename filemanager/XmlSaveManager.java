package filemanager;
import java.io.FileWriter;
import klausurmodel.Termin;
import klausurmodel.Terminkalender;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import datamodel.*;

/**
 * Klasse zum Speichern der Raeume, Faecher, Dozenten oder gesammten Projekte.
 * @author 
 *
 */
public class XmlSaveManager implements SaveManager {

	@Override
	/**
	 * Methode zum Speichern der Raeume.
	 * @param pfad		Der Pfad im Dateisystem, an welchem die Xml-Datei gespeichert wird.
	 */
	public void speicherRaeume(String pfad) {
		//Dokument wird erstellt und Wurzelelement festgelegt.
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("raeume");
		
		//Xml-Struktur wird erzeugt und mit Werten befuellt.
		for(Raum aktuellerRaum: Daten.getInstance().getRaeume()){
			Element raum = root.addElement("raum");
			raum.addElement("nummer")
					.addText(Integer.toString(aktuellerRaum.getNummer()));
			raum.addElement("kapazitaet")
					.addText(Integer.toString(aktuellerRaum.getKapazitaet()));
		}
		schreibeXml(document, pfad);
	}

	@Override
	/**
	 * Methode zum Speichern der Faecher.
	 * @param pfad		Der Pfad im Dateisystem, an welchem die Xml-Datei gespeichert wird.
	 */
	public void speicherFaecher(String pfad) {
		//Dokument wird erstellt und Wurzelelement festgelegt.
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("faecher");
		
		//Xml-Struktur wird erzeugt und mit Werten befuellt.
		for(Fach aktuellesFach: Daten.getInstance().getFaecher()){
			Element fach = root.addElement("fach");
			fach.addElement("vlnr")
					.addText(Integer.toString(aktuellesFach.getvNr()));
			fach.addElement("bezeichnung")
					.addText(aktuellesFach.getBezeichnung());
		}		
		schreibeXml(document, pfad);
	}

	@Override
	/**
	 * Methode zum Speichern der Dozenten.
	 * @param pfad		Der Pfad im Dateisystem, an welchem die Xml-Datei gespeichert wird.
	 */
	public void speicherDozenten(String pfad) {
		//Dokument wird erstellt und Wurzelelement festgelegt.
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("dozenten");
		
		//Xml-Struktur wird erzeugt und mit Werten befuellt.
		for(Dozent aktuellerDozent: Daten.getInstance().getDozenten()){
			Element dozent = root.addElement("dozent");
			dozent.addElement("name")
					.addText(aktuellerDozent.getName());
			dozent.addElement("vorname")
					.addText(aktuellerDozent.getVorname());
			dozent.addElement("kuerzel")
					.addText(aktuellerDozent.getKuerzel());
			//Element faecher = dozent.addElement("faecher");
			Element zeitraeume = dozent.addElement("zeitraeume");
			
//			//Es wird geprueft ob der Dozent Faecher hat, wenn ja werden diese zur Xml-Struktur hinzugefuegt.
//			if (!d.getFaecher().isEmpty()){     // ???
//				for(Fach f: d.getFaecher()){
//					faecher.addElement("fach")
//							.addText(f.getBezeichnung());
//				}
//			}

			//Es wird geprueft ob der Dozent Zeitraeume hat, wenn ja werden diese zur Xml-Struktur hinzugefuegt.
			if (!aktuellerDozent.getZeitraum().isEmpty()){	// ???
				for(Zeitraum z: aktuellerDozent.getZeitraum()){
					Element zeitraum = zeitraeume.addElement("zeitraum");
					Element von = zeitraum.addElement("von");
					Element bis = zeitraum.addElement("bis");
					von.addText(z.getVon().getTime().toString());
					bis.addText(z.getBis().getTime().toString());  
				}
			}
		}
		schreibeXml(document, pfad);
	}

	@Override
	/**
	 * Methode zum Speichern der Projekte.
	 * @param pfad		Der Pfad im Dateisystem, an welchem die Xml-Datei gespeichert wird.
	 * @param kal		Terminkalender aus dem die Klausuren-Daten entnommen werden.
	 */
	public void speicherProjekt(String pfad, Terminkalender kal) {
		//Dokument wird erstellt und Wurzelelement festgelegt.
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("projekt");
		
		//Xml-Struktur wird erstellt
		Element optionenElement = root.addElement("optionen");
		Element dozentenElement = optionenElement.addElement("dozenten");
		Element raeumeElement = optionenElement.addElement("raeume");
		Element faecherElement = optionenElement.addElement("faecher");
		Element klausurplanElement = root.addElement("klausurplan");
		Element pruefungszeitraumElement = klausurplanElement.addElement("pruefungszeitraum");
		Element pruefungszeitraumVon = pruefungszeitraumElement.addElement("von");
		Element pruefungszeitraumBis = pruefungszeitraumElement.addElement("bis");
		Element klausurenElement = klausurplanElement.addElement("klausuren");
		
		//Fuelle Dozenten
		for(Dozent aktuellerDozent: Daten.getInstance().getDozenten()){
			Element dozent = dozentenElement.addElement("dozent");
			dozent.addElement("name")
					.addText(aktuellerDozent.getName());
			dozent.addElement("vorname")
					.addText(aktuellerDozent.getVorname());
			dozent.addElement("kuerzel")
					.addText(aktuellerDozent.getKuerzel());
			Element faecherDozent = dozent.addElement("dozentenfaecher");
			Element zeitraeume = dozent.addElement("zeitraeume");

			//Es wird geprueft ob der Dozent Faecher hat, wenn ja werden diese zur Xml-Struktur hinzugefuegt.
			if (!aktuellerDozent.getFaecher().isEmpty()){     // ???
				for(Fach f: aktuellerDozent.getFaecher()){
					faecherDozent.addElement("dozentenfach")
							.addText(Integer.toString(f.getvNr()));
				}
			}
			
			//Es wird geprueft ob der Dozent Zeitraeume hat, wenn ja werden diese zur Xml-Struktur hinzugefuegt.
			if (!aktuellerDozent.getZeitraum().isEmpty()){	// ???
				for(Zeitraum z: aktuellerDozent.getZeitraum()){
					Element zeitraum = zeitraeume.addElement("zeitraum");
					Element von = zeitraum.addElement("von");
					Element bis = zeitraum.addElement("bis");
					von.addText(z.getVon().getTime().toString());
					bis.addText(z.getBis().getTime().toString());  
				}
			}
		}	
		
		//Fuelle Raeume
		for(Raum aktuellerRaum: Daten.getInstance().getRaeume()){
			Element raum = raeumeElement.addElement("raum");
			raum.addElement("nummer")
					.addText(Integer.toString(aktuellerRaum.getNummer()));
			raum.addElement("kapazitaet")
					.addText(Integer.toString(aktuellerRaum.getKapazitaet()));
		}
		
		//Fuelle Faecher
		for(Fach aktuellesFach: Daten.getInstance().getFaecher()){
			Element fach = faecherElement.addElement("fach");
			fach.addElement("vlnr")
					.addText(Integer.toString(aktuellesFach.getvNr()));
			fach.addElement("bezeichnung")
					.addText(aktuellesFach.getBezeichnung());
		}		
		
		//Fuelle Pruefungszeitraum
		pruefungszeitraumVon.addText(kal.getVon().getTime().toString());
		pruefungszeitraumBis.addText(kal.getBis().getTime().toString());
		
		//Fuelle Klausuren
		for(Klausur aktuelleKlausur: Daten.getInstance().getKlausuren()){
			Element klausur = klausurenElement.addElement("klausur");
			klausur.addElement("laenge")
					.addText(Long.toString(aktuelleKlausur.getLaenge()));
			klausur.addElement("typ")
					.addText(aktuelleKlausur.getTyp());
			klausur.addElement("semester")
					.addText(Integer.toString(aktuelleKlausur.getSemester()));
			klausur.addElement("anzahl_studenten")
					.addText(Integer.toString(aktuelleKlausur.getAnzStudenten()));
			Element klausurenFach = klausur.addElement("klausuren_fach");
			klausurenFach.addElement("klausuren_fach_nummer")
					.addText(Integer.toString(aktuelleKlausur.getFach().getvNr()));
			klausur.addElement("gesetzt")
					.addText(aktuelleKlausur.istGesetzt() ? "true" : "false");
			
			//Fuege Raeume zu den Klausuren hinzu 
			Element klausurenRaeume = klausur.addElement("klausuren_raeume");  
			for(Raum r: aktuelleKlausur.getRaeume()){
				klausurenRaeume.addElement("klausuren_raum_nummer")
						.addText(Integer.toString(r.getNummer()));
			}
			//Fuege Dozenten zu den Klausuren hinzu
			Element klausurenDozenten = klausur.addElement("klausuren_dozenten");
			for(Dozent d: aktuelleKlausur.getDozenten()){
				klausurenDozenten.addElement("klausuren_dozent_kuerzel")
						.addText(d.getKuerzel());
			}
			//Fuegt Termindatum hinzu, falls die Klausur bereits einen Termin hat
			Element klausurenDatum = klausur.addElement("datum");
			if(aktuelleKlausur.istGesetzt()){
				// Nur wenn ein Termin fuer die Klausur gestezt ist, wird das Datums-Element gefuellt
				for (Woche w : kal.getWochen()){
					for (Tag tag : w.getTage()){
						// Ueber Termine iterieren
						for (Termin t : tag.getTermine()){
							if (t.getKlausur().equals(aktuelleKlausur)){
								// Falls es einen Termin fuer die Klausur gibt, wird das Datum-Element gefuellt
								klausurenDatum.addText(t.getDate().getTime().toString());
							}
						}
					}
				}		
			}	
		}
		schreibeXml(document, pfad);
	}
	/**
	 * Methode die das uebergebene Dokument als Xml-Datei im Pretty-Print-Format auf den gegebenen Pfad schreibt.
	 * @param dokument	Dokument, welches die Xml-Struktur und Daten enthaelt und geschrieben werden soll.
	 * @param pfad		Der Pfad im Dateisystem, an welchem die Xml-Datei gespeichert wird.
	 */
	public void schreibeXml(Document dokument, String pfad){
		FileWriter out=null;
		try {
			out = new FileWriter(pfad);
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer;
			writer = new XMLWriter(out, format);
	        writer.write( dokument );
	        out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
