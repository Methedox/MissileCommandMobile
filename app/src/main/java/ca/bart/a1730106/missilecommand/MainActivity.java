package ca.bart.a1730106.missilecommand;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity implements Constants
{
    public static GameView gameView;
    public static int currentHealth = 0;
    public static boolean isPaused;
    Random rand;
    long lastUpdate = 0;
    float elapsedTime = 0.0f;

    int randomSX = 0;
    int randomEX = 0;
    boolean gameOver;
    boolean showPause;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Update();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rand = new Random();
        gameView = (GameView) findViewById(R.id.gameView);
        Missile.enemyMissile = new ArrayList<Missile>();
        Missile.playerMissile = new ArrayList<Missile>();
        Explosion.explosionList = new ArrayList<Explosion>();
        currentHealth = 100;
        showPause = false;
        gameOver = false;
        isPaused = true;
        MainMenu("Welcome", "What you want to do?");
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestUpdate();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelUpdate();
    }

    @Override
    protected void onPause()
    {
        if(!gameOver)
        {
            isPaused = true;
            showPause = true;
            //Pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        if (showPause)
            Pause();
        super.onResume();

    }

    public void Update()
    {
        long currentTime = System.nanoTime();
        long deltaTime = currentTime - lastUpdate;
        lastUpdate = currentTime;

        if (deltaTime < 2000000L * UPDATE_MILLIS)
        {
            float seconds = ((float) deltaTime) * 1e-9f;
            //Log.d("Update", "Update called (" + seconds + " seconds)!");

            if(!isPaused)
            {
                if(currentHealth <=0)
                {
                    gameOver = true;
                    isPaused = true;
                    MainMenu("Game Over", "Do you want to replay?");
                }

                for (int i = 0; i < Missile.enemyMissile.size(); i++)
                {
                    Missile.enemyMissile.get(i).Update(seconds);
                }
                for (int i = 0; i < Missile.playerMissile.size(); i++)
                {
                    Missile.playerMissile.get(i).Update(seconds);
                }
                for (int i = 0; i < Explosion.explosionList.size(); i++)
                {
                    Explosion.explosionList.get(i).Update(seconds);
                }
                elapsedTime += seconds;
                if (elapsedTime >= 2.0f)
                {
                    //Log.d("Update", "Update called (" + elapsedTime + " seconds)!");
                    randomSX = rand.nextInt(101);
                    randomEX = rand.nextInt(101);
                    Missile.CreateMissile(randomSX, 200, randomEX, 10, 20, false);
                    elapsedTime = 0.0f;
                }
            }
        }
        gameView.invalidate();
        requestUpdate();
    }

    private void requestUpdate()
    {
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    private void cancelUpdate()
    {
        handler.removeCallbacks(runnable);
    }

    private void MainMenu(String title, String message)
    {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isPaused = false;
                        dialog.dismiss();
                        rand = new Random();
                        gameView = (GameView) findViewById(R.id.gameView);
                        Missile.enemyMissile = new ArrayList<Missile>();
                        Missile.playerMissile = new ArrayList<Missile>();
                        Explosion.explosionList = new ArrayList<Explosion>();
                        currentHealth = 100;
                    }
                })
                .setNegativeButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameOver = true;
                        finish();
                    }
                })
                .show();
    }

    private void Pause()
    {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Pause")
                .setCancelable(false)
                .setPositiveButton("Resume", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isPaused = false;
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy()
    {
        gameView = null;
        super.onDestroy();
    }
}
