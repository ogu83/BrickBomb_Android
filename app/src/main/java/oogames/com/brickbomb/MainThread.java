package oogames.com.brickbomb;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.util.Random;

/**
 * Created by ADMIN on 11.02.2016.
 */
public class MainThread extends Thread {
    public static Canvas canvas;
    public static Random RND = new Random();
    private int FPS = 15;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / FPS;

        while (running) {
            startTime = System.currentTimeMillis();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    while (this.gamePanel.isUpdating)
                        sleep(1);
                    this.gamePanel.postInvalidate();
                    if (!this.gamePanel.isUpdating)
                        this.gamePanel.draw(canvas);
                }
            } catch (Exception ex) {
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            timeMillis = (System.currentTimeMillis() - startTime) / 1;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            } catch (Exception ex) {
            }

            totalTime += System.currentTimeMillis() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1);
                frameCount = 0;
                totalTime = 0;
                System.out.printf("AverageFPS : %s%n", averageFPS);
            }
        }
    }

    public void setRunning(boolean b) {
        running = b;
    }
}
