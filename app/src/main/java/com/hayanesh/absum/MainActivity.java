package com.hayanesh.absum;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final String URL = "http://192.168.1.6/Absum/retriveDetails.php";
    public RequestQueue requestqueue;
    public StringRequest stringRequest;
    RecyclerView recyclerView2;
    int progress = 60;
    List<Aspects> aspects;
    List<Aspects> aspects_temp;
    int i;
    String pid ="101";
    private TextView p_name;
    private TextView p_total;
    private TextView p_pos,p_neg;
    DonutProgress arcProgress;
    PrefManager prefManager;
    DatabaseHelper db;
    String products[];
    List<Aspects> asp_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollView));
        scrollview.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_UP);
            }
        },10);
        p_name = (TextView)findViewById(R.id.product_name);
        p_total = (TextView)findViewById(R.id.total);
        p_pos = (TextView)findViewById(R.id.pos);
        p_neg = (TextView)findViewById(R.id.neg);
        arcProgress = (DonutProgress) findViewById(R.id.arc_progress);
        prefManager = new PrefManager(getApplicationContext());
        db = new DatabaseHelper(getApplicationContext());
        products = new String []{"One plus 3T","Moto G","Moto G play","Iphone 6"};
       // new AspectAsyc().execute();

        pid = prefManager.getAspectId();
        Log.d("PID",pid);
        String pname = products[Integer.parseInt(prefManager.getAspectId())-100];
        p_name.setText(pname);
        final Typeface customtypeface = Typeface.createFromAsset(this.getAssets(),"fonts/Lobster-Regular.ttf");
        p_name.setTypeface(customtypeface);
        p_pos.setText(prefManager.getAspectPos());
        p_neg.setText(prefManager.getAspectNeg());
        p_total.setText(prefManager.getAspectTotal());
        progress = prefManager.getAspectScore();
        arcProgress.setProgress(progress);

        aspects_temp = db.getAspectList(pid);
        Log.d("SIZE","size"+aspects_temp.size());

        String pr[] = new String[]{"camera","battery","display","sound","memory","network","processor","os","body","conns"};



        Aspects a1 = new Aspects("100","Camera","67","faskdfjaklfjlkafkl f.ajdflkasjflkajfajlkfadf.afalsfj","100","100");
        Aspects a2 = new Aspects("100","Display","67","faskdfjaklfjlkafkl f.ajdflkasjflkajfajlkfadf.afalsfj","100","100");
        asp_list = new ArrayList<Aspects>();
        asp_list.add(a1);
        asp_list.add(a2);

        AspectAdapter aspectAdapter = new AspectAdapter(this,aspects_temp);
        recyclerView2 = (RecyclerView)findViewById(R.id.recycler_view2);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView2.setLayoutManager(mLayoutManager);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(aspectAdapter);
    }
   class AspectAsyc extends AsyncTask<Void,Void,Void>
    {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("processing");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d("Prg",String.valueOf(progress));
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            aspects = new ArrayList<>();
            aspects_temp = new ArrayList<>();
            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        Log.d("APS",response);
                        JSONObject json = new JSONObject(response);

                        if(json.names().get(0).equals("success")){
                            JSONArray complete_arr = json.getJSONArray("success");
                            JSONObject aspect_details = complete_arr.getJSONObject(0);
                            JSONArray content = aspect_details.getJSONArray("asp");
                            double scr= 0;
                            for(int i=0;i<content.length()-1;i++)
                            {
                                JSONObject j = content.getJSONObject(i);
                                String asp_name = j.getString("aspect");
                                double asp_score = j.getDouble("score");
                                double app_qual = j.getDouble("quality");
                                String summ = j.getString("summary");
                                scr = scr+asp_score;
                                int q = (int)app_qual;
                                Aspects a = new Aspects(pid,asp_name,String.valueOf(q),summ,null,null);
                                aspects_temp.add(a);
                            }
                            Log.d("score",String.valueOf(scr));
                            JSONObject pn = content.getJSONObject(content.length()-1);
                            int pos = pn.getInt("pos");
                            int neg = pn.getInt("neg");

                            Log.d("scr",String.valueOf(scr));

                            String pr[] = new String[]{"camera","battery","display","sound","memory","network","processor","os","body","conns"};
                            ArrayList<Integer> ind = new ArrayList<>();
                            for(int i=0;i<pr.length;i++) {
                                for (int j = 0; j < aspects_temp.size(); j++) {
                                    Aspects b = aspects_temp.get(j);
                                    if (b.getName().equals(pr[i])) {
                                        Log.d("asp", b.getName());
                                        aspects.add(b);
                                        ind.add(j);
                                    }
                                }
                            }

                        }

                    }catch (JSONException e)
                    {
                        Log.e("ASP",e.toString());
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ASP",error.toString());
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("pid",String.valueOf(pid));
                    return map;
                }
            };
            requestqueue.add(stringRequest);
            return null;
        }
    }

}
