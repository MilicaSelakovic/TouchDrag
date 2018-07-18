package touch.drag.milica.master;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        this.findViewById(R.id.imageButton10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
