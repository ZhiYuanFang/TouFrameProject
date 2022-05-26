package xyz.ttyz.tourfrxohc.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Vibrator;
import android.view.View;

import com.shouzhong.scanner.Callback;
import com.shouzhong.scanner.IViewFinder;
import com.shouzhong.scanner.Result;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityIdCardScanBinding;

public class IDCardScanActivity extends BaseTouActivity<ActivityIdCardScanBinding> {

    private Vibrator vibrator;
    ToolBarViewModel toolBarViewModel;

    public static void show() {
        Intent intent = new Intent(ActivityManager.getInstance(), IDCardScanActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_id_card_scan;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        toolBarViewModel = new ToolBarViewModel.Builder().title("扫描身份证").build();
        mBinding.setToolBarViewModel(toolBarViewModel);
        mBinding.setContext(this);

        mBinding.sv.setShouldAdjustFocusArea(true);
        mBinding.sv.setViewFinder(new ViewFinder(this));
        mBinding.sv.setRotateDegree90Recognition(true);
        mBinding.sv.setCallback(new Callback() {
            @Override
            public void result(Result result) {
                /*继续扫描*/
//                startVibrator();
//                mBinding.sv.restartPreviewAfterDelay(2000);
                ScanDetailActivity.show(result.toJsonString());
            }
        });
    }

    private void startVibrator() {
        if (vibrator == null)
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    @Override
    protected void initServer() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        mBinding.sv.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.sv.onPause();
    }

    @Override
    protected void onDestroy() {
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        super.onDestroy();
    }


    private static class ViewFinder extends View implements IViewFinder {
        private Rect framingRect;//扫码框所占区域
        private float widthRatio = 0.9f;//扫码框宽度占view总宽度的比例
        private float heightRatio = 0.8f;
        private float heightWidthRatio = 0.5626f;//扫码框的高宽比
        private int leftOffset = -1;//扫码框相对于左边的偏移量，若为负值，则扫码框会水平居中
        private int topOffset = -1;//扫码框相对于顶部的偏移量，若为负值，则扫码框会竖直居中

        private int laserColor = 0xff008577;// 扫描线颜色
        private int maskColor = 0x60000000;// 阴影颜色
        private int borderColor = 0xff008577;// 边框颜色
        private int borderStrokeWidth = 12;// 边框宽度
        private int borderLineLength = 72;// 边框长度

        private Paint laserPaint;// 扫描线
        private Paint maskPaint;// 阴影遮盖画笔
        private Paint borderPaint;// 边框画笔

        private int position;

        public ViewFinder(Context context) {
            super(context);
            setWillNotDraw(false);//需要进行绘制
            laserPaint = new Paint();
            laserPaint.setColor(laserColor);
            laserPaint.setStyle(Paint.Style.FILL);
            maskPaint = new Paint();
            maskPaint.setColor(maskColor);
            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderStrokeWidth);
            borderPaint.setAntiAlias(true);
        }

        @Override
        protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
            updateFramingRect();
        }

        @Override
        public void onDraw(Canvas canvas) {
            if (getFramingRect() == null) {
                return;
            }
            drawViewFinderMask(canvas);
            drawViewFinderBorder(canvas);
            drawLaser(canvas);
        }

        private void drawLaser(Canvas canvas) {
            Rect framingRect = getFramingRect();
            int top = framingRect.top + 10 + position;
            canvas.drawRect(framingRect.left + 10, top, framingRect.right - 10, top + 5, laserPaint);
            position = framingRect.bottom - framingRect.top - 25 < position ? 0 : position + 2;
            //区域刷新
            postInvalidateDelayed(20, framingRect.left + 10, framingRect.top + 10, framingRect.right - 10, framingRect.bottom - 10);
        }

        /**
         * 绘制扫码框四周的阴影遮罩
         */
        private void drawViewFinderMask(Canvas canvas) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            Rect framingRect = getFramingRect();
            canvas.drawRect(0, 0, width, framingRect.top, maskPaint);//扫码框顶部阴影
            canvas.drawRect(0, framingRect.top, framingRect.left, framingRect.bottom, maskPaint);//扫码框左边阴影
            canvas.drawRect(framingRect.right, framingRect.top, width, framingRect.bottom, maskPaint);//扫码框右边阴影
            canvas.drawRect(0, framingRect.bottom, width, height, maskPaint);//扫码框底部阴影
        }

        /**
         * 绘制扫码框的边框
         */
        private void drawViewFinderBorder(Canvas canvas) {
            Rect framingRect = getFramingRect();

            // Top-left corner
            Path path = new Path();
            path.moveTo(framingRect.left, framingRect.top + borderLineLength);
            path.lineTo(framingRect.left, framingRect.top);
            path.lineTo(framingRect.left + borderLineLength, framingRect.top);
            canvas.drawPath(path, borderPaint);

            // Top-right corner
            path.moveTo(framingRect.right, framingRect.top + borderLineLength);
            path.lineTo(framingRect.right, framingRect.top);
            path.lineTo(framingRect.right - borderLineLength, framingRect.top);
            canvas.drawPath(path, borderPaint);

            // Bottom-right corner
            path.moveTo(framingRect.right, framingRect.bottom - borderLineLength);
            path.lineTo(framingRect.right, framingRect.bottom);
            path.lineTo(framingRect.right - borderLineLength, framingRect.bottom);
            canvas.drawPath(path, borderPaint);

            // Bottom-left corner
            path.moveTo(framingRect.left, framingRect.bottom - borderLineLength);
            path.lineTo(framingRect.left, framingRect.bottom);
            path.lineTo(framingRect.left + borderLineLength, framingRect.bottom);
            canvas.drawPath(path, borderPaint);
        }

        /**
         * 设置framingRect的值（扫码框所占的区域）
         */
        private synchronized void updateFramingRect() {
            Point viewSize = new Point(getWidth(), getHeight());
            int width = getWidth() * 801 / 1080, height = getWidth() * 811 / 1080;
            width = (int) (getWidth() * widthRatio);
//            height = (int) (getHeight() * heightRatio);
            height = (int) (heightWidthRatio * width);

            int left, top;
            if (leftOffset < 0) {
                left = (viewSize.x - width) / 2;//水平居中
            } else {
                left = leftOffset;
            }
            if (topOffset < 0) {
                top = (viewSize.y - height) / 2;//竖直居中
            } else {
                top = topOffset;
            }
            framingRect = new Rect(left, top, left + width, top + height);
        }

        @Override
        public Rect getFramingRect() {
            return framingRect;
        }
    }
}
