package com.thereto.theretosvirtuales.ui.ver_reto;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thereto.model.Game;
import com.thereto.theretosvirtuales.R;
import com.thereto.theretosvirtuales.databinding.FragmentJuegoBinding;
import com.thereto.theretosvirtuales.interfas.ActualizarTicketsUsuario;
import com.thereto.theretosvirtuales.interfas.TicketUsuarioEvent;
import com.thereto.theretosvirtuales.service.ChallengesVirtualService;
import com.thereto.theretosvirtuales.ui.instrucciones.InstruccionesFragment;

import org.metatrans.apps.gravity.main.Activity_Main_Gravity;

import java.util.HashMap;
import java.util.Map;

public class VerRetoFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FragmentJuegoBinding binding;
    private Game game;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentJuegoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
    }

    @Override
    public void onResume() {
        DocumentReference docRef = new ChallengesVirtualService().getJuegoById(requireArguments().getString("id"));
        docRef.get()
                .addOnSuccessListener( documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                game = new Game();
                                game.documentId = documentSnapshot.getId();
                                game.accumulated_tickets = documentSnapshot.getLong("accumulated_tickets").intValue();
                                game.date_creation = documentSnapshot.getString("date_creation");
                                game.date_limit = documentSnapshot.getString("date_limit");
                                game.description = documentSnapshot.getString("description");
                                game.name_challeng = documentSnapshot.getString("name_challeng");
                                game.videoguide = documentSnapshot.getString("videoguide");
                                game.tickets = documentSnapshot.getLong("tickets").intValue();
                                game.id = documentSnapshot.getString("id");
                                binding.ticketsTextView.setText(game.tickets + "");
                                binding.acumulatedTicketsTextView.setText(game.accumulated_tickets + "");
                                binding.descripcionTextView.setText(game.description);
                                binding.tituloJuegoTextView.setText(game.name_challeng);
                                mostrarVideo();
                                binding.verMasTextView.setOnClickListener(v -> {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", requireArguments().getString("id"));
                                    bundle.putString("name", game.name_challeng);
                                    InstruccionesFragment fragment = new InstruccionesFragment();
                                    fragment.setArguments(bundle);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.nav_host_fragment_content_home, fragment)
                                            .addToBackStack(null)
                                            .commit();
                                });
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                if (currentUser == null) {
                                    Toast.makeText(requireContext(), "Necesita iniciar sesion para entrar a los juegos", Toast.LENGTH_LONG).show();
                                } else {
                                    binding.participarTextView.setOnClickListener(v -> {
                                        ticketsUsuario(currentUser.getUid(), new TicketUsuarioEvent() {

                                            @Override
                                            public void consultado(int cantidad, String[] uids) {
                                                if (cantidad > 0) {
                                                    actualizarTicket(currentUser.getUid(), game.tickets, new ActualizarTicketsUsuario() {

                                                        @Override
                                                        public void actualizado() {
                                                            DocumentReference docRef = new ChallengesVirtualService().getJuegoById(requireArguments().getString("id"));
                                                            docRef.get()
                                                                    .addOnSuccessListener( documentSnapshot -> {
                                                                        if (documentSnapshot.exists()) {
                                                                            int accumulated_tickets = documentSnapshot.getLong("accumulated_tickets").intValue() + game.tickets;
                                                                            Task<Void> updateTask = new ChallengesVirtualService().actualizarTicketsAcumulatedGame(requireArguments().getString("id"), accumulated_tickets);
                                                                            try {
                                                                                // Esperar a que la tarea se complete antes de pasar al siguiente documento
                                                                                Tasks.await(updateTask);
                                                                            } catch (Exception e) {
                                                                                // Manejar la excepción según tus necesidades
                                                                            }
                                                                            switch (game.id) {
                                                                                case "1":
                                                                                    Intent intent = new Intent(VerRetoFragment.this.requireContext(), Activity_Main_Gravity.class);
                                                                                    intent.putExtra("clave", "valor");
                                                                                    VerRetoFragment.this.startActivity(intent);
                                                                                    break;
                                                                                case "2":
                                                                                    intent = new Intent(VerRetoFragment.this.requireContext(), com.example.hundred.MainActivity.class);
                                                                                    intent.putExtra("clave", "valor");
                                                                                    VerRetoFragment.this.startActivity(intent);
                                                                                    break;
                                                                            }
                                                                        }
                                                                    });

                                                        }

                                                        @Override
                                                        public void cantidadNoPermitida() {
                                                            Toast.makeText(requireContext(), "No tiene suficientes tickets para jugar este juego.", Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                                } else {
                                                    Toast.makeText(VerRetoFragment.this.requireContext(), "No tiene suficientes tickets.", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    });
                                }
                            } else {
                                // El documento no existe
                            }
                        }

                )
                .addOnFailureListener ( e ->{}
                        // Ocurrió un error al consultar el documento
                );
        super.onResume();
    }

    private void mostrarVideo() {
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // URL del video de YouTube
        String videoId = game.videoguide;

        // Cargar el video de YouTube en el WebView
        binding.webView.loadData("<iframe width=\"100%\" height=\"100%\" src=\"" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
    }

    public void ticketsUsuario(String uid, TicketUsuarioEvent ticketUsuarioEvent) {

        getMisTickets(uid).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // La consulta fue exitosa, puedes acceder a los documentos resultantes
                    QuerySnapshot result = task.getResult();
                    if (result != null) {
                        int tam = result.size();
                        int totalTickets = 0;
                        String[] uidTickets = new String[tam];
                        int index = 0;
                        for (DocumentSnapshot documento : result.getDocuments()) {
                            Map<String, Object> datos = documento.getData();
                            int tickets = documento.getLong("total_ticket").intValue();
                            totalTickets += tickets;
                            uidTickets[index] = documento.getId();
                            index++;
                        }
                        if (ticketUsuarioEvent != null) {
                            ticketUsuarioEvent.consultado(totalTickets, uidTickets);
                        }
                    }
                } else {
                    // Maneja el caso en que la consulta no fue exitosa
                    Exception exception = task.getException();
                    // Trata la excepción según tus necesidades
                }
            }
        });
    }

    private void actualizarTicket(String uid, int catidadRestar, ActualizarTicketsUsuario ticketUsuarioEvent) {
        this.db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("total_tickect", catidadRestar);
        final int cantidaOuput = catidadRestar;
        getMisTickets(uid).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // La consulta fue exitosa, puedes acceder a los documentos resultantes
                    QuerySnapshot result = task.getResult();
                    int cantidadRestar = cantidaOuput;
                    if (result != null) {
                        int tam = result.size();
                        int totalTickets = 0;
                        String[] uidTickets = new String[tam];
                        int index = 0;
                        for (DocumentSnapshot documento : result.getDocuments()) {
                            Map<String, Object> datos = documento.getData();
                            int tickets = documento.getLong("total_ticket").intValue();
                            totalTickets += tickets;
                            uidTickets[index] = documento.getId();
                            index++;
                        }
                        if (totalTickets >= cantidadRestar) {
                            for (DocumentSnapshot documento : result.getDocuments()) {
                                if (cantidadRestar > 0) {
                                    Map<String, Object> datos = documento.getData();
                                    int tickets = documento.getLong("total_ticket").intValue();
                                    int cantidadCalculada = cantidadRestar;
                                    if (tickets < cantidadRestar) {
                                        cantidadCalculada = tickets;
                                        cantidadRestar -= tickets;
                                    } else {
                                        cantidadCalculada = tickets - cantidadRestar;
                                        cantidadRestar = 0;
                                    }
                                    Map<String, Object> data = new HashMap<>();
                                    Task<Void> updateTask = actualizarTotalTicket(documento.getId(), cantidadCalculada);
                                    try {
                                        // Esperar a que la tarea se complete antes de pasar al siguiente documento
                                        Tasks.await(updateTask);
                                    } catch (Exception e) {
                                        // Manejar la excepción según tus necesidades
                                    }
                                }

                            }
                            if (ticketUsuarioEvent != null) {
                                ticketUsuarioEvent.actualizado();
                            }
                        } else {
                            if (ticketUsuarioEvent != null) {
                                ticketUsuarioEvent.cantidadNoPermitida();
                            }
                        }
                    }
                } else {
                    // Maneja el caso en que la consulta no fue exitosa
                    Exception exception = task.getException();
                    // Trata la excepción según tus necesidades
                }
            }
        });
    }

    private Task<Void> actualizarTotalTicket(String documentId, int nuevoTotalTicket) {
        DocumentReference docRef = db.collection("player_tickets").document(documentId);
        return docRef.update("total_ticket", nuevoTotalTicket);
    }

    public Task<QuerySnapshot> getMisTickets(String uid) {
        this.db = FirebaseFirestore.getInstance();
        return db.collection("player_tickets")
                .whereEqualTo("uid", uid)
                .get();
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}