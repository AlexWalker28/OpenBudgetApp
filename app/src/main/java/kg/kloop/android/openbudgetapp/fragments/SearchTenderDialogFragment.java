package kg.kloop.android.openbudgetapp.fragments;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import kg.kloop.android.openbudgetapp.R;
import kg.kloop.android.openbudgetapp.models.MainViewModel;

public class SearchTenderDialogFragment extends DialogFragment {

    private static final String TAG = SearchTenderDialogFragment.class.getSimpleName();
    private EditText searchEditText;
    private Button submitButton;
    private Button cancelButton;

    public SearchTenderDialogFragment() {
    }

    public static SearchTenderDialogFragment newInstance(String title) {
        SearchTenderDialogFragment frag = new SearchTenderDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_search, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        searchEditText = view.findViewById(R.id.search_dialog_edit_text);
        submitButton = view.findViewById(R.id.search_dialog_submit_button);
        cancelButton = view.findViewById(R.id.search_dialog_cancel_button);
        // searchEditText.setText("16121331333387");

        // Show soft keyboard automatically and request focus to field
        searchEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchWords = searchEditText.getText().toString();
                viewModel.getSearchWords().setValue(searchWords);
                Log.i(TAG, "searchWords: " + searchWords);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
