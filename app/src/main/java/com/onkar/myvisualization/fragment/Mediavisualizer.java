package com.onkar.myvisualization.fragment;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.onkar.myvisualization.R;

import java.util.concurrent.TimeUnit;


public class Mediavisualizer extends Fragment {

    private Button b2;
    private Button b3;
    private MediaPlayer mediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;

    private final Handler myHandler = new Handler();
    private final int forwardTime = 5000;
    private final int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1;
    private TextView tx2;

    public static int oneTimeOnly = 0;
    public Mediavisualizer() {
        // Required empty public constructor
    }

    private AudioVisualization audioVisualization;

    public static androidx.fragment.app.Fragment newInstance() {
        return new Mediavisualizer();
    }


    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mediavisualizer, container, false);
        Button b1 = (Button) view.findViewById(R.id.button);
        b2 = (Button)view. findViewById(R.id.button2);
        b3 = (Button)view.findViewById(R.id.button3);
        Button b4 = (Button) view.findViewById(R.id.button4);


        tx1 = (TextView)view.findViewById(R.id.textView2);
        tx2 = (TextView)view.findViewById(R.id.textView3);
        TextView tx3 = (TextView) view.findViewById(R.id.textView4);
        audioVisualization = (GLAudioVisualizationView)view.findViewById(R.id.visualizer_view);
        tx3.setText(R.string.Song);

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.sample);
        seekbar=view.findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        b2.setEnabled(false);
        b3.setOnClickListener(v -> {
            Toast.makeText(requireActivity(), "Playing"+
                    "sound",Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();

            finalTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();

            if (oneTimeOnly == 0) {
                seekbar.setMax((int) finalTime);
                oneTimeOnly = 1;
            }

            tx2.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    finalTime)))
            );

            tx1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    startTime)))
            );

            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(UpdateSongTime,100);
            b2.setEnabled(true);
            b3.setEnabled(false);
        });

        b2.setOnClickListener(v -> {
            Toast.makeText(requireActivity(), "Pausing"+
                    "sound",Toast.LENGTH_SHORT).show();
                    mediaPlayer.pause();
            b2.setEnabled(false);
            b3.setEnabled(true);
        });

        b1.setOnClickListener(v -> {
            int temp = (int)startTime;

            if((temp+forwardTime)<=finalTime){
                startTime = startTime + forwardTime;
                mediaPlayer.seekTo((int) startTime);
                Toast.makeText(getActivity(),"You have Jumped forward 5"+
                        "seconds",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(requireActivity(),"Cannot jump forward 5"+
                        "seconds",Toast.LENGTH_SHORT).show();
            }
        });

        b4.setOnClickListener(v -> {
            int temp = (int)startTime;

            if((temp-backwardTime)>0){
                startTime = startTime - backwardTime;
                mediaPlayer.seekTo((int) startTime);
                Toast.makeText(requireActivity(),"You have Jumped backward 5"
                        +"seconds",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(requireActivity(),"Cannot jump backward 5"+
                        "seconds",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // audioVisualization = (AudioVisualization) view;
        audioVisualization.linkTo(DbmHandler.Factory.newVisualizerHandler(requireContext(), 0));
    }


    private final Runnable UpdateSongTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tx1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        audioVisualization.onResume();
    }

    @Override
    public void onPause() {
        audioVisualization.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        audioVisualization.release();
        super.onDestroyView();
    }
}