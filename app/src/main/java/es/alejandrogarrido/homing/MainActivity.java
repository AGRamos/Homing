package es.alejandrogarrido.homing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import es.alejandrogarrido.homing.ui.AdaptadorUsuarios.OnAveriaInteractionListener;
import es.alejandrogarrido.homing.ui.usuarios.Direccion;
import es.alejandrogarrido.homing.ui.usuarios.Geo;
import es.alejandrogarrido.homing.ui.usuarios.Rating;
import es.alejandrogarrido.homing.ui.usuarios.Usuarios;

public class MainActivity extends AppCompatActivity implements OnAveriaInteractionListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase db;
    protected SharedPreferences prefs;
    protected ArrayList<Usuarios> usuariosArrayList;

    private BottomNavigationView nav;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        i = getIntent();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (mFirebaseUser == null && i == null) {
            loadLogInView();
        } else {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                leerUsuarios();
                nav = findViewById(R.id.navigationView);
                nav.inflateMenu(R.menu.manu);
                nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_home:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, new UsuariosFragment(usuariosArrayList)).commit();
                                break;
                            case R.id.navigation_add:
//                                try {
//                                    includesForUploadFiles();
//                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
//                                }
                                break;
                            case R.id.navigation_message:
                                break;
                            case R.id.navigation_search:
                                break;

                            case R.id.navigation_user:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ConfigFragment()).commit();
                                break;
                        }
                        return true;
                    }
                });
        }
    }

    private void loadLogInView() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void registerUser(String email, String name, String tlf) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        List rating = new ArrayList<String>();
        rating.add("3.2");
        rating.add("4.2");
        rating.add("2.2");
        myRef.child("users").child(mFirebaseUser.getUid()).setValue(new Usuarios(mFirebaseUser.getUid(), name, name, email, new Direccion("fake adress", "36", "Talavera",
                "45600", new Geo("-37.3159", "81.1496")), tlf, "proveedor", new Rating(rating)));
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
                    usuariosArrayList.add(usu);
                }
                for (Usuarios usu:
                     usuariosArrayList) {
                    Log.i("ERRORLEER", "onDataChange: " + usu.getId());
                }
                getSupportFragmentManager().beginTransaction().add(R.id.container, new UsuariosFragment(usuariosArrayList)).commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onAveriaClick(Usuarios usuarios) {
        Toast.makeText(this, "He pulsado a: " + usuarios.getNombre(), Toast.LENGTH_SHORT).show();
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

    public void includesForUploadFiles() throws FileNotFoundException {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child("images/" + mFirebaseUser.getUid() + ".png");
        ImageView imageView = (ImageView)findViewById(R.id.imageViewFoto);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(MainActivity.this, "No se ha subido correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "Se ha subido correctamente", Toast.LENGTH_SHORT).show();
            }
        });
        // [END upload_memory]}
    }
}
