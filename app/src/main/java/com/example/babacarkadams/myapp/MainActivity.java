package com.example.babacarkadams.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    protected RecyclerView mBlogRecyclerView;
    private DatabaseReference mDatabaseReference;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseRecyclerAdapter mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_BLOGS);

        mDatabaseReference.keepSynced(true);

        mBlogRecyclerView = (RecyclerView) findViewById(R.id.blog_list);
        setUpFirebaseAdapter();
    }

    public void setUpFirebaseAdapter() {
        FirebaseRecyclerOptions<Blog> options = new FirebaseRecyclerOptions
                .Builder<Blog>()
                .setQuery(mDatabaseReference, Blog.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Blog, FirebaseBlogViewHolder>(options) {

            FirebaseBlogViewHolder mViewHolder;

            @Override
            public FirebaseBlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.blog_row, parent, false);

                mViewHolder = new FirebaseBlogViewHolder(view);
                return mViewHolder;
            }

            @Override
            protected void onBindViewHolder(FirebaseBlogViewHolder holder, int position, Blog model) {
                Log.i("Main", " ----- ----- pupulate view holder");
                mViewHolder.bindBlog(model);
            }

//            @Override
//            protected void populateViewHolder(FirebaseBlogViewHolder viewHolder, Blog model, int position) {
//                Log.i("Main", " ----- ----- pupulate view holder");
//                viewHolder.bindBlog(model);
//            }
        };

        mBlogRecyclerView.setHasFixedSize(true);
        mBlogRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBlogRecyclerView.setAdapter(mFirebaseAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAdapter.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mFirebaseAdapter.cleanup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(MainActivity.this, PostActivity.class));
        }
        if (item.getItemId() == R.id.action_logout) {
            logOut();
            if (mAuth.getCurrentUser() == null) {
                Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        mAuth.signOut();
    }
}
