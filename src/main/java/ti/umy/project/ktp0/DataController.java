/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ti.umy.project.ktp0;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author wachid
 */
@Controller
public class DataController {
    
    DataJpaController datactrl = new DataJpaController();
    List<Data> newdata = new ArrayList<>();
        
    @RequestMapping("/data")
    //@ResponseBody
    public String getDataKTP(Model model){
        int record = datactrl.getDataCount();
        String result="";
        try{
            newdata = datactrl.findDataEntities().subList(0, record);
        }
        catch (Exception e){
            result = e.getMessage();
        }
        model.addAttribute("goData", newdata);
        model.addAttribute("record", record);
        return "database";
    }
    
    @RequestMapping("/main")
    public String getMain(){
        return "menu";
    }
    
    @RequestMapping("/edit")
    public String doEdit(){
        return "editKtp";
    }
    
    @RequestMapping("/detail")
    public String doDetail(){
        return "detailKtp";
    }
    
    @RequestMapping("/createKtp")
    public String doCreateKtp(){
        return "createKtp";
    }
    
    @PostMapping(value="/data/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String newData(HttpServletRequest request, Model model) throws ParseException{
        Data dt = new Data();
        
        String tanggal = request.getParameter("tgllahir");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        
        
        
        return "data";
    }
}
