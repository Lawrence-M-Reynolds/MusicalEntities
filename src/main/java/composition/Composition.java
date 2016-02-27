package composition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import musicRelatedEntities.chord.Chord;
import musicRelatedEntities.key.Key;
import musicRelatedEntities.time.Tempo;
import musicRelatedEntities.time.TimeSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import composition.components.InstrumentTrackDetails;
import composition.components.trackComponents.Bar;
import composition.components.tracks.InstrumentTrack;
import composition.reporting.RuleReport;



/**
 * This object represents the enitire composition as created by the user. Includes the tracks,
 * notes etc.
 * @author BAZ
 *
 */
public class Composition implements IComposition, Serializable, Iterable<InstrumentTrack> {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(Composition.class);
	
	private String title;
	private Key key;
	private TimeSignature timeSignature;
	private Tempo tempo;
	private int numberOfBars;
	
	private Map<Integer, Collection<Chord>> harmonyLockdownMap;
	//TODO should be a map.
	private List<InstrumentTrack> instrumentTracks = new ArrayList<InstrumentTrack>();
	
	private List<RuleReport> ruleReports;
	
	/**
	 * Constructor used when creating a new composition from scratch.
	 * @param aTitle
	 * @param aKey
	 * @param aTimeSignature
	 * @param aTempo
	 * @param aNumberOfBars
	 * @param aHarmonyLockdownMap
	 */
	public Composition(final String aTitle, final Key aKey, final TimeSignature aTimeSignature, 
			final Tempo aTempo, final int aNumberOfBars){
		logger.debug("New Composition!");
		title = aTitle;
		key = aKey;
		timeSignature = aTimeSignature;
		tempo = aTempo;
		numberOfBars = aNumberOfBars;
		
		harmonyLockdownMap = new HashMap<Integer, Collection<Chord>>();
	}
	
	/**
	 * Used when translating a counterpoint composition to a front end composition.
	 * 
	 * Note: This constructor only copies the top level objects associated with the composition
	 * such as title, harmony lockdown map etc. The tracks, bars and note events must be handled 
	 * separately by calling setInstrumentTracks(List<InstrumentTrack> instrumentTracks).
	 * 
	 * CHANGE: This has been changed to the abstraction IComposition which is held in the musical entities
	 * project removing the dependency on the counterpoint project.
	 * @param counterpointComposition
	 */
	public Composition(IComposition counterpointComposition) {
		title = counterpointComposition.getTitle();
		key = counterpointComposition.getKey();
		timeSignature = counterpointComposition.getTimeSignature();
		tempo = counterpointComposition.getTempo();
		harmonyLockdownMap = counterpointComposition.getHarmonyLockdownMap();
		ruleReports = counterpointComposition.getRuleReports();
	}

	/**
	 * Returns the size of any track already in the composition. They should all have the
	 * same number of bars so need to check all of them. If there are no other tracks
	 * in the composition then it will return a default value as defined in the constructor,
	 * or from the counterpoint composition.
	 * @return
	 */
	public int getNumberOfBars(){
		if(instrumentTracks.isEmpty()){
			return numberOfBars;
		}else{			
			return instrumentTracks.get(0).getNumberOfBars();
		}
	}
	
	@Override
	public String toString(){
		String numberOfTracks = Integer.toString(instrumentTracks.size());
		return "This composition has " + numberOfTracks + " tracks.";
	}
	
	/**
	 * Adds a new InstrumentTrack.
	 * @param InstrumentTrack The track to add
	 */
	public void addNewTrack(final InstrumentTrack instrumentTrack){
		instrumentTracks.add(instrumentTrack);
	}

	/**
	 * Returns the time signature of the composition as defined at the composition creation.
	 * @return
	 */
	public TimeSignature getTimeSignature() {
		return timeSignature;
	}
	
	/**
	 * Returns the number of tracks in this composition.
	 * @return
	 */
	public int getNumberOfTracks(){
		return instrumentTracks.size();
	}

