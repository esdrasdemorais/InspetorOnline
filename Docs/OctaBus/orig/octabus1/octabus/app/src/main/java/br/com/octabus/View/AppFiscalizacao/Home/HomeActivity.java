package br.com.octabus.View.AppFiscalizacao.Home;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.octabus.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle(getResources().getString(R.string.app_name));

        HomeContentFragment fragment1 = new HomeContentFragment();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.fragment_place, fragment1);
        fragmentTransaction1.commit();

    }
}
