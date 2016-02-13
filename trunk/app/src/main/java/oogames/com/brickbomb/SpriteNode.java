package oogames.com.brickbomb;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by oguzkoroglu on 12/02/16.
 */
public class SpriteNode {
    public Bitmap image;
    public int X, Y;
    public int Width;
    public int Height;

    public SpriteNode(Bitmap res, int w, int h) {
        Width = w;
        Height = h;
        image = Bitmap.createScaledBitmap(res, w, h, false);
    }

    public void setSize(int w, int h) {
        Width = w;
        Height = h;
        image = Bitmap.createScaledBitmap(image, w, h, false);
    }

    public boolean ContainsCGPosition(float x, float y) {
        return x > X && x < X + Width && y > Y && y < Y + Height;
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, X, Y, null);
    }
}
