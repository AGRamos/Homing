package es.alejandrogarrido.homing;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.alejandrogarrido.homing.ui.usuarios.Direccion;
import es.alejandrogarrido.homing.ui.usuarios.Geo;
import es.alejandrogarrido.homing.ui.usuarios.Rating;
import es.alejandrogarrido.homing.ui.usuarios.Usuarios;

public class CargarUsuarios  {

    protected ArrayList<Usuarios> usuariosArrayList;
    MainActivity main;
    private LatLng currentLocation;

    private static final int REQUEST_CODE = 101;

    public CargarUsuarios(MainActivity main, LatLng currentLocation) {
        this.main = main;
        this.currentLocation = currentLocation;
    }

    public void leerUsuarios() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuariosArrayList = new ArrayList<>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Usuarios usu = cargarUsuario(child);

                    float[] dist = new float[1];
                    Location.distanceBetween(currentLocation.latitude, currentLocation.longitude,Double.parseDouble(usu.getDirecc().getGeo().getLat()), Double.parseDouble(usu.getDirecc().getGeo().getLng()),dist);
                    if(dist[0]/10000 < 1){
                        usuariosArrayList.add(usu);
                    }
                    Log.i("ERRORLEER", "onDataChange: " + usu.getId());
                }
                main.getSupportFragmentManager().beginTransaction().add(R.id.container, new UsuariosFragment(usuariosArrayList)).commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void reemmplazaUsuarios() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuariosArrayList = new ArrayList<>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Usuarios usu = cargarUsuario(child);
                    float[] dist = new float[1];
                    Location.distanceBetween(currentLocation.latitude, currentLocation.longitude,Double.parseDouble(usu.getDirecc().getGeo().getLat()), Double.parseDouble(usu.getDirecc().getGeo().getLng()),dist);
                    if(dist[0]/10000 < 1){
                        usuariosArrayList.add(usu);
                    }
                    Log.i("ERRORLEER", "onDataChange: " + usu.getId());
                }
                main.getSupportFragmentManager().beginTransaction().replace(R.id.container, new UsuariosFragment(usuariosArrayList)).commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public Usuarios cargarUsuario(DataSnapshot dataSnapshot){
        String id = (String) dataSnapshot.child("id").getValue();
        String nombre= (String) dataSnapshot.child("nombre").getValue();
        String usuario= (String) dataSnapshot.child("usuario").getValue();
        String email= (String) dataSnapshot.child("email").getValue();
        String tlf = (String) dataSnapshot.child("tlf").getValue();
        String tipo = (String) dataSnapshot.child("tipo").getValue();

        String calle = (String) dataSnapshot.child("direcc").child("calle").getValue();
        String numero = (String) dataSnapshot.child("direcc").child("numero").getValue();
        String ciudad = (String) dataSnapshot.child("direcc").child("ciudad").getValue();
        String cp = (String) dataSnapshot.child("direcc").child("cp").getValue();
        Geo geo = new Geo((String) dataSnapshot.child("direcc").child("geo").child("lat").getValue(), (String) dataSnapshot.child("direcc").child("geo").child("lng").getValue());
        Direccion direcc = new Direccion(calle, numero, ciudad, cp, geo);

        List notas = (List) dataSnapshot.child("ratings").child("notas").getValue();
        Rating rating = new Rating(notas);

        Usuarios usu = new Usuarios(id, nombre, usuario, email, direcc, tlf, tipo, rating);
        return usu;
    }




}
