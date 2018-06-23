/**
 * This file is created by Yang(James) CHEN.
 */
package nz.ara.game.model.role;

import nz.ara.game.model.impl.point.PointImpl;
import nz.ara.game.model.in.point.Point;

/**
 * @author yac0105
 *
 */
public class GameRole {

	protected Point startPosition = new PointImpl(0,0);
	
	protected Point position;

	public Point getPosition() {
		return position;
	}


	public void setPosition(Point position) {
		this.position = position;
	}


	public Point getStartPosition() {
		return startPosition;
	}


	public void setStartPosition(Point startPosition) {
		this.startPosition = startPosition;
	}
}
