package tamhoang.ldpro4.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tamhoang.ldpro4.Activity.Activity_AddKH;
import tamhoang.ldpro4.Activity.Activity_AddKH2;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Frag_Setting1 extends Fragment {
    Button btn_themKH;
    Database db;
    ListView lview;
    public List<String> mAddress = new ArrayList();
    public List<String> mAppuse = new ArrayList();
    public List<String> mDate = new ArrayList();
    public List<String> mPerson = new ArrayList();
    int mPoistion = 0;
    public List<Integer> mtype = new ArrayList();
    TextView tv_Sodt;
    TextView tv_tenKH;
    ImageButton tv_xoaKH;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.frag_setting1, container, false);
        this.db = new Database(getActivity());
        this.lview = (ListView) this.v.findViewById(R.id.lv_setting1);
        Button button = (Button) this.v.findViewById(R.id.btn_them_KH);
        FloatingActionButton fab_add = (FloatingActionButton) this.v.findViewById(R.id.fab_add);
        this.btn_themKH = button;
        fab_add.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Activity_AddKH.class);
            intent.putExtra("tenKH", "");
            intent.putExtra("use_app", "sms");
            intent.putExtra("kh_new", "");
            startActivity(intent);
        });
        this.lview.setOnItemClickListener((adapterView, view, position, id) -> {
            mPoistion = position;
            lview.showContextMenuForChild(view);
        });
        this.lview.setOnItemLongClickListener((adapterView, view, position, l) -> {
            mPoistion = position;
            return false;
        });
        xem_lv();
        registerForContextMenu(this.lview);
        return this.v;
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        String str2 = null;
        String name = null;
        if (i2 == -1) {
            if (i == 1) {
                try {
                    Uri data = intent.getData();
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Cursor query = contentResolver.query(data, null, null, null, null);
                    if (query.getCount() > 0) {
                        while (query.moveToNext()) {
                            String string = query.getString(query.getColumnIndex("_id"));
                            name = query.getString(query.getColumnIndex("display_name"));
                            query.getString(query.getColumnIndex("display_name"));
                            if (Integer.parseInt(query.getString(query.getColumnIndex("has_phone_number"))) > 0) {
                                if (Integer.parseInt(query.getString(query.getColumnIndex("has_phone_number"))) > 0) {
                                    Cursor query2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id = ?", new String[]{string}, null);
                                    String str = null;
                                    String str22 = null;
                                    while (query2.moveToNext()) {
                                        try {
                                            try {
                                                if (query2.getInt(query2.getColumnIndex("data2")) == 2) {
                                                    str = query2.getString(query2.getColumnIndex("data1"));
                                                }
                                            } catch (Exception e) {
                                                str22 = str;
                                            } catch (Throwable th) {
                                                str22 = str;
                                            }
                                        } catch (Exception e2) {
                                        }
                                    }
                                    if (query2 != null) {
                                        query2.close();
                                    }
                                    str2 = str;
                                }
                            }
                            str2 = null;
                        }
                        if (str2.length() > 0) {
                            String str3 = str2.replaceAll(" ", "");
                            try {
                                if (str3.length() < 12) {
                                    str3 = "+84" + str3.substring(1);
                                }
                                this.tv_Sodt.setText(str3);
                                this.tv_tenKH.setText(name);
                            } catch (Exception e3) {
                            }
                        }
                    }
                } catch (Exception e4) {
                }
            }
            getActivity().setResult(-1, intent);
        }
    }

    @Override
    public void onResume() {
        xem_lv();
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v2, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v2, menuInfo);
        menu.add(0, 1, 0, "Cài đặt lại giá");
        menu.add(0, 2, 0, "Cài đặt thời gian, giữ %");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        if (item.getItemId() == 1) {
            Intent intent = new Intent(getActivity(), Activity_AddKH.class);
            intent.putExtra("tenKH", this.mPerson.get(this.mPoistion));
            intent.putExtra("kh_new", "");
            intent.putExtra("use_app", this.mAppuse.get(this.mPoistion));
            startActivity(intent);
        } else if (item.getItemId() == 2) {
            Intent intent2 = new Intent(getActivity(), Activity_AddKH2.class);
            intent2.putExtra("tenKH", this.mPerson.get(this.mPoistion));
            startActivity(intent2);
        }
        return true;
    }

    public void xem_lv() {
        this.mAddress.clear();
        this.mPerson.clear();
        this.mtype.clear();
        this.mAppuse.clear();
        Cursor cursor = this.db.GetData("select * from tbl_kh_new Order by type_kh DESC, ten_kh");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                this.mPerson.add(cursor.getString(0));
                this.mAddress.add(cursor.getString(1));
                this.mtype.add(cursor.getInt(3));
                this.mAppuse.add(cursor.getString(2));
            }
            if (!cursor.isClosed()) cursor.close();
        }
        if (getActivity() != null) {
            this.lview.setAdapter((ListAdapter) new KHAdapter(getActivity(), R.layout.frag_setting1_lv, this.mPerson));
        }
    }

    public class KHAdapter extends ArrayAdapter {
        public KHAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder")
            View v = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.frag_setting1_lv, (ViewGroup) null);
            TextView tview1 = (TextView) v.findViewById(R.id.st1_tenkh);
            tview1.setText(mPerson.get(position));
            ((TextView) v.findViewById(R.id.st1_sdt)).setText(mAddress.get(position));

            v.setOnClickListener(view -> {
                mPoistion = position;
                lview.showContextMenuForChild(view);
            });

            v.setOnLongClickListener(view -> {
                mPoistion = position;
                return false;
            });

            tv_xoaKH = (ImageButton) v.findViewById(R.id.tv_xoaKH);
            tv_xoaKH.setOnClickListener(v1 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                builder.setTitle("Xoá Khách");
                builder.setMessage("Xoá bỏ " + mPerson.get(position) + " ra khỏi danh sách?");
                builder.setNegativeButton("Có", (dialog, which) -> {
                    db.QueryData("Delete FROM tbl_kh_new where ten_kh = '" + mPerson.get(position) + "'");
                    db.QueryData("Delete FROM tbl_tinnhanS where ten_kh = '" + mPerson.get(position) + "'");
                    db.QueryData("Delete FROM tbl_soctS where ten_kh = '" + mPerson.get(position) + "'");
                    db.QueryData("Delete FROM tbl_chuyenthang where kh_nhan = '" + mPerson.get(position) + "'");
                    db.QueryData("Delete FROM tbl_chuyenthang where kh_chuyen = '" + mPerson.get(position) + "'");
                    db.LayDanhsachKH();
                    xem_lv();
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Xoá thành công!", Toast.LENGTH_LONG).show();
                });
                builder.setPositiveButton("Không", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
            if (mtype.get(position) != 1) {
                tview1.setTextColor(-16776961);
            }
            return v;
        }
    }
}