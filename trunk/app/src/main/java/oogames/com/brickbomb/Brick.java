package oogames.com.brickbomb;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by oguzkoroglu on 12/02/16.
 */
public class Brick extends SpriteNode {
    public BrickType BType;
    public int PositionX;
    public int PositionY;
    public Brick Couple;
    public boolean IsMoved;
    public boolean IsReadyToExplode;
    public BrickCouple ParentCouple;

    public Brick(Bitmap res, int w, int h) {
        super(res, w, h);
    }

    public static Brick redBrick(Resources resources, int w, int h) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        Brick b = new Brick(BitmapFactory.decodeResource(resources, R.drawable.redbrick, opt), w, h);
        b.BType = BrickType.Red;
        return b;
    }

    public static Brick blueBrick(Resources resources, int w, int h) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        Brick b = new Brick(BitmapFactory.decodeResource(resources, R.drawable.bluebrick, opt), w, h);
        b.BType = BrickType.Blue;
        return b;
    }

    public static Brick greenBrick(Resources resources, int w, int h) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        Brick b = new Brick(BitmapFactory.decodeResource(resources, R.drawable.greenbrick, opt), w, h);
        b.BType = BrickType.Green;
        return b;
    }

    public static Brick yellowBrick(Resources resources, int w, int h) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        Brick b = new Brick(BitmapFactory.decodeResource(resources, R.drawable.yellowbrick, opt), w, h);
        b.BType = BrickType.Yellow;
        return b;
    }

    public static Brick orangeBrick(Resources resources, int w, int h) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        Brick b = new Brick(BitmapFactory.decodeResource(resources, R.drawable.orangebrick, opt), w, h);
        b.BType = BrickType.Orange;
        return b;
    }

    public static Brick fBrick(Resources resources, int w, int h) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        Brick b = new Brick(BitmapFactory.decodeResource(resources, R.drawable.fbrick, opt), w, h);
        b.BType = BrickType.f;
        return b;
    }

    public static Brick tBrick(Resources resources, int w, int h) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        Brick b = new Brick(BitmapFactory.decodeResource(resources, R.drawable.tbrick, opt), w, h);
        b.BType = BrickType.t;
        return b;
    }

    public static Brick purpleBrick(Resources resources, int w, int h) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        Brick b = new Brick(BitmapFactory.decodeResource(resources, R.drawable.purplebrick, opt), w, h);
        b.BType = BrickType.Purple;
        return b;
    }

    public static Brick BirckWithType(BrickType brickType, Resources resources, int w, int h) {
        switch (brickType) {
            case Red:
                return redBrick(resources, w, h);
            case Blue:
                return blueBrick(resources, w, h);
            case Green:
                return greenBrick(resources, w, h);
            case Yellow:
                return yellowBrick(resources, w, h);
            case Orange:
                return orangeBrick(resources, w, h);
            case f:
                return fBrick(resources, w, h);
            case t:
                return tBrick(resources, w, h);
            case Purple:
                return purpleBrick(resources, w, h);
            default:
                return null;
        }
    }

    public static Brick getRandomBrick(Resources resources, int w, int h) {
        int number = MainThread.RND.nextInt(8);
        BrickType brickType = BrickType.fromInteger(number);
        return BirckWithType(brickType, resources, w, h);
    }

    public Position Pos() {
        return Position.PositionWithX(PositionX, PositionY);
    }

    public void setPos(Position pos) {
        PositionX = pos.X;
        PositionY = pos.Y;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, X - Width / 2, Y - Height / 2, null);
    }

    public enum BrickType {
        Red, Blue, Green, Yellow, Orange, f, t, Purple;

        public static BrickType fromInteger(int x) {
            switch (x) {
                case 0:
                    return Red;
                case 1:
                    return Blue;
                case 2:
                    return Green;
                case 3:
                    return Yellow;
                case 4:
                    return Orange;
                case 5:
                    return f;
                case 6:
                    return t;
                case 7:
                    return Purple;
            }
            return null;
        }
    }
}
