/**
 * This file is created by Yang(James) CHEN.
 */
package nz.ara.game.model.in.loadable;

import nz.ara.game.model.in.point.Point;

/**
 * @author yac0105
 *
 */
public interface Loadable {
	
	void setWidthAcross(int widthAcross);
	
	void setDepthDown(int depthDown);
	
	void addWallAbove(Point where);
	
	void addWallLeft(Point where);
	
	void addTheseus(Point where);
	
	void addMinotaur(Point where); void addExit(Point where);
	
}
