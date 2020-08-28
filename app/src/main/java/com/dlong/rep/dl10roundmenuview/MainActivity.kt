package com.dlong.rep.dl10roundmenuview

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dlong.rep.dl10roundmenuview.databinding.ActivityMainBinding
import com.dlong.rep.dlroundmenuview.Interface.OnMenuClickListener
import com.dlong.rep.dlroundmenuview.Interface.OnMenuLongClickListener
import com.dlong.rep.dlroundmenuview.Interface.OnMenuTouchListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 单击
        binding.dlRmv.setOnMenuClickListener(object : OnMenuClickListener {
            override fun OnMenuClick(position: Int) {
                //Toast.makeText(mContext, "点击了："+position,Toast.LENGTH_SHORT).show();
                Log.i("单击", "点击了：$position")
            }
        })

        // 长按
        binding.dlRmv.setOnMenuLongClickListener(object : OnMenuLongClickListener{
            override fun OnMenuLongClick(position: Int) {
                Log.i("长按", "点击了：$position")
            }
        })

        // 触摸
        binding.dlRmv.setOnMenuTouchListener(object : OnMenuTouchListener {
            override fun OnTouch(event: MotionEvent?, position: Int) {
                Log.v("触摸", "事件=${event.toString()}")
                Log.d("触摸", "位置=$position")
            }
        })

        // 统一lambda接口
        binding.dlRmv.setOnMenuListener {
            onMenuClick { position ->
                // 单击
                Log.i("lambda 单击", "点击了：$position")
            }

            onMenuLongClick { position ->
                // 长按
                Log.i("lambda 长按", "点击了：$position")
            }

            onTouch { event, position ->
                // 触摸
                Log.v("lambda 触摸", "事件=${event.toString()}")
                Log.d("lambda 触摸", "位置=$position")
            }
        }
    }
}