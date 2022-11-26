package tamhoang.ldpro4.data;

import org.json.JSONException;

public class DbHelper {
    //cai dat
    static int getKHGiu(Database db, String theloai) throws JSONException {
        if (theloai.contains("de"))
            return db.caidat_tg.getInt("khgiu_de");
        if (theloai.contains("lo"))
            return db.caidat_tg.getInt("khgiu_lo");
        if (theloai.contains("xi"))
            return db.caidat_tg.getInt("khgiu_xi");
        if (theloai.contains("bc"))
            return db.caidat_tg.getInt("khgiu_bc");
        if (theloai.contains("xn"))
            return db.caidat_tg.getInt("khgiu_xn");
        return 0;
    };

    static int getDlyGiu(Database db, String theloai) throws JSONException {
        if (theloai.contains("de"))
            return db.caidat_tg.getInt("dlgiu_de");
        if (theloai.contains("lo"))
            return db.caidat_tg.getInt("dlgiu_lo");
        if (theloai.contains("xi"))
            return db.caidat_tg.getInt("dlgiu_xi");
        if (theloai.contains("bc"))
            return db.caidat_tg.getInt("dlgiu_bc");
        if (theloai.contains("xn"))
            return db.caidat_tg.getInt("dlgiu_xn");
        return 0;
    }

    static double getGia(Database db, String the_loai, String dan_so) throws JSONException {
        if (the_loai.contains("dea"))
            return db.caidat_gia.getDouble("dea");
        if (the_loai.contains("deb"))
            return db.caidat_gia.getDouble("deb");
        if (the_loai.contains("dec"))
            return db.caidat_gia.getDouble("dec");
        if (the_loai.contains("ded"))
            return db.caidat_gia.getDouble("ded");
        if (the_loai.contains("det"))
            return db.caidat_gia.getDouble("det");
        if (the_loai.contains("lo"))
            return db.caidat_gia.getDouble("lo");
        if (the_loai.contains("xi") && dan_so.length() == 5)
            return db.caidat_gia.getDouble("gia_x2");
        if (the_loai.contains("xi") && dan_so.length() == 8)
            return db.caidat_gia.getDouble("gia_x3");
        if (the_loai.contains("xi") && dan_so.length() == 11)
            return db.caidat_gia.getDouble("gia_x4");
        if (the_loai.contains("xn"))
            return db.caidat_gia.getDouble("gia_xn");
        if (the_loai.contains("bc"))
            return db.caidat_gia.getDouble("gia_bc");

        return 0.0d;
    }

    static double getLanAn(Database db, String the_loai, String dan_so) throws JSONException {
        if (the_loai.contains("dea"))
            return db.caidat_gia.getDouble("an_dea");
        if (the_loai.contains("deb"))
            return db.caidat_gia.getDouble("an_deb");
        if (the_loai.contains("dec"))
            return db.caidat_gia.getDouble("an_dec");
        if (the_loai.contains("ded"))
            return db.caidat_gia.getDouble("an_ded");
        if (the_loai.contains("det"))
            return db.caidat_gia.getDouble("an_det");
        if (the_loai.contains("lo"))
            return db.caidat_gia.getDouble("an_lo");
        if (the_loai.contains("xi") && dan_so.length() == 5)
            return db.caidat_gia.getDouble("an_x2");
        if (the_loai.contains("xi") && dan_so.length() == 8)
            return db.caidat_gia.getDouble("an_x3");
        if (the_loai.contains("xi") && dan_so.length() == 11)
            return db.caidat_gia.getDouble("an_x4");
        if (the_loai.contains("xn"))
            return db.caidat_gia.getDouble("an_xn");
        if (the_loai.contains("bc"))
            return db.caidat_gia.getDouble("an_bc");

        return 0.0d;
    }

}
