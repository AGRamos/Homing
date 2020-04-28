package es.alejandrogarrido.homing.ui.AdaptadorUsuarios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import es.alejandrogarrido.homing.R;
import es.alejandrogarrido.homing.ui.usuarios.Usuarios;


public class MyAveriaRecyclerViewAdapter extends RecyclerView.Adapter<MyAveriaRecyclerViewAdapter.ViewHolder> {

    private final List<Usuarios> mValues;
    private final OnAveriaInteractionListener mListener;
    private Context ctx;

    public MyAveriaRecyclerViewAdapter(Context context, List<Usuarios> items, OnAveriaInteractionListener listener) {
        ctx = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.usuarios_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.textViewTitulo.setText(holder.mItem.getNombre());
        holder.textViewModeloCoche.setText(holder.mItem.getEmail());
        holder.textViewNumPresupuestos.setText(holder.mItem.getTlf());

        Glide.with(ctx)
                .load("https://upload.wikimedia.org/wikipedia/commons/d/d3/User_Circle.png")
                .into(holder.imageViewFotoAveria);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onAveriaClick(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewTitulo;
        public final TextView textViewModeloCoche;
        public final TextView textViewNumPresupuestos;
        public final ImageView imageViewFotoAveria;
        public Usuarios mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewTitulo = (TextView) view.findViewById(R.id.textViewTitulo);
            textViewModeloCoche = (TextView) view.findViewById(R.id.textViewModeloCoche);
            textViewNumPresupuestos = (TextView) view.findViewById(R.id.textViewPresupuesto);
            imageViewFotoAveria = (ImageView) view.findViewById(R.id.imageViewFoto);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewTitulo.getText() + "'";
        }
    }
}
