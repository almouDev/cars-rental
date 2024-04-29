package com.almou.carsrental;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.almou.carsrental.entities.Car;
import com.almou.carsrental.firebase.Storage;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private List<Car> carList = new ArrayList<>();

    public void setCars(List<Car> cars) {
        carList = cars;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_main, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.bind(car);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        private TextView modelTextView;
        private TextView characteristics;
        private ImageView imageView;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
        }


        public void bind(Car car) {
            modelTextView.setText(car.getNom());
            Storage.getCarImage(car.getImage(),imageView);
            characteristics.setText(car.getMaxSpeed());
        }
    }
}