	/**
	 * Returns an itertor to iterate through the tracks.
	 */
	public Iterator<InstrumentTrack> iterator() {
		return instrumentTracks.iterator();
	}

	/**
	 * This inner iterator is used so that all of the voices across a single
	 * bar can be iterated through at once. The other iterator just iterates
	 * through each of the melody lists which isn't so easy to work with.
	 * 
	 * This makes it simpler to convert this into a counterpoint composition
	 * which holds the track information in groups of bars from each track
	 * rather than groups from a single track. ie vertically rather than
	 * horizontally.
	 * @author BAZ
	 *
	 */
	public Iterable<List<Bar>> getBarsIterator(){

		class BarsIterator implements Iterator<List<Bar>>{
			private int barNumber = 0;

			public boolean hasNext() {
				return (barNumber < getNumberOfBars());
			}
			/**
			 * Starts with notes from the first voice.
			 */
			public List<Bar> next() {
				List<Bar> voiceNotes = new ArrayList<Bar>();
				for(InstrumentTrack instrumentTrack : instrumentTracks){
					voiceNotes.add(instrumentTrack.getBar(barNumber));
				}
				
				barNumber++;
				
				return voiceNotes;
			}

			public void remove() {
				// GENERATED_TAG Auto-generated method stub
				
			}
			
		}

		
		return new Iterable<List<Bar>>(){
			public Iterator<List<Bar>> iterator() {
				return new BarsIterator();
			}		
		};
	}

	/**
	 * Returns the key of this composition object as defined at creation.
	 * @return
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * Returns the rule reports contained in this composition if there is any. If
	 * there isn't then null is returned.
	 * @return
	 */
	public List<RuleReport> getRuleReports() {
		return ruleReports;
	}
	
	/**
	 * Returns a map of the track number and the InstrumentTrackDetails object associated
	 * with the track. This is used when creating a counterpoint composition.
	 * @return
	 */
	public Map<Integer, InstrumentTrackDetails> getInstrumentTrackDetails(){
		
		Map<Integer, InstrumentTrackDetails> instrumentTrackDetailsMap = new HashMap<Integer, InstrumentTrackDetails>();
		for(InstrumentTrack instrumentTrack: instrumentTracks){
			instrumentTrackDetailsMap.put(instrumentTrack.getInstrumentNum(), 
					new InstrumentTrackDetails(instrumentTrack.getInstrumentNum(), instrumentTrack.getTrackName(), 
					instrumentTrack.getInstrumentType()));
		}
		
		return instrumentTrackDetailsMap;
	}

	/**
	 * Returns the title of the composition.
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the Tempo object of this composition.
	 * @return
	 */
	public Tempo getTempo() {
		return tempo;
	}

	/**
	 * Sets the Instrument tracks of this composition. This should be called when
	 * creating a composition from counterpoint composition.
	 * @param instrumentTracks
	 */
	public void setInstrumentTracks(List<InstrumentTrack> instrumentTracks) {
		this.instrumentTracks = instrumentTracks;
	}
	
	/**
	 * Returns the harmony lockdown map of the composition. This is map relating
	 * each step (set of bars of a given sequence number from each track) to a 
	 * particular chord/harmony.  
	 * Note: Not all steps may be mapped to a harmony.
	 * @return
	 */
	public Map<Integer, Collection<Chord>> getHarmonyLockdownMap() {
		return harmonyLockdownMap;
	}

	/**
	 * Sets a harmony lockdown mapping for a particular step. 
	 * @param barNum
	 * @param harmonies
	 */
	public void addHarmonyLockdownMapping(Integer barNum, Collection<Chord> harmonies) {
		this.harmonyLockdownMap.put(barNum, harmonies);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public void setTimeSignature(TimeSignature timeSignature) {
		this.timeSignature = timeSignature;
	}

	public void setTempo(Tempo tempo) {
		this.tempo = tempo;
	}

	public void setNumberOfBars(int numberOfBars) {
		this.numberOfBars = numberOfBars;
	}

}


















