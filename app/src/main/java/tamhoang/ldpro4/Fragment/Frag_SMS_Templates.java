package tamhoang.ldpro4.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Frag_SMS_Templates extends Fragment {
    String Chu_y1 = "Các số phải gần nhau hoặc cách bởi dấu , hoặc dấu . ";

    /* renamed from: db */
    Database f206db;
    ArrayList<HashMap<String, String>> formArray = new ArrayList<>();
    TextView giaithich;
    ListView lv_Template;
    public List<String> mGiaiThich = new ArrayList();
    public List<String> mNoiDung = new ArrayList();
    TextView mauTin;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.frag_sms_templates, viewGroup, false);
        this.lv_Template = (ListView) inflate.findViewById(R.id.lv_template_sms);
        this.mauTin = (TextView) inflate.findViewById(R.id.tv_mautin);
        this.giaithich = (TextView) inflate.findViewById(R.id.tv_giaithich);
        addtoListview();
        xem_lv();
        this.lv_Template.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Frag_SMS_Templates.this.mauTin.setText(Frag_SMS_Templates.this.mNoiDung.get(i));
                Frag_SMS_Templates.this.giaithich.setText(Frag_SMS_Templates.this.mGiaiThich.get(i));
            }
        });
        return inflate;
    }

    private void addtoListview() {
        this.mNoiDung.clear();
        this.mGiaiThich.clear();
        this.mNoiDung.add("Viết tắt các dạng:");
        this.mGiaiThich.add("dea: đề 2 số đầu giải ĐB (đề đầu ĐB/ đầu ĐB)deb: đề 2 số cuối giải đbdet: đề 8 số cuối giải đb nhưng trả thưởng 80.000 (đề 8/ đề ăn 8)dec: đề 2 số đầu giải nhất (đề đầu nhất/ đầu nhất)ded: đề 2 số cuối giải nhất (đề đít nhất/ đít nhất)");
        this.mNoiDung.add("Đầu đb");
        this.mGiaiThich.add("2 số đầu giải đặc biệt");
        this.mNoiDung.add("Đầu nhất");
        this.mGiaiThich.add("Đề 2 sô đầu giải nhất");
        this.mNoiDung.add("Đít nhất");
        this.mGiaiThich.add("Đề 2 số cuối giải nhất");
        this.mNoiDung.add("Tổng chia 3");
        this.mGiaiThich.add("Các số chia hết cho 3");
        this.mNoiDung.add("Chia 3 dư 1");
        this.mGiaiThich.add("Các số chia cho 3 dư 1");
        this.mNoiDung.add("Chia 3 dư 2");
        this.mGiaiThich.add("Các số chia cho 3 dư 2");
        this.mNoiDung.add("Không chia 3");
        this.mGiaiThich.add("Các số không chia hết cho 3");
        this.mNoiDung.add("Tổng trên 10");
        this.mGiaiThich.add("Các số có tổng lớn hơn 10");
        this.mNoiDung.add("Tổng dưới 10");
        this.mGiaiThich.add("Các số có tổng bé hơn 10");
        this.mNoiDung.add("Tổng 10");
        this.mGiaiThich.add("Phần mềm sẽ báo lỗi vì không có tổng 10, chỉ có tổng 0 hoặc tổng 1 và 0 thì ghi tổng 01");
        this.mNoiDung.add("xg2 010,030,78,89,60 x 10");
        this.mGiaiThich.add("Phần mềm sẽ tự động ghép xiên 2 của tất cả các số với nhau, hãy kiểm tra cẩn thận có số giống nhau khi phần mềm báo lỗi");
        this.mNoiDung.add("xg3 010,030,78,89,60 x 10");
        this.mGiaiThich.add("Phần mềm sẽ tự động ghép xiên 3 của tất cả các số với nhau, hãy kiểm tra cẩn thận có số giống nhau khi phần mềm báo lỗi");
        this.mNoiDung.add("xg4 010,030,78,89,60 x 10");
        this.mGiaiThich.add("Phần mềm sẽ tự động ghép xiên 4 của tất cả các số với nhau, hãy kiểm tra cẩn thận có số giống nhau khi phần mềm báo lỗi");
        this.mNoiDung.add("De dan 18 bor kep x 10");
        this.mGiaiThich.add("Chữ bo có 2 nghĩa là bỏ và bộ nên chữ bỏ phải thêm chữ 'r' thành bor");
        this.mNoiDung.add("De boj 02,04 x 10");
        this.mGiaiThich.add("Chữ bo có 2 nghĩa là bỏ và bộ nên chữ bộ phải thêm chữ 'j' thành boj");
        this.mNoiDung.add("de giap ty x 100, de giap chuột x 100");
        this.mGiaiThich.add("Các con giáp sẽ được ghi bằng cách viết giap + tên con giáp");
    }

    public void xem_lv() {
        this.lv_Template.setAdapter(new TNGAdapter(getActivity(), R.layout.frag_sms_temp_lv, this.mNoiDung));
    }

    class TNGAdapter extends ArrayAdapter {
        public TNGAdapter(Context context, int i, List<String> list) {
            super(context, i, list);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.frag_sms_temp_lv, (ViewGroup) null);
            ((TextView) inflate.findViewById(R.id.tv_noidung)).setText(Frag_SMS_Templates.this.mNoiDung.get(i));
            return inflate;
        }
    }
}
