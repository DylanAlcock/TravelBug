package com.app.travelbug.data;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.travelbug.R;
import com.app.travelbug.data.model.ClusterMarker;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InfoDialog extends BottomSheetDialogFragment {
    private InfoDialogListener mListener;
    private ClusterMarker mMarker;



    public InfoDialog(ClusterMarker marker){
        mMarker = marker;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View infoPopupView = inflater.inflate(R.layout.info_layout, container, false);

        TextView info_title = (TextView) infoPopupView.findViewById(R.id.info_title);
        TextView info_snippet = (TextView) infoPopupView.findViewById(R.id.info_snippet);
        ImageView info_picture = (ImageView) infoPopupView.findViewById(R.id.info_picture);
        ImageView info_favorite = (ImageView) infoPopupView.findViewById(R.id.info_favorite);

        info_title.setText(mMarker.getTitle());
        info_snippet.setText(mMarker.getSnippet());
        info_picture.setImageResource(mMarker.getIconPicture());
        info_favorite.setImageResource(R.drawable.ic_favorite);

        info_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFavoriteClicked(mMarker);
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
