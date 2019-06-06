package br.com.octabus.View;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zanjou.http.request.Request;
import com.zanjou.http.request.RequestStateListener;
import com.zanjou.http.response.JsonResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.octabus.Constantes;
import br.com.octabus.R;
import br.com.octabus.Services.ServicoLocalizacao;
import br.com.octabus.Util.GPSTracker;
import br.com.octabus.Util.Network;
import br.com.octabus.Util.SessionManager;
import br.com.octabus.View.AppFiscalizacao.Home.HomeActivity;
import br.com.octabus.View.Login.LoginActivity;

public class AppFragment extends Fragment  {
    View view;
    Context context;
    Boolean serviceConnected = false;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_app, container, false);
        context = inflater.getContext();

        if(serviceConnected == false)
        {
            getActivity().startService(new Intent(getActivity(),ServicoLocalizacao.class));
            serviceConnected = true;
        }


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
                //drawer.closeDrawer(GravityCompat.START);

                if (id == R.id.navHome)
                {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                }
                else if (id == R.id.navPerfil)
                {

                }
                else if (id == R.id.navExit)
                {
                    deslogar();
                }
                else if (id == R.id.navSobre)
                {

                }
                else if (id == R.id.navAvaliar)
                {

                }
                else if (id == R.id.navTelefones)
                {

                }
                else if (id == R.id.navReportar)
                {

                }

                return true;
            }
        });

        View headerLayout = navigationView.getHeaderView(0);

        TextView nomeUsuario = (TextView) headerLayout.findViewById(R.id.menuNomeUsuario);
        TextView menuRe = (TextView) headerLayout.findViewById(R.id.menuRe);

        SessionManager sessionManager = new SessionManager(context);
        HashMap _dataUser = sessionManager.getUserDetails();
        sessionManager = null;

        nomeUsuario.setText(_dataUser.get("nome").toString());
        menuRe.setText(_dataUser.get("re").toString());

        return view;
    }

    public void deslogar()
    {
        final GPSTracker gps = new GPSTracker(context);

        if(!Network.isOnline(context))
        {
            Snackbar.make(view.findViewById(R.id.currentView), getString(R.string.semConexao), Snackbar.LENGTH_LONG).show();
        }
        else
        {
            if(!gps.canGetLocation())
            {
                gps.showSettingsAlert();
            }
            else
            {
                Double latitude = gps.getLatitude();
                Double longitude = gps.getLongitude();

                SessionManager sessionManager = new SessionManager(context);
                HashMap _dataUser = sessionManager.getUserDetails();
                sessionManager = null;

                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage(getString(R.string.aguarde));
                dialog.setCancelable(false);

                Request request = Request.create(Constantes.urlDeslogar);
                request.setMethod("POST")
                    .setTimeout(60) //2 Minutes
                    .addParameter("tokenLoginService", Constantes.keyApp)
                    .addParameter("idLogin", (int) _dataUser.get("idLogin"))
                    .addParameter("latitude", latitude)
                    .addParameter("longitude", longitude)
                    .setRequestStateListener(new RequestStateListener() {
                        @Override
                        public void onStart() {
                            dialog.show();
                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onConnectionError(Exception e) {
                            dialog.dismiss();

                            Snackbar.make(view.findViewById(R.id.currentView), getString(R.string.erroProcessamento), Snackbar.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    })
                    .setResponseListener(new JsonResponseListener() {
                        @Override
                        public void onOkResponse(JSONObject jsonObject) throws JSONException {

                            if((Boolean) jsonObject.get("success"))
                            {
                                SessionManager sessionManager = new SessionManager(context);
                                sessionManager.logoutUser();
                                sessionManager = null;

                                if(serviceConnected)
                                {
                                    getActivity().stopService(new Intent(getActivity(),ServicoLocalizacao.class));
                                    serviceConnected = true;
                                }

                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            else
                            {
                                Snackbar.make(view.findViewById(R.id.currentView), jsonObject.getString("response"), Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onErrorResponse(JSONObject jsonObject) throws JSONException {
                            Snackbar.make(view.findViewById(R.id.currentView), getString(R.string.naoFoiPossivelDeslogar), Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onParseError(JSONException e) {
                            Snackbar.make(view.findViewById(R.id.currentView), getString(R.string.erroProcessamento), Snackbar.LENGTH_LONG).show();
                            e.printStackTrace();

                        }
                    }).execute();
            }
        }
    }
}
