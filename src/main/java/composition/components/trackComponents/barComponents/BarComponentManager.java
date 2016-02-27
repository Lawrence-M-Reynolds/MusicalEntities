package composition.components.trackComponents.barComponents;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import constants.GeneralConstants;
import musicRelatedEntities.Clef;
import musicRelatedEntities.key.Key;
import musicRelatedEntities.key.KeyNote;
import musicRelatedEntities.note.NoteLetter;
import musicRelatedEntities.note.StaveLineNote;
import musicRelatedEntities.note.noteAttributes.lengthValues.LengthValue;
import musicRelatedEntities.note.writtenNotes.CompositeWrittenNote;
import musicRelatedEntities.note.writtenNotes.WrittenNote;

public class BarComponentManager {
	private static BarComponentManager barComponentManager;
	
	//Singleton
	private BarComponentManager() {}
	
	public static BarComponentManager getInstance(){
		if (barComponentManager == null) {
			barComponentManager = new BarComponentManager();
		}
		return barComponentManager;
	}
	
	/**
	 * This static method returns the Note or composite note that has the right number of
	 * 32nd notes.
	 * @param CompositeWrittenNote aCompositeWrittenNote Passed in to get the accidental value
	 * @param int numberOf32ndNotes The length of the CompositeWrittenNote.
	 * @param LengthValue[] lengthValues All of the base length values.
	 * @return CompositeWrittenNote The built CompositeWrittenNote.
	 */
	//EXCEPTION - should thrown an exception if it can't find the right value.
	public CompositeWrittenNote getCompositeWrittenNoteWithNumberOf32ndNotes(
			final CompositeWrittenNote aCompositeWrittenNote, final int numberOf32ndNotes, 
			final LengthValue[] lengthValues){
		/*
		 * THE ONLY REASON aCompositeWrittenNote IS PASSED IN HERE IS TO GET THE ACCIDENTAL
		 * PROPERTIES (AND ANY OTHERS THAT MAY BE ADDED IN FUTURE). This value
		 * is transfered in the NoteValue constructor.
		 */
		if(numberOf32ndNotes == 0){
			//if this is the case then there are no notes to write
			return null;
		}
		
		CompositeWrittenNote compositeWrittenNote = null;
		for(LengthValue lengthValue : lengthValues){

			int returnNoteNumberOf32ndNotes = lengthValue.getNumberof32ndNotes();
			if(compositeWrittenNote != null){
				returnNoteNumberOf32ndNotes += 
						compositeWrittenNote.getNumberof32ndNotes();
			}

			int delta = numberOf32ndNotes - returnNoteNumberOf32ndNotes;
		
			if(delta == 0){ 
				if(compositeWrittenNote == null){
					//It has only one written note.
					return new CompositeWrittenNote(new WrittenNote(aCompositeWrittenNote.getWrittenNote(), lengthValue));
				}else{
					//There was one efore so just add this to the collection.
					compositeWrittenNote.addWrittenNoteValue(new WrittenNote(aCompositeWrittenNote.getWrittenNote(), lengthValue));
					return compositeWrittenNote;
				}
					
			}else if(delta < 0){
				//note is too large so move onto the next one
				continue;
			}else if(delta > 0){
				/*
				 * This note is not large enough but by now we know that none of the notes will match 
				 * this number of 32nd notes exactly. So a composite Note will have to be returned. 
				 * This is found by adding smaller values on.
				 */
				if(compositeWrittenNote == null){			
					//This is the first note.
					compositeWrittenNote = new CompositeWrittenNote(new WrittenNote(aCompositeWrittenNote.getWrittenNote(), lengthValue));
				}else{
					//There was already a note there so add this to the collection.
					compositeWrittenNote.addWrittenNoteValue(new WrittenNote(aCompositeWrittenNote.getWrittenNote(), lengthValue));
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Creates a PitchLineMidiMapper and performs all of the logic to work out
	 * what all the StaveLineNotes should be for each of the pitch line locations. It then
	 * maps all of these to that number and also to the keynote contained within it. 
	 * TODO - I think this logic is fixed but need to check the outputs of the notes
	 * MEMORY - Memory could be saved by checking if there's an instance in a map already
	 * and returning that. Would have to implement equals of the mapper.
	 */	
	public PitchLineMidiMapper createPitchLineMidiMapper(final Clef clef, final Key key){
		
		Map<Integer, StaveLineNote> staveLineMap = new HashMap<Integer, StaveLineNote>();
		Map<KeyNote, Collection<StaveLineNote>> keyNoteToStaveLineNoteMap = 
				new HashMap<KeyNote, Collection<StaveLineNote>>();
		
		/*
		 *  get the stave line note letter  of the clef which is worked out from the clef and
		 * key.
		 */		
		StaveLineNote clefStaveLineNote = clef.getStaveLineNote();
		int clefNotePitchLineLocation = clef.getpitchLineLocation();
		NoteLetter noteLetter = clefStaveLineNote.getNoteLetter();
		int clefNoteLetterOrdinal = noteLetter.ordinal();
		int clefNoteLetterNumber = clefStaveLineNote.getNoteNumber();
		
		/*
		 * The ordinal is the number of the note letter in the sequence
		 * A, B, C ... Subtract from this the location of the clef note
		 * in the pitch lines. This should give the sequence value for
		 * the letter of the lowest note on the stave. But if the
		 * bottom note is of a lower octave then this will be negative.
		 */
		int diff = clefNoteLetterOrdinal - clefNotePitchLineLocation;
		NoteLetter lowestNoteLetter = null;
		// Lowest note number is the octave number of the lowest note.
		//Initially assumed to be the same as the clef octave number.
		int lowestNoteNumber = clefNoteLetterNumber;
		boolean noteFound = false;
		while(!noteFound){
			if(diff >= 0){
				lowestNoteLetter = NoteLetter.values()[diff];
				noteFound = true;
			}else{
				//The lowest note must be of a lower octave number. Subtract from it,
				//add an octave to the diff value and try again.
				lowestNoteNumber -= 1;
				diff += 7;
			}
		}
		
		StaveLineNote staveLineNote =  
				new StaveLineNote(lowestNoteNumber, lowestNoteLetter);

		/*
		 * Need to count back from the position of the clef's defining note to the lowest
		 * pitch line number. This 
		 */
		//It's easier to use a list
		List<NoteLetter> list = Arrays.asList(NoteLetter.values());
		for(int stavePitchPosition = 0; 
				stavePitchPosition < GeneralConstants.NUMBER_OF_STAVE_LINE_LOCATIONS; 
				stavePitchPosition++){
			//Get the key note based on the note letter and put it in the
			//staveline note along with the pitch line location.
			KeyNote keyNote = key.getKeyNoteFromNoteLetter(noteLetter);
			staveLineNote.setKeyNote(keyNote);		
			staveLineNote.setStaveLineLocation(stavePitchPosition);
			
			//Map the stave line position to the stave line note.
			staveLineMap.put(stavePitchPosition, staveLineNote);
			
			//Putting the stave line note in a collection which is mapped to
			//the key note. TODO - Should be in another object.
			Collection<StaveLineNote> staveLineNotesOfKeyNote;
			if(!keyNoteToStaveLineNoteMap.containsKey(keyNote)){
				staveLineNotesOfKeyNote = new HashSet<StaveLineNote>();
				keyNoteToStaveLineNoteMap.put(keyNote, staveLineNotesOfKeyNote);
			}else{
				staveLineNotesOfKeyNote = keyNoteToStaveLineNoteMap.get(keyNote);
			}
			staveLineNotesOfKeyNote.add(staveLineNote);				
			
			//Get the next note letter and increment
			//the octave if necessary. Use this to get the next
			//StaveLineNote for the next iteration.
			int noteLetterSequenceLocation = list.indexOf(noteLetter);
			if(noteLetterSequenceLocation == 6){
				//The next note will be the next octave of note letters and so the 
				//clef value must be increased.
				clefNoteLetterNumber += 1;
			}
			noteLetter = noteLetter.getNextNoteLetter();
			
			staveLineNote = new StaveLineNote(clefNoteLetterNumber, noteLetter);
		}

		return new PitchLineMidiMapper(staveLineMap, 
				keyNoteToStaveLineNoteMap);
	}
}
