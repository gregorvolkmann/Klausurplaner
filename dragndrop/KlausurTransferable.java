package dragndrop;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import datamodel.Klausur;

public class KlausurTransferable implements Transferable {

	private Klausur k;
	private DataFlavor [] flavors;
	public KlausurTransferable(Klausur k){
		this.k = k;
		this.flavors = new DataFlavor[] {new KlausurFlavor()};
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if(!flavor.equals(new KlausurFlavor())){
			throw new UnsupportedFlavorException(flavor);
		}
		return this.k;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		
		return this.flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(new KlausurFlavor());
	}

}
