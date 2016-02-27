package musicRelatedEntities.note.writtenNotes;

import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

import musicRelatedEntities.note.noteAttributes.Accidental;
import musicRelatedEntities.note.noteAttributes.lengthValues.LengthValue;
import musicRelatedEntities.note.noteAttributes.lengthValues.RestLengthValue;

/**
 * A WrittenNote is selected by the user from the note selector. It can all
 * of the properties associated with a note before it is placed on the stave
 * such as length and accidental value.
 * As soon as it is placed it will become either a single compositionBarEvent
 * or a collection of them. It will be then that it has a pitch value associated
 * with it.
 */
public class WrittenNote implements Comparable<WrittenNote>, Serializable{
	private LengthValue noteLengthValue;
	//TODO - should be a collection of accidentals to allow more than one. A natural
	//would just delete them all.
	//TODO acciendals should probably have a visible flag which will depend on
	//whether the note before and the same values.
	private Accidental accidental;
	// TODO This should also allow the x1.5 dot notation.
	
	/*
	 * TODO - Could possibly use a singleton way of doing this where a map is checked for
	 * written notes that have already been created.
	 */	
	/**
	 * This constructor is used in the algorithm for creating a composite note value
	 * in CompositeNoteValue.getCompositeNoteValueWithNumberOf32ndNotes(CompositeNoteValue)
	 * where the attributes of the first one should be carried over to the rest.
	 * @param WrittenNote writtenNote Previous note with the accidental value to apply
	 * to this WrittenNote.
	 * @param LengthValue lengthValue The length value of the note.
	 */
	public WrittenNote(final WrittenNote writtenNote, final LengthValue lengthValue){
		this.noteLengthValue = lengthValue;
		if(writtenNote != null){
			this.accidental = writtenNote.getAccidental();
		}
	}
	
	/**
	 * Used when creating written notes to be draw on to the panel - generated
	 * by the Note selector.
	 * @param lengthValue
	 * @param anAccidental
	 */
	public WrittenNote(final LengthValue lengthValue, final Accidental anAccidental){
		this.noteLengthValue = lengthValue;
		this.accidental = anAccidental;
	}

	public void drawSymbol(final Graphics g, final Point point){
		noteLengthValue.drawSymbol(g, point);
		if(accidental != null){
			accidental.drawSymbol(g, point);
		}
	}
	
	/**
	 * Indicates whether this is a rest or not.
	 * @return
	 */
	public boolean isRest(){
		return noteLengthValue.isRest();
	}

	/**
	 * Returns the number of 32nd note length values that could
	 * be held in this writtenNote.
	 * @return
	 */
	public int getNumberof32ndNotes(){
		return noteLengthValue.getNumberof32ndNotes();
	}
	
	/**
	 * Returns the LengthValue associated with this WrittenNote which may
	 * be a RestLengthValue or a NoteLengthValue.
	 * @return
	 */
	public LengthValue getNoteLengthValue(){
		return noteLengthValue;
	}

	/**
	 * Comparable is implemented here so that when a composite note is drawn
	 * (or composite rests) the smaller ones will be placed first and the 
	 * larger ones afterwards.
	 */
	public int compareTo(WrittenNote aNoteValueToCompare) {
		return this.getNoteLengthValue().getNumberof32ndNotes() - 
				aNoteValueToCompare.getNoteLengthValue().getNumberof32ndNotes();
	}

	/**
	 * Returns the accidental of this writtenNote. If there is none assciated
	 * with it then this class returns null.
	 * @return
	 */
	public Accidental getAccidental() {
		return accidental;
	}
	
	/**
	 * Returns the number of semitones that this WrittenNote should
	 * be modified by due to it's Accidental Value.
	 * @return
	 */
	public int getAccidentalMidiModifier(){
		if(accidental != null){
			return accidental.getIntervalChangeValue();
		}
		return 0;
	}
	
	public void convertToEquivalentRest(){
		noteLengthValue = 
				RestLengthValue.getRestLengthValueEquivalentNoteLengthValue(noteLengthValue);
	}
}
