/**
 * This file is created by Yang(James) CHEN.
 */
package nz.ara.game.model.impl.saver;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import nz.ara.game.model.em.constvalue.Const;
import nz.ara.game.model.em.wall.Wall;
import nz.ara.game.model.impl.point.PointImpl;
import nz.ara.game.model.impl.saveable.SaveableImpl;
import nz.ara.game.model.in.point.Point;
import nz.ara.game.model.in.saveable.Saveable;
import nz.ara.game.model.in.saver.Saver;

/**
 * @author yac0105
 *
 */
public class SaverImpl implements Saver {

	private static final String TAG = "SaverImpl";
	
	private int level = 0;

	private String filePath;
	
	public SaverImpl(int level, String filePath) {
		this.level = level;
		this.filePath = filePath;
	}

    public String buildLevelMsgAsString(Saveable savable) {
    	
    	SaveableImpl s = (SaveableImpl) savable;
    	
		StringBuffer sf = new StringBuffer();
		
		//B=3,3
		String beginPointStr = "B=" + savable.getWidthAcross() + "," + savable.getDepthDown();
		Log.d(TAG,beginPointStr);
		sf.append(beginPointStr);
		sf.append(";");
		
		String aboveWallStr = "U=";
		int stepWidth = s.getGameImpl().getStepWidth();
		int stepHeight = s.getGameImpl().getStepHeight();
		
		int x = 0;
	    int y = 0;
		//5
	    int maxX = s.getGameImpl().getLoadable().getMaxX();
	    //4
	    int maxY = s.getGameImpl().getLoadable().getMaxY();
	    
		int j=0;
		while(j<maxY) {
			for(int i=0; i<maxX; i++) {

				Point aPoint = new PointImpl(x,y);
				
				if(savable.whatsAbove(aPoint).equals(Wall.SOMETHING)) {
        			aboveWallStr += "x";
        		}else {
        			aboveWallStr += "o";
        		}
				
				x+= stepWidth;
				
			}
			
			x = 0;
			if(j != maxY-1) {
				aboveWallStr += ",";
			}
			
			j++;
			y+=stepHeight; 
		}
		Log.d(TAG,aboveWallStr);
		sf.append(aboveWallStr);
		
		String leftWallStr = "L=";
		x=0;
		y=0;

		int p = 0;
		while(p<maxX) {
			for(int q=0; q<maxY; q++) {
				Point aPoint = new PointImpl(x,y);
				
				if(savable.whatsLeft(aPoint).equals(Wall.SOMETHING)) {
        			leftWallStr += "x";
        		}else {
        			leftWallStr += "o";
        		}
				
				y+=stepHeight;
				
			}
			
			y=0;
			if(p != maxX-1) {
				leftWallStr += ",";
			}
			
			p++;
			x+=stepWidth;
		}
        Log.d(TAG,leftWallStr);
        sf.append(";");
        sf.append(leftWallStr);
        
        //M=2,0
        Point point = savable.wheresMinotaur();
        int ac = stepWidth==0?point.across() : point.across()/stepWidth;
        int dn = stepHeight==0?point.down() : point.down()/stepHeight;
      	String minPointStr = "M=" + ac + "," + dn;
      	Log.d(TAG,minPointStr);
      	sf.append(";");
      	sf.append(minPointStr);
		
		//T=2,2
		point = savable.wheresTheseus();
		ac = stepWidth==0?point.across() : point.across()/stepWidth;
        dn = stepHeight==0?point.down() : point.down()/stepHeight;
		String thePointStr = "T=" + ac + "," + dn;
		Log.d(TAG,thePointStr);
		sf.append(";");
		sf.append(thePointStr);
		
		//E=0,1:
		point = savable.wheresExit();
		ac = stepWidth==0?point.across() : point.across()/stepWidth;
        dn = stepHeight==0?point.down() : point.down()/stepHeight;
		String exitPointStr = "E=" + ac + "," + dn;
		Log.d(TAG,exitPointStr);
		sf.append(";");
		sf.append(exitPointStr);
		sf.append(":");
		
		Log.d(TAG,"The save level [" + this.level + "] message: " + sf.toString());
		
		return sf.toString();
		
	}
	

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.saver.Saver#save(nz.ara.game.model.in.saveable.Saveable)
	 */
	@Override
	public void save(Saveable savable) {
		
		if(!filePath.endsWith(File.separator)) {
			filePath += File.separator;
		}
		
		File f = new File(filePath);
		if (!f.exists()|| !f.isDirectory()) {
		   f.mkdir();
		}
		
		String fileName = filePath + Const.LEVEL_FILE_NAME.getValue();
		
		this.save(savable, fileName);
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.saver.Saver#save(nz.ara.game.model.in.saveable.Saveable, java.lang.String)
	 */
	@Override
	public void save(Saveable savable, String fileName) {
		
		String levelName = this.level + "";
		
		this.save(savable, fileName, levelName);
		
	}

	/* (non-Javadoc)
	 * @see nz.ara.game.model.in.saver.Saver#save(nz.ara.game.model.in.saveable.Saveable, java.lang.String, java.lang.String)
	 */
	@Override
	public void save(Saveable savable, String fileName, String levelName) {

		String levelMsgString = this.buildLevelMsgAsString(savable); 
		
		this.writeMsgToFile(this.level, fileName, levelMsgString);
		
		Log.d(TAG,levelMsgString);
	}
	
	private void writeMsgToFile(int level, String fileName, String content) {

		System.setProperty("javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
		System.setProperty("javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
		System.setProperty("javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

		
		FileInputStream fis = null;
		
		FileOutputStream out = null;
		
		XSSFWorkbook workbook = null;
		
		try {
			
			try {
				fis = new FileInputStream(new File(fileName));
			} catch (FileNotFoundException e) {
				Log.d(TAG,"No File found: " + fileName);
			}
			
			
			XSSFSheet spreadsheet = null;
			
			int maxCol = 24;
			
			if(fis == null) {
				Log.d(TAG,"So we make it up totally! Happy!~");
				
				//Creating a blank workbook
				workbook = new XSSFWorkbook(); 
				
				spreadsheet = workbook.createSheet("Sheet1");  
				
				for(int rowNum = (level-1)*6; rowNum <=(level-1)*6 + 6; rowNum++) {
					
					//Creating row object
	                XSSFRow row= spreadsheet.createRow(rowNum);
					
					for(int colNum = 0; colNum<=maxCol; colNum++) {
						//Creating a cell
						row.createCell(colNum);

					}
					
					if(rowNum == 3 + 6*(level-1)) {
	                	try {
							CellRangeAddress cellMerge = new CellRangeAddress(rowNum, rowNum, 8, maxCol);
							//spreadsheet.addMergedRegion(cellMerge);
						} catch (Exception e) {
							Log.e(TAG,"some issures for creating the merge area");
						}

	                	Cell cell0 = row.getCell(7);
	                	if(cell0 == null) {
	                		cell0 = row.createCell(7);
                   	    }
	                	cell0.setCellValue("Level:" + level + ":");
	                	
	                	Cell cell = row.getCell(8);
	                	//Adding values to cell
	                    cell.setCellValue(content);
	                    CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
					}
					
					
				}
				
			}else {
				//reade content first
				Log.d(TAG,"update read it first and rewrite");
				workbook = new XSSFWorkbook(fis);
				spreadsheet = workbook.getSheetAt(0); 
				
				int rowMax = spreadsheet.getLastRowNum();
				
				int count = 0;
				
				boolean hasFound = false;
				
				for(int rowNum=0; rowNum<=rowMax; rowNum++) {
					 XSSFRow row = spreadsheet.getRow(rowNum);
					 
					 Cell cel= null;
					 
					 if(row!=null) {
						 for(int j =0 ; j<=24; j++) {
							 //getting a cell
						     if(row.getCell(j)==null) {
						         continue;
						     }
						         
						     cel= row.getCell(j);
						         
						     String cellStr = cel.getStringCellValue();
						       
						     if(cellStr.contains("U=") && cellStr.contains("L=")) {
						         count++;
						         if(count == level && rowNum == 3 + 6*(level-1)) {
						        	 
						        	 Cell cell0 = row.getCell(7);
						        	 if(cell0 == null) {
						        		 cell0 = row.createCell(7);
			                    	 }
					                 cell0.setCellValue("Level:" + level + ":");
						        	 
						        	 try {
										CellRangeAddress cellMerge = new CellRangeAddress(rowNum, rowNum, 8, maxCol);
										 //spreadsheet.addMergedRegion(cellMerge);
									} catch (Exception e) {
										
										Log.e(TAG,"some issures for creating the merge area");
										
									}
						        	 //Adding values to cell
						        	 cel.setCellValue(content);
						        	 CellUtil.setAlignment(cel, workbook, CellStyle.ALIGN_CENTER);
						        	 hasFound = true;
						        	 break;
						        }
						        	 
						     }
						 }
					 }
					 
					 
                     if(rowNum == 3 + 6*(level-1) && !hasFound) {
                    	 Log.d(TAG,"do not found the msg in the right place");
                    	 
                    	 if(row == null) {
                    		 spreadsheet.createRow(rowNum);
                    		 row = spreadsheet.getRow(rowNum);
                    	 }
                    	 
                    	 Cell cell0 = row.getCell(7);
                    	 if(cell0 == null) {
                    		 cell0 = row.createCell(7);
                    	 }
 	                	 cell0.setCellValue("Level:" + level + ":");
                    	 
                    	 cel= row.getCell(8);
                    	 
                    	 if(cel == null) {
                    		 cel = row.createCell(8);
                    	 }
                    	 
                    	 try {
							CellRangeAddress cellMerge = new CellRangeAddress(rowNum, rowNum, 8, maxCol);
							 //spreadsheet.addMergedRegion(cellMerge);
						} catch (Exception e) {
							Log.e(TAG,"some issures for creating the merge area");
						}
                    	 cel.setCellValue(content);
                    	 CellUtil.setAlignment(cel, workbook, CellStyle.ALIGN_CENTER);
			        	 hasFound = true;
			        	 break;
                    	 
					 }
				}
				
				if(!hasFound) {
					Log.d(TAG,"come on, hard work");
					
					for(int rowNum = 6*(level-1); rowNum <= 6*level; rowNum++) {
						
						//Creating row object
		                XSSFRow row= spreadsheet.createRow(rowNum);
						
						for(int colNum = 0; colNum<=maxCol; colNum++) {
							//Creating a cell
							row.createCell(colNum);
						}
						
						if(rowNum == 3 + 6*(level-1)) {
							
							Cell cell0 = row.getCell(7);
							if(cell0 == null) {
								cell0 = row.createCell(7);
	                    	}
	 	                	cell0.setCellValue("Level:" + level + ":");
							
							CellRangeAddress cellMerge = new CellRangeAddress(rowNum, rowNum, 8, maxCol);
							//spreadsheet.addMergedRegion(cellMerge);
		                	Cell cell = row.getCell(8);
		                	//Adding values to cell
		                    cell.setCellValue(content);
		                    CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
						}
						
						
					}
				}
				
			}
			
			out = new FileOutputStream(new File(fileName));
			
			workbook.write(out);
			
		} catch (FileNotFoundException e) {
			Log.e(TAG,e.getLocalizedMessage(),e);
			e.printStackTrace();
		}catch (IOException e) {
			Log.e(TAG,e.getLocalizedMessage(),e);
			e.printStackTrace();
		}catch (Exception e) {
			Log.e(TAG,e.getLocalizedMessage(),e);
			e.printStackTrace();
		}finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					Log.e(TAG,e.getLocalizedMessage(),e);
					e.printStackTrace();
				}
			}
			
			if(out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					Log.e(TAG,e.getLocalizedMessage(),e);
					e.printStackTrace();
				}
			}
			
			if(workbook!=null) {
				try {
					workbook.close();
				} catch (IOException e) {
					Log.e(TAG,e.getLocalizedMessage(),e);
					e.printStackTrace();
				}
			}
			
		}
		
		Log.d(TAG,"content has been wrote to file!");
		
	}

}
