package com.example.npampe.billmebro.ReceiptClasses;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.npampe.billmebro.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick Pampe on 5/6/2016.
 */
public class ReceiptProjectSummarizeDialog extends DialogFragment {
    private int[] COLORS_BASE = {Color.BLUE, Color.GREEN, Color.GRAY, Color.CYAN, Color.RED, Color.MAGENTA, Color.BLACK, Color.LTGRAY};

    private List<Receipt> mReceipts;
    private LinearLayout mReceiptsLinearLayout;

    private TextView mTotal;

    private ArrayList<Double> mValues;


    public ReceiptProjectSummarizeDialog(List<Receipt> receipts) {
        mReceipts = receipts;
        mValues = new ArrayList<>();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_summarized_project, null);

        initTextView(v);
        updateTextViews();

        TextView title = new TextView(getActivity());
        title.setText("Summarization of the Project");
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        for (Receipt receipt : mReceipts) {
            TextView receiptTextView = new TextView(getContext());
            receiptTextView.setText(String.format("%s: %s", receipt.getTitle(), receipt.getTotal()));
            receiptTextView.setTextColor(COLORS_BASE[mReceipts.indexOf(receipt)]);
            mReceiptsLinearLayout.addView(receiptTextView);
        }

        LinearLayout chartLinear = (LinearLayout)v.findViewById(R.id.chart_linear_layout);
        LinearLayout.LayoutParams parmas = (LinearLayout.LayoutParams) chartLinear.getLayoutParams();
        mValues = calculateData(mValues);
        MyGraphview graphview = new MyGraphview(getContext(), mValues);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            graphview.setRadiues(getActivity().getWindow().getDecorView().getWidth() * (float)0.55);
        } else {
            graphview.setRadiues(getActivity().getWindow().getDecorView().getWidth() * (float)0.25);
        }
        chartLinear.setLayoutParams(parmas);
        chartLinear.addView(graphview);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setCustomTitle(title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    private void updateTextViews() {
        double sum = 0;
        for (Receipt receipt : mReceipts) {
            sum += receipt.getTotal();
        }
        mTotal.setText(Double.toString(sum));
    }

    private void initTextView(View v) {
        mTotal = (TextView)v.findViewById(R.id.preview_total_on_project);
        mReceiptsLinearLayout = (LinearLayout)v.findViewById(R.id.dialog_list_receipts);

    }

    private ArrayList<Double> calculateData(ArrayList<Double> values) {
        for (Receipt receipt : mReceipts) {
            values.add(receipt.getTotal());
        }

        float total=0;
        for(int i=0;i<values.size();i++)
        {
            total+=values.get(i);
        }
        for(int i=0;i<values.size();i++)
        {
            values.set(i, 360*(values.get(i)/total));
        }
        return  values;
    }

    /**
     * Graph View class for displaying a pie chart
     * Curtice of SO: http://stackoverflow.com/questions/4397192/draw-pie-chart-in-android
     */
    public class MyGraphview extends View {
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float[] value_degree;
        private int[] COLORS = COLORS_BASE;


        private float radiues = 400;
        RectF rectf = new RectF(10, 10, radiues, radiues);
        int temp = 0;

        public MyGraphview(Context context, ArrayList<Double> values) {
            super(context);
            value_degree = new float[values.size()];
            for (int i = 0; i < values.size(); i++) {
                value_degree[i] = values.get(i).floatValue();
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
            temp = 0;

            RectF rectf = new RectF(10, 10, radiues, radiues);
            Log.d("TAG", "onDraw: rad " + radiues);

            for (int i = 0; i < value_degree.length; i++) {//values2.length; i++) {
                if (i == 0) {
                    paint.setColor(COLORS[i]);
                    canvas.drawArc(rectf, 0, value_degree[i], true, paint);
                } else {
                    temp += (int) value_degree[i - 1];
                    paint.setColor(COLORS[i]);
                    canvas.drawArc(rectf, temp, value_degree[i], true, paint);
                }
            }
        }

        public float getRadiues() {
            return radiues;
        }

        public void setRadiues(float radiues) {
            this.radiues = radiues;
        }
    }
}
