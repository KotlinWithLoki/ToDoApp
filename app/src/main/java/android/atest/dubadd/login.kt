package android.atest.dubadd

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var emailedit = findViewById<TextInputEditText>(R.id.emailedittex)
        var passwordedit = findViewById<TextInputEditText>(R.id.passwordedittex)
        var btn1 = findViewById<Button>(R.id.loginbtn)
        var signUp = findViewById<TextView>(R.id.signuptxt)

        var auth: FirebaseAuth

        auth = FirebaseAuth.getInstance()

        val sharedPref = getSharedPreferences("data", Context.MODE_PRIVATE)
        val number = sharedPref.getInt("isLogged", 0)

        if (number == 0) {
            //Open the login activity and set this so that next it value is 1 then this conditin will be false.
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

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            val prefEditor = sharedPref.edit()
                            prefEditor.putInt("isLogged", 1)
                            prefEditor.commit()
//
//                            var preferences: SharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
//                            val prefEditor: SharedPreferences.Editor? = preferences.edit()
//                            prefEditor?.putBoolean("login", true)
//                            prefEditor?.commit()
//                            prefEditor.?putBoolean("userlogin", true)
//                            prefEditor.?commit()

                            Toast.makeText(this, "Mufavvaqiyatli Dasturga Kirish !", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Bunday Account Royxatdan O'tmagan !", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        signUp.setOnClickListener{
            val intent = Intent(this, sign_up::class.java)
            startActivity(intent)
            finish()
        }
    }
}