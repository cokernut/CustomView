package top.cokernut.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import top.cokernut.customview.R;
import top.cokernut.customview.view.SimpleProgressView;

public class MainActivity extends AppCompatActivity {

    private SimpleProgressView mSPV01;
    private SimpleProgressView mSPV02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSPV01 = (SimpleProgressView) findViewById(R.id.spv_01);
        mSPV02 = (SimpleProgressView) findViewById(R.id.spv_02);
        mSPV01.startAnim();
        mSPV02.setProgressColors(0xFF008888, 0xFF888800, 0xFF880088);
        mSPV02.startAnim();
    }
}
