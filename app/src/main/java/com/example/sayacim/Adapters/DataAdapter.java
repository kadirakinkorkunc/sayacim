package com.example.sayacim.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sayacim.Hatirlatici.HatirlaticiMainActivity;
import com.example.sayacim.Util.DatabaseHelper;
import com.example.sayacim.R;

import java.util.Calendar;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private Integer mExpandedPosition = -1;
    private Integer previousExpandedPosition = -1;
    private long mTimeLeftInMillis;
    CountDownTimer countDownTimer;

    public DataAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;

    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);


        /**
         * viewGroup.getId() -> ui'de degisiklik yaparsan bu id'de değişir.
         */
        System.out.println(viewGroup.getId());
        if ( (viewGroup.getId()) == 2131230854){
            View view = layoutInflater.inflate(R.layout.layout_single_tatil_item,viewGroup,false);
            return new DataViewHolder(view);
        }
        else{
            View view = layoutInflater.inflate(R.layout.layout_single_item,viewGroup,false);
            return new DataViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder dataViewHolder, int position) {
        // datanın view'de görüntülendiğinden emin olacağız.
        if (!mCursor.moveToPosition(position)){ // burda işaretçinin pozisyona gidebildiği yani öyle bi girdi oldugunu doğruluyoruz
            return;
        }

        /**
         * açılır kapanır liste elemanı için logic
         */
        final boolean isExpanded = position==mExpandedPosition;
        dataViewHolder.expandablePart.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        dataViewHolder.itemView.setActivated(isExpanded);
        if (isExpanded){
            previousExpandedPosition = position;
        }
        dataViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            }
        });


        /**
         * eğer delete butonu yok ise (hatırlatıcılarda değil isem buton listener koyma cünkü buton yok)
         */
        if ( dataViewHolder.itemView.findViewById(R.id.btnSingleItemDelete) == null ){
        }else{
            dataViewHolder.delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper db = new DatabaseHelper(mContext);
                    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                    mCursor.moveToPosition(position);
                    Integer id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.COL_ID));
                    sqLiteDatabase.delete(DatabaseHelper.R_TABLE_NAME,DatabaseHelper.COL_ID + "=\"" + id+"\"",null);
                    ((HatirlaticiMainActivity)mContext).delData(id);

                }
            });
        }



        /**
         * single itemi dolduracak veriler db'den cekilip viewlere aktarılıyor
         */
        String baslik = mCursor.getString(mCursor.getColumnIndex((DatabaseHelper.COL_TITLE)));
        String aciklama = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_DESC));
        String tarih = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_DATE));
        String zaman = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_TIME));

        dataViewHolder.baslikText.setText(baslik);
        dataViewHolder.aciklamaText.setText(aciklama);
        dataViewHolder.tarihText.setText(tarih);



        mTimeLeftInMillis = getCalendarTimeInMillis(tarih,zaman);

        /**
         * prevents flickiring
         */
        if (dataViewHolder.timer != null){
            dataViewHolder.timer.cancel();
        }

        /**
         * countdowntimer
         */
        dataViewHolder.timer = new CountDownTimer(mTimeLeftInMillis,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                int day = (int) ((((mTimeLeftInMillis / 1000) / 60) / 60) / 24) ;
                int hour = (int) (((mTimeLeftInMillis / 1000) / 60) / 60) % 24 ;
                int minute = (int) ((mTimeLeftInMillis / 1000) / 60) % 60 ;
                int second = (int) (mTimeLeftInMillis / 1000) % 60 ;

                dataViewHolder.tvCountdown.setText(day + " gun " + hour + " saat " + minute + " dakika " + second + " saniye kaldi." );
            }

            @Override
            public void onFinish() {
                dataViewHolder.tvCountdown.setText("Gecmis etkinlik.");
            }
        }.start();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout expandablePart;
        public TextView idText,tarihText,aciklamaText,baslikText, tvCountdown;
        public Button delButton;
        CountDownTimer timer;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            idText = itemView.findViewById(R.id.single_item_id);
            delButton = itemView.findViewById(R.id.btnSingleItemDelete);
            expandablePart = itemView.findViewById(R.id.subItem);
            baslikText = itemView.findViewById(R.id.tvSingleItemBaslik);
            aciklamaText = itemView.findViewById(R.id.subItemDesc);
            tarihText = itemView.findViewById(R.id.tvSingleItemTarih);
            tvCountdown = itemView.findViewById(R.id.subItemCount);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public long getCalendarTimeInMillis(String tarih,String zaman){
        String[] tarihParts = tarih.split("\\/");
        String[] zamanParts = zaman.split("\\:");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(tarihParts[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(tarihParts[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tarihParts[0]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(zamanParts[0]));
        calendar.set(Calendar.MINUTE,Integer.parseInt(zamanParts[1]));
        calendar.set(Calendar.SECOND,00);

        Calendar now = Calendar.getInstance();


        long left = calendar.getTimeInMillis() - now.getTimeInMillis() ;
        System.out.println(left/1000/60/60/24);
        return left;
    }

    public Cursor swapCursor(Cursor newCursor){
        //update atınca yenı cursor atamamız lazım
        if (mCursor !=null){
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null){
            notifyDataSetChanged();
            return mCursor;
        }
        else{return mCursor; }
    }
}
