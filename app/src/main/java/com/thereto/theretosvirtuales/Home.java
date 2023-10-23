package com.thereto.theretosvirtuales;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.thereto.theretosvirtuales.databinding.ActivityHomeBinding;
import com.thereto.theretosvirtuales.ui.como_jugar.ComoJugarFragment;
import com.thereto.theretosvirtuales.ui.contactanos.ContactanosFragment;
import com.thereto.theretosvirtuales.ui.eTickets.ETicketsFragment;
import com.thereto.theretosvirtuales.ui.inicio.InicioFragment;
import com.thereto.theretosvirtuales.ui.login.LoginFragment;
import com.thereto.theretosvirtuales.ui.premios.PremiosFragment;
import com.thereto.theretosvirtuales.ui.quienes_somos.QuienesSomosFragment;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private ImageView amburguesaImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                )
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);

        NavigationUI.setupWithNavController(navigationView, navController);
        amburguesaImageView = findViewById(R.id.amburguesaImageView);
        amburguesaImageView.setOnClickListener(v -> {
            drawer.openDrawer(GravityCompat.START);
        });
        binding.ingresoButton.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
            drawer.closeDrawer(GravityCompat.START);
        });
        binding.premiosButton.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home, new PremiosFragment())
                    .addToBackStack(null)
                    .commit();
            drawer.closeDrawer(GravityCompat.START);
        });
        binding.comoJugarButton.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home, new ComoJugarFragment())
                    .addToBackStack(null)
                    .commit();
            drawer.closeDrawer(GravityCompat.START);
        });
        binding.quienesSomosButton.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home, new QuienesSomosFragment())
                    .addToBackStack(null)
                    .commit();
            drawer.closeDrawer(GravityCompat.START);
        });
        binding.eTicketsButton.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home, new ETicketsFragment())
                    .addToBackStack(null)
                    .commit();
            drawer.closeDrawer(GravityCompat.START);
        });
        binding.contactanosButton.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home, new ContactanosFragment())
                    .addToBackStack(null)
                    .commit();
            drawer.closeDrawer(GravityCompat.START);
        });
        binding.logoImageView.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home, new InicioFragment())
                    .addToBackStack(null)
                    .commit();
            drawer.closeDrawer(GravityCompat.START);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}