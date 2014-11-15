package com.example.miw;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class Configuracion extends ListActivity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_configuracion);
        setListAdapter(
                 new ArrayAdapter<String>(this,
                       R.layout.elemento_configuracion,
                       R.id.titulo
                      // Asteroides.almacen.listaPuntuaciones(10)
                       ));
    }

}
