package com.huafa.lixianjicha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by Administrator on 2017-02-22.
 */
public abstract class baseActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(savedInstanceState);
        //setContentView(R.layout.toolbar);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.zuozhe:
                /*        AlertDialog.Builder b = new AlertDialog.Builder(baseActivty.this);
                        b.setTitle("作者");
                        b.setMessage("朱晓琳");
                        b.create().show();*/
                        Intent intent=new Intent(baseActivty.this,shangYiCiJiChaJiLu.class);
                        startActivity(intent);
                        break;
                    case  R.id.sousuo:

                        break;
                }
                return true;
            }
        });

        getSupportActionBar().setTitle("华发稽查");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = new MenuInflater(this);
        mif.inflate(R.menu.menu_toobar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;


        }
        return true;
    }

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        if(query.equals("1")){
//            Toast.makeText(baseActivty.this, "1", Toast.LENGTH_SHORT).show();
//        }
//        return false;
//    }

    protected abstract void initContentView(Bundle savedInstanceState);

}

