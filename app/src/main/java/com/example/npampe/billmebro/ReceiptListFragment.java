package com.example.npampe.billmebro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReceiptListFragment extends Fragment {
    @Bind(R.id.receipt_recycler_view)
    RecyclerView mRecyclerView;

    private ReceiptAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_receipt_list, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        List<Receipt> receipts = Arrays.asList(new Receipt("Receipt A"), new Receipt("Receipt B"));

        if (mAdapter == null) {
            mAdapter = new ReceiptAdapter(receipts);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public class ReceiptHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Receipt mReceipt;
        @Bind(R.id.receipt_title_text_view)
        TextView mTitleTextView;

        public ReceiptHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindReceipt(Receipt receipt) {
            mReceipt = receipt;
            mTitleTextView.setText(mReceipt.getTitle());
        }

        @Override
        public void onClick(View v) {
            // Do nothing
        }
    }

    public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptHolder> {
        private List<Receipt> mReceipts;

        public ReceiptAdapter(List<Receipt> receipts) {
            mReceipts = receipts;
        }

        @Override
        public ReceiptHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_receipt, parent, false);
            return new ReceiptHolder(view);
        }

        @Override
        public void onBindViewHolder(ReceiptHolder holder, int position) {
            Receipt receipt = mReceipts.get(position);
            holder.bindReceipt(receipt);
        }

        @Override
        public int getItemCount() {
            return mReceipts.size();
        }
    }
}
