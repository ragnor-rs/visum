package io.reist.visum.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.mockito.Mockito;

import io.reist.visum.presenter.TestPresenter;

/**
 * Created by Reist on 16.06.16.
 */
public abstract class BaseTestVisumFragment extends VisumFragment<TestPresenter>
        implements VisumResultReceiver {

    private static final int REQUEST_CODE = 1;

    public static final int CONTAINER_ID = 1;

    private final VisumResultReceiver dummy = Mockito.mock(VisumResultReceiver.class);

    private TestPresenter presenter;

    public BaseTestVisumFragment(int viewId) {
        super(viewId);
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

    @SuppressWarnings("ResourceType")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setId(CONTAINER_ID);
        return frameLayout;
    }

    @Override
    public void setPresenter(TestPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void startActivityForResult() {
        startActivityForResult(new Intent(getActivity(), VisumViewTest.ChildActivity.class), REQUEST_CODE);
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
