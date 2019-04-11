package com.arshilgenius.kisan.agriculture;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class PredictedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NewsAdapter1 mAdapter;
    private ArrayList<Crop> allCropsList;
    private RelativeLayout noPredictions;

    private List<Crop> cropsList;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public PredictedFragment() {
        // Required empty public constructor
    }

    public static PredictedFragment newInstance(String param1) {
        PredictedFragment fragment = new PredictedFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cropsList = getArguments().getParcelableArrayList("data");
            mParam2 = getArguments().getString("testMsg");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_predicted, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        noPredictions = (RelativeLayout) view.findViewById(R.id.no_prediction);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        allCropsList = new ArrayList<Crop>(cropsList);
        mAdapter = new NewsAdapter1(getContext(), null,allCropsList,1);
        mRecyclerView.setAdapter(mAdapter);
        Log.e("Crops List onCreate ->", "" + allCropsList + " and test msg " + mParam2);
        loadPredictions(); //load the predictions
        return view;
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_slide_right);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    public void loadPredictions() {
        if (allCropsList.size() > 0) {
            noPredictions.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setHasFixedSize(true);
            runLayoutAnimation(mRecyclerView);
        } else  {
            mRecyclerView.setVisibility(View.GONE);
            noPredictions.setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
