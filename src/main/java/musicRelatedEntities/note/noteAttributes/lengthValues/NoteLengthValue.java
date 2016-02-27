package musicRelatedEntities.note.noteAttributes.lengthValues;

import java.awt.Graphics;
import java.awt.Point;

import utilities.images.Symbol;
import utilities.images.SymbolFactory;



public enum NoteLengthValue implements LengthValue{
	
	WholeNote(1, SymbolFactory.WHOLE_NOTE),
	HalfNote(2, SymbolFactory.HALF_NOTE), 
	QuaterNote(4, SymbolFactory.QUARTER_NOTE), 
	EigthNote(8, SymbolFactory.EIGTH_NOTE), 
	SixteenthNote(16, SymbolFactory.SIXTEENTH_NOTE), 
	ThirtySecondNote(32, SymbolFactory.THIRTY_SECOND_NOTE);

	private int recipricolValue;
	private Symbol noteSymbol;

	//Get rid of this once all images have been calibrated
	NoteLengthValue(final int aRecipricolValue, final Symbol aNoteSymbol){
		recipricolValue = aRecipricolValue;
		noteSymbol = aNoteSymbol;
	}
	
	public int getRecipricolValue() {
		return recipricolValue;
	}
	
	public void drawSymbol(Graphics g, Point point){
		if(point != null){
			noteSymbol.drawSymbol(g, point.x, point.y);
		}
	}
	
	public int getNumberof32ndNotes(){
		return 32/recipricolValue;
	}

	public boolean isRest() {
		return false;
	}
	
	public static NoteLengthValue getNoteLengthValueEquivalentNoteLengthValue(
			LengthValue aLengthValue){
		for(NoteLengthValue noteLengthValue : values()){
			if(aLengthValue.getNumberof32ndNotes() == noteLengthValue.getNumberof32ndNotes()){
				return noteLengthValue;
			}			
		}
		//Shouldn't get here
		return null;
	}

	/**
	 * Used for the composition creator jspinner. Assuming that
	 * the spinner will only ever pass in a value that is
	 * an existing recipricol value.
	 * @return
	 */
	public static NoteLengthValue getNoteLengthValueWithRecipricolValue(int value) {
		for(NoteLengthValue noteLengthValue: values()){
			if(noteLengthValue.getRecipricolValue() == value){
				return noteLengthValue;
			}
		}
		//Should never happen
		return null;
	}
	
	/**
	 * Used for the composition creator jspinner.
	 * @return
	 */
	public static Integer[] valuesAsRecipricol(){
		int numberOfValues = values().length;
		Integer[] recipricolValues= new Integer [numberOfValues];
		for(int i=0; i<numberOfValues; i++){
			recipricolValues[i] = values()[i].getRecipricolValue();
		}
		return recipricolValues;
	}
}




