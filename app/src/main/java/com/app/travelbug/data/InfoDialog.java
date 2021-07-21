package com.app.travelbug.data;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.travelbug.R;
import com.app.travelbug.data.model.ClusterMarker;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InfoDialog extends BottomSheetDialogFragment {
    private InfoDialogListener mListener;
    private ClusterMarker mMarker;

    private DatabaseHelper mDatabaseHelper;

    private String planName;

    public InfoDialog(ClusterMarker marker){
        mMarker = marker;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View infoPopupView = inflater.inflate(R.layout.info_layout, container, false);

        mDatabaseHelper = new DatabaseHelper(getActivity());
        Spinner dropdown = infoPopupView.findViewById(R.id.info_spinner);

        TextView info_title = (TextView) infoPopupView.findViewById(R.id.info_title);
        TextView info_snippet = (TextView) infoPopupView.findViewById(R.id.info_snippet);
        ImageView info_picture = (ImageView) infoPopupView.findViewById(R.id.info_picture);
        ImageView info_favorite = (ImageView) infoPopupView.findViewById(R.id.info_favorite);
        ImageView info_planner = (ImageView) infoPopupView.findViewById(R.id.info_planner);

        info_title.setText(mMarker.getTitle());
        info_snippet.setText(mMarker.getSnippet());
        info_picture.setImageResource(mMarker.getIconPicture());
        info_favorite.setImageResource(R.drawable.ic_favorite);
        info_planner.setImageResource(R.drawable.ic_planner);

        info_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFavoriteClicked(mMarker);
                dismiss();
            }
        });




        ArrayList<String> arrayList = mDatabaseHelper.getPlans();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(arrayAdapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                planName = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + planName,          Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        info_planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //need to bring up planner options and then add once one clicked
                mDatabaseHelper.addItemToPlan(planName, mMarker.getTitle());

                //mListener.onFavoriteClicked(mMarker);
                dismiss();
            }
        });

        return infoPopupView;
    }

    public interface InfoDialogListener {
        void onFavoriteClicked(ClusterMarker marker);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (InfoDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement InfoDialogListener");
        }

    }
}
