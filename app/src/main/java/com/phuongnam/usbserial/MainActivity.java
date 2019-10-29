package com.phuongnam.usbserial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

public class MainActivity extends AppCompatActivity {

    Button btOpen, btClose,  btWrite;
    EditText etWrite;
    TextView tvRead;
    Spinner spBaud;
    Switch aSwitch;
    Physicaloid mPhysicaloid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btOpen  = findViewById(R.id.openbtn);
        btClose = findViewById(R.id.closebtn);
        btWrite = findViewById(R.id.sendbtn);
        etWrite = findViewById(R.id.edit);
        tvRead  = findViewById(R.id.textshow);
        spBaud = findViewById(R.id.spinner);
        aSwitch = findViewById(R.id.autoscroll);
        setEnabledUi(false);

        mPhysicaloid = new Physicaloid(this);
        mPhysicaloid.setDataBits(8);
        mPhysicaloid.setStopBits(1);

    }

    public void clickOpen(View v){
        String baudrate = spBaud.getSelectedItem().toString();
        if (baudrate.equals("Baudrate")) Toast.makeText(this, R.string.baud_prompt, Toast.LENGTH_LONG).show();
        else {
            switch (baudrate) {
                case "300 baud":
                    mPhysicaloid.setBaudrate(300);
                    break;
                case "1200 baud":
                    mPhysicaloid.setBaudrate(1200);
                    break;
                case "2400 baud":
                    mPhysicaloid.setBaudrate(2400);
                    break;
                case "4800 baud":
                    mPhysicaloid.setBaudrate(4800);
                    break;
                case "9600 baud":
                    mPhysicaloid.setBaudrate(9600);
                    break;
                case "19200 baud":
                    mPhysicaloid.setBaudrate(19200);
                    break;
                case "38400 baud":
                    mPhysicaloid.setBaudrate(38400);
                    break;
                case "576600 baud":
                    mPhysicaloid.setBaudrate(576600);
                    break;
                case "744880 baud":
                    mPhysicaloid.setBaudrate(744880);
                    break;
                case "115200 baud":
                    mPhysicaloid.setBaudrate(115200);
                    break;
                case "230400 baud":
                    mPhysicaloid.setBaudrate(230400);
                    break;
                case "250000 baud":
                    mPhysicaloid.setBaudrate(250000);
                    break;
                default:
                    mPhysicaloid.setBaudrate(9600);
                    break;
            }

            if(mPhysicaloid.open()) {
                setEnabledUi(true);
                tvRead.setText("");
                if(aSwitch.isChecked())
                {
                    tvRead.setMovementMethod(new ScrollingMovementMethod());
                }
                mPhysicaloid.addReadListener(new ReadLisener() {
                    @Override
                    public void onRead(int size) {
                        byte[] buf = new byte[size];
                        mPhysicaloid.read(buf, size);
                        tvAppend(tvRead, Html.fromHtml("<font color=blue>" + new String(buf) + "</font><br>"));
                    }
                });
            } else {
                Toast.makeText(this, "Cannot open", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void clickClose(View v){
        if(mPhysicaloid.close()) {
            mPhysicaloid.clearReadListener();
            setEnabledUi(false);
        }
    }

    public void clickSend(View v){
        String str = etWrite.getText().toString()+"\r\n";
        if(str.length()>0) {
            byte[] buf = str.getBytes();
            mPhysicaloid.write(buf, buf.length);
        }
    }

    private void setEnabledUi(boolean on) {
        if(on) {
            btOpen.setEnabled(false);
            spBaud.setEnabled(false);
            aSwitch.setEnabled(false);
            btClose.setEnabled(true);
            btWrite.setEnabled(true);
            etWrite.setEnabled(true);
        } else {
            btOpen.setEnabled(true);
            spBaud.setEnabled(true);
            aSwitch.setEnabled(true);
            btClose.setEnabled(false);
            btWrite.setEnabled(false);
            etWrite.setEnabled(false);
        }
    }

    Handler mHandler = new Handler();
    private void tvAppend(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
            }
        });
    }
}
