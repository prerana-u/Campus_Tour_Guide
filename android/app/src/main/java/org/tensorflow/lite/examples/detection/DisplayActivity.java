package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.examples.detection.data.DatabaseHandler;
import org.tensorflow.lite.examples.detection.models.Building_Info;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class DisplayActivity extends AppCompatActivity {

    ImageView capimg;
    ImageButton arrow,arrow1,arrow2;
    LinearLayout hiddenView,hiddenView2;
    LinearLayout hiddenView1;
    CardView cardView,cardView1,cardView2;
    ArrayList<ListData> modelArrayList;

    public String type="";
    TextView title;
    DatabaseHandler db;
    RecyclerView rv,rv1,rv2;
    String blockname="";
    MyListAdapter ml;

    ImageButton cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent intent = getIntent();
        blockname=intent.getStringExtra("blockname");
        title=findViewById(R.id.title);
        title.setText(blockname);
        cb=findViewById(R.id.camerabtn);
       // Toast.makeText(getApplicationContext(),blockname,Toast.LENGTH_SHORT).show();
//        Intent intent = getIntent();
//        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
        db= new DatabaseHandler(this);
        rv = findViewById(R.id.officedatarv);
        rv1=findViewById(R.id.officedatarv1);
        rv2=findViewById(R.id.officedatarv2);

        rv.setHasFixedSize(false);

        modelArrayList = new ArrayList<ListData>();


        cardView = findViewById(R.id.base_cardview);
        arrow = findViewById(R.id.arrow_button);
        hiddenView = findViewById(R.id.hidden_view);
        cardView1 = findViewById(R.id.base_cardview1);
        arrow1 = findViewById(R.id.arrow_button1);
        hiddenView1 = findViewById(R.id.hidden_view1);
        cardView2 = findViewById(R.id.base_cardview2);
        arrow2 = findViewById(R.id.arrow_button2);
        hiddenView2 = findViewById(R.id.hidden_view2);

        //dropdown code
        arrow.setOnClickListener(view -> {
            // If the CardView is already expanded, set its visibility
            // to gone and change the expand less icon to expand more.
            if (hiddenView.getVisibility() == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenView.setVisibility(View.GONE);
                arrow.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);

            }

            // If the CardView is not expanded, set its visibility to
            // visible and change the expand more icon to expand less.
            else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenView.setVisibility(View.VISIBLE);
                hiddenView1.setVisibility(View.GONE);
                type="office";
                hiddenView2.setVisibility(View.GONE);
                arrow.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
                arrow1.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                arrow2.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                setData(type);
            }
        });

        arrow1.setOnClickListener(view -> {

            if (hiddenView1.getVisibility() == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(cardView1, new AutoTransition());
                hiddenView1.setVisibility(View.GONE);
                arrow1.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);

            }

            // If the CardView is not expanded, set its visibility to
            // visible and change the expand more icon to expand less.
            else {
                TransitionManager.beginDelayedTransition(cardView1, new AutoTransition());
                hiddenView1.setVisibility(View.VISIBLE);
                hiddenView.setVisibility(View.GONE);
                hiddenView2.setVisibility(View.GONE);
                arrow1.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
                arrow.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                arrow2.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                type="department";
                setData(type);
            }
        });

        arrow2.setOnClickListener(view -> {

            if (hiddenView2.getVisibility() == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(cardView2, new AutoTransition());
                hiddenView2.setVisibility(View.GONE);
                arrow2.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);

            }

            // If the CardView is not expanded, set its visibility to
            // visible and change the expand more icon to expand less.
            else {
                TransitionManager.beginDelayedTransition(cardView2, new AutoTransition());
                hiddenView2.setVisibility(View.VISIBLE);
                hiddenView1.setVisibility(View.GONE);
                hiddenView.setVisibility(View.GONE);
                arrow2.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
                arrow1.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                arrow.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                type="others";
                setData(type);
            }
        });

         Bitmap bitmap = BitmapData.getInstance().getBitmap();
         capimg=findViewById(R.id.capimg);
         capimg.setImageBitmap(bitmap);

         cb.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i=new Intent(DisplayActivity.this,DetectorActivity.class);
                 startActivity(i);
             }
         });



    }

    void setData(String type)
    {

        List<Building_Info> data = db.getAllBuilding_Info(blockname.toLowerCase(),type);
        modelArrayList.clear();
        //Toast.makeText(getApplicationContext(),blockname.toLowerCase()+type,Toast.LENGTH_SHORT).show();
        for (Building_Info cn : data) {

         //   Toast.makeText(getApplicationContext(),cn.getName(),Toast.LENGTH_SHORT).show();
            modelArrayList.add(new ListData(cn.getName(),cn.getRoomNo()));
            ml = new MyListAdapter( modelArrayList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            if (type.equals("office"))
            {
                rv.setLayoutManager(linearLayoutManager);
                rv.setAdapter(ml);
            }
            else if (type.equals("department")) {
                rv1.setLayoutManager(linearLayoutManager);
                rv1.setAdapter(ml);
            }
            else{
                rv2.setLayoutManager(linearLayoutManager);
                rv2.setAdapter(ml);
            }

        }
    }
}