package nz.ara.game.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.util.Log;

import com.example.yac0105.mz_kotlin.BR;

import java.io.File;

import nz.ara.game.model.em.constvalue.Const;
import nz.ara.game.model.em.direction.Direction;
import nz.ara.game.model.impl.game.GameImpl;
import nz.ara.game.model.in.point.Point;

/**
 * Created by yac0105 on 18/05/2018.
 */
public class MainViewModel  extends BaseObservable {

    private static final String TAG = "MainViewModel";

    //context
    private Context context;
    //model
    private GameImpl gameModel;

    // binding spnner levels
    private String[] levels;

    public final ObservableList<Point> wallLeftPointList = new ObservableArrayList<>();

    public final ObservableField<String> wallLeftPointListStr = new ObservableField<>();

    public final ObservableField<String> wallAbovePointListStr = new ObservableField<>();

    public final ObservableField<String> wallSquareStr = new ObservableField<>();

    public final ObservableField<String> thePointStr = new ObservableField<>();

    public final ObservableField<String> minPointStr = new ObservableField<>();

    public final ObservableField<String> heightStr = new ObservableField<>();

    public final ObservableField<String> moveCount = new ObservableField<String>();

    public MainViewModel(String level_string){

        initGameImpl(level_string);
    }

    public MainViewModel(Context context, String level_string){
        this.context = context;

        initGameImpl(level_string);
    }

    public void initGameImpl(String level_string){
        if(gameModel == null){
            gameModel = new GameImpl(level_string, this.context.getFilesDir().getAbsolutePath(),Const.LOAD_BY_STR);
        }else{
            gameModel.reLoad(level_string, Const.LOAD_BY_STR);
        }

        initParas();

    }

    public boolean initGameImplByFile(String level_string){
        try {
            if (gameModel == null) {
                gameModel = new GameImpl(level_string, this.context.getFilesDir().getAbsolutePath(), Const.LOAD_BY_FILE);
            } else {
                gameModel.reLoad(level_string, Const.LOAD_BY_FILE);
            }

            initParas();

            return true;

        }catch (Exception e){
            return false;
        }

    }

    private void initParas(){
        wallLeftPointListStr.set(this.gameModel.getMazeBean().getWallLeftPointListStr());
        wallAbovePointListStr.set(this.gameModel.getMazeBean().getWallAbovePointListStr());
        wallSquareStr.set(this.gameModel.getMazeBean().getWallSquareStr());
        thePointStr.set(gameModel.getTheseus().getPosition().across()
                + "," + gameModel.getTheseus().getPosition().down());

        minPointStr.set(gameModel.getMinotaur().getPosition().across()
                + "," + gameModel.getMinotaur().getPosition().down());

        moveCount.set(String.valueOf(gameModel.getMoveCount()));
    }

    public boolean moveThe(int rolePointXShort,int rolePointXLong,int rolePointYShort,int rolePointYLong,
                      float startX,float startY){

        boolean isTheMove = false;

        //left
        if((startX>0 && startX<rolePointXShort) && (startY>rolePointYShort && startY<rolePointYLong)){

            this.gameModel.moveTheseus(Direction.LEFT);

            isTheMove = true;
        }
        //right
        else if((startX>rolePointXLong) && (startY>rolePointYShort && startY<rolePointYLong)){

            this.gameModel.moveTheseus(Direction.RIGHT);
            isTheMove = true;

        }
        //up
        else if((startX>rolePointXShort && startX<rolePointXLong) && (startY>0 && startY<rolePointYShort)){

            this.gameModel.moveTheseus(Direction.UP);
            isTheMove = true;

        }
        //down
        else if((startX>rolePointXShort && startX<rolePointXLong) && (startY>rolePointYLong)){

            this.gameModel.moveTheseus(Direction.DOWN);
            isTheMove = true;

        }else{
            Log.d(TAG, "No Result for rolePointXShort:" + rolePointXShort + ",rolePointXLong: " + rolePointXLong + ",rolePointYShort:" + rolePointYShort
                    +",rolePointYLong: " + rolePointYLong +",startX:"+ startX +",startY:" + startY);
        }

        initParas();

        return isTheMove;

    }

    public boolean moveMin(){

        boolean couldMove = (this.gameModel.shouldMoveMin() && !this.gameModel.getMinotaur().isHasEaten());

        if(couldMove){
            this.gameModel.moveMinotaur();
        }

        initParas();

        return couldMove;
    }

    public boolean save(File directory){

        File file = new File(directory, Const.LEVEL_FILE_NAME.getValue());

        this.gameModel.setFilePath(directory.getAbsolutePath());

        return this.gameModel.save();

    }

   @Bindable
    public String[] getLevels() {
        levels = new GameImpl(1, Const.LOAD_BY_STR).getLevels();
        return levels;
    }

    public void setLevels(String[] levels) {
        this.levels = levels;
        notifyPropertyChanged(BR.levels);
    }


    public GameImpl getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameImpl gameModel) {
        this.gameModel = gameModel;
    }

}
