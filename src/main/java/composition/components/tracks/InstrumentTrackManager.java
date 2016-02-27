package composition.components.tracks;

import java.util.Map;

import musicRelatedEntities.Clef;
import musicRelatedEntities.key.Key;
import musicRelatedEntities.note.noteAttributes.lengthValues.NoteLengthValue;
import musicRelatedEntities.note.noteAttributes.lengthValues.RestLengthValue;
import musicRelatedEntities.note.writtenNotes.CompositeWrittenNote;
import musicRelatedEntities.time.Tempo;
import musicRelatedEntities.time.TimeSignature;
import composition.CompositionManager;
import composition.components.InstrumentTrackDetails;
import composition.components.trackComponents.Bar;
import composition.components.trackComponents.BarManager;
import composition.components.trackComponents.InstrumentType;
import composition.components.trackComponents.VoicePitchRangeLimitObject;
import composition.components.trackComponents.barComponents.BarComponentManager;
import composition.components.trackComponents.barComponents.CompositionBarEvent;

/**
 * Has all of the logic for creating and managing
 * the contents of an InstrumentTrack.
 * @author L Reynolds
 *
 */
public class InstrumentTrackManager{

	private BarManager barManager;
	private BarComponentManager barComponentManager;
	
	private static InstrumentTrackManager instrumentTrackManager;
	
	//Singleton
	private InstrumentTrackManager() {
		barManager = 
				BarManager.getInstance();
		barComponentManager = BarComponentManager.getInstance();
	}
	
	public static InstrumentTrackManager getInstance(){
		if (instrumentTrackManager == null) {
			instrumentTrackManager = new InstrumentTrackManager();
		}
		return instrumentTrackManager;
	}
	
	/**
	 * Creates a track with the supplied details.
	 * Note: Doesn't set the VoicePitchRangeLimitObjects in each
	 * bar in the track.  setVoicePitchRangeLimitObjectsForTrack(..) must
	 * be called after track creation for this.
	 * @param trackNumber
	 * @param aTrackName
	 * @param anInstrumentType
	 * @param numberOfBars
	 * @param aClef
	 * @param timeSignature
	 * @param key
	 * @param tempo
	 * @return InstrumentTrack The instrument track.
	 */
	public InstrumentTrack generateNewInstrumentTrack(int trackNumber, String aTrackName,
			InstrumentType anInstrumentType, int numberOfBars, Clef aClef,
			TimeSignature timeSignature, Key key, Tempo tempo,
			Map<Integer, VoicePitchRangeLimitObject> barVoicePitchRangeLimitObjectMap){		
		
		InstrumentTrack instrumentTrack = new InstrumentTrack(trackNumber, 
				aTrackName, anInstrumentType);

		for (int barNumber = 0; barNumber < numberOfBars; barNumber++) {
			VoicePitchRangeLimitObject barVoicePitchRangeLimitObject = 
					barVoicePitchRangeLimitObjectMap.get(barNumber);
			//EXCEPTION - If the barVoicePitchRangeLimitObjectMap doesn't have values
			//for each bar number
			instrumentTrack.addBar(barManager.createBar(
					aClef, timeSignature, key, tempo, barNumber, trackNumber,
					barVoicePitchRangeLimitObject));
		}	
		return instrumentTrack;		
	}
	
	/**
	 * Constructor used by when translating from a counterpoint composition to
	 * a front end composition.
	 * @param instrumentTrackDetails
	 */
	public InstrumentTrack createInstrumentTrackFromInstrumentTrackDetails
	(InstrumentTrackDetails instrumentTrackDetails) {
		return new InstrumentTrack(instrumentTrackDetails);
	}
	
	/**
	 * This manages how the CompositionBarEvent will change when those in a bar are modified.
	 * It also has the logic to determine what rests should be created when a bar is first created.
	 * If the CompositionBarEvent goes beyond the bar that it was written into, the one that
	 * was passed in, then this method calls itself with the next bar obtained from the track.
	 * @param track
	 * @param bar
	 * @param aBarEvent The existing event where the writtenNote has been placed.
	 * @param aCompositeNoteValue The new compositeNoteValue to place over the BarEvent.
	 * @param pitchLineValue The pitch line value of where the new  compositeNoteValue has
	 * been placed.
	 */
	public void sortBarEvents(final InstrumentTrack track, final Bar bar, 
			final CompositionBarEvent aBarEvent, 
			final CompositeWrittenNote aCompositeNoteValue, final int pitchLineValue){
		int numOf32ndBeats = bar.getNumberOf32ndNotes();
		int barEventTimeLocation = aBarEvent.getTimeLocation();
		int noteLength = aCompositeNoteValue.getNumberof32ndNotes();
		int timeLeftInBar = numOf32ndBeats - barEventTimeLocation;
		int delta = timeLeftInBar - noteLength;
		
		if(delta == 0){
			//The note exactly matches the bar length so there's no need to do anything except write 
			//the note as no rests need to be created.
			barManager.writeCompositeNoteValueToBar(
					bar, barEventTimeLocation, aCompositeNoteValue,
					pitchLineValue, false, null);
			
		}else if(delta < 0){
			/*
			 * The note is larger than the bar so:
			 * work out how much should be written here and then write it
			 * it should be drawn tied over to the next bar and then the 
			 * algorithm should be run again with what's left to check if rests will need to be 
			 * created in the next bar.
			 */
			barManager.writeCompositeNoteValueToBar(bar, barEventTimeLocation, 
					barComponentManager.getCompositeWrittenNoteWithNumberOf32ndNotes(
            				aCompositeNoteValue, timeLeftInBar, NoteLengthValue.values()), pitchLineValue, true, null);

			Bar nextBar = track.getNextBar(bar);	
			//FIXME - This is why overwriting a note early in a bar overwrites all notes in front of it.
			//Need a new "NoteEvent" of total value of delta. Problem is that this may have to be made
			//up of more than one note. So I've created a composite Note 
			CompositeWrittenNote compositeNote = barComponentManager.getCompositeWrittenNoteWithNumberOf32ndNotes(
					aCompositeNoteValue, -delta, NoteLengthValue.values());
			sortBarEvents(track, nextBar, nextBar.getFirstCompositionBarEvent(), compositeNote, pitchLineValue);
			
		}else if(delta > 0){
            //The note is smaller than the bar length so the note must be drawn and any notes/rests 
			//that it overwrites should be deleted and then any resulting 32nd units should be broken 
			//down into as few rests as possible.
			barManager.writeCompositeNoteValueToBar(bar, barEventTimeLocation, aCompositeNoteValue, pitchLineValue, false, null);
			//Make a composite rest of what's left in the bar up to the next event that wasn't over written.
			int restTimeLocation = barEventTimeLocation + aCompositeNoteValue.getNumberof32ndNotes();
			int timeLengthOfRests = barManager.getTimeLengthUntilToNextEvent(bar, restTimeLocation, delta);
			CompositeWrittenNote compositeRest = barComponentManager.getCompositeWrittenNoteWithNumberOf32ndNotes(
					new CompositeWrittenNote(null), timeLengthOfRests, RestLengthValue.values());
			if(compositeRest != null){
				barManager.writeCompositeNoteValueToBar(bar, restTimeLocation, compositeRest, 
						RestLengthValue.REST_PITCH_LINE_VALUE, false, null);
			}
		}
	}

	public BarManager getBarManager() {
		return barManager;
	}
}
