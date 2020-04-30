package es.alejandrogarrido.homing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class MainActivity extends AppCompatActivity implements OnAveriaInteractionListener, View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase db;
    private SharedPreferences prefs;
    private ArrayList<Usuarios> usuariosArrayList;
    private CargarUsuarios carga;
    private FirebaseListAdapter<MensajesChat> adapter;

    private BottomNavigationView nav;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        carga = new CargarUsuarios(this);
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
                carga.leerUsuarios();
                nav = findViewById(R.id.navigationView);
                nav.inflateMenu(R.menu.manu);
                nav.setOnNavigationItemSelectedListener(this);
                if (i.getStringExtra("tipo").equals("registro"))
                    registerUser(i.getStringExtra("email"), i.getStringExtra("name"), i.getStringExtra("tlf"));




        }
    }

    public void mostrarMensajes(){
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        FirebaseListOptions<MensajesChat> options = new FirebaseListOptions.Builder<MensajesChat>()
                .setLayout(R.layout.mensajes)//Note: The guide doesn't mention this method, without it an exception is thrown that the layout has to be set.
                .setQuery(FirebaseDatabase.getInstance().getReference(), MensajesChat.class)
                .build();
        adapter = new FirebaseListAdapter<MensajesChat>(options) {
            @Override
            protected void populateView(View v, MensajesChat model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMensajesTexto());
                messageUser.setText(model.getMensajesUsuario());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMensajesTiempo()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }

    public void enviarMensaje(){
        EditText input = (EditText)findViewById(R.id.input);
        FirebaseDatabase.getInstance()
                .getReference().child("chat")
                .push()
                .setValue(new MensajesChat(input.getText().toString(),
                        mFirebaseUser.getDisplayName())
                );

        // Clear the input
        input.setText("");
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
        rating.add("");
        myRef.child("users").child(mFirebaseUser.getUid()).setValue(new Usuarios(mFirebaseUser.getUid(), name, name, email, new Direccion("", "", "",
                "", new Geo("", "")), tlf, "", new Rating(rating)));
    }



    @Override
    public void onAveriaClick(Usuarios usuarios) {
        Toast.makeText(this, "He pulsado a: " + usuarios.getNombre(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.fotoPerfil)){
            Toast.makeText(this, "Foto", Toast.LENGTH_SHORT).show();
        }

        if (v == findViewById(R.id.cirInfPersButton)){
            Toast.makeText(this, "Boton info", Toast.LENGTH_SHORT).show();
        }

        if (v == findViewById(R.id.cirTlfButton)){
            Toast.makeText(this, "Boton tlf", Toast.LENGTH_SHORT).show();
        }

        if (v == findViewById(R.id.cirEmailButton)){
            Toast.makeText(this, "Boton email", Toast.LENGTH_SHORT).show();
        }

        if (v == findViewById(R.id.fab)){
            Toast.makeText(this, "Boton enviar", Toast.LENGTH_SHORT).show();
            enviarMensaje();
        }
            
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                carga.reemmplazaUsuarios();
                break;
            case R.id.navigation_add:
                break;
            case R.id.navigation_message:
                //mostrarMensajes();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new MensajesFragment()).commit();
                break;
            case R.id.navigation_search:
                break;
            case R.id.navigation_user:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ConfigFragment()).commit();
                break;
        }
        return true;
    }
}
