package com.zyc.doctor.ui;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * @author dundun
 */
public abstract class AnimFinishAdapter implements AnimationListener {

	@Override
	public void onAnimationEnd(Animation animation) {
		end();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	public abstract void end();
}
