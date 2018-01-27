package toong.vn.androidsoftkeyboardvisibilitylistener;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

public class MainActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        attachKeyboardListeners();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Rect rectangle = new Rect();
                Window window = getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
                int statusBarHeight = rectangle.top;
                int contentViewTop =
                        window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
                int titleBarHeight= contentViewTop - statusBarHeight;

                Log.i(TAG, "StatusBarHeight: "+statusBarHeight);
                Log.i(TAG, "TitleBarHeight: "+contentViewTop);
            }
        }, 2000);

    }

    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Log.i(TAG, "rootLayout.getRootView().getHeight(): "+rootLayout.getRootView().getHeight());
                    Log.i(TAG, "rootLayout.getHeight(): "+rootLayout.getHeight());

                    int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
                    int contentViewTop =
                            getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
                    Log.i(TAG, "contentViewTop: "+contentViewTop);

                    if (heightDiff <= contentViewTop) {
                        onHideKeyboard();
                    } else {
                        int keyboardHeight = heightDiff - contentViewTop;
                        onShowKeyboard(keyboardHeight);
                    }
                }
            };

    private boolean keyboardListenersAttached = false;
    private View rootLayout;

    protected void attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return;
        }

        rootLayout = findViewById(R.id.rootView);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);



        keyboardListenersAttached = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (keyboardListenersAttached) {
            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
        }
    }

    protected void onShowKeyboard(int keyboardHeight) {
        Log.i(TAG, "keyboard SHOW");
    }

    protected void onHideKeyboard() {
        Log.i(TAG, "keyboard HIDE");
    }
}
