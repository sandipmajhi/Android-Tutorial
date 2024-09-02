import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.EmpaView
import com.example.myapplication.R

class EmployeeAdapter(private var employees: List<EmpaView>? = listOf()) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    fun updateData(newEmployees: List<EmpaView>?) {
        employees = newEmployees ?: emptyList()  // If newEmployees is null, set employees to an empty list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_employee, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        // Employees list is non-null after being initialized in updateData or the constructor
        employees?.let {
            val employee = it[position]
            holder.bind(employee)
        } ?: run {
            Log.e("EmployeeAdapter", "Employees list is null")
        }
    }

    override fun getItemCount(): Int = employees?.size ?: 0  // Return 0 if employees is null

    class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val emailTextView: TextView = itemView.findViewById(R.id.textViewEmail)
        private val usernameTextView: TextView = itemView.findViewById(R.id.textViewUsername)
        private val roleTextView: TextView = itemView.findViewById(R.id.textViewRole)

        fun bind(employee: EmpaView) {
            emailTextView.text = employee.email
            usernameTextView.text = employee.username
            roleTextView.text = employee.role
        }
    }
}
