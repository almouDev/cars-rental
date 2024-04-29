package com.almou.carsrental.firebase;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.almou.carsrental.entities.Booking;
import com.almou.carsrental.entities.Car;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Database extends ViewModel {
    private MutableLiveData<List<Car>> listCars;
    private static FirebaseFirestore db=FirebaseFirestore.getInstance();
    private static MutableLiveData<Car> carToBook;
    private static MutableLiveData<List<Booking>> myBookings;
    public  void loadCars(){
        db.collection("cars")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Car> cars=new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Car car=document.toObject(Car.class);
                                car.setReference(document.getId());
                                cars.add(car);
                            }
                            listCars=new MutableLiveData<>();
                            listCars.setValue(cars);
                        }
                    }
                });
    }
    public List<Car> getCars(){
        if (listCars!=null)
            return listCars.getValue();
        listCars=new MutableLiveData<>();
        loadCars();
        return listCars.getValue();
    }

    public void loadCarById(String reference){
        db.collection("cars").document(reference)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Car car=documentSnapshot.toObject(Car.class);
                        car.setReference(documentSnapshot.getId());
                        carToBook.setValue(car);
                    }
                });
    }

    public Car getCarToBook(String reference){
        if (carToBook!=null)
            return carToBook.getValue();
        carToBook=new MutableLiveData<>();
        loadCarById(reference);
        return carToBook.getValue();
    }

    public List<Car> getCarsByMarque(String marque){
        return listCars.getValue()
                .stream()
                .filter(car -> car.getMarque().equals(marque))
                .collect(Collectors.toList());
    }

    public void loadReservations(String email){
        db.collection("reservation")
                .whereEqualTo("client",email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Booking> bookings=new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            Booking booking=documentSnapshot.toObject(Booking.class);
                            booking.setId(documentSnapshot.getId());
                            bookings.add(booking);
                        }
                        myBookings.setValue(bookings);
                    }
                });
    }

    //Get my reservations

    public List<Booking> getMyBookings(String email){
        if (myBookings!=null)
            return myBookings.getValue();
        myBookings=new MutableLiveData<>();
        loadReservations(email);
        return myBookings.getValue();
    }

    //Booking

    public static void book(String email, String carReference, Date book_date, Date return_date, Context context){
        db.collection("cars").document(carReference)
                .get().addOnSuccessListener(documentSnapshot -> {
                    Car car=documentSnapshot.toObject(Car.class);
                    Booking reservation=new Booking();

                    reservation.setClient(email);
                    reservation.setCar(car);
                    reservation.setReturnDate(book_date);
                    reservation.setReturnDate(return_date);
                    db.collection("reservation")
                            .add(reservation)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    Toast.makeText(context,"Your reservation is successfully registered",Toast.LENGTH_LONG).show();
                                }
                                else
                                    Toast.makeText(context,"An error has occurred please try again later",Toast.LENGTH_LONG).show();
                            });
                });
    }
    public static void cancelReservation(String booking_reference){
        db.collection("reservation")
                .document(booking_reference)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        List<Booking> newBooking=myBookings.getValue()
                                .stream()
                                .filter(booking -> !booking.getId().equals(booking_reference))
                                .collect(Collectors.toList());
                        myBookings.setValue(newBooking);
                    }
                });
    }

}
