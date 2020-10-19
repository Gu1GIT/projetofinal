package com.example.evento;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.evento.modelo.Evento;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int requestCodeNovoEvento = 1;
    private final int resultCodeNovoEvento = 10;
    private final int requestCodeEditarEvento = 2;
    private final int resultCodeEventotoEditado = 11;
    private final int resultCodeEventoExcluido = 12;

    private ListView listViewEventos;
    private ArrayAdapter<Evento> adapterEventos;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Eventos");

        listViewEventos = findViewById(R.id.listView_eventos);
        ArrayList<Evento> eventos = new ArrayList<Evento>();

        adapterEventos = new ArrayAdapter<Evento>(MainActivity.this, android.R.layout.simple_list_item_1, eventos);
        listViewEventos.setAdapter(adapterEventos);

        onClickListaEventos();
    }

    private void onClickListaEventos() {
        listViewEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Evento eventoSelecionado = adapterEventos.getItem(position);
                Intent intent = new Intent(MainActivity.this, CadastroEvento.class);
                intent.putExtra("eventotoSelecionado", eventoSelecionado);
                startActivityForResult(intent, requestCodeEditarEvento);
            }
        });
    }


    public void onClickNew(View v) {
        Intent intent = new Intent(MainActivity.this, CadastroEvento.class);
        startActivityForResult(intent, requestCodeNovoEvento);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == requestCodeNovoEvento && resultCode == resultCodeNovoEvento) {
            Evento evento = (Evento) data.getExtras().getSerializable("novoEvento");
            evento.setId(++id);
            this.adapterEventos.add(evento);
        } else if (requestCode == requestCodeEditarEvento && resultCode == resultCodeEventotoEditado) {
            Evento eventoEditado = (Evento) data.getExtras().getSerializable("eventoEditado");
            for (int i = 0; i < adapterEventos.getCount(); i++) {
                Evento evento = adapterEventos.getItem(i);
                if (evento.getId() == eventoEditado.getId()) {
                    adapterEventos.remove(evento);
                    adapterEventos.insert(eventoEditado, i);
                    break;
                }
            }
        } else if (requestCode == requestCodeEditarEvento && resultCode == resultCodeEventoExcluido) {
            Evento eventoExcluido = (Evento) data.getExtras().getSerializable("eventoExcluido");
            for (int i = 0; i < adapterEventos.getCount(); i++) {
                Evento evento = adapterEventos.getItem(i);
                if (evento.getId() == eventoExcluido.getId()) {
                    adapterEventos.remove(evento);
                    break;
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}