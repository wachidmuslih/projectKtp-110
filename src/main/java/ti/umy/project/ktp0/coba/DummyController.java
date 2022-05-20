/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ti.umy.project.ktp0.coba;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author wachid
 */
@Controller
public class DummyController {
    
    DummyJpaController dummyController = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();
    
    @RequestMapping("/read")
    @ResponseBody
    public List<Dummy> getDummy(Model model)
    {
        try{
            data = dummyController.findDummyEntities();
        }
        catch (Exception e){
            e.getMessage();
        }
        return data;
    } 
    @RequestMapping("/create")
    public String createDummy(){
        return "dummy/create";
    }
    @PostMapping(value = "/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String newDummy(HttpServletRequest data, MultipartFile file ) throws ParseException, Exception{
        
        Dummy dumdata = new Dummy();
        String id = data.getParameter("id");
        int iid = Integer.parseInt(id);
        String tanggal = data.getParameter("tanggal");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        
        String blob="";
        String filename = StringUtile.cleanPath(file.getOriginalFilename());
        
        
        dumdata.setId(iid);
        dumdata.setTanggal(date);
        dumdata.setGambar(blob);
        
        dummyController.create(dumdata);
        return "dummy/create";
    }
}
