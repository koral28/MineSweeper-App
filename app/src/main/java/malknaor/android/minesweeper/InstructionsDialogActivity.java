package malknaor.android.minesweeper;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class InstructionsDialogActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions_dialog);

        TextView instructionsTitle = findViewById(R.id.instructions_title);
        TextView instructionsTv = findViewById(R.id.instructions_tv);

        //font in english
        Typeface amaticSC_Regular = Typeface.createFromAsset(getAssets(), "fonts/AmaticSC-Regular.ttf");
        Typeface suezOne_Regular = Typeface.createFromAsset(getAssets(), "fonts/SuezOne-Regular.ttf");

        instructionsTitle.setTypeface(amaticSC_Regular);
        instructionsTv.setTypeface(suezOne_Regular);
    }
}
