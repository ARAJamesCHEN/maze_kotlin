/**
 * 
 */
package nz.ara.game.model.in.saveable;

import nz.ara.game.model.em.wall.Wall;
import nz.ara.game.model.in.point.Point;

/**
 * @author yac0105
 *
 */
public interface Saveable {
	int getWidthAcross();
	
	int getDepthDown();
	
	Wall whatsAbove(Point where);
	
	Wall whatsLeft(Point where);
	
	Point wheresTheseus();
	
	Point wheresMinotaur(); Point wheresExit();
	
}
