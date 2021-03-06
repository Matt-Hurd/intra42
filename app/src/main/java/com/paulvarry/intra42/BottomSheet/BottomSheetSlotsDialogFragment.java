package com.paulvarry.intra42.BottomSheet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.paulvarry.intra42.ApiService;
import com.paulvarry.intra42.AppClass;
import com.paulvarry.intra42.R;
import com.paulvarry.intra42.Tools.DateTool;
import com.paulvarry.intra42.Tools.SlotsTools;
import com.paulvarry.intra42.api.Slots;
import com.paulvarry.intra42.oauth.ServiceGenerator;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public /*abstract*/ class BottomSheetSlotsDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_SLOTS = "arg_slots";
    private SlotsTools.SlotsGroup slotsGroup;
    private AppClass app;
    private BottomSheetSlotsDialogFragment dialogFragment;

    private boolean isNew = true;

    private TextView textViewTitle;
    private TextView textViewStartDate;
    private TextView textViewStartTime;
    private TextView textViewEndDate;
    private TextView textViewEndTime;
    private TextView textViewError;
    private Button buttonSave;
    private Button buttonDelete;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public static BottomSheetSlotsDialogFragment newInstance(SlotsTools.SlotsGroup slotsGroup) {
        BottomSheetSlotsDialogFragment fragment = new BottomSheetSlotsDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SLOTS, ServiceGenerator.getGson().toJson(slotsGroup));
        fragment.setArguments(args);
        return fragment;
    }

    public static BottomSheetSlotsDialogFragment newInstance() {
        return new BottomSheetSlotsDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogFragment = this;

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_SLOTS)) {
                isNew = false;
                slotsGroup = ServiceGenerator.getGson().fromJson(getArguments().getString(ARG_SLOTS), SlotsTools.SlotsGroup.class);
            }
        }

        app = (AppClass) getActivity().getApplication();
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet_slots, null);
        dialog.setContentView(contentView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        textViewTitle = (TextView) contentView.findViewById(R.id.textViewTitle);
        textViewStartDate = (TextView) contentView.findViewById(R.id.textViewStartDate);
        textViewStartTime = (TextView) contentView.findViewById(R.id.textViewStartTime);
        textViewEndDate = (TextView) contentView.findViewById(R.id.textViewEndDate);
        textViewEndTime = (TextView) contentView.findViewById(R.id.textViewEndTime);
        textViewError = (TextView) contentView.findViewById(R.id.textViewError);
        buttonSave = (Button) contentView.findViewById(R.id.buttonSave);
        buttonDelete = (Button) contentView.findViewById(R.id.buttonDelete);

        if (isNew) {
            textViewTitle.setText(R.string.new_slot);
            buttonSave.setText(R.string.create);
            buttonDelete.setVisibility(View.GONE);

            slotsGroup = new SlotsTools.SlotsGroup();
            slotsGroup.beginAt = new Date(System.currentTimeMillis() + 1800 * 1000);
            slotsGroup.endAt = new Date(System.currentTimeMillis() + 1800 * 1000 + 900 * 1000);

        } else if (slotsGroup.scaleTeam != null || slotsGroup.isBooked) {
            textViewTitle.setText(R.string.booked_slot);
            buttonSave.setVisibility(View.GONE);
            textViewTitle.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorFail));
        } else {
            textViewTitle.setText(R.string.modify_slot);
        }

        setView();

        if (slotsGroup.scaleTeam == null) {
            setDatePicker(textViewStartDate, slotsGroup.beginAt);
            setTimePicker(textViewStartTime, slotsGroup.beginAt);
            setDatePicker(textViewEndDate, slotsGroup.endAt);
            setTimePicker(textViewEndTime, slotsGroup.endAt);
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNew)
                    newSlot();
                else
                    saveSlot();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slotsGroup.scaleTeam == null)
                    deleteSlotFull();
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle(R.string.delete_this_slot);
                    alert.setMessage(R.string.are_you_sure_to_delete_this_slot);

                    alert.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            deleteSlotFull();
                        }
                    });

                    alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
            }
        });


        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    private void setView() {
        textViewStartDate.setText(DateTool.getDateLong(slotsGroup.beginAt));
        textViewStartTime.setText(DateTool.getTimeShort(slotsGroup.beginAt));
        textViewEndDate.setText(DateTool.getDateLong(slotsGroup.endAt));
        textViewEndTime.setText(DateTool.getTimeShort(slotsGroup.endAt));

        if (slotsGroup.beginAt.after(slotsGroup.endAt)) {
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(R.string.slots_start_must_be_before_end);
            buttonSave.setVisibility(View.GONE);
        } else {
            textViewError.setVisibility(View.GONE);
            if (slotsGroup.scaleTeam == null)
                buttonSave.setVisibility(View.VISIBLE);
        }
    }

    private void setDatePicker(TextView textView, final Date date) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.setTime(date);
                DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance(Locale.getDefault());
                        calendar.setTime(date);
                        calendar.set(year, monthOfYear, dayOfMonth);
                        date.setTime(calendar.getTimeInMillis());

                        setView();

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 1800 * 1000);
                pickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1209600 * 1000);
                pickerDialog.setTitle("");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    pickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
                }
                pickerDialog.show();
            }
        });
    }

    private void setTimePicker(TextView textView, final Date date) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.setTime(date);
                TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance(Locale.getDefault());
                        calendar.setTime(date);
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        date.setTime(calendar.getTimeInMillis());

                        setView();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePicker.show();
            }
        });
    }


    private void saveSlot() {
        final ApiService api = app.getApiService();
//        List<Call<?>> call = new ArrayList<>();
        final ProgressDialog dialog = ProgressDialog.show(getContext(), null, getContext().getString(R.string.loading_please_wait), true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = true;
                if (slotsGroup.group != null) {
                    Date groupFirst = slotsGroup.group.get(0).beginAt;
                    if (slotsGroup.beginAt.compareTo(groupFirst) < 0) { //create new before
                        try {
                            api.createSlot(app.me.id, DateTool.getUTC(slotsGroup.beginAt), DateTool.getUTC(groupFirst)).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (slotsGroup.beginAt.compareTo(groupFirst) > 0) { //delete a the begin
                        int i = 0;

                        while (slotsGroup.beginAt.compareTo(slotsGroup.group.get(i).beginAt) > 0 && i < slotsGroup.group.size()) {
                            ApiService api = app.getApiService();
                            Call<Slots> call = api.destroySlot(slotsGroup.group.get(i).id);

                            try {
                                if (!call.execute().isSuccessful())
                                    isSuccess = false;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ++i;
                        }
                    }

                    Date groupLast = slotsGroup.group.get(slotsGroup.group.size() - 1).endAt;
                    if (slotsGroup.endAt.compareTo(groupLast) > 0) { //create new before
                        try {
                            if (!api.createSlot(app.me.id, DateTool.getUTC(groupLast), DateTool.getUTC(slotsGroup.endAt)).execute().isSuccessful())
                                isSuccess = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (slotsGroup.beginAt.compareTo(groupLast) < 0) { //delete a the begin
                        int i = slotsGroup.group.size() - 1;

                        while (slotsGroup.endAt.compareTo(slotsGroup.group.get(i).endAt) < 0 && i >= 0) {
                            ApiService api = app.getApiService();
                            Call<Slots> call = api.destroySlot(slotsGroup.group.get(i).id);

                            try {
                                if (!call.execute().isSuccessful())
                                    isSuccess = false;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            --i;
                        }
                    }

                }
                dialog.cancel();
            }
        }).start();
    }

    private void newSlot() {
        ApiService api = app.getApiService();
        Call<List<Slots>> call = api.createSlot(app.me.id, DateTool.getUTC(slotsGroup.beginAt), DateTool.getUTC(slotsGroup.endAt));

        call.enqueue(new Callback<List<Slots>>() {
            @Override
            public void onResponse(Call<List<Slots>> call, retrofit2.Response<List<Slots>> response) {
                Activity a = getActivity();
                if (a == null)
                    return;
                if (response.isSuccessful()) {
                    Toast.makeText(a, R.string.success, Toast.LENGTH_SHORT).show();
                    dialogFragment.dismiss();
                } else
                    Toast.makeText(a, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Slots>> call, Throwable t) {
                Activity a = getActivity();
                if (a == null)
                    return;
                Toast.makeText(a, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSlotFull() {
        final ProgressDialog dialog = ProgressDialog.show(getContext(), null, getContext().getString(R.string.loading_please_wait), false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (deleteSlot(dialog)) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), R.string.Deleted, Toast.LENGTH_SHORT).show();
                            dialogFragment.dismiss();
                        }
                    });
                }
            }
        }).start();
    }

    private boolean deleteSlot(ProgressDialog dialog) {
        boolean isSuccess = true;
        dialog.setMax(slotsGroup.group.size());
        int i = 0;

        for (Slots slot : slotsGroup.group) {
            dialog.setProgress(i);
            ApiService api = app.getApiService();
            Call<Slots> call = api.destroySlot(slot.id);

            try {
                if (!call.execute().isSuccessful())
                    isSuccess = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            ++i;
        }
        dialog.cancel();
        return isSuccess;
    }

//    abstract void refresh();
}