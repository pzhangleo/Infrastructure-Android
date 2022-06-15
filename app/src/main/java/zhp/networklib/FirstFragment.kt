package zhp.networklib

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import zhp.network.lib.adapter.NetworkResponse
import zhp.network.lib.adapter.NetworkResponseAdapterFactory
import zhp.networklib.databinding.FragmentFirstBinding
import zhp.networklib.network.Apis

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val loggingInterceptor = { chain: Interceptor.Chain ->
        val method = chain.request().method
        val url = chain.request().url
        println("$method: $url")
        chain.proceed(chain.request())
    }

    val okHttp = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            doRequest()
        }
    }

    private fun doRequest() {
        lifecycleScope.launch() {
            when (val response = retrofit.create(Apis::class.java).getPost1()) {
                is NetworkResponse.NetworkError -> TODO()
                is NetworkResponse.ServerError -> TODO()
                is NetworkResponse.UnknownError -> TODO()
                is NetworkResponse.Success -> {
                    binding.textviewFirst.text = response.body.toString()
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}