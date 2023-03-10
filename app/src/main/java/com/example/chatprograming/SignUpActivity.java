package com.example.chatprograming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatprograming.Models.Users;
import com.example.chatprograming.databinding.ActivitySignUpBinding;
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

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;  //*binding bağlama burası
    private FirebaseAuth mAuth;
    FirebaseDatabase database; //* database oluşturuldu
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;
    //*FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase; //* buu fazla gereksiz olabilir
    BeginSignInRequest signInRequest;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    SignInButton btSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater()); //*aktivite bağlama işlevi  yukarıda
        setContentView(binding.getRoot());
        // Firebase Auth'u başlat
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(); //* databse başlat


        getSupportActionBar().hide(); /*normalde yukarıda duran yani empty aktivitede üste duran barı gizler*/

        mAuth = FirebaseAuth.getInstance(); //* firebase işlemi getirme istekde bulunuyor
        firebaseDatabase = firebaseDatabase.getInstance(); //* örneğin sahibiyiz şu anda yani gelen bilginin

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Giriş");
        progressDialog.setMessage("Lütfen bekleyin \n ,Giriş Yapılıyor");
        //* google giriş yapmak için kod satırı başlangıç bunlar google tarafından veriliyor dökümantasyon
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Üyelik Oluşturuluyor");
        progressDialog.setMessage("Hesabınızı oluşturduk");
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() { //*üye ol butonuna tıklandığında yapılıcak işlemler
            @Override
            public void onClick(View view) {
                if (!binding.txtUserName.getText().toString().isEmpty() && !binding.txtEmail.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty()) {
                    //*üyelik giriş yerlerindeki bilgiler boş değilse buraya girer
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(), binding.txtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() { //* girilen bilgileri dinleme işlevi yapılmakta
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss(); //* bu kayıt olma sırasında döngüye girmesine ve orda takılmasına engel oluyor , yani olumlu cevap geldiğinde işlevi sonlandırıyor
                                    if (task.isSuccessful()) {
                                        //* işlem başarılı ise
                                        //* kullanıcı bilgilerini işleyeceğiz
                                        Users user = new Users(binding.txtUserName.getText().toString(), binding.txtEmail.getText().toString(), binding.txtPassword.getText().toString());
                                        String id = task.getResult().getUser().getUid(); //* firebasde benzersiz olan id yi getirir lazım çünkü kullanıcıların isimleri soyisimleri  aynı olabilir
                                        database.getReference().child("Users").child(id).setValue(user); //* userda aldığı bilgileri database e işleyecek realtime database e


                                        Toast.makeText(SignUpActivity.this, "Üyelik Başarıyla Oluşturuldu", Toast.LENGTH_SHORT).show();


                                    } else {
                                        //* hata gelirse hatayı bildiricek
                                        Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                    //* üste kutulardan alınan bilgilerle üyelik oluşturma işlevi yapılmakta


                } else {
                    Toast.makeText(SignUpActivity.this, "Lütfen Bilgileri Eksiksiz Giriniz ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.txtAllreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignlnActivity.class);
                startActivity(intent);

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
   int RC_SIGN_IN =2;

    private void signIn() {
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId()); //* firebaseAuthWithGoogle böyledi değişti
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in faild", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Check condition
                        if (task.isSuccessful()) {
                            Log.d("TAG", "SignInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            Users users = new Users();
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilePic(user.getPhotoUrl().toString());
                            firebaseDatabase.getReference().child("Users").child(user.getUid()).setValue(users);
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignUpActivity.this, "Google ile  giriş yapılıyor ", Toast.LENGTH_SHORT).show();


                        } else {
                            // When task is unsuccessful
                            // Display Toast
                            Log.w("TAG", "SignInWithCredential:failure", task.getException());


                        }
                    }
                });

    }
}