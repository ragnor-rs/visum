package io.reist.visum.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import org.mockito.Mockito;

import io.reist.visum.presenter.TestPresenter;

/**
 * Created by Reist on 16.06.16.
 */
public class TestVisumAccountAuthenticatorActivity
        extends VisumAccountAuthenticatorActivity<TestPresenter>
        implements VisumConfigurableResultReceiver {

    private static final int REQUEST_CODE = 1;

    private final VisumResultReceiver dummy = Mockito.mock(VisumResultReceiver.class);

    private boolean changingConfigurations;

    private TestPresenter presenter;

    public TestVisumAccountAuthenticatorActivity() {
        super(VisumViewTest.VIEW_ID);
    }

    @Override
    protected int getLayoutRes() {
        return 0;
    }

    @Override
    public TestPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void inject(@NonNull Object from) {
        ((VisumViewTest.TestSubComponent) from).inject(this);
    }

    @Override
    public void setPresenter(TestPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(new View(this));
    }

    @SuppressLint("PrivateResource")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(android.support.v7.appcompat.R.style.Theme_AppCompat);
        super.onCreate(savedInstanceState);
        changingConfigurations = false;
    }

    @Override
    public boolean isChangingConfigurations() {
        return changingConfigurations;
    }

    @Override
    public void setChangingConfigurations(boolean changingConfigurations) {
        this.changingConfigurations = changingConfigurations;
    }

    @Override
    public void startActivityForResult() {
        startActivityForResult(new Intent(this, VisumViewTest.ChildActivity.class), REQUEST_CODE);
    }

    @Override
    public VisumResultReceiver getDummy() {
        return dummy;
    }

    @Override
    public void onActivityResult() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dummy.onActivityResult();
    }

    @Override
    public void attachPresenter() {
        super.attachPresenter();
        dummy.attachPresenter();
    }

}
