package app.sten.wit;

import android.support.design.widget.Snackbar;
import android.view.View;


public class SnackbarManager {

    private final int MAX_LIFE_TIME = Integer.MAX_VALUE;
    private boolean IsShow;
    private Snackbar snackbar;

    public SnackbarManager() {
        IsShow = false;
    }

    public void Show(View layout, String message, int TextColor) {
        if (IsShow) return;

        snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int a = 1;
                    }
                })
                .setDuration(MAX_LIFE_TIME)
                .setActionTextColor(TextColor);

        snackbar.show();
        IsShow = true;
    }

    public void Dismiss() {
        if (IsShow) {
            snackbar.dismiss();
            IsShow = false;
        }
    }

}
