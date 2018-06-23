/**
 * This file is created by Yang(James) CHEN.
 */
package nz.ara.game.model.impl.saveable;

import java.util.Iterator;
import java.util.List;

import nz.ara.game.model.em.wall.Wall;
import nz.ara.game.model.impl.game.GameImpl;
import nz.ara.game.model.in.point.Point;

/**
 * @author yac0105
 *
 */
public class SaveableImpl implements nz.ara.game.model.in.saveable.Saveable {

	private static final String TAG = "SaveableImpl";
	
	private GameImpl gameImpl;
	
	public SaveableImpl(GameImpl gameImpl) {
		
		this.gameImpl = gameImpl;
		
	}
	
	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.saveable.Saveable#getWidthAcross()
	 */
	@Override
	public int getWidthAcross() {
		return gameImpl.getMazeBean().getWidthAcross();
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.saveable.Saveable#getDepthDown()
	 */
	@Override
	public int getDepthDown() {
		return gameImpl.getMazeBean().getDepthDown();
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.saveable.Saveable#whatsAbove(nz.ara.game.model.in.point.Point)
	 */
	@Override
	public Wall whatsAbove(Point where) {
		
		List<Point> aboveWallList = gameImpl.getMazeBean().getWallAbovePointList();
		
		for(Iterator<Point> itr = aboveWallList.iterator();itr.hasNext();) {
			
			Point point = itr.next();
			
			if(point.across()==where.across() && point.down() == where.down() ) {
				return Wall.SOMETHING;
			}
			
		}
		
		return Wall.NOTHING;
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.saveable.Saveable#whatsLeft(nz.ara.game.model.in.point.Point)
	 */
	@Override
	public Wall whatsLeft(Point where) {
        List<Point> leftWallPointList = gameImpl.getMazeBean().getWallLeftPointList();
		
		for(Iterator<Point> itr = leftWallPointList.iterator();itr.hasNext();) {
			
			Point point = itr.next();
			
			if(point.across()==where.across() && point.down() == where.down() ) {
				return Wall.SOMETHING;
			}
			
		}
		
		return Wall.NOTHING;
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.saveable.Saveable#wheresTheseus()
	 */
	@Override
	public Point wheresTheseus() {
		return gameImpl.getMazeBean().getTheStartPoint();
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.saveable.Saveable#wheresMinotaur()
	 */
	@Override
	public Point wheresMinotaur() {
		return gameImpl.getMazeBean().getMinStartPoint();
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.saveable.Saveable#wheresExit()
	 */
	@Override
	public Point wheresExit() {
		return gameImpl.getMazeBean().getExitPoint();
	}

	public GameImpl getGameImpl() {
		return this.gameImpl;
	}

	public void setGameImpl(GameImpl gameImpl) {
		this.gameImpl = gameImpl;
	}

}
