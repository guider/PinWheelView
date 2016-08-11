package com.yanyuanquan.android.pinwheelview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;


public class PinWheelView extends View implements SensorEventListener {

    private float r = 0;

    private Sensor sensor;
    private SensorManager manager;

    public PinWheelView(Context context) {
        this(context, null);
    }

    public PinWheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
            handler.sendEmptyMessageDelayed(1, 2);
            if (r>3)
                r -= r*0.005;
        }
    };

    private void init() {
        handler.sendEmptyMessageDelayed(1, 20);

        manager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int t = (int)(Math.pow(((int) (event.values[0])), 2) + Math.pow(((int) (event.values[1])), 2) + Math.pow(((int) (event.values[2])), 2));
        if (t>100)
            r += t;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (r > 0)
            canvas.rotate(r % 360, getWidth() / 2, getHeight() / 2);
        canvas.save();
        drawLeaf(canvas);
        canvas.restore();
        super.onDraw(canvas);

    }

    private void drawLeaf(Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        Path path1 = new Path();
        path1.moveTo(0, 0);
        path1.lineTo(0, 300);
        path1.lineTo(480, 300);
        path1.close();
        Path path2 = new Path();
        path2.moveTo(0, 0);
        path2.lineTo(300, 0);
        path2.lineTo(300, -480);
        path2.close();

        Path path3 = new Path();
        path3.moveTo(0, 0);
        path3.lineTo(0, -300);
        path3.lineTo(-480, -300);
        path3.close();

        Path path4 = new Path();
        path4.moveTo(0, 0);
        path4.lineTo(-300, -0);
        path4.lineTo(-300, 480);
        path4.close();


        canvas.drawPath(path1, paint);

        paint.setColor(Color.BLUE);
        canvas.drawPath(path2, paint);

        paint.setColor(Color.YELLOW);
        canvas.drawPath(path3, paint);

        paint.setColor(Color.GREEN);
        canvas.drawPath(path4, paint);

        paint.setColor(Color.parseColor("#ffc4c4c4"));
        canvas.drawCircle(0,0,40,paint);

    }


}
