package com.tonicartos.superslimexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tonicartos.superslim.GridSectionLayoutManager;
import com.tonicartos.superslim.LayoutManager;
import com.tonicartos.superslim.LinearSectionLayoutManager;
import com.tonicartos.superslim.SectionLayoutManager;

import java.util.Random;

/**
 * Fragment that displays a list of country names.
 */
public class CountriesFragment extends Fragment {

    private static final java.lang.String KEY_HEADER_MODE = "key_header_mode";

    private static final java.lang.String KEY_HEADERS_STICKY = "key_headers_sticky";

    private static final java.lang.String KEY_MARGINS_FIXED = "key_margins_fixed";

    private ViewHolder mViews;

    private CountryNamesAdapter mAdapter;

    private int mHeaderMode;

    private boolean mAreHeadersSticky;

    private boolean mAreMarginsFixed;

    private Random mRng = new Random();

    private Toast mToast = null;

    private GridSectionLayoutManager mGridSectionLayoutManager;

    private SectionLayoutManager mLinearSectionLayoutManager;

    private LayoutManager.SlmFactory mSlmFactory = new LayoutManager.SlmFactory() {

        @Override
        public SectionLayoutManager getSectionLayoutManager(LayoutManager layoutManager,
                int section) {
            int sectionKind = section % 2;
            final SectionLayoutManager slm;
            if (sectionKind == 0) {
                GridSectionLayoutManager grid = mGridSectionLayoutManager;
                slm = mGridSectionLayoutManager;
            } else {
                slm = mLinearSectionLayoutManager;
            }
            return slm;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mHeaderMode = savedInstanceState
                    .getInt(KEY_HEADER_MODE,
                            getResources().getInteger(R.integer.default_header_alignment));
            mAreHeadersSticky = savedInstanceState
                    .getBoolean(KEY_HEADERS_STICKY,
                            getResources().getBoolean(R.bool.default_headers_sticky));
            mAreMarginsFixed = savedInstanceState
                    .getBoolean(KEY_MARGINS_FIXED,
                            getResources().getBoolean(R.bool.default_margins_fixed));
        } else {
            mHeaderMode = getResources().getInteger(R.integer.default_header_alignment);
            mAreHeadersSticky = getResources().getBoolean(R.bool.default_headers_sticky);
            mAreMarginsFixed = getResources().getBoolean(R.bool.default_margins_fixed);
        }

        LayoutManager lm = new LayoutManager();
        mGridSectionLayoutManager = new GridSectionLayoutManager(lm, getActivity());
        mGridSectionLayoutManager.setColumnMinimumWidth((int) getResources()
                .getDimension(R.dimen.grid_column_width));
        mLinearSectionLayoutManager = new LinearSectionLayoutManager(lm);
        lm.setSlmFactory(mSlmFactory);

        mViews = new ViewHolder(view);
        mViews.initViews(getActivity(), lm);
        mAdapter = new CountryNamesAdapter(getActivity(), mHeaderMode);
        mAdapter.setHeadersSticky(mAreHeadersSticky);
        mAdapter.setMarginsFixed(mAreMarginsFixed);
        mAdapter.setHeaderMode(mHeaderMode);
        mViews.setAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_HEADER_MODE, mHeaderMode);
        outState.putBoolean(KEY_HEADERS_STICKY, mAreHeadersSticky);
        outState.putBoolean(KEY_MARGINS_FIXED, mAreMarginsFixed);
    }

    public void setHeaderMode(int mode) {
        mHeaderMode = mode;
        mAdapter.setHeaderMode(mode);
    }

    public int getHeaderMode() {
        return mHeaderMode;
    }

    public boolean areHeadersSticky() {
        return mAreHeadersSticky;
    }

    public boolean areMarginsFixed() {
        return mAreMarginsFixed;
    }

    public void setHeadersSticky(boolean areHeadersSticky) {
        mAreHeadersSticky = areHeadersSticky;
        mAdapter.setHeadersSticky(areHeadersSticky);
    }

    public void setMarginsFixed(boolean areMarginsFixed) {
        mAreMarginsFixed = areMarginsFixed;
        mAdapter.setMarginsFixed(areMarginsFixed);
    }

    public void scrollToRandomPosition() {
        int position = mRng.nextInt(mAdapter.getItemCount());
        String s = "Scroll to position " + position
                + (mAdapter.isItemHeader(position) ? ", header " : ", item ")
                + mAdapter.itemToString(position) + ".";
        if (mToast != null) {
            mToast.setText(s);
        } else {
            mToast = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
        }
        mToast.show();
        mViews.scrollToPosition(position);
    }

    public void smoothScrollToRandomPosition() {
        int position = mRng.nextInt(mAdapter.getItemCount());
        String s = "Smooth scroll to position " + position
                + (mAdapter.isItemHeader(position) ? ", header " : ", item ")
                + mAdapter.itemToString(position) + ".";
        if (mToast != null) {
            mToast.setText(s);
        } else {
            mToast = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
        }
        mToast.show();
        mViews.smoothScrollToPosition(position);
    }

    private static class ViewHolder {

        private final RecyclerView mRecyclerView;


        public ViewHolder(View view) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        }

        public void initViews(Context context, LayoutManager lm) {
            mRecyclerView.setLayoutManager(lm);
        }

        public void setAdapter(RecyclerView.Adapter<?> adapter) {
            mRecyclerView.setAdapter(adapter);
        }

        public void scrollToPosition(int position) {
            mRecyclerView.scrollToPosition(position);
        }

        public void smoothScrollToPosition(int position) {
            mRecyclerView.smoothScrollToPosition(position);
        }
    }
}
