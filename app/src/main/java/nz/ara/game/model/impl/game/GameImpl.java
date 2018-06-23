/**
 * This file is created by Yang(James) CHEN.
 */
package nz.ara.game.model.impl.game;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import android.util.Log;
import nz.ara.game.model.bean.maze.MazeBean;
import nz.ara.game.model.em.constvalue.Const;
import nz.ara.game.model.em.direction.Direction;
import nz.ara.game.model.impl.loadable.LoadableImpl;
import nz.ara.game.model.impl.point.PointImpl;
import nz.ara.game.model.impl.saveable.SaveableImpl;
import nz.ara.game.model.impl.saver.SaverImpl;
import nz.ara.game.model.in.game.Game;
import nz.ara.game.model.in.point.Point;
import nz.ara.game.model.in.saver.Saver;
import nz.ara.game.model.role.minotaur.Minotaur;
import nz.ara.game.model.role.theseus.Theseus;
import nz.ara.game.model.util.tools.UtilTools;

/**
 * @author yac0105
 *
 */
public class GameImpl implements Game {

	private String[] levels = {"Level-1","Level-2","Level-3","Level-4","Level-5","Level-6","Level-7","Level-8","Level-9","Level-10"};

	private static final String TAG = "GameImpl";
	
	private int level = -1;
	
	private String filePath = Const.FILE_PATH.getValue();
	
	private int stepWidth = 1;
	
	private int stepHeight = 1;
	
	private LoadableImpl loadable;
	
	private SaveableImpl saveable;
	
	private Theseus theseus = new Theseus();
	
	private Minotaur minotaur = new Minotaur();
	
	private MazeBean mazeBean;
	
	/**
	 * 0: play 1:killed 2: exit
	 */
	private Const status = Const.STATUS_PLAY;
	//the
	private int moveCount = 0;
	
	private int minMoveCount = 0;
	
	public GameImpl(){
		this.level = 1;
		this.setUp(null);
	}
	
	public GameImpl(int level) {
		this.level = level;
		this.setUp(null);
	}
	
	public GameImpl(int level, Const loadType) {
		this.level = level;
		this.setUp(loadType);
	}

	public GameImpl(int level, int stepWidth, int stepHeight){
		this.stepWidth = stepWidth;
		this.stepHeight = stepHeight;
		this.level = level;
		this.setUp(null);
	}

	public GameImpl(String level_string, String filePath, Const loadType){

		this.level = getLevelByLevelStr(level_string);

		this.filePath = filePath;

		Log.d(TAG, this.filePath);

		this.setUp(loadType);
	}
	
	private String setUp(Const loadType) {
		
		this.status = Const.STATUS_PLAY;
		
		this.moveCount = 0;
		
		this.minMoveCount = 0;
		
		if(loadType == null) {
			if(!this.loadGameByFile(this.level)) {
				Log.d(TAG,"Load from string");
				
				this.loadGameByString(this.level);
			}
		}else if(loadType.equals(Const.LOAD_BY_FILE)) {
			this.loadGameByFile(this.level);
		}else if(loadType.equals(Const.LOAD_BY_STR)) {
			this.loadGameByString(this.level);
		}else {
			Log.e(TAG,"no match load style");
		}
		
		try {
			//to keep the loadable side and game side independent 
			//as the game msg may change time by time
			this.mazeBean = loadable.getMazeBean();//(MazeBean) UtilTools.copyObj(loadable.getMazeBean());

			this.mazeBean.setWallAbovePointListStr(this.changePointListToStr(this.mazeBean.getWallAbovePointList()));;
			this.mazeBean.setWallLeftPointListStr(this.changePointListToStr(this.mazeBean.getWallLeftPointList()));
			this.mazeBean.setWallSquareStr(this.getWallSquareStr(this.level));

		} catch (Exception e) {
			Log.e(TAG,"has error when copy from loadable mazebean",e);
		}
		
		this.stepWidth = loadable.getStepWidth();
		this.stepHeight = loadable.getStepHeight();
		
		theseus.setPosition(this.mazeBean.getTheStartPoint());
		theseus.setStartPosition(this.mazeBean.getTheStartPoint());
		theseus.setHasWon(false);
		
		minotaur.setPosition(this.mazeBean.getMinStartPoint());
		minotaur.setStartPosition(this.mazeBean.getMinStartPoint());
		minotaur.setHasEaten(false);
		minotaur.setCanNotMove(0);
		
		return "sucess";
	}
	
