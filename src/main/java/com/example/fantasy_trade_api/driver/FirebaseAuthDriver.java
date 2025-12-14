package com.example.fantasy_trade_api.driver;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

public class FirebaseAuthDriver {
    public static FirebaseToken verifyToken(String idToken)throws FirebaseAuthException{
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }
}
