package io.github.bleoo.numberroll;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView iv_like;
    NumberRollView nrv_like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_like = (ImageView) findViewById(R.id.iv_like);
        nrv_like = (NumberRollView) findViewById(R.id.nrv_like);

        nrv_like.setOnLikeChangedListener(new NumberRollView.OnLikeChangedListener() {
            @Override
            public void OnLikeChanged(final boolean isLiked) {
                ObjectAnimator animator1a = ObjectAnimator.ofFloat(iv_like, "scaleX", 1f, 0.75f);
                ObjectAnimator animator1b = ObjectAnimator.ofFloat(iv_like, "scaleY", 1f, 0.75f);
                ObjectAnimator animator2a = ObjectAnimator.ofFloat(iv_like, "scaleX", 0.75f, 1f);
                ObjectAnimator animator2b = ObjectAnimator.ofFloat(iv_like, "scaleY", 0.75f, 1f);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(animator1a).before(animator2a);
                animatorSet.playTogether(animator1a, animator1b);
                animatorSet.playTogether(animator2a, animator2b);

                animator1a.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        iv_like.setImageResource(isLiked ? R.mipmap.ic_like_checked : R.mipmap.ic_like_normal);
                    }
                });

                animatorSet.setDuration(200);
                animatorSet.start();
            }
        });
    }

    public void set999(View v){
        nrv_like.setNumber(999);
    }

    public void setRandom(View v){
        nrv_like.setNumber((int) (Math.random() * 1000));
    }
}
