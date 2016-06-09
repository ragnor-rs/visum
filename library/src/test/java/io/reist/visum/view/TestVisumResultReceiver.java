package io.reist.visum.view;

/**
 * Created by Reist on 07.06.16.
 */
interface TestVisumResultReceiver extends TestVisumView {

    void startActivityForResult();

    /**
     * Returns a delegate which is basically a mock of this view to count lifecycle
     * method calls
     */
    TestVisumResultReceiver getDummy();

    void onActivityResult();

}
