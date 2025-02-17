package good.stuff.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import good.stuff.myapplication.adapter.GameAdapter
import good.stuff.myapplication.databinding.FragmentDashboardBinding
import good.stuff.myapplication.handler.GameDataHolder

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvGameItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = GameAdapter(this.context, GameDataHolder.games)
        }
    }
}
