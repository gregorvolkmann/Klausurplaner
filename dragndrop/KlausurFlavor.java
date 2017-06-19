package dragndrop;

import java.awt.datatransfer.DataFlavor;

import datamodel.Klausur;

public class KlausurFlavor extends DataFlavor {
	public KlausurFlavor(){
		super(Klausur.class, "Klausur");
	}
}
