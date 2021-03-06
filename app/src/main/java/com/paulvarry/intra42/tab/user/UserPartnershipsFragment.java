package com.paulvarry.intra42.tab.user;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.paulvarry.intra42.Adapter.ListAdapterPartnerships;
import com.paulvarry.intra42.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserPartnershipsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserPartnershipsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPartnershipsFragment extends Fragment {

    UserActivity activity;
    ListView listView;
    TextView textViewNothingToShow;
    private OnFragmentInteractionListener mListener;

    public UserPartnershipsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserPartnershipsFragment.
     */
    public static UserPartnershipsFragment newInstance() {
        return new UserPartnershipsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (UserActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_partnerships, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.listView);
        textViewNothingToShow = (TextView) view.findViewById(R.id.textViewNothingToShow);

        if (activity.user.partnerships != null && activity.user.partnerships.size() != 0) {
            ListAdapterPartnerships adapterPartnerships = new ListAdapterPartnerships(activity, activity.user.partnerships);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapterPartnerships);

            textViewNothingToShow.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            textViewNothingToShow.setVisibility(View.VISIBLE);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
