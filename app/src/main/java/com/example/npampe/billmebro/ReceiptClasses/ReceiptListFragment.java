package com.example.npampe.billmebro.ReceiptClasses;


import android.support.v4.app.FragmentTransaction;
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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.example.npampe.billmebro.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReceiptListFragment extends Fragment {
    private static final String TAG = "Receipt_List_Fragment";
    private static final String DIALOG_PREVIEW = "DialogPreview";

    @Bind(R.id.receipt_recycler_view)
    RecyclerView mRecyclerView;
    // For purposes of storing example receipts
    // TODO: Remove after further implementation
    List<ReceiptParentListItem> mItems;
    private Callbacks mCallbacks;
    private MyAdapter mAdapter;
    private Menu mOptionsMenu;

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
     * Options Menu, Has create new crime and view details about group
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mOptionsMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_receipt_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mOptionsMenu = menu;

        super.onPrepareOptionsMenu(menu);

        if (getActivity().findViewById(R.id.twopane_layout) == null) {
            mOptionsMenu.findItem(R.id.menu_item_delete_crime).setVisible(false);
        } else {
            mOptionsMenu.findItem(R.id.menu_item_delete_crime).setVisible(true);
        }
    }

    /**
     * Selection of an item from the menu in CrimeList
     *
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_receipt:
                addReceiptToReceiptList();
                return true;
            case R.id.menu_item_delete_all_receipts:
                ReceiptsList.get(getActivity()).clearDatabase();
                mItems.clear();
                updateUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Adds a receipt to the list.
     * Callback
     */
    public void addReceiptToReceiptList() {
        Receipt receipt = new Receipt();

        ReceiptsList.get(getActivity()).addReceipt(receipt);

        List<Receipt> receipts = new ArrayList<>();
        receipts.add(receipt);

        ReceiptParentListItem parentListItem = new ReceiptParentListItem(receipts, receipt.getDate(), receipt.getDayOfYear());
        updateUI();
        mCallbacks.onReceiptSelected(receipt);
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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.receipt_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void prepareExampleReceipts() {
        Log.d(TAG, "prepareExampleReceipts: SHIT SHIT SHIT");
        List<Receipt> receipts = Arrays.asList(new Receipt("Receipt A"), new Receipt("Receipt B"));

        if (ReceiptsList.get(getActivity()).getReceipts().isEmpty()) {
            ReceiptsList.get(getActivity()).addReceipt(receipts.get(0));
            ReceiptsList.get(getActivity()).addReceipt(receipts.get(1));
            ReceiptParentListItem item0 = new ReceiptParentListItem(receipts, receipts.get(0).getDate(), receipts.get(0).getDayOfYear());
            ReceiptParentListItem item1 = new ReceiptParentListItem(receipts, receipts.get(0).getDate(), receipts.get(0).getDayOfYear());
        }
    }

    public void updateUI() {
        ReceiptsList receiptsList = ReceiptsList.get(getActivity());
        List<Receipt> receipts = receiptsList.getReceipts();
        Log.d(TAG, "updateUI: the receipt size " + receipts.size());
        updateParentListItem(receipts);

        if (mAdapter == null) {
            mAdapter = new MyAdapter(getActivity().getApplicationContext(), mItems);
            mRecyclerView.setAdapter(mAdapter);
            Log.d(TAG, "updateUI: mAdapter is null");
        } else {
            mAdapter.setReceipts(receipts);
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, "updateUI: mAdapter is NOT null");

//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.receipt_recycler_view, this).commit();
//            getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .detach(this)
//                    .attach(this)
//                    .commit();
        }
        Log.d(TAG, "updateUI: Finished the updateUI");
    }

    public void updateParentListItem(List<Receipt> receipts) {
        ArrayList<ReceiptParentListItem> newParents = new ArrayList<>();

        for (Receipt receipt : receipts) {
            if (mItems == null) {
                mItems = new ArrayList<>();
                ReceiptParentListItem init = new ReceiptParentListItem(receipt, receipt.getDate(), receipt.getDayOfYear());
                mItems.add(init);
            } else {
                for (int i = 0; i < mItems.size(); i++) {
                    if (mItems.get(i).getDayOfYear() != receipt.getDayOfYear()) {
                        ReceiptParentListItem newItem = new ReceiptParentListItem(receipt, receipt.getDate(), receipt.getDayOfYear());
                        newParents.add(newItem);
                    }
                }
            }
        }

        for (ReceiptParentListItem parent : newParents) {
            mItems.add(parent);
        }

        for (Receipt receipt : receipts) {
            for (ReceiptParentListItem parent : mItems) {
                if (parent.getDayOfYear() == receipt.getDayOfYear() ) {
                    parent.add(receipt);
                }
            }
        }
    }

    public interface Callbacks {
        void onReceiptSelected(Receipt receipt);
    }

    public class ReceiptParentListItem implements ParentListItem {
        private List<Receipt> mReceipts;
        private Date mDate;
        private Calendar mCalendar = new GregorianCalendar();
        private int mDayOfYear;
        private ArrayList<DateFormat> mDateFormats;

        public ReceiptParentListItem(List<Receipt> receipts, Date date, int dayOfYear) {
            mReceipts = receipts;
            mDate = date;
            if (date != null) {
                mCalendar.setTime(date);
                mDayOfYear = dayOfYear;
            }

            mDateFormats = new ArrayList<DateFormat>();
            mDateFormats.add(DateFormat.getDateInstance(DateFormat.FULL));
            mDateFormats.get(0).setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        public ReceiptParentListItem(Receipt receipt, Date date, int dayOfYear) {
            mReceipts = new ArrayList<>();
            mReceipts.add(receipt);
            mDate = date;
            mCalendar.setTime(date);
            mDayOfYear = dayOfYear;

            mDateFormats = new ArrayList<DateFormat>();
            mDateFormats.add(DateFormat.getDateInstance(DateFormat.FULL));
            mDateFormats.get(0).setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        @Override
        public List<?> getChildItemList() {
            return mReceipts;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }

        public Date getDate() {
            return mDate;
        }

        public String getDateFormatted() {
            return mDateFormats.get(0).format(mDate);
        }

        public void setRecipts(List<Receipt> recipts) {
            mReceipts = recipts;
        }

        public void add(List<Receipt> receipts) {
            for (Receipt recpt : receipts) {
                mReceipts.add(recpt);
            }
        }

        public void add(Receipt receipt) {
            for (Receipt r : mReceipts) {
                if (r.getId() == receipt.getId()) {
                    return;
                }
            }
            mReceipts.add(receipt);
        }

        public Calendar getCalendar() {
            return mCalendar;
        }

        public void setCalendar(Calendar calendar) {
            mCalendar = calendar;
        }

        public int getDayOfYear() {
            return mDayOfYear;
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
            mRecipeTextView.setText(parentItem.getDateFormatted());
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
                final int swipeThresh = 0;
                final int minDist = 1;
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

        public MyAdapter(Context context, List<ReceiptParentListItem> parentItemList) {
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

        /**
         * Compares the ParentListItem to ReceiptParentListItem
         * sets the Receipts
         *
         * @param receipts
         */
        public void setReceipts(List<Receipt> receipts) {
            updateParentListItem(receipts);
        }
    }
}
