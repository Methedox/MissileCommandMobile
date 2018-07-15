package ca.bart.a1730106.missilecommand;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View
{
    int cx, cy, r;
    float scale;
    Paint background;
    Paint playerPaint;
    Paint hpFullPaint;
    Paint curHPPaint;
    Paint explosionPaint;
    Paint enemyMissile;
    int touchId = -1;

    public GameView(Context context)
    {
        this(context, null, 0, 0);
    }

    public GameView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        this(context, attrs, defStyleAttr, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);

        background = new Paint();
        hpFullPaint = new Paint();
        curHPPaint = new Paint();
        playerPaint = new Paint();
        explosionPaint = new Paint();
        enemyMissile = new Paint();

        int color = Color.RED;
        if (attrs != null)
        {
            TypedArray values = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GameView, 0, 0);
            if (values != null)
            {
                try
                {
                    color = values.getColor(R.styleable.GameView_color, Color.RED);
                } finally
                {
                    values.recycle();
                }
            }
        }


        background.setColor(Color.BLACK);
        background.setStyle(Paint.Style.FILL);
        hpFullPaint.setColor(Color.RED);
        hpFullPaint.setStyle(Paint.Style.FILL);
        explosionPaint.setColor(Color.WHITE);
        explosionPaint.setStyle(Paint.Style.FILL);
        curHPPaint.setColor(Color.GREEN);
        curHPPaint.setStyle(Paint.Style.FILL);
        playerPaint.setColor(Color.BLUE);
        playerPaint.setStyle(Paint.Style.FILL);
        playerPaint.setStrokeWidth(1);
        enemyMissile.setColor(Color.RED);
        enemyMissile.setStyle(Paint.Style.STROKE);
        enemyMissile.setStrokeWidth(1);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(0.0f, cy);
        canvas.scale(scale, -scale);

        //DrawBackground
        {
            canvas.drawRect(0, 0, 100, 200, background);
        }

        // Draw Player Missile
        {
            canvas.save();

            for (int i = 0; i < Missile.playerMissile.size(); i++)
            {
                Missile.playerMissile.get(i).Draw(canvas, playerPaint);
            }

            canvas.restore();
        }

        // Draw Player
        {
            canvas.save();
            canvas.translate(50, 10);
            canvas.drawCircle(0.0f, 0.0f, 10, playerPaint);

            canvas.restore();
        }

        // Draw HP Bar
        {
            canvas.drawRect(0.0f, 0.0f, 100, 10, hpFullPaint);
            canvas.drawRect(0.0f, 0.0f, MainActivity.currentHealth, 10, curHPPaint);
        }

        // Draw Enemy Missiles
        for (int i = 0; i < Missile.enemyMissile.size(); i++)
        {
            Missile.enemyMissile.get(i).Draw(canvas, enemyMissile);
        }

        // Draw Explosion
        for (int i = 0; i < Explosion.explosionList.size(); i++)
        {
            Explosion.explosionList.get(i).Draw(canvas, explosionPaint);
        }

        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        cx = w;
        cy = h;
        scale = w / 100f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        Log.d("TOUCH", "onTouchEvent(" + event + ")");

        int index = event.getActionIndex();
        int id = event.getPointerId(index);

        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (touchId != -1) break;
                touchId = index;
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                    float x = event.getX(index);
                    float y = cy-event.getY(index);
                    Missile.CreateMissile(50, 10, (x-getLeft())/scale, (y-getTop())/scale, 20f, true);
                    touchId = -1;
                    return true;

            case MotionEvent.ACTION_CANCEL:
                touchId = -1;
                break;
        }

        return super.onTouchEvent(event);
    }

}
