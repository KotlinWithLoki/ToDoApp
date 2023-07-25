package android.atest.dubadd.adapter

import android.atest.dubadd.R
import android.atest.dubadd.databinding.ExampleBinding
import android.atest.dubadd.utils.ToDOData
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNREACHABLE_CODE")
class ToDoAdapter(private val list: MutableList<ToDOData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listener: ToDoAdapterClicksInterface? = null


    fun setListener(listener:ToDoAdapterClicksInterface) {
        this.listener = listener
    }

    class ViewHolder(var binding: ExampleBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        // inflates the card_view_design view
        // that is used to hold list item
        val binding = ExampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        with(holder){
            with(list[position]){
                holder.itemView.findViewById<TextView>(R.id.tv_list).text = list[position].task
                holder.itemView.findViewById<Button>(R.id.delete_button).setOnClickListener {
                    listener?.onDeleteTask(this)
                }
                holder.itemView.setOnClickListener{
                    listener?.OnClickListener(this)
                }
                }
            }
        }
    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    interface ToDoAdapterClicksInterface{
        fun onDeleteTask(toDOData: ToDOData)
        fun OnClickListener(toDOData: ToDOData)
    }
    // Holds the views for adding it to image and text
}

