package harish.hibare.databasetest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static harish.hibare.databasetest.LoginPage.cursor;
import static harish.hibare.databasetest.LoginPage.email;
import static harish.hibare.databasetest.MainActivity.db;

public class Welcome extends AppCompatActivity {
    TextView txv_personMail;
    EditText txv_personfName, txv_PersonLname,txvloc;
    int isRecordDeleted=0;

    Button show,modify,deleterec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        txv_personfName =findViewById(R.id.txvPersonName);
        txv_PersonLname =findViewById(R.id.txvlnamePerson);
        txv_personMail=findViewById(R.id.txvemaiPerson);
        txvloc=findViewById(R.id.txvloc);
        txv_personMail.setText(email);

        modify=findViewById(R.id.modify);
        deleterec=findViewById(R.id.deletereco);
        txv_personfName.setFilters(new InputFilter[]{new MainActivity.EmojiExcludeFilter()});
        txv_PersonLname.setFilters(new InputFilter[]{new MainActivity.EmojiExcludeFilter()});
        txv_personMail.setFilters(new InputFilter[]{new MainActivity.EmojiExcludeFilter()});


        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(firstname VARCHAR,lastname VARCHAR,email VARCHAR,password VARCHAR,location VARCHAR,gender VARCHAR);");

        cursor = db.rawQuery("SELECT * FROM student WHERE email='" + email + "'", null);
        if (cursor.moveToFirst()) {
            txv_personfName.setText(cursor.getString(0));
            txv_PersonLname.setText(cursor.getString(1));
            txvloc.setText(cursor.getString(4));

        }


    }

    public void modify_detail(View view) {
        cursor = db.rawQuery("SELECT * FROM student WHERE email='" + email + "'", null);
        if (cursor.moveToFirst()) {
            db.execSQL("UPDATE student SET lastname='" + txv_PersonLname.getText() + "',firstname='" + txv_personfName.getText() +
                    "',location='" + txvloc.getText() + "' WHERE email='" + email + "'");
            showMessage("Success", "Record Modified");
        }

    }

    public void delete_rec(View view) {
        cursor = db.rawQuery("SELECT * FROM student WHERE email='" + email + "'", null);
        if (cursor.moveToFirst()) {
            db.execSQL("DELETE FROM student WHERE email='"+email+"'");
            isRecordDeleted=1;
            showMessage("Success", "Record Deleted");

        }
    }

    private void showMessage(String success, String o) {
        final AlertDialog.Builder builder_1 = new AlertDialog.Builder(this);
        builder_1.setTitle(success);
        builder_1.setMessage(o);
        builder_1.setIcon(R.mipmap.my_icon1);
        builder_1.setCancelable(false);
        if(isRecordDeleted==1){

            builder_1.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    builder_1.setCancelable(true);
                    finish();
                }
            });}
        if(isRecordDeleted==0){
            builder_1.setCancelable(false);
            builder_1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder_1.setCancelable(true);
                }
            });}
        builder_1.show();
        isRecordDeleted=0;
    }
}