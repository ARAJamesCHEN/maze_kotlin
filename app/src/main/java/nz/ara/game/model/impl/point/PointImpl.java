/**
 * This file is created by Yang(James) CHEN.
 */
package nz.ara.game.model.impl.point;

import nz.ara.game.model.in.point.Point;

/**
 * @author yac0105
 *
 */
public class PointImpl implements Point {
	
	private int across;
	
	private int down;
	
	public PointImpl(int across, int down ) {
		this.across = across;
		this.down = down;
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.point.Point#across()
	 */
	@Override
	public int across() {
		return this.across;
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.point.Point#down()
	 */
	@Override
	public int down() {
		return this.down;
	}

}
