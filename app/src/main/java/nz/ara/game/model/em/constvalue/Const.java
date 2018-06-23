/**
 * 
 */
package nz.ara.game.model.em.constvalue;

/**
 * @author yac0105
 *
 */
public enum Const {

	UP("UP"),
	LEFT("LEFT"),
	FILE_PATH("H:\\FILE282\\"),
	LEVEL_FILE_NAME("Layout.xlsx"),
	
	STATUS_PLAY(""),
	STATUS_WIN(""),
	STATUS_EATEN(""),
	STATUS_ERROR(""),
	
	LOAD_BY_FILE(""),
	LOAD_BY_STR(""),
	//it is a hard job
	LEVEL_ONE("B=3,3;U=xxxo,oxox,oxox,xxxo;L=xxxo,oooo,oxoo,xoxo;M=1,0;T=1,2;E=3,1:"),
	//LEVEL_TWO("B=3,3;U=oxxxo,xoxoo,xoxoo,oxxxo;L=oooo,xoxo,oxoo,oooo,xxxo;M=2,0;T=2,2;E=0,1:"),
	LEVEL_TWO("B=3,3;U=xxxxxxxo,oooooooo,oxoooooo,oooooxoo,xoxxxxxo,oooooooo;L=xxxxoo,oxooxo,oxooxo,oooooo,oooooo,ooxooo,ooxooo,xxxxoo;M=0,1;T=2,1;E=1,4:"),
	LEVEL_THREE("B=3,3;U=oxxxo,ooxox,oooox,oxooo,oxxxo;L=ooooo,xxxxo,ooxoo,ooooo,xoxxo;M=2,0;T=2,1;E=4,1:"),
	LEVEL_FOUR("B=3,3;U=ooooooo,oxxxoxo,ooooxoo,ooxoooo,ooxxooo,ooooxoo,oxxxxxo;L=ooooooo,oxxxxxo,oxooooo,ooxoooo,xooxxoo,xoxoxoo,oxxxxxo;M=5,2;T=2,1;E=3,0:"),
	LEVEL_FIVE("B=3,3;U=oooooooo,xxxxxoxo,oooooxoo,oxoooxoo,ooxooxoo,ooxxxooo,xxxxxxxo;L=oxxxxxo,ooxoooo,ooooxoo,oooxooo,oxxoxoo,xoooxoo,xxoxooo,oxxxxxo;M=2,4;T=2,1;E=5,0:"),
	LEVEL_SIX("B=3,3;U=oooooooo,oxxxxoxo,oxoooooo,ooxooxoo,oooxoxoo,oooxoooo,oooooooo,oxxxxxxo;L=oooooooo,oxxxxxxo,oooxxxoo,ooxxoxoo,oooxoooo,xoxoxooo,xxxooooo,oxxxxxxo;M=5,3;T=1,1;E=5,0:"),
	LEVEL_SEVEN("B=3,3;U=oooooooo,oxoxxxxo,ooxooxoo,oooooxoo,oooooooo,oxoooxoo,oooxoooo,oxxxxxxo;L=oooooooo,oxxxxxxo,xoooxooo,xooooooo,ooxxoooo,ooooxooo,oxoxxxoo,oxxxxxxo;M=1,5;T=5,5;E=2,0:"),
	LEVEL_EIGHT("B=3,3;U=oxxxxxxxxxo,oxxoooooxoo,oooxooooxoo,ooxooxxxooo,oxooxooxxxo,oooxoxxxxoo,ooxoxooxxxo,ooooxoxxxoo,oxoxxxxxxxo,ooooooooooo;L=oooooooooo,xxxxxxxxoo,ooxoxoxxxo,oxoxxxxoxo,oxxxoooooo,xxoooxxooo,oxxxxoxooo,xxoooooxoo,ooxooooooo,oxoooooooo,xxxxxxxxoo;M=3,2;T=1,0;E=2,8:"),
	LEVEL_NINE("B=3,3;U=ooooooooooo,oxxxoxxxxxo,oxxoooxoooo,ooooooooooo,ooooxoooooo,ooxoooooooo,ooooooooxoo,ooooooooooo,ooooxoooooo,oxxxxxxxxxo;L=oooooooooo,oxxxxxxxxo,ooooxxxooo,oooooooooo,xoooxooooo,xoooxooooo,oooooooooo,oooooooooo,oooooooooo,ooooxxxxoo,oxxxxxxxxo;M=1,1;T=9,1;E=4,0:"),
	LEVEL_TEN("B=3,3;U=oxxxxxxxxxo,oxoooooxoxo,ooooooooooo,ooooooooooo,oooxoooxooo,ooooooooooo,ooxoooooxoo,ooxoooxoooo,ooxxxxxxxxo;L=ooooooooo,ooooooooo,oxxxxxxxo,xooooxooo,oooooxxoo,ooooxoooo,oooxooxoo,ooxooooxo,oooooxooo,oxooooooo,xxxxxxxxo;M=2,0;T=3,1;E=1,0:")
	
	;
	
	private String value;

	private Const(String value){
        this.value=value;
    }

	public String getValue(){
        return value;
    }

}
