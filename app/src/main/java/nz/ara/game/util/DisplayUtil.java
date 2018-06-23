package nz.ara.game.util;

/**
 * Created by yac0105 on 31/05/2018.
 */

public class DisplayUtil {
    /**
     * px->dip/dp
     * @param pxValue
     * @param scale
     * @return
     */
    public static int px2dip(float pxValue, float scale) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dip/dp->px
     * @param dipValue
     * @param scale
     * @return
     */
    public static int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px->sp
     */
    public static int px2sp(float pxValue, float fontScale) {
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp->px
     * @param spValue
     * @param fontScale
     * @return
     */
    public static int sp2px(float spValue, float fontScale) {
        return (int) (spValue * fontScale + 0.5f);
    }
}
