/**
 * 
 */
package nz.ara.game.model.bean.maze;

import java.util.ArrayList;
import java.util.List;

import nz.ara.game.model.in.point.Point;

/**
 * @author yac0105
 *
 */
public class MazeBean {

	private int level;
	
	private int widthAcross;
	
	private int depthDown;
	
	private List<Point> wallLeftPointList = new ArrayList<Point>();

	private String wallLeftPointListStr;

	private String wallAbovePointListStr;

	private String wallSquareStr;

	private List<Point> wallAbovePointList = new ArrayList<Point>();
	
    private Point theStartPoint;
	
	private Point minStartPoint;
	
	private Point exitPoint;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getWidthAcross() {
		return widthAcross;
	}

	public void setWidthAcross(int widthAcross) {
		this.widthAcross = widthAcross;
	}

	public int getDepthDown() {
		return depthDown;
	}

	public void setDepthDown(int depthDown) {
		this.depthDown = depthDown;
	}

	public List<Point> getWallLeftPointList() {
		return wallLeftPointList;
	}

	public void setWallLeftPointList(List<Point> wallLeftPointList) {
		this.wallLeftPointList = wallLeftPointList;
	}

	public List<Point> getWallAbovePointList() {
		return wallAbovePointList;
	}

	public void setWallAbovePointList(List<Point> wallAbovePointList) {
		this.wallAbovePointList = wallAbovePointList;
	}

	public Point getExitPoint() {
		return exitPoint;
	}

	public void setExitPoint(Point exitPoint) {
		this.exitPoint = exitPoint;
	}

	public Point getTheStartPoint() {
		return theStartPoint;
	}

	public void setTheStartPoint(Point theStartPoint) {
		this.theStartPoint = theStartPoint;
	}

	public Point getMinStartPoint() {
		return minStartPoint;
	}

	public void setMinStartPoint(Point minStartPoint) {
		this.minStartPoint = minStartPoint;
	}

	public String getWallLeftPointListStr() {
		return wallLeftPointListStr;
	}

	public void setWallLeftPointListStr(String wallLeftPointListStr) {
		this.wallLeftPointListStr = wallLeftPointListStr;
	}

	public String getWallAbovePointListStr() {
		return wallAbovePointListStr;
	}

	public void setWallAbovePointListStr(String wallAbovePointListStr) {
		this.wallAbovePointListStr = wallAbovePointListStr;
	}

	public String getWallSquareStr() {
		return wallSquareStr;
	}

	public void setWallSquareStr(String wallSquareStr) {
		this.wallSquareStr = wallSquareStr;
	}

}
