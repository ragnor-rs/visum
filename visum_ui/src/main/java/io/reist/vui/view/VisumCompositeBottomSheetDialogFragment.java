package io.reist.vui.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reist.visum.presenter.VisumPresenter;
import io.reist.visum.view.VisumBottomSheetDialogFragment;

/**
 * Prevents client code from overriding standard Android lifecycle methods.
 * This is to minimize bugs regarding view attachment. For proper initialization / cleanup, use
 * {@link #init(Context, Bundle)}, {@link #attachPresenter()} and {@link #detachPresenter()}.
 *
 * Created by Reist on 15.03.17.
 */
public abstract class VisumCompositeBottomSheetDialogFragment<P extends VisumPresenter>
        extends VisumBottomSheetDialogFragment<P>
        implements VisumCompositeView<P> {

    private Unbinder unbinder;

    private void bindViews(View view) {
        if (unbinder == null && view != null) {
            unbinder = ButterKnife.bind(this, view);
        }
    }

    @Override
    public final void bindUiElements() {
        bindViews(getView());
    }

    @Override
    public final void unbindUiElements() {
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Override
    public final void rebindUiElements() {
        unbindUiElements();
        bindUiElements();
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        bindViews(view);
        init(getContext(), savedInstanceState);
        return view;
    }

    @Override
    public final void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public final void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public final void onResume() {
        super.onResume();
    }

    @Override
    public final void onPause() {
        super.onPause();
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
    }

    @Override
    public final void attachPresenter() {
        if (getActivity() != null && !getActivity().isFinishing()) {

            bindViews(getView());
            super.attachPresenter();
            onPresenterAttached(getPresenter());

        }
    }

    @Override
    public final void detachPresenter() {
        onPresenterDetached();
        super.detachPresenter();
        unbindUiElements();
    }

    @CallSuper
    @Override
    public void init(Context context, Bundle savedInstanceState) {}

    @CallSuper
    @Override
    public void onPresenterAttached(P presenter) {}

    @CallSuper
    @Override
    public void onPresenterDetached() {}

}
