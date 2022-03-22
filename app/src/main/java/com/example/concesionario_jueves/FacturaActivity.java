package com.example.concesionario_jueves;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FacturaActivity extends AppCompatActivity {
    EditText etcodigo,etfecha,etidentificacion,etplaca;
    Button btguardar, btconsultar, btanular, btcancelar, btregresar;
    long resp, sw;
    String codFactura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);
        getSupportActionBar().hide();
        etcodigo = findViewById(R.id.etcodigo);
        etfecha = findViewById(R.id.etfecha);
        etidentificacion = findViewById(R.id.etidentificacion);
        etplaca = findViewById(R.id.etplaca);
        btguardar = findViewById(R.id.btguardar);
        btanular = findViewById(R.id.btanular);
        btcancelar = findViewById(R.id.btcancelar);
        btconsultar = findViewById(R.id.btconsultar);
        btregresar = findViewById(R.id.btregresar);
    }
    public void Guardar(View view) {
        String fecha, iden, placa;
        codFactura = etcodigo.getText().toString();
        fecha = etfecha.getText().toString();
        iden = etidentificacion.getText().toString();
        placa = etplaca.getText().toString();

        if (codFactura.isEmpty() || fecha.isEmpty() || iden.isEmpty() || placa.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        } else {
            Conexion_concesionario admin = new Conexion_concesionario(this, "concesionario5.bd", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("codFactura", codFactura);
            registro.put("fecha", fecha);
            registro.put("identificacion", iden);
            registro.put("placa", placa);
            ConsultarVehiculo();
            if (sw == 1) {
                sw = 0;
                resp = db.update("TblFactura", registro, "codFactura='" + codFactura + "'", null);
            } else {
                resp = db.insert("TblFactura", null, registro);
            }

            if (resp > 0) {
                Limpiar_campos();
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error en guardar el registro", Toast.LENGTH_SHORT).show();
            }
            //   db.close();
        }
    }
    public void Consulta_vehiculo(View view) {
        ConsultarVehiculo();
    }

    public void ConsultarVehiculo() {

        codFactura = etcodigo.getText().toString();
        if (codFactura.isEmpty()) {
            Toast.makeText(this, "El codigo de factura es requerida para buscar", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        } else {
            Conexion_concesionario admin = new Conexion_concesionario(this, "concesionario5.bd", null, 1);
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor fila = db.rawQuery("select * from TblFactura where codFactura='" + codFactura + "'", null);
            if (fila.moveToNext()) {
                sw = 1;
                etfecha.setText(fila.getString(1));
                etidentificacion.setText(fila.getString(2));
                etplaca.setText(fila.getString(3));
            } else {
                Toast.makeText(this, "Registro no hallado", Toast.LENGTH_SHORT).show();
            }
            //   db.close();
        }
    }
    public void AnularVehiculo(View view) {
        ConsultarVehiculo();
        if (sw == 1) {
            Conexion_concesionario admin = new Conexion_concesionario(this, "concesionario5.bd", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("codFactura", codFactura);
            registro.put("activo", "no");
            resp = db.update("TblFactura", registro, "codFactura='" + codFactura + "'", null);
            if (resp > 0) {
                Toast.makeText(this, "Registro Anulado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            } else {
                Toast.makeText(this, "Error al anular el registro", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "La placa no esta registrada", Toast.LENGTH_SHORT).show();
        }
    }
    public void Limpiar_campos() {
        sw = 0;
        etcodigo.setText("");
        etplaca.setText("");
        etfecha.setText("");
        etidentificacion.setText("");
        etcodigo.requestFocus();
    }

    public void Regresar(View view) {
        Intent main = new Intent(this, MenuActivity.class);
        startActivity(main);
    }
}