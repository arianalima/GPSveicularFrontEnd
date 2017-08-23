package com.example.bernardo.myapplication;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getUsuario();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

    }

    public void getUsuario(){
        //IPs/endereços
        //bernardo: http://192.168.25.55:8080/WhereverIgo/rest/UserService/
        //ari: http://10.246.42.39:8080/UserManagement/rest/UserService/ ou 192.168.25.234
        //leut: http://10.246.13.221:8080/WhereverIGo/rest/UsuarioService/ ou 192.168.31.191

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.25.234:8080/CircularGPS/rest/LocalService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LocalService localService = retrofit.create(LocalService.class);


        Call<ArrayList<Float>> listaCall = localService.listRepos();

        listaCall.enqueue(new Callback<ArrayList<Float>>() {
            @Override
            public void onResponse(Call<ArrayList<Float>> call, Response<ArrayList<Float>> response) {
                if(response.isSuccessful()){
                    ArrayList<Float> lista = response.body();
                    if(!lista.isEmpty()){
                        LatLng sydney = new LatLng(lista.get(0), lista.get(1));
                        mMap.addMarker(new MarkerOptions().position(sydney).title("Circular"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                    }else{
                        Toast.makeText(getApplicationContext(),"" + response.message() ,Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"" + response.code() ,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Float>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
