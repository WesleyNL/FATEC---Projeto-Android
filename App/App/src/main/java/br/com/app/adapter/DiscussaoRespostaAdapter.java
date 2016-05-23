package br.com.app.adapter;

import br.com.app.activity.R;
import br.com.app.business.forum.discussao.Resposta;
import br.com.app.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Jefferson on 20/05/2016.
 */
public class DiscussaoRespostaAdapter extends RecyclerView.Adapter<DiscussaoRespostaAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private LinkedList<Resposta> resposta = null;

    public DiscussaoRespostaAdapter(Context context, int layout, LinkedList<Resposta> resposta) {
        this.context = context;
        this.layout = layout;
        this.resposta = resposta;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Resposta r = resposta.get(position);

        Utils.carregarImagem(context, r.getContato().getProfilePictureURL(), holder.imgPerfil);
        holder.lblIDResposta.setText("#" + r.getIDResposta());
        holder.lblAutor.setText(" Por: " + r.getContato().getFirstName());
        holder.lblDataHora.setText(Utils.formatDate(r.getDataHora(), "dd/MM/yyyy HH:mm:ss"));
        holder.lblResposta.setText(r.getResposta());
    }

    @Override
    public int getItemCount() {
        return resposta.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPerfil;
        public TextView lblIDResposta;
        public TextView lblAutor;
        public TextView lblDataHora;
        public TextView lblResposta;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            imgPerfil = (ImageView) itemLayoutView.findViewById(R.id.imgPerfil);
            lblIDResposta = (TextView) itemLayoutView.findViewById(R.id.lblIDResposta);
            lblAutor = (TextView) itemLayoutView.findViewById(R.id.lblAutor);
            lblDataHora = (TextView) itemLayoutView.findViewById(R.id.lblDataHora);
            lblResposta = (TextView) itemLayoutView.findViewById(R.id.lblResposta);
        }
    }
}