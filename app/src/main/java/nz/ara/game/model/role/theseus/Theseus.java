/**
 * This file is created by Yang(James) CHEN.
 */
package nz.ara.game.model.role.theseus;

import nz.ara.game.model.role.GameRole;

/**
 * @author yac0105
 *
 */
public class Theseus extends GameRole {

	private boolean hasWon = false;

	public boolean isHasWon() {
		return hasWon;
	}

	public void setHasWon(boolean hasWon) {
		this.hasWon = hasWon;
	}

}
