package musicRelatedEntities.key;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import musicRelatedEntities.note.Note;

/**
 * Holds the logic to set the keyNoteHarmonics in each KeyNote of the Key.
 */
public class KeyManager {
	
	/**
	 * This method is called whenever a key is created. It takes a list of the 
	 * key notes and, within each one, creates a map of all of the keynotes with
	 * the key of the map being the relationship of that key note to the one in
	 * which the map will be placed.
	 * @param sortedKeyNoteList
	 * @return
	 */
	public static void  evaluateKeyNoteHarmonics(List<KeyNote> sortedKeyNoteList){
		sortedKeyNoteList.toArray();

		int listLength = sortedKeyNoteList.size();
		for(int i = 0; i < listLength; i++){
			/*
			 * This list must retain its order as it represents the notes
			 * function in the harmony. 
			 */
			Map<ScaleDegree, KeyNote> keyChordNotes = new HashMap<ScaleDegree, KeyNote>();
			//iterating through all of the harmonics of the root note to add to the
			//chord.
			for(int harmonicSequenceNumber = 0; harmonicSequenceNumber < 7; harmonicSequenceNumber++){
				//This gets every third note
				KeyNote chordNote = sortedKeyNoteList.get((i + (harmonicSequenceNumber*2)) % listLength);
				keyChordNotes.put(ScaleDegree.getScaleDegree(
						(harmonicSequenceNumber*2) % listLength), chordNote);
			}

			/*
			 * Setting the chord into the root note so that the note can be used
			 * to select the chord.
			 */			
			KeyNote rootNote = keyChordNotes.get(ScaleDegree.TONIC);
			rootNote.setKeyNoteHarmonics(keyChordNotes);
		}
	}
}