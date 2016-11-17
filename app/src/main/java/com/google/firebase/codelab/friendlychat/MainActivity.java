/**
 * Copyright Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.firebase.codelab.friendlychat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.database.*;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.lang.reflect.Array;
import java.util.*;


public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {



    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
        }
    }

    private static final String TAG = "MainActivity";
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;


    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;


    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Scan, MessageViewHolder> mFirebaseAdapter;
    private FirebaseListAdapter<Scan> mListAdapter;


    //settings
    private int tagCount = 100;
    ArrayList<String> gateNames = new ArrayList<>();
    ArrayList<String> gates = new ArrayList<>(5);
    ArrayList<String> locFilter = new ArrayList<>();
    ArrayList<Integer> selectedIndices;

    String id;
    HashMap<String, String> workerHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);


        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

//        Query locQuery = mFirebaseDatabaseReference.child("locations").orderByChild("name");
//        locQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
//                    Location location = postSnapshot.getValue(Location.class);
//                    gateNames.add(location.deviceName);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        workerHashMap = new HashMap<>();

        Query workerQuery = mFirebaseDatabaseReference.child("workers").orderByChild("gateScanId");
        workerQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Worker worker = postSnapshot.getValue(Worker.class);
                    String name = worker.firstname + " " + worker.lastname;
                    workerHashMap.put(worker.gateScanId, name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        gates.add("Test Gate");
        gates.add("Demo-Gate");
        gates.add("The Park");
        gates.add("Park-Main");
        gates.add("Park-2");
        gates.add("Kern-Lock");
        gates.add("Kern-Wide1");
        gates.add("Kern-Wide2");
        gates.add("kccf gate1");
        gates.add("SDG-Test1");
        locFilter = gates;
        workerHashMap = new HashMap<>();

        displayFirebaseQuery();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in.
        // TODO: Add code to check if user is signed in.
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showFilterDialog();
        return true;
    }

    public void showFilterDialog(){
        selectedIndices = new ArrayList<>();
        boolean[] chosen = new boolean[gates.size()];
        for (int j = 0; j < chosen.length; j++){
            if (locFilter.contains(gates.get(j))) {
                chosen[j] = true;
                selectedIndices.add(j);
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        builder.setTitle("Choose gates");
        builder.setMultiChoiceItems(R.array.filter, chosen, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                        if (isChecked && !selectedIndices.contains(which)) {
                            selectedIndices.add(which);
                        } else if (selectedIndices.contains(which)) {
                            selectedIndices.remove(Integer.valueOf(which));
                        }

                    }
                });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        locFilter = new ArrayList<String>();
                        for (Integer k : selectedIndices){
                            locFilter.add(gates.get(k));
                        }

                        displayFirebaseQuery();
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //remove AlertDialog
                    }
                });
        builder.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    private void displayFirebaseQuery() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Scan, MessageViewHolder>(
                Scan.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child("scans").limitToLast(tagCount).orderByChild("scantime")) {
                //mFirebaseDatabaseReference.child("scans").orderByChild("scantime").startAt("end"-100)) {

            //override of method for anon inner class
            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder, Scan model, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                if(locFilter.contains(model.location)) {
                    id = model.workerid;
                    id = id.replaceFirst("^0+(?!$)", "");

                    if(workerHashMap.containsKey(id)){
                        if(workerHashMap.get(id) == null) {
                            viewHolder.messageTextView.setText(id);
                        } else {
                            viewHolder.messageTextView.setText(workerHashMap.get(id));
                        }
                    } else {
                        //final String id2 = "20150100444";  //for testing, this is jesus gomez
                        //query firebase for worker name
                        Query workerQuery = mFirebaseDatabaseReference.child("workers").orderByChild("gateScanId").startAt(id).endAt(id);
                        workerQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String name = "";
                                Worker worker = dataSnapshot.child(id).getValue(Worker.class);

                                if (worker != null) {
                                    name = worker.firstname + " " + worker.lastname;
                                    workerHashMap.put(id, name);
                                }
                                if (name.equals("")) {
                                    name = id;
                                    workerHashMap.put(id, null);
                                }
                                viewHolder.messageTextView.setText(name);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    Date scanned = new Date(model.scantime);
                    String about = model.location + ", " + scanned.toString();
                    viewHolder.messengerTextView.setText(about);
                }
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = tagCount;
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
    }
}
