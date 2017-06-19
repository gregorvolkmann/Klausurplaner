package error;

@SuppressWarnings("serial")
public class DozentException extends Exception {
	
	public DozentException(String msg){
		super(msg);
	}
	
	public DozentException(){
		super();
	}
}
