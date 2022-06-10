/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ti.umy.project.ktp0.coba;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ti.umy.project.ktp0.coba.exceptions.NonexistentEntityException;

/**
 *
 * @author wachid
 */
@Controller
public class DummyController {
    
    DummyJpaController dummyCtrl = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();
    
    @RequestMapping("/read")
//    @ResponseBody
    public List<Dummy> getDummy(Model model)
    {
        try{
            data = dummyCtrl.findDummyEntities();
        }
        catch (Exception e){
            e.getMessage();
        }
        model.addAttribute("data", data);
        return data;
    } 
    
    @RequestMapping("/create")
    public String createDummy(){
        return "dummy/create";
    }
    
    
    @PostMapping(value = "/newdatadum", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String newDummy(@RequestParam("gambar") MultipartFile file, HttpServletRequest request, HttpServletResponse response ) throws ParseException, Exception{
        
        Dummy dumdata = new Dummy();
        String id = request.getParameter("id");
        int iid = Integer.parseInt(id);
        String tanggal = request.getParameter("tanggal");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        
        
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        byte[] image = file.getBytes();
        
        
        dumdata.setId(iid);
        dumdata.setTanggal(date);
        dumdata.setGambar(image);
        
        dummyCtrl.create(dumdata);
        response.sendRedirect("/read");
        
        //dummyController.create(dumdata);
        return "dummy/create";
    }
    
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable int id, Model model) {
        Dummy data = dummyCtrl.findDummy(id);
        
        if (data.getGambar() != null) {
            byte[] gambar = data.getGambar();
            String base64Image = Base64.getEncoder().encodeToString(gambar);
            String imgLink = "data:image/jpg;base64,".concat(base64Image);
            model.addAttribute("gambar", imgLink);
        } else {
            model.addAttribute("gambar", "");
        }
        
        model.addAttribute("data", data);
        return "dummy/detailDum";
    }
    
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Dummy data = new Dummy();
        try {
            data = dummyCtrl.findDummy(id);
        } catch (Exception e) {
        }
        
        if (data.getGambar()!= null) {
            byte[] gambar = data.getGambar();
            String base64Image = Base64.getEncoder().encodeToString(gambar);
            String imgLink = "data:image/jpg;base64,".concat(base64Image);
            model.addAttribute("gambar", imgLink);
        } else {
            model.addAttribute("gambar", "");
        }
        
        model.addAttribute("data", data);
        return "dummy/editDummy";
    }
    @PostMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RedirectView update(@PathVariable String id, HttpServletRequest request, @RequestParam("foto") MultipartFile file) throws ParseException, IOException, Exception {
        Dummy data = new Dummy();
        
        String sid = request.getParameter("id");
        String tanggal = request.getParameter("tanggal");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        
        if (file.isEmpty()) {
            Dummy data2 = dummyCtrl.findDummy(Integer.parseInt(sid));
            data.setGambar(data2.getGambar());
        } else {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            byte[] gambar = file.getBytes();
            data.setGambar(gambar);
        }
        
        data.setId(Integer.parseInt(sid));
       
        data.setTanggal(date);
        
        
        dummyCtrl.edit(data);
        return new RedirectView("/read");
    }
    
    @GetMapping("/hapus/{id}")
    public RedirectView destroy(@PathVariable int id) throws NonexistentEntityException {
        dummyCtrl.destroy(id);
        return new RedirectView("/read");
    }
}
