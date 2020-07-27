package harish.hibare.databasetest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static harish.hibare.databasetest.MainActivity.db;

public class AdminPage extends AppCompatActivity implements View.OnClickListener {
    EditText editfname, editlname, editEmail;
    Button btnAdd, btnDelete, btnModify, btnView, btnViewAll, btnShowInfo;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        editfname = findViewById(R.id.editfname);
        editlname = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editemail);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnModify = findViewById(R.id.btnModify);
        btnView = findViewById(R.id.btnView);
        btnViewAll = findViewById(R.id.btnViewAll);
        btnShowInfo = findViewById(R.id.btnShowInfo);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        btnShowInfo.setOnClickListener(this);
        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(firstname VARCHAR,lastname VARCHAR,email VARCHAR,password VARCHAR,location VARCHAR,gender VARCHAR);");

        editfname.setFilters(new InputFilter[]{new MainActivity.EmojiExcludeFilter()});
        editEmail.setFilters(new InputFilter[]{new MainActivity.EmojiExcludeFilter()});
        editlname.setFilters(new InputFilter[]{new MainActivity.EmojiExcludeFilter()});

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                if (editfname.getText().toString().trim().length() == 0 ||
                        editlname.getText().toString().trim().length() == 0 ||
                        editEmail.getText().toString().trim().length() == 0) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    showMessage("Error", "Please enter all values");
                    return;
                }
                db.execSQL("INSERT INTO student(firstname,lastname,email) VALUES('" + editfname.getText() + "','" + editlname.getText() +
                        "','" + editEmail.getText() + "');");
                showMessage("Success", "Record added");
                clearText();
                break;
            case R.id.btnDelete:
                if (editEmail.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Email_ID");
                    return;
                }
                cursor = db.rawQuery("SELECT * FROM student WHERE email='" + editEmail.getText() + "'", null);
                if (cursor.moveToFirst()) {
                    db.execSQL("DELETE FROM student  WHERE email='" + editEmail.getText() + "'");
                    showMessage("Success", "Record Deleted");
                    clearText();
                }
                break;
            case R.id.btnModify:
                if (editEmail.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Email_ID");
                    return;
                }
                cursor = db.rawQuery("SELECT * FROM student  WHERE email='" + editEmail.getText() + "'", null);
                if (cursor.moveToFirst()) {
                    db.execSQL("UPDATE student SET lastname='" + editlname.getText() + "',firstname='" + editfname.getText() +
                            "'  WHERE email='" + editEmail.getText() + "'");
                    showMessage("Success", "Record Modified");
                }
                break;
            case R.id.btnView:
                if (editEmail.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Email_ID");
                    return;
                }
                cursor = db.rawQuery("SELECT * FROM student WHERE email='" + editEmail.getText() + "'", null);
                if (cursor.moveToFirst()) {
                    editlname.setText(cursor.getString(1));
                    editfname.setText(cursor.getString(0));
                }
                else
                {
                    showMessage("Error", "Invalid Email_ID");
                    clearText();
                }
                break;
            case R.id.btnViewAll:
                cursor = db.rawQuery("SELECT * FROM student ", null);
                if (cursor.getCount()==0)
                {
                    showMessage("Error","No Records Found");
                    return;
                }
                else
                {
                    StringBuffer buffer = new StringBuffer();
                    while (cursor.moveToNext())
                    {
                        buffer.append("F_Nmae : "+cursor.getString(0)+"\n");
                        buffer.append("L_Name : "+cursor.getString(1)+"\n");
                        buffer.append("Email  : "+cursor.getString(2)+"\n");
                    }
                    showMessage("All Recods !!",buffer.toString());
                }
                break;
            case R.id.btnShowInfo:
                showMessage("Database Application", "Developed By Harish Hibare");
                break;
        }

    }
    private void showMessage(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setIcon(R.mipmap.my_icon1);

        builder.setCancelable(true);
        builder.show();
    }
    private void clearText() {
        editfname.setText("");
        editlname.setText("");
        editEmail.setText("");
        editEmail.requestFocus();
    }

}