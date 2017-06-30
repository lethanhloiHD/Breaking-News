package Modules;

/**
 * Created by LeThanhLoi on 29/03/2017.
 */
public class tinTuc {
    public  String hinhAnh;
    public  String title;
    public  String link;
    public  String time;
    public  String diaDiem;

    public tinTuc() {
    }


    public tinTuc( String time,String title, String link,String diaDiem,String hinhAnh) {
        this.title = title;
        this.link = link;
        this.time = time;
        this.diaDiem =diaDiem;
        this.hinhAnh= hinhAnh;
        //this.time=time;

    }

    @Override
    public String toString() {
        return this.title;
    }
}
