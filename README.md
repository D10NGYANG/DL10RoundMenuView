# DL10RoundMenuView
Android 圆形遥控器按钮盘 带中间按钮

[![](https://jitpack.io/v/D10NGYANG/DL10RoundMenuView.svg)](https://jitpack.io/#D10NGYANG/DL10RoundMenuView)

链接：https://blog.csdn.net/sinat_38184748/article/details/89182372

# 效果图
![](/img/sc_1.png)
![](/img/sc_2.png)
![](/img/sc_3.png)
![](/img/sc_4.png)
![](/img/sc_5.png)
# 使用说明
## 注意说明
**版本1.0.7已改成androidx+kotlin代码，如有appcompat-v7的需求，请使用1.0.6版本。**
## 添加依赖
Step 1. Add the JitPack repository to your build file 

```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency

```java
	dependencies {
	        implementation 'com.github.D10NGYANG:DL10RoundMenuView:1.0.9'
	}
```
## 在布局中使用

```java
<com.dlong.rep.dlroundmenuview.DLRoundMenuView
        android:id="@+id/dl_rmv"
        android:layout_width="200dp"
        android:layout_height="200dp"
        app:RMCoreMenuDrawable="@mipmap/circle"
        app:RMRoundMenuDrawable="@mipmap/go"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```
## 参数设定
### 设定是否有中间按钮
布局中
```java
app:RMHasCoreMenu="true"
```
代码中

```java
dlRoundMenuView.setHasCoreMenu(true);
```
### 设定正常情况下的中间按钮的背景颜色
布局中
```java
app:RMCoreMenuNormalBackgroundColor="#ffffffff"
```
代码中

```java
dlRoundMenuView.setCoreMenuNormalBackgroundColor(0xffffffff);
```
### 设定中间按钮的圆圈描边颜色
布局中
```java
app:RMCoreMenuStrokeColor="#cc999999"
```
代码中

```java
dlRoundMenuView.setCoreMenuStrokeColor(0xcc999999);
```
### 设定中间按钮的圆圈描边边框大小
布局中
```java
app:RMCoreMenuStrokeSize="1dp"
```
代码中

```java
dlRoundMenuView.setCoreMenuStrokeSize(2.0f);
```
### 设定中间按钮被点击时的背景颜色
布局中
```java
app:RMCoreMenuSelectedBackgroundColor="#cc999999"
```
代码中

```java
dlRoundMenuView.setCoreMenuSelectedBackgroundColor(0xcc999999);
```
### 设定中心按钮图片
布局中
```java
app:RMCoreMenuDrawable="@mipmap/circle"
```
代码中

```java
dlRoundMenuView.setCoreMenuDrawable(mContext.getResources().getDrawable(R.mipmap.circle));
```
### 设定中心按钮的圆形半径
布局中
```java
app:RMCoreMenuRoundRadius="50dp"
```
代码中

```java
dlRoundMenuView.setCoreMenuRoundRadius(50f);
```
### 设定菜单数量
布局中
```java
app:RMRoundMenuNumber="4"
```
代码中

```java
dlRoundMenuView.setRoundMenuNumber(4);
```
### 设定菜单偏移角度
布局中
```java
app:RMRoundMenuDeviationDegree="45"
```
代码中

```java
dlRoundMenuView.setRoundMenuDeviationDegree(45f);
```
### 设定菜单图片
统一图片即可，程序里有旋转处理
布局中
```java
app:RMRoundMenuDrawable="@mipmap/go"
```
代码中
可单独设定每个位置的图片，0代表位置0，如果是4个菜单的话就是正上方的位置。
```java
dlRoundMenuView.setRoundMenuDrawable(0,mContext.getResources().getDrawable(R.mipmap.go));
```
### 设定是否画每个菜单扇形到中心点的直线
布局中
```java
app:RMIsDrawLineToCenter="false"
```
代码中

```java
dlRoundMenuView.setIsDrawLineToCenter(false);
```
### 设定是否画每个菜单扇形到中心点的直线
布局中
```java
app:RMIsDrawLineToCenter="false"
```
代码中

```java
dlRoundMenuView.setIsDrawLineToCenter(false);
```
### 设定菜单正常背景颜色
布局中
```java
app:RMRoundMenuNormalBackgroundColor="@color/white"
```
代码中

```java
dlRoundMenuView.setRoundMenuNormalBackgroundColor(0xffffffff);
```
### 设定菜单点击背景颜色
布局中
```java
app:RMRoundMenuSelectedBackgroundColor="#cc999999"
```
代码中
```java
dlRoundMenuView.setRoundMenuSelectedBackgroundColor(0xcc999999);
```
### 设定菜单描边颜色
布局中
```java
app:RMRoundMenuStrokeColor="#cc999999"
```
代码中
```java
dlRoundMenuView.setRoundMenuStrokeColor(0xcc999999);
```
### 设定菜单描边宽度
布局中
```java
app:RMRoundMenuStrokeSize="1dp"
```
代码中
```java
dlRoundMenuView.setRoundMenuStrokeSize(2f);
```
### 菜单图片与中心点的距离
布局中
```java
app:RMRoundMenuDistance="80%"
```
代码中
```java
dlRoundMenuView.setRoundMenuDistance(0.8f);
```
## 使用代码

```kotlin
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
```

# 混淆规则
```kotlin
-keep class com.dlong.rep.dlroundmenuview.** {*;}
-dontwarn com.dlong.rep.dlroundmenuview.**
```
