package com.paulvarry.intra42.tab.project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paulvarry.intra42.ApiService;
import com.paulvarry.intra42.R;
import com.paulvarry.intra42.Tools.DateTool;
import com.paulvarry.intra42.Tools.ProjectUserStatus;
import com.paulvarry.intra42.api.Projects;
import com.paulvarry.intra42.api.ProjectsSessions;
import com.paulvarry.intra42.api.ProjectsUsers;
import com.paulvarry.intra42.api.Skills;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectOverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectOverviewFragment extends Fragment implements View.OnClickListener {

    private Button buttonParent;
    @Nullable
    private ProjectActivity activity;
    private Projects project;
    private ProjectsUsers projectUser;
    private ProjectsSessions session;
    private Button buttonMine;
    private Button buttonRegister;
    private OnFragmentInteractionListener mListener;

    public ProjectOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProjectOverviewFragment.
     */
    public static ProjectOverviewFragment newInstance() {
        return new ProjectOverviewFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (activity != null && activity.projectUser != null) {
            activity = (ProjectActivity) getActivity();
            project = activity.projectUser.project;
            if (activity.projectUser.user != null)
                projectUser = activity.projectUser.user;
            if (project.sessionsList != null && !project.sessionsList.isEmpty())
                session = ProjectsSessions.getScaleForMe(getContext(), project.sessionsList);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { // need to update this (when activity state is restored -> bug)
        super.onCreate(savedInstanceState);

        if (getActivity() != null)
            activity = (ProjectActivity) getActivity();

        if (activity != null && activity.projectUser != null) {
            project = activity.projectUser.project;
            if (activity.projectUser.user != null)
                projectUser = activity.projectUser.user;
            if (project.sessionsList != null && !project.sessionsList.isEmpty())
                session = ProjectsSessions.getScaleForMe(getContext(), project.sessionsList);
        }
    }

    // TODO : add "recommendation": "forbidden"

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FrameLayout frameLayoutStatus = (FrameLayout) view.findViewById(R.id.frameLayoutStatus);
        RelativeLayout linearLayoutMark = (RelativeLayout) view.findViewById(R.id.linearLayoutMark);
        LinearLayout linearLayoutStatus = (LinearLayout) view.findViewById(R.id.linearLayoutStatus);
        FrameLayout frameLayoutRegister = (FrameLayout) view.findViewById(R.id.frameLayoutRegister);
        TextView textViewFinalMark = (TextView) view.findViewById(R.id.textViewFinalMark);
        TextView textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);
        buttonRegister = (Button) view.findViewById(R.id.buttonRegister);

        LinearLayout linearLayoutDescription = (LinearLayout) view.findViewById(R.id.linearLayoutDescription);
        TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        View viewDescriptionLine = view.findViewById(R.id.viewDescriptionLine);
        LinearLayout linearLayoutSkills = (LinearLayout) view.findViewById(R.id.linearLayoutSkills);
        TextView tvSkills = (TextView) view.findViewById(R.id.tvSkills);
        View viewSkillsLine = view.findViewById(R.id.viewSkillsLine);
        LinearLayout linearLayoutInfo = (LinearLayout) view.findViewById(R.id.linearLayoutInfo);
        TextView tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        View viewInfoLine = view.findViewById(R.id.viewInfoLine);
        LinearLayout linearLayoutSession = (LinearLayout) view.findViewById(R.id.linearLayoutSession);
        TextView tvSessionTitle = (TextView) view.findViewById(R.id.tvSessionTitle);
        TextView tvSession = (TextView) view.findViewById(R.id.tvSession);
        View viewSessionLine = view.findViewById(R.id.viewSessionLine);
        LinearLayout linearLayoutObjectives = (LinearLayout) view.findViewById(R.id.linearLayoutObjectives);
        TextView tvObjectives = (TextView) view.findViewById(R.id.tvObjectives);
        View viewObjectivesLine = view.findViewById(R.id.viewObjectivesLine);

        buttonParent = (Button) view.findViewById(R.id.buttonParent);
        buttonMine = (Button) view.findViewById(R.id.buttonMine);

        frameLayoutStatus.setVisibility(View.VISIBLE);
        linearLayoutStatus.setVisibility(View.GONE);
        linearLayoutMark.setVisibility(View.GONE);
        frameLayoutRegister.setVisibility(View.GONE);

        if (project == null) {
            Toast.makeText(getContext(), "Error, please report it", Toast.LENGTH_SHORT).show();
            return;
        }
        if (projectUser == null) { // register
            if (project.recommendation.equals("forbidden")) {
                linearLayoutStatus.setVisibility(View.VISIBLE);
                textViewStatus.setText(getString(R.string.forbidden));
                textViewStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTintCross));
            } else {
                frameLayoutRegister.setVisibility(View.VISIBLE);
                buttonRegister.setOnClickListener(this);
            }
        } else if (projectUser.status.equals(ProjectUserStatus.FINISHED) && projectUser.validated != null && projectUser.finalMark != null) {
            linearLayoutMark.setVisibility(View.VISIBLE);
            textViewFinalMark.setText(String.valueOf(projectUser.finalMark));

            if (projectUser.validated)
                textViewFinalMark.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTintCheck));
            else
                textViewFinalMark.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTintCross));
        } else if (projectUser.status.equals(ProjectUserStatus.FINISHED)) {
            linearLayoutStatus.setVisibility(View.VISIBLE);
            textViewStatus.setText(R.string.no_scale);
        } else if (projectUser.status.equals(ProjectUserStatus.PARENT)) {
            frameLayoutStatus.setVisibility(View.GONE);
        } else {
            linearLayoutStatus.setVisibility(View.VISIBLE);
            textViewStatus.setText(ProjectUserStatus.getProjectStatus(getContext(), projectUser.status));
        }

        if (session == null && project.sessionsList != null && project.sessionsList.size() != 0) {
            linearLayoutSession.setVisibility(View.VISIBLE);
            viewSessionLine.setVisibility(View.VISIBLE);
            tvSession.setText(R.string.no_session_found_for_this_cursus_campus);
        } else if (session == null || session.endAt == null) {
            linearLayoutSession.setVisibility(View.GONE);
            viewSessionLine.setVisibility(View.GONE);
        } else {
            linearLayoutSession.setVisibility(View.VISIBLE);
            viewSessionLine.setVisibility(View.VISIBLE);
            if (DateTool.isInPast(session.endAt))
                tvSessionTitle.setText(R.string.past_session);
            String s = DateTool.getDateTimeLong(session.beginAt) + " - " + DateTool.getDateTimeLong(session.endAt);
            tvSession.setText(s);
        }

        if (project.description == null || project.description.isEmpty()) {
            linearLayoutDescription.setVisibility(View.GONE);
            viewDescriptionLine.setVisibility(View.GONE);
        } else {
            linearLayoutDescription.setVisibility(View.VISIBLE);
            viewDescriptionLine.setVisibility(View.VISIBLE);
            tvDescription.setText(project.description);
        }

        if (project.skills == null || project.skills.isEmpty()) {
            linearLayoutSkills.setVisibility(View.GONE);
            viewSkillsLine.setVisibility(View.GONE);
        } else {
            linearLayoutSkills.setVisibility(View.VISIBLE);
            viewSkillsLine.setVisibility(View.VISIBLE);
            StringBuilder builder = new StringBuilder();
            String currentSeparator = "";
            for (Skills s : project.skills) {
                builder.append(currentSeparator);
                builder.append(s.name);
                // From the second iteration onwards, use this
                currentSeparator = " • ";
            }
            tvSkills.setText(builder);
        }

        linearLayoutInfo.setVisibility(View.VISIBLE);
        viewInfoLine.setVisibility(View.VISIBLE);
        String info = "";
        String separator = "";
        if (project.tier != null) {
            info += separator + "T" + String.valueOf(project.tier);
            separator = " • ";
        }

        if (session.solo)
            info += separator + "solo";
        else
            info += separator + "group";
        separator = " • ";

        if (session.estimateTime != 0) {
            long ago = System.currentTimeMillis() - session.estimateTime * 1000;
            PrettyTime p = new PrettyTime(Locale.getDefault());
            info += separator + p.formatApproximateDuration(new Date(ago));
        }
        if (session != null) {
            ProjectsSessions.Scales scales = ProjectsSessions.Scales.getPrimary(session.scales);
            if (scales != null) {
                info += separator + scales.correctionNumber;
                if (scales.correctionNumber > 1)
                    info += " " + getContext().getString(R.string.scales);
                else
                    info += " " + getContext().getString(R.string.scale);
            }
        }
        tvInfo.setText(info);

        if (project.objectives == null || project.objectives.isEmpty()) {
            linearLayoutObjectives.setVisibility(View.GONE);
            viewObjectivesLine.setVisibility(View.GONE);
        } else {
            linearLayoutObjectives.setVisibility(View.VISIBLE);
            viewObjectivesLine.setVisibility(View.VISIBLE);

            String objectives = "";
            String sep = "";
            for (String s : project.objectives) {
                objectives += sep + s;
                sep = " • ";
            }

            tvObjectives.setText(objectives);
        }

        if (project != null && project.parent != null) {
            buttonParent.setText(project.parent.name);
            buttonParent.setOnClickListener(this);
        } else
            buttonParent.setVisibility(View.GONE);

        if (projectUser != null &&
                !projectUser.status.equals(ProjectUserStatus.PARENT) &&
                projectUser.user != null &&
                activity != null &&
                !projectUser.user.equals(activity.app.me)) {
            buttonMine.setVisibility(View.VISIBLE);
            buttonMine.setOnClickListener(this);
        } else {
            buttonMine.setVisibility(View.GONE);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ProjectActivity)
            activity = (ProjectActivity) context;


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

    @Override
    public void onClick(View v) {
        if (v == buttonParent) {
            if (projectUser != null && projectUser.user != null)
                ProjectActivity.openIt(getContext(), project.parent, projectUser.user.id);
            else
                ProjectActivity.openIt(getContext(), project.parent);
        } else if (v == buttonMine) {
            ProjectActivity.openIt(getContext(), project);
        } else if (v == buttonRegister) {

            if (activity != null) {
                ApiService api = activity.app.getApiService();
                Call<Projects> call = api.createProjectRegister(project.id);
                call.enqueue(new Callback<Projects>() {
                    @Override
                    public void onResponse(Call<Projects> call, retrofit2.Response<Projects> response) {
                        if (response.isSuccessful())
                            Toast.makeText(getContext(), "Success\nDon't forget to refresh", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "Error: " + response.message() + "\nDon't forget to refresh", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Projects> call, Throwable t) {
                        Toast.makeText(getContext(), "Failed: " + t.getMessage() + "\nDon't forget to refresh", Toast.LENGTH_SHORT).show();
                    }
                });
            } else
                Toast.makeText(getContext(), "problem", Toast.LENGTH_LONG).show();
        }
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
