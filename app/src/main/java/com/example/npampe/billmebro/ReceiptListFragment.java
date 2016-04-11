package com.example.npampe.billmebro;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReceiptListFragment extends Fragment {
    private static final String TAG = "Receipt_List_Fragment";
    @Bind(R.id.receipt_recycler_view)
    RecyclerView mRecyclerView;

    private Callbacks mCallbacks;
    private MyAdapter mAdapter;

    public interface Callbacks {
        void onReceiptSelected(Receipt receipt);
    }

    /**
     * Callbacks onAttach
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mCallbacks = (Callbacks) context;
    }

    /**
     * Callbacks onDetach
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * View created for current fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_receipt_list, container, false);
        ButterKnife.bind(this, view);

       mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    /**
     * Options Menu, Has create new crime and view details about group
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // TODO: update menu options
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
        List<Receipt> receipts0 = Arrays.asList(new Receipt("Receipt A"), new Receipt("Receipt B"));
        List<Receipt> receipts1 = Arrays.asList(new Receipt("Receipt C"), new Receipt("Receipt D"));

        ReceiptParentListItem item0 = new ReceiptParentListItem(receipts0, receipts0.get(0).getDate());
        ReceiptParentListItem item1 = new ReceiptParentListItem(receipts1, receipts1.get(0).getDate());

        List<ParentListItem> items = new ArrayList<ParentListItem>();
        items.add(item0);
        items.add(item1);

        if (mAdapter == null) {
            //Log.d(TAG, "updateUI: receipts size = " + receipts.size());

            mAdapter = new MyAdapter(getActivity().getApplicationContext(), items);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public void addReceiptToReceipList() {
        Receipt receipt = new Receipt();
        ReceiptsList.get(getActivity()).addReceipt(receipt);
        updateUI();
        mCallbacks.onReceiptSelected(receipt);
    }

    public class ReceiptParentListItem implements ParentListItem {
        private List mReceipts;
        private String mDate;

        public ReceiptParentListItem(List receipts, Date date) {
            mReceipts = receipts;
            mDate = date.toString();
        }

        @Override
        public List<?> getChildItemList() {
            return mReceipts;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }

        public String getDate() {
            return mDate;
        }
    }

    public class ReceiptParentViewHolder extends ParentViewHolder {
        private TextView mRecipeTextView;
        private ImageView mArrowExpandImageView;

        public ReceiptParentViewHolder(View itemView) {
            super(itemView);
            mRecipeTextView = (TextView) itemView.findViewById(R.id.list_item_receipt_parent_text_view);

            mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.list_item_receipt_parent_drop_down_button);
            mArrowExpandImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded()) {
                        collapseView();
                    } else {
                        expandView();
                    }
                }
            });
        }

        @Override
        public boolean shouldItemViewClickToggleExpansion() {
            return false;
        }

        public void bind(ReceiptParentListItem parentItem) {
            mRecipeTextView.setText(parentItem.getDate());
        }
    }

    public class ReceiptChildViewHolder extends ChildViewHolder implements View.OnClickListener {
        @Bind(R.id.list_item_receipt_child_text_view)
        TextView mReceiptChildTextView;

        private Receipt mReceipt;

        public ReceiptChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Receipt receipt) {
            mReceiptChildTextView.setText(receipt.getTitle());
        }

        /**
         * implements the View.OnClickListener
         * @param v
         */
        @Override
        public void onClick(View v) {
            mCallbacks.onReceiptSelected(mReceipt);
        }
    }

    public class MyAdapter extends ExpandableRecyclerAdapter<ReceiptParentViewHolder, ReceiptChildViewHolder> {

        private LayoutInflater mInflator;

        public MyAdapter(Context context, List<ParentListItem> parentItemList) {
            super(parentItemList);
            mInflator = LayoutInflater.from(context);
        }

        // onCreate ...
        @Override
        public ReceiptParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
            Log.d(TAG, "onCreateParentViewHolder: inflating parent holder");
            View recipeView = mInflator.inflate(R.layout.list_item_receipt_parent, parentViewGroup, false);
            return new ReceiptParentViewHolder(recipeView);
        }

        @Override
        public ReceiptChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
            Log.d(TAG, "onCreateChildViewHolder: inflating child holder");
            View ingredientView = mInflator.inflate(R.layout.list_item_receipt_child, childViewGroup, false);
            return new ReceiptChildViewHolder(ingredientView);
        }

        // onBind ...
        @Override
        public void onBindParentViewHolder(ReceiptParentViewHolder receiptParentViewHolder, int position, ParentListItem parentListItem) {
            ReceiptParentListItem receipt = (ReceiptParentListItem) parentListItem;
            receiptParentViewHolder.bind(receipt);
        }

        @Override
        public void onBindChildViewHolder(ReceiptChildViewHolder receiptChildViewHolder, int position, Object childListItem) {
            Receipt receiptChild = (Receipt) childListItem;
            receiptChildViewHolder.bind(receiptChild);
        }
    }

//    public class ReceiptHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private Receipt mReceipt;
//        @Bind(R.id.receipt_title_text_view)
//        TextView mTitleTextView;
//
//        public ReceiptHolder(View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(this);
//            ButterKnife.bind(this, itemView);
//        }
//
//        public void bindReceipt(Receipt receipt) {
//            mReceipt = receipt;
//            mTitleTextView.setText(mReceipt.getTitle());
//        }
//
//        @Override
//        public void onClick(View v) {
//            // Do nothing
//        }
//    }
//    public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptHolder> {
//        private List<Receipt> mReceipts;
//
//        public ReceiptAdapter(List<Receipt> receipts) {
//            Log.d(TAG, "ReceiptAdapter: receipts size = " + receipts.size());
//            mReceipts = receipts;
//        }
//
//        @Override
//        public ReceiptHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//            View view = layoutInflater
//                    .inflate(R.layout.list_item_receipt, parent, false);
//            return new ReceiptHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(ReceiptHolder holder, int position) {
//            Log.d(TAG, "ReceiptAdapter::onBindViewHolder: pos = " + position);
//            Receipt receipt = mReceipts.get(position);
//            holder.bindReceipt(receipt);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mReceipts.size();
//        }
//    }
}
