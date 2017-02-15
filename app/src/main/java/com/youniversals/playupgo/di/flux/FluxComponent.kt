package com.youniversals.playupgo.di.flux;

import com.youniversals.playupgo.di.*
import com.youniversals.playupgo.login.LoginActivity
import com.youniversals.playupgo.main.MainActivity
import com.youniversals.playupgo.main.MatchPickerBottomSheetDialogFragment
import com.youniversals.playupgo.matchdetail.MatchDetailsActivity
import com.youniversals.playupgo.newmatch.AddNewMatchActivity
import com.youniversals.playupgo.newmatch.step.AddDetailsStepFragment
import com.youniversals.playupgo.newmatch.step.PickSportStepFragment
import com.youniversals.playupgo.newmatch.step.SetDateAndTimeStepFragment
import com.youniversals.playupgo.newmatch.step.SetLocationStepFragment
import com.youniversals.playupgo.notifications.NotificationActivity
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
    fun inject(addDetailsStepFragment: AddDetailsStepFragment)
    fun inject(setDateAndTimeStepFragment: SetDateAndTimeStepFragment)
    fun inject(setLocationStepFragment: SetLocationStepFragment)
    fun inject(notificationActivity: NotificationActivity)

}