package com.thereto.theretosvirtuales.service;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ChallengesVirtualService {
    public Task<Void> actualizarTicketsAcumulatedGame(String documentId, int nuevoAcumulatedTickets) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("challenges_virtual").document(documentId);
        return docRef.update("accumulated_tickets", nuevoAcumulatedTickets);
    }

    public DocumentReference getJuegoById(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();
        return db.collection("challenges_virtual")
                .document(id);
    }
}
