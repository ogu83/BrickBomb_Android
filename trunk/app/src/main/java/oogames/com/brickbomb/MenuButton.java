package oogames.com.brickbomb;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by ADMIN on 11.02.2016.
 */
public class MenuButton extends SpriteNode {

    public MenuButton(Bitmap res, int w, int h) {
        super(res, w, h);
    }

    private static double buttonScaleRatio() {
        return 266.0 / 845.0;
    }

    public static MenuButton StartButton(int screenW, int screenH, Resources resources) {
        int w = screenW / 2;
        int h = (int) (w * buttonScaleRatio());
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        MenuButton mb = new MenuButton(BitmapFactory.decodeResource(resources, R.drawable.start, opt), w, h);
        mb.X = screenW / 2 - w / 2;
        mb.Y = (int) (screenH / 2 - h * 1.1 * 2);
        return mb;
    }

    public static MenuButton HowToPlay(int screenW, int screenH, Resources resources) {
        int w = screenW / 2;
        int h = (int) (w * buttonScaleRatio());
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        MenuButton mb = new MenuButton(BitmapFactory.decodeResource(resources, R.drawable.howtoplay, opt), w, h);
        mb.X = screenW / 2 - w / 2;
        mb.Y = (int) (screenH / 2 - h * 1.1);
        return mb;
    }

    public static MenuButton HighScore(int screenW, int screenH, Resources resources) {
        int w = screenW / 2;
        int h = (int) (w * buttonScaleRatio());
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        MenuButton mb = new MenuButton(BitmapFactory.decodeResource(resources, R.drawable.highscore, opt), w, h);
        mb.X = screenW / 2 - w / 2;
        mb.Y = (screenH / 2);
        return mb;
    }

    public static MenuButton Rate(int screenW, int screenH, Resources resources) {
        int w = screenW / 2;
        int h = (int) (w * buttonScaleRatio());
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        MenuButton mb = new MenuButton(BitmapFactory.decodeResource(resources, R.drawable.rate, opt), w, h);
        mb.X = screenW / 2 - w / 2;
        mb.Y = (int) (screenH / 2 - h * -1.1);
        return mb;
    }

    public static MenuButton ExitButton(int screenW, int screenH, Resources resources) {
        int w = screenW / 2;

        int h = (int) (w * buttonScaleRatio());
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        MenuButton mb = new MenuButton(BitmapFactory.decodeResource(resources, R.drawable.exit, opt), w, h);
        mb.X = screenW / 2 - w / 2;
        mb.Y = (int) (screenH / 2 - h * -1.1 * 2);
        return mb;
    }
}
