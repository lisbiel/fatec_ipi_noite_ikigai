package br.com.lisboa.fatec_ipi_noite_paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class DoodleView extends View {

    private static final int TOUCH_TOLERANCE = 10;

    private Paint paintLine;
    private Bitmap bitmap;
    private Canvas canvasBitmap;
    private Paint paintScreen;

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
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvasBitmap = new Canvas(bitmap);
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
        canvas.drawBitmap(bitmap, 0, 0, paintScreen);
        int A = (int)(Math.random()*256);
        int R = (int)(Math.random()*256);
        int G = (int)(Math.random()*256);
        int B = (int)(Math.random()*256);
        setDrawingColor(Color.argb(A, R, G, B));
        for (Integer key: pathMap.keySet()) {
            canvas.drawPath(pathMap.get(key), paintLine);
        }
    }

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
    }
}

