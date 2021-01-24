package com.tws.courier.domain.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

/**
 * The type App time picker dialog.
 */
public class AppTimePickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private AppTimePickerDialogListener dialogListener;

    /**
     * Sets dialog listener.
     *
     * @param dialogListener the dialog listener
     */
    public void setDialogListener(AppTimePickerDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    /**
     * Create and show dialog.
     *
     * @param pickerId        the picker id
     * @param fragmentManager the fragment manager
     * @param dialogListener  the dialog listener
     */
    public static void createAndShowDialog(int pickerId, FragmentManager fragmentManager,
                                           @NonNull AppTimePickerDialogListener dialogListener) {
        AppTimePickerDialog datePickerDialog = new AppTimePickerDialog();
        datePickerDialog.setDialogListener(dialogListener);
        datePickerDialog.show(fragmentManager, String.valueOf(pickerId));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (dialogListener != null) dialogListener.onTimeSet(view, hourOfDay, minute);
    }

    /**
     * The interface App time picker dialog listener.
     */
    public interface AppTimePickerDialogListener {
        /**
         * On time set.
         *
         * @param view      the view
         * @param hourOfDay the hour of day
         * @param minute    the minute
         */
        void onTimeSet(TimePicker view, int hourOfDay, int minute);
    }
}