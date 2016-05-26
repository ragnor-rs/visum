package io.reist.visum;

public abstract class VisumTestCase {

    protected ComponentCache componentCache;

    public void start() {
        componentCache = new ComponentCache();
    }

    public void finish() {
        componentCache = null;
    }

}