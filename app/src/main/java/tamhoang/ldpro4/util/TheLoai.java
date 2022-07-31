package tamhoang.ldpro4.util;

public enum TheLoai {
    lo("lo","lo"),
    loa("loa","lo dau"),
    dea("dea","de dau db"),
    deb("deb","de dit db"),
    dec("dec","de dau nhat"),
    ded("ded","de dit nhat"),
    det("det","de 8"),
    hc("hc","hai cua"),
    xn("xn","xn"),
    de_("de ","de dit db"),
    bc("bc","bc"),
    cang("cang","bc"),
    bca("bca","bc dau"),
    xia("xia","xien dau"),
    xi("xi","xi"),
    xg_2("xg 2","xg 2"),
    xg_3("xg 3","xg 3"),
    xg_4("xg 4","xg 4"),
    xq("xq","xq"),
    xqa("xqa","xqa");

    String value;
    String convert;
    TheLoai(String value, String convert) {
        this.value = value;
        this.convert = convert;
    };
}
