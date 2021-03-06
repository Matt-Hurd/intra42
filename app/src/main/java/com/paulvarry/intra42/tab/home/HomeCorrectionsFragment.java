package com.paulvarry.intra42.tab.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.paulvarry.intra42.Adapter.ListAdapterCorrections;
import com.paulvarry.intra42.ApiService;
import com.paulvarry.intra42.Tools.Pagination;
import com.paulvarry.intra42.api.ScaleTeams;
import com.paulvarry.intra42.api.UserLTE;
import com.paulvarry.intra42.tab.user.UserActivity;
import com.paulvarry.intra42.ui.BasicFragmentCall;

import java.util.List;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeCorrectionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeCorrectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeCorrectionsFragment extends BasicFragmentCall<ScaleTeams, ListAdapterCorrections> {

    @Nullable
    private HomeActivity activity;
    private OnFragmentInteractionListener mListener;

    public HomeCorrectionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeCorrectionsFragment.
     */
    public static HomeCorrectionsFragment newInstance() {
        return new HomeCorrectionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (HomeActivity) getActivity();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public Call<List<ScaleTeams>> getCall(ApiService apiService, @Nullable List<ScaleTeams> list) {
        return apiService.getScaleTeamsMe(Pagination.getPage(list));
    }

    @Override
    public void onItemClick(ScaleTeams scaleTeams) {

        if (scaleTeams.corrector != null && activity != null && !scaleTeams.corrector.isMe(activity.app))
            UserActivity.openIt(getContext(), scaleTeams.corrector, activity);
        else if (scaleTeams.correcteds != null && !scaleTeams.correcteds.isEmpty()) {
            boolean correcteds = true;
            for (UserLTE u : scaleTeams.correcteds)
                if (u.isMe(activity.app))
                    correcteds = false;
            if (correcteds)
                UserActivity.openIt(getContext(), scaleTeams.correcteds.get(0), activity);
        }
    }

    @Override
    public ListAdapterCorrections generateAdapter(List<ScaleTeams> list) {
        if (activity != null)
            return new ListAdapterCorrections(activity, list);
        return null;
    }

    @Override
    public String getEmptyMessage() {
        return "you have no correction";
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
