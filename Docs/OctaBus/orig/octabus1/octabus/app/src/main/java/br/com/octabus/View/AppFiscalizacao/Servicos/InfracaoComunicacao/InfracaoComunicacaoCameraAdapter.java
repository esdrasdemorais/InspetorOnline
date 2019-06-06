package br.com.octabus.View.AppFiscalizacao.Servicos.InfracaoComunicacao;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import br.com.octabus.R;

/**
 * Created by marcatti on 18/03/17.
 */

public class InfracaoComunicacaoCameraAdapter extends BaseAdapter {
    private Context context;
    private List listaItem;

    public InfracaoComunicacaoCameraAdapter(Context context, List listaItem) {
        this.context = context;
        this.listaItem = listaItem;
    }

    public int getCount() {
        return listaItem.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        Bitmap imagem = (Bitmap) listaItem.get(position);
        final View view;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.adapter_infracao_comunicao_imagem_camera, parent, false);
        } else {
            view = convertView;
        }

        ImageView imageViewDelete = (ImageView) view.findViewById(R.id.imageViewDelete);

        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaItem.remove(position);
                notifyDataSetChanged();
            }
        });


        ImageView imagemItem = (ImageView) view.findViewById(R.id.imagemItem);
        imagemItem.setImageBitmap(imagem);

        return view;
    }

}
