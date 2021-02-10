package com.example.organizze2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze2.R;
import com.example.organizze2.config.ConfiguracaoFirebase;
import com.example.organizze2.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail;
    private EditText campoSenha;
    private Button botaoEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editEmail2);
        campoSenha = findViewById(R.id.editSenha2);
        botaoEntrar = findViewById(R.id.buttonEntrar);

        botaoEntrar.setOnClickListener(new View.OnClickListener() {

            String textoEmailLog = campoEmail.getText().toString();
            String textoSenhaLog = campoSenha.getText().toString();

            @Override
            public void onClick(View view) {
                if (!textoEmailLog.isEmpty()){
                    if (!textoSenhaLog.isEmpty()){

                        usuario = new Usuario();
                        usuario.setEmail(textoEmailLog);
                        usuario.setSenha(textoSenhaLog);
                    }
                    else{
                        Log.i("senha", textoSenhaLog );
                        Toast.makeText(LoginActivity.this, "Preencha a Senha!", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Log.i("email", textoEmailLog );
                    Toast.makeText(LoginActivity.this, "Preencha o Email!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFireBaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            abrirTelaPrincipal();
                        }
                        else {
                            String excecao = "";

                            try {
                                throw task.getException();
                            }
                            catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "Email e senha não correspondem a um usuário cadastrado!";
                            }
                            catch (FirebaseAuthInvalidUserException e){
                                excecao = "Usuário não cadastrado";
                            }
                            catch (Exception e){
                                excecao = "Erro ao Cadastrar Usuário: " + e.getMessage();
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this, excecao,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }
}