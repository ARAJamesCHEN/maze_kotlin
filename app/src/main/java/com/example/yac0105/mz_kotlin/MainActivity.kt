package com.example.yac0105.mz_kotlin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.graphics.*
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ImageView
import com.example.yac0105.mz_kotlin.R.id.mapview
import com.example.yac0105.mz_kotlin.R.id.theview
import com.example.yac0105.mz_kotlin.R.id.minview
import com.example.yac0105.mz_kotlin.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import nz.ara.game.model.em.constvalue.Const
import nz.ara.game.util.DisplayParams
import nz.ara.game.util.DisplayUtil
import nz.ara.game.viewmodel.MainViewModel
import java.io.File


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    var level_string: String = "Level-1"
    var mainViewModel: MainViewModel? = null
    var context: Context? = null;


    private var itemsWallLeftStr: String? = null

    private var itemsWallAboveStr: String? = null

    private var thePointStr: String? = null

    private var minPointStr: String? = null

    private var drawPaint: Paint? = null

    internal var canvas: Canvas? = null

    private var mHeight = 100
    private var mWidth = 100

    private var stepWidthX = 100

    private var stepWidthY = 100

    private var startPointX = 100

    private var startPointY = 200


    var rolePointXShort = 100

    var rolePointXLong = 100

    var rolePointYShort = 200

    var rolePointYLong = 200

    var startX: Float = 0.toFloat()

    var startY: Float = 0.toFloat()

    private var wallSquareStr: String? = null

    var mapView1: ImageView? = null

    var theView: ImageView? = null

    var minView: ImageView? = null

    private var directory: File? = null

    private var fileP: String? = null

    private var isSaveSuccessful = false

    private var isLoadSuccessful = false

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        context = this

        mainViewModel = MainViewModel(context, level_string)

        binding.mainViewModel = mainViewModel

        mapView1 = frameLayout.getChildAt(0) as ImageView
        theView = frameLayout.getChildAt(1) as ImageView
        minView = frameLayout.getChildAt(2) as ImageView

        theView!!.setOnTouchListener(View.OnTouchListener { v, event -> roleViewOnTouched(event) })

        button_help.setOnClickListener {
            helpButtonClicked()
        }

        level_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerItemSelected()
            }

        }

        button_reset.setOnClickListener(
                View.OnClickListener { resetButtonClicked() }
        )


        button_pause.setOnClickListener(
                View.OnClickListener { pauseButtonClicked() }
        )

        button_save.setOnClickListener(
                View.OnClickListener { saveButtonClicked() }
        )


        button_new.setOnClickListener(View.OnClickListener { loadByFileButtonClicked() }
        )

        this.drawMapByImageView()

        this.drawRoleByImageView(theView!!, resources.getString(R.string.ROLE_TYPE_THESEUS))
        this.drawRoleByImageView(minView!!, resources.getString(R.string.ROLE_TYPE_MINOTAUR))

    }


    private fun loadByFileButtonClicked() {
        directory = context!!.getFilesDir()

        fileP = directory!!.getAbsolutePath() + File.separator + Const.LEVEL_FILE_NAME.value

        Thread(Runnable {
            isLoadSuccessful = mainViewModel!!.initGameImplByFile(level_string)
            Log.d(TAG, "Load $level_string from $fileP successfully!")
        }).start()

        showLaodFileProgressDialog()


        fileP = directory!!.getAbsolutePath() + File.separator + Const.LEVEL_FILE_NAME.value

    }

    private fun saveButtonClicked() {
        mainViewModel!!.initGameImpl(level_string)

        directory = context!!.getFilesDir()

        fileP = directory!!.getAbsolutePath() + File.separator + Const.LEVEL_FILE_NAME.value

        Thread(Runnable {
            isLoadSuccessful = mainViewModel!!.save(directory)
            Log.d(TAG, "Save to $fileP successfully!")
        }).start()

        showSaveFileProgressDialog()
    }

    private fun pauseButtonClicked() {

        mainViewModel!!.moveMin()
        mainViewModel!!.moveMin()

        if (mainViewModel!!.getGameModel().minotaur.isHasEaten) {

            minView!!.bringToFront()
            playLost()
            minKillTheDialog()
        }

        this.drawRoleByImageView(minView!!, resources.getString(R.string.ROLE_TYPE_MINOTAUR))

    }

    fun spinnerItemSelected() {

        var aNewlevel_string = level_spinner.selectedItem as String

        if (!aNewlevel_string.equals(level_string)) {

            level_string = aNewlevel_string

            if (mainViewModel == null)
                mainViewModel = MainViewModel(context, aNewlevel_string);
            else{
                mainViewModel!!.initGameImpl(aNewlevel_string)
                theView!!.bringToFront()
                this.drawMapByImageView()
                this.drawRoleByImageView(theView!!, resources.getString(R.string.ROLE_TYPE_THESEUS))
                this.drawRoleByImageView(minView!!, resources.getString(R.string.ROLE_TYPE_MINOTAUR))
            }

        }

    }

    private fun setParas() {
        this.thePointStr = mainViewModel!!.thePointStr.get()
        this.itemsWallAboveStr = mainViewModel!!.wallAbovePointListStr.get()
        this.itemsWallLeftStr = mainViewModel!!.wallLeftPointListStr.get()
        this.minPointStr = mainViewModel!!.minPointStr.get()
        this.wallSquareStr = mainViewModel!!.wallSquareStr.get()
    }

    private fun drawRoleByImageView(imageView: ImageView, roleStr: String?) {
        this.setParas()
        //this.calParas();

        val bitmap = Bitmap.createBitmap(mWidth, mWidth,
                Bitmap.Config.ARGB_4444)
        canvas = Canvas(bitmap)

        if (roleStr != null && roleStr == resources.getString(R.string.ROLE_TYPE_THESEUS)) {
            this.drapRole(canvas!!, this.thePointStr, roleStr)
        } else if (roleStr != null && roleStr == resources.getString(R.string.ROLE_TYPE_MINOTAUR)) {
            this.drapRole(canvas!!, this.minPointStr, roleStr)
        } else {
            Log.e(TAG, "Error Type:" + roleStr!!)
        }

        imageView.setImageBitmap(bitmap)
        imageView.invalidate()
    }

    private fun drawMapByAttrs() {
        drawPaint = Paint(Paint.DITHER_FLAG)
        drawPaint!!.setAntiAlias(true)
        drawPaint!!.setColor(Color.BLACK)
        drawPaint!!.setStrokeWidth(5f)
        drawPaint!!.setStyle(Paint.Style.STROKE)
        drawPaint!!.setStrokeJoin(Paint.Join.ROUND)
    }

    private fun drapRole(canvas: Canvas, pointStr: String?, type: String?) {
        if (pointStr != null && pointStr.trim { it <= ' ' }.length > 0) {

            val pointStrArray = pointStr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val pointX = Integer.parseInt(pointStrArray[0])
            val pointY = Integer.parseInt(pointStrArray[1])

            val left = startPointX + pointX * this.stepWidthX - this.stepWidthX / 2 + 5
            val top = startPointY + pointY * this.stepWidthY - this.stepWidthY / 2 + 5
            val right = startPointX + pointX * this.stepWidthX + this.stepWidthX / 2 - 5
            val bottom = startPointY + pointY * this.stepWidthY + this.stepWidthY / 2 - 5


            val rectangle = Rect(left, top, right, bottom)


            var bitmap: Bitmap? = null


            if (type != null && type == resources.getString(R.string.ROLE_TYPE_THESEUS)) {

                rolePointXShort = left
                rolePointYShort = top
                rolePointXLong = right
                rolePointYLong = bottom

                bitmap = BitmapFactory.decodeResource(resources, R.drawable.theseus)

            } else if (type != null && type == resources.getString(R.string.ROLE_TYPE_MINOTAUR)) {
                bitmap = BitmapFactory.decodeResource(resources, R.drawable.minotaur)
            } else {
                Log.e(TAG, "Error Type:" + type!!)
                return
            }

            canvas.drawBitmap(bitmap!!, null, rectangle, null)


        }


    }

    private fun drawMap(canvas: Canvas, wallStr: String?, type: String?) {
        if (wallStr != null && wallStr.trim { it <= ' ' }.length > 0) {
            val wallStrArray = wallStr.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            for (pointStr in wallStrArray) {

                val pointStrArray = pointStr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                val pointX = Integer.parseInt(pointStrArray[0])
                val pointY = Integer.parseInt(pointStrArray[1])
                Log.d(TAG, "Point: $pointX,$pointY")

                val drawPointX = startPointX + pointX * this.stepWidthX - this.stepWidthX / 2
                val drawPointY = startPointY + pointY * this.stepWidthX - this.stepWidthY / 2

                if (type != null && type == resources.getString(R.string.WALL_TYPE_ABOVE)) {
                    canvas.drawLine(drawPointX.toFloat(), drawPointY.toFloat(), (drawPointX + this.stepWidthX).toFloat(), drawPointY.toFloat(), drawPaint)
                } else if (type != null && type == resources.getString(R.string.WALL_TYPE_LEFT)) {
                    canvas.drawLine(drawPointX.toFloat(), drawPointY.toFloat(), drawPointX.toFloat(), (drawPointY + this.stepWidthY).toFloat(), drawPaint)
                } else {
                    Log.e(TAG, "Error Type:" + type!!)
                }

            }

        }
    }

    private fun roleViewOnTouched(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_SCROLL -> {
            }
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y

                if (mainViewModel!!.moveThe(rolePointXShort, rolePointXLong, rolePointYShort, rolePointYLong, startX, startY)) {

                    this.drawRoleByImageView(theView!!, resources.getString(R.string.ROLE_TYPE_THESEUS))

                    if (mainViewModel!!.getGameModel().theseus.isHasWon) {
                        playWin()
                        theWinDialog()
                    }
                }

                if (mainViewModel!!.moveMin()) {
                    if (mainViewModel!!.getGameModel().minotaur.isHasEaten) {
                        minView!!.bringToFront()
                        playLost()
                        minKillTheDialog()
                    }

                    this.drawRoleByImageView(minView!!, resources.getString(R.string.ROLE_TYPE_MINOTAUR))
                }
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> if (mainViewModel!!.moveMin()) {

                if (mainViewModel!!.getGameModel().minotaur.isHasEaten) {

                    minView!!.bringToFront()
                    playLost()
                    minKillTheDialog()
                }

                this.drawRoleByImageView(minView!!, resources.getString(R.string.ROLE_TYPE_MINOTAUR))

            }
            else -> return false
        }

        Log.d(TAG, "Touch Event::" + event.action)
        return true
    }

    private fun showLaodFileProgressDialog() {
        val MAX_PROGRESS = 100
        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.progress = 0
        progressDialog.setTitle("Loading")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.max = MAX_PROGRESS
        progressDialog.show()

        Thread(Runnable {
            var progress = 0

            while (progress < MAX_PROGRESS) {
                try {
                    Thread.sleep(100)
                    if (!isLoadSuccessful) {
                        progress++
                        progressDialog.progress = progress
                    } else {
                        progressDialog.progress = MAX_PROGRESS
                        progressDialog.cancel()
                        if (isLoadSuccessful) {
                            isLoadSuccessful = false
                        }

                        drawRoleByImageView(theView!!, resources.getString(R.string.ROLE_TYPE_THESEUS))
                        drawRoleByImageView(minView!!, resources.getString(R.string.ROLE_TYPE_MINOTAUR))
                    }

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
            progressDialog.cancel()
        }).start()

    }

    private fun showSaveFileProgressDialog() {
        val MAX_PROGRESS = 100
        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.progress = 0
        progressDialog.setTitle("Saving")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.max = MAX_PROGRESS
        progressDialog.show()

        Thread(Runnable {
            var progress = 0

            while (progress < MAX_PROGRESS) {
                try {
                    Thread.sleep(100)
                    if (!isSaveSuccessful) {
                        progress++
                        progressDialog.progress = progress
                    } else {
                        progressDialog.progress = MAX_PROGRESS
                        progressDialog.cancel()
                        if (isSaveSuccessful) {
                            isSaveSuccessful = false
                        }
                    }

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
            progressDialog.cancel()
        }).start()

    }

    private fun minKillTheDialog() {

        val minKillTheDialog = AlertDialog.Builder(this)

        minKillTheDialog.setTitle("Minotaur killed Theseus!")

        minKillTheDialog.setMessage("Game Over")

        minKillTheDialog.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
            minKillTheOptionDialog()
        }
        minKillTheDialog.show()
    }

    private fun minKillTheOptionDialog() {
        val theDialog = AlertDialog.Builder(this)

        theDialog.setTitle("Do you like to play these level again?")

        theDialog.setMessage("")

        theDialog.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
            resetButtonClicked()
        }

        theDialog.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
            level_string = "Level-1"
            resetButtonClicked()
            level_spinner.setSelection(0)
        }
        theDialog.show()
    }

    fun helpButtonClicked() {
        helpButtonDialog()
    }

    fun helpButtonDialog() {
        val minKillTheDialog = AlertDialog.Builder(this)

        minKillTheDialog.setTitle("HELP")

        minKillTheDialog.setMessage("As Theseus, you must escape the Minotaur's maze!\n" +
                "\n" +
                "For every move you make, the Minotaur makes two moves. Luckily, he isn't terribly bright. He will move toward Theseus, favoring horizontal over vertical moves, without knowing how to get around a wall in his way. Escape by luring the Minotaur into a place where he gets stuck!\n" +
                "\n" +
                "Code: Yang CHEN 99168512")

        minKillTheDialog.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        minKillTheDialog.show()
    }

    fun resetButtonClicked() {
        mainViewModel!!.initGameImpl(level_string)
        theView!!.bringToFront()
        this.drawMapByImageView()
        this.drawRoleByImageView(theView!!, resources.getString(R.string.ROLE_TYPE_THESEUS))
        this.drawRoleByImageView(minView!!, resources.getString(R.string.ROLE_TYPE_MINOTAUR))
    }


    private fun calParas() {

        var countX = 4
        var countY = 4

        if (wallSquareStr != null && wallSquareStr!!.trim { it <= ' ' }.length > 0) {
            Log.d(TAG, "Wall suare:" + wallSquareStr)

            val wallSqurArray = wallSquareStr!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            countX = Integer.parseInt(wallSqurArray[0])
            countY = Integer.parseInt(wallSqurArray[1])

        }

        val currentDisplay = windowManager.defaultDisplay


        val displayParams = DisplayParams.getInstance(context)
        mWidth = DisplayUtil.dip2px(342F, displayParams.scale)//currentDisplay.getWidth();
        mHeight = DisplayUtil.dip2px(342F, displayParams.scale)//currentDisplay.getHeight();

        this.stepWidthX = mWidth / countX
        this.stepWidthY = mHeight / countY

        this.startPointX = this.stepWidthX

        val hTx = textView_move_name.getMeasuredHeight()
        this.startPointY = hTx + 12 + this.stepWidthY / 2
    }

    private fun drawMapByImageView() {
        this.drawMapByAttrs()
        this.setParas()
        this.calParas()
        val bitmap = Bitmap.createBitmap(mWidth, mWidth.toInt(),
                Bitmap.Config.ARGB_4444)
        canvas = Canvas(bitmap)

        this.drawMap(canvas!!, this.itemsWallAboveStr, resources.getString(R.string.WALL_TYPE_ABOVE))
        this.drawMap(canvas!!, this.itemsWallLeftStr, resources.getString(R.string.WALL_TYPE_LEFT))

        mapView1!!.setImageBitmap(bitmap)

        mapView1!!.invalidate()
    }

    private fun theWinDialog() {

        val minKillTheDialog = AlertDialog.Builder(this)

        minKillTheDialog.setTitle("Theseus win!")

        minKillTheDialog.setMessage("Congratulations~")

        minKillTheDialog.setPositiveButton("OK"
        ) { dialog, which ->
            dialog.dismiss()
            theWinOptionDialog()
        }
        minKillTheDialog.show()
    }

    private fun theWinOptionDialog() {
        val theDialog = AlertDialog.Builder(this)

        theDialog.setTitle("Do you like to play these level again?")

        theDialog.setMessage("")

        theDialog.setPositiveButton("OK", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
                resetButtonClicked()
            }
        })

        theDialog.setNegativeButton("Next Level", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
                val theNum = mainViewModel!!.getGameModel().getLevelByLevelStr(level_string)
                level_string = mainViewModel!!.getGameModel().levels[theNum]
                resetButtonClicked()
                level_spinner.setSelection(theNum)
            }
        })
        theDialog.show()
    }

    fun playWin() {

        val mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.you_win_sound_effect)
        mediaPlayer.start()
    }

    fun playLost() {

        val mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.game_over_sound_effect)
        mediaPlayer.start()
    }
}
