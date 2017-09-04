package no.hiof.larseknu.playingwithfragments.buttonhandlingexample;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import no.hiof.larseknu.playingwithfragments.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ButtonHandlingActivityFragment extends Fragment {
    private int clickCount = 0;
    private TextView textViewClickCountNumber = null;

    public ButtonHandlingActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_handling, container, false);

        textViewClickCountNumber = view.findViewById(R.id.textViewClickCountNumber);
        Button clickMeButton = view.findViewById(R.id.clickMeButton);

        clickMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewClickCountNumber != null)
                    textViewClickCountNumber.setText("" + ++clickCount);
            }
        });

        return view;
    }
}
