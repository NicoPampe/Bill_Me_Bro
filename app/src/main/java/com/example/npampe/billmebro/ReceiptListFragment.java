package com.example.npampe.billmebro;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String DIALOG_PREVIEW = "DialogPreview";

    @Bind(R.id.receipt_recycler_view)
    RecyclerView mRecyclerView;
    // For purposes of storing example receipts
    // TODO: Remove after further implementation
    List<ParentListItem> items;
    private Callbacks mCallbacks;
    private MyAdapter mAdapter;

    /**
     * Callbacks onAttach
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
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
        prepareExampleReceipts();
    }

    /**
     * View created for current fragment
     *
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

        // Make some fake data.
        ReceiptsList rl = new ReceiptsList(getActivity());

        // Our db should stay small.
        if (!rl.getReceipts().isEmpty()) {
            Log.i(TAG, "database is not empty! Nuking it.");
            rl.nukeIt();
        }

        // Now add in our fake data.
        List<Receipt> receipts = Arrays.asList(new Receipt("Receipt A"),
                new Receipt("Receipt B"),
                new Receipt("Receipt C"),
                new Receipt("Receipt D"));

        for (Receipt receipt : receipts) {
            rl.addReceipt(receipt);
        }

        rl.addReceipt(receipts.get(0));
        rl.removeReceipt(receipts.get(0));

        // </ fake data >

        updateUI();
        return view;
    }

    /**
     * Options Menu, Has create new crime and view details about group
     *
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

    public void prepareExampleReceipts() {
        List<Receipt> receipts0 = Arrays.asList(new Receipt("Receipt A"), new Receipt("Receipt B"));

        // For Alpha Release Demonstration Purposes Only
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Receipt> receipts1 = Arrays.asList(new Receipt("Receipt C"), new Receipt("Receipt D"));
        ReceiptsList.get(getActivity()).addReceipt(receipts0.get(0));
        ReceiptsList.get(getActivity()).addReceipt(receipts0.get(1));
        ReceiptsList.get(getActivity()).addReceipt(receipts1.get(0));
        ReceiptsList.get(getActivity()).addReceipt(receipts1.get(1));
        ReceiptParentListItem item0 = new ReceiptParentListItem(receipts0, receipts0.get(0).getDate());
        ReceiptParentListItem item1 = new ReceiptParentListItem(receipts1, receipts1.get(0).getDate());

        items = new ArrayList<ParentListItem>();
        items.add(item0);
        items.add(item1);
    }

    public void updateUI() {
        if (mAdapter == null) {
            mAdapter = new MyAdapter(getActivity().getApplicationContext(), items);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    public interface Callbacks {
        void onReceiptSelected(Receipt receipt);
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
        @Bind(R.id.list_item_receipt_parent_text_view)
        TextView mRecipeTextView;

        @Bind(R.id.list_item_receipt_parent_drop_down_button)
        ImageView mArrowExpandImageView;

        public ReceiptParentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mArrowExpandImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded()) {
                        mArrowExpandImageView.setImageResource(R.drawable.arrow_down_float);
                        collapseView();
                    } else {
                        mArrowExpandImageView.setImageResource(R.drawable.arrow_up_float);
                        expandView();
                    }
                }
            });
        }

        @Override
        public boolean shouldItemViewClickToggleExpansion() {
            return true;
        }

        public void bind(ReceiptParentListItem parentItem) {
            mRecipeTextView.setText(parentItem.getDate());
        }
    }

    public class ReceiptChildViewHolder extends ChildViewHolder {
        private TextView mReceiptChildTextView;
        private Button mEditButton;
        private Receipt mReceipt;

        private final GestureDetector detector = new GestureDetector(new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    updateUI();
                    mCallbacks.onReceiptSelected(mReceipt);
                } else {
                    FragmentManager fm = getFragmentManager();
                    ReceiptPreviewDialog dialog = new ReceiptPreviewDialog();
                    dialog.show(fm, DIALOG_PREVIEW);
                }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                final int swipeThresh = 75;
                final int minDist = 50;
                if (e1.getX() - e2.getX() > minDist && Math.abs(velocityX) > swipeThresh) {
                    Log.d(TAG, "onFling: Right -> Left");

                    // Show/enable edit button
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateUI();
                            mCallbacks.onReceiptSelected(mReceipt);
                        }
                    });
                    return true;
                } else if (e2.getX() - e1.getX() > minDist && Math.abs(velocityX) > swipeThresh) {
                    Log.d(TAG, "onFling: Left -> Right");

                    // Hide/disable edit button
                    mEditButton.setVisibility(View.INVISIBLE);
                    mEditButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Deactivate edit buttons onClick functionality
                        }
                    });
                    return false;
                }

                if (e1.getY() - e2.getY() > 100 && Math.abs(velocityY) > 200) {
                    return false;
                } else if (e2.getY() - e1.getY() > 100 && Math.abs(velocityY) > 200) {
                    return false;
                }
                return false;
            }

        });

        public ReceiptChildViewHolder(View itemView) {
            super(itemView);
            mReceiptChildTextView = (TextView) itemView.findViewById(R.id.list_item_receipt_child_text_view);
            mEditButton = (Button) itemView.findViewById(R.id.list_item_receipt_child_edit_button);

            itemView.findViewById(R.id.list_item_receipt_child).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(TAG, "onTouch: ");
                    detector.onTouchEvent(event);
                    return true;
                }
            });
        }

        public void bind(Receipt receipt) {
            mReceiptChildTextView.setText(receipt.getTitle());
            mReceipt = receipt;
        }
    }

    // Clean up
    public class MyAdapter extends ExpandableRecyclerAdapter<ReceiptParentViewHolder, ReceiptChildViewHolder> {

        private LayoutInflater mInflator;

        public MyAdapter(Context context, List<ParentListItem> parentItemList) {
            super(parentItemList);
            mInflator = LayoutInflater.from(context);
        }

        // onCreate ...
        @Override
        public ReceiptParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
            View recipeView = mInflator.inflate(R.layout.list_item_receipt_parent, parentViewGroup, false);
            return new ReceiptParentViewHolder(recipeView);
        }

        @Override
        public ReceiptChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
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
}
