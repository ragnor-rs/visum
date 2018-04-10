package io.reist.visum.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import io.reist.visum.model.ViewModel;

/**
 * Created by Reist on 07/04/2017.
 */
public interface ViewModelWidget<VM extends ViewModel> extends UiElementBinder {

    void init(@Nullable AttributeSet attributeSet);

    void bindViewModel(@NonNull VM viewModel);

    @Nullable
    VM getViewModel();

}
