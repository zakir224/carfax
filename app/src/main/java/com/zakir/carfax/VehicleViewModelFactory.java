package com.zakir.carfax;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.zakir.carfax.data.source.Repository;
import com.zakir.carfax.util.Injection;
import com.zakir.carfax.vehicles.VehicleListViewModel;
import com.zakir.carfax.vehicles.details.VehicleDetailsViewModel;
import org.jetbrains.annotations.NotNull;

public class VehicleViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile VehicleViewModelFactory INSTANCE;

    private final Repository mVehicleRepository;

    public static VehicleViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (VehicleViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VehicleViewModelFactory(
                            Injection.INSTANCE.provideRepository(application.getApplicationContext()));
                }
            }
        }
        return INSTANCE;
    }

    private VehicleViewModelFactory(Repository repository) {
        mVehicleRepository = repository;
    }

    @NotNull
    @Override
    public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VehicleListViewModel.class)) {
            //noinspection unchecked
            return (T) new VehicleListViewModel(mVehicleRepository);
        } else if (modelClass.isAssignableFrom(VehicleDetailsViewModel.class)) {
            //noinspection unchecked
            return (T) new VehicleDetailsViewModel(mVehicleRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
