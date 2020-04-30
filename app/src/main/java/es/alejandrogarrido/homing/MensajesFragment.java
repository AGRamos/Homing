package es.alejandrogarrido.homing;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;


public class MensajesFragment extends Fragment {
    private ListView listOfMessages;
    private FirebaseListAdapter<MensajesChat> adapter;

    public MensajesFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listOfMessages = getActivity().findViewById(R.id.list_of_messages);
        FirebaseListOptions<MensajesChat> options =
                new FirebaseListOptions.Builder<MensajesChat>().setLayout(R.layout.mensajes)
                .setQuery(FirebaseDatabase.getInstance().getReference().child("chat"), MensajesChat.class).build();
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
                Log.i("LISTACHAT", "onViewCreated: " + model.getMensajesTexto());
                Log.i("LISTACHAT", "onViewCreated: " + model.getMensajesTexto());
                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMensajesTiempo()));
            }
        };
        listOfMessages.setAdapter(adapter);

    }
}