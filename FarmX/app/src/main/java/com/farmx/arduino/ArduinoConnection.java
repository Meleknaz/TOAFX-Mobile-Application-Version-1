package com.farmx.arduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.farmx.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ArduinoConnection extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    boolean connectionstate = false;
    byte BtAdapterSayac = 0;
    Button btnOpen, btnFind, btnAyarlar, btnForward, btnLeft, btnRight, btnBack, btnSensorOpen, btnSensorOff;
    TextView MtxtVwState;
    //EditText edTxt;
    String sGelenVeri;
    boolean bisChecked = false;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino_connection);

        btnOpen = (Button) findViewById(R.id.btnOpen);/*Bluetooth açıp kapatmak buton tanımlıyoruz*/
        btnFind = (Button) findViewById(R.id.btnFind);/*Bluetooth açıldıktan sonra cihazımıza bağlanmak için buton tanımlıyoruz.*/
        btnAyarlar = (Button) findViewById(R.id.btnSettings);/*Bluetooth ayarları sayfasına gitmek için buton tanımlıyoruz.*/
        btnForward = (Button) findViewById(R.id.btnForward);/*Aracı ileri götürmek için  için buton tanımlıyoruz.*/
        btnBack = (Button) findViewById(R.id.btnBack);/*Aracı geri götürmek için  için buton tanımlıyoruz.*/
        btnLeft = (Button) findViewById(R.id.btnLeft);/*Aracı sola götürmek için tanımlıyoruz.*/
        btnRight = (Button) findViewById(R.id.btnRight);/*Aracı sağa götürmek için tanımlıyoruz.*/
       // btnSensorOpen = (Button) findViewById(R.id.btnSensorOpen);/*Aracı sağa götürmek için tanımlıyoruz.*/
        //btnSensorOff = (Button) findViewById(R.id.btnSensorOff);/*Aracı sağa götürmek için tanımlıyoruz.*/
        //edTxt = (EditText) findViewById(R.id.editText);/*Veri girişi almak için edittext tanımlıyoruz.*/
        MtxtVwState = (TextView) findViewById(R.id.MtxtVwState);


        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bisChecked) {
                    try {
                        openBT();
                        findBT();
                        openBT();
                        findBT();
                        openBT();
                        findBT();
                        MtxtVwState.setText("Bağlantı Açıldı");
                        bisChecked = true;
                        btnOpen.setText("Kapat");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        closeBT();
                        MtxtVwState.setText("Bağlantı Kapandı");
                        bisChecked = false;
                        btnOpen.setText("Bt Aç");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    openBT();
                    findBT();
                    openBT();
                    findBT();
                    openBT();
                    findBT();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        btnAyarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ArduinoConnection.this, btsettings.class));
                finish();
            }
        });

        btnSensorOpen.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("EmptyCatchBlock")
            @Override
            public void onClick(View view) {

                try {
                    sendData("W");
                    sendData("S");
                } catch (Exception ignored) {
                }
            }
        });

        btnSensorOff.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("EmptyCatchBlock")
            @Override
            public void onClick(View view) {

                try {
                    sendData("w");
                    sendData("S");
                } catch (Exception ignored) {
                }
            }
        });



        btnForward.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("EmptyCatchBlock")
            @Override
            public void onClick(View view) {

                new CountDownTimer(500, 250) {

                    public void onTick(long millisUntilFinished) {
                        try {
                            sendData("F");
                        } catch (Exception ignored) {
                        }
                    }

                    public void onFinish() {
                        try {
                            sendData("S");
                        } catch (Exception ignored) {
                        }
                    } }.start();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("EmptyCatchBlock")
            @Override
            public void onClick(View view) {

                new CountDownTimer(500, 250) {

                    public void onTick(long millisUntilFinished) {
                        try {
                            sendData("B");
                        } catch (Exception ignored) {
                        }
                    }

                    public void onFinish() {
                        try {
                            sendData("S");
                        } catch (Exception ignored) {
                        }
                    } }.start();
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("EmptyCatchBlock")
            @Override
            public void onClick(View view) {

                new CountDownTimer(500, 250) {

                    public void onTick(long millisUntilFinished) {
                        try {
                            sendData("L");
                        } catch (Exception ignored) {
                        }
                    }

                    public void onFinish() {
                        try {
                            sendData("S");
                        } catch (Exception ignored) {
                        }
                    } }.start();

            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("EmptyCatchBlock")
            @Override
            public void onClick(View view) {

                new CountDownTimer(500, 250) {

                    public void onTick(long millisUntilFinished) {
                        try {
                            sendData("R");
                        } catch (Exception ignored) {
                        }
                    }

                    public void onFinish() {
                        try {
                            sendData("S");
                        } catch (Exception ignored) {
                        }
                    } }.start();
            }
        });

    }

    /**********************************************************************************************
     * onCreate End
     *********************************************************************************************/



    void openBT() throws IOException {

        /*Bluetooth u açıyoruz.*/
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService I
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            beginListenForData();/*Bluetooth üzerinden gelen verileri yakalamak için bir listener oluşturuyoruz.*/
        } catch (Exception ignored) {
        }

    }

    void findBT() {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                MtxtVwState.setText("Bluetooth adaptörü bulunamadı");
            }
            if (BtAdapterSayac == 0) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetooth, 0);
                    BtAdapterSayac = 1;
                }
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (("HC-06").equals(device.getName().toString())) {/*Eşleşmiş cihazlarda HC-06 adında cihaz varsa bağlantıyı aktişleştiriyoruz. Burada HC-05 yerine bağlanmasını istediğiniz Bluetooth adını yazabilirsiniz.*/
                        mmDevice = device;
                        MtxtVwState.setText("Bağlantı Bulundu");
                        connectionstate = true;
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    void closeBT() throws IOException {
        try {
            /*Aktif olan bluetooth bağlantımızı kapatıyoruz.*/
            if (mBluetoothAdapter.isEnabled()) {
                stopWorker = true;
                mBluetoothAdapter.disable();
                mmOutputStream.close();
                mmInputStream.close();
                mmSocket.close();
            } else {
            }
        } catch (Exception ignored) {
        }
    }

    void sendData(String data) throws IOException {
        try {
            if (connectionstate) {
                /*Bluetooth bağlantımız aktifse veri gönderiyoruz.*/
                mmOutputStream.write(String.valueOf(data).getBytes());
            }
        } catch (Exception ignored) {
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();
            final byte delimiter = 10; //This is the ASCII code for a newline character

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];
            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        final byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        final String data = new String(readBuffer, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                sGelenVeri = data.toString();
                                                sGelenVeri = sGelenVeri.substring(0, 3);
                                                MtxtVwState.setText(sGelenVeri);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            stopWorker = true;
                        }
                    }
                }
            });
            workerThread.start();
        } catch (Exception ignored) {
        }
    }

}