	/**
	 * 1. Load a level from a file
	 * @param theLevel
	 * @return
	 */
	public boolean loadGameByFile(int theLevel) {
		
		//this.moveCount = 0;
		
		boolean isLoaded = false;
		
		loadable = new LoadableImpl(theLevel, this.filePath,this.stepWidth, this.stepHeight);

		try {
			isLoaded = loadable.loadByFile();
		} catch (FileNotFoundException e) {
			Log.e(TAG,e.getLocalizedMessage(),e);
			isLoaded = false;
		}
		
		return isLoaded;
	}
	
	/**
	 * 21. load by string
	 * @param theLevel
	 */
	public boolean loadGameByString(int theLevel) {

		boolean isLoaded = false;
		//this.moveCount = 0;
		loadable = new LoadableImpl(theLevel, this.filePath,this.stepWidth, this.stepHeight);
		
		String levelString = this.getLevelStringByConst(this.level);
		Log.d(TAG,"Load from string: " + levelString);
		if(UtilTools.isBlank(levelString)){
			return false;
		}

		loadable.loadByString(levelString);
		return true;
	}
	
	/**
	 * 2. Save a level to a file
	 */
	public boolean save() {
		
		boolean isSucess = true;
		
		try {
			saveable = new SaveableImpl(this);
			
			Saver saver = new SaverImpl(this.level,this.filePath);
			
			saver.save(saveable);
		} catch (Exception e) {
			isSucess = false;
			Log.e(TAG,e.getLocalizedMessage(),e);
		}
		
		return isSucess;
	}
	

	/**
	 * 22. reset or go to another level
	 * @param aNewLevel
	 */
	public void reLoad(int aNewLevel, Const loadType) {
		if(aNewLevel<1) {
			this.level = 1;
		}
		
		this.level = aNewLevel;
		
		this.setUp(Const.LOAD_BY_STR);
	}

	public void reLoad(String aNewLevelStr, Const loadType) {

		int aNewLevel = this.getLevelByLevelStr(aNewLevelStr);

		this.reLoad(aNewLevel,loadType);
	}
	
	
	/* (non-Javadoc)
	 * one step, loop twice for each move (one the step, two min steps)
	 * @see nz.ara.game.model.in.game.Game#moveMinotaur()
	 */
	@Override
	public void moveMinotaur() {
		//18. Stops after kills
		//19. Stops after exits
		if(!this.status.equals(Const.STATUS_PLAY)) {
			Log.e(TAG,"moveMinotaur error status:" + this.status);
			this.minMoveCount = 0;
			this.minotaur.setCanNotMove(0);
			return;
		}

		Point thePosition = this.theseus.getPosition();

		Point minPoint = this.minotaur.getPosition();

		if(this.moveMinotaurLogic(thePosition, minPoint)) {
			this.minMoveCount++;
		}else {
			int canNotMove = this.minotaur.getCanNotMove();
			canNotMove++;
			this.minotaur.setCanNotMove(canNotMove);
		}

		if((this.minMoveCount + this.minotaur.getCanNotMove())>=2) {
			this.minMoveCount = 0;
			this.minotaur.setCanNotMove(0);
		}

		
	}

	public boolean shouldMoveMin(){
		if((this.minMoveCount + this.minotaur.getCanNotMove())<2) {

			return true;

		}
		return false;
	}
	
	
	public boolean moveMinotaurLogic(Point thePosition, Point minPoint) {

		if(thePosition.across() == minPoint.across()
				&& thePosition.down() == minPoint.down()){

			if(this.checkEaten()) {
				Log.d(TAG,"Killed!");
				this.minotaur.setHasEaten(true);
				this.status = Const.STATUS_EATEN;
			}

			return true;
		}

		if(this.moveMinotaurHorizontally(thePosition,minPoint)) {
			return true;
		}
		
		if(this.moveMinotaurVertically(thePosition, minPoint)) {
			return true;
		}
		
		return false;
	}
	
