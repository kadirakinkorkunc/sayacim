package com.example.sayacim.Hatirlatici;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sayacim.Util.DatabaseHelper;
import com.example.sayacim.R;

import java.util.Calendar;

public class AddReminderFragment extends Fragment {
    private static final String TAG = "AddReminderFragment";
    EditText etBaslik, etAciklama;
    Button btnKaydet,btnDatePick,btnTimePick;
    TextView tvTarih,tvSaat;
    TextView tvId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addreminder,container,false);
        Log.d(TAG,"onCreateView:starting.");

        etBaslik = view.findViewById(R.id.etYeniHatirlaticiBaslik);
        etAciklama = view.findViewById(R.id.etYeniHatirlaticiAciklama);
        btnDatePick = view.findViewById(R.id.btnYeniHatirlaticiDatePicker);
        btnKaydet = view.findViewById(R.id.btnYeniHatirlaticiEkle);
        btnTimePick = view.findViewById(R.id.btnYeniHatirlaticiTimePicker);
        tvSaat = view.findViewById(R.id.tvSaat);
        tvTarih = view.findViewById(R.id.tvTarih);
        tvId = view.findViewById(R.id.single_item_id);

        AddData();
        DatePicker();
        TimePick();
        return  view;
    }


    public void AddData(){
        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("girdik");
                String title = etBaslik.getText().toString();
                String aciklama = etAciklama.getText().toString();
                String tarih = tvTarih.getText().toString();
                String saat = tvSaat.getText().toString();


                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.COL_TITLE,title);
                cv.put(DatabaseHelper.COL_DESC,aciklama);
                cv.put(DatabaseHelper.COL_DATE,tarih);
                cv.put(DatabaseHelper.COL_TIME,saat);


                ((HatirlaticiMainActivity)getActivity()).InsertData(cv);
            }
        });
    }

    private void TimePick() {
        btnTimePick.setOnClickListener(new View.OnClickListener() {//saatButona Click Listener ekliyoruz

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldýk
                int minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayý aldýk
                TimePickerDialog timePicker; //Time Picker referansýmýzý oluþturduk

                //TimePicker objemizi oluþturuyor ve click listener ekliyoruz
                timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvSaat.setText( selectedHour + ":" + selectedMinute);//Ayarla butonu týklandýðýnda textview'a yazdýrýyoruz
                    }
                }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("Saat Seçiniz");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Ýptal", timePicker);

                timePicker.show();
            }
        });
    }

    public void DatePicker(){
        btnDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tvTarih.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day);
                dpd.show();
            }});
    }
}
