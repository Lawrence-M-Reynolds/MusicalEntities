package musicRelatedEntities.note;

import java.io.Serializable;

import musicRelatedEntities.note.noteAttributes.Accidental;



/**
 * Extended by StaveLine Note and KeyNote
 * @author L Reynolds
 *
 */
public class Note implements Serializable{
	protected NoteLetter noteLetter;
	protected Accidental accidental;

	public Note(NoteLetter noteLetter, Accidental accidental) {
		super();
		this.noteLetter = noteLetter;
		this.accidental = accidental;
	}

	public Accidental getAccidental() {
		return accidental;
	}

	public NoteLetter getNoteLetter() {
		return noteLetter;
	}
	
	@Override
	public String toString(){
		String returnString = noteLetter.toString();
		if(accidental != null){
			returnString += accidental.toString();
		}
		return returnString;
	}
}