	public boolean moveMinotaurHorizontally(Point thePosition, Point minPoint) {
		
        Point nextPosition = null;
		
		int nextAcross = -1;
		
		int nextDepth = -1;
		
        if(minPoint.across() > thePosition.across()) {
			
			//move left
			//16. No Minotaur moving through a wall – loses move
			if(this.checkCanMoveLeftOrRight(minPoint)) {
				//9. Minotaur moves LEFT
				nextAcross = minPoint.across() + Direction.LEFT.getAcross();
				nextDepth = minPoint.down() + Direction.LEFT.getDown();
				nextPosition = new PointImpl(nextAcross, nextDepth);
				//20. No Minotaur onto exit
				if(!this.cheackExit(nextPosition)) {
					this.minotaur.setPosition(nextPosition);
				}else {
					this.status = Const.STATUS_ERROR;
				}
				
				if(this.checkEaten()) {
					Log.d(TAG,"Killed!");
		    		this.minotaur.setHasEaten(true);
		    		this.status = Const.STATUS_EATEN;
				}
				
				if(!this.cheackExit(nextPosition)) {
					return true;
				}
				
			}
			
		}else if(minPoint.across() < thePosition.across()){
			
			//10. Minotaur moves RIGHT
			nextAcross = minPoint.across() + Direction.RIGHT.getAcross();
			nextDepth = minPoint.down() + Direction.RIGHT.getDown();
			
			nextPosition = new PointImpl(nextAcross, nextDepth);
			
			//move right
			if(this.checkCanMoveLeftOrRight(nextPosition)) {
				if(!this.cheackExit(nextPosition)) {
					this.minotaur.setPosition(nextPosition);
				}else {
					this.status = Const.STATUS_ERROR;
				}
				
				//12. Minotaur kills Theseus
				if(this.checkEaten()) {
					Log.d(TAG,"Killed!");
		    		this.minotaur.setHasEaten(true);
		    		this.status = Const.STATUS_EATEN;
				}
				
				if(!this.cheackExit(nextPosition)) {
					return true;
				}
				
			}
			
		}
        
        return false;
		
	}
	
	
	public boolean moveMinotaurVertically(Point thePosition, Point minPoint) {
		
        Point nextPosition = null;
		
		int nextAcross = -1;
		
		int nextDepth = -1;
		
		if(minPoint.down() > thePosition.down()) {
			//move up
			if(this.checkCanMoveUpOrDown(minPoint)) {
				//8. Minotaur moves UP
				nextAcross = minPoint.across() + Direction.UP.getAcross();
				nextDepth = minPoint.down() + Direction.UP.getDown();
				nextPosition = new PointImpl(nextAcross, nextDepth);
				if(!this.cheackExit(nextPosition)) {
					this.minotaur.setPosition(nextPosition);
				}else {
					this.status = Const.STATUS_ERROR;
				}
				
				if(this.checkEaten()) {
					Log.d(TAG,"Killed!");
		    		this.minotaur.setHasEaten(true);
		    		this.status = Const.STATUS_EATEN;
				}
				
				if(!this.cheackExit(nextPosition)) {
					return true;
				}
			}
		}else if(minPoint.down() < thePosition.down()) {
			//move down
			//11. Minotaur moves DOWN
			nextAcross = minPoint.across() + Direction.DOWN.getAcross();
			nextDepth = minPoint.down() + Direction.DOWN.getDown();
			
			nextPosition = new PointImpl(nextAcross, nextDepth);
			if(this.checkCanMoveUpOrDown(nextPosition)) {
				
				if(!this.cheackExit(nextPosition)) {
					this.minotaur.setPosition(nextPosition);
				}else {
					this.status = Const.STATUS_ERROR;
				}
				
				if(this.checkEaten()) {
					Log.d(TAG,"Killed!");
		    		this.minotaur.setHasEaten(true);
		    		this.status = Const.STATUS_EATEN;
				}
				
				if(!this.cheackExit(nextPosition)) {
					return true;
				}
			}
		}
		
		return false;
		
	}
	
	public boolean checkEaten() {
		if(this.minotaur.getPosition().across() == this.theseus.getPosition().across() &&
				this.minotaur.getPosition().down() == this.theseus.getPosition().down()) {
			return true;
		}
		
		return false;
	}

