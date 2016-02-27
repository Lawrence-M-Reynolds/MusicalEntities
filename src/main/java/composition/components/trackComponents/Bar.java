package composition.components.trackComponents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import composition.components.trackComponents.barComponents.CompositionBarEvent;
import composition.components.trackComponents.barComponents.PitchLineMidiMapper;
import composition.reporting.AnnotationObject;

import musicRelatedEntities.Clef;
import musicRelatedEntities.key.Key;
import musicRelatedEntities.note.StaveLineNote;
import musicRelatedEntities.note.noteAttributes.lengthValues.NoteLengthValue;
import musicRelatedEntities.note.noteAttributes.lengthValues.RestLengthValue;
import musicRelatedEntities.note.writtenNotes.CompositeWrittenNote;
import musicRelatedEntities.note.writtenNotes.WrittenNote;
import musicRelatedEntities.time.Tempo;
import musicRelatedEntities.time.TimeSignature;



/**
 * Represents a single bar from a single track/voice. Each bar contains the clef, time signature,
 * key and Tempo as these are things that could change from one bar to the next.
 * 
 * TODO - There is a lot of logic in this class which should probably be put into a separate class.
 * @author BAZ
 *
 */
public class Bar implements Comparable<Bar>, Iterable<CompositionBarEvent>, Serializable{
	//Track Events
	private Clef clefType;
	private TimeSignature timeSignature;
	private Key key;
	private Tempo tempo;
	private PitchLineMidiMapper pitchLineMidiMapper;
	private int barNumber;
	private int trackNumber;
	private VoicePitchRangeLimitObject voicePitchRangeLimitObject;
	
	protected List<CompositionBarEvent> compositionBarEvents = new ArrayList<CompositionBarEvent>();
	
	/**
	 * Creates a new Bar with the supplied details. This shouldn't be
	 * called directly (which is why it's package friendly). Instead,
	 * use BarManager.createBar(...) which also sets the inital
	 * rest.
	 * @param aClefType
	 * @param aTimeSignature
	 * @param aKey
	 * @param aTempo
	 * @param aBarNumber
	 * @param aTrackNumber
	 * @param barVoicePitchRangeLimitObject 
	 */
	Bar(final Clef aClefType, final TimeSignature aTimeSignature, 
			final Key aKey, final Tempo aTempo, final int aBarNumber, final int aTrackNumber,
			final VoicePitchRangeLimitObject barVoicePitchRangeLimitObject,
			final PitchLineMidiMapper aPitchLineMidiMapper){
		clefType = aClefType;
		timeSignature = aTimeSignature;
		key = aKey;
		tempo = aTempo;
		barNumber = aBarNumber;
		pitchLineMidiMapper = aPitchLineMidiMapper;
		trackNumber = aTrackNumber;
		voicePitchRangeLimitObject = barVoicePitchRangeLimitObject;
	}

	/**
	 * Returns the bar sequence number in the track.
	 * @return
	 */
	public int getBarNumber() {
		return barNumber;
	}

	/**
	 * Sets the bar sequence number in the track. This doesn't manage all of the other
	 * bars so this should be handled when calling this method.
	 * @param barNumber
	 */
	public void setBarNumber(int barNumber) {
		this.barNumber = barNumber;
	}

	/**
	 * Returns the ClefType of this bar.
	 * @return
	 */
	public Clef getClefType() {
		return clefType;
	}

	/**
	 * Sets the Clef Type of the bar.
	 * @param clefType
	 */
	public void setClefType(Clef clefType) {
		this.clefType = clefType;
	}
	
	/**
	 * Orders the bar by their sequence number.
	 */
	public int compareTo(Bar aBar) {
		return (this.getBarNumber() - aBar.getBarNumber());
	}

	/**
	 * Returns the time signature of the bar.
	 * @return
	 */
	public TimeSignature getTimeSignature() {
		return timeSignature;
	}

	/**
	 * Returns the key of the bar.
	 * @return
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * Returns the tempo of the bar.
	 * @return
	 */
	public Tempo getTempo() {
		return tempo;
	}

	/**
	 * Sets the List of CompositionBarEvents that is held in this bar.
	 */
	public void setCompositionBarEvents(
			List<CompositionBarEvent> compositionBarEvents) {
		this.compositionBarEvents = compositionBarEvents;
	}

	/**
	 * Allows iteration through the CompositionBarEvents.
	 */
	public Iterator<CompositionBarEvent> iterator() {
		return compositionBarEvents.iterator();
	}
	
	/**
	 * Returns the first CompositionBarEvent that is held
	 * in this bar.
	 * @return
	 */
	public CompositionBarEvent getFirstCompositionBarEvent(){
		return compositionBarEvents.get(0);
	}
	
	/**
	 * Returns the total number of 32nd notes that could be held within
	 * this bar.
	 * @return
	 */
	public int getNumberOf32ndNotes(){
		return timeSignature.getNumberOf32ndNotesPerBar();
	}
	
	/**
	 * Returns the total number of CompositionBarEvent in this bar.
	 * @return
	 */
	public int getNumberOfCompositionBarEventsInBar(){
		return compositionBarEvents.size();
	}
	
	/**
	 * Returns the list of CompositionBarEvents in the Bar.
	 * @return
	 */
	public List<CompositionBarEvent> getCompositionBarEvents() {
		return compositionBarEvents;
	}

	/**
	 * Returns the sequence number of the track containing this bar.
	 * @return
	 */
	public int getTrackNumber() {
		return trackNumber;
	}

	/**
	 * Returns the PitchLineMidiMapper of this Bar which relates the pitch line
	 * location value to the staveline note.
	 * @return
	 */
	public PitchLineMidiMapper getPitchLineMidiMapper() {
		return pitchLineMidiMapper;
	}

	public VoicePitchRangeLimitObject getVoicePitchRangeLimitObject() {
		return voicePitchRangeLimitObject;
	}

	public void setVoicePitchRangeLimitObject(
			VoicePitchRangeLimitObject voicePitchRangeLimitObject) {
		this.voicePitchRangeLimitObject = voicePitchRangeLimitObject;
	}

	@Override
	public String toString() {
		return "Bar [clefType=" + clefType + ", timeSignature=" + timeSignature
				+ ", key=" + key + ", tempo=" + tempo
				+ ", pitchLineMidiMapper=" + pitchLineMidiMapper
				+ ", barNumber=" + barNumber + ", trackNumber=" + trackNumber
				+ ", voicePitchRangeLimitObject=" + voicePitchRangeLimitObject
				+ ", compositionBarEvents=" + compositionBarEvents + "]";
	}
}












