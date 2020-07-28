package client;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import static chat.ros.testing2.helpers.AttachToReport.*;

public class WatcherTests implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable throwable) {
        String methodName = String.valueOf(context.getTestMethod());
        AScreenshot(methodName);
        ABrowserLogNetwork();
        ABrowserLogConsole();
    }
}
