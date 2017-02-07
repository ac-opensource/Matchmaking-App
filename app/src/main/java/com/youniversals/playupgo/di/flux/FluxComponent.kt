package com.youniversals.playupgo.di.flux;

import com.youniversals.playupgo.di.*
import com.youniversals.playupgo.login.LoginActivity
import com.youniversals.playupgo.main.MainActivity
import com.youniversals.playupgo.main.MatchPickerBottomSheetDialogFragment
import com.youniversals.playupgo.matchdetail.MatchDetailsActivity
import com.youniversals.playupgo.newmatch.AddNewMatchActivity
import com.youniversals.playupgo.newmatch.PickSportStepFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ActionCreatorModule::class,
        AppModule::class,
        RestModule::class,
        ModelModule::class,
        StoreModule::class)
)
interface FluxComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(matchDetailsActivity: MatchDetailsActivity)
    fun inject(matchPickerBottomSheetDialogFragment: MatchPickerBottomSheetDialogFragment)
    fun inject(newMatchActivity: AddNewMatchActivity)
    fun inject(pickSportStepFragment: PickSportStepFragment)

}