/**
 * This file is created by Yang(James) CHEN.
 */
package nz.ara.game.model.impl.loadable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.util.Log;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import nz.ara.game.model.bean.maze.MazeBean;
import nz.ara.game.model.em.constvalue.Const;
import nz.ara.game.model.impl.point.PointImpl;
import nz.ara.game.model.in.loadable.Loadable;
import nz.ara.game.model.in.point.Point;
import nz.ara.game.model.util.tools.UtilTools;

/**
 * @author yac0105
 *
 */
public class LoadableImpl implements Loadable {

	private static final String TAG = "LoadableImpl";
	
	private int level;
	
	private String absoluteFilePath;
	
	private String levelString;
	
	private MazeBean mazeBean = new MazeBean();

	private int stepWidth = 1;
	
	private int stepHeight = 1;
	
	private int maxX;
	
	private int maxY;
	
    public LoadableImpl(int level, String absoluteFilePath) {
    	this.level = level;
    	this.absoluteFilePath = absoluteFilePath;
        this.mazeBean.setLevel(level);
    }

	public LoadableImpl(int level, String absoluteFilePath,int stepWidth, int stepHeight) {
		this.level = level;
		this.absoluteFilePath = absoluteFilePath;
		this.mazeBean.setLevel(level);
		this.stepWidth = stepWidth;
		this.stepHeight = stepHeight;
	}
    
    public boolean loadByFile() throws FileNotFoundException {
    	this.levelString = this.readLevelStringByFile(this.level);

    	if(UtilTools.isBlank(this.levelString)){
            return false;
		}

		try {
    		this.load(this.level);
		}catch (Exception e){
			return false;
		}

		return true;

    }
    
    public void loadByString(String string) {
    	this.levelString = string;
    	this.load(this.level);
    }
    
    public void load(int theLevel){
    	//read the level msg from the file
    	//U=oxxxo,xoxoo,xoxoo,oxxxo;L=oooo,xoxo,oxoo,oooo,xxxo;M=2,0;T=2,2;E=0,1:
    	Log.d(TAG,"levelString:" + levelString);
    	
    	String[] levelStrArray = levelString.split(";");
    	
    	//give the part string msg from the array
    	String beginString = null;
    	String wallUPString = null;
    	String wallLeftString = null;
    	String minString = null;
    	String theString = null;
    	String exitString = null;
    	
    	for(String str: levelStrArray) {
    		if(str.contains("B=")) {
    			beginString = str;
    			Log.d(TAG,"beginString:" + beginString);
    		}else if(str.contains("U=")) {
    			wallUPString = str;
    			Log.d(TAG,"wallUPString:" + wallUPString);
    		}else if(str.contains("L=")) {
    			wallLeftString = str;
    			Log.d(TAG,"wallLeftString:" + wallLeftString);
    		}else if(str.contains("M=")) {
    			minString = str;
    			Log.d(TAG,"minString:" + minString);
    		}else if(str.contains("T=")) {
    			theString = str;
    			Log.d(TAG,"theString:" + theString);
    		}else if(str.contains("E=")) {
    			exitString = str;
    			Log.d(TAG,"exitString:" + exitString);
    		}
    	}
    	
    	if(!UtilTools.isBlank(beginString)) {
    		Point beginPoint = this.extractPointFromString(beginString);
        	this.setWidthAcross(beginPoint.across());
        	this.setDepthDown(beginPoint.down());
    	}

    	//process walls above
    	if(!UtilTools.isBlank(wallUPString)) {
    		processWalls(wallUPString, Const.UP.name());
    	}
    	
    	
    	//process walls left
    	if(!UtilTools.isBlank(wallLeftString)) {
    		processWalls(wallLeftString, Const.LEFT.name());
    	}
    	
    	if(!UtilTools.isBlank(wallLeftString) && exitString.contains(":")) {
    		exitString = exitString.replace(":", "");
    	}
    	Log.d(TAG,"exitString:" + exitString);
    	
    	Point minStPoint = this.extractPointFromString(minString);
    	this.addMinotaur(minStPoint);
    	
    	Point theStPoint = this.extractPointFromString(theString);
    	this.addTheseus(theStPoint);
    	
    	Point exPoint = this.extractPointFromString(exitString);
    	this.addExit(exPoint);
    	
    }
    
