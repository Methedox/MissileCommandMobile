package ca.bart.a1730106.missilecommand;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.lang.annotation.Target;
import java.util.List;

public class Missile
{

    public static List<Missile> enemyMissile;
    public static List<Missile> playerMissile;

    float startX, startY, endX, endY, curX, curY, lengthperc = 0;
    float speed = 0;
    boolean isplayer;

    Missile(float sX, float sY, float eX, float eY, float speed, boolean isplayer)
    {
        this.speed = speed;
        startX = sX;
        startY = sY;
        endX = eX;
        endY = eY;
        lengthperc = 0;
        curX = startX;
        curY = startY;
        this.isplayer = isplayer;
    }

    public void Update(float deltaTime)
    {
        if (lengthperc >= 100)
        {
            if(!this.isplayer)
            {
                MainActivity.currentHealth -= 5;
            }
            DestroyMissile(this);
            return;
        }
        lengthperc += speed * deltaTime;

        curX = startX + (lengthperc / 100) * (endX - startX);
        curY = startY + (lengthperc / 100) * (endY - startY);
    }

    public void Draw(Canvas canvas, Paint paint)
    {
        canvas.drawLine(startX, startY, curX, curY, paint);
    }

    public void DestroyMissile(Missile missile)
    {
        if (!isplayer)
        {
            enemyMissile.remove(missile);
        } else
        {
            Explosion explosion = new Explosion(missile.endX, missile.endY, 5, 5f);
            Explosion.explosionList.add(explosion);
            playerMissile.remove(missile);
        }
    }

    public static void CreateMissile(float sx, float sy, float ex, float ey, float speed, boolean isplayer)
    {
        Missile missile = new Missile(sx, sy, ex, ey, speed, isplayer);
        if (isplayer)
        {
            playerMissile.add(missile);
        } else
        {
            enemyMissile.add(missile);
        }

    }
}
