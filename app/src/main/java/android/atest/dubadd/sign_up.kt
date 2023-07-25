package android.atest.dubadd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class sign_up : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        var emailedit = findViewById<TextInputEditText>(R.id.signemailedittex)
        var passwordedit = findViewById<TextInputEditText>(R.id.signpasswordedittex)
        var btn1 = findViewById<Button>(R.id.signupbtn)
        var logintxt = findViewById<TextView>(R.id.logintxt)



        auth = FirebaseAuth.getInstance()

        btn1.setOnClickListener {
            var email = "${emailedit.text}"
            var password = "${passwordedit.text}"

            if(email.isEmpty()){
                emailedit.error = "Email Kiritilmadi !"
                emailedit.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailedit.error = "Email Xato !"
                emailedit.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()){
                passwordedit.error = "Password Kiritilmadi !"
                passwordedit.requestFocus()
                return@setOnClickListener
            }

            if(password.length < 6){
                passwordedit.error = "Password Kamida 6ta belgidan iborat bolishi kerak !"
                passwordedit.requestFocus()
                return@setOnClickListener
            }


            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Akkauntingiz Bazaga Qo'shildi !", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, login::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Qandaydur Xatolik Bor !", Toast.LENGTH_LONG).show()
                    }
                }
        }

        logintxt.setOnClickListener{
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()
        }

    }

}