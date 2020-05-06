package es.alejandrogarrido.homing;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import es.alejandrogarrido.homing.ui.AdaptadorUsuarios.MyAveriaRecyclerViewAdapter;
import es.alejandrogarrido.homing.ui.AdaptadorUsuarios.OnAveriaInteractionListener;
import es.alejandrogarrido.homing.ui.usuarios.Usuarios;

public class UsuariosFragment extends Fragment {

    OnAveriaInteractionListener mListener;
    List<Usuarios> usuariosList;
    Usuarios usu;

    public UsuariosFragment() {
        Log.i("ERRORCARGA", "ENTRO");
    }

    public UsuariosFragment(List<Usuarios> usuariosList) {
        this.usuariosList = usuariosList;
    }



    public UsuariosFragment(Usuarios usu) {
        this.usu = usu;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuario_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyAveriaRecyclerViewAdapter(getActivity(), usuariosList, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAveriaInteractionListener) {
            mListener = (OnAveriaInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAveriaInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
