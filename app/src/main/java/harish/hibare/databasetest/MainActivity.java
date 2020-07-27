package harish.hibare.databasetest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edt_firstname,edt_lastname,edt_emailadd,edt_pas,edt_confpas,location;
    Button btn_Register;
    Spinner spinner;
    TextView txv_reg;

    static String s_lastname, s_firstname, s_email, s_pass, s_cnf_pass,s_location,s_gender;
    static SQLiteDatabase db;

    int flag=0,f1=0;
    String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String passwordpattern =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_firstname=findViewById(R.id.edt_firstname);
        edt_lastname=findViewById(R.id.edt_lastname);
        edt_emailadd=findViewById(R.id.edt_email);
        edt_pas=findViewById(R.id.edt_pass);
        edt_confpas=findViewById(R.id.edt_confpass);
        btn_Register=findViewById(R.id.regbtn);
        location=findViewById(R.id.edLocation);
        spinner=findViewById(R.id.spinner);
        txv_reg=findViewById(R.id.txv_register);


        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(firstname VARCHAR,lastname VARCHAR,email VARCHAR,password VARCHAR,location VARCHAR,gender VARCHAR);");


        edt_firstname.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        edt_lastname.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        edt_emailadd.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        edt_pas.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        edt_confpas.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        location.setFilters(new InputFilter[]{new EmojiExcludeFilter()});

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                s_gender= String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void register_ac(View view) {


        s_firstname = edt_firstname.getText().toString().toUpperCase().trim();
        s_lastname = edt_lastname.getText().toString().toUpperCase().trim();
        s_email = edt_emailadd.getText().toString().toLowerCase().trim();
        s_pass = edt_pas.getText().toString().trim();
        s_cnf_pass = edt_confpas.getText().toString().trim();
        s_location = location.getText().toString().trim();





        if(s_firstname.isEmpty())
        {

            edt_firstname.setError("Please enter your first name");
            return;

        }else if(s_lastname.isEmpty())
        {
            edt_lastname.setError("Please Enter Your last Name");
            return;
        }else  if(s_email.isEmpty() || !s_email.matches(emailPattern))
        {

            edt_emailadd.setError("Please enter right email Address");
            return;



        }
        else if (   s_pass.isEmpty()|| s_cnf_pass.isEmpty()|| !s_pass.equals(s_cnf_pass)  ) {
            Toast.makeText(MainActivity.this,"Password Not matching",Toast.LENGTH_LONG).show();
            edt_pas.setError("Please enter right Password");
            return;

        } else if(!s_pass.matches(passwordpattern))
        {

            Toast.makeText(MainActivity.this,"Password length is between 6 to 20",Toast.LENGTH_LONG).show();
            edt_pas.setError("password should contain uppercase lowercase and special symbol");

        }else if(s_gender.isEmpty()){
            Toast.makeText(MainActivity.this,"Please select your gender",Toast.LENGTH_LONG).show();
            edt_pas.setError("Please select your Gender");
            return;
        }


        else   {

            flag=1;
        }

        if(flag==1)
        {
            db.execSQL("INSERT INTO student VALUES('"+s_firstname+"','"+s_lastname+"','"+s_email+"','"+s_pass+ "','"+s_location+"','"+s_gender+"');");
            show_Message_Confirmation("Success","Registration Done");
            clearText();

        }



    }


    private void show_Message_Confirmation(String success, String o) {
        final AlertDialog.Builder builder_1 = new AlertDialog.Builder(this);
        builder_1.setTitle(success);
        builder_1.setMessage(o);
        builder_1.setIcon(R.mipmap.my_icon1);
        flag=0;
        builder_1.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent_my = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent_my);
                builder_1.setCancelable(true);
            }
        });
        builder_1.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();

            }
        });
        builder_1.show();


    }




    public void reg_to_login(View view) {
        Intent intent_log = new Intent(getApplicationContext(),LoginPage.class);
        startActivity(intent_log);
    }
    private void clearText() {
        edt_firstname.setText("");
        edt_lastname.setText("");
        edt_emailadd.setText("");
        edt_pas.setText("");
        edt_confpas.setText("");
        location.setText("");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                s_gender= String.valueOf(adapterView.getItemAtPosition(0));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public static class EmojiExcludeFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                } }

            return null;
        }
    }
}