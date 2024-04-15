package com.example.navee_1_5;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.widget.Button;
import android.view.View;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import com.example.navee_1_5.databinding.ActivityMainBinding;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PythonInterpreter interpreter;
    private Button voiceControlButton;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Khởi tạo một interpreter Python
        interpreter = new PythonInterpreter();

        voiceControlButton = findViewById(R.id.voice_control_button);

        // Gán sự kiện nghe cho button
        voiceControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức startVoiceControl khi button được nhấn
                startVoiceControl();
            }
        });
    }


    private void startVoiceControl() {
        // Mở microphone và chờ người dùng nói
        InputStream inputStream = getResources().openRawResource(R.raw.voicereg);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        // Đọc nội dung của BufferedReader thành một chuỗi
        StringBuilder pythonScript = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                pythonScript.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        interpreter.exec(pythonScript.toString());

        // Gọi các hàm Python cần thiết
        interpreter.exec("from voicereg import capture_voice_input");
        interpreter.exec("from voicereg import convert_voice_to_text");
        interpreter.exec("from voicereg import process_voice_command");
        interpreter.exec("from voicereg import text_to_speech");
        interpreter.exec("from voicereg import main");

        // Gọi hàm main() và lấy kết quả
        PyObject pyObject = interpreter.get("main").__call__();
        String destinationText = pyObject.toString();

        // Điều hướng dựa trên dữ liệu destinationText
        if ("dashboard".equals(destinationText)) {
            navController.navigate(R.id.navigation_dashboard);
        } else if ("notifications".equals(destinationText)) {
            navController.navigate(R.id.navigation_notifications);
        } else if ("home".equals(destinationText)) {
            navController.navigate(R.id.navigation_home);
        } else {
            System.out.println("Error");
        }
        // Đây là nơi bạn có thể gọi mã Python để xử lý giọng nói
    }

}