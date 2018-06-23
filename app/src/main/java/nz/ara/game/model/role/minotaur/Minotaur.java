/**
 * This file is created by Yang(James) CHEN.
 */
package nz.ara.game.model.role.minotaur;

import nz.ara.game.model.role.GameRole;

/**
 * @author yac0105
 *
 */
public class Minotaur extends GameRole {
	private boolean hasEaten = false;

	private int canNotMove = 0;

	public boolean isHasEaten() {
		return hasEaten;
	}

	public void setHasEaten(boolean hasEaten) {
		this.hasEaten = hasEaten;
	}

	public int getCanNotMove() {
		return canNotMove;
	}

	public void setCanNotMove(int canNotMove) {
		this.canNotMove = canNotMove;
	}


}
