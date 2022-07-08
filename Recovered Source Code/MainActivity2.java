package com.example.bls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class MainActivity2 extends AppCompatActivity {
    GameView GV;
    GameWorld GW;
    Button ItemSelector, RM,RP;

    int SAVE =1;


    int MOV = 0;    //1
    int WIRE = 1;   //2
    int DEL =2;     //3
    int SWITCH =3;
    int IND = 4;
    int NOT = 5;     //4
    int BUF = 6;
    int AND = 7;
    int OR = 8;
    int XOR = 9;
    int N_AND = 10;
    int N_OR = 11;
    int N_XOR = 12;


    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public final int EXTERNAL_REQUEST = 138;

    public boolean requestForPermission() {
        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions( EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }
        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));
    }

    @Override
    public GameWorld onRetainCustomNonConfigurationInstance() {
        return GW;
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Selection", GV.Selection );
        outState.putString("SSelection", ItemSelector.getText().toString() );
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        GV.Selection = savedInstanceState.getInt("Selection");
        ItemSelector.setText(savedInstanceState.getString("SSelection"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setTitle("Logic Box");
        ItemSelector =  (Button)findViewById (R.id.ItemSelector);
        RM=  (Button)findViewById (R.id.RM);
        RP=  (Button)findViewById (R.id.RP);

        registerForContextMenu(ItemSelector);
        GV = findViewById (R.id.Gview);

        GameWorld SGW = (GameWorld)getLastCustomNonConfigurationInstance();
        if(SGW!=null){GW = SGW;}else{
            Intent intent =getIntent();
            String FN = intent.getStringExtra("FileName");
            if(FN!=null){GW = new GameWorld(FN); }else{GW = new GameWorld();}

        }
        GV.GW = GW;

        if(GW.isEditMode){
            ItemSelector.setVisibility(View.VISIBLE);
            RM.setVisibility(View.VISIBLE);
            RP.setVisibility(View.VISIBLE);
        }else{
            ItemSelector.setVisibility(View.GONE);
            RM.setVisibility(View.GONE);
            RP.setVisibility(View.GONE);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        menu.add("Edit mode").setCheckable(true).setChecked(GW.isEditMode);


            menu.add(1, SAVE, 0,"Save");

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        //Пункты меню с ID группы = 1 видны, если в CheckBox стоит галка
        menu.setGroupVisible(1, menu.getItem(0).isChecked());
        return super.onPrepareOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item){
        if(item.isCheckable()) item.setChecked(!item.isChecked());
        GW.isEditMode = item.isChecked();

        if(item.isChecked()){
        ItemSelector.setVisibility(View.VISIBLE);
        RM.setVisibility(View.VISIBLE);
        RP.setVisibility(View.VISIBLE);
        }else{
            ItemSelector.setVisibility(View.GONE);
            RM.setVisibility(View.GONE);
            RP.setVisibility(View.GONE);
        }
       // Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        if(item.getItemId()==SAVE){
            if(requestForPermission()){
                Intent intent = new Intent(MainActivity2.this, FileNav.class);
                intent.putExtra("Mode",'W');
                startActivityForResult(intent, 1);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        switch (v.getId()){
            case R.id.ItemSelector:
                menu.add(0, MOV, 0, "MOV");
                menu.add(0, DEL, 0, "DEL");
                menu.add(0, WIRE, 0, "WIRE");
                // menu.add(0, SWITCH, 0, "SWITCH");
                menu.add(0, NOT, 0, "NOT");
                menu.add(0, BUF, 0, "BUF");
                menu.add(0, AND, 0, "AND");
                menu.add(0, OR, 0, "OR");
                menu.add(0, XOR, 0, "XOR");
                menu.add(0, N_AND, 0, "NAND");
                menu.add(0, N_OR, 0, "NOR");
                menu.add(0, N_XOR, 0, "NXOR");
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        GV.Selection = item.getItemId();
        ItemSelector.setText(item.getTitle());
        return super.onContextItemSelected(item);
    }

    public void onRC(View view){

    if(GW.SelEl!=null){
        switch(view.getId()){
            case R.id.RP: GW.SelEl.rot=GW.SelEl.rot+45; break;
            case R.id.RM: GW.SelEl.rot=GW.SelEl.rot-45; break;

        }
    }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        String name = data.getStringExtra("name");
        GW.save(name);
    }

}
