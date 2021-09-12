package tamhoang.ldpro4.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Frag_Setting3 extends Fragment {
    String[] bc_apman;
    String[] bor_tintrung;
    Database db;
    String[] dv_chuyenXien;
    String[] hetgio;
    String[] kytu;
    String[] lamtron;
    String[] luachonBCao;
    String[] mCanhbao;
    String[] mTachxien;
    String[] mTinThieu;
    String[] sapxepBaocao;
    Spinner sp_BC_apman;
    Spinner sp_ChuyenXien;
    Spinner sp_LuachonBC;
    Spinner sp_TinThieu;
    Spinner sp_bo_tintrung;
    Spinner sp_canhbao;
    Spinner sp_chotTachXien;
    Spinner sp_hetgio;
    Spinner sp_kytu;
    Spinner sp_lamtron;
    Spinner sp_sapxepbaocao;
    Spinner sp_trathuonglo;
    String[] trathuong_lo;
    View v;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.frag_setting3, container, false);
        this.db = new Database(getActivity());
        init();
        this.bc_apman = new String[]{"0 trả thưởng", "Nhân 1 lần", "Nhân 2 lần", "Nhân 3 lần", "Nhân 4 lần", "Nhân 5 lần", "Nhân 6 lần", "Nhân 7 lần", "Nhân 8 lần", "Nhân 9 lần", "Nhân 10 lần"};
        this.sp_BC_apman.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.bc_apman));
        try {
            this.sp_BC_apman.setSelection(MainActivity.jSon_Setting.getInt("ap_man"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.sp_BC_apman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Setting3.AnonymousClass1 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_Setting3.this.db.Save_Setting("ap_man", i);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.hetgio = new String[]{"1. Không nhắn hết giờ", "2. Nhắn báo hết giờ"};
        this.sp_hetgio.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.hetgio));
        try {
            this.sp_hetgio.setSelection(MainActivity.jSon_Setting.getInt("tin_qua_gio"));
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        this.sp_hetgio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Setting3.AnonymousClass2 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_Setting3.this.db.Save_Setting("tin_qua_gio", i);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.bor_tintrung = new String[]{"1. Có nhận tin trùng", "2. Không nhận tin trùng"};
        this.sp_bo_tintrung.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.bor_tintrung));
        try {
            this.sp_bo_tintrung.setSelection(MainActivity.jSon_Setting.getInt("tin_trung"));
        } catch (JSONException e3) {
            e3.printStackTrace();
        }
        this.sp_bo_tintrung.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Setting3.AnonymousClass3 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_Setting3.this.db.Save_Setting("tin_trung", i);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.kytu = new String[]{"1. Không giới hạn", "2. 160 ký tự", "3. 320 ký tự", "4. 480 ký tự", "5. 1000 ký tự", "6. 2000 ký tự (Zalo)"};
        this.sp_kytu.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.kytu));
        try {
            this.sp_kytu.setSelection(MainActivity.jSon_Setting.getInt("gioi_han_tin") - 1);
        } catch (JSONException e4) {
            e4.printStackTrace();
        }
        this.sp_kytu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Setting3.AnonymousClass4 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_Setting3.this.db.Save_Setting("gioi_han_tin", i + 1);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.sapxepBaocao = new String[]{"1. Theo tổng tiền nhận", "2. Theo tổng tiền tồn"};
        this.sp_sapxepbaocao.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.sapxepBaocao));
        try {
            this.sp_sapxepbaocao.setSelection(MainActivity.jSon_Setting.getInt("bao_cao_so"));
        } catch (JSONException e5) {
            e5.printStackTrace();
        }
        this.sp_sapxepbaocao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Setting3.AnonymousClass5 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_Setting3.this.db.Save_Setting("bao_cao_so", i);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.dv_chuyenXien = new String[]{"1. Chuyển theo tiền", "2. Chuyển theo điểm"};
        this.sp_ChuyenXien.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.dv_chuyenXien));
        try {
            this.sp_ChuyenXien.setSelection(MainActivity.jSon_Setting.getInt("chuyen_xien"));
        } catch (JSONException e6) {
            e6.printStackTrace();
        }
        this.sp_ChuyenXien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Setting3.AnonymousClass6 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_Setting3.this.db.Save_Setting("chuyen_xien", i);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.lamtron = new String[]{"1. Không làm tròn", "2. Làm tròn đến 10", "3. Làm tròn đến 50", "4. Làm tròn đến 100"};
        this.sp_lamtron.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.lamtron));
        try {
            this.sp_lamtron.setSelection(MainActivity.jSon_Setting.getInt("lam_tron"));
        } catch (JSONException e7) {
            e7.printStackTrace();
        }
        this.sp_lamtron.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Setting3.AnonymousClass7 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_Setting3.this.db.Save_Setting("lam_tron", i);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.luachonBCao = new String[]{"1. Báo cáo kiểu cũ", "2. Báo cáo kiểu mới"};
        this.sp_LuachonBC.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.luachonBCao));
        try {
            this.sp_LuachonBC.setSelection(MainActivity.jSon_Setting.getInt("kieu_bao_cao"));
        } catch (JSONException e8) {
            e8.printStackTrace();
        }
        this.sp_LuachonBC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Setting3.AnonymousClass8 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_Setting3.this.db.Save_Setting("kieu_bao_cao", i);
                MainActivity mainActivity = (MainActivity) Frag_Setting3.this.getActivity();
                MainActivity.setListFragment(i);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.trathuong_lo = new String[]{"1. Trả đủ", "2. Nhiều nhất 2 nháy", "3. Nhiều nhất 3 nháy", "4. Nhiều nhất 4 nháy"};
        this.sp_trathuonglo.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.trathuong_lo));
        try {
            this.sp_trathuonglo.setSelection(MainActivity.jSon_Setting.getInt("tra_thuong_lo"));
        } catch (JSONException e9) {
            e9.printStackTrace();
        }
        this.sp_trathuonglo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Setting3.AnonymousClass9 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_Setting3.this.db.Save_Setting("tra_thuong_lo", i);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.mCanhbao = new String[]{"1. Không cảnh báo", "2. Có cảnh báo"};
        this.sp_canhbao.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.mCanhbao));
        try {
            this.sp_canhbao.setSelection(MainActivity.jSon_Setting.getInt("canhbaodonvi"));
        } catch (JSONException e10) {
            e10.printStackTrace();
        }
        this.sp_canhbao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Setting3.AnonymousClass10 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_Setting3.this.db.Save_Setting("canhbaodonvi", i);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.mTachxien = new String[]{"1. Không tách xiên", "2. Tách riêng xiên 2-3-4"};
        this.sp_chotTachXien.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.mTachxien));
        try {
            this.sp_chotTachXien.setSelection(MainActivity.jSon_Setting.getInt("tachxien_tinchot"));
        } catch (JSONException e11) {
            e11.printStackTrace();
        }
        this.sp_chotTachXien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Setting3.AnonymousClass11 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_Setting3.this.db.Save_Setting("tachxien_tinchot", i);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.mTinThieu = new String[]{"1. Không báo thiếu tin", "2. Có báo thiếu tin"};
        this.sp_TinThieu.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.mTinThieu));
        try {
            this.sp_TinThieu.setSelection(MainActivity.jSon_Setting.getInt("baotinthieu"));
        } catch (JSONException e12) {
            e12.printStackTrace();
        }
        this.sp_TinThieu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Setting3.AnonymousClass12 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_Setting3.this.db.Save_Setting("baotinthieu", i);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return this.v;
    }

    public void init() {
        this.sp_BC_apman = (Spinner) this.v.findViewById(R.id.sp_BC_Apman);
        this.sp_bo_tintrung = (Spinner) this.v.findViewById(R.id.sp_bo_tintrung);
        this.sp_sapxepbaocao = (Spinner) this.v.findViewById(R.id.sp_sapxepbaocao);
        this.sp_ChuyenXien = (Spinner) this.v.findViewById(R.id.sp_ChuyenXien);
        this.sp_LuachonBC = (Spinner) this.v.findViewById(R.id.sp_LuachonBC);
        this.sp_lamtron = (Spinner) this.v.findViewById(R.id.sp_lamtron);
        this.sp_trathuonglo = (Spinner) this.v.findViewById(R.id.sp_trathuonglo);
        this.sp_kytu = (Spinner) this.v.findViewById(R.id.sp_kytu);
        this.sp_hetgio = (Spinner) this.v.findViewById(R.id.sp_hetgio);
        this.sp_canhbao = (Spinner) this.v.findViewById(R.id.sp_canhbao);
        this.sp_chotTachXien = (Spinner) this.v.findViewById(R.id.sp_chotTachxien234);
        this.sp_TinThieu = (Spinner) this.v.findViewById(R.id.sp_baotinthieu);
    }
}