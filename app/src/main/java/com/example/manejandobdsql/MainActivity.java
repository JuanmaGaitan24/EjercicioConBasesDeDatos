package com.example.manejandobdsql;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etidentificador, etMarca, etModelo, etPrecio, etPreInicial, etPreFinal;
    Button btnMostrar, btnGuardar, btnBorrar, btnActualizar, btnBuscar;
    ListView lista;
    ArrayList<Movil> movil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etidentificador = findViewById(R.id.editTextID);
        etMarca = findViewById(R.id.editTextMarca);
        etModelo = findViewById(R.id.editTextModelo);
        etPrecio = findViewById(R.id.editTextPrecio);
        btnMostrar = findViewById(R.id.buttonMostrar);
        btnGuardar = findViewById(R.id.buttonGuardar);
        btnBorrar = findViewById(R.id.buttonBorrar);
        btnActualizar = findViewById(R.id.buttonActualizar);
        lista = findViewById(R.id.listviewlista);
        btnBuscar = findViewById(R.id.buttonBuscar);
        etPreInicial = findViewById(R.id.editTextPrecioIni);
        etPreFinal = findViewById(R.id.editTextPrecioFin);

        ManejadorBD manejadorBD = new ManejadorBD(this);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean resultado = manejadorBD.insertar(etModelo.getText().toString(), etMarca.getText().toString(), etPrecio.getText().toString());

                if (resultado) {
                    Toast.makeText(MainActivity.this, "se ha insertado correctamente", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Error al insertar", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor = manejadorBD.listar();
                movil = new ArrayList<>();
                ArrayAdapter <String> arrayAdapter;
                List<String> list = new ArrayList<>();

                if (cursor != null && cursor.getCount() > 0){
                    while (cursor.moveToNext()){
                        Movil movil1 = new Movil(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                        String fila = movil1.toString();
                        list.add(fila);
                        movil.add(movil1);
                    }

                    arrayAdapter = new ArrayAdapter<>(getBaseContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
                    lista.setAdapter(arrayAdapter);

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            etidentificador.setText(movil.get(i).getId());
                            etModelo.setText(movil.get(i).getModelo());
                            etMarca.setText(movil.get(i).getMarca());
                            etPrecio.setText(movil.get(i).getPrecio());

                        }
                    });
                }

            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean resultado = manejadorBD.actualizar(etidentificador.getText().toString(), etModelo.getText().toString(), etMarca.getText().toString(), etPrecio.getText().toString());

                Toast.makeText(MainActivity.this, resultado?"Modificado correctamente":"No se ha modificado", Toast.LENGTH_SHORT).show();
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("¿BORRAR?");
                builder.setMessage("¿Seguro que quiere borrar el registro?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean resultado = manejadorBD.borrar(etidentificador.getText().toString());
                        Toast.makeText(MainActivity.this, resultado?"Borrado correctamente":"No se ha Borrado", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    int preini = Integer.valueOf(String.valueOf(etPreInicial.getText()));
                    int prefin = Integer.valueOf(String.valueOf(etPreFinal.getText()));

                    manejadorBD.ListarPorPrecios(preini, prefin);

                    if (lista.getCount() == 0){
                        Toast.makeText(MainActivity.this, "Intorduzca los numeros correctamente",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Intorduzca los numeros correctamente",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    class Movil{

        String id, modelo, marca, precio;

        public Movil(String id, String modelo, String marca, String precio) {
            this.id = id;
            this.modelo = modelo;
            this.marca = marca;
            this.precio = precio;
        }

        @Override
        public String toString() {
            return "Movil{" +
                    "id='" + id + '\'' +
                    ", modelo='" + modelo + '\'' +
                    ", marca='" + marca + '\'' +
                    ", precio='" + precio + '\'' +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getModelo() {
            return modelo;
        }

        public void setModelo(String modelo) {
            this.modelo = modelo;
        }

        public String getMarca() {
            return marca;
        }

        public void setMarca(String marca) {
            this.marca = marca;
        }

        public String getPrecio() {
            return precio;
        }

        public void setPrecio(String precio) {
            this.precio = precio;
        }
    }

}