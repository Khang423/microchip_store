package com.example.microchip.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.microchip.GlobalSession;
import com.example.microchip.R;
import com.example.microchip.db.CustomerHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private Button btnChangeImage, btnEdit;
    private CircleImageView imageReview;
    private TextInputEditText inputName, inputTel, inputMail, inputPassword, inputGender, inputBirthday, inputAddress;
    private Uri selectedImageUri;
    private SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        String img_url = GlobalSession.getSession().getUrl_avatar();
        String name = GlobalSession.getSession().getName();
        String email = GlobalSession.getSession().getEmail();
        String tel = GlobalSession.getSession().getTel();
        String pass = GlobalSession.getSession().getPassword();
        int gender = GlobalSession.getSession().getGender();
        String birthdate = GlobalSession.getSession().getBirthday();
        String address = GlobalSession.getSession().getAddress();

        imageReview.setImageURI(Uri.parse(img_url));
        inputName.setText(name);
        inputMail.setText(email);
        inputTel.setText(tel);
        inputBirthday.setText(birthdate);
        if(gender == 1){
            inputGender.setText("Nam");
        }else {
            inputGender.setText("Nữ");
        }

        inputPassword.setText(pass);
        inputAddress.setText(address);

        inputBirthday.setOnClickListener(v -> showDatePickerDialog());

        btnChangeImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                changeImage();
            }
        });

        btnEdit.setOnClickListener(v -> update());

        return view;
    }

    private void init(View view) {
        btnChangeImage = view.findViewById(R.id.btn_change_image);
        imageReview = view.findViewById(R.id.imageReview);
        btnEdit = view.findViewById(R.id.btn_edit);
        inputName = view.findViewById(R.id.input_name);
        inputTel = view.findViewById(R.id.input_tel);
        inputMail = view.findViewById(R.id.input_mail);
        inputPassword = view.findViewById(R.id.input_password);
        inputAddress = view.findViewById(R.id.input_address);
        inputBirthday = view.findViewById(R.id.input_birthday);
        inputGender = view.findViewById(R.id.input_gender);
    }

    private void changeImage() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageReview.setImageURI(selectedImageUri);
        }
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            inputBirthday.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    public void update() {
        int id = GlobalSession.getSession().getId();
        String oldUriImg = GlobalSession.getSession().getUrl_avatar();
        String name = inputName.getText().toString().trim();
        String tel = inputTel.getText().toString().trim();
        String email = inputMail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();
        String birthday = inputBirthday.getText().toString().trim();
        int gender = Integer.parseInt(inputGender.getText().toString().trim());
        String imgPath = selectedImageUri != null ? selectedImageUri.toString() : oldUriImg;
        String hashPassword = BCrypt.withDefaults().hashToString(12,password.toCharArray());

        CustomerHelper dbHelper = new CustomerHelper(getContext());
        try {
            dbHelper.update(id, name, email, tel, imgPath, gender, birthday, hashPassword, address);
            Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(getContext(), "Lỗi khi cập nhật thông tin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
