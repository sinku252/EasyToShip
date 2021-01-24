package com.tws.courier.domain.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;
import java.util.Date;

/**
 * The type App date picker dialog.
 */
public class AppDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private AppDatePickerDialogListener dialogListener;
    private boolean shouldDisablePastDates = false, shouldDisableUpcomingDates = false;
    private Date startDate;
    private Date endDate;
    private Date currentDate;

    /**
     * Sets dialog listener.
     *
     * @param dialogListener the dialog listener
     */
    public void setDialogListener(AppDatePickerDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    /**
     * Sets should disable past dates.
     *
     * @param shouldDisablePastDates the should disable past dates
     */
    public void setShouldDisablePastDates(boolean shouldDisablePastDates) {
        this.shouldDisablePastDates = shouldDisablePastDates;
    }

    public void setShouldDisableUpcomingDates(boolean shouldDisableUpcomingDates) {
        this.shouldDisableUpcomingDates = shouldDisableUpcomingDates;
    }

    /**
     * Sets start date.
     *
     * @param startDate the start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Sets end date.
     *
     * @param endDate the end date
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * Create and show dialog.
     *
     * @param pickerId               the picker id
     * @param shouldDisablePastDates the should disable past dates
     * @param fragmentManager        the fragment manager
     * @param dialogListener         the dialog listener
     */
    public static void createAndShowDialog(int pickerId, boolean shouldDisablePastDates, FragmentManager fragmentManager,
                                           @NonNull AppDatePickerDialogListener dialogListener) {
        AppDatePickerDialog datePickerDialog = new AppDatePickerDialog();
        datePickerDialog.setDialogListener(dialogListener);
        datePickerDialog.setShouldDisablePastDates(shouldDisablePastDates);
        datePickerDialog.show(fragmentManager, String.valueOf(pickerId));
    }

    public static void createAndShowDialog(int pickerId, boolean shouldDisablePastDates, Date currentDate, FragmentManager fragmentManager,
                                           @NonNull AppDatePickerDialogListener dialogListener) {
        AppDatePickerDialog datePickerDialog = new AppDatePickerDialog();
        datePickerDialog.setDialogListener(dialogListener);
        if (currentDate != null) datePickerDialog.setCurrentDate(currentDate);
        datePickerDialog.setShouldDisablePastDates(shouldDisablePastDates);
        datePickerDialog.show(fragmentManager, String.valueOf(pickerId));
    }

//    public static void createAndShowDialogDob(int pickerId, boolean shouldDisableUpcomingDates, Date currentDate, FragmentManager fragmentManager,
//                                           @NonNull AppDatePickerDialogListener dialogListener) {
//        AppDatePickerDialog datePickerDialog = new AppDatePickerDialog();
//        datePickerDialog.setDialogListener(dialogListener);
//        if (currentDate != null) datePickerDialog.setCurrentDate(currentDate);
//        datePickerDialog.setShouldDisableUpcomingDates(shouldDisableUpcomingDates);
//        datePickerDialog.show(fragmentManager, String.valueOf(pickerId));
//    }

    /**
     * Create and show dialog.
     *
     * @param pickerId        the picker id
     * @param startDate       the start date
     * @param endDate         the end date
     * @param fragmentManager the fragment manager
     * @param dialogListener  the dialog listener
     */
    public static void createAndShowDialog(int pickerId, Date startDate, Date endDate, FragmentManager fragmentManager,
                                           @NonNull AppDatePickerDialogListener dialogListener) {
        AppDatePickerDialog datePickerDialog = new AppDatePickerDialog();
        datePickerDialog.setDialogListener(dialogListener);
        if (startDate != null) datePickerDialog.setStartDate(startDate);
        if (endDate != null) datePickerDialog.setEndDate(endDate);
        datePickerDialog.show(fragmentManager, String.valueOf(pickerId));
    }

    public static void createAndShowDialog(int pickerId, Date startDate, Date endDate, Date currentDate,
                                           FragmentManager fragmentManager,
                                           @NonNull AppDatePickerDialogListener dialogListener) {
        AppDatePickerDialog datePickerDialog = new AppDatePickerDialog();
        datePickerDialog.setDialogListener(dialogListener);
        if (startDate != null) datePickerDialog.setStartDate(startDate);
        if (endDate != null) datePickerDialog.setEndDate(endDate);
        if (currentDate != null) datePickerDialog.setCurrentDate(currentDate);
        datePickerDialog.show(fragmentManager, String.valueOf(pickerId));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        if (shouldDisablePastDates)
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
        if (shouldDisableUpcomingDates)
            dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis() - 1000);
        if (startDate != null) dialog.getDatePicker().setMinDate(startDate.getTime());
        if (endDate != null) dialog.getDatePicker().setMaxDate(endDate.getTime());

        if (currentDate != null) {
            final Calendar c1 = Calendar.getInstance();
            c1.setTime(currentDate);
            int year1 = c1.get(Calendar.YEAR);
            int month1 = c1.get(Calendar.MONTH);
            int day1 = c1.get(Calendar.DAY_OF_MONTH);
            dialog.getDatePicker().updateDate(year1, month1, day1);
        }
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (dialogListener != null) dialogListener.onDateSet(view, year, month, day);
    }

    /**
     * The interface App date picker dialog listener.
     */
    public interface AppDatePickerDialogListener {
        /**
         * On date set.
         *
         * @param view  the view
         * @param year  the year
         * @param month the month
         * @param day   the day
         */
        void onDateSet(DatePicker view, int year, int month, int day);
    }
}