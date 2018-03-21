package com.jiubao.componentes.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/21.
 * 版本:
 */

public class DragImageView extends AppCompatImageView{
    private Matrix matrix = new Matrix();
    private float imageWidth;
    private float imageHeight;
    private GestureDetector gestureDetector;

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MatrixTouchListener listener = new MatrixTouchListener();
        setOnTouchListener(listener);
        gestureDetector = new GestureDetector(getContext(),new GestureListener(listener));
        setBackgroundColor(Color.BLACK);
        setScaleType(ScaleType.FIT_CENTER);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        matrix.set(getImageMatrix());
        float[] values = new float[9];
        matrix.getValues(values);
        imageWidth = getWidth() / values[Matrix.MSCALE_X];
        imageHeight = (getHeight() - values[Matrix.MSCALE_Y] * 2 ) / values[Matrix.MSCALE_X];
    }

    public class MatrixTouchListener implements OnTouchListener{

        private static final int MODE_DRAG = 1;
        private static final int MODE_ZOOM = 2;
        private static final int MODE_UNABLE = 3;
        float maxScale = 6;
        float doubleClickScale = 2;
        private int mode = 0;
        private float startDis ;
        private Matrix currentMatrix = new Matrix();
        private PointF startPoint = new PointF();

        private void isMartixEnable(){
            if(getScaleType() != ScaleType.CENTER){
                setScaleType(ScaleType.MATRIX);
            }else{
                mode = MODE_UNABLE;//设置为不支持手势
            }
        }

        private boolean checkRest(){
            float[] values = new float[9];
            getImageMatrix().getValues(values);
            //获取当前X轴缩放级别
            float scale = values[Matrix.MSCALE_X];
            //获取模板的X轴缩放级别，两者做比较
            matrix.getValues(values);
            return scale < values[Matrix.MSCALE_X];
        }

        private void reSetMatrix(){
            if(checkRest()){
                currentMatrix.set(matrix);
                setImageMatrix(currentMatrix);
            }
        }

        private float distance(MotionEvent event){
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            return (float)Math.sqrt(dx* dx + dy * dy);
        }

        private float checkMaxScale(float scale,float[] values){
            if(scale * values[Matrix.MSCALE_X] > maxScale)
                scale = maxScale / values[Matrix.MSCALE_X];
            currentMatrix.postScale(scale,scale,getWidth() / 2,getHeight() / 2);
            return scale;
        }

        private void setZoomMatrix(MotionEvent event){
            if(event.getPointerCount() < 2)
                return;
            float endDis = distance(event);
            if(endDis > 10f){
                float scale = endDis / startDis;
                startDis = endDis;
                currentMatrix.set(getImageMatrix());
                float[] values = new float[9];
                currentMatrix.getValues(values);
                scale = checkMaxScale(scale,values);
                setImageMatrix(currentMatrix);
            }
        }

        private boolean isZoomChanged(){
            float[] values = new float[9];
            getImageMatrix().getValues(values);
            float scale = values[Matrix.MSCALE_X];
            matrix.getValues(values);
            return scale != values[Matrix.MSCALE_X];
        }

        private float checkDxBound(float[] values,float value){
            float width = getWidth();
            if(imageWidth * values[Matrix.MSCALE_X] < width){
                return 0;
            }
            if(values[Matrix.MSCALE_X] + value > 0){
                value = -values[Matrix.MSCALE_X];
            }else if(values[Matrix.MTRANS_X] + value < -(imageWidth * values[Matrix.MSCALE_X] - width)){
                value = -(imageWidth * values[Matrix.MSCALE_X] - width) - values[Matrix.MTRANS_X];
            }
            return value;
        }

        private void setDrawMatrix(MotionEvent event){
            if(isZoomChanged()){
                float dx = event.getX() - startPoint.x;
                float dy = event.getY() - startPoint.y;
                if(Math.sqrt(dx * dx + dy * dy) > 10f){
                    startPoint.set(event.getX(),event.getY());
                    currentMatrix.set(getImageMatrix());
                    float[] values = new float[9];
                    currentMatrix.getValues(values);
                    dx = checkDxBound(values,dx);
                    dy = checkDxBound(values,dy);
                    currentMatrix.postTranslate(dx,dy);
                    setImageMatrix(currentMatrix);
                }
            }
        }

        public void onDoubleClick(){
            float scale = isZoomChanged() ? 1 : doubleClickScale;
            currentMatrix.set(matrix);
            currentMatrix.postScale(scale,scale,getWidth() / 2,getHeight() / 2);
            setImageMatrix(currentMatrix);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_DRAG;
                    startPoint.set(motionEvent.getX(),motionEvent.getY());
                    isMartixEnable();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    reSetMatrix();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(mode == MODE_ZOOM){
                        setZoomMatrix(motionEvent);
                    }else if(mode == MODE_DRAG){
                        setDrawMatrix(motionEvent);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    if(mode == MODE_UNABLE) return true;
                    mode = MODE_ZOOM;
                    startDis = distance(motionEvent);
                    break;
                default:
                    break;
            }
            return gestureDetector.onTouchEvent(motionEvent);
        }
    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private final MatrixTouchListener matrixTouchListener;

        public GestureListener(MatrixTouchListener matrixTouchListener) {
            this.matrixTouchListener = matrixTouchListener;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
            //捕获事件
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            matrixTouchListener.onDoubleClick();
            return true;
        }
    }
}
