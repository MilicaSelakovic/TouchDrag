package geometry.calculations.descretegeometrycalculations;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class FakeActivity extends Activity {
    private static float density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateDPI();
    }

    public void updateDPI() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        density = getResources().getDisplayMetrics().density;
    }

    public static float getDensity() {
        return density;
    }
}
