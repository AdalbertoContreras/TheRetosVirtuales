package com.thereto.theretosvirtuales;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thereto.theretosvirtuales.databinding.ActivityHomeBinding;
import com.thereto.theretosvirtuales.dialog.LogoutConfirmationDialogFragment;
import com.thereto.theretosvirtuales.interfas.LogoutDialogListener;
import com.thereto.theretosvirtuales.interfas.OnTaskCompleteListener;
import com.thereto.theretosvirtuales.ui.como_jugar.ComoJugarFragment;
import com.thereto.theretosvirtuales.ui.contactanos.ContactanosFragment;
import com.thereto.theretosvirtuales.ui.eTickets.ETicketsFragment;
import com.thereto.theretosvirtuales.ui.inicio.InicioFragment;
import com.thereto.theretosvirtuales.ui.login.LoginFragment;
import com.thereto.theretosvirtuales.ui.premios.PremiosFragment;
import com.thereto.theretosvirtuales.ui.quienes_somos.QuienesSomosFragment;

public class Home extends AppCompatActivity  implements LogoutDialogListener, OnTaskCompleteListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private ImageView amburguesaImageView;
    private FirebaseAuth mAuth;
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
            LoginFragment loginFragment = new LoginFragment();
            loginFragment.setAutenticarListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home, loginFragment)
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
        binding.salirButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager(); // Para actividades
            LogoutConfirmationDialogFragment dialogFragment = new LogoutConfirmationDialogFragment();
            dialogFragment.show(fragmentManager, "logout_dialog");
            FirebaseAuth.getInstance().signOut();
        });
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI();
    }

    public void updateUI() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            binding.miCuentaButton.setVisibility(View.VISIBLE);
            binding.salirButton.setVisibility(View.VISIBLE);
            binding.usuarioButton.setVisibility(View.VISIBLE);
            binding.ingresoButton.setVisibility(View.GONE);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Name
                String name = user.getDisplayName();
                binding.userTextView.setText(name);
            }
        } else {
            binding.usuarioButton.setVisibility(View.GONE);
            binding.miCuentaButton.setVisibility(View.GONE);
            binding.salirButton.setVisibility(View.GONE);
            binding.ingresoButton.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void onLogoutConfirmed() {
        Toast.makeText(getBaseContext(), "Cerrado sesion", Toast.LENGTH_SHORT).show();
        updateUI();
    }

    @Override
    public void onLogoutCancelled() {

    }

    @Override
    public void onTaskComplete() {
        updateUI();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_home, new InicioFragment())
                .addToBackStack(null)
                .commit();
    }
}