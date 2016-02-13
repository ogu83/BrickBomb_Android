package oogames.com.brickbomb;

import android.content.res.Resources;

/**
 * Created by oguzkoroglu on 12/02/16.
 */
public class BrickCouple {
    public Brick Brick1;
    public Brick Brick2;
    public int rotationPos;

    public BrickCouple() {

    }

    public static BrickCouple RandomBrickCouple(Resources resources, int w, int h) {
        Brick b0;
        Brick b1;
        while (true) {
            b0 = Brick.getRandomBrick(resources, w, h);
            b1 = Brick.getRandomBrick(resources, w, h);
            if (b1.BType != b0.BType) break;
        }

        BrickCouple b = new BrickCouple();
        b0.Couple = b1;
        b1.Couple = b0;
        b.Brick1 = b0;
        b.Brick2 = b1;
        b.Brick1.ParentCouple = b;
        b.Brick2.ParentCouple = b;
        return b;
    }
}
