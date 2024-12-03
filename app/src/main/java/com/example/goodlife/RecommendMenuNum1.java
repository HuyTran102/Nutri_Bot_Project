package com.example.goodlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class RecommendMenuNum1 extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private TextView banhPho, thitBo1, banhQuay, rauSong, suaTuoi, chuoiTieu, gaoTe1, thitBaChi
            , caThu, cai, thitBam, dauAn1, nho, gaoTe2, caRo, thitBo2, rauMuong, dauAn2, taoTay
            , tongSang, tongTrua, tongToi;
    private String name;
    private double recommendWeight, recommendEnergy;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_menu_num1);

        banhPho = findViewById(R.id.banh_pho_g);
        thitBo1 = findViewById(R.id.thit_bo_1_g);
        banhQuay = findViewById(R.id.banh_quay_g);
        rauSong = findViewById(R.id.rau_song_g);
        suaTuoi = findViewById(R.id.sua_tuoi_g);
        chuoiTieu = findViewById(R.id.chuoi_tieu_g);
        gaoTe1 = findViewById(R.id.gao_te_1_g);
        thitBaChi = findViewById(R.id.thit_ba_chi_g);
        caThu = findViewById(R.id.ca_thu_g);
        cai = findViewById(R.id.cai_g);
        thitBam = findViewById(R.id.thit_bam_g);
        dauAn1 = findViewById(R.id.dau_an_1_g);
        nho = findViewById(R.id.nho_g);
        gaoTe2 = findViewById(R.id.gao_te_2_g);
        caRo = findViewById(R.id.ca_ro_g);
        thitBo2 = findViewById(R.id.thit_bo_2_g);
        rauMuong = findViewById(R.id.rau_muong_g);
        dauAn2 = findViewById(R.id.dau_an_2_g);
        taoTay = findViewById(R.id.tao_tay_g);
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
                Intent intent = new Intent(RecommendMenuNum1.this, RecommendMenu.class);
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

                            banhPho.setText(String.format("%.0f", (sang * 25 / 100) * 100 / 143));
                            thitBo1.setText(String.format("%.0f", (sang * 10 / 100) * 100 / 118));
                            banhQuay.setText(String.format("%.0f", (sang * 23 / 100) * 100 / 292));
                            rauSong.setText(String.format("%.0f", (sang * 1 / 100) * 100 / 18));
                            suaTuoi.setText(String.format("%.0f", (sang * 26 / 100) * 100 / 74));
                            chuoiTieu.setText(String.format("%.0f", (sang * 15 / 100) * 100 / 99));

                            gaoTe1.setText(String.format("%.0f", (trua * 48 / 100) * 100 / 347));
                            thitBaChi.setText(String.format("%.0f", (trua * 15 / 100) * 100 / 260));
                            caThu.setText(String.format("%.0f", (trua * 15 / 100) * 100 / 166));
                            cai.setText(String.format("%.0f", (trua * 3 / 100) * 100 / 17));
                            thitBam.setText(String.format("%.0f", (trua * 6 / 100) * 100 / 260));
                            dauAn1.setText(String.format("%.0f", (trua * 3 / 100) * 100 / 900));
                            nho.setText(String.format("%.0f", (trua * 10 / 100) * 100 / 68));

                            gaoTe2.setText(String.format("%.0f", (toi * 48 / 100) * 100 / 347));
                            caRo.setText(String.format("%.0f", (toi * 20 / 100) * 100 / 126));
                            thitBo2.setText(String.format("%.0f", (toi * 15 / 100) * 100 / 118));
                            rauMuong.setText(String.format("%.0f", (toi * 4 / 100) * 100 / 25));
                            dauAn2.setText(String.format("%.0f", (toi * 3 / 100) * 100 / 900));
                            taoTay.setText(String.format("%.0f", (toi * 10 / 100) * 100 / 48));

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