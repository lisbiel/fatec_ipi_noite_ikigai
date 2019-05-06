package br.com.lisboa.fatec_ipi_noite_ikigai;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class DoodleView extends View {

    private static final int TOUCH_TOLERANCE = 10;
    private static final int RADIUS = 450;
    private static final int ALPHA = 255;
    private Paint paintLine;
    private Bitmap bitmap;
    private Canvas canvasBitmap;
    private Paint paintScreen;
    private boolean isDrawn;
    private Region region;

    private Map<Integer, Path> pathMap = new HashMap<>();

    private  Map<Integer, Point> previousPointMap = new HashMap<>();

    public DoodleView(Context context, AttributeSet set) {
        super(context, set);
        paintScreen = new Paint();
        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.BLACK);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);
        paintLine.setStrokeCap(Paint.Cap.ROUND);
        isDrawn = false;
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvasBitmap = new Canvas(bitmap);
        canvasBitmap.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        bitmap.eraseColor(Color.WHITE);
    }

    public void clear()  {
        pathMap.clear();
        previousPointMap.clear();
        bitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    public void setDrawingColor (int color) {
        this.paintLine.setColor(color);
    }

    public int getDrawingColor () {
        return this.paintLine.getColor();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawIkigai(canvas, paintLine);
    }

    public void drawIkigai(Canvas canvas, Paint paintLine) {
        DisplayMetrics dm = new DisplayMetrics();
        paintLine.setStyle(Paint.Style.FILL);
        paintLine.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        randomizeColor(paintLine);
        canvas.drawCircle(getWidth() / 2, getHeight() / 4 + 350, RADIUS, paintLine);
        randomizeColor(paintLine);
        canvas.drawCircle(getWidth() / 4 * 3 - 100, getHeight() / 2, RADIUS, paintLine);
        randomizeColor(paintLine);
        canvas.drawCircle(getWidth() / 4 + 100, getHeight() / 2, RADIUS, paintLine);
        randomizeColor(paintLine);
        canvas.drawCircle(getWidth() / 2, getHeight() / 4 * 3 - 350, RADIUS, paintLine);
        isDrawn = true;
    }

    private void randomizeColor(Paint paintLine) {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        paintLine.setColor(Color.argb(ALPHA, r, g, b));
    }


/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        int actionIndex = event.getActionIndex();

        if( action == MotionEvent.ACTION_DOWN ||
                                action == MotionEvent.ACTION_POINTER_DOWN) {
            touchStarted(
                    event.getX(actionIndex),
                    event.getY(actionIndex),
                    event.getPointerId(actionIndex)
            );

        } else if(action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_POINTER_UP) {
            touchEnded (event.getPointerId(actionIndex));
        } else {
            touchMoved(event);
        }
        invalidate();
        return true;
    }

    private void
    touchEnded (
            int
                    lineID){
        Path path =
                pathMap
                        .get(lineID);
        canvasBitmap.drawPath(path, paintLine);
        path.reset();
    }

    private void
    touchMoved (MotionEvent event){
        for (int i = 0; i < event.getPointerCount(); i++){
            int
                    pointerID = event.getPointerId(i);
            int
                    pointerIndex = event.findPointerIndex(pointerID);
            if
            (
                    pathMap
                            .containsKey(pointerID)){
                float
                        newX = event.getX(pointerIndex);
                float
                        newY = event.getY(pointerIndex);
                Path path =
                        pathMap
                                .get(pointerID);
                Point point =
                        previousPointMap
                                .get(pointerID);
                float deltaX = Math.abs(newX - point.x);
                float deltaY = Math.abs(newY - point.y);
                if
                (deltaX >= TOUCH_TOLERANCE
                        || deltaY >= TOUCH_TOLERANCE
                ){
                    path.quadTo(point.x, point.y,
                            (newX + point.x) / 2,
                            (newY + point.y) / 2
                    );
                    point.x = (int) newX;
                    point.y = (int) newY;}
            }
        }
    }

    private void touchStarted (float x, float y, int lineID) {
        Path path;
        Point point;
        if(pathMap.containsKey(lineID)) {
            path = pathMap.get(lineID);
            path.reset();
            point = previousPointMap.get(lineID);
        } else {
            path = new Path();
            pathMap.put(lineID, path);
            point = new Point();
            previousPointMap.put(lineID, point);
        }
        path.moveTo(x, y);
        point.x = (int) x;
        point.y = (int) y;
    }*/
}

