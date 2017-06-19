package error;

@SuppressWarnings("serial")
public class KlausurException extends Exception {
		
	public KlausurException(String msg){
		super(msg);
	}
	
	public KlausurException(){
		super();
	}
}
