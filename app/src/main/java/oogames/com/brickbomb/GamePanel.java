package oogames.com.brickbomb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by ADMIN on 11.02.2016.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;

    private boolean onMenu = true;
    private MenuButton btnStart;
    private MenuButton btnHowToPlay;
    private MenuButton btnHighScore;
    private MenuButton btnRate;
    private MenuButton btnExit;

    public GamePanel(Context context) {
        super(context);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        //make gamePanel focusable and handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                retry = false;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int w = this.getWidth();
        int h = this.getHeight();
        System.out.printf("ScreenW: %d ScreenH: %d%n", w, h);
        Resources resources = getResources();

        //createMenu
        btnStart = MenuButton.StartButton(w, h, resources);
        btnHowToPlay = MenuButton.HowToPlay(w, h, resources);
        btnHighScore = MenuButton.HighScore(w, h, resources);
        btnRate = MenuButton.Rate(w, h, resources);
        btnExit = MenuButton.ExitButton(w, h, resources);

        //start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float locX = event.getX();
        float locY = event.getY();

        if (onMenu) {
            if (btnStart.ContainsCGPosition(locX, locY)) {
                System.out.println("btnStart Clicked");
            } else if (btnHowToPlay.ContainsCGPosition(locX, locY)) {
                System.out.println("btnHowToPlay Clicked");
            } else if (btnHighScore.ContainsCGPosition(locX, locY)) {
                System.out.println("btnHighScore Clicked");
            } else if (btnRate.ContainsCGPosition(locX, locY)) {
                System.out.println("btnRate Clicked");
            } else if (btnExit.ContainsCGPosition(locX, locY)) {
                System.out.println("btnExit Clicked");

                new AlertDialog.Builder(getContext())
                        .setTitle("Leaving")
                        .setMessage("Do you want to leave?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ((Activity) getContext()).finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }

        return super.onTouchEvent(event);
    }

    public void update() {
        if (onMenu) {
            btnStart.update();
            btnStart.update();
            btnHowToPlay.update();
            btnHighScore.update();
            btnRate.update();
            btnExit.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            final int savedState = canvas.save();
            super.draw(canvas);

            if (onMenu) {
                btnStart.draw(canvas);
                btnHowToPlay.draw(canvas);
                btnHighScore.draw(canvas);
                btnRate.draw(canvas);
                btnExit.draw(canvas);
            }

            canvas.restoreToCount(savedState);
        }
    }
}