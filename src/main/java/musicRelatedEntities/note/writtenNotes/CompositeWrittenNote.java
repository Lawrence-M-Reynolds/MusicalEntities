package musicRelatedEntities.note.writtenNotes;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


import utilities.images.SymbolFactory;

import musicRelatedEntities.note.noteAttributes.lengthValues.LengthValue;



/**
 * Represents a written note that cannot be represented as any of the
 * base length values:
 * 	WholeNote, HalfNote, QuaterNote...
 * For instance, a bar with a timesignature of
 * @author UKGC
 *
 */
public class CompositeWrittenNote {
	//TODO shouldn't this be an ordered collection? Also, why aren't all of
	//the WrittenNotes contained in this?
	private Set<WrittenNote> writtenNoteValues = new TreeSet<WrittenNote>();
	//The other properties of the passed in noteValue will be relevant to all of the others
	private WrittenNote writtenNote;

	public CompositeWrittenNote(final WrittenNote aWrittenNote){
		//At create a null is passed in here from the bar.
		writtenNote = aWrittenNote;
		if(aWrittenNote != null){
			writtenNoteValues.add(aWrittenNote);
		}
	}
	
	public WrittenNote getWrittenNote() {
		return writtenNote;
	}

	public void addWrittenNoteValue(WrittenNote writtenNote){
		writtenNoteValues.add(writtenNote);
	}
	
	public int getNumberof32ndNotes() {
		int numberOf32ndNotes = 0;
		for(WrittenNote writtenNote : writtenNoteValues){
			numberOf32ndNotes += 
					writtenNote.getNumberof32ndNotes();
		}
		return numberOf32ndNotes;
	}
	
	public Iterator<WrittenNote> iterator() {
		return writtenNoteValues.iterator();
	}

	public Set<WrittenNote> getWrittenNoteValues() {
		return writtenNoteValues;
	}

	@Override
	public String toString() {
		return "CompositeWrittenNote [writtenNoteValues=" + writtenNoteValues +
				", writtenNote=" + writtenNote + "]";
	}
}