    /**
     * realtive point, should add widthAcross, depthdown for drawing
     * @param wallStr
     * @param wallType UP: above LEFT:left
     */
    public void processWalls(String wallStr, String wallType) {
    	if(wallStr.contains("=")) {
    		wallStr = wallStr.substring(wallStr.indexOf("=")+1);
		}
    	
    	String[] wallStrArray = wallStr.split(",");
    	
    	int x = 0;
		int y = 0;

    	for(int i=0; i<wallStrArray.length; i++) {
	
    		char[] wallCharArray = wallStrArray[i].toCharArray();
    		
    		if(UtilTools.equal(wallType, Const.UP.name())) {
    			maxX = wallCharArray.length;
    		}else if(UtilTools.equal(wallType, Const.LEFT.name())) {
    			maxY = wallCharArray.length;
    		}
    		
    		for(char ch : wallCharArray) {
    			if(ch == 'x') {
    				Point point = new PointImpl(x,y);
    				
    				if(UtilTools.equal(wallType, Const.UP.name())) {
    					this.addWallAbove(point);
    				}else if(UtilTools.equal(wallType, Const.LEFT.name())) {
    					this.addWallLeft(point);
    				}else {
    					Log.e(TAG,"processWalls has error");
    					
    				}
    				
    			}
    			
    			if(UtilTools.equal(wallType, Const.UP.name())) {
    				x+= stepWidth;
				}else if(UtilTools.equal(wallType, Const.LEFT.name())) {
					y+= stepHeight;
				}
    			
    		}
            
    		if(UtilTools.equal(wallType, Const.UP.name())) {
    			x=0;
        		y+=stepHeight;
			}else if(UtilTools.equal(wallType, Const.LEFT.name())) {
				x+=stepWidth;
	    		y=0;
			}
    		
    	}
    }
    
    /**
     * 
     * @param pointStr
     * @return
     */
    public Point extractPointFromString(String pointStr) {
    	Point point = null;
    	
    	if(!UtilTools.isBlank(pointStr) && pointStr.contains(",")) {
    		
    		if(pointStr.contains("=")) {
    			pointStr = pointStr.substring(pointStr.indexOf("=")+1);
    		}
    		
    		String[] theArray = pointStr.split(",");
    		
    		int x = Integer.parseInt(theArray[0]);
    		int y = Integer.parseInt(theArray[1]);
    		
    		x = x*this.stepWidth;
    		y = y*this.stepHeight;
    		
    		point = new PointImpl(x,y);
    		
    	}
    	
    	return point;
    	
    }

    /**
     * 
     * @return
     * @throws FileNotFoundException
     */
    public String readLevelStringByFile(int thelevel) throws FileNotFoundException{

		if(!absoluteFilePath.endsWith(File.separator)) {
			absoluteFilePath += File.separator;
		}

    	String filePath = this.absoluteFilePath + Const.LEVEL_FILE_NAME.getValue();
       
    	String  levelMsgFromFile = this.loadFileMsgByLevel(filePath, thelevel);
    	
    	return levelMsgFromFile;
    
    }
    
