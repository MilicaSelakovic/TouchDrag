package touch.drag.milica.master;

import android.graphics.DashPathEffect;
import android.graphics.PointF;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import geometry.calculations.descretegeometrycalculations.*;

public class DrawingController extends View {

    public enum Mode {
        MODE_MOVE, MODE_USUAL, MODE_SELECT;

    }

    private Path drawPath;
    private Paint drawPaint, canvasPaint, objectPaint, drawObject;
    private Bitmap canvasBitmap;

    private Vector<PointF> points;

    boolean actionDown;


    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.f;

    private Mode mode;
    private PointInformations pointInformations;

    public boolean isUnsaved() {
        return unsaved;
    }

    public void setUnsaved(boolean unsaved) {
        this.unsaved = unsaved;
    }

    private boolean unsaved = true;

    private TextView textView;

    private DrawingModel model;
    public DrawingController(Context context, AttributeSet attrs) {
        super(context, attrs);
        pointInformations = new PointInformations(0);
        setupDrawing();

        actionDown = false;
        mode = Mode.MODE_USUAL;
        points = new Vector<>();

        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        model = new DrawingModel();
        textView = null;
    }

    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(pointInformations.getDrawPathColor());
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(10);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        drawObject = new Paint();
        drawObject.setColor(pointInformations.getTempObjectColor());
        drawObject.setAntiAlias(true);
        drawObject.setStrokeWidth(3);
        drawObject.setStyle(Paint.Style.STROKE);
        drawObject.setStrokeJoin(Paint.Join.ROUND);
        drawObject.setStrokeCap(Paint.Cap.ROUND);
        drawObject.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));


        objectPaint = new Paint();
        objectPaint.setColor(pointInformations.getPaintColor());
        objectPaint.setAntiAlias(true);
        objectPaint.setStrokeWidth(4);
        objectPaint.setStyle(Paint.Style.STROKE);
        objectPaint.setStrokeJoin(Paint.Join.ROUND);
        objectPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

        if (mode == Mode.MODE_USUAL) {
            if (actionDown) {
                canvas.drawPath(drawPath, drawPaint);
            }
            GeometricObject current = model.getCurrent();

            if (current != null && actionDown) {
                current.draw(canvas, drawObject, false, false, pointInformations);
                if (textView != null) {
                    textView.setText(current.getLabel());
                }
            } else {
                if (textView != null) {
                    textView.setText("");
                }
            }
        }
        HashMap<String, GeometricObject> geometricObjects = model.getGeometricObjects();

        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() != null && !(entry.getValue() instanceof GeomPoint))
                entry.getValue().draw(canvas, objectPaint, true, mode == Mode.MODE_SELECT, pointInformations);
        }

        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() != null && entry.getValue() instanceof GeomPoint)
                entry.getValue().draw(canvas, objectPaint, true, mode == Mode.MODE_SELECT, pointInformations);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mode == Mode.MODE_MOVE) {
            mScaleGestureDetector.onTouchEvent(event);
        }
        float touchX = event.getX();
        float touchY = event.getY();
        PointF touchPoint = new PointF(touchX, touchY);

        if (mode == Mode.MODE_MOVE) {
            if (mScaleGestureDetector.isInProgress()) {
                return true;
            }
        }


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                switch (mode) {
                    case MODE_USUAL:
                        drawPath.moveTo(touchX, touchY);
                        points.removeAllElements();
                        points.add(touchPoint);
                        actionDown = true;
                        invalidate();
                        break;
                    case MODE_SELECT:
                        model.select(touchX, touchY);
                        invalidate();
                        break;
                    case MODE_MOVE:
                        model.underCurrsor(touchX, touchY);
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_MOVE:

                switch (mode) {
                    case MODE_MOVE:
                        model.translate(touchX, touchY);
                        invalidate();
                        break;
                    case MODE_USUAL:
                        points.add(touchPoint);
                        drawPath.lineTo(touchX, touchY);
                        model.recognize(points);
                        invalidate();
                        break;
                    default:
                        break;
                }
                break;

            case MotionEvent.ACTION_UP:
                switch (mode) {
                    case MODE_MOVE:
                        model.translateFinish(touchX, touchY);
                        unsaved = true;
                        invalidate();
                        break;
                    case MODE_USUAL:
                        actionDown = false;
                        model.drawingFinished(points);
                        drawPath.reset();
                        unsaved = true;
                        invalidate();
                        break;
                    default:
                        break;
                }
                break;
            default:
                return false;
        }

        return true;
    }


    public void clearPanel() {
        model.clear();
        unsaved = true;
        invalidate();
    }


    public void setMode(Mode mode) {
        this.mode = mode;
        invalidate();
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor = detector.getScaleFactor();

            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
            model.update(mScaleFactor, getWidth(), getHeight());
            invalidate();
            return true;
        }
    }


    public void redo() {
        model.redo();
        unsaved = true;
        invalidate();
    }

    public void undo() {
        model.undo();

        unsaved = true;
        invalidate();

    }



    public void setPointInformations(PointInformations pointInformations) {
        this.pointInformations = pointInformations;

        invalidate();
    }

    public void setDensity(double density, int densityDPI) {
        Constants constants = new Constants(density, densityDPI);
        model.setUp(constants, new DiscreteCurvature(constants));
        this.pointInformations = new PointInformations(densityDPI);
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setTrics(HashMap<String, Vector<String>> trics) {
        model.setTrics(trics);
    }

    public void setSenisitivityFactor(float factor) {
        model.setSenisitivity(factor);
    }

    public String save() {
        return model.save();
    }


    public void load(Vector<String> file) {
        model.load(file);
        invalidate();
    }


    public void center() {
        model.center(getWidth(), getHeight());
        invalidate();
    }

}


