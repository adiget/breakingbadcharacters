package com.ags.annada.characters.characters

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ags.annada.characters.R
import com.ags.annada.characters.databinding.FragmentCharactersBinding
import com.ags.annada.characters.datasource.room.entities.CharacterItem
import com.ags.annada.characters.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CharactersFragment : Fragment() {
    private val viewModel by viewModels<CharactersViewModel>()
    private lateinit var adapter: CharactersAdapter
    private lateinit var seasonsAdapter: ArrayAdapter<String>

    private lateinit var binding: FragmentCharactersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        setupNavigation()
        setupAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        val item = menu.findItem(R.id.action_search)
        val searchView: SearchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.setSearchString(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.setSearchString(newText)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_filter -> {
                setupSeasonsFilter()
                true
            }
            else -> false
        }

    private fun setupSeasonsFilter() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_seasons, menu)

            setOnMenuItemClickListener {
                viewModel.setSeasonNumber(
                    when (it.itemId) {
                        R.id.seasons_all -> SeasonNumber.ALL_SEASONS
                        R.id.season_one -> SeasonNumber.SEASON_ONE
                        R.id.season_two -> SeasonNumber.SEASON_TWO
                        R.id.season_three -> SeasonNumber.SEASON_THREE
                        R.id.seasons_four -> SeasonNumber.SEASON_FOUR
                        R.id.seasons_five -> SeasonNumber.SEASON_FIVE
                        else -> SeasonNumber.ALL_SEASONS
                    }
                )
                true
            }
            show()
        }
    }

    private fun setupAdapter() {
        val viewModel = binding.viewmodel

        if (viewModel != null) {
            adapter = CharactersAdapter(viewModel)
            binding.charactersList.adapter = adapter
        } else {
            Log.d("setupAdapter()", "ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupNavigation() {
        viewModel.selectItemEvent.observe(viewLifecycleOwner, EventObserver { it ->
            Log.d("SELECTED ID", "selected=${it}")

            val id = it
            val item: CharacterItem? = viewModel.items.value?.first { it.char_id == id }

            openDetails(it, item?.name ?: "Details")
        })
    }

    private fun openDetails(chracterId: Int, title: String) {
        val action =
            CharactersFragmentDirections.actionCharactersFragmentToDetailsFragment(
                chracterId,
                title
            )
        findNavController().navigate(action)
    }
}

const val SELECTED_SEASON_NUMBER_KEY = "SELECTED_SEASON_NUMBER_KEY"