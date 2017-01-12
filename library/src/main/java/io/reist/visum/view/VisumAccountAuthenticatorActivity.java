package io.reist.visum.view;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.os.Bundle;

import io.reist.visum.presenter.SingleViewPresenter;
import io.reist.visum.presenter.VisumPresenter;

/**
 * This class provides functionality of {@link AccountAuthenticatorActivity} but extends VisumActivity,
 * providing support library and Visum benefits.
 *
 * Created by defuera on 01/02/2016.
 */
public abstract class VisumAccountAuthenticatorActivity<P extends VisumPresenter> extends VisumActivity<P> {

    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;

    @SuppressWarnings("unused")
    public VisumAccountAuthenticatorActivity() {
        this(SingleViewPresenter.DEFAULT_VIEW_ID);
    }

    public VisumAccountAuthenticatorActivity(int viewId) {
        super(viewId);
    }

    /**
     * Set the result that is to be sent as the result of the request that caused this
     * Activity to be launched. If result is null or this method is never called then
     * the request will be canceled.
     *
     * @param result this is returned as the result of the AbstractAccountAuthenticator request
     */
    @SuppressWarnings("unused")
    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

    /**
     * Retreives the AccountAuthenticatorResponse from either the intent of the icicle, if the
     * icicle is non-zero.
     *
     * @param icicle the save instance data of this Activity, may be null
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mAccountAuthenticatorResponse = getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }
    }

    /**
     * Sends the result or a Constants.ERROR_CODE_CANCELED error if a result isn't present.
     */
    public void finish() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED, "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }
        super.finish();
    }

}
