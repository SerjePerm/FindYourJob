package ru.practicum.android.diploma.filter.ui.sector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.databinding.FragmentSectorBinding

class SectorFragment : Fragment() {

    private var _binding: FragmentSectorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SectorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSectorBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tbSector.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}