	@Override
	public void moveTheseus(Direction direction) {
		//18. Stops after kills
		//19. Stops after exits
		if(!this.status.equals(Const.STATUS_PLAY)) {
			Log.e(TAG,"moveTheseus error status:" + this.status);
			return;
		}
		
		Point currentPoint = this.theseus.getPosition();
		
		Point position = null;
		
		int nextAcross = -1;
		
		int nextDepth = -1;
		
		
        if(direction.equals(Direction.PAUSE)) {
        	//7. Theseus pauses
        	Log.d(TAG,"Direction:" + Direction.PAUSE);
			
        	//this.moveMinotaur();
			
		}else if(direction.equals(Direction.UP)) {
			//3. Theseus moves UP
			Log.d(TAG,"Direction:" + Direction.UP);
			//17. No Theseus moving through a wall – not a valid move
			if(this.checkCanMoveUpOrDown(currentPoint)) {
				nextAcross = currentPoint.across() + Direction.UP.getAcross()*this.stepHeight;
				nextDepth = currentPoint.down() + Direction.UP.getDown()*this.stepHeight;
				
				position = new PointImpl(nextAcross,nextDepth);
				
				this.theseus.setPosition(position);
				//14. Counts moves
				this.moveCount++;
				
				//this.moveMinotaur();
				//13. Theseus wins/escapes
				if(this.cheackExit(position)) {
					Log.d(TAG,"Win!");
		    		this.theseus.setHasWon(true);
		    		this.status = Const.STATUS_WIN;
				}
			}
			
		}else if(direction.equals(Direction.DOWN)) {
			//6. Theseus moves DOWN
			Log.d(TAG,"Direction:" + Direction.DOWN);
		    nextAcross = currentPoint.across() + Direction.DOWN.getAcross()*this.stepHeight;
			nextDepth = currentPoint.down() + Direction.DOWN.getDown()*this.stepHeight;
			
			position = new PointImpl(nextAcross,nextDepth);
			
			if(this.checkCanMoveUpOrDown(position)) {
				
                this.theseus.setPosition(position);
                this.moveCount++;
				//this.moveMinotaur();
				
				if(this.cheackExit(position)) {
					Log.d(TAG,"Win!");
		    		this.theseus.setHasWon(true);
		    		this.status = Const.STATUS_WIN;
				}
				
			}
			
		}else if(direction.equals(Direction.LEFT)) {
			//4. Theseus moves LEFT
			Log.d(TAG,"Direction:" + Direction.LEFT);
			if(this.checkCanMoveLeftOrRight(currentPoint)) {
				nextAcross = currentPoint.across() + Direction.LEFT.getAcross()*this.stepWidth;
				nextDepth = currentPoint.down() + Direction.LEFT.getDown()*this.stepWidth;
				
				position = new PointImpl(nextAcross,nextDepth);
				
                this.theseus.setPosition(position);
                this.moveCount++;
				//this.moveMinotaur();
				
				if(this.cheackExit(position)) {
					Log.d(TAG,"Win!");
		    		this.theseus.setHasWon(true);
		    		this.status = Const.STATUS_WIN;
				}
			}
			
		}else if(direction.equals(Direction.RIGHT)) {
			//5. Theseus moves RIGHT
			Log.d(TAG,"Direction:" + Direction.RIGHT);
			nextAcross = currentPoint.across() + Direction.RIGHT.getAcross()*this.stepWidth;
			nextDepth = currentPoint.down() + Direction.RIGHT.getDown()*this.stepWidth;
			
			position = new PointImpl(nextAcross,nextDepth);
			
			if(this.checkCanMoveLeftOrRight(position)) {
                this.theseus.setPosition(position);
                this.moveCount++;
				//this.moveMinotaur();
				
				if(this.cheackExit(position)) {
					Log.d(TAG,"Win!");
		    		this.theseus.setHasWon(true);
		    		this.status = Const.STATUS_WIN;
				}
			}
			
		}else {
			Log.e(TAG,"cannot match your direction!");
		}
		
		
	}
	
	public boolean checkCanMoveUpOrDown(Point tp) {
		
		if(tp.down() <0) {
			return false;
		}
		
		List<Point> aboveWallList = this.getMazeBean().getWallAbovePointList();
		
		Iterator<Point> iter = aboveWallList.iterator();
		
		while(iter.hasNext()) {
			Point p = iter.next();
			
			if(tp.across()==p.across() && tp.down()==p.down()) {
				Log.d(TAG,"Wall! Cannot move:" + tp.across() + "," + tp.down());
				return false;
			}
			
		}
		
		return true;
	}
	
