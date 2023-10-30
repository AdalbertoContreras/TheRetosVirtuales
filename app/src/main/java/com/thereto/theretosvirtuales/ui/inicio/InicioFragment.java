package com.thereto.theretosvirtuales.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thereto.model.Game;
import com.thereto.theretosvirtuales.databinding.FragmentHomeBinding;

import java.util.Objects;

public class InicioFragment extends Fragment {

    private FragmentHomeBinding binding;
    private View mCustomView;
    private Game[] contenido = {
            new Game("CUERDAS O SABANAS", 1, "Sep/30/2023"),
            new Game("BALL BOX", 1, "Sep/30/2023"),
            new Game("CANASTA REVES", 1, "Sep/30/2023"),
            new Game("JUEGO 1", 1, "Sep/30/2023"),
            new Game("JUEGO 2", 1, "Sep/30/2023"),
            new Game("JUEGO 3", 1, "Sep/30/2023"),
    };

    private Game[] juegosEnVista = {};
    private float x1, x2;
    static final int MIN_DISTANCE = 20;
    private int pos = 0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        imprimirLista();
        Objects.requireNonNull(binding.listaContenerLayout).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        return true;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        if (x2 < x1) {
                            if ((pos + 1) < contenido.length) {
                                pos ++;
                                imprimirLista();
                                //Toast.makeText(requireContext(), contenido[pos], Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (pos > 0) {
                                pos --;
                                imprimirLista();
                                //Toast.makeText(requireContext(), contenido[pos], Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                }
                return false;
            }
        });
        return root;
    }

    private void imprimirLista() {
        if (pos == 0) {
            juegosEnVista = new Game[]{null, contenido[0], contenido[1]};
        } else {
            if (pos + 1 < contenido.length) {
                juegosEnVista = new Game[]{contenido[pos - 1], contenido[pos], contenido[pos + 1]};
            }
            if (pos + 1 == contenido.length) {
                juegosEnVista = new Game[]{contenido[pos - 1], contenido[pos], null};
            }
        }
        imprimirJuegosEnVista();
    }

    private void imprimirJuegosEnVista() {
        if (juegosEnVista.length == 3) {
            if (juegosEnVista[0] != null) {
                binding.gameOneLayout.setVisibility(View.VISIBLE);
                binding.nameGameOne.setText(juegosEnVista[0].name_challeng);
                binding.ticketGameOne.setText(juegosEnVista[0].accumulated_tickets + " eTickets");
                binding.dateGameOne.setText(juegosEnVista[0].date_limit);
            } else {
                binding.gameOneLayout.setVisibility(View.INVISIBLE);
            }

            binding.nameGameTwo.setText(juegosEnVista[1].name_challeng);
            binding.ticketGameTwo.setText(juegosEnVista[1].accumulated_tickets + " eTickets");
            binding.dateGameTwo.setText(juegosEnVista[1].date_limit);

            if (juegosEnVista[2] != null) {
                binding.gameThreeLayout.setVisibility(View.VISIBLE);
                binding.nameGameThree.setText(juegosEnVista[2].name_challeng);
                binding.ticketGameThree.setText(juegosEnVista[2].accumulated_tickets + " eTickets");
                binding.dateGameThree.setText(juegosEnVista[2].date_limit);
            } else {
                binding.gameThreeLayout.setVisibility(View.INVISIBLE);
            }
        }
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
}