
package com.jjjjisun.dudeoji_game

import android.content.Intent
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import com.jjjjisun.dudeoji_game.databinding.ActivityGameStageBinding
import java.util.*
import kotlin.collections.ArrayList


class GameStage : AppCompatActivity() {

    lateinit var binding: ActivityGameStageBinding

    var score = 0
    var imageArray = ArrayList<ImageButton>()
    var handler: Handler = Handler()
    var runnable: Runnable = Runnable { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameStageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        score = 0

        //이미지 배열에 저장
        imageArray = arrayListOf(
            binding.ibRabbit1, binding.ibRabbit2,
            binding.ibRabbit3, binding.ibRabbit4,
            binding.ibRabbit5, binding.ibRabbit6
        )

        hindImages()

        //스코어 텍스트 넘기기
        val intent = Intent(this, GameFinish::class.java)

        //타이머 (총 30초를 1초씩 내려오기)
        object : CountDownTimer(30000, 1000) {
            override fun onTick(p0: Long) {
                binding.tvGameTime.text = "${p0 / 1000}"
            }

            //타임이 끝났을 때
            override fun onFinish() {
                binding.tvGameTime.text = "TIME OVER"
                handler.removeCallbacks(runnable)
                for (image in imageArray) image.visibility = View.INVISIBLE

                //finish액티비티 불러오기
                startActivity(Intent(this@GameStage, GameFinish::class.java))

                //스코어 텍스트 넘기기기
                intent.putExtra("score", binding.tvGameScore.text.toString())
                startActivity(intent)
            }
        }.start()
    }

    //이미지 보이고 사라지게 하기
    fun hindImages() {
        runnable = object : Runnable {
            override fun run() {
                for (image in imageArray) {
                    image.visibility = View.INVISIBLE
                }

                //랜덤으로 배열의 이미지 불러오기(index값 5 ~ 0까지)
                val random = Random()
                val index = random.nextInt(5 - 0)
                imageArray[index].visibility = View.VISIBLE

                //이미지를 클릭했을 때 increaseScore함수 불러오기
                imageArray[index].onThrottleClick {
                    increaseScore(imageArray[index])
                }

                //이미지 딜레이 속도
                handler.postDelayed(runnable, 500)
            }
        }
        handler.post(runnable)
    }

    //스코어 카운트
    fun increaseScore(view: View) {
        score += 10
        binding.tvGameScore.text = "$score"
    }

    //중복 클릭 방지
    fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }

    //중복 클릭 방지
    class OnThrottleClickListener(
        private val clickListener: View.OnClickListener,
        private val interval: Long = 300
    ) :
        View.OnClickListener {

        private var clickable = true

        override fun onClick(v: View?) {
            if (clickable) {
                clickable = false
                v?.run {
                    postDelayed({
                        clickable = true
                    }, interval)
                    clickListener.onClick(v)
                }
            } else {
                Log.d("msg", "waiting for a while")
            }
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

    override fun onPause() {
        super.onPause()
        binding.ibPause.setImageResource(R.drawable.start)

    }

    override fun onRestart() {
        super.onRestart()
        //액티비티 재시작
        recreate()
//        finish()
//        startActivity(intent)
    }

}