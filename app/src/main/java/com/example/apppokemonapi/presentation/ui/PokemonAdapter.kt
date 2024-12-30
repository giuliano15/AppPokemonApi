import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apppokemon.PokemonM
import com.example.apppokemonapi.R
import com.example.apppokemonapi.presentation.ui.OnItemClickListener
import com.example.apppokemonapi.util.PokemonTypeUtils
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class PokemonAdapter(
    private var pokemonList: List<PokemonM>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    companion object {
        private const val MAX_TYPES_DISPLAYED = 3 // Definir o limite de Chips a serem exibidos
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name_text_view)
        val idTextView: TextView = itemView.findViewById(R.id.id_text_view)
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val typeChipGroup: ChipGroup = itemView.findViewById(R.id.type_chip_group)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val pokemon = pokemonList[position]
                    onItemClickListener.onItemClick(pokemon)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.nameTextView.text = pokemon.name
        holder.idTextView.text = String.format("#%03d", pokemon.id)
        Glide.with(holder.itemView.context).load(pokemon.imageUrl).into(holder.imageView)


        //veja como fiz

        // Limpar os Chips antigos
        holder.typeChipGroup.removeAllViews()

        // Mostrar apenas os tipos dentro do limite
        val typesToShow = pokemon.types.take(MAX_TYPES_DISPLAYED)
        val hasMoreTypes = pokemon.types.size > MAX_TYPES_DISPLAYED

        // Adicionar Chips para os tipos a serem mostrados
        for (type in typesToShow) {
            val chip = Chip(holder.itemView.context).apply {
                text = type
                val colorResId = PokemonTypeUtils.getTypeColor(type)
                setChipBackgroundColorResource(colorResId)
                setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            }
            holder.typeChipGroup.addView(chip)
        }

        // Adicionar Chip "..." se houver mais tipos
        if (hasMoreTypes) {
            val moreChip = Chip(holder.itemView.context).apply {
                text = "..."
                setChipBackgroundColorResource(R.color.gray_21)
                setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            }
            holder.typeChipGroup.addView(moreChip)
        }

    }

    override fun getItemCount(): Int = pokemonList.size

    // Método para atualizar a lista filtrada
    fun updateList(filteredPokemonList: List<PokemonM>) {
        pokemonList = filteredPokemonList
        notifyDataSetChanged()
    }

}


