package com.example.bbcnewsreader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NewsFragment extends Fragment {

    // Keys for arguments
    private static final String ARG_TITLE = "title";
    private static final String ARG_CONTENT = "content";
    private ListView newsListView;

    // Static method to create a new instance of the fragment with arguments
    public static NewsFragment newInstance(String title, String content) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();

        args.putString(ARG_TITLE, title);
        args.putString(ARG_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        // Extract arguments and set them to the TextViews
        if (getArguments() != null) {
            String title = getArguments().getString(ARG_TITLE);
            String content = getArguments().getString(ARG_CONTENT);

            TextView titleTextView = view.findViewById(R.id.detailTitleTextView);
            TextView contentTextView = view.findViewById(R.id.detailContentTextView);

            titleTextView.setText(title);
            contentTextView.setText(content);
        }
        newsListView = view.findViewById(R.id.newsListView);

        // Set an item click listener for the ListView
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Example data: Replace with real data from your adapter
                String title = "Sample News Title";
                String content = "This is the content of the selected news.";

                // Call showNewsDetail() from MainActivity to display the detail fragment
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).showNewsDetail(title, content);
                }
            }
        });

        return view;
    }
}