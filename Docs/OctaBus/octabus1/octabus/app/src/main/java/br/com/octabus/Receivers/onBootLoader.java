package br.com.octabus.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.com.octabus.Services.ServicoConexao;

public class onBootLoader extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("Script", "onReceive()");
		intent = new Intent(context, ServicoConexao.class);
		context.startService(intent);
	}

}
