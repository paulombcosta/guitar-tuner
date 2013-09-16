package com.pcosta.guitartuner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

public class GuitarTunerActivity extends RoboActivity {

    private PdUiDispatcher dispatcher;

    @InjectView(R.id.e_button)
    private Button eButton;

    @InjectView(R.id.a_button)
    private Button aButton;

    @InjectView(R.id.d_button)
    private Button dButton;

    @InjectView(R.id.g_button)
    private Button gButton;

    @InjectView(R.id.b_button)
    private Button bButton;

    @InjectView(R.id.ee_button)
    private Button eeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            initGui();
            initPd();
            loadPatch();
        } catch (IOException e) {
            Ln.e(e, "Error");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PdAudio.startAudio(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PdAudio.stopAudio();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PdAudio.release();
        PdBase.release();
    }

    private void initGui() {
        setContentView(R.layout.activity_main);
        eButton.setOnClickListener(onClickListener);
        aButton.setOnClickListener(onClickListener);
        dButton.setOnClickListener(onClickListener);
        gButton.setOnClickListener(onClickListener);
        bButton.setOnClickListener(onClickListener);
        eeButton.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.e_button:
                    triggerNote(40);
                    break;

                case R.id.a_button:
                    triggerNote(45);
                    break;

                case R.id.d_button:
                    triggerNote(50);
                    break;

                case R.id.g_button:
                    triggerNote(55);
                    break;

                case R.id.b_button:
                    triggerNote(59);
                    break;

                case R.id.ee_button:
                    triggerNote(64);
                    break;
            }
        }
    };

    private void triggerNote(int note) {
        PdBase.sendFloat("midinote", note);
        PdBase.sendBang("trigger");
    }

    private void initPd() throws IOException {
        int sampleRate = AudioParameters.suggestSampleRate();
        PdAudio.initAudio(sampleRate,0 ,2, 8, true);
        dispatcher = new PdUiDispatcher();
        PdBase.setReceiver(dispatcher);
    }

    private void loadPatch() throws IOException {
        File dir = getFilesDir();
        IoUtils.extractZipResource(getResources().openRawResource(R.raw.tuner), dir, true);
        File patchFile = new File(dir, "tuner.pd");
        PdBase.openPatch(patchFile.getAbsolutePath());

    }


}
