package tamhoang.ldpro4.Activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import tamhoang.ldpro4.Congthuc.BaseToolBarActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Activity_thaythe extends BaseToolBarActivity {
    Button btn_Luu;
    Database db;
    ListView lv_thaythe;
    private List<String> mNoidung = new ArrayList();
    private List<String> mThaythe = new ArrayList();
    TextView tvNDthaythe;
    TextView tvThaythe;

    /* access modifiers changed from: protected */
    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity
    public int getLayoutId() {
        return R.layout.activity_thaythe;
    }

    /* access modifiers changed from: protected */
    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.SupportActivity, android.support.v4.app.FragmentActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thaythe);
        this.tvThaythe = (TextView) findViewById(R.id.tv_Thaythe);
        this.tvNDthaythe = (TextView) findViewById(R.id.tv_ndThaythe);
        this.lv_thaythe = (ListView) findViewById(R.id.lv_thaythe);
        Button button = (Button) findViewById(R.id.btn_luu);
        this.btn_Luu = button;
        button.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_thaythe.AnonymousClass1 */

            public void onClick(View v) {
                Cursor cursor = Activity_thaythe.this.db.GetData("Select count(id) From thay_the_phu WHERE str = '" + Activity_thaythe.this.tvThaythe.getText().toString() + "'");
                cursor.moveToFirst();
                if (cursor.getInt(0) == 0) {
                    Activity_thaythe.this.db.QueryData("Insert into thay_the_phu values (null, '" + Activity_thaythe.this.tvThaythe.getText().toString() + "', '" + Activity_thaythe.this.tvNDthaythe.getText().toString() + "')");
                } else {
                    Database database = Activity_thaythe.this.db;
                    database.QueryData("Update thay_the_phu set str_rpl = '" + Activity_thaythe.this.tvNDthaythe.getText().toString() + "' WHERE str = '" + Activity_thaythe.this.tvThaythe.getText().toString() + "'");
                }
                Activity_thaythe.this.tvThaythe.setText("");
                Activity_thaythe.this.tvNDthaythe.setText("");
                Activity_thaythe.this.listview_thaythe();
            }
        });
        this.lv_thaythe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_thaythe.AnonymousClass2 */

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Activity_thaythe.this.tvThaythe.setText((CharSequence) Activity_thaythe.this.mNoidung.get(position));
                Activity_thaythe.this.tvNDthaythe.setText((CharSequence) Activity_thaythe.this.mThaythe.get(position));
            }
        });
        this.db = new Database(this);
        listview_thaythe();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void listview_thaythe() {
        this.mNoidung.clear();
        this.mThaythe.clear();
        Cursor cursor = this.db.GetData("Select * FROM thay_the_phu");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                this.mNoidung.add(cursor.getString(1));
                this.mThaythe.add(cursor.getString(2));
            }
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        this.lv_thaythe.setAdapter((ListAdapter) new Thaythe_Adapter(this, R.layout.activity_thaythe_lv, this.mNoidung));
    }

    /* access modifiers changed from: package-private */
    public class Thaythe_Adapter extends ArrayAdapter {
        public Thaythe_Adapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        public View getView(final int position, View v, ViewGroup parent) {
            View v2 = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_thaythe_lv, (ViewGroup) null);
            ((TextView) v2.findViewById(R.id.tv_stt)).setText((position + 1) + "");
            ((TextView) v2.findViewById(R.id.tv_cumtu)).setText((CharSequence) Activity_thaythe.this.mNoidung.get(position));
            ((TextView) v2.findViewById(R.id.tv_thaybang)).setText((CharSequence) Activity_thaythe.this.mThaythe.get(position));
            ((TextView) v2.findViewById(R.id.tv_delete)).setOnClickListener(new View.OnClickListener() {
                /* class tamhoang.ldpro4.Activity.Activity_thaythe.Thaythe_Adapter.AnonymousClass1 */

                public void onClick(View v) {
                    Activity_thaythe.this.db.QueryData("Delete From thay_the_phu WHERE str = '" + ((String) Activity_thaythe.this.mNoidung.get(position)) + "'");
                    Activity_thaythe.this.listview_thaythe();
                }
            });
            return v2;
        }
    }
}