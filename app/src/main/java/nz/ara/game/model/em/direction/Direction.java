/**
 * This file is created by Yang(James) CHEN.
 */
package nz.ara.game.model.em.direction;

/**
 * @author yac0105
 *
 */
public enum Direction {

	PAUSE(0,0),UP(0,-1), LEFT(-1,0), RIGHT(1,0), DOWN(0,1);
	
    private int across;
	
	private int down;
	
	private Direction(int across, int down) {
		this.across = across;
		this.down = down;
	}

	public int getAcross() {
		return across;
	}

	public void setAcross(int across) {
		this.across = across;
	}

	public int getDown() {
		return down;
	}

	public void setDown(int down) {
		this.down = down;
	}
	
	
	
}
