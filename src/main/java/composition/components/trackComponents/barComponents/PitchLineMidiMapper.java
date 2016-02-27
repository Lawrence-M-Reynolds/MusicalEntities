package composition.components.trackComponents.barComponents;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import constants.GeneralConstants;

import musicRelatedEntities.Clef;
import musicRelatedEntities.chord.Chord;
import musicRelatedEntities.key.Key;
import musicRelatedEntities.key.KeyNote;
import musicRelatedEntities.note.NoteLetter;
import musicRelatedEntities.note.StaveLineNote;


/**
 * When a CompositionBarEvent is drawn to the staves it has an int value associated with it
 * indicating what position it is. This starts from 0 from the bottom of the stave and counts 
 * up to the maximum position. This number obviously has to be translated into something
 * meaningful and that is what this pitchLineMidiMapper does.
 * To do this it uses requires both the key information and the clef information. The key relates
 * the pattern of semitones that each note letter is separated by and the clef indicates which
 * note letter each stave line is associated with and how many ocataves above zero. Using this
 * information it creates StaveLineNote objects and maps them to each of the stave line pitch
 * locations. These StaveLineNotes conatain the KeyNote and the midi information. 
 * 
 * This class also performs an imporatant task for the generation algorithm. Every StaveLineNote 
 * that is created is also mapped to the KeyNote that's associated with it. This makes it very
 * simple for the generator algorithm to know what staveline notes it can use by simply passing
 * in the keynotes of a chord.
 * @author BAZ
 *
 */
public class PitchLineMidiMapper implements Serializable{
	//Maps the pitch line number to the staveline note
	private Map<Integer, StaveLineNote> staveLineMap = new HashMap<Integer, StaveLineNote>();
	//Maps each key note to a all of the staveline notes it is contained within.
	private Map<KeyNote, Collection<StaveLineNote>> keyNoteToStaveLineNoteMap = 
			new HashMap<KeyNote, Collection<StaveLineNote>>();
	
	PitchLineMidiMapper(final Map<Integer, StaveLineNote> aStaveLineMap,
			final Map<KeyNote, Collection<StaveLineNote>> aKeyNoteToStaveLineNoteMap){
		staveLineMap = aStaveLineMap;
		keyNoteToStaveLineNoteMap = aKeyNoteToStaveLineNoteMap;
	}
	
	/**
	 * Retrieves the StaveLineNote associated with the pitch line location for the key and
	 * clef.
	 * @param pitchLineLocation
	 * @return
	 */
	public StaveLineNote getStaveLineNoteFromPitchLineLocation(int pitchLineLocation){
		return staveLineMap.get(pitchLineLocation);
	}
		
	/**
	 * Used by the generator algorihtm, this method takes in a chord and, using the
	 * KeyNotes within it, returns a collection of all of the StaveLineNotes that
	 * would belong to that chord. 
	 * @param chord
	 * @return
	 */
	public Collection<StaveLineNote> getStavlineNotesForChord(Chord chord){
		Collection<StaveLineNote> staveLineNotesForChord = null;
		for(KeyNote chordKeyNote : chord){
			if(staveLineNotesForChord == null){
				staveLineNotesForChord = keyNoteToStaveLineNoteMap.get(chordKeyNote);
			}else{
				staveLineNotesForChord.addAll(keyNoteToStaveLineNoteMap.get(chordKeyNote));
			}			
		}
		return staveLineNotesForChord;
	}
}