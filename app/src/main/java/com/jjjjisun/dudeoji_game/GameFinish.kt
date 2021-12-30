package com.jjjjisun.dudeoji_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jjjjisun.dudeoji_game.databinding.ActivityGameFinishBinding
import kotlin.system.exitProcess

class GameFinish : AppCompatActivity() {

    lateinit var binding: ActivityGameFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityGameFinishBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //한판 더! 버튼 클릭 - finish액티비티 종료
        binding.ibRestart.setOnClickListener {
            finish()
        }

        //게임 끝! 버튼 클릭 - 모든 액티비티 완전종료
        binding.ibFinish.setOnClickListener {
            finishAffinity()
            System.runFinalization()
            exitProcess(0)
        }

        //GameStage 점수 받아오기
        if (intent.hasExtra("score")) {
            binding.tvEndScore.text = intent.getStringExtra("score")
        } else {
            finish()
        }
    }


    //풀스크린 화면
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}