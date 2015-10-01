package isf.proagro.android.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by eddyhugues on 15-05-19.
 */
public class AnimationUtils {

    public static void runEnterAnimation(Context context, View view,int animationTime, int delay) {
        view.setTranslationY(AndroidUtils.getScreenHeight((AppCompatActivity) context));
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(animationTime)
                .setStartDelay(delay)
                .start();
    }

    public static int getLongTimeAnimation(Context context) {
        return context.getResources().getInteger(android.R.integer.config_longAnimTime);
    }

}
