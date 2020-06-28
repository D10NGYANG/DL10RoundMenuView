package com.dlong.rep.dl10roundmenuview

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dlong.rep.dl10roundmenuview.databinding.ActivityMainBinding
import com.dlong.rep.dlroundmenuview.Interface.OnMenuClickListener
import com.dlong.rep.dlroundmenuview.Interface.OnMenuTouchListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.dlRmv.setOnMenuClickListener(object : OnMenuClickListener {
            override fun OnMenuClick(position: Int) {
                //Toast.makeText(mContext, "点击了："+position,Toast.LENGTH_SHORT).show();
                Log.e("TAG", "点击了：$position")
            }
        })
        binding.dlRmv.setOnMenuTouchListener(object : OnMenuTouchListener {
            override fun OnTouch(event: MotionEvent?) {
                // 触摸监听
            }
        })
    }
}