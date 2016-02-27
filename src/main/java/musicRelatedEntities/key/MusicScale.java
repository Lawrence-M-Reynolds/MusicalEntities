package musicRelatedEntities.key;


public enum MusicScale {
	//Diatonic major scale has the pattern TTSTTTS
	Diatonic(new Interval[] {
			Interval.MAJOR_SECOND,
			Interval.MAJOR_SECOND,
			Interval.MINOR_SECOND,
			Interval.MAJOR_SECOND,
			Interval.MAJOR_SECOND,
			Interval.MAJOR_SECOND,
			Interval.MINOR_SECOND,
	});
	
	private Interval[] scaleIntervalSequence;
	
	private MusicScale(final Interval[] aScaleIntervalSequence){
		scaleIntervalSequence = aScaleIntervalSequence;
	}
	
	public Interval[] getIntervalArray(){
		return scaleIntervalSequence;
	}
	
	/**
	 * To String returns the quality scale. This is determined by counting the
	 * number of semitones between the first and third notes.
	 * If the interval between the first and fifth is not a perfect 5th (7
	 * semitones) but a diminished one (6 semitones) then it is 
	 */
	public String toString(){
		int semitonesBetweenTonicAndMediant = scaleIntervalSequence[0].getNumberOfSemitones() +
				scaleIntervalSequence[1].getNumberOfSemitones();
		if(semitonesBetweenTonicAndMediant == 3){
			return "Minor";
		}else{
			return "Major";
		}
	}
}
