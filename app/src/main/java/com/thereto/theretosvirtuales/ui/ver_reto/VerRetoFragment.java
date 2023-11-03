package com.thereto.theretosvirtuales.ui.ver_reto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thereto.model.Game;
import com.thereto.theretosvirtuales.databinding.FragmentJuegoBinding;
import com.thereto.theretosvirtuales.databinding.FragmentPremiosBinding;

import java.util.ArrayList;

public class VerRetoFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FragmentJuegoBinding binding;
    private Game game;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentJuegoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DocumentReference docRef = db.collection("challenges_virtual").document(requireArguments().getString("id"));

        docRef.get()
                .addOnSuccessListener( documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        game = new Game();
                        game.id = documentSnapshot.getId();
                        game.accumulated_tickets = documentSnapshot.getLong("accumulated_tickets").intValue();
                        game.date_creation = documentSnapshot.getString("date_creation");
                        game.date_limit = documentSnapshot.getString("date_limit");
                        game.description = documentSnapshot.getString("description");
                        game.name_challeng = documentSnapshot.getString("name_challeng");
                        binding.descripcionTextView.setText(game.description);
                        binding.tituloJuegoTextView.setText(game.name_challeng);
                        mostrarVideo();
                    } else {
                        // El documento no existe
                    }
                }

        )
    .addOnFailureListener ( e ->{}
            // Ocurrió un error al consultar el documento
    );
        return root;
    }

    private void mostrarVideo() {
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // URL del video de YouTube
        String videoId = "kWiiMoafKME";

        // Cargar el video de YouTube en el WebView
        binding.webView.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}