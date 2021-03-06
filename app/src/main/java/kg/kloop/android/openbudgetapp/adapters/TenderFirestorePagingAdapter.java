package kg.kloop.android.openbudgetapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;

import java.io.Serializable;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.activities.TenderActivity;
import kg.kloop.android.openbudgetapp.objects.Tender;
import kg.kloop.android.openbudgetapp.objects.User;

public class TenderFirestorePagingAdapter extends FirestorePagingAdapter<Tender, TenderFirestorePagingAdapter.TenderViewHolder> {
    private static final String TAG = TendersRecyclerViewAdapter.class.getSimpleName();
    private Context context;
    private User currentUser;

    public TenderFirestorePagingAdapter(Context context, User currentUser, @NonNull FirestorePagingOptions<Tender> options) {
        super(options);
        this.context = context;
        this.currentUser = currentUser;
    }

    @Override
    protected void onBindViewHolder(@NonNull TenderViewHolder holder, int position, @NonNull Tender model) {
        Log.i(TAG, "onBindViewHolder: list size: " + getCurrentList().size());
        try {
            Tender tender = getItem(position).toObject(Tender.class);
            holder.purchaseTextView.setText(tender.getProcurement_object());
            holder.orgNameTextView.setText(tender.getProcuring_entity());
            holder.addressTextView.setText(tender.getRegion());
            String sum = tender.getPlanned_sum_int() + " " + tender.getCurrency();
            holder.plannedSumTextView.setText(sum);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onLoadingStateChanged(LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state) {
            case LOADING_INITIAL:
                //TODO: implement progress bar
        }
    }

    @NonNull
    @Override
    public TenderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.i(TAG, "onCreateViewHolder: item count: " + getItemCount());
        return new TenderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_all_tenders, viewGroup, false));
    }

    public class TenderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView purchaseTextView;
        TextView orgNameTextView;
        TextView plannedSumTextView;
        TextView addressTextView;
        public TenderViewHolder(@NonNull View itemView) {
            super(itemView);
            purchaseTextView = itemView.findViewById(R.id.item_purchase_text_view);
            orgNameTextView = itemView.findViewById(R.id.item_org_name_text_view);
            plannedSumTextView = itemView.findViewById(R.id.item_planned_sum_text_view);
            addressTextView = itemView.findViewById(R.id.item_address_text_view);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, TenderActivity.class);
            intent.putExtra("tender", (Serializable) getItem(getAdapterPosition()).toObject(Tender.class));
            if (currentUser != null) {
                intent.putExtra("current_user", currentUser);
                Log.v(TAG, "current_user: " + currentUser.getName());
            }
            context.startActivity(intent);
        }
    }
}
