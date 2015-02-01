package com.example.propertyanimsecdemo;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";

	private ValueAnimator valueAnimator;
	
	private ImageView flower;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);				
		
		flower = (ImageView) findViewById(R.id.flower);
		final Button button = (Button) findViewById(R.id.button1);		
		button.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				valueAnimator.start();
				//第二个属性动画效果，逐渐变大
				ObjectAnimator anim1 = ObjectAnimator.ofFloat(flower, "scaleX",
						1.0f, 2f);
				ObjectAnimator anim2 = ObjectAnimator.ofFloat(flower, "scaleY",
						1.0f, 2f);
				AnimatorSet animSet = new AnimatorSet();
				animSet.play(anim1).with(anim2);
				animSet.setDuration(2000);
				animSet.start();
				
				
			}
		});		
		
		
		valueAnimator = ValueAnimator.ofObject(new BezierEvaluator(),new PointF(324,893), new PointF(270,193));
		valueAnimator.setDuration(2000);		
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {			
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
                //在次对目标view做修改
				PointF pointF = (PointF)animation.getAnimatedValue();
				flower.setX(pointF.x-flower.getWidth()/2);
				flower.setY(pointF.y-flower.getHeight()/2);
			}
		});
//		valueAnimator.setTarget(flower);  该方法目前不知道有何作用
		valueAnimator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				Log.i(TAG, "onAnimationStart");
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				Log.i(TAG, "onAnimationRepeat");
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				Log.i(TAG, "onAnimationEnd");
				
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				Log.i(TAG, "onAnimationCancel");
				
			}
		});
		valueAnimator.setRepeatMode(ValueAnimator.RESTART);
		
	}

		
	class BezierEvaluator implements TypeEvaluator<PointF>{

		@Override
		public PointF evaluate(float fraction, PointF startValue,
				PointF endValue) {
			final float t = fraction;	
			float oneMinusT = 1.0f - t;
			PointF point = new PointF();	//返回计算好的点
			
			PointF point0 = (PointF)startValue;	//开始起点
			
			PointF point1 = new PointF();	//贝塞尔曲线控制点
			point1.set(480, 30);
			
			PointF point3 = (PointF)endValue;	//结束终点
			
			//B0(t) = (1-t)2P0 + 2(1-t)tC1 + t2P1    (0 ≤ t ≤ 1) 根据二次贝塞尔曲线方程计算，可在网上查找该方程公式，根据需求更改
			
			point.x = oneMinusT*oneMinusT*(point0.x)+2*oneMinusT*t*(point1.x)+t*t*(point3.x);
			point.y = oneMinusT*oneMinusT*(point0.y)+2*oneMinusT*t*(point1.y)+t*t*(point3.y);
			return point;
		}	
	}
}
