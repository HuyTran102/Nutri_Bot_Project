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

public class RecommendMenuNum2 extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private TextView bun, thitBo1, chanGio, rauSong, suaChua, dauTay, gaoTe1, thitGa
            , muc, khoaiTim, suon, dauAn1, duaHau, gaoTe2, caHoi, thitBo2, rauMongToi, dauAn2, mitThai
            , tongSang, tongTrua, tongToi;
    private String name;
    private double recommendWeight, recommendEnergy;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_menu_num2);

        // Make status bar fully transparent
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));

        // Set the layout to extend into the status bar
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        bun = findViewById(R.id.bun_g);
        thitBo1 = findViewById(R.id.thit_bo_1_g);
        chanGio = findViewById(R.id.chan_gio_g);
        rauSong = findViewById(R.id.rau_song_g);
        suaChua = findViewById(R.id.sua_chua_g);
        dauTay = findViewById(R.id.dau_tay_g);
        gaoTe1 = findViewById(R.id.gao_te_1_g);
        thitGa = findViewById(R.id.thit_ga_g);
        muc = findViewById(R.id.muc_g);
        khoaiTim = findViewById(R.id.khoai_tim_g);
        suon = findViewById(R.id.suon_g);
        dauAn1 = findViewById(R.id.dau_an_1_g);
        duaHau = findViewById(R.id.dua_hau_g);
        gaoTe2 = findViewById(R.id.gao_te_2_g);
        caHoi = findViewById(R.id.ca_hoi_g);
        thitBo2 = findViewById(R.id.thit_bo_2_g);
        rauMongToi = findViewById(R.id.rau_mong_toi_g);
        dauAn2 = findViewById(R.id.dau_an_2_g);
        mitThai = findViewById(R.id.mit_thai_g);
        tongSang = findViewById(R.id.tong_sang);
        tongTrua = findViewById(R.id.tong_trua);
        tongToi = findViewById(R.id.tong_toi);
        backButton = findViewById(R.id.back_button);

        SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);

        name = sp.getString("Name", null);

        LoadData();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendMenuNum2.this, RecommendMenu.class);
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

                            bun.setText(String.format("%.0f", (sang * 30 / 100) * 100 / 110));
                            thitBo1.setText(String.format("%.0f", (sang * 16 / 100) * 100 / 118));
                            chanGio.setText(String.format("%.0f", (sang * 27 / 100) * 100 / 230));
                            rauSong.setText(String.format("%.0f", (sang * 2 / 100) * 100 / 18));
                            suaChua.setText(String.format("%.0f", (sang * 15 / 100) * 100 / 61));
                            dauTay.setText(String.format("%.0f", (sang * 10 / 100) * 100 / 43));

                            gaoTe1.setText(String.format("%.0f", (trua * 48 / 100) * 100 / 347));
                            thitGa.setText(String.format("%.0f", (trua * 18 / 100) * 100 / 199));
                            muc.setText(String.format("%.0f", (trua * 12 / 100) * 100 / 73));
                            khoaiTim.setText(String.format("%.0f", (trua * 6 / 100) * 100 / 109));
                            suon.setText(String.format("%.0f", (trua * 8 / 100) * 100 / 187));
                            dauAn1.setText(String.format("%.0f", (trua * 3 / 100) * 100 / 900));
                            duaHau.setText(String.format("%.0f", (trua * 5 / 100) * 100 / 16));

                            gaoTe2.setText(String.format("%.0f", (toi * 48 / 100) * 100 / 347));
                            caHoi.setText(String.format("%.0f", (toi * 20 / 100) * 100 / 136));
                            thitBo2.setText(String.format("%.0f", (toi * 17 / 100) * 100 / 118));
                            rauMongToi.setText(String.format("%.0f", (toi * 3 / 100) * 100 / 14));
                            dauAn2.setText(String.format("%.0f", (toi * 2 / 100) * 100 / 756));
                            mitThai.setText(String.format("%.0f", (toi * 10 / 100) * 100 / 50));

                            tongSang.setText(String.format("%.0f", sang));

                            tongTrua.setText(String.format("%.0f", trua));

                            tongToi.setText(String.format("%.0f", toi));

                        }

                        Log.d("Firestore", "All tasks completed successfully");
                    } else {
                        Log.w("Firestore", "Error completing tasks", task.getException());
                    }
                });
    }
}