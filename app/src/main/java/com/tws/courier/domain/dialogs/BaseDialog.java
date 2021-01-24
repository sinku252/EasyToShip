package com.tws.courier.domain.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

public abstract class BaseDialog {

    private AlertDialog dialog;
    private View view;

    protected abstract Context getContext();

    protected abstract @LayoutRes
    int getLayoutResource();

    protected abstract @StyleRes
    int getDialogStyle();

    protected abstract void show();

    public void initDialog() {
        getDialog();
    }

    public AlertDialog getDialog() {
        if (dialog == null) {
            if (getContext() == null) throw new RuntimeException("Context cannot be null!");
            AlertDialog.Builder builder;
            if (getDialogStyle() == 0)
//                builder = new AlertDialog.Builder(getContext(), R.style.AppTheme_Dialog);
                builder = new AlertDialog.Builder(getContext());
            else builder = new AlertDialog.Builder(getContext(), getDialogStyle());
            if (getLayoutResource() != 0) {
                view = LayoutInflater.from(getContext()).inflate(getLayoutResource(), null);
                builder.setView(view);
            }
            dialog = builder.create();
        }
        return dialog;
    }

    public View getView() {
        return view;
    }
}