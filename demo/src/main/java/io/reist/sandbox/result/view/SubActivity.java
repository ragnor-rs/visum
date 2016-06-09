package io.reist.sandbox.result.view;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reist.sandbox.R;

/**
 * Created by Reist on 07.06.16.
 */
public class SubActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_sub);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.return_btn)
    void onReturnClicked() {
        setResult(RESULT_OK);
        finish();
    }

}
