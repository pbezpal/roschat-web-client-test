package client;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import static chat.ros.testing2.helpers.AttachToReport.*;

public class WatcherTests implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable throwable) {
        String filename = String.valueOf(context.getTestMethod());
        AScreenshot(filename);
        ABrowserLogNetwork();
        ABrowserLogConsole();
    }
}
