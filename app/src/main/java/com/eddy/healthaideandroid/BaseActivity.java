package com.eddy.healthaideandroid;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qmuiteam.qmui.widget.QMUITopBar;

public abstract class BaseActivity extends AppCompatActivity {

    protected QMUITopBar mTopBar;

    protected String title = "";

    protected View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutId();
        setContentView(root);
        mTopBar = findViewById(R.id.topbar);
        setTile();
        initTopBar();
    }

    abstract protected void setTile();

    abstract protected void setLayoutId();

    private void initTopBar() {
        mTopBar.setBackgroundColor(ContextCompat.getColor(this, R.color.app_color_theme_4));
        mTopBar.setTitle(title);
    }
}
