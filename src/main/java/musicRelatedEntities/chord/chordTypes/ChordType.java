package musicRelatedEntities.chord.chordTypes;

/**
 * Although this is passed into a chord on creation,
 * this object doesn't perform anything meaningful because
 * it will always build a triad. The idea was that
 * in future I may want to build 7ths... and the logic
 * could be put in the constructor using this value.
 * @author UKGC
 *
 */
public enum ChordType {

	TRIAD
}
