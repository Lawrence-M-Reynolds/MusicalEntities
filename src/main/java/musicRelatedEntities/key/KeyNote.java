package musicRelatedEntities.key;

import java.io.Serializable;
import java.util.Map;

import musicRelatedEntities.chord.Chord;
import musicRelatedEntities.chord.chordTypes.ChordType;
import musicRelatedEntities.note.Note;
import musicRelatedEntities.note.NoteLetter;
import musicRelatedEntities.note.noteAttributes.Accidental;




/**
 * A key note represents a note that is in the key - a diatonic
 * note. This has a key function enum associated with it. It does not have
 * any specific pitch/midi value associated with it as it could represent
 * any of the notes of the key of any octave.
 * 
 * The KeyNote also has a reference to each of the other 7 KeyNotes. They are stored
 * in the keyNoteHarmonics Map which is created by the HarmonyEvaluator when the key
 * is created. They are mapped to the scale degree in relation to this
 * keyNote - ie this note is the tonic and it works up from there. This is the
 * case even if the key note isn't the tonic of the key itself. The reason for
 * doing this is it allows the chords to be built on top of the keynote
 * much easier.
 * 
 * TODO it would probably be better having it stored by root, second, third etc..
 * @author BAZ
 *
 */
public class KeyNote extends Note implements Comparable<KeyNote>, Serializable{
	/** Scale degree within the context of the key. */
	private ScaleDegree scaleDegree;
	/**Other keynotes mapped to this one by their Scale degree which
	 * is in the context of this keynote being the tonic.*/
	private Map<ScaleDegree, KeyNote> keyNoteHarmonics;

	public KeyNote(NoteLetter noteLetter, Accidental accidental, ScaleDegree scaleDegree){
		super(noteLetter, accidental);
		this.scaleDegree = scaleDegree;
	}

	/**
	 * Orders they KeyNote according to its scale degree number.
	 */
	public int compareTo(KeyNote keyNote) {
		return this.scaleDegree.getScaleDegreeNumber() - keyNote.getScaleDegree().getScaleDegreeNumber();
	}

	/**
	 * Returns the scale degree number of the KeyNote.
	 * @return
	 */
	public ScaleDegree getScaleDegree() {
		return scaleDegree;
	}

	/**
	 * Returns the harmonic keyNote based on the ScaleDegree value which is in
	 * relation to this note. This different to the scale degree of the key.
	 * For example, this keynote may be anyother scale degree of the key
	 * but with regards to it's harmonics it is treated as the tonic and that
	 * is how it is mapped.
	 * 
	 * LOW perhaps this insn't quite the best way to do this.
	 * @param aScaleDegree
	 * @return
	 */
	public KeyNote getKeyNoteHarmonic(ScaleDegree aScaleDegree) {
		return keyNoteHarmonics.get(aScaleDegree);
	}

	/**
	 * Sets the key note harmonics of the KeyNote. This is called by the 
	 * Harmony Evaluator when the key is created.
	 * @param keyNoteHarmonics
	 */
	public void setKeyNoteHarmonics(Map<ScaleDegree, KeyNote> keyNoteHarmonics) {
		this.keyNoteHarmonics = keyNoteHarmonics;
	}

	/**
	 * Builds a basic triad chord with this keynote being the root. The chordType
	 * Currently doens't do anything but in future it could be use to create
	 * different types of chord.
	 * @param chordType
	 * @param chordRootKeyNote
	 * @return
	 */
	public Chord buildKeyChord(ChordType chordType) {
		//TODO - only implemented to bring back a basic triad.
		KeyNote third = this.getKeyNoteHarmonic(ScaleDegree.MEDIANT);
		KeyNote fifth = this.getKeyNoteHarmonic(ScaleDegree.DOMINANT);
		
		return new Chord(this, third, fifth, chordType);
			
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof KeyNote)){
			return false;
		}
		KeyNote keyNote = (KeyNote)obj;
		if(this.accidental != keyNote.accidental
				|| this.noteLetter != keyNote.noteLetter
				|| this.scaleDegree != keyNote.scaleDegree){
			return false;
		}		
		return super.equals(obj);
	}


	
}
