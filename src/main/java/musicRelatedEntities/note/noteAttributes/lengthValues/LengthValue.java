package musicRelatedEntities.note.noteAttributes.lengthValues;

import java.awt.Graphics;
import java.awt.Point;

/**
 * Implemented by two enums: NoteLengthValue and RestLengthValue. May
 * not have been necessary to separate the enums into two and
 * use an interface.
 * The entities have the ability to draw the appropriate symbol.
 * @author L Reynolds
 *
 */
public interface LengthValue {
	public void drawSymbol(Graphics g, Point point);

	public int getNumberof32ndNotes();
	
	public boolean isRest();
	
}
