package com.example.bbcnewsreader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.bbcnewsreader.network.RetrofitClient;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private static final String API_KEY = "YOUR_API_KEY_HERE";  // Replace with your real API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("BBC News Reader - v1.0");

        // Set up navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Fetch news using Retrofit
        fetchNewsData();
// Load default fragment (NewsFragment)
if (savedInstanceState == null) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NewsFragment())
                .commit();
        navigationView.setCheckedItem(R.id.nav_home);
    }
    }
    // This method will display the NewsDetailFragment
    public void showNewsDetail(String title, String content) {
    NewsFragment detailFragment= NewsFragment.newInstance(title, content);
    FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailFragment);
        transaction.addToBackStack(null);  // Allows the user to go back
        transaction.commit();
}

    // Step 2.1: Inflate the help menu
    @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_help, menu);
    return true;
}

// Step 2.2: Handle the Help menu item click to show AlertDialog
@Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
//showHelpDialog();
return true;
        }
                return super.onOptionsItemSelected(item);
    }

    // Step 2.3: Method to display the Help dialog
    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Help")
                .setMessage("Instructions on how to use the app:\n\n- Use the navigation menu to access different sections.\n- Click on any news article to see its details.\n- Use the back button to return to the main list.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    })
            .create()
                .show();
}

    // Fetch news data from API
    private void fetchNewsData() {
        // Create the Retrofit service
        NewsApiService apiService = RetrofitClient.getRetrofitInstance().create(NewsApiService.class);

        // Make the API call to get top headlines
        Call<NewsResponse> call = apiService.getTopHeadlines("us", API_KEY);

        // Handle the response asynchronously
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    // Get the list of news articles
                    NewsResponse newsResponse = response.body();
                    if (newsResponse != null) {
                        List<NewsArticle> articles = newsResponse.getArticles();
                        for (NewsArticle article : articles) {
                            Log.d("News", "Title: " + article.getTitle());
                        }
                    }
                } else {
                    Log.e("News", "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("News", "Network call failed: " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_favorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}