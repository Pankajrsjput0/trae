package com.maka.launcher.ads

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val coroutineScope: CoroutineScope
) {
    private var rewardedAd: RewardedAd? = null
    private val _adState = MutableStateFlow<AdState>(AdState.NotLoaded)
    val adState: StateFlow<AdState> = _adState
    
    private var retryCount = 0
    private val maxRetries = 3
    private val retryDelayMs = 5000L // 5 seconds
    
    companion object {
        private const val TAG = "AdManager"
        private const val AD_UNIT_ID = "ca-app-pub-7674769237472802/7875166720"
    }

    fun loadRewardedAd() {
        if (_adState.value == AdState.Loading) return
        
        _adState.value = AdState.Loading
        Log.d(TAG, "Loading rewarded ad")
        
        RewardedAd.load(
            context,
            AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad loaded successfully")
                    rewardedAd = ad
                    _adState.value = AdState.Loaded
                    retryCount = 0 // Reset retry count on successful load
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(TAG, "Ad failed to load: ${error.message}")
                    rewardedAd = null
                    handleLoadError(error)
                }
            }
        )
    }

    private fun handleLoadError(error: LoadAdError) {
        if (retryCount < maxRetries) {
            retryCount++
            _adState.value = AdState.Error("Retrying ad load (attempt $retryCount)")
            
            coroutineScope.launch {
                delay(retryDelayMs)
                loadRewardedAd()
            }
        } else {
            _adState.value = AdState.Error("Failed to load ad after $maxRetries attempts")
            retryCount = 0 // Reset for next time
        }
    }

    fun showRewardedAd(
        activity: android.app.Activity,
        onRewarded: (Int) -> Unit,
        onAdClosed: () -> Unit = {},
        onAdFailedToShow: (String) -> Unit = {}
    ) {
        val ad = rewardedAd
        if (ad != null) {
            Log.d(TAG, "Showing rewarded ad")
            ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad dismissed")
                    onAdClosed()
                    _adState.value = AdState.NotLoaded
                    loadRewardedAd() // Preload next ad
                }

                override fun onAdFailedToShowFullScreenContent(error: com.google.android.gms.ads.AdError) {
                    Log.e(TAG, "Ad failed to show: ${error.message}")
                    onAdFailedToShow(error.message)
                    _adState.value = AdState.Error(error.message)
                    loadRewardedAd()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content")
                }
            }
            
            ad.show(activity) { rewardItem ->
                Log.d(TAG, "User earned reward")
                onRewarded(7) // Grant 7 minutes of free usage
            }
            rewardedAd = null
        } else {
            Log.w(TAG, "Attempted to show ad but it wasn't loaded")
            onAdFailedToShow("Ad not loaded")
            loadRewardedAd() // Try to load for next time
        }
    }
}

sealed class AdState {
    object NotLoaded : AdState()
    object Loading : AdState()
    object Loaded : AdState()
    data class Error(val message: String) : AdState()
}