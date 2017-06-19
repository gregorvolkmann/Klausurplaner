package filemanager;
import datamodel.Projekt;
import error.ProjektLadenException;

/**
 * Interface zum beschreiben der benoetigten Methoden zum Laden eines Projektes oder einzelner Bestandteile.
 * @author 
 *
 */
public interface LoadManager {
	
	public void loadRaeume(String pfad) throws ProjektLadenException;
	public void loadFaecher(String pfad) throws ProjektLadenException;
	public void loadDozenten(String pfad) throws ProjektLadenException;
	public Projekt loadProjekt(String pfad) throws ProjektLadenException;
}
