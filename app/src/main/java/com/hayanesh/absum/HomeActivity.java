package com.hayanesh.absum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.adroitandroid.chipcloud.FlowLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fontometrics.Fontometrics;
import com.facebook.stetho.Stetho;
import com.john.waveview.WaveView;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class HomeActivity extends AppCompatActivity {
    private TextView textView;
    private SeekBar seekBar;
    private WaveView waveView;
    private FloatingActionButton fab;
    LinearLayout l;
    int flag = 0;
    List<Aspects> aspects;
    List<Aspects> aspects_temp;
    final String URL = "http://192.168.43.50/Absum/retriveDetails.php";
    public RequestQueue requestqueue;
    public StringRequest stringRequest;
    int id[] = new int[]{100,101,102,103};
    String products[];
    String pname;
    int pid = 0;
    PrefManager prefManager;
    DatabaseHelper db;
    ChipCloud chipCloud;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Stetho.initializeWithDefaults(this);
        textView = (TextView)findViewById(R.id.title);
        final Typeface customtypeface = Typeface.createFromAsset(this.getAssets(),"fonts/Lobster-Regular.ttf");
        textView.setTypeface(customtypeface);
        waveView = (WaveView)findViewById(R.id.wave_view);
        fab = (FloatingActionButton)findViewById(R.id.search);
        l = (LinearLayout)findViewById(R.id.product_panel);

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
        final Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slide_down.setFillAfter(true);
        slide_up.setFillAfter(true);
        products = new String []{"One plus 3T","Moto G","Moto G play","Iphone 6"};

        prefManager = new PrefManager(getApplicationContext());
        db = new DatabaseHelper(getApplicationContext());
        requestqueue = Volley.newRequestQueue(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //rippleBackground.stopRippleAnimation();

                chipCloud.chipDeselected(position);
                Intent toMain = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(toMain);
                //Log.d("ot",pid+" "+pname);



            }
        });
        chipCloud = (ChipCloud) findViewById(R.id.chip_cloud);

       new ChipCloud.Configure()
                .chipCloud(chipCloud)
                .selectedColor(Color.parseColor("#2196f3"))
                .selectedFontColor(Color.parseColor("#ffffff"))
                .deselectedColor(Color.parseColor("#e1e1e1"))
                .deselectedFontColor(Color.parseColor("#333333"))
                .selectTransitionMS(500)
                .deselectTransitionMS(250)
                .labels(products)
                .mode(ChipCloud.Mode.SINGLE)
                .allCaps(false)
                .gravity(ChipCloud.Gravity.STAGGERED)
                .textSize(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin))
                .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
                .typeface(Fontometrics.too_freakin_cute(this))
                .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
                .chipListener(new ChipListener() {
                    @Override
                    public void chipSelected(final int index) {
                        position = index;
                        pid = id[index];
                        pname = products[index];
                        l.animate().translationY(900).start();
                        waveView.setProgress(30);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rippleBackground.setVisibility(View.VISIBLE);
                            }
                        },100);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new AspectAsyc().execute();
                            }
                        },200);

                    }
                    @Override
                    public void chipDeselected(int index) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rippleBackground.setVisibility(View.INVISIBLE);
                            }
                        },20);
                       l.animate().translationY(-50).start();
                        waveView.setProgress(80);



                    }
                })
                .build();

    }

    class AspectAsyc extends AsyncTask<Void,Void,Void> {
        final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(final Void... params) {
            aspects = new ArrayList<>();
            aspects_temp = new ArrayList<>();
            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d("APS", response);
                        JSONObject json = new JSONObject(response);

                        if (json.names().get(0).equals("success")) {
                            JSONArray complete_arr = json.getJSONArray("success");
                            JSONObject aspect_details = complete_arr.getJSONObject(0);
                            JSONArray content = aspect_details.getJSONArray("asp");
                            double scr = 0;

                            db.DeleteAspects();
                            for (int i = 0; i < content.length() - 1; i++) {
                                JSONObject j = content.getJSONObject(i);
                                String asp_name = j.getString("aspect");
                                double asp_score = j.getDouble("score");
                                double app_qual = j.getDouble("quality");
                                String summ = j.getString("summary");
                                String pos = j.getString("pros");
                                String neg = j.getString("cons");
                                scr = scr + asp_score;
                                int q = (int) app_qual;
                                Aspects a = new Aspects(String.valueOf(pid),asp_name, String.valueOf(q), summ,pos,neg);
                                aspects_temp.add(a);
                            }
                            String pr[] = new String[]{"camera","battery","display","sound","memory","network","processor","os","body","conns"};
                            for(int i=0;i<pr.length;i++) {
                                for (int j = 0; j < aspects_temp.size(); j++) {
                                    Aspects b = aspects_temp.get(j);
                                    if (b.getName().equals(pr[i])) {
                                        Log.d("asp", b.getName());
                                        db.createDetails(b);
                                    }
                                }
                            }
                            Log.d("score", String.valueOf(scr));
                            JSONObject pn = content.getJSONObject(content.length() - 1);
                            int pos = pn.getInt("pos");
                            int neg = pn.getInt("neg");
                            prefManager.setAspectId(String.valueOf(pid));
                            prefManager.setAspectPos(String.valueOf(pos));
                            prefManager.setAspectNeg(String.valueOf(neg));
                            prefManager.setAspectTotal(String.valueOf(pos+neg));
                            prefManager.setAspectScore((int)scr);

                        }

                    } catch (JSONException e) {
                        Log.e("ASP", e.toString());
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ASP", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("pid", String.valueOf(pid));
                    return map;
                }
            };
            requestqueue.add(stringRequest);
            return null;
        }
    }

}
