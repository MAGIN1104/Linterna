package com.example.botonesconaccion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    int n=0;
    Camera Camara;
    Camera.Parameters Parametros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Abrir camera, en el hardware como tal
        //Camara = android.hardware.Camera.open();
        //Parametros = Camara.getParameters();
        findViewById(R.id.btnOn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encenderluz();
            }
        });
        findViewById(R.id.btnOff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(n==1)
                {
                    off();
                    n=0;
                    Toast.makeText(getApplicationContext(),"LINTERNA APAGADA", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"NO ENCENDISTE LA LINTERNA", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults)
    {
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "PERMISOS ACEPTADOS", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "PERMISOS DENEGADOS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void encenderluz() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
        {
            // SI EL PERMISO YA ESTA DISPONIBLE ENSENDEMOS LA LINTERNA
            Toast.makeText(getApplicationContext(),"LINTERNA ENCENDIDA", Toast.LENGTH_SHORT).show();
                    Camara = android.hardware.Camera.open();
                    Parametros = Camara.getParameters();
                    on();
                    n=1;
        } else {
            // DE LO CONTRARIO VOLVEMOS A SOLICITAR PERMISOS
            solicitudCamara();
        }
    }
    private void solicitudCamara() {
        // LOS PERMISOS QUE NO SE NOS OTORGA DEBEMOS SOLICITARLO
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
        {
           ActivityCompat.requestPermissions(MainActivity.this,
                   new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        } else {
           ActivityCompat.requestPermissions(this,
                   new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    //ENCENDER EL FLASH
    public  void on()
    {
        Parametros.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
        Camara.setParameters(Parametros);
        Camara.startPreview();
    }
    //APAGAR EL FLASH
    public  void off()
    {
        Parametros.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
        Camara.setParameters(Parametros);
        Camara.stopPreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Camara!=null)
        {Camara.release();}
    }
}
