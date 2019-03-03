package com.example.slesha.recyclerview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Data> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DataAdapter mAdapter;
    EditText ed1,ed2,ed3;
    String product_id="";
    String product_name="";
    String product_price="";
    Button b1;
    private boolean multiSelect = false;
    private ArrayList<Integer> selectedItems = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ed1 = (EditText) findViewById(R.id.productId);
        ed2 = (EditText) findViewById(R.id.productName);
        ed3 = (EditText) findViewById(R.id.productPrice);
        b1 = (Button) findViewById(R.id.btnAdd);
        mAdapter = new DataAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_id = ed1.getText().toString();
                product_name = ed2.getText().toString();
                product_price = ed3.getText().toString();
                prepareMovieData(product_id,product_name,product_price);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

                removeData(position);
            //    recyclerView.removeItemDecorationAt(position);
            }
        }));

    }
    private void removeData(int position){
        movieList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }
    private void prepareMovieData(String product_id,String product_name,String product_price) {


        Data movie = new Data(product_id,product_name,product_price);
        movieList.add(movie);


        mAdapter.notifyDataSetChanged();
    }

    public static class ClickListner {
    }
}
interface ClickListener{
    public void onClick(View view,int position);
    public void onLongClick(View view,int position);
}
class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

    private ClickListener clicklistener;
    private GestureDetector gestureDetector;

    public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

        this.clicklistener=clicklistener;
        gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                if(child!=null && clicklistener!=null){
                    clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child=rv.findChildViewUnder(e.getX(),e.getY());
        if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
            clicklistener.onClick(child,rv.getChildAdapterPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
