package oogames.com.brickbomb;

/**
 * Created by oguzkoroglu on 12/02/16.
 */
public class Position {

    public int X;
    public int Y;

    public static Position PositionWithX(int x, int y) {
        Position p = new Position();
        p.X = x;
        p.Y = y;
        return p;
    }

    public Position Left() {
        return PositionWithX(X - 1, Y);
    }

    public Position Right() {
        return PositionWithX(X + 1, Y);
    }

    public Position Bottom() {
        return PositionWithX(X, Y + 1);
    }

    public Position Top() {
        return PositionWithX(X, Y - 1);
    }

    public boolean isEqualPos(Position p) {
        return p.X == X && p.Y == Y;
    }
}