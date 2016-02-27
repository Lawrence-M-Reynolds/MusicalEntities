package composition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import composition.reporting.RuleReport;


import musicRelatedEntities.chord.Chord;
import musicRelatedEntities.key.Key;
import musicRelatedEntities.time.Tempo;
import musicRelatedEntities.time.TimeSignature;

/**
 * Created so that the counterpoint composition reference can be pulled out of the composition
 * object. This abstraction is now passed into the copy constructor.
 * @author LMR
 *
 */
public interface IComposition {

	public String getTitle();

	public Key getKey();

	public TimeSignature getTimeSignature();

	public Tempo getTempo();

	public Map<Integer, Collection<Chord>> getHarmonyLockdownMap();

	public List<RuleReport> getRuleReports();

}
