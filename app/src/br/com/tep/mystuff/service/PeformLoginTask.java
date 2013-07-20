package br.com.tep.mystuff.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import br.com.tep.mystuff.Main;
import br.com.tep.mystuff.converter.ResponseConverter;
import br.com.tep.mystuff.converter.UsuarioConverter;
import br.com.tep.mystuff.dto.Response;
import br.com.tep.mystuff.dto.ResponseStatus;
import br.com.tep.mystuff.util.Constant;
import br.com.tep.mystuff.util.WebClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;


public class PeformLoginTask extends AsyncTask<Object, Object, String> {

	private final Context context;
	private ProgressDialog progressDialog;
	private String telefone;
	private String senha;

	public PeformLoginTask(Context context, String telefone, String senha) {
		this.context = context;
		this.telefone = telefone;
		this.senha = senha;
	}

	@Override
	protected String doInBackground(Object... params) {

		try {
			String url = "http://192.168.1.40:8086/mystuff/login";
			String json = UsuarioConverter.toJSON(telefone, senha,"");

			WebClient wc = new WebClient(url);

			return wc.post(json);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	@Override
	protected void onPostExecute(String result) {
		progressDialog.dismiss();

		if (!result.equals("")) {
			Response response = ResponseConverter.toObject(result);

			if (response.getStatus().equals(ResponseStatus.ERROR)) {
				Toast.makeText(context,
						response.getMessages().get(0).getValue(),
						Toast.LENGTH_SHORT).show();
			} else {
				mainAction();
			}
			
		} else {
			Toast.makeText(context, Constant.ERROR_SERVIDOR, Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void mainAction() {
		Intent i = new Intent(context, Main.class);
		context.startActivity(i);
		((Activity) context).finish();
	}

	@Override
	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(context, "Aguarde...",
				"Envio de dados para servidor", true, true);
	}

}
