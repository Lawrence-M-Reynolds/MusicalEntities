package musicRelatedEntities.note.noteAttributes;

import java.awt.Graphics;
import java.awt.Point;

import utilities.images.Symbol;
import utilities.images.SymbolFactory;


/**
 * 
 * All notes in the diatonic scale may be sharpened or flattend.
 * That is represented by this enum which contains information on
 * how the midi value should be modified in terms of the number of 
 * semitones. It also contains the image for it to be drawn.
 * 
 * TODO - The accidentals I have associated with the key should be a
 * separate entity to those that are applied to notes. Those that
 * are applied to notes can have a different effect depending
 * on the KeyNote on which they were written to.
 * @author BAZ
 *
 */
public enum Accidental {
/*
 * Although the natural doesn't have an interval change value,
 * it will be used to cancel out the effect of the other two.
 */

	SHARP(SymbolFactory.SHARP, 1, "#"),
	NATURAL(SymbolFactory.NATURAL, 0, ""),
	FLAT(SymbolFactory.FLAT, -1, "b");
	
	private Symbol accidentalSymbol;
	private int intervalChangeValue;
	private static final int ACCIDENTAL_IMAGE_X_OFFSET = 15;
	private static final int ACCIDENTAL_IMAGE_Y_OFFSET = 0;
	private String stringSymbol;
	
	Accidental(final Symbol anAccidentalSymbol, final int anIntervalChangeValue, final String aSymbol) {
		intervalChangeValue = anIntervalChangeValue;
		accidentalSymbol = anAccidentalSymbol;
		stringSymbol = aSymbol;
	}
	
	/**
	 * Draws the image of this accidental.
	 * @param g
	 * @param point
	 */
	public void drawSymbol(Graphics g, Point point){
		if(point != null){
			accidentalSymbol.drawSymbol(g, point.x - ACCIDENTAL_IMAGE_X_OFFSET, 
					point.y - ACCIDENTAL_IMAGE_Y_OFFSET);
		}
	}

	/**
	 * Returns the number of semitones that this acidental changes the note
	 * it is associated with by.
	 * @return
	 */
	public int getIntervalChangeValue() {
		return intervalChangeValue;
	}
	
	public String toString(){
		return stringSymbol;
	}

	/**
	 * This method is used by the Key to check whether a key note needs a sharp
	 * or a flat. If -1 is passed in then a flat is returned, +1 gives a sharp
	 * and 0 gives null. Any other value throws a runtime exception as this
	 * shouldn't be requried when creating the key notes.
	 * @param requiredSemitoneModifierOfNextNote
	 * @return
	 * EXCEPTION - DO THIS PROPERLY
	 */
	public static Accidental getAccidentalForSemitoneModifier(
			final int requiredSemitoneModifierOfNextNote) {
		if(requiredSemitoneModifierOfNextNote == -1){
			return Accidental.FLAT;
		}else if(requiredSemitoneModifierOfNextNote == 1){
			return Accidental.SHARP;
		}else if(requiredSemitoneModifierOfNextNote == 0){
			return null;
		}
		throw new RuntimeException(){
			public String toString(){
				return "An accidental of semitone modifier of " + requiredSemitoneModifierOfNextNote + 
						" was requested from Accidental.getAccidentalForSemitoneModifier(int requiredSemitoneModifierOfNextNote). " +
						"This shouldn't happen when creating the key notes.";
			}
		};
	}
}
