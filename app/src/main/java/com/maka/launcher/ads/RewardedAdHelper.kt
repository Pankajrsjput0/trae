package com.maka.launcher.ads

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import com.maka.launcher.data.repository.AppUsageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RewardedAdHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val adManager: AdManager,
    private val appUsageRepository: AppUsageRepository
) {
    private val _rewardTimeRemaining = MutableStateFlow<Int>(0)
    val rewardTimeRemaining: StateFlow<Int> = _rewardTimeRemaining

    fun showRewardedAd(activity: Activity, lifecycleScope: LifecycleCoroutineScope) {
        if (adManager.adState.value == AdState.Loaded) {
            adManager.showRewardedAd(
                activity = activity,
                onRewarded = { minutes ->
                    startRewardTimer(minutes, lifecycleScope)
                },
                onAdClosed = {
                    // Preload next ad
                    adManager.loadRewardedAd()
                }
            )
        } else {
            // Try to load ad if not ready
            adManager.loadRewardedAd()
        }
    }

    private fun startRewardTimer(minutes: Int, lifecycleScope: LifecycleCoroutineScope) {
        _rewardTimeRemaining.value = minutes * 60 // Convert to seconds
        
        lifecycleScope.launch {
            while (_rewardTimeRemaining.value > 0) {
                delay(1000) // Wait for 1 second
                _rewardTimeRemaining.value = _rewardTimeRemaining.value - 1
            }
        }
    }

    fun hasActiveReward(): Boolean = _rewardTimeRemaining.value > 0
}