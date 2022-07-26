package com.dhananjay.exposyslabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;

public class ProfileFragment extends Fragment {
    TextView fullName,email,phone,branch,clg,displayBranch,displayClg;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;
    Button resetPassLocalBtn,changeProfileImageBtn,logoutBtn;
    String userId;
    ImageView profileImage,imageView,imageView1,imageView2;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        fullName=view.findViewById(R.id.profileName);
        email=view.findViewById(R.id.profileEmail);
        phone=view.findViewById(R.id.profilePhone);
        resetPassLocalBtn=view.findViewById(R.id.resetPasswordLocal);
        profileImage=view.findViewById(R.id.profileImage);
        branch=view.findViewById(R.id.profileBranch);
        clg=view.findViewById(R.id.profileClg);
        displayClg=view.findViewById(R.id.profile_display_clg);
        displayBranch=view.findViewById(R.id.profile_display_branch);
        imageView1=view.findViewById(R.id.imgview3);
        imageView2=view.findViewById(R.id.imgview4);
        changeProfileImageBtn=view.findViewById(R.id.changeProfile);
        logoutBtn=view.findViewById(R.id.profile_logout);
        progressBar= view.findViewById(R.id.profileFragProgessbar);
        progressBar.setVisibility(View.VISIBLE);

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        imageView = navigationView.getHeaderView(0).findViewById(R.id.nav_user_profile);

        //when returning from EditProfile after updating the profile photo this will be needed.
        navigationView.getMenu().getItem(1).setChecked(true);
        getActivity().setTitle(navigationView.getMenu().getItem(1).getTitle());

        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        userId= fAuth.getCurrentUser().getUid();
        user=fAuth.getCurrentUser();


        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                progressBar.setVisibility(View.GONE);
                Picasso.get().load(uri).into(profileImage);
                Picasso.get().load(uri).into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Log.d("TAG","No image in the storage "+e.getMessage());
            }
        });


            DocumentReference documentReference= fStore.collection("users").document(userId);
            DocumentReference internRef= fStore.collection("internshipDetails").document(userId);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    //https://stackoverflow.com/questions/54676404/attempt-to-invoke-virtual-method-boolean-com-google-firebase-firestore-querysna
                    if(error == null){
                        if(value.exists()){
                            phone.setText(value.getString("phone"));
                            fullName.setText(value.getString("fName"));
                            email.setText(value.getString("email"));

                        }else{
                            Toast.makeText(getActivity(),"Got error "+error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });
           internRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
               @Override
               public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                  if(error == null){
                      if(value.exists()){
                          branch.setText(value.getString("branch"));
                          clg.setText(value.getString("college"));
                          branch.setVisibility(View.VISIBLE);
                          imageView1.setVisibility(View.VISIBLE);
                          imageView2.setVisibility(View.VISIBLE);
                          displayBranch.setVisibility(View.VISIBLE);
                          displayClg.setVisibility(View.VISIBLE);
                          clg.setVisibility(View.VISIBLE);
                      }
                  }

               }
           });



        resetPassLocalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPassword = new EditText(v.getContext());

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter New Password > 6 Characters long.");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(), "Password Reset Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Password Reset Failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close
                    }
                });

                passwordResetDialog.create().show();

            }
        });

        changeProfileImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                Intent i = new Intent(v.getContext(),EditProfile.class);
                i.putExtra("fullName",fullName.getText().toString());
                i.putExtra("email",email.getText().toString());
                i.putExtra("phone",phone.getText().toString());
                startActivity(i);
//
            }
        });

        //check later working or not , if not then remove
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(getActivity(),Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

}
