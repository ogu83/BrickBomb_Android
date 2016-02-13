package oogames.com.brickbomb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ADMIN on 11.02.2016.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    long updatedTime;
    private int posXCount = 9;
    private int posYCount = 16;
    private int xMargin = 10;
    private int yMargin = 40;
    private int newCoupleScreenDivider = 2;
    private double levelIntervalInSeconds = 0.5;
    private boolean gamePaused;
    private boolean gameOver;
    private boolean onMenu;
    private boolean onMoveBricksDown;

    private MediaPlayer player;
    private boolean isSoundOn;

    private int level = 1;
    private int nextLevelScore = 100;
    private int levelPow = 10;
    private String UserName;

    private MainThread thread;

    private List<Brick> bricks;

    private boolean gridLinesOn = true;
    private BrickCouple brickCouple;
    private Brick selectedBrick;
    private int firstTouchX;
    private boolean brickMoved;

    private int score;

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


        bricks = Collections.synchronizedList(new ArrayList<Brick>());
        gridLinesOn = true;
        createMenu(w, h, resources);
        createBackground();
        createBackgroundMusic2();

        //start the game loop
        thread.setRunning(true);
        thread.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float locX = event.getX();
        float locY = event.getY();


        //System.out.println("Touch Action : " + event.getActionMasked());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (onMenu) {
                    if (btnStart.ContainsCGPosition(locX, locY)) {
                        System.out.println("btnStart Clicked");
                        startGame();
                    } else if (btnHowToPlay.ContainsCGPosition(locX, locY)) {
                        System.out.println("btnHowToPlay Clicked");
                    } else if (btnHighScore.ContainsCGPosition(locX, locY)) {
                        System.out.println("btnHighScore Clicked");
                        gotoHighScores();
                    } else if (btnRate.ContainsCGPosition(locX, locY)) {
                        System.out.println("btnRate Clicked");
                    } else if (btnExit.ContainsCGPosition(locX, locY)) {
                        System.out.println("btnExit Clicked");
                        doExit();
                    }
                } else {
                    for (Brick b : bricks) {
                        int left = CGPosXFromPosX(b.Pos().Left().X);
                        int right = CGPosXFromPosX(b.Pos().Right().X);
                        int top = CGPosYFromPosY(b.Pos().Top().Y);
                        int bottom = CGPosYFromPosY(b.Pos().Bottom().Y);

                        if (locX > left && locX < right &&
                                locY < bottom && locY > top) {
                            selectedBrick = b;
                            firstTouchX = (int) locX;
                            System.out.println("Selected Brick:" + b.BType.toString());
                            break;
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                slideBrick((int) locX, (int) locY);

                break;
            case MotionEvent.ACTION_UP:
                if (selectedBrick != null && !brickMoved)
                    rotateBrick(selectedBrick);

                brickMoved = false;
                selectedBrick = null;
                firstTouchX = 0;

                break;
            default:
                return false;
        }

        return true;
    }

    private int CGPosXFromPosX(int x) {
        int frameW = this.getWidth();
        int w = (frameW - xMargin * 2) / (posXCount + 1);
        int bx = w * (x + 1) + xMargin - w / 2;
        return bx;
    }

    private int CGPosYFromPosY(int y) {
        int frameH = this.getHeight();
        int h = (frameH - yMargin * 2) / (posYCount + 1);
        int by = h * (y + 1) + yMargin - h / 2;
        return by;
    }

    private void createMenu(int w, int h, Resources resources) {
        btnStart = MenuButton.StartButton(w, h, resources);
        btnHowToPlay = MenuButton.HowToPlay(w, h, resources);
        btnHighScore = MenuButton.HighScore(w, h, resources);
        btnRate = MenuButton.Rate(w, h, resources);
        btnExit = MenuButton.ExitButton(w, h, resources);
        onMenu = true;
    }

    private void removeMenu() {
        btnStart = null;
        btnHowToPlay = null;
        btnHighScore = null;
        btnRate = null;
        btnExit = null;
        onMenu = false;
    }

    private void startGameWithBricks() {
        //[music2 removeFromParent];
        isSoundOn = true;
        gamePaused = false;
        //[self createSoundButton:isSoundOn];
        //[self createPlayPauseButton:gamePaused];
        //[self createGotoMenuButton];

        createBackgroundMusic1();
        gridLinesOn = true;
        //[self drawScore];
        removeMenu();

        gameOver = false;
    }

    private void startGame() {
        bricks = Collections.synchronizedList(new ArrayList<Brick>());
        //[self createBrickCouple];
        startGameWithBricks();
    }

    private void createBrickCouple() {
        int frameW = this.getWidth();
        int frameH = this.getHeight();
        //System.out.printf("ScreenW: %d ScreenH: %d%n", w, h);
        Resources resources = getResources();

        int w = (frameW - xMargin * 2) / (posXCount + 1);
        int h = (frameH - yMargin * 2) / (posYCount + 1);
        brickCouple = BrickCouple.RandomBrickCouple(resources, w, h);

        brickCouple.Brick1.PositionX = MainThread.RND.nextInt(posXCount - 1);
        brickCouple.Brick1.PositionY = 0;

        brickCouple.Brick2.PositionY = MainThread.RND.nextInt(2);
        brickCouple.Brick2.PositionX = brickCouple.Brick2.PositionY == 0 ? brickCouple.Brick1.PositionX + 1 : brickCouple.Brick1.PositionX;

        brickCouple.rotationPos = brickCouple.Brick2.PositionY == 0 ? 1 : 0;

        bricks.add(brickCouple.Brick2);
        bricks.add(brickCouple.Brick1);

        setBrickPosition(brickCouple.Brick1);
        setBrickPosition(brickCouple.Brick2);
    }

    private void setBrickPosition(Brick b) {
        if (b == null) return;

        int frameW = this.getWidth();
        int frameH = this.getHeight();
        //System.out.printf("ScreenW: %d ScreenH: %d%n", w, h);

        int bx = (b.Width) * (b.PositionX + 1) + xMargin;
        int by = (b.Height) * (b.PositionY + 1) + yMargin;
        b.X = bx;
        b.Y = by;

        if (!bricks.contains(b)) {
            int w = (frameW - xMargin * 2) / (posXCount + 1);
            int h = (frameH - yMargin * 2) / (posYCount + 1);
            b.setSize(w, h);
            bricks.add(b);
        }
    }

    private boolean canMoveDown(Brick brick) {
        boolean retVal = brick.PositionY + 1 < posYCount;
        if (retVal)
            for (Brick b : bricks)
                if (b != brick)
                    if (b.PositionY == brick.PositionY + 1 && b.PositionX == brick.PositionX) {
                        retVal = false;
                        break;
                    }
        return retVal;
    }

    private boolean canMoveUp(Brick brick) {
        boolean retVal = brick.PositionY - 1 > 0;
        if (retVal)
            for (Brick b : bricks)
                if (b != brick)
                    if (b.PositionY == brick.PositionY - 1 && b.PositionX == brick.PositionX) {
                        retVal = false;
                        break;
                    }
        return retVal;
    }

    private boolean canMoveRight(Brick brick) {
        boolean retVal = brick.PositionX + 1 < posXCount;
        if (retVal)
            for (Brick b : bricks)
                if (b != brick)
                    if (b.PositionX == brick.PositionX + 1 && b.PositionY == brick.PositionY) {
                        retVal = false;
                        break;
                    }
        return retVal;
    }

    private boolean canMoveLeft(Brick brick) {
        boolean retVal = brick.PositionX > 0;
        if (retVal)
            for (Brick b : bricks)
                if (b != brick)
                    if (b.PositionX == brick.PositionX - 1 && b.PositionY == brick.PositionY) {
                        retVal = false;
                        break;
                    }
        return retVal;
    }

    private boolean anyBrickAtPosition(Position p) {
        for (Brick b : bricks)
            if (p.isEqualPos(b.Pos()))
                return true;
        return false;
    }

    private void moveBricksDown() {
        onMoveBricksDown = true;
        boolean newCoupleNeed = true;

        for (Brick b : bricks)
            b.IsMoved = false;

        for (Brick b : bricks) {
            if (canMoveDown(b)) {
                if (b.Couple != null) {
                    if (b.Couple.PositionX != b.PositionX) {
                        if (canMoveDown(b.Couple)) {
                            b.PositionY++;
                            b.IsMoved = true;
                            newCoupleNeed = false;
                        } else if (b.Couple.IsMoved) {
                            b.PositionY++;
                            b.IsMoved = true;
                            newCoupleNeed = false;
                        }
                    } else {
                        b.PositionY++;
                        b.IsMoved = true;
                        b.Couple.PositionY++;
                        b.Couple.IsMoved = true;
                        newCoupleNeed = false;
                    }
                } else {
                    b.PositionY++;
                    b.IsMoved = true;
                    newCoupleNeed = false;
                }
            }
            newCoupleNeed = newCoupleNeed || (b.PositionY > posYCount / newCoupleScreenDivider);
            setBrickPosition(b);
            setBrickPosition(b.Couple);
        }

        if (newCoupleNeed)
            createBrickCouple();

        onMoveBricksDown = false;
    }

    public void slideBrick(int positionInSceneX, int positionInSceneY) {
        if (selectedBrick != null && selectedBrick.Couple != null) {
            if (Math.abs(positionInSceneX - CGPosXFromPosX(selectedBrick.PositionX)) > selectedBrick.Width) {
                int xDeltaPos = Math.round((positionInSceneX - xMargin / 2) / (selectedBrick.Width));
                xDeltaPos = Math.min(xDeltaPos, posXCount - 1);
                xDeltaPos = Math.max(xDeltaPos, 0);

                if (positionInSceneX > firstTouchX) {
                    if (selectedBrick.PositionX == selectedBrick.Couple.PositionX) {
                        while (xDeltaPos > selectedBrick.PositionX) {
                            if (canMoveRight(selectedBrick) && canMoveRight(selectedBrick.Couple)) {
                                selectedBrick.IsMoved = selectedBrick.Couple.IsMoved = true;
                                selectedBrick.PositionX++;
                                selectedBrick.Couple.PositionX++;
                                setBrickPosition(selectedBrick);
                                setBrickPosition(selectedBrick.Couple);
                                break;
                            } else
                                break;
                        }
                    } else if (selectedBrick.PositionX > selectedBrick.Couple.PositionX) {
                        while (xDeltaPos > selectedBrick.PositionX) {
                            if (canMoveRight(selectedBrick)) {
                                selectedBrick.IsMoved = selectedBrick.Couple.IsMoved = true;
                                selectedBrick.PositionX++;
                                selectedBrick.Couple.PositionX++;
                                setBrickPosition(selectedBrick);
                                setBrickPosition(selectedBrick.Couple);
                                break;
                            } else
                                break;
                        }
                    } else {
                        while (xDeltaPos > selectedBrick.Couple.PositionX) {
                            if (canMoveRight(selectedBrick.Couple)) {
                                selectedBrick.IsMoved = selectedBrick.Couple.IsMoved = true;
                                selectedBrick.PositionX++;
                                selectedBrick.Couple.PositionX++;
                                setBrickPosition(selectedBrick);
                                setBrickPosition(selectedBrick.Couple);
                                break;
                            } else
                                break;
                        }
                    }
                    //NSLog(@"Right Move");
                } else {
                    if (selectedBrick.PositionX == selectedBrick.Couple.PositionX) {
                        while (xDeltaPos < selectedBrick.PositionX) {
                            if (canMoveLeft(selectedBrick) && canMoveLeft(selectedBrick.Couple)) {
                                selectedBrick.IsMoved = selectedBrick.Couple.IsMoved = true;
                                selectedBrick.PositionX--;
                                selectedBrick.Couple.PositionX--;
                                setBrickPosition(selectedBrick);
                                setBrickPosition(selectedBrick.Couple);
                                break;
                            } else
                                break;
                        }
                    } else if (selectedBrick.PositionX < selectedBrick.Couple.PositionX) {
                        while (xDeltaPos < selectedBrick.PositionX) {
                            if (canMoveLeft(selectedBrick)) {
                                selectedBrick.IsMoved = selectedBrick.Couple.IsMoved = true;
                                selectedBrick.PositionX--;
                                selectedBrick.Couple.PositionX--;
                                setBrickPosition(selectedBrick);
                                setBrickPosition(selectedBrick.Couple);
                                break;
                            } else
                                break;
                        }
                    } else {
                        while (xDeltaPos < selectedBrick.Couple.PositionX) {
                            if (canMoveLeft(selectedBrick.Couple)) {
                                selectedBrick.IsMoved = selectedBrick.Couple.IsMoved = true;
                                selectedBrick.PositionX--;
                                selectedBrick.Couple.PositionX--;
                                setBrickPosition(selectedBrick);
                                setBrickPosition(selectedBrick.Couple);
                                break;
                            } else
                                break;
                        }
                    }
                    //NSLog(@"Left Move");
                }
                brickMoved = true;
            }
        }
    }

    public void rotateBrick(Brick b) {
        if (!onMoveBricksDown) {
            if (b.Couple != null) {
                BrickCouple bc = b.ParentCouple;
                if (bc == null) return;
                if (canMoveDown(b) || canMoveDown(b.Couple)) {
                    if (bc.rotationPos == 0) {
                        if (canMoveRight(bc.Brick2) && !anyBrickAtPosition(bc.Brick1.Pos().Right())) {
                            bc.Brick2.setPos(bc.Brick1.Pos().Right());
                            bc.rotationPos = 1;
                            bc.Brick2.IsMoved = true;
                            setBrickPosition(bc.Brick2);
                        }
                    } else if (bc.rotationPos == 1) {
                        if (canMoveUp(bc.Brick2) && !anyBrickAtPosition(bc.Brick1.Pos().Top())) {
                            bc.Brick2.setPos(bc.Brick1.Pos().Top());
                            bc.rotationPos = 2;
                            bc.Brick2.IsMoved = true;
                            setBrickPosition(bc.Brick2);
                        }
                    } else if (bc.rotationPos == 2) {
                        if (canMoveLeft(bc.Brick2) && !anyBrickAtPosition(bc.Brick1.Pos().Left())) {
                            bc.Brick2.setPos(bc.Brick1.Pos().Left());
                            bc.rotationPos = 3;
                            bc.Brick2.IsMoved = true;
                            setBrickPosition(bc.Brick2);
                        }
                    } else {
                        if (canMoveDown(bc.Brick2) && !anyBrickAtPosition(bc.Brick1.Pos().Bottom())) {
                            bc.Brick2.setPos(bc.Brick1.Pos().Bottom());
                            bc.rotationPos = 0;
                            bc.Brick2.IsMoved = true;
                            setBrickPosition(bc.Brick2);
                        }
                    }
                }
            }
        }
    }

    public void explodeBricks() {
        List<Brick> discardedItems = Collections.synchronizedList(new ArrayList<Brick>());

        for (int b0 = 0; b0 < bricks.size(); b0++) {
            for (int b1 = b0 + 1; b1 < bricks.size(); b1++) {
                Brick bb0 = bricks.get(b0);
                Brick bb1 = bricks.get(b1);
                if (bb0.Couple != bb1 && bb1.Couple != bb0 && bb0 != bb1
                        && !bb0.IsReadyToExplode && !bb1.IsReadyToExplode
                        && bb1.BType == bb0.BType) {
                    boolean isbb0Stuck = !bb0.IsMoved;
                    boolean isbb1Stuck = !bb1.IsMoved;

                    if (isbb0Stuck && isbb1Stuck) {
                        bb0.IsReadyToExplode = bb1.IsReadyToExplode =
                                bb0.Pos().Right().isEqualPos(bb1.Pos()) ||
                                        bb0.Pos().Left().isEqualPos(bb1.Pos()) ||
                                        bb0.Pos().Top().isEqualPos(bb1.Pos()) ||
                                        bb0.Pos().Bottom().isEqualPos(bb1.Pos());

                        if (bb0.IsReadyToExplode) {
                            discardedItems.add(bb0);
                            discardedItems.add(bb1);
                        }
                    }
                }
            }
        }

        if (discardedItems.size() > 0) {
            bricks.removeAll(discardedItems);
            for (Brick b : discardedItems) {
                b.Couple.Couple = null;
                b.Couple = null;

                score += posXCount * posYCount * newCoupleScreenDivider / levelIntervalInSeconds / 100;
                //[self drawScore];

                //SKAction* bum = [SKAction playSoundFileNamed:@"Bum4.wav" waitForCompletion:NO];
                //[self runAction:bum];
            }
        }
    }

    private void createBackground() {
        setBackgroundColor(Color.rgb(25, 25, 25));
    }

    private void createBackgroundMusic1() {
        if (player != null) player.stop();
        player = MediaPlayer.create(getContext(), R.raw.music1);
        player.setLooping(true); // Set looping
        player.setVolume(100, 100);
        player.start();
    }

    private void createBackgroundMusic2() {
        if (player != null) player.stop();
        player = MediaPlayer.create(getContext(), R.raw.music2);
        player.setLooping(true); // Set looping
        player.setVolume(100, 100);
        player.start();
    }

    private void gotoHighScores() {
        String link = Constants.WebSite + "/HighScore";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        getContext().startActivity(browserIntent);
    }

    private void doExit() {
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

    public void update() {
        long currentTime = System.currentTimeMillis();
        if (!onMenu && !gameOver && !gamePaused) {
            if (updatedTime == 0 || (currentTime - updatedTime) > 1000 * levelIntervalInSeconds) {
                //[self checkGameOver];
                //[self explodeBircks];
                explodeBricks();
                moveBricksDown();
                for (Brick b : bricks)
                    b.update();
                //[self calculateScore];
                updatedTime = currentTime;
            }
        } else if (onMenu) {
            btnStart.update();
            btnStart.update();
            btnHowToPlay.update();
            btnHighScore.update();
            btnRate.update();
            btnExit.update();

            if (updatedTime == 0 || currentTime - updatedTime > 1000 * 0.25) {
                moveBricksDown();
                for (Brick b : bricks)
                    b.update();
                updatedTime = currentTime;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            final int savedState = canvas.save();
            super.draw(canvas);

            if (gridLinesOn) {
                Paint gridPaint = new Paint();
                gridPaint.setColor(Color.GRAY);
                for (int x = 0; x < posXCount + 1; x++)
                    canvas.drawLine(CGPosXFromPosX(x), CGPosYFromPosY(0), CGPosXFromPosX(x), CGPosYFromPosY(posYCount), gridPaint);
                for (int y = 0; y < posYCount + 1; y++)
                    canvas.drawLine(CGPosXFromPosX(0), CGPosYFromPosY(y), CGPosXFromPosX(posXCount), CGPosYFromPosY(y), gridPaint);
            }

            for (Brick b : bricks)
                b.draw(canvas);

            if (onMenu) {
                btnStart.draw(canvas);
                btnHowToPlay.draw(canvas);
                btnHighScore.draw(canvas);
                btnRate.draw(canvas);
                btnExit.draw(canvas);
            } else {

            }

            canvas.restoreToCount(savedState);
        }
    }
}