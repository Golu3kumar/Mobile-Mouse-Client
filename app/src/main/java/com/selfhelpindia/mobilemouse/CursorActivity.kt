package com.selfhelpindia.mobilemouse

import android.annotation.SuppressLint
import android.os.*
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.selfhelpindia.mobilemouse.databinding.ActivityCursorBinding
import com.selfhelpindia.mobilemouse.networkutils.CommandSender
import android.text.Editable

import android.text.TextUtils
import kotlin.math.sqrt


class CursorActivity : AppCompatActivity() {

    lateinit var binding: ActivityCursorBinding

    var mLastInput: String = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCursorBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ip = intent.getStringExtra("IP_ADDRESS")
//        port = intent.getIntExtra("PORT", -1)
        binding.btnLeftClick.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> Utility.sendCommand(
                    OperationData.OPERATION_CLICK_DOWN,
                    0,
                    0,
                    null
                )
                MotionEvent.ACTION_UP -> Utility.sendCommand(
                    OperationData.OPERATION_CLICK_UP,
                    0,
                    0,
                    null
                )
            }
            true
        }

        binding.btnRightClick.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> Utility.sendCommand(
                    OperationData.OPERATION_RIGHT_CLICK,
                    0,
                    0,
                    null
                )
            }
            true
        }


        binding.editBoardInput.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                Utility.sendCommand(OperationData.OPERATION_DEL_TEXT, 0, 0, null)
            }
            return@setOnKeyListener false
        }

        binding.editBoardInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                Log.i("I don't know", "before: $s")
                mLastInput = s.toString()
            }


            //
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()){
                    Utility.sendCommand(OperationData.OPERATION_DEL_TEXT, 0, 0, null)
                    return
                }
                Log.i(
                    "I don't know",
                    "on: " + s + ", last char: " + s.toString().substring(s.length - 1)
                )
                val lastChar = s.subSequence(s.length - 1, s.length).toString()
                if (!TextUtils.isEmpty(lastChar) && s.length > mLastInput.length) {
                    Utility.sendCommand(OperationData.OPERATION_TYPE_TEXT, 0, 0, lastChar)
                }else{
                    Utility.sendCommand(OperationData.OPERATION_DEL_TEXT, 0, 0, null)
                }
            }

            override fun afterTextChanged(s: Editable) {
                Log.i("I don't know", "after: $s")
            }
        })
        binding.touchBoard.setOnTouchListener { v, event ->
            onBoardTouch(event)
            true
        }

    }


    private var mDownTime: Long = 0
    private var mDownX = 0f
    private var mDownY = 0f

    private var mUpTime: Long = 0
    private var mUpX = 0f
    private var mUpY = 0f

    private var mLastMoveX = Float.MAX_VALUE
    private var mLastMoveY = Float.MAX_VALUE

    private var mCurMoveX = 0f
    private var mCurMoveY = 0f
    private var mLastMoveTime: Long = 0


    private fun onBoardTouch(event: MotionEvent) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> onSingClick(event, true)
            MotionEvent.ACTION_UP -> onSingClick(event, false)
            MotionEvent.ACTION_MOVE -> onMove(event)
        }
    }

    private fun onMove(event: MotionEvent) {
        var distanceX = 0
        var distanceY = 0
        mCurMoveX = event.x
        mCurMoveY = event.y
        if (mLastMoveX != Float.MAX_VALUE && mLastMoveY != Float.MAX_VALUE) {
            distanceX = (mCurMoveX - mDownX).toInt()
            distanceY = (mCurMoveY - mDownY).toInt()
        }
        val distance =
            sqrt((distanceX * distanceX + distanceY * distanceY).toDouble()).toInt()

        // send a move command per 0.5 s
        if (distance > 100 || System.currentTimeMillis() - mLastMoveTime > 100) {
            Utility.sendCommand(OperationData.OPERATION_MOVE, distanceX, distanceY, null)
            mLastMoveX = mCurMoveX
            mLastMoveY = mCurMoveY
            mLastMoveTime = System.currentTimeMillis()
        }
    }

    private fun onSingClick(event: MotionEvent, down: Boolean) {
        if (down) {
            mDownTime = System.currentTimeMillis()
            mDownX = event.x
            mDownY = event.y
        } else {
            mUpTime = System.currentTimeMillis()
            mUpX = event.x
            mUpY = event.y
        }
    }


}