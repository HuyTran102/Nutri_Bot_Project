package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class RecommendMenuNum3 extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private TextView mien, gioSong, cuaBien, rauSong, caramen, le, gaoTe1, cotLet
            , ghe, trung, bongCaiXanh, dauAn1, mangCau, gaoTe2, caLoc, thitNac, bapCai, dauAn2, duDu
            , tongSang, tongTrua, tongToi;
    private String name;
    private double recommendWeight, recommendEnergy;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_menu_num3);

        // Make status bar fully transparent
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));

        // Set the layout to extend into the status bar
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mien = findViewById(R.id.mien_g);
        gioSong = findViewById(R.id.gio_song_g);
        cuaBien = findViewById(R.id.cua_bien_g);
        rauSong = findViewById(R.id.rau_song_g);
        caramen = findViewById(R.id.caramen_g);
        le = findViewById(R.id.le_g);
        gaoTe1 = findViewById(R.id.gao_te_1_g);
        cotLet = findViewById(R.id.cot_let_g);
        ghe = findViewById(R.id.ghe_g);
        trung = findViewById(R.id.trung_g);
        bongCaiXanh = findViewById(R.id.bong_cai_xanh_g);
        dauAn1 = findViewById(R.id.dau_an_1_g);
        mangCau = findViewById(R.id.mang_cau_g);
        gaoTe2 = findViewById(R.id.gao_te_2_g);
        caLoc = findViewById(R.id.ca_loc_g);
        thitNac = findViewById(R.id.thit_nac_g);
        bapCai = findViewById(R.id.bap_cai_g);
        dauAn2 = findViewById(R.id.dau_an_2_g);
        duDu = findViewById(R.id.du_du_g);
        tongSang = findViewById(R.id.tong_sang);
        tongTrua = findViewById(R.id.tong_trua);
        tongToi = findViewById(R.id.tong_toi);
        backButton = findViewById(R.id.back_button);

        SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sp.getString("Name", null);

        LoadData();

//        Toast.makeText(this, "" + recommendEnergy + "", Toast.LENGTH_SHORT).show();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendMenuNum3.this, RecommendMenu.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void LoadData() {
        // Get the current date
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // tháng 1 bắt đầu từ 0 trong Calendar
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Task<QuerySnapshot> nutritionTask = firebaseFirestore.collection("GoodLife")
                .document(name).collection("Dinh dưỡng")
                .get();

        Task<QuerySnapshot> activityTask = firebaseFirestore.collection("GoodLife")
                .document(name).collection("Hoạt động thể lực")
                .whereEqualTo("year", String.valueOf(year))
                .whereEqualTo("month", String.valueOf(month))
                .whereEqualTo("day", String.valueOf(day))
                .get();

        Tasks.whenAll(nutritionTask, activityTask)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot nutritionResult = nutritionTask.getResult();
                        if (nutritionResult != null) {
                            for (QueryDocumentSnapshot document : nutritionResult) {
                                if (document.getString("userHeight") != null
                                        && document.getString("userWeight") != null
                                        && document.getString("userRecommendHeight") != null
                                        && document.getString("userRecommendWeight") != null) {
                                    try {
                                        recommendWeight = Double.parseDouble(document.getString("userRecommendWeight"));
                                    } catch (Exception e) {
                                        recommendWeight = 0.0;
                                    }
                                }
                            }
                        }

                        recommendEnergy = recommendWeight * 24 * 1.5;

                        QuerySnapshot activityResult = activityTask.getResult();
                        if (activityResult != null) {
                            double total_sum = 0;
                            for (QueryDocumentSnapshot document : activityResult) {
                                String amount = document.getString("userUsedEnergy");
                                if (amount != null && !amount.isEmpty()) {
                                    try {
                                        total_sum += Double.parseDouble(amount);
                                    } catch (NumberFormatException e) {
                                        Log.w("Firestore", "Error parsing used energy", e);
                                    }
                                }
                            }

                            recommendEnergy = recommendWeight * 24 * 1.5 + total_sum;

                            double sang = recommendEnergy * 30 / 100, trua = recommendEnergy * 40 /100, toi = recommendEnergy * 30 / 100;

                            mien.setText(String.format("%.0f", (sang * 36 / 100) * 100 / 332));
                            gioSong.setText(String.format("%.0f", (sang * 10 / 100) * 100 / 136));
                            cuaBien.setText(String.format("%.0f", (sang * 23 / 100) * 100 / 103));
                            rauSong.setText(String.format("%.0f", (sang * 1 / 100) * 100 / 18));
                            caramen.setText(String.format("%.0f", (sang * 20 / 100) * 100 / 109));
                            le.setText(String.format("%.0f", (sang * 10 / 100) * 100 / 45));

                            gaoTe1.setText(String.format("%.0f", (trua * 48 / 100) * 100 / 347));
                            cotLet.setText(String.format("%.0f", (trua * 20 / 100) * 100 / 187));
                            ghe.setText(String.format("%.0f", (trua * 10 / 100) * 100 / 54));
                            trung.setText(String.format("%.0f", (trua * 6 / 100) * 100 / 166));
                            bongCaiXanh.setText(String.format("%.0f", (trua * 3 / 100) * 100 / 26));
                            dauAn1.setText(String.format("%.0f", (trua * 3 / 100) * 100 / 900));
                            mangCau.setText(String.format("%.0f", (trua * 10 / 100) * 100 / 53));

                            gaoTe2.setText(String.format("%.0f", (toi * 48 / 100) * 100 / 347));
                            caLoc.setText(String.format("%.0f", (toi * 26 / 100) * 100 / 406));
                            thitNac.setText(String.format("%.0f", (toi * 15 / 100) * 100 / 139));
                            bapCai.setText(String.format("%.0f", (toi * 3 / 100) * 100 / 29));
                            dauAn2.setText(String.format("%.0f", (toi * 3 / 100) * 100 / 900));
                            duDu.setText(String.format("%.0f", (toi * 5 / 100) * 100 / 36));

                            tongSang.setText(String.format("%.0f", sang));

                            tongTrua.setText(String.format("%.0f", trua));

                            tongToi.setText(String.format("%.0f", toi));

//                            Toast.makeText(this, "" + recommendEnergy + " " + recommendEnergy * 30 / 100 + " " + trua + " " + toi + "", Toast.LENGTH_SHORT).show();

                        }

                        Log.d("Firestore", "All tasks completed successfully");
                    } else {
                        Log.w("Firestore", "Error completing tasks", task.getException());
                    }
                });
    }
}