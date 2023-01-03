package com.onkar.myvisualization.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.onkar.myvisualization.R;


public class MainFragment extends ListFragment implements AdapterView.OnItemClickListener {


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        show();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void show() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.selection_items, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Audio Visualization")
                    .setMessage("Open any music player and play your favorite songs. Audio Visualizer will detect sound and animate.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, AudioVisualizationFragment.newInstance())
                                    .addToBackStack(null)
                                    .commit();
                        }
                    })
                    .show();
        } else if (position == 1) {
          /*  getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SpeechRecognitionFragment.newInstance())
                    .addToBackStack(null)
                    .commit();*/
        } else if (position == 2) {
           /* getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AudioRecordingFragment.newInstance())
                    .addToBackStack(null)
                    .commit();*/
        }
    }
}