    /**
     * 
     * @param absoluteFilePath
     * @param level
     * @return
     * @throws FileNotFoundException 
     */
    private String loadFileMsgByLevel(String absoluteFilePath, int level) throws FileNotFoundException{
		String result = "";

		//File to be read
		FileInputStream fis = null;
		
		XSSFWorkbook workbook = null;
		
	    try {
			fis = new FileInputStream(new File(absoluteFilePath));
		
			//Creating workbook object
		    workbook = new XSSFWorkbook(fis);
	   
		    //getting a spreadsheet
		    XSSFSheet spreadsheet = workbook.getSheetAt(0);
	    
		    //to get last row number in spreadsheet
		    int rowcount=spreadsheet.getLastRowNum();
		    
		    int count = 0;
		    
		    for (int i=0; i<=rowcount; i++) {
			    //getting a row
			    XSSFRow row = spreadsheet.getRow(i);
			        
			    if(row == null) {
			        continue;
			    }
			        
			    Cell cel= null;
			        
			    for(int j =0 ; j<=24; j++) {
			        
			         //getting a cell
			         if(row.getCell(j)==null) {
			        	 continue;
			         }
			            
			         cel= row.getCell(j);
			         
			         String cellStr = cel.getStringCellValue();
			         
			         if(cellStr.contains("U=") && cellStr.contains("L=") && i <= 3 + 6*(level-1)) {
			        	 count++;
			        	 
			        	 if(count == level || i == 3 + 6*(level-1)) {
			        		 result =  cellStr.trim();
			        		 return result;
			        	 }
			        	 
			         }
			        
			    }
			        
		    }
		    
	    } catch (FileNotFoundException e) {
			Log.e(TAG,absoluteFilePath + ", FileNotFoundException",e);
	    	throw e;
		} catch (IOException e) {
			Log.e(TAG,e.getLocalizedMessage(),e);
		} catch (Exception e) {
			Log.e(TAG,e.getLocalizedMessage(),e);
		}finally {
			try {
				if(fis!=null) {
					fis.close();
				}
				
				if(workbook!=null ) {
					workbook.close();
				}
			} catch (Exception e) {
				Log.e(TAG,e.getLocalizedMessage(),e);
			}
			
			
		}
		
		return result;

    }


	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.loadable.Loadable#setWidthAcross(int)
	 */
	@Override
	public void setWidthAcross(int widthAcross) {
		
		//this.widthAcross = widthAcross;
		mazeBean.setWidthAcross(widthAcross);

	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.loadable.Loadable#setDepthDown(int)
	 */
	@Override
	public void setDepthDown(int depthDown) {
		
		//this.depthDown =depthDown;
		mazeBean.setDepthDown(depthDown);
		
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.loadable.Loadable#addWallAbove(nz.ara.game.model.in.point.Point)
	 */
	@Override
	public void addWallAbove(Point where) {
 
		//aboveWallPointList.add(where);
		mazeBean.getWallAbovePointList().add(where);
		
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.loadable.Loadable#addWallLeft(nz.ara.game.model.in.point.Point)
	 */
	@Override
	public void addWallLeft(Point where) {
		//leftWallPointList.add(where);
		mazeBean.getWallLeftPointList().add(where);
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.loadable.Loadable#addTheseus(nz.ara.game.model.in.point.Point)
	 */
	@Override
	public void addTheseus(Point where) {
		
		//this.theStartPoint = where;
		mazeBean.setTheStartPoint(where);
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.loadable.Loadable#addMinotaur(nz.ara.game.model.in.point.Point)
	 */
	@Override
	public void addMinotaur(Point where) {
		//this.minStartPoint = where;
		mazeBean.setMinStartPoint(where);
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.loadable.Loadable#addExit(nz.ara.game.model.in.point.Point)
	 */
	@Override
	public void addExit(Point where) {
		//this.exitPoint = where;
		mazeBean.setExitPoint(where);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getAbsoluteFilePath() {
		return absoluteFilePath;
	}

	public void setAbsoluteFilePath(String absoluteFilePath) {
		this.absoluteFilePath = absoluteFilePath;
	}


	public String getLevelString() {
		return levelString;
	}

	public void setLevelString(String levelString) {
		this.levelString = levelString;
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

	
	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public MazeBean getMazeBean() {
		return mazeBean;
	}

	public void setMazeBean(MazeBean mazeBean) {
		this.mazeBean = mazeBean;
	}


}
