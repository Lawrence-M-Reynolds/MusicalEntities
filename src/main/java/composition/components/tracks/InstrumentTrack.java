

package composition.components.tracks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import composition.components.InstrumentTrackDetails;
import composition.components.trackComponents.Bar;
import composition.components.trackComponents.InstrumentType;



/**
 * This represents a single track or voice in a composition and holds all of
 * the bars associated with it.
 */
public class InstrumentTrack implements Serializable,
		Comparable<InstrumentTrack>, Iterable<Bar> {
	private static final long serialVersionUID = 1L;
	private int trackNum;
	private String trackName;

	private InstrumentType instrumentType;
	private List<Bar> bars = new ArrayList<Bar>();
	
	/**
	 * Constructor used when constructing a new track. The number of bars to add
	 * is passed in which is obtained from the composition object.
	 * @param aTrackNum The Track number.
	 * @param aTrackName The Track Name.
	 * @param anInstrumentType The Track instrument type..
	 */
	InstrumentTrack(final int aTrackNum, final String aTrackName,
			final InstrumentType anInstrumentType) {
		trackName = aTrackName;
		instrumentType = anInstrumentType;
		trackNum = aTrackNum;
	}

	/**
	 * Constructor used by when translating from a counterpoint composition to
	 * a front end composition.
	 * @param instrumentTrackDetails
	 */
	InstrumentTrack(InstrumentTrackDetails instrumentTrackDetails) {
		trackName = instrumentTrackDetails.getTrackName();
		instrumentType = instrumentTrackDetails.getInstrumentType();
		trackNum = instrumentTrackDetails.getTrackNum();
	}

	/**
	 * Returns the sequence number of this track in the 
	 * composition. 
	 * @return
	 */
	public int getInstrumentNum() {
		return trackNum;
	}
	
	/**
	 * Adds the bar to the track.
	 * @param bar
	 */
	public void addBar(Bar bar){
		bars.add(bar);
	}
	
	/**
	 * Sets the track number of the track. This doesn't modify 
	 * any of the other tracks in the composition so that must
	 * be dealt with when calling this method.
	 * @param trackNum
	 */
	public void setTrackNum(int trackNum) {
		this.trackNum = trackNum;
	}

	/**
	 * Obtains the bar in the track at the given sequence
	 * location.
	 * @param barNumber
	 * @return
	 */
	public Bar getBar(final int barNumber) {
		return bars.get(barNumber);
	}

	/**
	 * Returns the name of the track. If the track doesn't have a name then the
	 * instrument name is returned instead. 
	 * @return
	 */
	public String getTrackName() {
		if (trackName != null) {
			return trackName;
		}
		return instrumentType.toString();
	}

	/**
	 * Sets the track name.
	 * @param trackName
	 */
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	/**
	 * Comparable is implemented so that the track with the lowest track number
	 * will be at the top of the list. 
	 * @param track
	 * @return
	 */
	public int compareTo(InstrumentTrack track) {
		return (track.getInstrumentNum() - this.getInstrumentNum());
	}

	/**
	 * Returns the number of bars held within this track.
	 * @return
	 */
	public int getNumberOfBars() {
		return bars.size();
	}

	/**
	 * Returns the list of bars held within this track.
	 * @return
	 */
	public List<Bar> getBars() {
		return bars;
	}	
	
	/**
	 * This method accepts a bar which should exist within this track and returns one
	 * in front of it. If it's the last bar then null is returned.
	 * @param aBar
	 * @return
	 */
	public Bar getNextBar(final Bar aBar){
		if(bars.size() > bars.indexOf(aBar)+1){		
			return bars.get((bars.indexOf(aBar) + 1));
		}
		return null;
	}

	/**
	 * Returns the list iterator of the list of bars contained in the track.
	 */
	public Iterator<Bar> iterator() {
		return bars.iterator();
	}

	/**
	 * Returns the InstrumentType of this track.
	 * @return
	 */
	public InstrumentType getInstrumentType() {
		return instrumentType;
	}	
}
