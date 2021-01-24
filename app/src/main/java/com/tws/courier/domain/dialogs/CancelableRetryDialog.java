package com.tws.courier.domain.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import com.tws.courier.R;
import com.tws.courier.domain.annotations.DataRequestType;

/**
 * The type Cancelable retry dialog.
 */
public class CancelableRetryDialog {

    private AlertDialog alertDialog;

    private CancelableRetryDialog() {
    }

    /**
     * Instantiates a new Cancelable retry dialog.
     *
     * @param context        the context
     * @param requesterType  the requester type
     * @param message        the message
     * @param dialogListener the dialog listener
     */
    public CancelableRetryDialog(Context context,
                                 @DataRequestType final int requesterType,
                                 String message,
                                 final CancelableRetryDialogListener dialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (TextUtils.isEmpty(message)) builder.setMessage(R.string.err_something_wrong);
        else builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (dialogListener != null) dialogListener.onRetry(dialog, requesterType);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (dialogListener != null) dialogListener.onCancel(dialog, requesterType);
            }
        });
        alertDialog = builder.create();
    }

    /**
     * Show.
     */
    public void show() {
        alertDialog.show();
    }

    /**
     * The interface Cancelable retry dialog listener.
     */
    public interface CancelableRetryDialogListener {
        /**
         * On retry.
         *
         * @param dialog        the dialog
         * @param requesterType the requester type
         */
        void onRetry(DialogInterface dialog, @DataRequestType int requesterType);

        /**
         * On cancel.
         *
         * @param dialog        the dialog
         * @param requesterType the requester type
         */
        void onCancel(DialogInterface dialog, @DataRequestType int requesterType);
    }
}
