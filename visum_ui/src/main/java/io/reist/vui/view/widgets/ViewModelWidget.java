package io.reist.vui.view.widgets;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import io.reist.vui.model.ViewModel;
import io.reist.vui.view.UiElementBinder;

/**
 * Created by Reist on 07/04/2017.
 */
public interface ViewModelWidget<VM extends ViewModel> extends UiElementBinder {

    void init(@Nullable AttributeSet attributeSet);

    void bindViewModel(@NonNull VM viewModel);

    void onWidgetShow();

    void onWidgetHide();

    @Nullable
    VM getViewModel();

}
