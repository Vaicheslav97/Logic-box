import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FileNav extends AppCompatActivity implements MyDialogListener {

    private String ESDPath = Environment.getExternalStorageDirectory().getPath();
    private String currentPath = Environment.getExternalStorageDirectory().getPath();
    private List<File> files ;//= new ArrayList<File>();
    ListView FList;
    TextView TV;
    String NFN = null;

    private FilenameFilter filenameFilter;
    private int selectedIndex = -1;

    private Drawable folderIcon;
    private Drawable fileIcon;
    private String accessDeniedMessage;
    private boolean isOnlyFoldersFilter;

    public void setFolderIcon(Drawable drawable) {
        this.folderIcon = drawable;
    }

    public void setFileIcon(Drawable drawable) {
        this.fileIcon = drawable;
    }

    private List getFiles(String directoryPath){
        File directory = new File(directoryPath,"");
        List<File> fileList = Arrays.asList(directory.listFiles());
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                if (file.isDirectory() && file2.isFile())
                    return -1;
                else if (file.isFile() && file2.isDirectory())
                    return 1;
                else
                    return file.getPath().compareTo(file2.getPath());
            }
        });
        return fileList;
    }

    private void setDrawable(TextView view, Drawable drawable) {
        if (view != null) {
            if (drawable != null) {
                drawable.setBounds(0, 0, 60, 60);
                view.setCompoundDrawables(drawable, null, null, null);
            } else {
                view.setCompoundDrawables(null, null, null, null);
            }
        }
    }

    @Override
    public void onReturnValue(String foo) {
         NFN = foo;
        if(!NFN.matches("")){
            Intent intent = new Intent();
            intent.putExtra("name", currentPath+'/'+NFN+".nfn");
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    private class FileAdapter extends ArrayAdapter<File> {

        public FileAdapter(Context context, List<File> files) {
            super(context, android.R.layout.simple_list_item_1, files);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            File file = getItem(position);
            if (view != null) {
                view.setText(file.getName());
                if (file.isDirectory()) {
                    setDrawable(view, folderIcon);
                } else {
                    setDrawable(view, fileIcon);
                    if (selectedIndex == position)
                        view.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
                    else
                        view.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
                }
            }
            return view;
        }
    }

    public void onPrevClick(View view) {
        File file = new File(currentPath);
        File parentDirectory = file.getParentFile();
        if (parentDirectory != null && !currentPath.matches(ESDPath)) {
            currentPath = parentDirectory.getPath();
            RebuildFiles(((FileAdapter) FList.getAdapter()));
        }
    }


    private static Display getDefaultDisplay(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    private static Point getScreenSize(Context context) {
        Point screeSize = new Point();
        getDefaultDisplay(context).getSize(screeSize);
        return screeSize;
    }

    public int getTextWidth(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.left + bounds.width() + 80;
    }

    private void changeTitle() {
        String titleText = currentPath;
        int screenWidth = getScreenSize(this).x;
        int maxWidth = (int) (screenWidth * 0.99);
        if (getTextWidth(titleText, TV .getPaint()) > maxWidth) {
            while (getTextWidth("..." + titleText, TV .getPaint()) > maxWidth) {
                int start = titleText.indexOf("/", 2);
                if (start > 0)
                    titleText = titleText.substring(start);
                else
                    titleText = titleText.substring(2);
            }
            TV .setText("..." + titleText);
        } else {
            TV .setText(titleText);
        }
    }

    private void RebuildFiles(ArrayAdapter<File> adapter) {
        try {

            files=getFiles(currentPath);
            FList.setAdapter(new FileAdapter(this, files));
            //files.clear();
            selectedIndex = -1;


            changeTitle();
        } catch (NullPointerException e) {
            String message = getResources().getString(android.R.string.unknownName);
            if (!accessDeniedMessage.equals(""))
                message = accessDeniedMessage;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_nav);
        FList =(ListView) findViewById (R.id.FList);
        TV = (TextView) findViewById (R.id.textView2);
        TV.setText(currentPath);
        // getFiles(currentPath);
        files =getFiles(currentPath);
        FList.setAdapter(new FileAdapter(this, files));
        getSupportActionBar().setTitle("Logic Box");
        FList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                final ArrayAdapter<File> adapter = (FileAdapter) adapterView.getAdapter();
                File file = adapter.getItem(index);
                if (file.isDirectory()) {
                    currentPath = file.getPath();
                    RebuildFiles(adapter);
                } else {
                    if (index != selectedIndex)
                        selectedIndex = index;
                    else
                        selectedIndex = -1;
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        Button SoOBtn =(Button) findViewById (R.id.SoOBtn);
        if(getIntent().getCharExtra("Mode", 'R')=='W'){
            SoOBtn.setText("Save");
        }else{SoOBtn.setText("Open");}
    }



    public void onSoOBtnClick(View v) {
        boolean Wmode = getIntent().getCharExtra("Mode", 'R')=='W';

            if(selectedIndex!=-1){
            Intent intent = new Intent();
            intent.putExtra("name", files.get(selectedIndex).getPath());
            setResult(RESULT_OK, intent);
            finish();
            }else if(Wmode){
                showMyDialog(this);
            }

    }

    private void showMyDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Enter file name")
                //.setMessage("Enter your mobile number?")
                .setView(taskEditText)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MyDialogListener activity = (MyDialogListener) getActivity();
                        activity.onReturnValue(taskEditText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
  }
