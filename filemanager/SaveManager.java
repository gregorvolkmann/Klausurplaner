package filemanager;
import klausurmodel.Terminkalender;

/**
 * Interface zum beschreiben der benoetigten Methoden zum Speichern eines Projektes oder einzelner Bestandteile.
 * @author
 *
 */
public interface SaveManager {
	
	public void speicherRaeume(String pfad);
	public void speicherFaecher(String pfad);
	public void speicherDozenten(String pfad);
	public void speicherProjekt(String pfad, Terminkalender kal);
}
