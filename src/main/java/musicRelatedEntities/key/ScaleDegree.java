package musicRelatedEntities.key;

/**
 * Each of the 7 notes in a scale and the chords that 
 * are built upon them have a certain function in the key. 
 * This is called a degree and is represented in this enum 
 * which is associated with each of the key notes and also
 * with the key chords. 
 *  
 * This has been implemented as an inner class to the scale
 * as it is so closely related to it.
 * @author BAZ
 *
 */
public enum ScaleDegree {
	TONIC,
	SUPERTONIC,
	MEDIANT,
	SUBDOMINANT,
	DOMINANT,
	SUBMEDIANT,
	SUBTONIC;
	
	public static ScaleDegree getScaleDegree(int degreeNumber){
		return values()[degreeNumber];
	}
	
	public int getScaleDegreeNumber(){
		return (ordinal() + 1);
	}
}
