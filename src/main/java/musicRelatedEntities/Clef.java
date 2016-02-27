package musicRelatedEntities;

import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import musicRelatedEntities.note.NoteLetter;
import musicRelatedEntities.note.StaveLineNote;
import utilities.images.Symbol;
import utilities.images.SymbolFactory;




public class Clef implements Serializable{
	private transient Symbol clefSymbol;
	/** The location that the clef symbol centers on.*/
	private int pitchLineLocation;
	private StaveLineNote staveLineNote;
	private int spacingModifier;
	
	public static final Clef F_CLEF = new Clef(new StaveLineNote(3, NoteLetter.F), SymbolFactory.BASS_CLEF, 10, 35); 
	public static final Clef G_CLEF = new Clef(new StaveLineNote(4, NoteLetter.G), SymbolFactory.G_CLEF, 6, 35); 
	
	private Clef(final StaveLineNote aStaveLineNote, final Symbol aClefSymbol,
			final int aPitchLineLocation, final int aSpacingModifier){
		clefSymbol = aClefSymbol;
		pitchLineLocation = aPitchLineLocation;
		spacingModifier = aSpacingModifier;
		staveLineNote = aStaveLineNote;
	}
	
	/**
	 * A new instnce of the C clef is created and it will be centred on the line number
	 * specified. 
	 */
	public static Clef getMoveableC_Clef(final int aPitchLineLocation){
		return new Clef(new StaveLineNote(4, NoteLetter.C), SymbolFactory.C_CLEF, aPitchLineLocation, 35);
	}
	
	
	public void drawSymbol(Graphics g, int xPosition, int yPosition){
		clefSymbol.drawSymbol(g, xPosition, yPosition);
	}
	
	/**
	 * returns the line number that the clef symbol centers on.
	 * @return
	 */
	public int getpitchLineLocation(){
		return pitchLineLocation;
	}

	public int getSpacingModifier() {
		return spacingModifier;
	}

	public StaveLineNote getStaveLineNote() {
		return staveLineNote;
	}

	@Override
	public String toString() {
		return (staveLineNote.getNoteLetter() + " Clef");
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		
		//Buffered images aren't serializable so need to get them on loading the object.
		int staveLineNoteNumber = staveLineNote.getNoteNumber();
		NoteLetter staveLineNoteLetter = staveLineNote.getNoteLetter();
		
		if(staveLineNoteNumber == 3 && NoteLetter.F.equals(staveLineNoteLetter)){
			clefSymbol = SymbolFactory.BASS_CLEF;
		}else if(staveLineNoteNumber == 4 && NoteLetter.G.equals(staveLineNoteLetter)){
			clefSymbol = SymbolFactory.G_CLEF;
		}else{
			clefSymbol = SymbolFactory.C_CLEF;
		}
	}
}
