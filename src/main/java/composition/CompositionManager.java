package composition;

import musicRelatedEntities.note.writtenNotes.CompositeWrittenNote;
import musicRelatedEntities.note.writtenNotes.WrittenNote;

import composition.components.trackComponents.Bar;
import composition.components.trackComponents.barComponents.CompositionBarEvent;
import composition.components.tracks.InstrumentTrack;
import composition.components.tracks.InstrumentTrackManager;

/**
 * Manages operations performed on a Composition object.
 * @author L Reynolds
 *
 */
public class CompositionManager {
	
	private InstrumentTrackManager instrumentTrackManager;
	
	private static CompositionManager compositionManager;
	
	//Singleton
	private CompositionManager() {
		instrumentTrackManager = 
				InstrumentTrackManager.getInstance();
	}
	
	public static CompositionManager getInstance(){
		if (compositionManager == null) {
			compositionManager = new CompositionManager();
		}
		return compositionManager;
	}
	
	/**
	 * Writes the selected note value into the composition over
	 * the existing ones as given by the mappedCompositionObject. 
	 * The mappedCompositionObject also contains the pitch value.
	 * 
	 * Before:
	 * 					compositionDrawer.getBarEventMappingObject().writeEvent(point,
						noteSelector.generateNoteValue());
	 * @param pitchLineNumber 
	 * @param instrumentTrack 
	 * @param bar 
	 * @param compositionBarEvent 
	 * 
	 * @param mappedCompositionObject
	 * @param selectedNote
	 */
	public void writeSelectedNoteToComposition(
			CompositionBarEvent compositionBarEvent, Bar bar, 
			InstrumentTrack instrumentTrack, int pitchLineNumber, 
			WrittenNote selectedNote) {		

		if (selectedNote != null) {
			instrumentTrackManager.sortBarEvents(instrumentTrack,
					bar, compositionBarEvent,
					new CompositeWrittenNote(selectedNote),
					pitchLineNumber);
		} else {
			/*
			 * A null WrittenNote represents a delete -
			 *  convert the note into a rest
			 */
			compositionBarEvent.convertToRest();
		}
	}

	public InstrumentTrackManager getInstrumentTrackManager() {
		return instrumentTrackManager;
	}
}