    public boolean checkCanMoveLeftOrRight(Point tp) {
    	
    	if(tp.across()<0) {
    		return false;
    	}
		
    	List<Point> leftWallPointList = this.getMazeBean().getWallLeftPointList();
		
    	Iterator<Point> iter = leftWallPointList.iterator();
    	
		while(iter.hasNext()) {
			Point p = iter.next();
			
			if(tp.across()==p.across() && tp.down()==p.down()) {
				Log.d(TAG,"Wall! Cannot move:" + tp.across() + "," + tp.down());
				return false;
			}
			
		}
		
		return true;
	}
	
    public boolean cheackExit(Point tp) {
    	
    	Point p = this.mazeBean.getExitPoint();
    	
    	if(tp.across() == p.across() && tp.down() == p.down()) {
    		return true;
    	}
    	
    	return false;
    }
    
    
    public String getLevelStringByConst(int level) {
    	switch(level) {
    	    case 1: 
    	    	return Const.LEVEL_ONE.getValue();
    	    case 2: 
    	    	return Const.LEVEL_TWO.getValue();
    	    case 3: 
    	    	return Const.LEVEL_THREE.getValue();
    	    case 4:
    	    	return Const.LEVEL_FOUR.getValue();
    	    case 5:
    	    	return Const.LEVEL_FIVE.getValue();
    	    case 6:
    	    	return Const.LEVEL_SIX.getValue();
    	    case 7:
    	    	return Const.LEVEL_SEVEN.getValue();
    	    case 8:
    	    	return Const.LEVEL_EIGHT.getValue();
    	    case 9:
    	    	return Const.LEVEL_NINE.getValue();
    	    case 10:
    	    	return Const.LEVEL_TEN.getValue();
    	    default: 
    	    	return Const.LEVEL_ONE.getValue();
    	}
    }

	public int getLevelByLevelStr(String levelStr) {
		switch(levelStr) {
			case "Level-1":
				return 1;
			case "Level-2":
				return 2;
			case "Level-3":
				return 3;
			case "Level-4":
				return 4;
			case "Level-5":
				return 5;
			case "Level-6":
				return 6;
			case "Level-7":
				return 7;
			case "Level-8":
				return 8;
			case "Level-9":
				return 9;
			case "Level-10":
				return 10;
			default:
				return 1;
		}
	}

	public String getWallSquareStr(int level) {
		switch(level) {
			case 1:
				return "4,4";
			case 2:
				return "8,8";
			case 3:
				return "5,5";
			case 4:
				return "7,7";
			case 5:
				return "8,8";
			case 6:
				return "8,8";
			case 7:
				return "8,8";
			case 8:
				return "11,11";
			case 9:
				return "11,11";
			case 10:
				return "11,11";
			default:
				return "4,4";
		}
	}

	private String changePointListToStr(List<Point> wallPoints){

		String result = "";

		for(int i= 0; i<wallPoints.size(); i++){
			result+= wallPoints.get(i).across() + "," + wallPoints.get(i).down();

			if(i != (wallPoints.size()-1)){
				result+="|";
			}

		}

		return result;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		
		this.level = level;
		
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public LoadableImpl getLoadable() {
		return loadable;
	}

	public void setLoadable(LoadableImpl loadable) {
		this.loadable = loadable;
	}

	public Theseus getTheseus() {
		return theseus;
	}

	public void setTheseus(Theseus theseus) {
		this.theseus = theseus;
	}

	public Minotaur getMinotaur() {
		return minotaur;
	}

	public void setMinotaur(Minotaur minotaur) {
		this.minotaur = minotaur;
	}

	public MazeBean getMazeBean() {
		return mazeBean;
	}

	public void setMazeBean(MazeBean mazeBean) {
		this.mazeBean = mazeBean;
	}

	public int getStepWidth() {
		return stepWidth;
	}

	public void setStepWidth(int stepWidth) {
		this.stepWidth = stepWidth;
	}

	public int getStepHeight() {
		return stepHeight;
	}

	public void setStepHeight(int stepHeight) {
		this.stepHeight = stepHeight;
	}

	public SaveableImpl getSaveable() {
		return saveable;
	}

	public void setSaveable(SaveableImpl saveable) {
		this.saveable = saveable;
	}

	public Const getStatus() {
		return status;
	}

	public void setStatus(Const status) {
		this.status = status;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}

	public String[] getLevels() {
		return levels;
	}

	public void setLevels(String[] levels) {
		this.levels = levels;
	}
}
