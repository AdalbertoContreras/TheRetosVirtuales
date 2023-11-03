package com.thereto.theretosvirtuales.ui.instrucciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thereto.model.Game;
import com.thereto.theretosvirtuales.R;
import com.thereto.theretosvirtuales.adapter.InstruccionesAdapter;
import com.thereto.theretosvirtuales.databinding.FragmentETicketsBinding;
import com.thereto.theretosvirtuales.databinding.FragmentInstruccionesBinding;

import java.util.ArrayList;

public class InstruccionesFragment extends Fragment {

    private FragmentInstruccionesBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInstruccionesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.tituloJuegoTextView.setText(requireArguments().getString("name"));

        // Obtén una referencia a la colección de instrucciones
        CollectionReference  docRef = db.collection("challenges_virtual").document(requireArguments().getString("id")).collection("instructions");
        Query query = docRef.orderBy("order", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<String> descriptions = new ArrayList<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Para cada documento en la consulta, obtén la descripción
                    String description = document.getString("description");
                    if (description != null) {
                        // Agrega la descripción al ArrayList
                        descriptions.add(description);
                    }
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
                InstruccionesAdapter adapter = new InstruccionesAdapter(requireContext(), descriptions);
                binding.instruccionesRecycler.setLayoutManager(layoutManager);
                binding.instruccionesRecycler.setAdapter(adapter);
                // Aquí puedes trabajar con el ArrayList de descripciones
            } else {

            }
        });









        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}