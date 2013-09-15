package com.pcosta.guitartuner;

import android.os.Bundle;
import android.app.Activity;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;

public class GuitarTunerActivity extends Activity {

    private PdUiDispatcher dispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            initGui();
            initPd();
            loadPatch();
        } catch (IOException e) {
            e.printStackTrace();
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

    private void initGui() {
        setContentView(R.layout.activity_main);
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
