package ca.bart.a1730106.missilecommand;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

public class Explosion
{
    public static List<Explosion> explosionList;

    float curX, curY, r, maxR, speed;
    boolean isGrown;

    Explosion(float x, float y, float maxR, float speed)
    {
        curX = x;
        curY = y;
        this.maxR = maxR;
        this.speed = speed;
        r = 0;
    }

    public void Update(float deltaTime)
    {
        if(isGrown && r <= 0)
        {
            RemoveExplosion(this);
            return;
        }
        if (r >= maxR)
        {
            isGrown = true;
        }
        if(!isGrown)
            r += speed * deltaTime;
        else
            r -= speed * deltaTime;

        for(int i = 0; i < Missile.enemyMissile.size(); i++)
        {
            Missile curMissile = Missile.enemyMissile.get(i);
            float missileX = curMissile.curX;
            float missileY = curMissile.curY;
            double distancee2 = Math.pow((missileX - curX), 2) + Math.pow((missileY - curY),2);
            if( distancee2 < Math.pow(r, 2))
            {
                Missile.enemyMissile.remove(curMissile);
            }
        }
    }

    public void Draw(Canvas canvas, Paint paint)
    {
        canvas.drawCircle(curX, curY, r, paint);
    }

    public void RemoveExplosion(Explosion explosion)
    {
        explosionList.remove(explosion);
    }


}
