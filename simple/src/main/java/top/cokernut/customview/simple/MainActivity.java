package top.cokernut.customview.simple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import top.cokernut.customview.simple.R;
import top.cokernut.customview.view.CellView;
import top.cokernut.customview.view.RippleView;
import top.cokernut.customview.view.SimpleProgressView;
import top.cokernut.customview.view.WifiView;

public class MainActivity extends AppCompatActivity {

    private SimpleProgressView mSPV01;
    private SimpleProgressView mSPV02;
    private WifiView mWV;
    private RippleView mRV;
    private CellView mCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSPV01 = (SimpleProgressView) findViewById(R.id.spv_01);
        mSPV01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HorizontalViewPagerActivity.class));
            }
        });
        mSPV02 = (SimpleProgressView) findViewById(R.id.spv_02);
        mSPV02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FlowLayoutActivity.class));
            }
        });
        mSPV01.startAnim();
        mSPV02.setProgressColors(0xFF06B6F8, 0xFFCDE815, 0xFFED0A70);
        mSPV02.startAnim();
        mWV = (WifiView) findViewById(R.id.wv);
        mWV.startAnim();
        mRV = (RippleView) findViewById(R.id.rv);
        mRV.startAnim();
        mCV = (CellView) findViewById(R.id.cv);
        mCV.startAnim();
    }
}
