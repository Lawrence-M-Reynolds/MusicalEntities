package musicRelatedEntities.note;

import java.io.Serializable;

import musicRelatedEntities.key.KeyNote;
import musicRelatedEntities.note.noteAttributes.Accidental;




/**
 * This represents the note at each bar line/space. If the key has sharps or flats
 * then these are set via the setter.
 * This extends Note and is a more specific type as it also defines where it is located
 * on the stave in the bar, the note sequence number and it's midi value.
 * @author BAZ
 *TODO - This encapsulates a KeyNote which also extends the Note. This means the
 *information is stored twice.
 */
public class StaveLineNote extends Note implements Serializable{
	private int staveLineLocation;
	private int octaveNumber;
	private int midiValue;
	private KeyNote keyNote;
	//LOW rather than extending note it may be better for it to just encapsulate one.
	
	/**
	 * Creates a StaveLineNote with the octave number being how many octaves above the
	 * lowest midi value and the note letter being the notletter associated with the
	 * stave line. 
	 * The Midi value of the stave line note is worked out form these parameters.
	 * @param anOctaveNumber
	 * @param aNoteLetter
	 */
	public StaveLineNote(final int anOctaveNumber, final NoteLetter aNoteLetter){
		//Accidental doesn't seem to be used... But the letter is.
		super(aNoteLetter, null);
		octaveNumber = anOctaveNumber;
		/*
		 * TODO This isn't so good - the multiplication shouldn't be done in the letter
		 * and the method should be more descriptive.
		 */
		
		midiValue = aNoteLetter.getNoteLetterMidiValue(anOctaveNumber);
	}
	
	@Override
	public String toString(){
		return (Integer.toString(octaveNumber) + super.toString());
	}

	/**
	 * Returns the midi value associated with the StaveLineNote.
	 * @return
	 */
	public int getMidiValue() {
		return midiValue;
	}

	/**
	 * Returns the octave number of this stave line note which indicates how many
	 * octaves above the lowset midi value it is. 
	 * @return
	 */
	public int getNoteNumber() {
		return octaveNumber;
	}

	/**
	 * Returns the number associated with the stave lines position on the stave.
	 * @return
	 */
	public int getStaveLineLocation() {
		return staveLineLocation;
	}

	/**
	 * Sets the int value of the stave lines location on the stave.
	 * @param staveLineLocation
	 */
	public void setStaveLineLocation(int staveLineLocation) {
		this.staveLineLocation = staveLineLocation;
	}

	/**
	 * Sets the keyNote of the StaveLineNote.
	 */
	public void setKeyNote(KeyNote keyNote) {
		this.keyNote = keyNote;
	}

	/**
	 * Returns the KeyNote associated with this StaveLineNote.
	 * @return
	 */
	public KeyNote getKeyNote() {
		return keyNote;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof StaveLineNote)){
			return false;
		}
		StaveLineNote otherStaveLineNote = (StaveLineNote)obj;
		if(this.accidental != otherStaveLineNote.accidental
				|| this.keyNote != otherStaveLineNote.keyNote
				|| this.midiValue != otherStaveLineNote.midiValue
				|| this.noteLetter != otherStaveLineNote.noteLetter
				|| this.octaveNumber != otherStaveLineNote.octaveNumber
				|| this.staveLineLocation != otherStaveLineNote.staveLineLocation){
			
		}
		return super.equals(obj);
	}	
}
