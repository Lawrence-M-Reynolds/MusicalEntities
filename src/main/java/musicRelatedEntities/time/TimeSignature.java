package musicRelatedEntities.time;

import java.io.Serializable;

import musicRelatedEntities.note.noteAttributes.lengthValues.NoteLengthValue;

/**
 * Holds the time signature information. This is held as a number of beats per
 * bar, which is just an int, and the length value of each of those beats, 
 * which is a NoteLengthValue.
 * @author BAZ
 *
 */
public class TimeSignature implements Serializable{
	private int numberOfBeatsPerBar;
	private NoteLengthValue beatValue;

	public TimeSignature(int numberOfBeatsPerBar, NoteLengthValue beatValue) {
		this.numberOfBeatsPerBar = numberOfBeatsPerBar;
		this.beatValue = beatValue;
	}

	/**
	 * Returns the number of beats per bar.
	 * @return
	 */
	public int getNumberOfBeatsPerBar() {
		return numberOfBeatsPerBar;
	}

	/**
	 * Returns the NoteLengthValue representing the beat value of the
	 * time signature.
	 * @return
	 */
	public NoteLengthValue getBeatValue() {
		return beatValue;
	}
	
	/**
	 * Returns the number of 32nd notes that would be held in a bar that has
	 * this time signature.
	 * @return
	 */
	public int getNumberOf32ndNotesPerBar(){
		return (int)((numberOfBeatsPerBar* 32)/beatValue.getRecipricolValue());
	}
	
	public String toString(){
		return (numberOfBeatsPerBar + "/" + beatValue.getRecipricolValue());
	}

	@Override
	public boolean equals(Object obj) {
		TimeSignature timeSignatureToComapare = (TimeSignature)obj;
		return (this.getNumberOf32ndNotesPerBar() == timeSignatureToComapare.getNumberOf32ndNotesPerBar() &&
				this.getBeatValue() == timeSignatureToComapare.getBeatValue());
	}

	@Override
	public int hashCode() {
		return beatValue.getRecipricolValue();
	}
	

}
