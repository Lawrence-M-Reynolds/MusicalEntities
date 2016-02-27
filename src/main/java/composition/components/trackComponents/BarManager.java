package composition.components.trackComponents;


import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import composition.components.trackComponents.Bar;
import composition.components.trackComponents.barComponents.BarComponentManager;
import composition.components.trackComponents.barComponents.CompositionBarEvent;
import composition.components.trackComponents.barComponents.PitchLineMidiMapper;
import composition.components.tracks.InstrumentTrack;
import composition.components.tracks.InstrumentTrackManager;
import composition.reporting.AnnotationObject;

import musicRelatedEntities.Clef;
import musicRelatedEntities.key.Key;
import musicRelatedEntities.note.StaveLineNote;
import musicRelatedEntities.note.noteAttributes.lengthValues.LengthValue;
import musicRelatedEntities.note.noteAttributes.lengthValues.NoteLengthValue;
import musicRelatedEntities.note.noteAttributes.lengthValues.RestLengthValue;
import musicRelatedEntities.note.writtenNotes.CompositeWrittenNote;
import musicRelatedEntities.note.writtenNotes.WrittenNote;
import musicRelatedEntities.time.Tempo;
import musicRelatedEntities.time.TimeSignature;

/**
 * Manages changes to the Bar objects.
 * @author L Reynolds
 *
 */
public class BarManager {
	
	private static BarManager barManager;
	
	private BarComponentManager barComponentManager;
	
	//Singleton
	private BarManager() {
		barComponentManager = BarComponentManager.getInstance();
	}
	
	public static BarManager getInstance(){
		if (barManager == null) {
			barManager = new BarManager();
		}
		return barManager;
	}
	
	/**
	 * Returns the time length (in 32nd notes) of the bar event ahead of this one. If there isn't any
	 * then the default value is returned.
	 * @param barEventTimeLocation
	 * @param delta
	 * @return
	 */
	public int getTimeLengthUntilToNextEvent(
			Bar bar, int barEventTimeLocation, int defaultValue) {
		int nextBarEventTimeLocation = 0;
		ListIterator<CompositionBarEvent> reverseIterator =
				bar.getCompositionBarEvents().listIterator(
				bar.getCompositionBarEvents().size());
		while(reverseIterator.hasPrevious()){
			CompositionBarEvent barEvent = reverseIterator.previous();
			if(barEvent.getTimeLocation() < barEventTimeLocation){
				//This note event is before the one passed in to the method so break out of loop.
				break;
			}
			nextBarEventTimeLocation = barEvent.getTimeLocation();
		}
		
		if(nextBarEventTimeLocation == 0){
			//There isn't any CompositionBarEvents ahead of this one so return the default
			//value.
			return defaultValue;
		}
		//Returns the difference in time.
		return nextBarEventTimeLocation - barEventTimeLocation;
	}

	/**
	 * Used to create a new Bar. As well as creating the bar it also sets
	 * the inital rest of the correct size within it.
	 * @param aClefType
	 * @param aTimeSignature
	 * @param aKey
	 * @param aTempo
	 * @param aBarNumber
	 * @param aTrackNumber
	 * @param barVoicePitchRangeLimitObject 
	 * @return Bar
	 */
	public Bar createBar(final Clef aClefType, final TimeSignature aTimeSignature, 
			final Key aKey, final Tempo aTempo, final int aBarNumber, final int aTrackNumber, 
			VoicePitchRangeLimitObject barVoicePitchRangeLimitObject){

		PitchLineMidiMapper pitchLineMidiMapper = 
				barComponentManager.createPitchLineMidiMapper(aClefType, aKey);
		
		Bar bar = new Bar(aClefType, aTimeSignature, aKey, aTempo, aBarNumber, 
				aTrackNumber, barVoicePitchRangeLimitObject,
				pitchLineMidiMapper);
	////Default rest(s) should be added to every new bar based on the time signature
		writeCompositeNoteValueToBar(bar, 0, 
				//new CompositeNoteValue(null) passed in because it's used to get the accidental which
				//isn't required here.
				barComponentManager.getCompositeWrittenNoteWithNumberOf32ndNotes(
						new CompositeWrittenNote(null),
						aTimeSignature.getNumberOf32ndNotesPerBar(), RestLengthValue.values()), 
				RestLengthValue.REST_PITCH_LINE_VALUE, false, null);
		return bar;
	}
	
	/**
	 * Writes the CompositeNoteValue at the specified barEventTimeLocation (in terms of 32nd note length
	 * values) to the bar. The pitchLineValue is used to obtain the staveline note associated with the
	 * point on the stave where the user wrote and this is used to create the CompositionBarEvent
	 * which contains all of the information required to play the composition. This is then
	 * added to the collection of CompositionBarEvents.
	 * 
	 * It's assumed that the CompositeNoteValue will not exceed the length of this bar so the logic
	 * to handle that should be dealt with beforehand. This method also deals with overwritten 
	 * CompositionBarEvents.
	 * @param Bar bar The bar to make the changes to.
	 * @param barEventTimeLocation
	 * @param aCompositeNoteValue
	 * @param pitchLineValue
	 * @param isTied
	 * @param aColOfCounterpointAnnotationObjects
	 */
	//EXCEPTION  to handle second paragraph of the above case.
	public void writeCompositeNoteValueToBar(final Bar bar,
			final int barEventTimeLocation, 
			final CompositeWrittenNote aCompositeNoteValue, 
			final int pitchLineValue, final boolean isTied,
			Collection<AnnotationObject> aColOfCounterpointAnnotationObjects){
		/*
		 * Deleting overwritten notes.
		 * Gets the noteEventsCollection into array and then clears it. They
		 * are only added back from the array if they don't occur between 
		 * the end and start time.
		 */
		Object[] compositionBarEventsArray = 
				bar.getCompositionBarEvents().toArray();
		bar.getCompositionBarEvents().clear();
		for(int noteEventNum = 0; 
				noteEventNum < compositionBarEventsArray.length; noteEventNum++){
			CompositionBarEvent barEvent = 
					(CompositionBarEvent)compositionBarEventsArray[noteEventNum];
			int existingNoteStartTime = barEvent.getTimeLocation();
			int barEventTimeLocationEnd = barEventTimeLocation + 
					aCompositeNoteValue.getNumberof32ndNotes();
			if(!(existingNoteStartTime >= barEventTimeLocation
					&& existingNoteStartTime < barEventTimeLocationEnd)){
				bar.getCompositionBarEvents().add(barEvent);
			}
		}	
		

//		Writing the new notes
		int newEventTimeLocation = barEventTimeLocation;
		Iterator<WrittenNote> compositeNoteValueIterator = aCompositeNoteValue.iterator();	
		while(compositeNoteValueIterator.hasNext()){
			WrittenNote noteValue = compositeNoteValueIterator.next();
			
			boolean tieNote = ((noteValue.getNoteLengthValue() instanceof NoteLengthValue)
					&& (isTied || compositeNoteValueIterator.hasNext()));
			StaveLineNote barEventStaveLineNote =
					bar.getPitchLineMidiMapper().getStaveLineNoteFromPitchLineLocation(
							pitchLineValue);
			bar.getCompositionBarEvents().add(
					new CompositionBarEvent(barEventStaveLineNote, newEventTimeLocation,
							noteValue, pitchLineValue, tieNote, bar.getTrackNumber(),
							aColOfCounterpointAnnotationObjects));
			newEventTimeLocation += noteValue.getNumberof32ndNotes();
		}
	}
	

}
