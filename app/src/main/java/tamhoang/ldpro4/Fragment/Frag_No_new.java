package tamhoang.ldpro4.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tamhoang.ldpro4.Activity.Activity_khach;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.NotificationReader;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Frag_No_new extends Fragment {
    boolean Running = true;
    TextView bc_Chuyen;
    TextView bc_ChuyenAn;
    TextView bc_Nhan;
    TextView bc_NhanAn;
    TextView bca_Chuyen;
    TextView bca_ChuyenAn;
    TextView bca_Nhan;
    TextView bca_NhanAn;
    JSONObject caidat_tg;

    /* renamed from: db */
    Database f202db;
    TextView dea_Chuyen;
    TextView dea_ChuyenAn;
    TextView dea_Nhan;
    TextView dea_NhanAn;
    TextView deb_Chuyen;
    TextView deb_ChuyenAn;
    TextView deb_Nhan;
    TextView deb_NhanAn;
    TextView dec_Chuyen;
    TextView dec_ChuyenAn;
    TextView dec_Nhan;
    TextView dec_NhanAn;
    TextView ded_Chuyen;
    TextView ded_ChuyenAn;
    TextView ded_Nhan;
    TextView ded_NhanAn;
    TextView det_Chuyen;
    TextView det_ChuyenAn;
    TextView det_Nhan;
    TextView det_NhanAn;
    Handler handler;
    JSONObject json;
    List<JSONObject> jsonKhachHang;
    LinearLayout li_bca;
    LinearLayout li_dea;
    LinearLayout li_dec;
    LinearLayout li_ded;
    LinearLayout li_det;
    LinearLayout li_loa;
    LinearLayout li_xia2;
    LinearLayout li_xn;
    TextView lo_Chuyen;
    TextView lo_ChuyenAn;
    TextView lo_Nhan;
    TextView lo_NhanAn;
    TextView loa_Chuyen;
    TextView loa_ChuyenAn;
    TextView loa_Nhan;
    TextView loa_NhanAn;
    ListView lv_baocaoKhach;
    LayoutInflater mInflate;
    /* access modifiers changed from: private */
    public List<String> mSDT = new ArrayList();
    /* access modifiers changed from: private */
    public List<String> mTenKH = new ArrayList();
    int position;
    private Runnable runnable = new Runnable() {
        public void run() {
            new MainActivity();
            if (MainActivity.sms) {
                try {
                    Frag_No_new.this.lv_baoCao();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MainActivity.sms = false;
            }
            Frag_No_new.this.handler.postDelayed(this, 1000);
        }
    };
    TextView tv_TongGiu;
    TextView tv_TongTienChuyen;
    TextView tv_TongTienNhan;

    /* renamed from: v */
    View f203v;
    TextView xi2_Chuyen;
    TextView xi2_ChuyenAn;
    TextView xi2_Nhan;
    TextView xi2_NhanAn;
    TextView xia2_Chuyen;
    TextView xia2_ChuyenAn;
    TextView xia2_Nhan;
    TextView xia2_NhanAn;
    TextView xn_Chuyen;
    TextView xn_ChuyenAn;
    TextView xn_Nhan;
    TextView xn_NhanAn;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.f203v = layoutInflater.inflate(R.layout.frag_norp1, viewGroup, false);
        this.f202db = new Database(getActivity());
        this.mInflate = layoutInflater;
        init();
        this.lv_baocaoKhach.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                Frag_No_new frag_No_new = Frag_No_new.this;
                frag_No_new.position = i;
                frag_No_new.Dialog(frag_No_new.mTenKH.get(i));
                return false;
            }
        });
        if (!Congthuc.CheckTime("18:30")) {
            this.handler = new Handler();
            this.handler.postDelayed(this.runnable, 1000);
        }
        try {
            lv_baoCao();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.f203v;
    }

    public void onStop() {
        this.Running = false;
        super.onStop();
        try {
            this.handler.removeCallbacks(this.runnable);
        } catch (Exception unused) {
        }
    }

    public void Dialog(String str) {
        SeekBar seekBar = null;
        SeekBar seekBar2 = null;
        TextView textView = null;
        TextView textView2 = null;
        SeekBar seekBar3 = null;
        SeekBar seekBar4 = null;
        SeekBar seekBar5 = null;
        TextView textView3 = null;
        TextView textView4 = null;
        TextView textView5 = null;
        SeekBar seekBar6 = null;
        TextView textView6 = null;
        SeekBar seekBar7 = null;
        TextView textView7 = null;
        TextView textView8 = null;
        TextView textView9 = null;
        SeekBar seekBar8 = null;
        SeekBar seekBar9 = null;
        StringBuilder sb = null;
        String str2 = null;
        SeekBar seekBar10 = null;
        SeekBar seekBar11 = null;
        SeekBar seekBar12 = null;
        SeekBar seekBar13 = null;
        SeekBar seekBar14 = null;
        TextView textView10 = null;
        StringBuilder sb2 = null;
        String str3 = null;
        StringBuilder sb3 = null;
        String str4 = null;
        String str5 = null;
        int i = 0;
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.frag_no_menu);
        new MainActivity();
        String Get_date = MainActivity.Get_date();
        Database database = this.f202db;
        StringBuilder sb4 = new StringBuilder();
        @SuppressLint("WrongConstant") ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService("clipboard");
        sb4.append("Select * From tbl_kh_new WHERE ten_kh ='");
        sb4.append(str);
        sb4.append("'");
        Cursor GetData = database.GetData(sb4.toString());
        GetData.moveToFirst();
        Button button = (Button) dialog.findViewById(R.id.tinhlaitien);
        Button button2 = (Button) dialog.findViewById(R.id.nhanchottien);
        Button button3 = (Button) dialog.findViewById(R.id.copytinchitiet);
        Button button4 = (Button) dialog.findViewById(R.id.copytinchotien);
        Button button5 = (Button) dialog.findViewById(R.id.xoadulieu);
        String str6 = Get_date;
        Button button6 = (Button) dialog.findViewById(R.id.Baocaochitiet);
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.ln2);
        Switch switchR = (Switch) dialog.findViewById(R.id.switch1);
        SeekBar seekBar15 = (SeekBar) dialog.findViewById(R.id.seek_Giu3ckhach);
        SeekBar seekBar16 = (SeekBar) dialog.findViewById(R.id.seek_GiuDedly);
        SeekBar seekBar17 = (SeekBar) dialog.findViewById(R.id.seek_GiuXikhach);
        SeekBar seekBar18 = (SeekBar) dialog.findViewById(R.id.seek_GiuLodly);
        SeekBar seekBar19 = (SeekBar) dialog.findViewById(R.id.seek_GiuLokhach);
        SeekBar seekBar20 = (SeekBar) dialog.findViewById(R.id.seek_GiuXidly);
        SeekBar seekBar21 = (SeekBar) dialog.findViewById(R.id.seek_GiuDekhach);
        SeekBar seekBar22 = (SeekBar) dialog.findViewById(R.id.seek_Giu3cdly);
        String str7 = "khgiu_bc";
        String str8 = "khgiu_xi";
        String str9 = "khgiu_lo";
        TextView textView11 = (TextView) dialog.findViewById(R.id.pt_giu_lo_khach);
        TextView textView12 = (TextView) dialog.findViewById(R.id.pt_giu_xi_khach);
        TextView textView13 = (TextView) dialog.findViewById(R.id.pt_giu_bc_khach);
        final TextView textView14 = (TextView) dialog.findViewById(R.id.pt_giu_de_dly);
        TextView textView15 = (TextView) dialog.findViewById(R.id.pt_giu_de_khach);
        TextView textView16 = (TextView) dialog.findViewById(R.id.pt_giu_lo_dly);
        String str10 = "khgiu_de";
        TextView textView17 = (TextView) dialog.findViewById(R.id.pt_giu_xi_dly);
        Dialog dialog2 = dialog;
        TextView textView18 = (TextView) dialog.findViewById(R.id.pt_giu_bc_dly);
        try {
            seekBar2 = seekBar22;
            seekBar7 = seekBar20;
            this.json = new JSONObject(GetData.getString(5));
            this.caidat_tg = this.json.getJSONObject("caidat_tg");
            textView14.setText(this.caidat_tg.getInt("dlgiu_de") + "%");
            textView16.setText(this.caidat_tg.getInt("dlgiu_lo") + "%");
            textView17.setText(this.caidat_tg.getInt("dlgiu_xi") + "%");
            textView18.setText(this.caidat_tg.getInt("dlgiu_bc") + "%");
            seekBar16.setProgress(this.caidat_tg.getInt("dlgiu_de") / 5);
            seekBar18.setProgress(this.caidat_tg.getInt("dlgiu_lo") / 5);
            seekBar8 = seekBar7;
            seekBar8.setProgress(this.caidat_tg.getInt("dlgiu_xi") / 5);
            seekBar9 = seekBar2;
            seekBar9.setProgress(this.caidat_tg.getInt("dlgiu_bc") / 5);
            sb = new StringBuilder();
            str2 = str10;
            sb.append(this.caidat_tg.getInt(str2));
            sb.append("%");
            textView4 = textView15;
            textView4.setText(sb.toString());
            sb2 = new StringBuilder();
            str3 = str9;
            sb2.append(this.caidat_tg.getInt(str3));
            sb2.append("%");
            textView3 = textView11;
            textView3.setText(sb2.toString());
            sb3 = new StringBuilder();
            seekBar2 = seekBar9;
            textView = textView18;
            str4 = str8;
            sb3.append(this.caidat_tg.getInt(str4));
            sb3.append("%");
            textView5 = textView12;
            textView5.setText(sb3.toString());
            StringBuilder sb5 = new StringBuilder();
            seekBar = seekBar8;
            textView2 = textView17;
            str5 = str7;
            sb5.append(this.caidat_tg.getInt(str5));
            sb5.append("%");
            textView6 = textView13;
            textView6.setText(sb5.toString());
            seekBar5 = seekBar21;
            seekBar5.setProgress(this.caidat_tg.getInt(str2) / 5);
            int i2 = this.caidat_tg.getInt(str3) / 5;
            seekBar3 = seekBar19;
            seekBar3.setProgress(i2);
            i = this.caidat_tg.getInt(str4) / 5;
            seekBar4 = seekBar17;
            seekBar4.setProgress(i);
            int i3 = this.caidat_tg.getInt(str5) / 5;
            seekBar6 = seekBar15;
            seekBar6.setProgress(i3);
            GetData.close();
        } catch (Exception unused2) {
        }

        final LinearLayout linearLayout22222222222222222222222 = linearLayout;
        final Switch switchR22222222222222222222222 = switchR;
        switchR22222222222222222222222.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (switchR22222222222222222222222.isChecked()) {
                    linearLayout22222222222222222222222.setVisibility(0);
                } else {
                    linearLayout22222222222222222222222.setVisibility(8);
                }
            }
        });
        final String str112222222222222222222222 = str;
        final Dialog dialog32222222222222222222222 = dialog2;
        SeekBar seekBar232222222222222222222222 = seekBar18;
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Frag_No_new.this.getActivity(), Activity_khach.class);
                intent.putExtra("tenKH", str112222222222222222222222);
                Frag_No_new.this.startActivity(intent);
                dialog32222222222222222222222.cancel();
            }
        });
        final String str122222222222222222222222 = str6;
        TextView textView192222222222222222222222 = textView16;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Frag_No_new.this.TinhlaitienKhachnay(str122222222222222222222222, str112222222222222222222222);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                dialog32222222222222222222222.cancel();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Database database = Frag_No_new.this.f202db;
                Cursor GetData = database.GetData("Select * From tbl_kh_new Where ten_kh = '" + ((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position)) + "'");
                GetData.moveToFirst();
                if (GetData.getString(2).indexOf("sms") > -1) {
                    try {
                        if (MainActivity.jSon_Setting.getInt("tachxien_tinchot") == 0) {
                            Frag_No_new.this.f202db.SendSMS((String) Frag_No_new.this.mSDT.get(Frag_No_new.this.position), Frag_No_new.this.f202db.Tin_Chottien((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position)));
                        } else {
                            Frag_No_new.this.f202db.SendSMS((String) Frag_No_new.this.mSDT.get(Frag_No_new.this.position), Frag_No_new.this.f202db.Tin_Chottien_xien((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position)));
                        }
                        Toast.makeText(Frag_No_new.this.getActivity(), "Đã nhắn chốt tiền!", 1).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (GetData.getString(2).indexOf("TL") > -1) {
                    try {
                        if (MainActivity.jSon_Setting.getInt("tachxien_tinchot") == 0) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    new MainActivity();
                                    try {
                                        MainActivity.sendMessage(Long.parseLong((String) Frag_No_new.this.mSDT.get(Frag_No_new.this.position)), Frag_No_new.this.f202db.Tin_Chottien((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position)));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    new MainActivity();
                                    try {
                                        MainActivity.sendMessage(Long.parseLong((String) Frag_No_new.this.mSDT.get(Frag_No_new.this.position)), Frag_No_new.this.f202db.Tin_Chottien_xien((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position)));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        Toast.makeText(Frag_No_new.this.getActivity(), "Đã nhắn chốt tiền!", 1).show();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                } else if (MainActivity.contactsMap.containsKey(GetData.getString(1))) {
                    NotificationReader notificationReader = new NotificationReader();
                    try {
                        if (MainActivity.jSon_Setting.getInt("tachxien_tinchot") == 0) {
                            notificationReader.NotificationWearReader(GetData.getString(1), Frag_No_new.this.f202db.Tin_Chottien((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position)));
                        } else {
                            notificationReader.NotificationWearReader(GetData.getString(1), Frag_No_new.this.f202db.Tin_Chottien_xien((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position)));
                        }
                    } catch (JSONException e3) {
                        e3.printStackTrace();
                    }
                } else {
                    Toast.makeText(Frag_No_new.this.getActivity(), "Không có người này trong Chatbox", 1).show();
                }
                dialog32222222222222222222222.cancel();
            }
        });
        final ClipboardManager clipboardManager22222222222222222222222 = clipboardManager;
        SeekBar seekBar242222222222222222222222 = seekBar16;
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clipboardManager22222222222222222222222.setPrimaryClip(ClipData.newPlainText("Tin chốt:", Frag_No_new.this.f202db.Tin_Chottien_CT((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position))));
                Toast.makeText(Frag_No_new.this.getActivity(), "Đã copy vào bộ nhớ tạm!", 1).show();
                dialog32222222222222222222222.cancel();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ClipData clipData;
                Database database = Frag_No_new.this.f202db;
                database.GetData("Select * From tbl_kh_new Where ten_kh = '" + ((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position)) + "'").moveToFirst();
                try {
                    if (MainActivity.jSon_Setting.getInt("tachxien_tinchot") == 0) {
                        clipData = ClipData.newPlainText("Tin chốt:", Frag_No_new.this.f202db.Tin_Chottien((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position)));
                    } else {
                        clipData = ClipData.newPlainText("Tin chốt:", Frag_No_new.this.f202db.Tin_Chottien_xien((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position)));
                    }
                    clipboardManager22222222222222222222222.setPrimaryClip(clipData);
                    Toast.makeText(Frag_No_new.this.getActivity(), "Đã copy vào bộ nhớ tạm!", 1).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog32222222222222222222222.cancel();
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Frag_No_new.this.getActivity());
                builder.setTitle((CharSequence) "Xoá dữ liệu của KH này?");
                builder.setPositiveButton((CharSequence) "YES", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Frag_No_new.this.f202db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + str122222222222222222222222 + "' AND ten_kh = '" + ((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position)) + "'");
                        Frag_No_new.this.f202db.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + str122222222222222222222222 + "' AND ten_kh = '" + ((String) Frag_No_new.this.mTenKH.get(Frag_No_new.this.position)) + "'");
                        try {
                            Frag_No_new.this.lv_baoCao();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog32222222222222222222222.cancel();
                        Toast.makeText(Frag_No_new.this.getActivity(), "Đã xoá", 1).show();
                    }
                });
                builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });
        TextView finalTextView3 = textView4;
        seekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView textView = finalTextView3;
                StringBuilder sb = new StringBuilder();
                int i2 = i * 5;
                sb.append(i2);
                sb.append("%");
                textView.setText(sb.toString());
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Frag_No_new.this.caidat_tg.put("khgiu_de", this.max);
                    Database database = Frag_No_new.this.f202db;
                    database.QueryData("update tbl_kh_new set tbl_MB = '" + Frag_No_new.this.json.toString() + "' WHERE ten_kh = '" + str112222222222222222222222 + "'");
                    Frag_No_new.this.TinhlaitienKhachnay(str122222222222222222222222, str112222222222222222222222);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
        TextView finalTextView = textView3;
        assert seekBar3 != null;
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView textView = finalTextView;
                StringBuilder sb = new StringBuilder();
                int i2 = i * 5;
                sb.append(i2);
                sb.append("%");
                finalTextView.setText(sb.toString());
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Frag_No_new.this.caidat_tg.put("khgiu_lo", this.max);
                    Database database = Frag_No_new.this.f202db;
                    database.QueryData("update tbl_kh_new set tbl_MB = '" + Frag_No_new.this.json.toString() + "' WHERE ten_kh = '" + str112222222222222222222222 + "'");
                    Frag_No_new.this.TinhlaitienKhachnay(str122222222222222222222222, str112222222222222222222222);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        assert seekBar4 != null;
        TextView finalTextView1 = textView5;
        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView textView = finalTextView1;
                StringBuilder sb = new StringBuilder();
                int i2 = i * 5;
                sb.append(i2);
                sb.append("%");
                textView.setText(sb.toString());
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Frag_No_new.this.caidat_tg.put("khgiu_xi", this.max);
                    Database database = Frag_No_new.this.f202db;
                    database.QueryData("update tbl_kh_new set tbl_MB = '" + Frag_No_new.this.json.toString() + "' WHERE ten_kh = '" + str112222222222222222222222 + "'");
                    Frag_No_new.this.TinhlaitienKhachnay(str122222222222222222222222, str112222222222222222222222);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        TextView finalTextView2 = textView6;
        seekBar6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView textView = finalTextView2;
                StringBuilder sb = new StringBuilder();
                int i2 = i * 5;
                sb.append(i2);
                sb.append("%");
                textView.setText(sb.toString());
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Frag_No_new.this.caidat_tg.put("khgiu_bc", this.max);
                    Database database = Frag_No_new.this.f202db;
                    database.QueryData("update tbl_kh_new set tbl_MB = '" + Frag_No_new.this.json.toString() + "' WHERE ten_kh = '" + str112222222222222222222222 + "'");
                    Frag_No_new.this.TinhlaitienKhachnay(str122222222222222222222222, str112222222222222222222222);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        seekBar242222222222222222222222.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView textView = textView14;
                StringBuilder sb = new StringBuilder();
                int i2 = i * 5;
                sb.append(i2);
                sb.append("%");
                textView.setText(sb.toString());
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Frag_No_new.this.caidat_tg.put("dlgiu_de", this.max);
                    Database database = Frag_No_new.this.f202db;
                    database.QueryData("update tbl_kh_new set tbl_MB = '" + Frag_No_new.this.json.toString() + "' WHERE ten_kh = '" + str112222222222222222222222 + "'");
                    Frag_No_new.this.TinhlaitienKhachnay(str122222222222222222222222, str112222222222222222222222);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        final TextView textView202222222222222222222222 = textView192222222222222222222222;
        seekBar232222222222222222222222.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView textView = textView202222222222222222222222;
                StringBuilder sb = new StringBuilder();
                int i2 = i * 5;
                sb.append(i2);
                sb.append("%");
                textView.setText(sb.toString());
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Frag_No_new.this.caidat_tg.put("dlgiu_lo", this.max);
                    Database database = Frag_No_new.this.f202db;
                    database.QueryData("update tbl_kh_new set tbl_MB = '" + Frag_No_new.this.json.toString() + "' WHERE ten_kh = '" + str112222222222222222222222 + "'");
                    Frag_No_new.this.TinhlaitienKhachnay(str122222222222222222222222, str112222222222222222222222);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        final TextView textView212222222222222222222222 = textView2;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView textView = textView212222222222222222222222;
                StringBuilder sb = new StringBuilder();
                int i2 = i * 5;
                sb.append(i2);
                sb.append("%");
                textView.setText(sb.toString());
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Frag_No_new.this.caidat_tg.put("dlgiu_xi", this.max);
                    Database database = Frag_No_new.this.f202db;
                    database.QueryData("update tbl_kh_new set tbl_MB = '" + Frag_No_new.this.json.toString() + "' WHERE ten_kh = '" + str112222222222222222222222 + "'");
                    Frag_No_new.this.TinhlaitienKhachnay(str122222222222222222222222, str112222222222222222222222);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        final TextView textView222222222222222222222222 = textView;
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView textView = textView222222222222222222222222;
                StringBuilder sb = new StringBuilder();
                int i2 = i * 5;
                sb.append(i2);
                sb.append("%");
                textView.setText(sb.toString());
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Frag_No_new.this.caidat_tg.put("dlgiu_bc", this.max);
                    Database database = Frag_No_new.this.f202db;
                    database.QueryData("update tbl_kh_new set tbl_MB = '" + Frag_No_new.this.json.toString() + "' WHERE ten_kh = '" + str112222222222222222222222 + "'");
                    Frag_No_new.this.TinhlaitienKhachnay(str122222222222222222222222, str112222222222222222222222);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        dialog32222222222222222222222.getWindow().

                setLayout(-1, -2);
        dialog32222222222222222222222.setCancelable(true);
        dialog32222222222222222222222.setTitle("Xem dạng:");
        dialog32222222222222222222222.show();
    }

    /* access modifiers changed from: private */
    public void TinhlaitienKhachnay(String str, String str2) throws Throwable {
        Database database = this.f202db;
        database.QueryData("Delete From tbl_soctS WHERE  ngay_nhan = '" + str + "' AND ten_kh = '" + this.mTenKH.get(this.position) + "'");
        Database database2 = this.f202db;
        Cursor GetData = database2.GetData("Select * FROM tbl_tinnhanS WHERE  ngay_nhan = '" + str + "' AND phat_hien_loi = 'ok' AND ten_kh = '" + this.mTenKH.get(this.position) + "'");
        while (GetData.moveToNext()) {
            String replaceAll = GetData.getString(10).replaceAll("\\*", "");
            Database database3 = this.f202db;
            database3.QueryData("Update tbl_tinnhanS set nd_phantich = '" + replaceAll + "' WHERE id = " + GetData.getInt(0));
            this.f202db.NhapSoChiTiet(GetData.getInt(0));
        }
        Tinhtien();
        lv_baoCao();
        if (!GetData.isClosed()) {
            GetData.close();
        }
    }

    private void Tinhtien() throws JSONException {
        new MainActivity();
        String Get_date = MainActivity.Get_date();
        Database database = this.f202db;
        Cursor GetData = database.GetData("Select * From Ketqua WHERE ngay = '" + Get_date + "'");
        GetData.moveToFirst();
        int i = 2;
        while (true) {
            if (i >= 29) {
                break;
            }
            try {
                if (GetData.isNull(i)) {
                    break;
                } else if (!Congthuc.isNumeric(GetData.getString(i))) {
                    break;
                } else {
                    i++;
                }
            } catch (Exception unused) {
            }
        }
        if (i >= 29) {
            this.f202db.Tinhtien(Get_date);
        }
        if (GetData != null && !GetData.isClosed()) {
            GetData.close();
        }
    }

    public void onDestroy() {
        try {
            this.mTenKH.clear();
            this.mSDT.clear();
            this.lv_baocaoKhach.setAdapter((ListAdapter) null);
        } catch (Exception unused) {
        }
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void lv_baoCao() throws JSONException {
        Cursor cursor = null;
        String str = null;
        String str2 = null;
        String str3 = null;
        DecimalFormat decimalFormat = null;
        Frag_No_new frag_No_new = null;
        String str4 = "dec";
        String str5 = "det";
        String str6 = "deb";
        new MainActivity();
        String Get_date = MainActivity.Get_date();
        DecimalFormat decimalFormat2 = new DecimalFormat("###,###");
        Cursor GetData = this.f202db.GetData("Select the_loai\n, sum((type_kh = 1)*(100-diem_khachgiu)*diem/100) as mDiem\n, CASE WHEN the_loai = 'xi' OR the_loai = 'xia' \n THEN sum((type_kh = 1)*(100-diem_khachgiu)*diem/100*so_nhay*lan_an/1000) \n ELSE sum((type_kh = 1)*(100-diem_khachgiu)*diem/100*so_nhay)  END nAn\n, sum((type_kh = 1)*ket_qua*(100-diem_khachgiu)/100/1000) as mKetqua\n, sum((type_kh = 2)*(100-diem_khachgiu)*diem/100) as mDiem\n, CASE WHEN the_loai = 'xi' OR the_loai = 'xia' \n THEN sum((type_kh = 2)*(100-diem_khachgiu)*diem/100*so_nhay*lan_an/1000) \n ELSE sum((type_kh = 2)*(100-diem_khachgiu)*diem/100*so_nhay)  END nAn\n, sum((type_kh = 2)*ket_qua*(100-diem_khachgiu)/100/1000) as mKetqua\n  From tbl_soctS Where ngay_nhan = '" + Get_date + "'\n  AND the_loai <> 'tt' GROUP by the_loai");
        if (GetData != null) {
            JSONObject jSONObject = new JSONObject();
            double d = 0.0d;
            double d2 = 0.0d;
            while (true) {
                str = str4;
                str2 = str5;
                str3 = str6;
                if (!GetData.moveToNext()) {
                    break;
                }
                try {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("DiemNhan", decimalFormat2.format(GetData.getDouble(1)));
                    jSONObject2.put("AnNhan", decimalFormat2.format(GetData.getDouble(2)));
                    jSONObject2.put("KQNhan", decimalFormat2.format(GetData.getDouble(3)));
                    jSONObject2.put("DiemChuyen", decimalFormat2.format(GetData.getDouble(4)));
                    jSONObject2.put("AnChuyen", decimalFormat2.format(GetData.getDouble(5)));
                    jSONObject2.put("KQChuyen", decimalFormat2.format(GetData.getDouble(6)));
                    d += GetData.getDouble(3);
                    d2 += GetData.getDouble(6);
                    jSONObject.put(GetData.getString(0), jSONObject2.toString());
                    str4 = str;
                    str5 = str2;
                    str6 = str3;
                } catch (JSONException unused) {
                }
            }
            double d3 = d2;
            if (jSONObject.length() > 0) {
                if (jSONObject.has("dea")) {
                    frag_No_new = this;
                    cursor = GetData;
                    try {
                        frag_No_new.li_dea.setVisibility(0);
                        JSONObject jSONObject3 = new JSONObject(jSONObject.getString("dea"));
                        if (jSONObject3.getString("DiemNhan").length() > 0) {
                            TextView textView = frag_No_new.dea_Nhan;
                            StringBuilder sb = new StringBuilder();
                            decimalFormat = decimalFormat2;
                            sb.append(jSONObject3.getString("DiemNhan"));
                            sb.append("(");
                            sb.append(jSONObject3.getString("AnNhan"));
                            sb.append(")");
                            textView.setText(sb.toString());
                            frag_No_new.dea_NhanAn.setText(jSONObject3.getString("KQNhan"));
                        } else {
                            decimalFormat = decimalFormat2;
                        }
                        if (jSONObject3.getString("DiemChuyen").length() > 0) {
                            frag_No_new.dea_Chuyen.setText(jSONObject3.getString("DiemChuyen") + "(" + jSONObject3.getString("AnChuyen") + ")");
                            frag_No_new.dea_ChuyenAn.setText(jSONObject3.getString("KQChuyen"));
                        }
                    } catch (JSONException unused3) {
                    }
                } else {
                    frag_No_new = this;
                    cursor = GetData;
                    decimalFormat = decimalFormat2;
                }
                String str7 = str3;
                if (jSONObject.has(str7)) {
                    JSONObject jSONObject4 = new JSONObject(jSONObject.getString(str7));
                    if (jSONObject4.getString("DiemNhan").length() > 0) {
                        frag_No_new.deb_Nhan.setText(jSONObject4.getString("DiemNhan") + "(" + jSONObject4.getString("AnNhan") + ")");
                        frag_No_new.deb_NhanAn.setText(jSONObject4.getString("KQNhan"));
                    }
                    if (jSONObject4.getString("DiemChuyen").length() > 0) {
                        frag_No_new.deb_Chuyen.setText(jSONObject4.getString("DiemChuyen") + "(" + jSONObject4.getString("AnChuyen") + ")");
                        frag_No_new.deb_ChuyenAn.setText(jSONObject4.getString("KQChuyen"));
                    }
                }
                String str8 = str2;
                if (jSONObject.has(str8)) {
                    frag_No_new.li_det.setVisibility(0);
                    JSONObject jSONObject5 = new JSONObject(jSONObject.getString(str8));
                    if (jSONObject5.getString("DiemNhan").length() > 0) {
                        frag_No_new.det_Nhan.setText(jSONObject5.getString("DiemNhan") + "(" + jSONObject5.getString("AnNhan") + ")");
                        frag_No_new.det_NhanAn.setText(jSONObject5.getString("KQNhan"));
                    }
                    if (jSONObject5.getString("DiemChuyen").length() > 0) {
                        frag_No_new.det_Chuyen.setText(jSONObject5.getString("DiemChuyen") + "(" + jSONObject5.getString("AnChuyen") + ")");
                        frag_No_new.det_ChuyenAn.setText(jSONObject5.getString("KQChuyen"));
                    }
                }
                String str9 = str;
                if (jSONObject.has(str9)) {
                    frag_No_new.li_dec.setVisibility(0);
                    JSONObject jSONObject6 = new JSONObject(jSONObject.getString(str9));
                    if (jSONObject6.getString("DiemNhan").length() > 0) {
                        frag_No_new.dec_Nhan.setText(jSONObject6.getString("DiemNhan") + "(" + jSONObject6.getString("AnNhan") + ")");
                        frag_No_new.dec_NhanAn.setText(jSONObject6.getString("KQNhan"));
                    }
                    if (jSONObject6.getString("DiemChuyen").length() > 0) {
                        frag_No_new.dec_Chuyen.setText(jSONObject6.getString("DiemChuyen") + "(" + jSONObject6.getString("AnChuyen") + ")");
                        frag_No_new.dec_ChuyenAn.setText(jSONObject6.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("ded")) {
                    frag_No_new.li_ded.setVisibility(0);
                    JSONObject jSONObject7 = new JSONObject(jSONObject.getString("ded"));
                    if (jSONObject7.getString("DiemNhan").length() > 0) {
                        frag_No_new.ded_Nhan.setText(jSONObject7.getString("DiemNhan") + "(" + jSONObject7.getString("AnNhan") + ")");
                        frag_No_new.ded_NhanAn.setText(jSONObject7.getString("KQNhan"));
                    }
                    if (jSONObject7.getString("DiemChuyen").length() > 0) {
                        frag_No_new.ded_Chuyen.setText(jSONObject7.getString("DiemChuyen") + "(" + jSONObject7.getString("AnChuyen") + ")");
                        frag_No_new.ded_ChuyenAn.setText(jSONObject7.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("lo")) {
                    JSONObject jSONObject8 = new JSONObject(jSONObject.getString("lo"));
                    if (jSONObject8.getString("DiemNhan").length() > 0) {
                        frag_No_new.lo_Nhan.setText(jSONObject8.getString("DiemNhan") + "(" + jSONObject8.getString("AnNhan") + ")");
                        frag_No_new.lo_NhanAn.setText(jSONObject8.getString("KQNhan"));
                    }
                    if (jSONObject8.getString("DiemChuyen").length() > 0) {
                        frag_No_new.lo_Chuyen.setText(jSONObject8.getString("DiemChuyen") + "(" + jSONObject8.getString("AnChuyen") + ")");
                        frag_No_new.lo_ChuyenAn.setText(jSONObject8.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("loa")) {
                    frag_No_new.li_loa.setVisibility(0);
                    JSONObject jSONObject9 = new JSONObject(jSONObject.getString("loa"));
                    if (jSONObject9.getString("DiemNhan").length() > 0) {
                        frag_No_new.loa_Nhan.setText(jSONObject9.getString("DiemNhan") + "(" + jSONObject9.getString("AnNhan") + ")");
                        frag_No_new.loa_NhanAn.setText(jSONObject9.getString("KQNhan"));
                    }
                    if (jSONObject9.getString("DiemChuyen").length() > 0) {
                        frag_No_new.loa_Chuyen.setText(jSONObject9.getString("DiemChuyen") + "(" + jSONObject9.getString("AnChuyen") + ")");
                        frag_No_new.loa_ChuyenAn.setText(jSONObject9.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("xi")) {
                    JSONObject jSONObject10 = new JSONObject(jSONObject.getString("xi"));
                    if (jSONObject10.getString("DiemNhan").length() > 0) {
                        frag_No_new.xi2_Nhan.setText(jSONObject10.getString("DiemNhan") + "(" + jSONObject10.getString("AnNhan") + ")");
                        frag_No_new.xi2_NhanAn.setText(jSONObject10.getString("KQNhan"));
                    }
                    if (jSONObject10.getString("DiemChuyen").length() > 0) {
                        frag_No_new.xi2_Chuyen.setText(jSONObject10.getString("DiemChuyen") + "(" + jSONObject10.getString("AnChuyen") + ")");
                        frag_No_new.xi2_ChuyenAn.setText(jSONObject10.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("xn")) {
                    frag_No_new.li_xn.setVisibility(0);
                    JSONObject jSONObject11 = new JSONObject(jSONObject.getString("xn"));
                    if (jSONObject11.getString("DiemNhan").length() > 0) {
                        frag_No_new.xn_Nhan.setText(jSONObject11.getString("DiemNhan") + "(" + jSONObject11.getString("AnNhan") + ")");
                        frag_No_new.xn_NhanAn.setText(jSONObject11.getString("KQNhan"));
                    }
                    if (jSONObject11.getString("DiemChuyen").length() > 0) {
                        frag_No_new.xn_Chuyen.setText(jSONObject11.getString("DiemChuyen") + "(" + jSONObject11.getString("AnChuyen") + ")");
                        frag_No_new.xn_ChuyenAn.setText(jSONObject11.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("xia")) {
                    frag_No_new.li_xia2.setVisibility(0);
                    JSONObject jSONObject12 = new JSONObject(jSONObject.getString("xia"));
                    if (jSONObject12.getString("DiemNhan").length() > 0) {
                        frag_No_new.xia2_Nhan.setText(jSONObject12.getString("DiemNhan") + "(" + jSONObject12.getString("AnNhan") + ")");
                        frag_No_new.xia2_NhanAn.setText(jSONObject12.getString("KQNhan"));
                    }
                    if (jSONObject12.getString("DiemChuyen").length() > 0) {
                        frag_No_new.xia2_Chuyen.setText(jSONObject12.getString("DiemChuyen") + "(" + jSONObject12.getString("AnChuyen") + ")");
                        frag_No_new.xia2_ChuyenAn.setText(jSONObject12.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("bc")) {
                    JSONObject jSONObject13 = new JSONObject(jSONObject.getString("bc"));
                    if (jSONObject13.getString("DiemNhan").length() > 0) {
                        frag_No_new.bc_Nhan.setText(jSONObject13.getString("DiemNhan") + "(" + jSONObject13.getString("AnNhan") + ")");
                        frag_No_new.bc_NhanAn.setText(jSONObject13.getString("KQNhan"));
                    }
                    if (jSONObject13.getString("DiemChuyen").length() > 0) {
                        frag_No_new.bc_Chuyen.setText(jSONObject13.getString("DiemChuyen") + "(" + jSONObject13.getString("AnChuyen") + ")");
                        frag_No_new.bc_ChuyenAn.setText(jSONObject13.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("bca")) {
                    frag_No_new.li_bca.setVisibility(0);
                    JSONObject jSONObject14 = new JSONObject(jSONObject.getString("bca"));
                    if (jSONObject14.getString("DiemNhan").length() > 0) {
                        frag_No_new.bca_Nhan.setText(jSONObject14.getString("DiemNhan") + "(" + jSONObject14.getString("AnNhan") + ")");
                        frag_No_new.bca_NhanAn.setText(jSONObject14.getString("KQNhan"));
                    }
                    if (jSONObject14.getString("DiemChuyen").length() > 0) {
                        frag_No_new.bca_Chuyen.setText(jSONObject14.getString("DiemChuyen") + "(" + jSONObject14.getString("AnChuyen") + ")");
                        frag_No_new.bca_ChuyenAn.setText(jSONObject14.getString("KQChuyen"));
                    }
                }
                DecimalFormat decimalFormat3 = decimalFormat;
                frag_No_new.tv_TongTienNhan.setText(decimalFormat3.format(d));
                double d4 = d3;
                frag_No_new.tv_TongTienChuyen.setText(decimalFormat3.format(d4));
                frag_No_new.tv_TongGiu.setText(decimalFormat3.format((-d) - d4));
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                XemListview();
            }
            cursor = GetData;
            cursor.close();
            XemListview();
        }
    }

    private void XemListview() {
        new MainActivity();
        String Get_date = MainActivity.Get_date();
        this.jsonKhachHang = new ArrayList();
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        this.mTenKH.clear();
        this.mSDT.clear();
        try {
            Cursor GetData = this.f202db.GetData("Select ten_kh, so_dienthoai, the_loai\n, sum((type_kh = 1)*(100-diem_khachgiu)*diem/100) as mDiem\n, CASE WHEN the_loai = 'xi' OR the_loai = 'xia' \n THEN sum((type_kh = 1)*(100-diem_khachgiu)*diem/100*so_nhay*lan_an/1000) \n ELSE sum((type_kh = 1)*(100-diem_khachgiu)*diem/100*so_nhay)  END nAn\n, sum((type_kh = 1)*ket_qua*(100-diem_khachgiu)/100/1000) as mKetqua\n, sum((type_kh = 2)*(100-diem_khachgiu)*diem/100) as mDiem\n, CASE WHEN the_loai = 'xi' OR the_loai = 'xia' \n THEN sum((type_kh = 2)*(100-diem_khachgiu)*diem/100*so_nhay*lan_an/1000) \n ELSE sum((type_kh = 2)*(100-diem_khachgiu)*diem/100*so_nhay)  END nAn\n, sum((type_kh = 2)* ket_qua/1000) as mKetqua\n  From tbl_soctS Where ngay_nhan = '" + Get_date + "'\n  AND the_loai <> 'tt' GROUP by ten_kh, the_loai");
            JSONObject jSONObject = new JSONObject();
            if (GetData != null) {
                String str = "";
                double d = 0.0d;
                double d2 = 0.0d;
                while (GetData.moveToNext()) {
                    if (str.length() == 0) {
                        this.mTenKH.add(GetData.getString(0));
                        this.mSDT.add(GetData.getString(1));
                        str = GetData.getString(0);
                    } else if (str.indexOf(GetData.getString(0)) != 0) {
                        jSONObject.put("Tien_Nhan", decimalFormat.format(d));
                        jSONObject.put("Tien_Chuyen", decimalFormat.format(d2));
                        jSONObject.put("Tong_Tien", decimalFormat.format(d + d2));
                        this.jsonKhachHang.add(jSONObject);
                        this.mTenKH.add(GetData.getString(0));
                        this.mSDT.add(GetData.getString(1));
                        String string = GetData.getString(0);
                        d = 0.0d;
                        d2 = 0.0d;
                        str = string;
                        jSONObject = new JSONObject();
                    }
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("DiemNhan", decimalFormat.format(GetData.getDouble(3)));
                    jSONObject2.put("AnNhan", decimalFormat.format(GetData.getDouble(4)));
                    jSONObject2.put("KQNhan", decimalFormat.format(GetData.getDouble(5)));
                    jSONObject2.put("DiemChuyen", decimalFormat.format(GetData.getDouble(6)));
                    jSONObject2.put("AnChuyen", decimalFormat.format(GetData.getDouble(7)));
                    jSONObject2.put("KQChuyen", decimalFormat.format(GetData.getDouble(8)));
                    d += GetData.getDouble(5);
                    d2 += GetData.getDouble(8);
                    jSONObject.put(GetData.getString(2), jSONObject2.toString());
                }
                jSONObject.put("Tien_Nhan", decimalFormat.format(d));
                jSONObject.put("Tien_Chuyen", decimalFormat.format(d2));
                jSONObject.put("Tong_Tien", decimalFormat.format(d + d2));
                if (GetData.getCount() > 0) {
                    this.jsonKhachHang.add(jSONObject);
                }
                if (GetData != null && !GetData.isClosed()) {
                    GetData.close();
                }
            }
        } catch (SQLException unused) {
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (getActivity() != null) {
            this.lv_baocaoKhach.setAdapter(new NoRP_TN_Adapter(getActivity(), R.layout.frag_norp1_2, this.jsonKhachHang));
        }
    }

    class NoRP_TN_Adapter extends ArrayAdapter {
        TextView bc_Chuyen;
        TextView bc_ChuyenAn;
        TextView bc_Nhan;
        TextView bc_NhanAn;
        TextView bca_Chuyen;
        TextView bca_ChuyenAn;
        TextView bca_Nhan;
        TextView bca_NhanAn;
        TextView dea_Chuyen;
        TextView dea_ChuyenAn;
        TextView dea_Nhan;
        TextView dea_NhanAn;
        TextView deb_Chuyen;
        TextView deb_ChuyenAn;
        TextView deb_Nhan;
        TextView deb_NhanAn;
        TextView dec_Chuyen;
        TextView dec_ChuyenAn;
        TextView dec_Nhan;
        TextView dec_NhanAn;
        TextView ded_Chuyen;
        TextView ded_ChuyenAn;
        TextView ded_Nhan;
        TextView ded_NhanAn;
        TextView det_Chuyen;
        TextView det_ChuyenAn;
        TextView det_Nhan;
        TextView det_NhanAn;
        LinearLayout li_bca;
        LinearLayout li_dea;
        LinearLayout li_dec;
        LinearLayout li_ded;
        LinearLayout li_det;
        LinearLayout li_loa;
        LinearLayout li_xi2;
        LinearLayout li_xia2;
        TextView lo_Chuyen;
        TextView lo_ChuyenAn;
        TextView lo_Nhan;
        TextView lo_NhanAn;
        TextView loa_Chuyen;
        TextView loa_ChuyenAn;
        TextView loa_Nhan;
        TextView loa_NhanAn;
        TextView tv_TongKhach;
        TextView tv_TongTienChuyen;
        TextView tv_TongTienNhan;
        TextView tv_ket_qua;
        TextView tv_tenKH;
        TextView tv_tongtien;
        TextView xi2_Chuyen;
        TextView xi2_ChuyenAn;
        TextView xi2_Nhan;
        TextView xi2_NhanAn;
        TextView xia2_Chuyen;
        TextView xia2_ChuyenAn;
        TextView xia2_Nhan;
        TextView xia2_NhanAn;

        public NoRP_TN_Adapter(Context context, int i, List<JSONObject> list) {
            super(context, i, list);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2;
            String str;
            String str2;
            int i2 = i;
            View inflate = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.frag_norp1_2, (ViewGroup) null);
            this.tv_tongtien = (TextView) inflate.findViewById(R.id.tv_no_Tong);
            this.tv_ket_qua = (TextView) inflate.findViewById(R.id.tv_ThangThua);
            this.tv_tenKH = (TextView) inflate.findViewById(R.id.tv_tenKH);
            this.dea_Nhan = (TextView) inflate.findViewById(R.id.dea_Nhan);
            this.deb_Nhan = (TextView) inflate.findViewById(R.id.deb_Nhan);
            this.det_Nhan = (TextView) inflate.findViewById(R.id.det_Nhan);
            this.dec_Nhan = (TextView) inflate.findViewById(R.id.dec_Nhan);
            this.ded_Nhan = (TextView) inflate.findViewById(R.id.ded_Nhan);
            this.lo_Nhan = (TextView) inflate.findViewById(R.id.lo_Nhan);
            this.loa_Nhan = (TextView) inflate.findViewById(R.id.loa_Nhan);
            this.bc_Nhan = (TextView) inflate.findViewById(R.id.bc_Nhan);
            this.bca_Nhan = (TextView) inflate.findViewById(R.id.bca_Nhan);
            this.dea_NhanAn = (TextView) inflate.findViewById(R.id.dea_NhanAn);
            this.deb_NhanAn = (TextView) inflate.findViewById(R.id.deb_NhanAn);
            this.det_NhanAn = (TextView) inflate.findViewById(R.id.det_NhanAn);
            this.dec_NhanAn = (TextView) inflate.findViewById(R.id.dec_NhanAn);
            this.ded_NhanAn = (TextView) inflate.findViewById(R.id.ded_NhanAn);
            this.lo_NhanAn = (TextView) inflate.findViewById(R.id.lo_NhanAn);
            this.loa_NhanAn = (TextView) inflate.findViewById(R.id.loa_NhanAn);
            this.bc_NhanAn = (TextView) inflate.findViewById(R.id.bc_NhanAn);
            this.dea_Chuyen = (TextView) inflate.findViewById(R.id.dea_Chuyen);
            this.deb_Chuyen = (TextView) inflate.findViewById(R.id.deb_Chuyen);
            this.det_Chuyen = (TextView) inflate.findViewById(R.id.det_Chuyen);
            this.dec_Chuyen = (TextView) inflate.findViewById(R.id.dec_Chuyen);
            this.ded_Chuyen = (TextView) inflate.findViewById(R.id.ded_Chuyen);
            this.lo_Chuyen = (TextView) inflate.findViewById(R.id.lo_Chuyen);
            this.loa_Chuyen = (TextView) inflate.findViewById(R.id.loa_Chuyen);
            this.bc_Chuyen = (TextView) inflate.findViewById(R.id.bc_Chuyen);
            this.dea_ChuyenAn = (TextView) inflate.findViewById(R.id.dea_ChuyenAn);
            this.deb_ChuyenAn = (TextView) inflate.findViewById(R.id.deb_ChuyenAn);
            this.det_ChuyenAn = (TextView) inflate.findViewById(R.id.det_ChuyenAn);
            this.dec_ChuyenAn = (TextView) inflate.findViewById(R.id.dec_ChuyenAn);
            this.ded_ChuyenAn = (TextView) inflate.findViewById(R.id.ded_ChuyenAn);
            this.lo_ChuyenAn = (TextView) inflate.findViewById(R.id.lo_ChuyenAn);
            this.loa_ChuyenAn = (TextView) inflate.findViewById(R.id.loa_ChuyenAn);
            this.bc_ChuyenAn = (TextView) inflate.findViewById(R.id.bc_ChuyenAn);
            this.tv_TongKhach = (TextView) inflate.findViewById(R.id.tv_TongTien);
            this.tv_TongTienNhan = (TextView) inflate.findViewById(R.id.tv_TongTienNhan);
            this.tv_TongTienChuyen = (TextView) inflate.findViewById(R.id.tv_TongTienChuyen);
            this.li_dea = (LinearLayout) inflate.findViewById(R.id.li_dea);
            this.li_det = (LinearLayout) inflate.findViewById(R.id.li_det);
            this.li_dec = (LinearLayout) inflate.findViewById(R.id.li_dec);
            this.li_ded = (LinearLayout) inflate.findViewById(R.id.li_ded);
            this.li_loa = (LinearLayout) inflate.findViewById(R.id.li_loa);
            this.li_bca = (LinearLayout) inflate.findViewById(R.id.li_bca);
            this.li_xi2 = (LinearLayout) inflate.findViewById(R.id.li_xi2);
            this.li_xia2 = (LinearLayout) inflate.findViewById(R.id.li_xia2);
            this.xi2_Nhan = (TextView) inflate.findViewById(R.id.xi2_Nhan);
            this.xi2_NhanAn = (TextView) inflate.findViewById(R.id.xi2_NhanAn);
            this.xi2_Chuyen = (TextView) inflate.findViewById(R.id.xi2_Chuyen);
            this.xi2_ChuyenAn = (TextView) inflate.findViewById(R.id.xi2_ChuyenAn);
            this.xia2_Nhan = (TextView) inflate.findViewById(R.id.xia2_Nhan);
            this.xia2_NhanAn = (TextView) inflate.findViewById(R.id.xia2_NhanAn);
            this.xia2_Chuyen = (TextView) inflate.findViewById(R.id.xia2_Chuyen);
            this.xia2_ChuyenAn = (TextView) inflate.findViewById(R.id.xia2_ChuyenAn);
            this.bca_Nhan = (TextView) inflate.findViewById(R.id.bca_Nhan);
            this.bca_NhanAn = (TextView) inflate.findViewById(R.id.bca_NhanAn);
            this.bca_Chuyen = (TextView) inflate.findViewById(R.id.bca_Chuyen);
            this.bca_ChuyenAn = (TextView) inflate.findViewById(R.id.bca_ChuyenAn);
            JSONObject jSONObject = Frag_No_new.this.jsonKhachHang.get(i2);
            try {
                this.tv_TongTienNhan.setText(jSONObject.getString("Tien_Nhan"));
                this.tv_TongTienChuyen.setText(jSONObject.getString("Tien_Chuyen"));
                this.tv_TongKhach.setText(jSONObject.getString("Tong_Tien"));
                this.tv_tenKH.setText((CharSequence) Frag_No_new.this.mTenKH.get(i2));
                view2 = inflate;
                if (jSONObject.has("dea")) {
                    try {
                        str = "ded";
                        this.li_dea.setVisibility(0);
                        JSONObject jSONObject2 = new JSONObject(jSONObject.getString("dea"));
                        if (jSONObject2.getString("DiemNhan").length() > 0) {
                            TextView textView = this.dea_Nhan;
                            StringBuilder sb = new StringBuilder();
                            str2 = "dec";
                            sb.append(jSONObject2.getString("DiemNhan"));
                            sb.append("(");
                            sb.append(jSONObject2.getString("AnNhan"));
                            sb.append(")");
                            textView.setText(sb.toString());
                            this.dea_NhanAn.setText(jSONObject2.getString("KQNhan"));
                        } else {
                            str2 = "dec";
                        }
                        if (jSONObject2.getString("DiemChuyen").length() > 0) {
                            TextView textView2 = this.dea_Chuyen;
                            textView2.setText(jSONObject2.getString("DiemChuyen") + "(" + jSONObject2.getString("AnChuyen") + ")");
                            this.dea_ChuyenAn.setText(jSONObject2.getString("KQChuyen"));
                        }
                    } catch (Exception unused) {
                        Toast.makeText(Frag_No_new.this.getActivity(), "OK", 1).show();
                        return view2;
                    }
                } else {
                    str = "ded";
                    str2 = "dec";
                }
                if (jSONObject.has("deb")) {
                    JSONObject jSONObject3 = new JSONObject(jSONObject.getString("deb"));
                    if (jSONObject3.getString("DiemNhan").length() > 0) {
                        TextView textView3 = this.deb_Nhan;
                        textView3.setText(jSONObject3.getString("DiemNhan") + "(" + jSONObject3.getString("AnNhan") + ")");
                        this.deb_NhanAn.setText(jSONObject3.getString("KQNhan"));
                    }
                    if (jSONObject3.getString("DiemChuyen").length() > 0) {
                        TextView textView4 = this.deb_Chuyen;
                        textView4.setText(jSONObject3.getString("DiemChuyen") + "(" + jSONObject3.getString("AnChuyen") + ")");
                        this.deb_ChuyenAn.setText(jSONObject3.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("det")) {
                    this.li_det.setVisibility(0);
                    JSONObject jSONObject4 = new JSONObject(jSONObject.getString("det"));
                    if (jSONObject4.getString("DiemNhan").length() > 0) {
                        TextView textView5 = this.det_Nhan;
                        textView5.setText(jSONObject4.getString("DiemNhan") + "(" + jSONObject4.getString("AnNhan") + ")");
                        this.det_NhanAn.setText(jSONObject4.getString("KQNhan"));
                    }
                    if (jSONObject4.getString("DiemChuyen").length() > 0) {
                        TextView textView6 = this.det_Chuyen;
                        textView6.setText(jSONObject4.getString("DiemChuyen") + "(" + jSONObject4.getString("AnChuyen") + ")");
                        this.det_ChuyenAn.setText(jSONObject4.getString("KQChuyen"));
                    }
                }
                String str3 = str2;
                if (jSONObject.has(str3)) {
                    this.li_dec.setVisibility(0);
                    JSONObject jSONObject5 = new JSONObject(jSONObject.getString(str3));
                    if (jSONObject5.getString("DiemNhan").length() > 0) {
                        TextView textView7 = this.dec_Nhan;
                        textView7.setText(jSONObject5.getString("DiemNhan") + "(" + jSONObject5.getString("AnNhan") + ")");
                        this.dec_NhanAn.setText(jSONObject5.getString("KQNhan"));
                    }
                    if (jSONObject5.getString("DiemChuyen").length() > 0) {
                        TextView textView8 = this.dec_Chuyen;
                        textView8.setText(jSONObject5.getString("DiemChuyen") + "(" + jSONObject5.getString("AnChuyen") + ")");
                        this.dec_ChuyenAn.setText(jSONObject5.getString("KQChuyen"));
                    }
                }
                String str4 = str;
                if (jSONObject.has(str4)) {
                    this.li_ded.setVisibility(0);
                    JSONObject jSONObject6 = new JSONObject(jSONObject.getString(str4));
                    if (jSONObject6.getString("DiemNhan").length() > 0) {
                        TextView textView9 = this.ded_Nhan;
                        textView9.setText(jSONObject6.getString("DiemNhan") + "(" + jSONObject6.getString("AnNhan") + ")");
                        this.ded_NhanAn.setText(jSONObject6.getString("KQNhan"));
                    }
                    if (jSONObject6.getString("DiemChuyen").length() > 0) {
                        TextView textView10 = this.ded_Chuyen;
                        textView10.setText(jSONObject6.getString("DiemChuyen") + "(" + jSONObject6.getString("AnChuyen") + ")");
                        this.ded_ChuyenAn.setText(jSONObject6.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("lo")) {
                    JSONObject jSONObject7 = new JSONObject(jSONObject.getString("lo"));
                    if (jSONObject7.getString("DiemNhan").length() > 0) {
                        TextView textView11 = this.lo_Nhan;
                        textView11.setText(jSONObject7.getString("DiemNhan") + "(" + jSONObject7.getString("AnNhan") + ")");
                        this.lo_NhanAn.setText(jSONObject7.getString("KQNhan"));
                    }
                    if (jSONObject7.getString("DiemChuyen").length() > 0) {
                        TextView textView12 = this.lo_Chuyen;
                        textView12.setText(jSONObject7.getString("DiemChuyen") + "(" + jSONObject7.getString("AnChuyen") + ")");
                        this.lo_ChuyenAn.setText(jSONObject7.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("loa")) {
                    this.li_loa.setVisibility(0);
                    JSONObject jSONObject8 = new JSONObject(jSONObject.getString("loa"));
                    if (jSONObject8.getString("DiemNhan").length() > 0) {
                        TextView textView13 = this.loa_Nhan;
                        textView13.setText(jSONObject8.getString("DiemNhan") + "(" + jSONObject8.getString("AnNhan") + ")");
                        this.loa_NhanAn.setText(jSONObject8.getString("KQNhan"));
                    }
                    if (jSONObject8.getString("DiemChuyen").length() > 0) {
                        TextView textView14 = this.loa_Chuyen;
                        textView14.setText(jSONObject8.getString("DiemChuyen") + "(" + jSONObject8.getString("AnChuyen") + ")");
                        this.loa_ChuyenAn.setText(jSONObject8.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("xi")) {
                    this.li_xi2.setVisibility(0);
                    JSONObject jSONObject9 = new JSONObject(jSONObject.getString("xi"));
                    if (jSONObject9.getString("DiemNhan").length() > 0) {
                        TextView textView15 = this.xi2_Nhan;
                        textView15.setText(jSONObject9.getString("DiemNhan") + "(" + jSONObject9.getString("AnNhan") + ")");
                        this.xi2_NhanAn.setText(jSONObject9.getString("KQNhan"));
                    }
                    if (jSONObject9.getString("DiemChuyen").length() > 0) {
                        TextView textView16 = this.xi2_Chuyen;
                        textView16.setText(jSONObject9.getString("DiemChuyen") + "(" + jSONObject9.getString("AnChuyen") + ")");
                        this.xi2_ChuyenAn.setText(jSONObject9.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("xia")) {
                    this.li_xia2.setVisibility(View.VISIBLE);
                    JSONObject jSONObject10 = new JSONObject(jSONObject.getString("xia"));
                    if (jSONObject10.getString("DiemNhan").length() > 0) {
                        TextView textView17 = this.xia2_Nhan;
                        textView17.setText(jSONObject10.getString("DiemNhan") + "(" + jSONObject10.getString("AnNhan") + ")");
                        this.xia2_NhanAn.setText(jSONObject10.getString("KQNhan"));
                    }
                    if (jSONObject10.getString("DiemChuyen").length() > 0) {
                        TextView textView18 = this.xia2_Chuyen;
                        textView18.setText(jSONObject10.getString("DiemChuyen") + "(" + jSONObject10.getString("AnChuyen") + ")");
                        this.xia2_ChuyenAn.setText(jSONObject10.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("bc")) {
                    JSONObject jSONObject11 = new JSONObject(jSONObject.getString("bc"));
                    if (jSONObject11.getString("DiemNhan").length() > 0) {
                        TextView textView19 = this.bc_Nhan;
                        textView19.setText(jSONObject11.getString("DiemNhan") + "(" + jSONObject11.getString("AnNhan") + ")");
                        this.bc_NhanAn.setText(jSONObject11.getString("KQNhan"));
                    }
                    if (jSONObject11.getString("DiemChuyen").length() > 0) {
                        TextView textView20 = this.bc_Chuyen;
                        textView20.setText(jSONObject11.getString("DiemChuyen") + "(" + jSONObject11.getString("AnChuyen") + ")");
                        this.bc_ChuyenAn.setText(jSONObject11.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("bca")) {
                    this.li_bca.setVisibility(0);
                    JSONObject jSONObject12 = new JSONObject(jSONObject.getString("bca"));
                    if (jSONObject12.getString("DiemNhan").length() > 0) {
                        TextView textView21 = this.bca_Nhan;
                        textView21.setText(jSONObject12.getString("DiemNhan") + "(" + jSONObject12.getString("AnNhan") + ")");
                        this.bca_NhanAn.setText(jSONObject12.getString("KQNhan"));
                    }
                    if (jSONObject12.getString("DiemChuyen").length() > 0) {
                        TextView textView22 = this.bca_Chuyen;
                        textView22.setText(jSONObject12.getString("DiemChuyen") + "(" + jSONObject12.getString("AnChuyen") + ")");
                        this.bca_ChuyenAn.setText(jSONObject12.getString("KQChuyen"));
                    }
                }
            } catch (Exception unused2) {
                view2 = inflate;
                Toast.makeText(Frag_No_new.this.getActivity(), "OK", 1).show();
                return view2;
            }
            return view2;
        }

    }

    private void init() {
        this.xn_Nhan = (TextView) this.f203v.findViewById(R.id.xn_Nhan);
        this.xn_NhanAn = (TextView) this.f203v.findViewById(R.id.xn_NhanAn);
        this.xn_Chuyen = (TextView) this.f203v.findViewById(R.id.xn_Chuyen);
        this.xn_ChuyenAn = (TextView) this.f203v.findViewById(R.id.xn_ChuyenAn);
        this.dea_Nhan = (TextView) this.f203v.findViewById(R.id.dea_Nhan);
        this.deb_Nhan = (TextView) this.f203v.findViewById(R.id.deb_Nhan);
        this.det_Nhan = (TextView) this.f203v.findViewById(R.id.det_Nhan);
        this.dec_Nhan = (TextView) this.f203v.findViewById(R.id.dec_Nhan);
        this.ded_Nhan = (TextView) this.f203v.findViewById(R.id.ded_Nhan);
        this.lo_Nhan = (TextView) this.f203v.findViewById(R.id.lo_Nhan);
        this.loa_Nhan = (TextView) this.f203v.findViewById(R.id.loa_Nhan);
        this.bc_Nhan = (TextView) this.f203v.findViewById(R.id.bc_Nhan);
        this.bca_Nhan = (TextView) this.f203v.findViewById(R.id.bca_Nhan);
        this.dea_NhanAn = (TextView) this.f203v.findViewById(R.id.dea_NhanAn);
        this.deb_NhanAn = (TextView) this.f203v.findViewById(R.id.deb_NhanAn);
        this.det_NhanAn = (TextView) this.f203v.findViewById(R.id.det_NhanAn);
        this.dec_NhanAn = (TextView) this.f203v.findViewById(R.id.dec_NhanAn);
        this.ded_NhanAn = (TextView) this.f203v.findViewById(R.id.ded_NhanAn);
        this.lo_NhanAn = (TextView) this.f203v.findViewById(R.id.lo_NhanAn);
        this.loa_NhanAn = (TextView) this.f203v.findViewById(R.id.loa_NhanAn);
        this.bc_NhanAn = (TextView) this.f203v.findViewById(R.id.bc_NhanAn);
        this.dea_Chuyen = (TextView) this.f203v.findViewById(R.id.dea_Chuyen);
        this.deb_Chuyen = (TextView) this.f203v.findViewById(R.id.deb_Chuyen);
        this.det_Chuyen = (TextView) this.f203v.findViewById(R.id.det_Chuyen);
        this.dec_Chuyen = (TextView) this.f203v.findViewById(R.id.dec_Chuyen);
        this.ded_Chuyen = (TextView) this.f203v.findViewById(R.id.ded_Chuyen);
        this.lo_Chuyen = (TextView) this.f203v.findViewById(R.id.lo_Chuyen);
        this.loa_Chuyen = (TextView) this.f203v.findViewById(R.id.loa_Chuyen);
        this.bc_Chuyen = (TextView) this.f203v.findViewById(R.id.bc_Chuyen);
        this.dea_ChuyenAn = (TextView) this.f203v.findViewById(R.id.dea_ChuyenAn);
        this.deb_ChuyenAn = (TextView) this.f203v.findViewById(R.id.deb_ChuyenAn);
        this.det_ChuyenAn = (TextView) this.f203v.findViewById(R.id.det_ChuyenAn);
        this.dec_ChuyenAn = (TextView) this.f203v.findViewById(R.id.dec_ChuyenAn);
        this.ded_ChuyenAn = (TextView) this.f203v.findViewById(R.id.ded_ChuyenAn);
        this.lo_ChuyenAn = (TextView) this.f203v.findViewById(R.id.lo_ChuyenAn);
        this.loa_ChuyenAn = (TextView) this.f203v.findViewById(R.id.loa_ChuyenAn);
        this.bc_ChuyenAn = (TextView) this.f203v.findViewById(R.id.bc_ChuyenAn);
        this.tv_TongGiu = (TextView) this.f203v.findViewById(R.id.tv_TongGiu);
        this.tv_TongTienNhan = (TextView) this.f203v.findViewById(R.id.tv_TongTienNhan);
        this.tv_TongTienChuyen = (TextView) this.f203v.findViewById(R.id.tv_TongTienChuyen);
        this.li_dea = (LinearLayout) this.f203v.findViewById(R.id.li_dea);
        this.li_det = (LinearLayout) this.f203v.findViewById(R.id.li_det);
        this.li_dec = (LinearLayout) this.f203v.findViewById(R.id.li_dec);
        this.li_ded = (LinearLayout) this.f203v.findViewById(R.id.li_ded);
        this.li_loa = (LinearLayout) this.f203v.findViewById(R.id.li_loa);
        this.li_bca = (LinearLayout) this.f203v.findViewById(R.id.li_bca);
        this.li_xia2 = (LinearLayout) this.f203v.findViewById(R.id.li_xia2);
        this.li_xn = (LinearLayout) this.f203v.findViewById(R.id.li_xn);
        this.xi2_Nhan = (TextView) this.f203v.findViewById(R.id.xi2_Nhan);
        this.xi2_NhanAn = (TextView) this.f203v.findViewById(R.id.xi2_NhanAn);
        this.xi2_Chuyen = (TextView) this.f203v.findViewById(R.id.xi2_Chuyen);
        this.xi2_ChuyenAn = (TextView) this.f203v.findViewById(R.id.xi2_ChuyenAn);
        this.xia2_Nhan = (TextView) this.f203v.findViewById(R.id.xia2_Nhan);
        this.xia2_NhanAn = (TextView) this.f203v.findViewById(R.id.xia2_NhanAn);
        this.xia2_Chuyen = (TextView) this.f203v.findViewById(R.id.xia2_Chuyen);
        this.xia2_ChuyenAn = (TextView) this.f203v.findViewById(R.id.xia2_ChuyenAn);
        this.bca_Nhan = (TextView) this.f203v.findViewById(R.id.bca_Nhan);
        this.bca_NhanAn = (TextView) this.f203v.findViewById(R.id.bca_NhanAn);
        this.bca_Chuyen = (TextView) this.f203v.findViewById(R.id.bca_Chuyen);
        this.bca_ChuyenAn = (TextView) this.f203v.findViewById(R.id.bca_ChuyenAn);
        this.lv_baocaoKhach = (ListView) this.f203v.findViewById(R.id.lv_baocaoKhach);
    }
}
