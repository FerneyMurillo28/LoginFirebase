package com.example.loginfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.loginfirebase.Screens.LoginScreen
import com.example.loginfirebase.Screens.Principal
import com.example.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {
    companion object{
        //Valor de inicializacion de una tarea
        const val RC_SIGN_IN=100
    }
    //VAriable nulas hasta ser modificadas
    private lateinit var mAuth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Instancia de autenticador de firebase
        mAuth = FirebaseAuth.getInstance()

        //Configuracion Google SignIn
        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(this,gso)
        setContent {
            LoginFirebaseTheme {
                if (mAuth.currentUser==null){
                    LoginScreen {
                        signIn()
                    }
                }else{
                    val user:FirebaseUser=mAuth.currentUser!!
                    Principal(profileImage = user.photoUrl!!,
                        name = user.displayName!!,
                        email = user.email!!,
                        signOutClicked = {
                            singOut()
                        }
                    )
                }
            }
        }
    }

    private fun signIn(){
        val signInIntent=googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //result returned from launching the intent from GoogleSignInApi.getSignInIntent()
        if (requestCode== RC_SIGN_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception=task.exception
            if(task.isSuccessful){
                try{
                    //Google signin was succesful, auth with Firebase
                    val account= task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                }catch (e: Exception){
                    //Google SignIn Failed
                    Log.d("SignIn","Google Sign Failed")
                }
            }else{
                Log.d("Signin", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken:String){
        val credential=GoogleAuthProvider.getCredential(idToken,null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task->
                if(task.isSuccessful){
                    //SignIn Succesful
                    Toast.makeText(this,"SignIn Succesful",Toast.LENGTH_SHORT).show()
                    setContent {
                        LoginFirebaseTheme{
                            val user: FirebaseUser=mAuth.currentUser!!
                            Principal(
                                profileImage = user.photoUrl!!,
                                name = user.displayName!!,
                                email = user.email!!,
                                signOutClicked = {
                                    singOut()
                                }
                            )
                        }
                    }
                }else{
                    //SignIn Failed
                    Toast.makeText(this,"SignIn failedl",Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun singOut(){
        val googleSignInClient:GoogleSignInClient
        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient=GoogleSignIn.getClient(this, gso)
        //SignInOut
        mAuth.signOut()
        googleSignInClient.signOut().addOnSuccessListener {
        Toast.makeText(this, "Sign Out Succes", Toast.LENGTH_SHORT).show()
            setContent{
                LoginFirebaseTheme{
                    LoginScreen {
                        signIn()
                    }
                }
            }
        }
            .addOnFailureListener{
            Toast.makeText(this, "Sign Out Failed", Toast.LENGTH_SHORT).show()
        }
    }
}