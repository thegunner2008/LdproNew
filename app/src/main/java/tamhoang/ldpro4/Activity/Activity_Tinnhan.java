package tamhoang.ldpro4.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import tamhoang.ldpro4.Congthuc.BaseToolBarActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Activity_Tinnhan extends BaseToolBarActivity {
    Button btn_suatin;
    Button btn_xoatin;
    Database db;
    EditText editText_suatin;
    String id = "";
    JSONObject json;
    int lv_position = -1;
    ListView lv_suatin;
    private List<String> mDanGoc = new ArrayList();
    private List<String> mPhantich = new ArrayList();
    String ngay_nhan = "";
    String soTN = "";
    String tenKH = "";
    int typeKH;

    /* access modifiers changed from: protected */
    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity
    public int getLayoutId() {
        return R.layout.activity_tinnhan;
    }

    /* access modifiers changed from: protected */
    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.SupportActivity, android.support.v4.app.FragmentActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinnhan);
        this.id = getIntent().getStringExtra("m_ID");
        this.db = new Database(this);
        this.btn_suatin = (Button) findViewById(R.id.btn_suatin_suatin);
        this.btn_xoatin = (Button) findViewById(R.id.btn_suatin_xoatin);
        this.editText_suatin = (EditText) findViewById(R.id.editText_suatin);
        this.lv_suatin = (ListView) findViewById(R.id.lv_suatin);
        Database database = this.db;
        Cursor cursor = database.GetData("Select * From tbl_tinnhanS WHere id = " + this.id);
        cursor.moveToFirst();
        if (cursor.getString(6).contains("ChayTrang")) {
            Toast.makeText(this, "Không sửa được tin chạy vào trang", Toast.LENGTH_SHORT).show();
            cursor.close();
            finish();
            return;
        }
        this.ngay_nhan = cursor.getString(1);
        this.tenKH = cursor.getString(4);
        this.soTN = cursor.getString(7);
        this.typeKH = cursor.getInt(3);
        if (cursor.getString(11).contains("ok")) {
            try {
                this.mDanGoc.clear();
                this.mPhantich.clear();
                this.json = new JSONObject(cursor.getString(15));
                this.editText_suatin.setText(cursor.getString(9));
                Iterator<String> keys = this.json.keys();
                while (keys.hasNext()) {
                    JSONObject dan = this.json.getJSONObject(keys.next());
                    List<String> list = this.mDanGoc;
                    list.add(dan.getString("du_lieu") + " (" + dan.getString("so_luong") + ")");
                    List<String> list2 = this.mPhantich;
                    list2.add(dan.getString("dan_so") + "x" + dan.getString("so_tien"));
                }
                this.lv_suatin.setAdapter((ListAdapter) new TN_Adapter(this, R.layout.frag_suatin_lv1, this.mDanGoc));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            this.editText_suatin.setText(cursor.getString(9));
        }
        @SuppressLint("WrongConstant") InputMethodManager imm = (InputMethodManager) getSystemService("input_method");
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        this.btn_suatin.setOnClickListener(v -> {
            db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + ngay_nhan + "' AND ten_kh = '" + tenKH + "'  AND so_tin_nhan = " + soTN + " And type_kh = " + typeKH);
            String sb = "Update tbl_tinnhanS Set nd_phantich = '" + editText_suatin.getText().toString() + "', phat_hien_loi = 'ko' WHERE id = " + id;
            db.QueryData(sb);
            try {
                db.Update_TinNhanGoc(Integer.parseInt(id), typeKH);
            } catch (Exception e) {
                Toast.makeText(Activity_Tinnhan.this, "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            Database database2 = db;
            Cursor cur = database2.GetData("Select * FROM tbl_tinnhanS Where id = " + id);
            cur.moveToFirst();
            if (cur.getString(11).contains("Không hiểu")) {
                editText_suatin.setText(Html.fromHtml(cur.getString(10).replace("ldpro", "<font color='#FF0000'>")));
                if (cur.getString(10).contains("ldpro")) {
                    editText_suatin.setSelection(cur.getString(10).indexOf("ldpro"));
                }
                mDanGoc.clear();
                mPhantich.clear();
                ListView listView = lv_suatin;
                Activity_Tinnhan activity_Tinnhan = Activity_Tinnhan.this;
                listView.setAdapter((ListAdapter) new TN_Adapter(activity_Tinnhan, R.layout.frag_suatin_lv1, activity_Tinnhan.mDanGoc));
                return;
            }
            editText_suatin.setText(cur.getString(9));
            mDanGoc.clear();
            mPhantich.clear();
            try {
                json = new JSONObject(cur.getString(15));
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    JSONObject dan = json.getJSONObject(keys.next());
                    List<String> list = mDanGoc;
                    list.add(dan.getString("du_lieu") + " (" + dan.getString("so_luong") + ")");
                    List<String> list2 = mPhantich;
                    list2.add(dan.getString("dan_so") + "x" + dan.getString("so_tien"));
                }
                lv_suatin.setAdapter((ListAdapter) new TN_Adapter(Activity_Tinnhan.this, R.layout.frag_suatin_lv1, mDanGoc));
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        });
        this.lv_suatin.setOnItemLongClickListener((adapterView, view1, position, id) -> {
            lv_position = position;
            return false;
        });
        this.btn_xoatin.setOnClickListener(v -> finish());
        registerForContextMenu(this.lv_suatin);
        cursor.close();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Copy ?");
    }

    @SuppressLint("WrongConstant")
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        if (item.getItemId() == 1) {
            ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Tin chốt:", this.mDanGoc.get(this.lv_position) + "\n" + this.mPhantich.get(this.lv_position)));
            Toast.makeText(this, "Đã copy thành công", 1).show();
        }
        return true;
    }

    class TN_Adapter extends ArrayAdapter {
        public TN_Adapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        public View getView(int position, View v, ViewGroup parent) {
            @SuppressLint("ViewHolder") View v2 = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.frag_suatin_lv1, (ViewGroup) null);
            TextView tv_dangoc = (TextView) v2.findViewById(R.id.dan_goc);
            final TextView tv_danpt = (TextView) v2.findViewById(R.id.dan_phantich);
            tv_dangoc.setText((CharSequence) mDanGoc.get(position));
            tv_dangoc.setOnClickListener(v1 -> {
                if (tv_danpt.getVisibility() == View.VISIBLE) {
                    tv_danpt.setVisibility(View.GONE);
                } else {
                    tv_danpt.setVisibility(View.VISIBLE);
                }
            });
            tv_danpt.setText((CharSequence) mPhantich.get(position));
            tv_danpt.setVisibility(View.GONE);
            return v2;
        }
    }
}