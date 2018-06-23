/**
 * 
 */
package nz.ara.game.model.in.saver;

import nz.ara.game.model.in.saveable.Saveable;

/**
 * @author yac0105
 *
 */
public interface Saver {
	void save(Saveable savable);
	
	void save(Saveable savable, String fileName);
	
	void save(Saveable savable, String fileName, String levelName);
}
