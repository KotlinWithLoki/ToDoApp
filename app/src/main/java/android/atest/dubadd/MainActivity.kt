package android.atest.dubadd

import android.annotation.SuppressLint
import android.atest.dubadd.adapter.ToDoAdapter
import android.atest.dubadd.databinding.ActivityMainBinding
import android.atest.dubadd.utils.ToDOData
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), ToDoAdapter.ToDoAdapterClicksInterface {

    private lateinit var database: DatabaseReference
    private val uid = Firebase.auth.currentUser?.uid
    private lateinit var binding: ActivityMainBinding
    private lateinit var mList: MutableList<ToDOData>
    private lateinit var adapterTo: ToDoAdapter
    private var databaseRef = FirebaseDatabase.getInstance().reference.child("users").child(uid.toString())



    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference

        val databaseRef = FirebaseDatabase.getInstance().reference.child("users").child(uid.toString())



        val btn = findViewById<Button>(R.id.sendbtn)
        val edittext = findViewById<EditText>(R.id.needmessage)

        mList = mutableListOf()
        adapterTo = ToDoAdapter(mList)
        adapterTo.notifyDataSetChanged()


        binding.todolist.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.todolist.adapter = adapterTo
        binding.todolist.setHasFixedSize(true)

        adapterTo


        fun loadData(){

            databaseRef.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mList.clear()
                    for (postSnapshot in dataSnapshot.children) {
                        val todotask = postSnapshot.key?.let {
                            ToDOData(it, postSnapshot.value.toString())
                        }
                        if(todotask != null) {
                            mList.add(todotask)
                        }else{
                            Toast.makeText(this@MainActivity, "boom", Toast.LENGTH_LONG).show()
                        }
                    }
                    adapterTo.notifyDataSetChanged()
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }
            })

        }

        loadData()

        fun writeNewUser(uid: String?, name: String) {
            database.child("users").child(uid.toString()).push().setValue(name)
        }

        btn.setOnClickListener {
            if (edittext.text.isNotEmpty()){
                writeNewUser(uid, "${edittext.text}")
                edittext.text.clear()
            }else{
                edittext.error = "Field is empty"
            }
        }

        adapterTo.setListener(this)



    }


    override fun onDeleteTask(toDOData: ToDOData) {
        databaseRef.child(toDOData.taskid).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this@MainActivity, "Removed Succesfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@MainActivity, "boom", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun OnClickListener(toDOData: ToDOData) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("To-Do List")
        builder.setMessage("${toDOData.task}")
        builder.setIcon(R.drawable.iconmain)
        builder.setPositiveButton("Ok") { dialog, which -> }
        builder?.show()
    }

}