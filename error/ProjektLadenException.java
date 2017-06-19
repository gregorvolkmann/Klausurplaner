package error;

@SuppressWarnings("serial")
public class ProjektLadenException extends Exception {
	public ProjektLadenException(){
		this("Fehler beim Laden des Projekts");
	}
	
	public ProjektLadenException(String message){
		super(message);
	}
}
