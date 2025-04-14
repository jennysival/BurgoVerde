package com.jennysival.burgoverde.ui.home

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jennysival.burgoverde.R
import com.jennysival.burgoverde.databinding.FragmentHomeBinding
import com.jennysival.burgoverde.factory.HomeViewModelFactory
import com.jennysival.burgoverde.navigation.BurgoVerdeNavigator
import com.jennysival.burgoverde.navigation.BurgoVerdeNavigatorImpl
import com.jennysival.burgoverde.utils.helper.SharedPreferencesHelper
import com.jennysival.burgoverde.utils.showToast
import com.jennysival.burgoverde.utils.viewstate.PlantViewState
import com.jennysival.burgoverde.utils.viewstate.ProfileViewState

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var navigator: BurgoVerdeNavigator
    private lateinit var sharedPrefs: SharedPreferencesHelper

    private var isAnimationCompleted = false
    private var currentAnimator: ValueAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefs = SharedPreferencesHelper(requireContext())
        initViewModel()
        initNavigator()
        initObserver()
        setUpHeader()
        viewModel.getProfilePhoto()
        sharedPrefs.getUserId()?.let { viewModel.getPlantsCount(it) }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this, HomeViewModelFactory(requireActivity())
        )[HomeViewModel::class.java]
    }

    private fun initNavigator() {
        navigator = BurgoVerdeNavigatorImpl(this)
    }

    private fun initObserver() {
        observePlantCount()
        observeProfilePhoto()
    }

    private fun observePlantCount() {
        viewModel.plantsCountState.observe(this.viewLifecycleOwner) {
            when (it) {
                is PlantViewState.Success -> {
                    val groupTotal = it.data.total
                    val userTotal = it.data.user

                    setUpAnimation(groupTotal = groupTotal)
                    setUpGroupProgress(groupTotal = groupTotal)
                    setUpUserProgress(userTotal = userTotal)
                }

                is PlantViewState.Error -> {
                    showToast(
                        messageRes = R.string.burgoverde_ops_text, context = requireContext()
                    )
                }

                is PlantViewState.None -> {
                    //do nothing
                }
            }
        }
    }

    private fun observeProfilePhoto() {
        viewModel.profilePhotoState.observe(this.viewLifecycleOwner) {
            when (it) {
                is ProfileViewState.Success -> {
                    loadProfilePhoto(it.data)
                }

                is ProfileViewState.Error -> {
                    showToast(
                        messageRes = R.string.burgoverde_image_load_error,
                        context = requireContext()
                    )
                }
            }
        }
    }

    private fun setUpAnimation(groupTotal: Int) {
        val animationProgress = (groupTotal.toFloat() / GROUP_GOAL_INT.toFloat()).coerceIn(
            ANIMATION_MIN_PROGRESS, ANIMATION_MAX_PROGRESS)
        currentAnimator?.cancel()

        binding.animationView.apply {
            setMinAndMaxProgress(ANIMATION_MIN_PROGRESS, ANIMATION_MAX_PROGRESS)

            if (groupTotal >= GROUP_GOAL_INT) {
                if (!isAnimationCompleted) {
                    repeatCount = ANIMATION_REPEAT_COUNT
                    playAnimation()
                    isAnimationCompleted = true
                }
            } else {
                isAnimationCompleted = false
                repeatCount = ANIMATION_REPEAT_COUNT

                currentAnimator = ValueAnimator.ofFloat(progress, animationProgress).apply {
                    duration = ANIMATION_DURATION_MS
                    addUpdateListener { animation ->
                        progress = animation.animatedValue as Float
                    }
                    start()
                }
            }
        }
    }

    private fun setUpGroupProgress(groupTotal: Int) {
        binding.groupGoalProgressionTv.text =
            getString(R.string.burgoverde_group_progress, groupTotal, GROUP_GOAL_INT)
        binding.plantTrackerPb.max = GROUP_GOAL_INT
        binding.plantTrackerPb.progress = groupTotal.coerceAtMost(GROUP_GOAL_INT)
    }

    private fun setUpUserProgress(userTotal: Int) {
        binding.yourPlantsTv.text = getString(R.string.burgoverde_user_contribution, userTotal)
    }

    private fun setUpHeader() {
        val userName = sharedPrefs.getUserName()
        binding.helloTv.text = getString(R.string.burgoverde_hello_user, userName)
    }

    private fun loadProfilePhoto(uri: String?) {
        Glide.with(this).load(uri.takeIf { !it.isNullOrBlank() })
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).error(R.drawable.ic_profile)
            .circleCrop().transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.photoIv)
    }

    companion object {
        private const val GROUP_GOAL_INT = 30
        private const val ANIMATION_REPEAT_COUNT = 0
        private const val ANIMATION_DURATION_MS = 2000L
        private const val ANIMATION_MIN_PROGRESS = 0f
        private const val ANIMATION_MAX_PROGRESS = 1f
    }
}
