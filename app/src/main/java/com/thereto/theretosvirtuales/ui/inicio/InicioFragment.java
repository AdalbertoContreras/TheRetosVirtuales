package com.thereto.theretosvirtuales.ui.inicio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thereto.model.Game;
import com.thereto.theretosvirtuales.R;
import com.thereto.theretosvirtuales.databinding.FragmentHomeBinding;
import com.thereto.theretosvirtuales.helpers.Fecha;
import com.thereto.theretosvirtuales.interfas.OnTaskCompleteListener;
import com.thereto.theretosvirtuales.ui.eTickets.ETicketsFragment;
import com.thereto.theretosvirtuales.ui.login.LoginFragment;
import com.thereto.theretosvirtuales.ui.ver_reto.VerRetoFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InicioFragment extends Fragment implements OnTaskCompleteListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FragmentHomeBinding binding;
    private View mCustomView;
    private List<Game> contenido = new ArrayList<>();

    private Game[] juegosEnVista = {};
    private float x1, x2;
    static final int MIN_DISTANCE = 20;
    private FirebaseAuth mAuth;
    private int pos = 0;
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mostrarVideo();
        binding.jugarGameOne.setOnClickListener(v -> {
            verJuego(juegosEnVista[0]);
        });
        binding.jugarGameTwo.setOnClickListener(v -> {
            verJuego(juegosEnVista[1]);
        });
        binding.jugarGameThree.setOnClickListener(v -> {
            verJuego(juegosEnVista[2]);
        });
        binding.ingresoRegistroButtonHeader.setOnClickListener(v -> {
            LoginFragment loginFragment = new LoginFragment();
            loginFragment.setAutenticarListener(this);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_home, loginFragment)
                    .addToBackStack(null)
                    .commit();
        });
        binding.premiosButtonHeader.setOnClickListener(v -> {
            // Define la URL que deseas abrir en el navegador
            String url = "https://theretos.co/app/tabs/marketPlace/store";
            navegarPagina(url);
        });
        binding.comoParticiparButtonHeader.setOnClickListener(v -> {
            // Define la URL que deseas abrir en el navegador
            String url = "https://theretos.co/about";
            navegarPagina(url);
        });
        binding.eTicketsButtonHeader.setOnClickListener(v -> {
            // Define la URL que deseas abrir en el navegador
            String url = "https://theretos.co/app/tabs/marketPlace";
            navegarPagina(url);
        });
        binding.crearRetoButton.setOnClickListener(v -> {
            // Define la URL que deseas abrir en el navegador
            String url = "https://theretos.co/app/tabs/marketPlace";
            navegarPagina(url);
        });
        Objects.requireNonNull(binding.listaContenerLayout).setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    return true;
                case MotionEvent.ACTION_UP:
                    x2 = event.getX();
                    float deltaX = x2 - x1;
                    if (deltaX > 20 || deltaX < -20) {
                        if (x2 < x1) {
                            if ((pos + 1) < contenido.size()) {
                                pos ++;
                            }
                        } else {
                            if (pos > 0) {
                                pos --;
                            }
                        }
                        imprimirLista();
                        return true;
                    }
            }
            return false;
        });
        consultarJuegos();
        updateUi();
        return root;
    }

    public void navegarPagina(String url) {

        // Crea un intent con la acción ACTION_VIEW
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Establece la URL en el intent
        intent.setData(Uri.parse(url));

        // Verifica si hay una aplicación que pueda manejar la acción
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Abre el navegador con la URL especificada
            startActivity(intent);
        } else {
            // No se encontró ninguna aplicación para manejar la acción
            // Puedes mostrar un mensaje de error o manejarlo de otra manera
        }
    }

    private void mostrarVideo() {
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // URL del video de YouTube
        String videoId = "kWiiMoafKME";

        // Cargar el video de YouTube en el WebView
        binding.webView.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
    }

    private void consultarJuegos() {
        Query consultaMayor = db.collection("challenges_virtual");

        consultaMayor.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            contenido = new ArrayList<>();

                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                // Convierte cada documento en un objeto Ticket
                                Game ticket = new Game();

                                ticket.id = document.getId();
                                ticket.accumulated_tickets = document.getLong("accumulated_tickets").intValue();
                                ticket.date_creation = document.getString("date_creation");
                                ticket.date_limit = document.getString("date_limit");
                                ticket.description = document.getString("description");
                                ticket.name_challeng = document.getString("name_challeng");
                                ticket.background = document.getString("background");
                                ticket.icon = document.getString("icon");

                                contenido.add(ticket);
                            }
                            imprimirLista();
                            // Ahora, "tickets" es una lista que contiene los documentos
                            // Puedes acceder a los datos de los tickets usando esta lista
                        } else {
                            // No se encontraron documentos
                        }
                    }
                });
    }

    private void imprimirLista() {
        if (pos == 0) {
            juegosEnVista = new Game[]{null, contenido.get(0), contenido.get(1)};
        } else {
            if (pos + 1 < contenido.size()) {
                juegosEnVista = new Game[]{contenido.get(pos - 1), contenido.get(pos), contenido.get(pos + 1)};
            }
            if (pos + 1 == contenido.size()) {
                juegosEnVista = new Game[]{contenido.get(pos - 1), contenido.get(pos), null};
            }
        }
        imprimirJuegosEnVista();
        binding.listaContenerLayout.setVisibility(View.VISIBLE);
    }

    private void imprimirJuegosEnVista() {
        if (juegosEnVista.length == 3) {
            if (juegosEnVista[0] != null) {
                binding.gameOneLayout.setVisibility(View.VISIBLE);
                binding.nameGameOne.setText(juegosEnVista[0].name_challeng);
                binding.dateGameOne.setText(Fecha.formatearFecha(juegosEnVista[0].date_limit));
                try {
                    String svgFondoContent = juegosEnVista[0].background;
                    String svgIconContent = juegosEnVista[0].icon;
                    // Convierte el SVG en un Drawable
                    SVG svgFondo = SVG.getFromString(svgFondoContent);
                    SVG svgIcon = SVG.getFromString(svgIconContent);
                    Drawable svgFondoDrawable = new PictureDrawable(svgFondo.renderToPicture());
                    Drawable svgIconDrawable = new PictureDrawable(svgIcon.renderToPicture());

                    binding.gameOneImageView.setImageDrawable(svgFondoDrawable);
                    binding.iconOne.setImageDrawable(svgIconDrawable);
                } catch (SVGParseException svgException){}
            } else {
                binding.gameOneLayout.setVisibility(View.INVISIBLE);
            }
            binding.nameGameTwo.setText(juegosEnVista[1].name_challeng);
            binding.dateGameTwo.setText(Fecha.formatearFecha(juegosEnVista[1].date_limit));
            try {
                String svgFondoContent = juegosEnVista[1].background;
                String svgIconContent = juegosEnVista[1].icon;
                // Convierte el SVG en un Drawable
                SVG svgFondo = SVG.getFromString(svgFondoContent);
                SVG svgIcon = SVG.getFromString(svgIconContent);
                Drawable svgFondoDrawable = new PictureDrawable(svgFondo.renderToPicture());
                Drawable svgIconDrawable = new PictureDrawable(svgIcon.renderToPicture());

                binding.gameTwoImageView.setImageDrawable(svgFondoDrawable);
                binding.iconTwo.setImageDrawable(svgIconDrawable);
            } catch (SVGParseException svgException){}
            if (juegosEnVista[2] != null) {
                binding.gameThreeLayout.setVisibility(View.VISIBLE);
                binding.nameGameThree.setText(juegosEnVista[2].name_challeng);
                binding.dateGameThree.setText(Fecha.formatearFecha(juegosEnVista[2].date_limit));
                try {
                    String svgFondoContent = juegosEnVista[2].background;
                    String svgIconContent = juegosEnVista[2].icon;
                    // Convierte el SVG en un Drawable
                    SVG svgFondo = SVG.getFromString(svgFondoContent);
                    SVG svgIcon = SVG.getFromString(svgIconContent);
                    Drawable svgFondoDrawable = new PictureDrawable(svgFondo.renderToPicture());
                    Drawable svgIconDrawable = new PictureDrawable(svgIcon.renderToPicture());

                    binding.gameThreeImageView.setImageDrawable(svgFondoDrawable);
                    binding.iconThree.setImageDrawable(svgIconDrawable);
                } catch (SVGParseException svgException){}
            } else {
                binding.gameThreeLayout.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void verJuego(Game game) {
        Bundle bundle = new Bundle();
        bundle.putString("id", game.id);
        VerRetoFragment verRetoFragment = new VerRetoFragment();
        verRetoFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_home, verRetoFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.webView.setWebChromeClient(new WebChromeClient(){


            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                mCustomView = view;
                binding.webView.setVisibility(View.GONE);
                binding.frameLayout.setVisibility(View.VISIBLE);

                binding.frameLayout.addView(view);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                if (mCustomView == null)
                    return;

                binding.webView.setVisibility(View.VISIBLE);
                binding.frameLayout.setVisibility(View.GONE);
                mCustomView.setVisibility(View.GONE);
                binding.frameLayout.removeView(mCustomView);

                mCustomView = null;

            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void updateUi() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            binding.ingresoRegistroButtonHeader.setVisibility(View.VISIBLE);
        } else {
            binding.ingresoRegistroButtonHeader.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTaskComplete() {
        updateUi();
    }
}