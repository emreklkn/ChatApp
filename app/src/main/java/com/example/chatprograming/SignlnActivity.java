package com.example.chatprograming;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatprograming.Models.Users;
import com.example.chatprograming.databinding.ActivitySignlnBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;


public class SignlnActivity<idToken> extends AppCompatActivity {
    ActivitySignlnBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth; //* doğrulama işlevi için
    FirebaseDatabase firebaseDatabase;
    BeginSignInRequest signInRequest;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    SignInButton btSignIn;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignlnBinding.inflate(getLayoutInflater()); //* içini doldurma şişirme
        setContentView(binding.getRoot());




        getSupportActionBar().hide(); /* üsteki action barı engeller*/


        mAuth =FirebaseAuth.getInstance(); //* firebase işlemi getirme istekde bulunuyor
        firebaseDatabase = firebaseDatabase.getInstance(); //* örneğin sahibiyiz şu anda yani gelen bilginin

        progressDialog = new ProgressDialog(SignlnActivity.this);
        progressDialog.setTitle("Giriş");
        progressDialog.setMessage("Lütfen bekleyin \n ,Giriş Yapılıyor");


        //* google giriş yapmak için kod satırı başlangıç bunlar google tarafından veriliyor dökümantasyon
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        //* google bitiş

        //facebook başlangıç







        ///* facebook bitiş

        





        /**/


        binding.btnSignIn.setOnClickListener(new View.OnClickListener() { //* dinleyici oluşturduk dinleyecek
            @Override
            public void onClick(View view) {
                if(!binding.txtEmail.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty()){

                    progressDialog.show(); //* izliyor bağlantıyı o dönen çubuğu oluşturuyor
                    mAuth.signInWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();//* döngüye girmesni engelliyor
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(SignlnActivity.this, MainActivity.class); //* başarılı şekilde gerçekleştiğinde mainactivitye gidicek

                                        startActivity(intent);
                                        


                                    }
                                    else {
                                        Toast.makeText(SignlnActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        //* Toast bildirim yazı çıkarıyor ekrana burda hatayı alarak ileride önünce geçicez hata yakalamayı kolaylaştırmak gibi


                                    }

                                }
                            });



                    




                }
                else{
                    Toast.makeText(SignlnActivity.this, "Lütfen Bilgileri Giriniz", Toast.LENGTH_SHORT).show();




                }




            }
        });
        if(mAuth.getCurrentUser()!=null){
            Intent intent =new Intent(SignlnActivity.this,MainActivity.class);
            startActivity(intent);
        }
        binding.txtClickSignUp.setOnClickListener(new View.OnClickListener() { //*üye ol yazısına tıklayıncaO
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignlnActivity.this, SignUpActivity.class);//* buradan buraya gider
                startActivity(intent); //* intenti yani yönlendiriciyi başlat


            }
        });
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() { //* google butonunu dinliyoruz tıkladığı anda olacak işlemler
            @Override
            public void onClick(View view) {
                signIn();

            }
        });




    }

   /* private void SignIn(){
        Intent intent=mGoogleSignInClient.getSignInIntent();
        // Start activity for result

        startActivityForResult(intent ,RC_SIGN_IN);
        }
        */
    int RC_SIGN_IN =1;
    private void signIn(){
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent ,RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode , Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                GoogleSignInAccount account =task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" +account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            }
            catch(ApiException e)
            {
                Log.w("TAG" , "Google sign in faild" ,e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential =GoogleAuthProvider.getCredential(idToken,null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Check condition
                        if(task.isSuccessful())
                        {
                            Log.d("TAG", "SignInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            Users users = new Users();
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilePic(user.getPhotoUrl().toString());
                            firebaseDatabase.getReference().child("Users").child(user.getUid()).setValue(users);
                            Intent intent = new Intent(SignlnActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignlnActivity.this, "Google ile  giriş yapılıyor ", Toast.LENGTH_SHORT).show();


                        }
                        else
                        {
                            // When task is unsuccessful
                            // Display Toast
                            Log.w("TAG", "SignInWithCredential:failure", task.getException());


                        }
                    }
                });




    }






}