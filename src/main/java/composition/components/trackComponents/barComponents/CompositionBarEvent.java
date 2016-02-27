package composition.components.trackComponents.barComponents;

import java.awt.Point;
import java.io.Serializable;
import java.util.Collection;

import composition.reporting.AnnotationObject;


import musicRelatedEntities.BarEvent;
import musicRelatedEntities.note.StaveLineNote;
import musicRelatedEntities.note.noteAttributes.lengthValues.RestLengthValue;
import musicRelatedEntities.note.writtenNotes.WrittenNote;

/**
 * A CompositionBarEvent can be either a rest or note of any length in a bar. It contains all of the information
 * combining the properties of the WrittenNote selected by the user form the note selector, the stave line note 
 * which has information on the function of the note in the key (through the KeyNote) as well as exactly
 * what pitch it is. It also has any other details such as whether it is tied, it's time location, accidentals
 * associated with it... basically everything that the sound generation would need to know in order to turn
 * it into a sound.
 * 
 * @author BAZ
 *
 */
public class CompositionBarEvent  extends BarEvent implements Comparable<CompositionBarEvent>, Serializable{
//LOW - remove stavePosition from classes if possible as this should now only be held in the StaveLineNote
	private WrittenNote writtenNote;
	private StaveLineNote staveLineNote;
	private int stavePosition;
	private int timeLocation;
	private boolean isTied;
	private int trackNumber;
	
	private Collection<AnnotationObject> counterpointAnnotationObjects; 
	
	public CompositionBarEvent(final StaveLineNote aStaveLineNote, final int aTimeLocation, 
			final WrittenNote aWrittenNote, final int aStavePosition, final boolean isTiedBoolean, int aTrackNumber,
			Collection<AnnotationObject> aColOfCounterpointAnnotationObjects){
		timeLocation = aTimeLocation;
		writtenNote = aWrittenNote;
		stavePosition = aStavePosition;
		isTied = isTiedBoolean;
		staveLineNote = aStaveLineNote;
		trackNumber = aTrackNumber;
		counterpointAnnotationObjects = aColOfCounterpointAnnotationObjects;
	}
	
	/**
	 * If the compositon has been through the evaluator algorithm then it may have counterpoint
	 * annotation objects associated with some of the counterpointBarEvents. The point information
	 * required for the markings to be made for the rule reports is passed into this method
	 * whent the drawer draws the event.
	 * 
	 * When this is done the same annontation object is stored in the compositions rule report and it's 
	 * drawing logic will be able to use the point informationin the annotation object.
	 * @param point
	 */
	public void addPointToCounterpointAnnotationObject(Point point){
		if(counterpointAnnotationObjects != null){
			for(AnnotationObject counterpointAnnotationObject : counterpointAnnotationObjects){
				counterpointAnnotationObject.addPoint(point);
			}
		}
	}
	
	/**
	 * Works out the midi value by adding the midi value associated with the midi line to the 
	 * modified amount of the accidental of the written note.
	 */
	public int getMidiValue(){
		return (staveLineNote.getMidiValue() + writtenNote.getAccidentalMidiModifier());
	}
	
	/**
	 * Returns the WrittenNote object that was selected by the user form the note selector before it
	 * was written.
	 */
	public WrittenNote getWrittenNote() {
		return writtenNote;
	}
	
	/**
	 * Returns the numerical value defining where the note is on it's stave. 0 being the very bottom and 
	 * 17 being the top.
	 */
	public int getStavePosition() {
		return stavePosition;
	}
	
	/**
	 * Sets the WrittenNote of the CompositionBarEvent.
	 * @param aNoteValue
	 */
	public void setWrittenNote(WrittenNote aNoteValue) {
		this.writtenNote = aNoteValue;
	}
	
	/**
	 * Sets the stave position of the CompositionBarEvent.
	 * @param stavePosition
	 */
	public void setStavePosition(int stavePosition) {
		this.stavePosition = stavePosition;
	}	

	/**
	 * Orders the CompositionBarEvents according to their time starting location
	 * in the bar.
	 */
	public int compareTo(CompositionBarEvent noteEvent) {
		return this.timeLocation - noteEvent.timeLocation;
	}
	
	public boolean equals(Object object){
		if(!(object instanceof CompositionBarEvent)){
			return false;
		}
		
		CompositionBarEvent aBarEvent = (CompositionBarEvent) object;		
		if(		this.timeLocation != aBarEvent.timeLocation ||
				this.writtenNote != aBarEvent.writtenNote ||
				this.stavePosition != aBarEvent.stavePosition ||
				this.isTied != aBarEvent.isTied
		){
			return false;
		}
		return true;
	}

	/**
	 * Returns the CompositionBarEvents time starting location.
	 */
	public int getTimeLocation() {
		return timeLocation;
	}

	/**
	 * Returns true if the CompositionBarEvent is tied.
	 */
	public boolean isTied() {
		return isTied;
	}

	/**
	 * Sets the CompositionBarEvent tied status.
	 * @param isTied
	 */
	public void setTied(boolean isTied) {
		this.isTied = isTied;
	}
	
	public String toString(){
		return staveLineNote.toString();
	}
	
	/**
	 * Returns the CompositionBarEvent length in terms of 32nd notes.
	 */
	public int getEventLength(){
		return this.writtenNote.getNumberof32ndNotes();
	}
	
	/**
	 * Returns the CompositionBarEvent Rest status, indicating whether it
	 * is tied over to the next note or not.
	 */
	public boolean isRest(){
		return this.writtenNote.isRest();
	}

	/**
	 * Returns the tracknumber of the track in to which this CompositionBarEvent
	 * belongs to.
	 * @return
	 */
	public int getTrackNumber() {
		return trackNumber;
	}

	/**
	 * Returns the StaveLineNote that belongs to this CompositionBarEvent.
	 */
	public StaveLineNote getStaveLineNote() {
		return staveLineNote;
	}
	
	public void convertToRest(){
		writtenNote.convertToEquivalentRest();
		stavePosition = RestLengthValue.REST_PITCH_LINE_VALUE;
	}
}
