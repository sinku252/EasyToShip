package com.tws.courier.domain.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import com.tws.courier.R;
import com.tws.courier.domain.annotations.DataRequestType;

/**
 * The type Force retry dialog.
 */
public class ForceRetryDialog {

    private AlertDialog alertDialog;

    private ForceRetryDialog() {
    }

    /**
     * Instantiates a new Force retry dialog.
     *
     * @param context        the context
     * @param requesterType  the requester type
     * @param message        the message
     * @param dialogListener the dialog listener
     */
    public ForceRetryDialog(Context context,
                            @DataRequestType final int requesterType,
                            String message,
                            final ForceRetryDialogListener dialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (TextUtils.isEmpty(message)) builder.setMessage(R.string.err_something_wrong);
        else builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (dialogListener != null) dialogListener.onRetry(dialog, requesterType);
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
     * The interface Force retry dialog listener.
     */
    public interface ForceRetryDialogListener {
        /**
         * On retry.
         *
         * @param dialog        the dialog
         * @param requesterType the requester type
         */
        void onRetry(DialogInterface dialog, @DataRequestType int requesterType);
    }
}
