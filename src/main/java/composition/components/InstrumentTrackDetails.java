package composition.components;

import composition.components.trackComponents.InstrumentType;

/**
 * Track information is lost when it's 
 * converted into permuation objects and back again. This
 * allows the information to be carried through.
 * @author L Reynolds
 *
 */
public class InstrumentTrackDetails {
		private int trackNum;
		private String trackName;
		private InstrumentType instrumentType;
		
		public InstrumentTrackDetails(int trackNum, String trackName,
				InstrumentType instrumentType) {
			super();
			this.trackNum = trackNum;
			this.trackName = trackName;
			this.instrumentType = instrumentType;
		}

		public int getTrackNum() {
			return trackNum;
		}

		public String getTrackName() {
			return trackName;
		}

		public InstrumentType getInstrumentType() {
			return instrumentType;
		}

		public void setTrackNum(int trackNum) {
			this.trackNum = trackNum;
		}

		public void setTrackName(String trackName) {
			this.trackName = trackName;
		}

		public void setInstrumentType(InstrumentType instrumentType) {
			this.instrumentType = instrumentType;
		}

	
}
