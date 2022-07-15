package com.example.consulta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.interfaz.UsuariosApi;
import com.example.modelo.Usuarios;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ListView listaPersonas;
    ListView listaUsuarios;
    ArrayList<String> titulos=new ArrayList<>();
    ArrayList<String> titulos2=new ArrayList<>();
    ArrayAdapter arrayAdapter;
    ArrayAdapter arrayAdapter2;
    EditText idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        obtenerLista();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titulos);
        arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titulos2);
        idUsuario= (EditText) findViewById(R.id.idUsuario);
        listaPersonas=(ListView)  findViewById(R.id.listusers);
        listaUsuarios=(ListView)  findViewById(R.id.listusers1);
        listaPersonas.setAdapter(arrayAdapter);
        listaUsuarios.setAdapter(arrayAdapter2);
        idUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                obtenerUsuario(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    private void obtenerLista() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UsuariosApi usuarioApi = retrofit.create(UsuariosApi.class);
        Call <List<Usuarios>> calllista =usuarioApi.getUsuarios();
        calllista.enqueue(new Callback<List<Usuarios>>() {
            @Override
            public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                for (Usuarios usuarios : response.body()){
                    Log.i(usuarios.getTitle(), "onResponse");
                    titulos.add(usuarios.getTitle());
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Usuarios>> call, Throwable t) {
            }
        });
    }

    private void obtenerUsuario( String texto) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UsuariosApi usuariosApi = retrofit.create(UsuariosApi.class);
        Call<Usuarios> callLista = usuariosApi.getUsuarios(texto);
        callLista.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {
                Log.i(response.body().getTitle(), "onResponse");
                titulos2.add(response.body().getTitle());
                arrayAdapter2.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {
            }
        });
    }
}