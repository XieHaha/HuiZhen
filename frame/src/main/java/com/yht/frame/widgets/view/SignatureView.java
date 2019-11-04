package com.yht.frame.widgets.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/9/2 15:11
 * @description 签名
 */
public class SignatureView extends View {
    /**
     * 笔画X坐标起点
     */
    private float mX;
    /**
     * 笔画Y坐标起点
     */
    private float mY;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 路径
     */
    private Path mPath;
    /**
     * 记录每一个笔画的对象
     */
    private DrawPath mDrawPath;
    /**
     * 签名bitmap画布
     */
    private Canvas mBitmapCanvas;
    /**
     * 签名位图
     */
    private Bitmap mBitmap;
    /**
     * 画笔宽度
     */
    private int mPaintWidth = 6;
    /**
     * 笔画颜色
     */
    private int mPaintColor = Color.BLUE;
    /**
     * 背景色
     */
    private int mBgColor = Color.TRANSPARENT;
    /**
     * 点位计算   须大于30个点才保存
     */
    private int num = 0;
    /**
     * mSavePath：保存笔画的集合，list有序保存;mDeletePath：撤销的笔画
     */
    private List<DrawPath> mSavePath, mDeletePath;

    /**
     * 笔画路径和画笔储存
     */
    public class DrawPath {
        /**
         * 路径
         */
        Path path;
        /**
         * 画笔
         */
        Paint paint;
    }

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
        setPaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initCanvas();
        mSavePath = new ArrayList<>();
        mDeletePath = new ArrayList<>();
    }

    /**
     * 设置画笔
     */
    private void setPaint() {
        mPaint = new Paint();
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置签名笔画样式
        mPaint.setStyle(Paint.Style.STROKE);
        //设置笔画宽度
        mPaint.setStrokeWidth(mPaintWidth);
        //设置签名颜色
        mPaint.setColor(mPaintColor);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * 初始化bitmap、画布
     */
    private void initCanvas() {
        //创建跟view一样大的bitmap，用来保存签名
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBitmapCanvas = new Canvas(mBitmap);
        mBitmapCanvas.drawColor(mBgColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                //将路径画到bitmap中，即一次笔画完成才去更新bitmap，而手势轨迹是实时显示在画板上的。
                mBitmapCanvas.drawPath(mPath, mPaint);
                //将一条完整的路径保存下来(相当于入栈操作)
                mSavePath.add(mDrawPath);
                // 重新置空
                mPath = null;
                break;
            default:
                break;
        }
        // 更新绘制
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画此次笔画之前的签名
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        // 通过画布绘制多点形成的图形, 当手指滑动时也实时画上
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }
    }

    /**
     * 手指点下屏幕时调用
     */
    private void touchDown(MotionEvent event) {
        // 每次按下都是新的一笔，创建新的path
        mPath = new Path();
        float x = event.getX();
        float y = event.getY();
        mX = x;
        mY = y;
        // mPath绘制的绘制起点
        mPath.moveTo(x, y);
        //新的笔画存在新的对象里，方便撤回操作
        mDrawPath = new DrawPath();
        mDrawPath.path = mPath;
        mDrawPath.paint = mPaint;
    }

    /**
     * 手指在屏幕上滑动时调用
     */
    private void touchMove(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        final float previousX = mX;
        final float previousY = mY;
        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);
        // 两点之间的距离大于等于3时，生成贝塞尔绘制曲线
        if (dx >= 3 || dy >= 3) {
            // 设置贝塞尔曲线的操作点为起点和终点的一半
            float cX = (x + previousX) / 2;
            float cY = (y + previousY) / 2;
            // 二次贝塞尔，实现平滑曲线；previousX, previousY为操作点，cX, cY为终点
            mPath.quadTo(previousX, previousY, cX, cY);
            // 第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
            mX = x;
            mY = y;
            num++;
        }
    }

    /**
     * 撤销上一步
     */
    public void goBack() {
        if (mSavePath != null && mSavePath.size() > 0) {
            DrawPath drawPath = mSavePath.get(mSavePath.size() - 1);
            mDeletePath.add(drawPath);
            mSavePath.remove(mSavePath.size() - 1);
            redrawBitmap();
        }
    }

    /**
     * 前进
     */
    public void goForward() {
        if (mDeletePath != null && mDeletePath.size() > 0) {
            DrawPath drawPath = mDeletePath.get(mDeletePath.size() - 1);
            mSavePath.add(drawPath);
            mDeletePath.remove(mDeletePath.size() - 1);
            redrawBitmap();
        }
    }

    /**
     * 是否有签名,根据是否有笔画来判断
     */
    public boolean hasDraw() {
        return mSavePath != null && num > 30;
    }

    /**
     * 重画bitmap
     */
    private void redrawBitmap() {
        initCanvas();
        for (DrawPath drawPath : mSavePath) {
            mBitmapCanvas.drawPath(drawPath.path, drawPath.paint);
        }
        invalidate();// 刷新
    }

    /**
     * 清除画板
     */
    public void clear() {
        if (mSavePath != null && mSavePath.size() > 0) {
            num = 0;
            mSavePath.clear();
            mDeletePath.clear();
            redrawBitmap();
        }
    }

    /**
     * 保存bitmap到本地
     */
    @SuppressLint("WrongThread")
    public void saveBitmap(String path) {
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        Bitmap bitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
        File filePic;
        try {
            filePic = new File(path);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取画板的bitmap
     */
    public Bitmap getBitMap() {
        Bitmap bitmap = getDrawingCache();
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        destroyDrawingCache();
        return result;
    }

    /**
     * 逐行扫描 清楚边界空白。
     *
     * @param blank 边距留多少个像素
     */
    private Bitmap clearBlank(Bitmap bp, int blank) {
        int height = bp.getHeight();
        int width = bp.getWidth();
        int top = 0, left = 0, right = 0, bottom = 0;
        int[] pixs = new int[width];
        boolean isStop;
        //扫描上边距不等于背景颜色的第一个点
        for (int y = 0; y < height; y++) {
            bp.getPixels(pixs, 0, width, 0, y, width, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBgColor) {
                    top = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        //扫描下边距不等于背景颜色的第一个点
        for (int y = height - 1; y >= 0; y--) {
            bp.getPixels(pixs, 0, width, 0, y, width, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBgColor) {
                    bottom = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        pixs = new int[height];
        //扫描左边距不等于背景颜色的第一个点
        for (int x = 0; x < width; x++) {
            bp.getPixels(pixs, 0, 1, x, 0, 1, height);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBgColor) {
                    left = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        //扫描右边距不等于背景颜色的第一个点
        for (int x = width - 1; x > 0; x--) {
            bp.getPixels(pixs, 0, 1, x, 0, 1, height);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBgColor) {
                    right = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        if (blank < 0) {
            blank = 0;
        }
        //计算加上保留空白距离之后的图像大小
        left = left - blank > 0 ? left - blank : 0;
        top = top - blank > 0 ? top - blank : 0;
        right = right + blank > width - 1 ? width - 1 : right + blank;
        bottom = bottom + blank > height - 1 ? height - 1 : bottom + blank;
        return Bitmap.createBitmap(bp, left, top, right - left, bottom - top);
    }

    /**
     * 设置画笔宽度 默认宽度为10px
     * 这边设置可以对接下来的笔画生效
     */
    public void setPaintWidth(int mPaintWidth) {
        mPaintWidth = mPaintWidth > 0 ? mPaintWidth : 10;
        this.mPaintWidth = mPaintWidth;
        setPaint();
    }

    public void setBgColor(@ColorInt int backColor) {
        mBgColor = backColor;
    }

    /**
     * 设置画笔颜色
     * 这边设置可以对接下来的笔画生效
     *
     * @param paintColor 画笔颜色
     */
    public void setPaintColor(int paintColor) {
        this.mPaintColor = paintColor;
        setPaint();
    }
}
