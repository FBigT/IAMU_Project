package good.stuff.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import good.stuff.myapplication.databinding.FragmentGameDetailBinding

class GameDetailFragment : Fragment() {
    private lateinit var binding: FragmentGameDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
}