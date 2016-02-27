package composition.reporting;

import java.io.Serializable;


/**
 * This holds the scoring value of a counterpoint composition which is changed
 * as the generator algorithm runs the evaluator rules. It then discards
 * weaker solutions based on the value. Currently there is only one overall
 * score value but in future this could be modified to have scoring of
 * different aspects.
 * @author BAZ
 *
 */
public class EvaluationScoringObject implements Serializable{
	
	//Various scoring values
	//The lower this value the better - a score of zero would mean that no rules
	//were broken.
	private int overallScore = 0;

	/**
	 * Returns the overall score value.
	 * @return
	 */
	public int getOverallScore() {
		return overallScore;
	}
	
	/**
	 * Adds the scoring to the overall score value.
	 * @param scoring
	 */
	public void addToScore(int scoring){
		overallScore += scoring;
	}

	/**
	 * Resets the scoring value. This is called for each iteration of the generation
	 * algorithm so that the score isn't forever increasing.
	 */
	public void reset() {
		overallScore = 0;	
	}

	@Override
	public String toString() {
		return "EvaluationScoringObject [overallScore=" + overallScore;
	}
}
