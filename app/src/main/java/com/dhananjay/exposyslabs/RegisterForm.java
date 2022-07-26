package com.dhananjay.exposyslabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.dhananjay.exposyslabs.Register.TAG;

public class RegisterForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseDatabase firebaseDatabase;
    EditText mName,mMail,mBranch,mPhone,mTen,mTwelve,mLoc,mClg;
    Button mSubmitbtn,mResetbtn;
    ProgressBar progressBar;

    boolean valid=true;
    String ug,pg,dur,sub,userId;
    String[] ugArr={"BE/BTECH","BCA","BBA","BSc Computers","Others"};
    String[] pgArr={"ME/MTECH/MCA","MSc","MBA","MBA IT","None"};
    String[] durationArr={"1W","2W","3W","4W","5W"};
    String[] subChoiceArr={"Software Development","Web Dev","IOT","Data Science","Full Stack Dev","App Dev","UI/UX","Content Writer","Marketing"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mName = findViewById(R.id.form_name);
        mMail= findViewById(R.id.form_email);
        mBranch= findViewById(R.id.form_branch);
        mPhone= findViewById(R.id.form_phone);
        mTen = findViewById(R.id.form_tenthpercent);
        mTwelve = findViewById(R.id.form_twelthpercent);
        mLoc = findViewById(R.id.form_loc);
        mClg = findViewById(R.id.form_college);
        mSubmitbtn=findViewById(R.id.submitBtn1);
        mResetbtn= findViewById(R.id.resetBtn);
        progressBar=findViewById(R.id.progressBar1);

        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();

        Spinner mUgSpin =findViewById(R.id.form_ugSpinner);
        Spinner mPgSpin =findViewById(R.id.form_pgSpinner);
        Spinner mChoiceSpin =findViewById(R.id.form_choiceSpinner);
        Spinner mDurationSpin =findViewById(R.id.form_durationSpin);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa1 = new ArrayAdapter(this,R.layout.simple_item,ugArr);
        ArrayAdapter aa2 = new ArrayAdapter(this,R.layout.simple_item,pgArr);
        ArrayAdapter aa3 = new ArrayAdapter(this,R.layout.simple_item,subChoiceArr);
        ArrayAdapter aa4 = new ArrayAdapter(this,R.layout.simple_item,durationArr);

        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mUgSpin.setAdapter(aa1);
        mPgSpin.setAdapter(aa2);
        mChoiceSpin.setAdapter(aa3);
        mDurationSpin.setAdapter(aa4);

        mUgSpin.setOnItemSelectedListener(this);
        mPgSpin.setOnItemSelectedListener(this);
        mChoiceSpin.setOnItemSelectedListener(this);
        mDurationSpin.setOnItemSelectedListener(this);

        mSubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fname = mName.getText().toString();
                final String mail = mMail.getText().toString().trim();
                final String branch = mBranch.getText().toString();
                final String clg = mClg.getText().toString();
                final String phone = mPhone.getText().toString();
                final String tenPer = mTen.getText().toString();
                final String twentlePer = mTwelve.getText().toString();
                final String loc = mLoc.getText().toString();
                final String[] alreadyRegMail = new String[1];


                if(TextUtils.isEmpty((fname))){
                    mName.setError("Email is Required");
                    return;
                }if(TextUtils.isEmpty((mail))){
                    mMail.setError("Email is Required");
                    return;
                }if(TextUtils.isEmpty((branch))){
                    mBranch.setError("Email is Required");
                    return;
                }if(TextUtils.isEmpty((clg))){
                    mClg.setError("Email is Required");
                    return;
                }
                valid=checkIfAlreadyReg(mail);
                if(valid){
                    progressBar.setVisibility(View.VISIBLE);

                    userId= fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("internshipDetails").document(userId);
                    Map<String,Object> details = new HashMap<>();
                    details.put("fName",fname);
                    details.put("email",mail);
                    details.put("branch",branch);
                    details.put("college",clg);
                    details.put("phone",phone);
                    details.put("tenPercent",tenPer);
                    details.put("twelvePercent",twentlePer);
                    details.put("location",loc);
                    details.put("ug",ug);
                    details.put("pg",pg);
                    details.put("internship",sub);
                    details.put("duration",dur);
                    progressBar.setVisibility(View.GONE);
                    documentReference.set(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("yoyoyo", "onSuccess: user Profile is created for "+ userId);
                            Toast.makeText(getApplicationContext(),"Response is submitted " ,Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("yoyoyo", "onFailure: " + e.toString());
                            Toast.makeText(getApplicationContext(),"Submission Failed " ,Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{

                    Toast.makeText(getApplicationContext(),"User Mail already registered ",Toast.LENGTH_LONG).show();
                }

            }
        });
        mResetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName.getText().clear();
                mMail.getText().clear();
                mBranch.getText().clear();
                mPhone.getText().clear();
                mTen.getText().clear();
                mTwelve.getText().clear();
                mLoc.getText().clear();
                mClg.getText().clear();
                ug=pg=sub=dur="";

                Toast.makeText(getApplicationContext(),"Values are reset",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkIfAlreadyReg(String mail) {
        DocumentReference df = fStore.collection("internshipDetails").document(fAuth.getCurrentUser().getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                valid= !mail.equals(documentSnapshot.getString("email"));
                Log.d("HELLO","email is received");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Receive nhi ho paya email ",Toast.LENGTH_LONG).show();
            }
        });
        return valid;
    }

    @Override
        public boolean onSupportNavigateUp() {
            onBackPressed();
            return true;
        }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.form_ugSpinner:
                ug=ugArr[position];
                break;
            case R.id.form_pgSpinner:
                pg=pgArr[position];
                break;
            case R.id.form_choiceSpinner:
                sub=subChoiceArr[position];
                break;
            case R.id.form_durationSpin:
                dur=durationArr[position];
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}