package com.example.chatprograming.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.chatprograming.Adapter.UsersAdapter;
import com.example.chatprograming.Models.Users;
import com.example.chatprograming.R;
import com.example.chatprograming.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//* buradaki fragment kodlarıyla chatteki aşağı doğru sıralınacak kullanıcıları yapmaya yariyicak
public class ChatsFragment extends Fragment {






    public ChatsFragment() {
        // Required empty public constructor
    }
    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater,container,false); //* chatsfragmenti bağlama işlevi fragment_chats.xml dosyasından alır
        database = FirebaseDatabase.getInstance();

        UsersAdapter adapter = new UsersAdapter(list,getContext()); //* adapter bildiğimiz anlamla yakın olarak  aracı olarak görevi var
        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()); //* linner layaout diğerlerinden farklı olarak parçalı bir şekilde şablon oluşturmamıza olanak sağlıyor
        binding.chatRecyclerView.setLayoutManager(layoutManager); //* oluşturduğumuz xml kodlarındaki bölümü buraya bağlıyoruz ilgili bölüm fragment_chats.xml dosyasında

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); //* herkes listeye gelmesin diye
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                    {
                        list.add(users);
                    }
                    list.add(users);
                }
                 adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //* veritabanındaki hangi tablodan alıcağımız
        return binding.getRoot();
    }
}