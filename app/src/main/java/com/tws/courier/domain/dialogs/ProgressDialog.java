package com.tws.courier.domain.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tws.courier.R;

/**
 * The type Progress dialog.
 */
public class ProgressDialog extends BaseDialog {

    private Context context;
    private ProgressDialogListener progressDialogListener;
//    private ImageView image;
//    private ProgressBar progressBar;
    private TextView textMessage;
    private String message;

    private ProgressDialog() {
    }

    /**
     * Instantiates a new Progress dialog.
     *
     * @param context the context
     */
    public ProgressDialog(Context context) {
        this.context = context;
        initDialog();
        initViews();
    }

    /**
     * Instantiates a new Progress dialog.
     *
     * @param context the context
     * @param message the message
     */
    public ProgressDialog(Context context, String message) {
        this.context = context;
        this.message = message;
        initDialog();
        initViews();
    }

    /**
     * Instantiates a new Progress dialog.
     *
     * @param context                the context
     * @param progressDialogListener the progress dialog listener
     */
    public ProgressDialog(Context context, @NonNull ProgressDialogListener progressDialogListener) {
        this.context = context;
        this.progressDialogListener = progressDialogListener;
        initDialog();
        initViews();
    }

    /**
     * Instantiates a new Progress dialog.
     *
     * @param context                the context
     * @param message                the message
     * @param progressDialogListener the progress dialog listener
     */
    public ProgressDialog(Context context, String message, @NonNull ProgressDialogListener progressDialogListener) {
        this.context = context;
        this.message = message;
        this.progressDialogListener = progressDialogListener;
        initDialog();
        initViews();
    }

    /**
     * Init views.
     */
    protected void initViews() {
        if (getView() == null) return;
//        progressBar = getView().findViewById(R.id.prog);
        textMessage = getView().findViewById(R.id.txtMessage);
        setProgressMessage(message);
        if (progressDialogListener != null) {
            getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progressDialogListener.onProgressDialogCanceled();
                }
            });
        }
//        if (image != null) {
//            RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
//                    Animation.RELATIVE_TO_SELF, 0.5f,
//                    Animation.RELATIVE_TO_SELF, 0.5f);
//            rotateAnimation.setDuration(5000);
//            rotateAnimation.setInterpolator(new LinearInterpolator());
//            rotateAnimation.setRepeatCount(Animation.INFINITE);
//            image.startAnimation(rotateAnimation);
//        }
    }

    @Override
    public void show() {
        try {
            getDialog().setCancelable(false);
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().getWindow().setDimAmount(0.7f);
            getDialog().show();
        } catch (Exception e) {
        }
    }

    /**
     * Sets progress message.
     *
     * @param message the message
     */
    public void setProgressMessage(String message) {
        if (TextUtils.isEmpty(message)) textMessage.setText(R.string.msg_please_wait);
        else textMessage.setText(message);
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_progress;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.AppTheme_NoBackDialog;
    }

    /**
     * The interface Progress dialog listener.
     */
    public interface ProgressDialogListener {
        /**
         * On progress dialog canceled.
         */
        void onProgressDialogCanceled();
    }
}